#register.py
from flask import request
from flask_jwt_extended.utils import get_jwt_identity, get_jwt
from flask_restx import Namespace, Resource, fields
import database
from pymysql import err
from flask_request_validator import *
from flask_jwt_extended import jwt_required

Register = Namespace(
    name='Register',
    description="회원가입/탈퇴를 위한 API"    
)


# API문서 작성을 위한 것들
parser = Register.parser()
parser.add_argument('Authorization', location='headers')
RegisterFields = Register.model('1-1 Register Request json model', {
    "email" : fields.String(description="your email", required=True, example="testemail@testdomain.com"),
    "password" : fields.String(description="your password", required=True, example="testpw"),
    "name" : fields.String(description="your name", required=True, example="testname")
    })
FailedModel = Register.model('1-2 Register Failed json model', {
    "status" : fields.String(description="Success or Failed", example="Failed"),
    "message" : fields.String(description="message", example="Email Duplicated")
    })
SuccessModel = Register.model('1-3 Register/Delete Success json model', {
    "status" : fields.String(description="Success or Failed", example="Success")
    })
DeleteFailedModel = Register.model('1-4 Delete Failed json model', {
    "status" : fields.String(description="Success or Failed", example="Failed"),
    "message" : fields.String(description="message", example="The email could not be found. It doesn't seem to be registered.")
    })
ChangePW_Fileds = Register.model('1-2 ChangePW Request json model', {
    "email" : fields.String(description="your email", required=True, example="testemail@testdomain.com"),
    "new_password" : fields.String(description="your original password", required=True, example="testpw2!"),
    "new_password_again" : fields.String(description="your new password ", required=True, example="testpw2!")
    })
ChangeSucessModel = Register.model('1-5 ChangePW Success json model', {
    "status" : fields.String(description="Success or Failed", example="Success"),
    "message" : fields.String(description="message", example="The password has changed.")
    })
ChangeFailedModel_1 = Register.model('1-7 ChangePW Failed_2 json model', {
    "status" : fields.String(description="Success or Failed", example="Success"),
    "message" : fields.String(description="message", example="Wrong email")
    })
ChangeFailedModel_2 = Register.model('1-6 ChangePW Failed_1 json model', {
    "status" : fields.String(description="Success or Failed", example="Success"),
    "message" : fields.String(description="message", example="The two passwords entered are different")
    })

# 일반 이메일 회원가입, 회원탈퇴 클래스
@Register.route('/register')
class register(Resource):
    @Register.doc(params={"email" : "your email", "password" : "your password(영어+숫자+특수문자의 8자리 이상 20자리 미만)", "name" : "your name"})
    @Register.expect(RegisterFields)
    @Register.response(201, 'Success', SuccessModel)
    @Register.response(400, 'Failed', FailedModel)
    # request 유효성 검사
    @validate_params(
        Param('email', JSON, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')]),   # 이메일 형식 체크
        Param('name', JSON, str, rules=CompositeRule(Pattern(r'[a-zA-Z가-힣]'), MinLength(1))),   # 이름 형식 체크
        Param('password', JSON, str, rules=CompositeRule(Pattern(r'(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^\w\s]).*'), MinLength(8), MaxLength(20))),   # 비밀번호 형식 체크(영, 숫자, 특수문자 포함)
    )
    # 회원 가입 API
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

    # 회원 탈퇴 API
    @Register.expect(parser)
    @jwt_required()
    @Register.response(201, 'Success', SuccessModel)
    @Register.response(400, 'Failed', DeleteFailedModel)
    def delete(self, *args):
        """Authorization header에 존재하는 jwt토큰에서 email을 분리하여 회원 탈퇴"""
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
            return {"status":"Failed", "message": "The email could not be found. It doesn't seem to be registered."}, 401
        else:
            query = '''
                DELETE FROM users WHERE email=(%s);
            '''
            db.execute(query, (dbdata,))
            db.commit()
            return { "status" : "Success" }, 201
    
    # 비밀번호 변경 API
    @Register.expect(ChangePW_Fileds)
    @Register.response(201, 'Success', ChangeSucessModel)
    @Register.response(401, 'Failed(입력한 비밀번호가 서로 다를때)', ChangeFailedModel_2)
    @Register.response(400, 'Failed(입력한 email이 틀렸을 때)', ChangeFailedModel_1)
    def put(self, *args):
        """json객체로 보내진 email, password"""
        data = request.json

        db = database.DBClass()
        query_list = [
            "select * from users where email = %(email)s;",
            "update users set password =%(new_password)s where email = %(email)s;"
            ]
        if request.json['new_password'] != request.json['new_password_again']:
            return {"status":"Failed", "message": "The two passwords entered are different"}, 401

        if db.executeOne(query_list[0], data):
            db.execute_and_commit(query_list[1], data)
            return {"status":"Success", "message":"The password has changed"},201
        else:
             return {"status":"Failed", "message": "Wrong email"}, 400
