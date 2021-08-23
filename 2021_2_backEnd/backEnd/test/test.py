# 이미지 전송 테스트 소스

from flask import send_file
from flask_restx import Resource, Api, reqparse, Namespace
from werkzeug.datastructures import FileStorage
from werkzeug.utils import secure_filename
import io

Test = Namespace('Test') # 일반 flask의 blueprint와 같은 기능

@Test.route('/image')
class imageTest(Resource):

    # 위 url로 http 'get'요청이 왔을 때 "coding.png"반환
    def get(self):
        path = "C:/projects/GFW1/2021_2_backEnd/backEnd/images/" # 이미지 경로
        with open(path+"coding.png","rb") as f:
            data = f.read()
        data_io = io.BytesIO(data) # 바이트 스트림으로 이미지 파일 오픈

        if f is None:
            return {"status" : "nothing"}
        else:
            return send_file(data_io, mimetype="image/gif")

    #위 url로 http 'post'요청이 왔을 때
    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('image', type=FileStorage, location='files')
        args = parser.parse_args()

        image = args['image']

        if image is None:
            return {"status":"Fail"}
        else:
            image.save('C:/projects/2021_2_backEnd/backEnd/images/{0}'.format(secure_filename(image.filename)))

        return {"status":"Upload Success"}