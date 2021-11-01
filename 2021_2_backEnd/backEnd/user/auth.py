# auth.py
from flask import request
from flask_restx import Namespace, Resource, fields
from flask_request_validator import *
from flask_jwt_extended import create_access_token, jwt_required, get_jwt
import database, swaggerModel

Auth = Namespace(
    name = 'Auth',
    description="로그인/아웃을 처리하는 API"
)

parser = Auth.parser()
parser.add_argument('Authorization', location='headers', type=str, help='유저의 jwt토큰, 회원 인증에 사용된다.')
AuthPostRequest = Auth.model('2-1 Login Request json model', {
    "email" : fields.String(description="유저의 이메일", required=True, example="testemail@testdomain.com"),
    "password" : fields.String(description="유저의 비밀번호", required=True, example="testpw123!")
})
AuthFailedModel = Auth.inherit('2-5. Login Failed json model', swaggerModel.BaseFailedModel, {
    "message" : fields.String(description="에러 메시지", example="Cannot Login")
})
AuthSuccessModel = Auth.inherit('2-6. Login Success json model', swaggerModel.BaseSuccessModel,{
    "access token" : fields.String(description="access jwt token", example="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTYzMDI4NzE3MiwianRpIjoiMjcwNjNjODctYWNhYS00NDJhLTk1M2UtMWM1MWQ3YmNjNTJkIiwidHlwZSI6ImFjY2VzcyIsInN1YiI6Imp1bnZlcnkyQG5hdmVyLmNvbSIsIm5iZiI6MTYzMDI4NzE3Mn0.QnyKfFmjHWNg6ShGsgRRawCzgWzoSDT3YjUzTtg2_Yg"),
})

# userAuth 클래스, 로그인과 로그아웃기능을 함
@Auth.route('/auth')
@Auth.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class userAuth(Resource): 
    @Auth.expect(AuthPostRequest)
    @Auth.doc(params={'payload' : 'email : 유저의 이메일\npassword : 유저의 비밀번호'})
    @Auth.response(200, 'Success (access용 jwt 토큰을 반환한다. 유효기간은 10일)', AuthSuccessModel)
    @Auth.response(400, 'Failed (일치하는 회원 정보가 없어 로그인에 실패했을 경우)', AuthFailedModel)   
    @validate_params(
        Param('email', JSON, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')]),   # 이메일 형식 체크
        Param('password', JSON, str, rules=CompositeRule(Pattern(r'(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^\w\s]).*'), MinLength(8), MaxLength(20))),   # 비밀번호 형식 체크(한, 영, 숫자, 특수문자 포함)
    )
    def post(self, *args):
        """클라이언트로부터 유저의 로그인 정보를 받아서 access용 jwt 토큰을 반환한다."""
        # json형식으로 data parsing, mysql connection을 위한 객체 생성
        data = request.json
        db = database.DBClass()
        query = '''
            SELECT * FROM users WHERE email=%(email)s AND password=%(password)s;
        '''
        dbData = db.executeOne(query, data)
        db.close()

        # users 테이블에 일치하는 이메일과 비밀번호 한 쌍이 존재한다면 jwt토큰과 success메시지 반환
        if dbData is not None:
            return {
                "status" : "Success",
                "access token" : create_access_token(identity = data['email'])
                }, 200
        # 일치하는 이메일이 없을 경우에는 failed 메시지 반환
        else:
            return {"status" : "Failed", "message" : "Cannot Login"}, 400

    @Auth.expect(parser)
    @Auth.response(200, 'Success', swaggerModel.BaseSuccessModel)
    @Auth.response(401, 
    'Failed(jwt 토큰 관련 이슈)\nmessage : Missing Authorization Header(header에 jwt토큰이 존재하지 않을 때)\nmessage : Token has been revoked(토큰이 blocklist에 존재할 때)\nmessage : Token has expired(토큰이 만료되었을 때)',
    swaggerModel.NoAuthModel
    )
    @jwt_required()
    # DELETE method로 url에 접근했을 때
    def delete(self):
        """클라이언트로부터 받은 jwt토큰을 blocklist에 등록해 로그아웃 처리를 한다."""
        # 받은 request의 header에서 jwt토큰을 분리해서 DB의 블랙리스트 테이블(revoked_tokens)에 등록한다.
        jti = get_jwt()['jti']
        db = database.DBClass()
        query = '''
            insert into revoked_tokens(jti) values(%s);
        '''
        db.execute(query, (jti,))
        db.commit()

        return {"status" : "Success"}, 200
