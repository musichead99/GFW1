# notification.py
from flask import request
from flask_restx import Namespace, Resource
from flask_jwt_extended import jwt_required
from flask_request_validator import *
from pyfcm import FCMNotification
import swaggerModel, config, database

Notification = Namespace(name='Notification', description='한 클라이언트가 다른 클라이언트에게 푸시 알람을 보낼 수 있도록 해 주는 API')
push_service = FCMNotification(config.fcmServerKey)

# 미완성
@Notification.route('/notification')
class AppNotification(Resource):
    def post(self, *args):
        userEmail = 'rdd0426@gmail.com'
        data = request.json
        db = database.DBClass()
        query = '''
            select * from users where email=(%s);
        '''
        token = db.executeOne(query,(userEmail,))['fcmToken']

        result = push_service.notify_single_device(registration_id=token, message_title=data['title'], message_body=data['body'], data_message=data)

        return {"status" : "Success"}, 200
