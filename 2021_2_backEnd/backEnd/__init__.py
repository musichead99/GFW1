# __ini__.py
from flask import Flask, jsonify
from flask_restx import Api
from flask_jwt_extended import JWTManager
from .test.test import Test
from .user.register import Register
from .user.login import Login
from .user.logintest import LoginTest
from .user.logout import Logout
from flask_request_validator.error_formatter import demo_error_formatter
from flask_request_validator.exceptions import InvalidRequestError 

app = Flask(__name__)
app.config.update(JWT_SECRET_KEY = "backendTest") # jwt encoding을 위한 secret key, 추후 수정 필요
jwt = JWTManager(app)
api = Api(app)

# request 유효성 체크 에러 메시지 처리
@app.errorhandler(InvalidRequestError)
def data_error(e):
    ReqErr = demo_error_formatter(e)[0]

    return jsonify(demo_error_formatter(e)[0]), 400

# namespace 등록
api.add_namespace(Test,'/')
api.add_namespace(Register,'/user')
api.add_namespace(Login, '/user')
api.add_namespace(LoginTest,'/user')
api.add_namespace(Logout,'/user')

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)