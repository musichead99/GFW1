# delete_account.py
from flask import request
from flask_restx import Namespace, Resource,fields
from pymysql import err
import database
from flask_request_validator import *


Delete = Namespace(
    name = 'Delete',
    description="계정 삭제을 위한 API"
)

DeleteFields = Delete.model('5-1 Delete Request json model', {
    "email" : fields.String(description="your email", required=True, example="testemail@testdomain.com"),
    "password" : fields.String(description="your password", required=True, example="testpw")
    })
SuccessModel = Delete.model('5-2 Delete Success json model', {
    "status" : fields.String(description="Success or Failed", example="Success"),
    "message" : fields.String(description="message", example="The email has been removed.")
    })
FailedModel = Delete.model('5-3 Delete Failed json model', {
    "status" : fields.String(description="Success or Failed", example="Failed"),
    "message" : fields.String(description="message", example="The email could not be found. It doesn't seem to be registered.")
    })


# Delete_account 클래스, 클라이언트로부터 이메일과 비밀번호를 받아 계정을 삭제한다
@Delete.route('/delete')
@Delete.doc(params={'email' : "your email", "password": "your password"})
class userDelete(Resource):
    @Delete.expect(DeleteFields)
    @Delete.response(400, 'Failed', FailedModel)
    @Delete.response(201, 'Success', SuccessModel)
    
   
    @validate_params(
        Param('email', JSON, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')]),   # 이메일 형식 체크
        Param('password', JSON, str, rules=CompositeRule(Pattern(r'(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^\w\s]).*'), MinLength(8), MaxLength(20))),   # 비밀번호 형식 체크(한, 영, 숫자, 특수문자 포함)
    )
    def post(self, *args):
        """json형식으로 email, password로 계정삭제"""
        data = request.json
        
        # db에 해당 이메일이 있는지 체크
        
        db = database.DBClass()
        query = '''
                select * from users WHERE email=%(email)s AND password=%(password)s
            '''
        data = db.executeOne(query,data)
        
        # 해당 이메일이 없다면 에러 반환
        if data is None:
            return {"status":"Failed", "message": "The email could not be found. It doesn't seem to be registered. "}, 401
        
        # 해당 이메일 삭제
        else:
            query = '''
                DELETE FROM users WHERE email=%(email)s AND password=%(password)s;
            '''
            db.executeOne(query, data)
            db.commit()
            return {"status":"Successed", "message": "The email has been removed."},200 

