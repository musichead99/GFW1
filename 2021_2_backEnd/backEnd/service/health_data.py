# health_data.py

from flask import request
from flask_restx import Resource, Namespace, fields
from flask_jwt_extended.utils import get_jwt_identity, get_jwt
from flask_jwt_extended import jwt_required
from flask_request_validator import *
import database, swaggerModel

HealthData = Namespace(name = "HealthData", description="운동데이터를 가져오고 관리하는 API")
parser = HealthData.parser()
parser.add_argument('Authorization', location='headers', type=str, help='본인 인증으로 jwt 토큰이 사용된다.')
parser2 = HealthData.parser()
parser2.add_argument('stepCount', location='body', type=str, help='본인 걸음수')
HealthDataPutRequest = HealthData.model('HealthData put model', {
    "stepCount" : fields.String(description="총 걸음수", required=True, example="10000")
})
HealthDataPostRequest = HealthData.model('HealthData post model', {
    "friendEmail" : fields.String(description="친구 이메일", required=True, example="[test2@naver.com, test3@naver.com]")
})
HealthDataPostFailed = HealthData.inherit('Friend failed model ', swaggerModel.BaseFailedModel, {
    "message" : fields.String(description="에러 메시지", example="Not your friend")
})

@HealthData.route("/HealthData/")
@HealthData.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class AppFriend(Resource):
    @jwt_required()
    @HealthData.response(200, 'Success', swaggerModel.BaseSuccessModel)
    @HealthData.expect(parser)
    def get(self, *args, **kwargs):
        """ 친구 목록과 랭킹을 반환한다."""
        userEmail = get_jwt_identity()
        return {"status" : "success", "ranking" : ""}

    @jwt_required()
    @HealthData.response(200, 'Success', swaggerModel.BaseSuccessModel)
    @HealthData.response(400, 'Failed (친구 이메일이 잘못되었거나 친구가 아니다.)', HealthDataPostFailed)   
    @HealthData.expect(parser, HealthDataPostRequest)
    @validate_params(
        Param('friendEmail', JSON, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')]),   # 이메일 형식 체크
    )
    def post(self, *args, **kwargs):
        """ 친구의 운동데이터를 반환한다."""
        userEmail = get_jwt_identity()
        return {"status" : "success", "message" : "Downloading has Completed"}

    @jwt_required()
    @HealthData.response(200, 'Success', swaggerModel.BaseSuccessModel)
    @HealthData.expect(parser, parser2, HealthDataPutRequest)
    def put(self, *args, **kwargs):
        """ 업데이트 된 운동정보를 DB에 저장한다."""
        userEmail = get_jwt_identity()
        return {"status" : "success", "message" : "Your data has benn updated"}
