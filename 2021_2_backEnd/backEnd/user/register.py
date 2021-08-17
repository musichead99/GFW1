#register.py
from flask import request
from flask_restx import Namespace, Resource
from .. import database
from pymysql import err

Register = Namespace('Register')

@Register.route('/register')
class register(Resource):
    def post(self):
        data = request.json
        db = database.DBClass()

        # 중복된 email 입력 시 예외 처리
        try:
            query = '''INSERT INTO users(email, password, name)
                VALUES(%(email)s, %(password)s, %(name)s);
                '''
            db.execute(query, data)
            message = "Register Success"
        except err.IntegrityError:
            message = "Register Failed(Email Duplicated)"
        finally:
            db.commit()
        return {"message": message }