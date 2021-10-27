# mail.py

from flask import request
from flask_restx import Namespace, Resource, fields
from flask_request_validator import *
import database

Email = Namespace(name = 'Email', description="메일 작업을 위한 API")

FailedModel = Email.model('3-2. Email Not_registed model ', {
    "status" : fields.String(description="Success or Failed", example="Failed"),
    "message" : fields.String(description="message", example="Email not registered")
    })
SuccessModel_2 = Email.model('3-4. Email is in DB json model', {
    "status" : fields.String(description="Success or Failed", example="Success"),
    "message" : fields.String(description="message", example="The email registered"),
    })

# 이메일이 현재 db에 존재하는지 확인해주는 클래스
@Email.route('/email/<string:userEmail>')
class emailAuth(Resource):
    @Email.response(200, 'Success', SuccessModel_2)
    @Email.response(400, 'Failed', FailedModel)
    def get(self, userEmail):
        db = database.DBClass()
        query = '''
                select * from users where email=%s
            '''
        data = db.executeOne(query,(userEmail,))
        db.close()
        if data is None:
            return {"status":"Failed", "message": "Email not registered"}, 400
        else:
            return {"status":"Success", "message":"The email registered"},200