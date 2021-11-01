# notification.py
from flask import request
from flask_restx import Namespace, Resource, fields
from flask_jwt_extended import jwt_required, get_jwt_identity
from flask_request_validator import *
from pyfcm import FCMNotification
import swaggerModel, config, database

Notification = Namespace(name='Notification', description='푸시 알람 기능을 처리하는 API')
push_service = FCMNotification(config.fcmServerKey)

parser = Notification.parser()
parser.add_argument('Authorization', location='headers', type=str, help='유저의 jwt토큰, 회원 인증에 사용된다.')
NotificationPostRequest = Notification.model('notification request model', {
    "receiver" : fields.String(description="메시지를 받을 유저의 이메일", required=True, example="testemail@testdomain.com"),
    "title" : fields.String(description="메시지의 타이틀", required=True, example="test message title"),
    "body" : fields.String(description="메시지의 내용", required=True, example="test message body")
})
NotificationPostFailedResponse = Notification.inherit('notification failed response model', swaggerModel.BaseFailedModel, {
    "message" : fields.String(description="오류 메시지", example="There's no fcm token")
})

# 미완성
@Notification.route('/notification')
@Notification.response(401, 
    'Failed(jwt 토큰 관련 이슈)\nmessage : Missing Authorization Header(header에 jwt토큰이 존재하지 않을 때)\nmessage : Token has been revoked(토큰이 blocklist에 존재할 때)\nmessage : Token has expired(토큰이 만료되었을 때)',
    swaggerModel.NoAuthModel
    )
@Notification.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class AppNotification(Resource):
    @Notification.doc(params={'payload' : 'receiver : 메시지를 받을 유저의 이메일\ntitle : 메시지의 타이틀\nbody : 메시지의 내용'})
    @Notification.expect(parser, NotificationPostRequest)
    @Notification.response(200, 'Sucess', swaggerModel.BaseSuccessModel)
    @Notification.response(400, 'Failed(메시지를 받을 유저의 fcmToken이 존재하지 않을 경우)', NotificationPostFailedResponse)
    @jwt_required()
    def post(self, *args):
        '''클라이언트로부터 푸시 메시지를 받을 유저의 정보와 푸시 메시지를 받아 전송하고 결과를 반환한다.'''
        data = request.json
        userEmail = data['receiver']
        db = database.DBClass()
        query = '''
            select * from users where email=(%s);
        '''
        token = db.executeOne(query,(userEmail,))['fcmToken']

        # 메시지를 받을 유저의 fcmToken이 존재하지 않을 때
        if token is None:
            return {'status' : 'Failed', 'message' : 'There''s no fcm token'}, 400

        result = push_service.notify_single_device(registration_id=token, message_title=data['title'], message_body=data['body'], data_message=data)

        return {"status" : "Success"}, 200
