# profile.py

from flask import request
from flask_restx import Resource, Namespace, fields
from flask_jwt_extended import jwt_required
from flask_request_validator import *
import database, swaggerModel

Profile = Namespace(name="Profile", description="프로필 정보를 처리하는 API")

parser = Profile.parser()
parser.add_argument('Authorization Header', location='headers', type=str, help='회원 인증용 jwt토큰')
ProfileGetSuccessModel = Profile.inherit('5-1. Profile get success model', swaggerModel.BaseSuccessModel,{
    "profile" : fields.Nested(swaggerModel.BaseProfileModel)
})
ProfileGetFailedModel = Profile.inherit('5-2. Profile get/put failed model', swaggerModel.BaseFailedModel, 
    {"message" : fields.String(description="message", example="Email not registered")}
)

# 프로필 관련 요청을 처리하는 userProfile class
@Profile.route('/profile/<string:userEmail>')
@Profile.doc(params={'userEmail' : '사용자의 이메일'})
class userProfile(Resource):
    @Profile.expect(parser)
    @Profile.response(200, 'Success(프로필 정보 요청 성공)', ProfileGetSuccessModel)
    @Profile.response(401, 'Failed(이메일이 가입되어 있지 않을)', ProfileGetFailedModel)
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
        db.close()
        if profileData is None:
            return {"status" : "Failed", "message" : "Email not registered"}, 401
        else:
            return {"status" : "Success", "profile" : profileData}
    
    @validate_params (
        Param('userEmail', PATH, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')]),
        Param('name', JSON, str, required=False, rules=CompositeRule(Pattern(r'[a-zA-Z가-힣]'), MinLength(1))),   
        Param('profilePhoto', JSON, str, required=False, rules=[MinLength(1)])
    )
    @jwt_required()
    @Profile.expect(swaggerModel.BaseProfileModel)
    @Profile.response(200, '(Success)프로필 정보 변경 성공', swaggerModel.BaseSuccessModel)
    def put(self, *args, **kwargs):
        userEmail = kwargs['userEmail']
        data = request.json
        db = database.DBClass()

        # 이메일이 현재 가입되어 있는지 체크
        query = '''
                select from users where email = %s
            '''
        if db.executeOne(query, (userEmail,)) is None:
            return {"status":"Failed", "message": "Email not registered"}, 401

        # 각 파라미터별로 DB에 갱신
        if 'name' in data :
            query = '''
                update users set name = %s where email = %s
            '''
            db.execute(query, (data['name'], userEmail))
        
        if 'profilePhoto' in data :
            pass

        db.commit()
        db.close()

        return {'status' : 'Success'}