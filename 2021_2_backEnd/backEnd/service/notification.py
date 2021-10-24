# notification.py
from flask import request
from flask_restx import Namespace, Resource
from flask_jwt_extended import jwt_required
from flask_request_validator import *
from pyfcm import FCMNotification
import swaggerModel, config

Notification = Namespace(name='Notification', description='한 클라이언트가 다른 클라이언트에게 푸시 알람을 보낼 수 있도록 해 주는 API')
push_service = FCMNotification(config.fcmServerKey)

@Notification.route('/Notification')
class Notification(Resource):
    @jwt_required()
    def post(self, *args):
        testMessage = {
            "title" : "hello",
            "message" : "this is test"
        }
        