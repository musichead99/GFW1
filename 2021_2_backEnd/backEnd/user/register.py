#register.py
from flask import request
from flask_restx import Namespace, Resource, fields
import database
from pymysql import err
from flask_request_validator import *

Register = Namespace(
    name='Register',
    description="회원가입을 위한 API"    
)

RegisterFields = Register.model('1-1 Register Request json model', {
    "email" : fields.String(description="your email", required=True, example="testemail@testdomain.com"),
    "password" : fields.String(description="your password", required=True, example="testpw"),
    "name" : fields.String(description="your name", required=True, example="testname")
    })
FailedModel = Register.model('1-2 Register Failed json model', {
    "status" : fields.String(description="Success or Failed", example="Failed"),
    "message" : fields.String(description="message", example="Email Duplicated")
    })
SuccessModel = Register.model('1-3 Register Success json model', {"status" : fields.String(description="Success or Failed", example="Success")})

# 일반 이메일 회원가입 클래스
@Register.route('/register')
@Register.doc(params={"email" : "your email", "password" : "your password(영어+숫자+특수문자의 8자리 이상 20자리 미만)", "name" : "your name"})
class register(Resource):
    @Register.expect(RegisterFields)
    @Register.response(201, 'Success', SuccessModel)
    @Register.response(400, 'Failed', FailedModel)
    # request 유효성 검사
    @validate_params(
        Param('email', JSON, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')]),   # 이메일 형식 체크
        Param('name', JSON, str, rules=CompositeRule(Pattern(r'[a-zA-Z가-힣]'), MinLength(1))),   # 이름 형식 체크
        Param('password', JSON, str, rules=CompositeRule(Pattern(r'(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^\w\s]).*'), MinLength(8), MaxLength(20))),   # 비밀번호 형식 체크(영, 숫자, 특수문자 포함)
    )
    # POST method로 'email', 'name', 'password' 필드를 가진 데이터를 body에 담아서 접근했을 때
    def post(self, *args):
        """json객체로 보내진 email, name, password로 회원가입"""
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
        return {"status": "Success" }, 201