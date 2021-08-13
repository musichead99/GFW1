# login.py
from flask import request
from flask_restx import Namespace, Resource 
from flask_jwt_extended import *
from .. import database

Login = Namespace('Login')

@Login.route('/login')
class userLogin(Resource):
    def post(self):

        data = request.json

        db = database.DBClass()
        query = '''
            SELECT * FROM users WHERE email=%(email)s AND password=%(password)s;
        '''
        dbData = db.executeOne(query, data)

        if dbData:
            return {
                "message" : "Login Success",
                "access token" : create_access_token(identity = data['email'], expires_delta = False)
                }
        else:
            return {"message" : "Login Failed"}