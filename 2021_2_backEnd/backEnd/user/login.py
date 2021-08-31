# login.py
from flask import request
from flask_restx import Namespace, Resource,fields
from flask_jwt_extended import create_access_token
import database
from flask_request_validator import *

Login = Namespace(
    name = 'Login',
    description="로그인을 위한 API"
)
LoginFields = Login.model('2-1 Login Request json model', {
    "email" : fields.String(description="your email", required=True, example="testemail@testdomain.com"),
    "password" : fields.String(description="your password", required=True, example="testpw")
    })
FailedModel = Login.model('2-2 Login Fail json model', {
    "status" : fields.String(description="Success or Failed", example="Failed"),
    "message" : fields.String(description="message", example="Cannot Login")
    })
SuccessModel = Login.model('2-3 Login Success json model', {
    "status" : fields.String(description="Success or Failed", example="Success"),
    "access token" : fields.String(description="Token", example="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTYzMDI4NzE3MiwianRpIjoiMjcwNjNjODctYWNhYS00NDJhLTk1M2UtMWM1MWQ3YmNjNTJkIiwidHlwZSI6ImFjY2VzcyIsInN1YiI6Imp1bnZlcnkyQG5hdmVyLmNvbSIsIm5iZiI6MTYzMDI4NzE3Mn0.QnyKfFmjHWNg6ShGsgRRawCzgWzoSDT3YjUzTtg2_Yg"),
    })


# userLogin 클래스, 클라이언트로부터 이메일과 비밀번호를 받아 jwt access token을 반환한다.
@Login.route('/login')
@Login.doc(params={'email' : "your email", "password": "your password"})
class userLogin(Resource):
    @Login.expect(LoginFields)
    @Login.response(201, 'Success', SuccessModel)
    @Login.response(400, 'Failed', FailedModel)
    
    @validate_params(
        Param('email', JSON, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')]),   # 이메일 형식 체크
        Param('password', JSON, str, rules=CompositeRule(Pattern(r'(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^\w\s]).*'), MinLength(8), MaxLength(20))),   # 비밀번호 형식 체크(한, 영, 숫자, 특수문자 포함)
    )
    def post(self, *args):
        """json형식으로 email, password로 회원가입"""
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