# ranking.py

from flask import request
from flask_restx import Resource, Namespace, fields
from flask_jwt_extended.utils import get_jwt_identity, get_jwt
from flask_jwt_extended import jwt_required
from flask_request_validator import *
import database, swaggerModel

Ranking = Namespace(name = "Ranking", description="랭킹정보를 가져오고 관리하는 API222")
parser = Ranking.parser()
parser.add_argument('Authorization', location='headers', type=str, help='본인 인증으로 jwt 토큰이 사용된다.')
RankingSuccessModel = Ranking.inherit('Ranking Success model', swaggerModel.BaseSuccessModel,{
    "ranking" : fields.String(description="access jwt token", example="[test1@naver.com, test2@naver.com, test3@naver.com... ]"),
})

@Ranking.route("/Ranking/")
@Ranking.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class AppFriend(Resource):
    @jwt_required()
    @Ranking.response(200, 'Success(친구 목록을 랭킹 순서대로 반환)', RankingSuccessModel)
    @Ranking.expect(parser)
    def get(self, *args, **kwargs):
        """ 사용자의 친구 목록을 랭킹(걸음수)에 따라 정렬하여 반환한다."""
        userEmail = get_jwt_identity()
        return {"status" : "success", "ranking" : ""}
