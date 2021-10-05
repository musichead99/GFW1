# kakaoFriendList.py

from flask_restx import Resource, Namespace
from flask_jwt_extended import jwt_required
from flask_request_validator import *
import requests, database
from decorator import kakaoJWT_check

KakaoFriendList = Namespace("KakaoFiendList")

# 카카오톡 친구목록 api
@KakaoFriendList.route("/kakaoFriendList/<string:userEmail>")
class UserKakaoFriendList(Resource):
    @jwt_required()
    @kakaoJWT_check # 카카오톡의 access token을 체크하는 데코레이터
    @validate_params (
        Param('userEmail', PATH, str, rules=[Pattern(r'^[\w+-_.]+@[\w-]+\.[a-zA-Z-.]+$')])
    )
    def get(self, *args, **kwargs):
        userEmail = kwargs['userEmail']

        db = database.DBClass()
        query = '''
            select * from users where email = (%s);
        '''
        accessToken = db.executeOne(query, (userEmail,))

        userFriendList = requests.get("https://kapi.kakao.com/v1/api/talk/friends", headers={"Authorization" : f"Bearer {accessToken['kakaoAccessToken']}"}).json()
        
        db.close()
        return userFriendList
