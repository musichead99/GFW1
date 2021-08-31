# login.py
from flask import request
from flask_restx import Namespace, Resource 
from flask_jwt_extended import create_access_token
import database
from flask_request_validator import *

Login = Namespace('Login')

# userLogin 클래스, 클라이언트로부터 이메일과 비밀번호를 받아 jwt access token을 반환한다.
@Login.route('/login')
class userLogin(Resource):
    @validate_params(
        Param('email', JSON, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')]),   # 이메일 형식 체크
        Param('password', JSON, str, rules=CompositeRule(Pattern(r'(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^\w\s]).*'), MinLength(8), MaxLength(20))),   # 비밀번호 형식 체크(한, 영, 숫자, 특수문자 포함)
    )
    def post(self, *args):
        
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