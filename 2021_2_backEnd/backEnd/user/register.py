# register.py
from flask import request
from flask_jwt_extended.utils import get_jwt_identity, get_jwt
from flask_restx import Namespace, Resource, fields
import database, swaggerModel
from pymysql import err
from flask_request_validator import *
from flask_jwt_extended import jwt_required

Register = Namespace(
    name='Register',
    description="회원가입/탈퇴, 비밀번호 변경을 처리하는 API"    
)

# API문서 작성을 위한 것들
parser = Register.parser()
parser.add_argument('Authorization', location='headers', help='사용자의 jwt토큰, 회원 인증에 사용된다.')
RegisterPostRequest = Register.model('1-1 Register Request model', {
    "email" : fields.String(description="유저의 이메일", required=True, example="testemail@testdomain.com", help='testpw123!'),
    "password" : fields.String(description="유저의 비밀번호", required=True, example="testpw123!", help='test'),
    "name" : fields.String(description="유저의 이름", required=True, example="testname", help='test')
    })
RegisterPostFailedResponse = Register.inherit('1-2 Register Failed Response model', swaggerModel.BaseFailedModel, {
    "message" : fields.String(description="오류 메시지", example="Email Duplicated")
    })
RegisterDeleteFailedResponse = Register.inherit('1-3 Delete Failed Response model', swaggerModel.BaseFailedModel,{
    "message" : fields.String(description="오류 메시지", example="The email could not be found. It doesn't seem to be registered.")
    })
RegisterPutRequest = Register.model('1-4 ChangePW Request model', {
    "email" : fields.String(description="유저의 이메일", required=True, example="testemail@testdomain.com"),
    "new_password" : fields.String(description="유저의 새로운 비밀번호", required=True, example="testpw2!"),
    "new_password_again" : fields.String(description="유저의 새로운 비밀번호", required=True, example="testpw2!")
    })
ChangeFailedModel_1 = Register.inherit('1-5 ChangePW Failed_2 Response model', swaggerModel.BaseFailedModel,{
    "message" : fields.String(description="오류 메시지", example="Wrong email")
    })
ChangeFailedModel_2 = Register.inherit('1-6 ChangePW Failed_1 Response model', swaggerModel.BaseFailedModel, {
    "message" : fields.String(description="오류 메시지", example="The two passwords entered are different")
    })

# 일반 이메일 회원가입, 회원탈퇴 클래스
@Register.route('/register')
@Register.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class register(Resource):
    @Register.expect(RegisterPostRequest)
    @Register.doc(description='test')
    @Register.response(201, 'Success', swaggerModel.BaseSuccessModel)
    @Register.response(400, 'Failed(회원가입 실패, 이미 가입되어 있는 이메일일 경우)', RegisterPostFailedResponse)
    # request 유효성 검사
    @validate_params(
        Param('email', JSON, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')]),   # 이메일 형식 체크
        Param('name', JSON, str, rules=CompositeRule(Pattern(r'[a-zA-Z가-힣]'), MinLength(1))),   # 이름 형식 체크
        Param('password', JSON, str, rules=CompositeRule(Pattern(r'(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^\w\s]).*'), MinLength(8), MaxLength(20))),   # 비밀번호 형식 체크(영, 숫자, 특수문자 포함)
    )
    # 회원 가입 API
    def post(self, *args):
        """클라이언트로부터 회원 정보를 받아 회원가입을 수행하고 결과를 반환한다."""
        # json형식으로 data parsing, mysql connection을 위한 객체 생성
        data = request.json
        db = database.DBClass()

        # 중복된 email 입력 시 예외 처리
        try:
            query = '''INSERT INTO users(email, password, name)
                VALUES(%(email)s, %(password)s, %(name)s);
                '''
            db.execute(query, data)
        except err.IntegrityError:
            return {"status": "Failed", "message" : "Email Duplicated"}, 400
        finally:
            db.commit()
            db.close()
        return {"status": "Success"}, 201

    # 회원 탈퇴 API
    @Register.expect(parser)
    @Register.response(200, 'Success(회원탈퇴 성공)', swaggerModel.BaseSuccessModel)
    @Register.response(400, 'Failed(이미 탈퇴되었거나 가입되지 않은 이메일일 경우)', RegisterDeleteFailedResponse)
    @Register.response(401, 
    'Failed(jwt 토큰 관련 이슈)\nmessage : Missing Authorization Header(header에 jwt토큰이 존재하지 않을 때)\nmessage : Token has been revoked(토큰이 blocklist에 존재할 때)\nmessage : Token has expired(토큰이 만료되었을 때)',
    swaggerModel.NoAuthModel
    )
    @jwt_required()
    def delete(self, *args):
        """클라이언트로부터 받은 jwt토큰에서 이메일을 분리하여 회원탈퇴를 수행하고 결과를 반환한다."""
        userEmail = get_jwt_identity()
        jti = get_jwt()['jti']
        db = database.DBClass()

        # API접근 시 사용된 토큰을 만료시킨다.
        query = '''
            insert into revoked_tokens(jti) values(%s);
        '''
        db.execute(query, (jti,))

        query = '''
                select email from users WHERE email=(%s)
            '''
        dbdata = db.executeOne(query,(userEmail,))
        dbdata = dbdata['email']

        if dbdata is None:
            return {"status":"Failed", "message": "The email could not be found. It doesn't seem to be registered."}, 400
        else:
            query = '''
                DELETE FROM users WHERE email=(%s);
            '''
            db.execute(query, (dbdata,))
            db.commit()
            return { "status" : "Success" }, 200
    
    # 비밀번호 변경 API
    @Register.expect(RegisterPutRequest)
    @Register.response(201, 'Success', swaggerModel.BaseSuccessModel)
    @Register.response(400, 'Failed(입력한 비밀번호가 서로 다를 경우)', ChangeFailedModel_2)
    @Register.response(400, 'Failed(입력한 email이 틀렸을 경우)', ChangeFailedModel_1)
    def put(self, *args):
        """클라이언트로부터 비밀번호를 받아서 비밀번호 변경을 수행한다."""
        data = request.json

        db = database.DBClass()
        query_list = [
            "select * from users where email = %(email)s;",
            "update users set password =%(new_password)s where email = %(email)s;"
            ]
        if request.json['new_password'] != request.json['new_password_again']:
            return {"status":"Failed", "message": "The two passwords entered are different"}, 400

        if db.executeOne(query_list[0], data):
            db.execute_and_commit(query_list[1], data)
            return {"status":"Success"},200
        else:
            return {"status":"Failed", "message": "Wrong email"}, 400
