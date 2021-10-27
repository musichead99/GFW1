from flask_restx import Resource, Namespace
from flask import request
import database

FcmToken = Namespace('FcmToken')

# 미완성
@FcmToken.route('/fcmToken')
class AppFcmToken(Resource):
    def post(self):
        data = request.json['token']
        userEmail = 'rdd0426@gmail.com'
        db = database.DBClass()

        query = '''
            update users set fcmToken=(%s) where email=(%s);
        '''
        db.execute(query,(data, userEmail))
        db.commit()
        db.close()

        return {'status' : 'Success'}
