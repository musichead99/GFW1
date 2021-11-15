# ranking.py

from flask import request
from flask_restx import Resource, Namespace, fields
from flask_jwt_extended.utils import get_jwt_identity, get_jwt
from flask_jwt_extended import jwt_required
from flask_request_validator import *
import database, swaggerModel, config
import datetime

Ranking = Namespace(name = "Ranking", description="랭킹정보를 가져오고 관리하는 API")
parser = Ranking.parser()
parser.add_argument('Authorization', location='headers', type=str, help='본인 인증으로 jwt 토큰이 사용된다.')
RankingSuccessModel = Ranking.inherit('Ranking Success model', swaggerModel.BaseSuccessModel,{
    "ranking" : fields.String(description="access jwt token", example=[{"user_friend_email": "test8@gmail.com","name": "손흥민","profilePhoto": "http://180.80.221.11:5000/service/images/default_profile.jpg","step_count": 80000,"rank": 1},
    {"user_friend_email": "test7@gmail.com","name": "손연재","profilePhoto": "http://180.80.221.11:5000/service/images/default_profile.jpg","step_count": 70000,"rank": 2}]),
})

@Ranking.route("/ranking")
@Ranking.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class AppFriend(Resource):
    @jwt_required()
    @Ranking.response(200, 'Success(친구 목록을 랭킹 순서대로 반환)', RankingSuccessModel)
    @Ranking.expect(parser)
    def get(self, *args, **kwargs):
        """ 사용자의 친구 목록을 랭킹(걸음수)에 따라 정렬하여 반환한다."""
        userEmail = get_jwt_identity()
        yesterday = datetime.datetime.now() - datetime.timedelta(days=1)
        
        yesterday = datetime.datetime(2021, 11, 30)
        
        yesterday= yesterday.strftime('%Y-%m-%d')
        print(yesterday)

        db = database.DBClass()
        query=f"""select table_1.user_friend_email, table_2.name, table_2.profilePhoto, table_3.step_count
            from (select user_friend_email from friends where user_email = "{userEmail}") as table_1 
            inner join (select email, name, profilePhoto from users) as table_2 
            on table_1.user_friend_email = table_2.email 
            inner join (select user_email, step_count from health_data where Date = "{yesterday}") as table_3 
            on table_2.email = table_3.user_email order by table_3.step_count desc;
        """

        if not(result := db.executeAll(query)):
            return  {"status" : "Failed", "message" : "no friends"}, 400

        for i,v in enumerate(result, 1):
            if not v['profilePhoto']:
                v['profilePhoto'] = config.baseUrl + '/service/images/default_profile.jpg'
            v['rank'] = i

        return {"status" : "success", "ranking" : result}