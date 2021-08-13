# __ini__.py
from flask import Flask
from flask_restx import Api
from flask_jwt_extended import JWTManager
from .test.test import Test
from .user.register import Register
from .user.login import Login
from .user.logintest import LoginTest
from .user.logout import Logout

app = Flask(__name__)
app.config.update(JWT_SECRET_KEY = "backendTest")
jwt = JWTManager(app)
api = Api(app)

api.add_namespace(Test,'/')
api.add_namespace(Register,'/user')
api.add_namespace(Login, '/user')
api.add_namespace(LoginTest,'/user')
api.add_namespace(Logout,'/user')

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)