# __init__.py
from flask import Flask
from flask_restx import Api
from flask_jwt_extended import JWTManager, exceptions
from flask_request_validator.error_formatter import demo_error_formatter
from flask_request_validator.exceptions import InvalidRequestError 
from test.test import Test
from user.register import Register
from user.logintest import LoginTest
from user.mail import Email, mail
from user.auth import Auth
from user.kakao import Kakao
from user.naver import Naver
from service.profile import Profile
from service.kakaoFriendList import KakaoFriendList
from service.friends import Friends

import database, swaggerModel, werkzeug.exceptions, datetime

app = Flask(__name__)
app.config.update(JWT_SECRET_KEY = "backendTest") # jwt encoding을 위한 secret key, 추후 수정 필요

app.config['MAIL_SERVER'] = 'smtp.gmail.com'
app.config['MAIL_PORT'] = 465
app.config['MAIL_USERNAME'] = 'testforcapstone@gmail.com'
app.config['MAIL_PASSWORD'] = 'kklikhaaedpkyurm'
app.config['MAIL_USE_TLS'] = False
app.config['MAIL_USE_SSL'] = True
app.config['JWT_ACCESS_TOKEN_EXPIRES'] = datetime.timedelta(days=10) # access token의 유효 기간 (10일)
jwt = JWTManager(app)
mail.init_app(app)
api = Api(
    app,
    version='test',
    title='2021_2_backend API Server',
    description='2021년 2학기 캡스톤디자인을 위한 API Server입니다.',
    term_url="/",
    contact="musichead99@naver.com"
    )

# request 유효성 체크 에러 메시지 처리
@api.errorhandler(InvalidRequestError)
def data_error(e):
    ReqErr = demo_error_formatter(e)[0]

    return { "status" : "Failed", "message" : ReqErr['message']}, 400

# jwt_required 에러 메시지 처리(추후 문제생길 가능성 있음;;)
@api.errorhandler(exceptions.NoAuthorizationError)
def unauthorized_token(e):
    return {"status" : "Failed", "message" : str(e)}, 403

# jwt_required에 blocklist checking기능 추가
@jwt.token_in_blocklist_loader
def check_if_token_revoked(jwt_header, jwt_payload):
    jti = jwt_payload["jti"]
    db = database.DBClass()
    query = '''
        select * from revoked_tokens where jti=%s
    '''
    token = db.executeOne(query, (jti,))
    return token is not None

# 만료된 토큰으로 접근 시 반환할 메시지 처리
@jwt.expired_token_loader
def check_if_token_expired(jwt_header, jwt_payload):
    return {"status" : "Failed"}, 403

# 파기된 토큰으로 접근 시 반환할 메시지 처리(추후 문제생길 가능성 있음;;)
@api.errorhandler(exceptions.RevokedTokenError)
def revoked_token_response(e):
    return {"status" : "Failed", "message" : str(e)}, 400

# 잘못된 메소드로 요청시 반환할 메시지 처리
@api.errorhandler(werkzeug.exceptions.MethodNotAllowed)
def not_allowd_method(e):
    return {"status" : "Error", "message" : "The method is not allowed for the requested URL."}, 405

# namespace 등록
api.add_namespace(swaggerModel.SwaggerModel)
api.add_namespace(Test,'/test')
api.add_namespace(Register,'/user')
api.add_namespace(LoginTest,'/user')
api.add_namespace(Email,'/user')
api.add_namespace(Auth,'/user')
api.add_namespace(Kakao,'/user/kakao')
api.add_namespace(Naver,'/user/Naver')
api.add_namespace(Profile,'/service')
api.add_namespace(KakaoFriendList, '/service')
api.add_namespace(Friends, '/service')

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug="true")