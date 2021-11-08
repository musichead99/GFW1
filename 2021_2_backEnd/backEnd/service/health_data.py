# health_data.py

import re
from flask import request
from flask_restx import Resource, Namespace, fields
from flask_jwt_extended.utils import get_jwt_identity, get_jwt
from flask_jwt_extended import jwt_required
from flask_request_validator import *
import database, swaggerModel

import datetime

HealthData = Namespace(name = "HealthData", description="운동데이터를 가져오고 관리하는 API")
parser = HealthData.parser()
parser.add_argument('Authorization', location='headers', type=str, help='본인 인증으로 jwt 토큰이 사용된다.')
parser2 = HealthData.parser()
parser2.add_argument('stepCount', location='body', type=str, help='본인 걸음수')
HealthDataPostRequest = HealthData.model('HealthData put model', {
    "stepCount" : fields.String(description="총 걸음수", required=True, example="10000"),
    "calories" : fields.String(description="소비한 칼로리(kcal)", required=True, example="2500"),
    "distance" : fields.String(description="운동한 거리(km)", required=True, example="5"),
    "time" : fields.String(description="운동한 시간(분)", required=True, example="10"),

})
HealthDataGetFailed = HealthData.inherit('HealthData failed model ', swaggerModel.BaseFailedModel, {
    "message" : fields.String(description="에러 메시지", example="Not your friend")
})

HealthDataGetSuccess = HealthData.inherit('HealthData get success response model', swaggerModel.BaseSuccessModel,{
    "FriendHealthData" : fields.Nested(swaggerModel.BaseHealthDataModel)
})

@HealthData.route("/healthData")
@HealthData.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class AppFriend(Resource):

    @jwt_required()
    @HealthData.response(200, 'Success', swaggerModel.BaseSuccessModel)
    @HealthData.expect(parser, HealthDataPostRequest)
    @HealthData.doc(params={"payload":"stepCount : 총 걸음 수\n kcal : 소비한 칼로리(kcal)\n km : 운동한 거리(km)\n"})
    def post(self, *args, **kwargs):
        """ 업데이트 된 운동정보를 DB에 저장한다."""
        userEmail = get_jwt_identity()
        db = database.DBClass()
        now = datetime.datetime.now()
        nowDate = now.strftime('%Y-%m-%d')
        data = request.json
        query = f'''
    insert into health_data values('{userEmail}','{nowDate}','{data["stepCount"]}','{data["calories"]}','{data["distance"]}','{data["time"]}');
        '''
        db.execute_and_commit(query)
        db.close()
        print(data)
        return {"status" : "success", "message" : "Your data has benn updated"}

@HealthData.route("/healthData/<string:friendEmail>")
@HealthData.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class AppFriend(Resource):

    @jwt_required()
    @HealthData.response(200, 'Success(친구의 운동데이터를 반환)', HealthDataGetSuccess)
    @HealthData.response(400, 'Failed (친구 이메일이 잘못되었거나 친구가 아니다.)', HealthDataGetFailed)  
    def get(self, friendEmail):
        """ 사용자가 선택한 친구의 운동데이터를 반환한다."""
        userEmail = get_jwt_identity()
        db = database.DBClass()

        query = f'''
                select * from friends where user_email = "{userEmail}" and user_friend_email = "{friendEmail}";
            '''
        
        if not db.executeOne(query):
            return {"status":"Failed", "message":"Not your friend"}, 400



        query2 = f'''
                select * from health_data where user_email = "{friendEmail}" ORDER BY date limit 30;
            '''
        FriendHealthData= db.executeAll(query2)


        return {"status" : "success", "FriendHealthData" : f"{FriendHealthData}"},200
