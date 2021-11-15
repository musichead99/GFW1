from flask_restx import Resource, Namespace, fields
from flask_jwt_extended import jwt_required, get_jwt_identity
from flask import request
import database, swaggerModel

FcmToken = Namespace(name='FcmToken', description='푸시 알림 기능을 사용하기 위해 클라이언트의 fcm token을 저장하는 API')

FcmTokenPostRequest = FcmToken.model('fcm token post request model', {
    'token' : fields.String(description='fcm 토큰', example='{some exmaple fcm token}')
})
FcmTokenPostFailedResponse = FcmToken.inherit('fcm token post failed response model', swaggerModel.BaseFailedModel, {
    'message' : fields.String(description='오류 메시지', example='Email not registered')
})

@FcmToken.route('/fcmToken')
class AppFcmToken(Resource):
    @FcmToken.expect(FcmTokenPostRequest)
    @FcmToken.doc(params={'payload' : 'token : 유저의 fcm token, 푸시 메시지를 보낼 때 각 유저들을 구분하기 위해 사용된다.'})
    @FcmToken.response(200, 'Success', swaggerModel.BaseSuccessModel)
    @FcmToken.response(400, 'Failed(유저가 가입되어 있지 않을 경우)', FcmTokenPostFailedResponse)
    @FcmToken.response(401, 
    'Failed(jwt 토큰 관련 이슈)\nmessage : Missing Authorization Header(header에 jwt토큰이 존재하지 않을 때)\nmessage : Token has been revoked(토큰이 blocklist에 존재할 때)\nmessage : Token has expired(토큰이 만료되었을 때)',
    swaggerModel.NoAuthModel
    )
    @jwt_required()
    def post(self):
        '''클라이언트로부터 fcm token을 받아 DB에 저장하고 결과를 반환한다.'''
        data = request.json['token']
        userEmail = get_jwt_identity()
        db = database.DBClass()
        query = '''
            select * from users where email = (%s);
        '''
        if db.executeOne(query, (userEmail,)) is None:
            return {'status' : 'Failed', 'message' : 'Email not registered'}, 400

        query = '''
            update users set fcmToken=(%s) where email=(%s);
        '''
        db.execute(query,(data, userEmail))
        db.commit()
        db.close()

        return {'status' : 'Success'}
