# profile.py

from flask import request
from flask_restx import Resource, Namespace, fields
from flask_jwt_extended import jwt_required, get_jwt_identity
from pymysql import NULL
import database, swaggerModel, config
from datetime import date, timedelta

import os

Profile = Namespace(name="Profile", description="프로필 정보를 처리하는 API")

parser = Profile.parser()
parser.add_argument('Authorization', location='headers', type=str, help='유저의 jwt토큰, 회원 인증에 사용된다.')
ProfileGetSuccessResponse = Profile.inherit('5-1. Profile get success response model', swaggerModel.BaseSuccessModel,{
    "profile" : fields.Nested(swaggerModel.BaseProfileGetModel)
})
ProfileGetFailedResponse = Profile.inherit('5-2. Profile get/put failed response model', swaggerModel.BaseFailedModel, 
    {"message" : fields.String(description="오류 메시지", example="Email not registered")}
)

# 프로필 관련 요청을 처리하는 userProfile class
@Profile.route('/profile', '/profile/<string:friendEmail>')
@Profile.response(401, 
    'Failed(jwt 토큰 관련 이슈)\nmessage : Missing Authorization Header(header에 jwt토큰이 존재하지 않을 때)\nmessage : Token has been revoked(토큰이 blocklist에 존재할 때)\nmessage : Token has expired(토큰이 만료되었을 때)',
    swaggerModel.NoAuthModel
    )
@Profile.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
@Profile.expect(parser)
class userProfile(Resource):
    @Profile.response(200, 'Success(프로필 정보 요청 성공)', ProfileGetSuccessResponse)
    @Profile.response(400, 'Failed(유저가 가입되어 있지 않을 경우)', ProfileGetFailedResponse)
    @jwt_required()
    def get(self, **kwargs):
        '''클라이언트로부터 받은 jwt토큰에서 이메일을 분리하여 해당하는 프로필 정보를 반환한다.'''

        yesterday = date.today() - timedelta(days=1)

        if 'friendEmail' in kwargs:
            userEmail = kwargs['friendEmail']
        else:
            userEmail = get_jwt_identity()

        db = database.DBClass()
        query = '''
            select name, dateOfBirth, abode, profilePhoto from users where email=(%s);
        '''

        profileData = db.executeOne(query,(userEmail,))

        if profileData is None:
            return {"status" : "Failed", "message" : "Email not registered"}, 400

        if profileData['profilePhoto'] is None:
            profileData['profilePhoto'] = os.path.join(config.baseUrl,'service', 'image', 'default_profile.jpg')

        if profileData['dateOfBirth'] is not None:
            profileData['dateOfBirth'] = date.isoformat(profileData['dateOfBirth'])

        query = '''
            select step_count from health_data where user_email = (%s) and Date = (%s);
        '''

        profileData['step_count'] = db.executeOne(query,(userEmail, yesterday))['step_count']
        db.close()

        return {"status" : "Success", "profile" : profileData}
    
    @jwt_required()
    @Profile.expect(swaggerModel.BaseProfilePutModel)
    @Profile.doc(params={'payload' : 'name : 유저의 이름(닉네임)\ndateOfBirth : 유저의 생년월일\n abode : 유저의 거주지(도, 광역시, 특별시 단위)\n profilePhoto : base64로 인코딩된 유저의 프로필 사진'})
    @Profile.response(200, 'Success(프로필 정보 변경 성공)', swaggerModel.BaseSuccessModel)
    @Profile.response(400, 'Failed(유저가 가입되어 있지 않을 경우)', ProfileGetFailedResponse)
    def put(self, *args):
        '''클라이언트로 받은 프로필 정보들로 현재 프로필 정보를 갱신하고 결과를 반환한다.'''
        userEmail = get_jwt_identity()
        data = request.json
        db = database.DBClass()

        # 이메일이 현재 가입되어 있는지 체크
        query = '''
                select * from users where email = (%s);
            '''
        if db.executeOne(query, (userEmail,)) is None:
            return {"status":"Failed", "message": "Email not registered"}, 400

        # 각 파라미터별로 DB에 갱신
        if 'name' in data and data['name'] is not None:
            query = '''
                update users set name = (%s) where email = (%s);
            '''
            db.execute(query, (data['name'], userEmail))
        
        if 'profilePhoto' in data and data['profilePhoto'] is not None:
            pass

        if 'dateOfBirth' in data and data['dateOfBirth'] is not None:
            query = '''
                update users set dateOfBirth = (%s) where email = (%s);
            '''
            db.execute(query, (date.fromisoformat(data['dateOfBirth']), userEmail))

        if 'abode' in data and data['abode'] is not None:
            query = '''
                update users set abode = (%s) where email = (%s);
            '''
            db.execute(query, (data['abode'], userEmail))

        db.commit()
        db.close()

        return {'status' : 'Success'}