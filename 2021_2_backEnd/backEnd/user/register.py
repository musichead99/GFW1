#register.py
from flask import request
from flask_restx import Namespace, Resource
from .. import database

Register = Namespace('Register')

@Register.route('/register')
class register(Resource):
    def post(self):
        data = request.json
        db_class = database.DBClass()

        query = '''INSERT INTO users(email, password, name)
            VALUES(%(email)s, %(password)s, %(name)s);
            '''
        db_class.execute(query, data)
        db_class.commit()

        return {"message":"Success"}