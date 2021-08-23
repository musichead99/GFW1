#register.py
from flask import request
from flask_restx import Namespace, Resource
import database
from pymysql import err
from flask_request_validator import *

Register = Namespace('Register')

# 일반 이메일 회원가입 클래스
@Register.route('/register')
class register(Resource):
    # request 유효성 검사
    @validate_params(
        Param('email', JSON, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')]),   # 이메일 형식 체크
        Param('name', JSON, str, rules=CompositeRule(Pattern(r'[a-zA-Z가-힣]'), MinLength(1))),   # 이름 형식 체크
        Param('password', JSON, str, rules=CompositeRule(Pattern(r'(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^\w\s]).*'), MinLength(8), MaxLength(20))),   # 비밀번호 형식 체크(영, 숫자, 특수문자 포함)
    )
    # POST method로 'email', 'name', 'password' 필드를 가진 데이터를 body에 담아서 접근했을 때
    def post(self, *args):
        # json형식으로 data parsing, mysql connection을 위한 객체 생성
        data = request.json
        db = database.DBClass()

        # 중복된 email 입력 시 예외 처리
        try:
            query = '''INSERT INTO users(email, password, name)
                VALUES(%(email)s, %(password)s, %(name)s);
                '''
            db.execute(query, data)
            message = "Register Success"
            code = 201
        except err.IntegrityError:
            message = "Register Failed(Email Duplicated)"
            code = 400
        finally:
            db.commit()
        return {"message": message }, code