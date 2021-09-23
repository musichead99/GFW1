# mail.py

from flask import request
from flask_restx import Namespace, Resource, fields
from flask_request_validator import *
from flask_mail import Message, Mail
import random, string, database

mail = Mail()
Email = Namespace(name = 'Email', description="메일 작업을 위한 API")

EmailFields = Email.model('3-1. Email authentication json model', {
    "email" : fields.String(description="your email", required=True, example="testemail@testdomain.com"),
    })
FailedModel = Email.model('3-2. Email Not_registed model ', {
    "status" : fields.String(description="Success or Failed", example="Failed"),
    "message" : fields.String(description="message", example="Email not registered")
    })
SuccessModel_1 = Email.model('3-3. Email Success send code json model', {
    "status" : fields.String(description="Success or Failed", example="Success"),
    "code" : fields.String(description="code", example="HJYD6K"),
    })
SuccessModel_2 = Email.model('3-4. Email is in DB json model', {
    "status" : fields.String(description="Success or Failed", example="Success"),
    "message" : fields.String(description="message", example="The email registered"),
    })


# 이메일 클래스
@Email.route('/email')
@Email.doc(params={'email' : "your email"})
class emailAuth(Resource):
    @Email.expect(EmailFields)
    @Email.response(200, 'Success', SuccessModel_1)
    @Email.response(401, 'Failed', FailedModel)
    
    # POST method로 요청을 보냈을 시에는 인증 메일을 발송시키고 클라이언트에게 인증 코드를 리턴한다.
    @validate_params(
        Param('email', JSON, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')])   # 이메일 형식 체크
        )
    def post(self, *args):
        """json형식으로 email 작성하면 해당 메일로 인증코드 발송"""
        # 6자리 영어 대소문자 + 숫자를 혼합해 인증코드 생성
        rndNum = ''
        for i in range(6):
            rndNum = rndNum + random.choice(string.ascii_uppercase + string.digits)

        # 인증코드를 보낼 이메일 주소를 가져 옴
        userEmail = request.json['email']

        # db에 해당 이메일이 있는지 체크
        db = database.DBClass()
        query = '''
                select * from users where email=%s
            '''
        data = db.executeOne(query,(userEmail,))

        # 이메일이 등록되지 않았으면 에러 반환
        if data is None:
            return {"status":"Failed", "message": "Email not registered"}, 401
        else:
            msg = Message('test for email auth', sender = 'testforcapstone@gmail.com', recipients = [userEmail])
            msg.body = '회원가입을 위한 인증 코드입니다 : '+rndNum
            mail.send(msg)

            # 생성된 인증코드 리턴
            return {"status": "Success", "code" : rndNum},200
    


# 이메일이 현재 db에 존재하는지 확인해주는 클래스
@Email.route('/email/<string:userEmail>')
class emailAuth(Resource):
    @Email.response(200, 'Success', SuccessModel_2)
    @Email.response(401, 'Failed', FailedModel)
    def get(self, userEmail):
        db = database.DBClass()
        query = '''
                select * from users where email=%s
            '''
        data = db.executeOne(query,(userEmail,))

        if data is None:
            return {"status":"Failed", "message": "Email not registered"}, 401
        else:
            return {"status":"Success", "message":"The email registered"},200