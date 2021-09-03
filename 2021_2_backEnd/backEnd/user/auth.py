from flask import request
from flask_restx import Namespace, Resource, fields
from flask_request_validator import *
from flask_jwt_extended import create_access_token, jwt_required, get_jwt
import database

Auth = Namespace(
    name = 'Auth',
    description="로그인/아웃을 위한 API"
)

parser = Auth.parser()
parser.add_argument('Authorization', location='headers')

# userAuth 클래스, 로그인과 로그아웃기능을 함
@Auth.route('/auth')
@Auth.doc(params={'email' : "your email", "password": "your password"})
class userAuth(Resource):    
    @validate_params(
        Param('email', JSON, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')]),   # 이메일 형식 체크
        Param('password', JSON, str, rules=CompositeRule(Pattern(r'(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^\w\s]).*'), MinLength(8), MaxLength(20))),   # 비밀번호 형식 체크(한, 영, 숫자, 특수문자 포함)
    )
    def post(self, *args):
        """json형식으로 전달받은 email, password로 로그인"""
        # json형식으로 data parsing, mysql connection을 위한 객체 생성
        data = request.json
        db = database.DBClass()
        query = '''
            SELECT * FROM users WHERE email=%(email)s AND password=%(password)s;
        '''
        dbData = db.executeOne(query, data)

        # users 테이블에 일치하는 이메일과 비밀번호 한 쌍이 존재한다면 jwt토큰과 success메시지 반환
        if dbData is not None:
            return {
                "status" : "Success",
                "access token" : create_access_token(identity = data['email'], expires_delta = False)
                }, 200
        # 일치하는 이메일이 없을 경우에는 failed 메시지 반환
        else:
            return {"status" : "Failed", "message" : "Cannot Login"}, 403

    @jwt_required()
    @Auth.expect(parser)
    # DELETE method로 url에 접근했을 때
    def delete(self):
        """header의 Authorization fields에 JWT토큰을 포함해서 요청하면 해당 토큰을 blocklist에 등록한다."""
        # 받은 request의 header에서 jwt토큰을 분리해서 DB의 블랙리스트 테이블(revoked_tokens)에 등록한다.
        jti = get_jwt()['jti']
        db = database.DBClass()
        query = '''
            insert into revoked_tokens(jti) values(%s);
        '''
        db.execute(query, (jti,))
        db.commit()

        return {"status" : "Success"}, 200
