# __ini__.py
from flask import Flask
from flask_restx import Api
from werkzeug.utils import secure_filename
from .test import test

app = Flask(__name__)
api = Api(
    app,
    version = 'test',
    title = '2021_2_Capstone_BackEnd',
    description="캡스톤 백엔드 서버",
    contact="musichead99@naver.com",
    terms_url="/"
)

api.add_namespace(test.Test,'/')

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)