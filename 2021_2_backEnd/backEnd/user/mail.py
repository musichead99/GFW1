from flask import request
from flask_restx import Namespace, Resource
from flask_request_validator import *
from flask_mail import Message, Mail
import random, string

mail = Mail()
Email = Namespace('Email')

# 이메일 인증 클래스
@Email.route('/email')
class emailAuth(Resource):
    @validate_params(
        Param('email', JSON, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')])   # 이메일 형식 체크
        )
    def post(self, *args):

        # 6자리 영어 대소문자 + 숫자를 혼합해 인증코드 생성
        rndNum = ''
        for i in range(6):
            rndNum = rndNum + random.choice(string.ascii_uppercase + string.digits)

        # 인증코드를 보낼 이메일 주소를 가져 옴
        userEmail = request.json['email']

        msg = Message('test for email auth', sender = 'testforcapstone@gmail.com', recipients = [userEmail])
        msg.body = '회원가입을 위한 인증 코드입니다 : '+rndNum
        mail.send(msg)

        # 생성된 인증코드 리턴
        return {"message" : rndNum}