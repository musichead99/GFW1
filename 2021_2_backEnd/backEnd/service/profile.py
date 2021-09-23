# profile.py

from flask_restx import Resource, Namespace, fields
from flask_jwt_extended import jwt_required
from flask_request_validator import *
import database, swaggerModel

Profile = Namespace(name="Profile", description="프로필 정보를 처리하는 API")

parser = Profile.parser()
parser.add_argument('Authorization Header', location='headers')
ProfileGetSuccessModel = Profile.inherit('5-1. Profile get success model', swaggerModel.BaseSuccessModel,{
    "profile" : fields.Nested(swaggerModel.BaseProfileModel)
})
ProfileGetFailedModel = Profile.inherit('5-2. Profile get failed model', swaggerModel.BaseFailedModel, 
    {"message" : fields.String(description="message", example="Email not registered")}
)

# 프로필 관련 요청을 처리하는 userProfile class
@Profile.route('/<string:userEmail>')
class userProfile(Resource):
    @Profile.expect(parser)
    @Profile.response(200, 'Success(프로필 정보 요청 성공)', ProfileGetSuccessModel)
    @Profile.response(403, 'Failed(이메일이 가입되어 있지 않을)', ProfileGetFailedModel)
    @validate_params (
        Param('userEmail', PATH, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')])
    )
    @jwt_required()
    def get(self, *args, **kwargs):
        '''path로 전달된 이메일에 해당하는 프로필 정보를 클라이언트에 반환'''
        userEmail = kwargs['userEmail']
        db = database.DBClass()
        query = '''
            select name from users where email=(%s);
        '''

        profileData = db.executeOne(query,(userEmail,))
            
        if profileData is None:
            return {"status" : "Failed", "message" : "Email not registered"}, 403
        else:
            return {"status" : "Success", "profile" : profileData}
