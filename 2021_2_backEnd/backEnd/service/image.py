# image.py
from flask import send_file
from flask_restx import Namespace, Resource, fields
from flask_jwt_extended import jwt_required
import config, swaggerModel

baseimagePath = config.basePath + '\\2021_2_backEnd\\backEnd\\images'

Image = Namespace(name='Image', description='특정 이미지를 리턴하는 API')
ImageGetFailedResponse = Image.inherit('Image get request model', swaggerModel.BaseFailedModel, {
    "message" : fields.String(description="오류 메시지", example="'filename' is not exist")
})
# parser = Image.parser()
# parser.add_argument('Authorization', location='headers', type=str, help='유저의 jwt토큰, 회원 인증에 사용된다.')

@Image.route('/image/<string:fileName>')
class AppImage(Resource):
    # @Image.expect(parser)
    @Image.doc(params={'fileName' : '어떤 이미지 파일의 이름, 그 이미지 파일을 불러오기 위해 사용한다.'})
    @Image.response(200, description='fileName으로 받은 파일 이름으로 이미지 반환')
    @Image.response(400, description='Failed(인자로 받은 파일이름을 가진 이미지 파일이 존재하지 않을 경우)', model=ImageGetFailedResponse)
    @Image.produces(['image/png', 'application/json'])
    @Image.response(401, 
    'Failed(jwt 토큰 관련 이슈)\nmessage : Missing Authorization Header(header에 jwt토큰이 존재하지 않을 때)\nmessage : Token has been revoked(토큰이 blocklist에 존재할 때)\nmessage : Token has expired(토큰이 만료되었을 때)',
    swaggerModel.NoAuthModel
    )
    # @jwt_required()
    def get(self, *args, **kwargs):
        '''특정 이미지 파일의 이름을 path에 인자로 받아 그 이미지를 반환한다.'''
        fname = kwargs['fileName']

        imagePath = baseimagePath + '\\' + fname

        try:
            return send_file(imagePath, mimetype='image/gif')
        except FileNotFoundError:
            return {"status" : "Failed", "message" : f"'{fname}' is not exist"}, 400
