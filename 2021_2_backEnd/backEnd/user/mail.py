# mail.py

from flask import request
from flask_restx import Namespace, Resource, fields
from flask_request_validator import *
import database, swaggerModel

Email = Namespace(name = 'Email', description="사용자 계정 메일을 검색하는 API")

FailedResponse = Email.inherit('3-2. Email Not_registed model ', swaggerModel.BaseFailedModel, {
    
    "message" : fields.String(description="message", example="Email not registered")
    })
SuccessResponse = Email.inherit('3-4. Email is in DB json model', swaggerModel.BaseSuccessModel, {
    "message" : fields.String(description="message", example="The email registered"),
    })

# 이메일이 현재 db에 존재하는지 확인해주는 클래스
@Email.route('/email/<string:userEmail>')
@Email.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class emailAuth(Resource):
    @Email.doc(params={'userEmail' : '유저의 이메일'})
    @Email.response(200, 'Success', SuccessResponse)
    @Email.response(400, 'Failed( 등록되지 않은 이메일 )', FailedResponse)
    def get(self, userEmail):
        """이메일이 현재 DB에 존재하는지 확인한다."""
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