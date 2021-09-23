#Naver.py
import re
from flask_restx import Namespace, Resource, fields
from flask import request
from flask_jwt_extended import create_access_token
import requests, database

Naver = Namespace(name='Naver', description="네이버 소셜 로그인을 위한 API")

NaverAuthSuccessModel = Naver.model('5-1. Naver auth success model', {
    "status" : fields.String(description="Success or Failed", example="Success"),
    "link" : fields.String(description="link", example="https://kauth.Naver.com/oauth/authorize?client_id=b7f91f6772db2d633fb992e80d06827d&redirect_uri=http://182.212.194.105:5000/user/Naver/callback&response_type=code")
    })

NaverAuthCallbackSuccessModel = Naver.model('5-2. Naver auth callback success model', {
    "status" : fields.String(description="Success or Failed", example="Success"),
    "access token" : fields.String(description="access token", example="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTYzMDI4NzE3MiwianRpIjoiMjcwNjNjODctYWNhYS00NDJhLTk1M2UtMWM1MWQ3YmNjNTJkIiwidHlwZSI6ImFjY2VzcyIsInN1YiI6Imp1bnZlcnkyQG5hdmVyLmNvbSIsIm5iZiI6MTYzMDI4NzE3Mn0.QnyKfFmjHWNg6ShGsgRRawCzgWzoSDT3YjUzTtg2_Yg")
    })

NaverAuthCallbackKeyErrorModel = Naver.model('5-3 Naver auth callback keyerror model', {
    "status" : fields.String(description="Success or Failed", example="Failed"),
    "message" : fields.String(description="message", example="there's no key 'email'")
    })

@Naver.route('/')
class NaverAuth(Resource):
    @Naver.response(200, 'Success', NaverAuthSuccessModel)
    def get(self):
        '''해당 라우트로 클라이언트가 접속 시 네이버 로그인 페이지를 json객체에 담아 반환'''
        clientID = 'f_nWrpPQdHxaUSGVuQZY'
        redirectUri = 'http://172.23.14.215:5000/user/Naver/callback'
        Naver_oauthurl = f"https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id={clientID}&redirect_uri={redirectUri}&response_type=code"

        return {"status" : "Success", "link": Naver_oauthurl}, 200

@Naver.route('/callback')
class NaverAuthCallback(Resource):
    @Naver.response(200, 'Success', NaverAuthCallbackSuccessModel)
    @Naver.response(400, 'Failed', NaverAuthCallbackKeyErrorModel)
    def get(self):
        '''네이버 로그인 후 인증 코드를 받아 회원가입, 로그인 처리를 하는 콜백 함수'''
        db = database.DBClass()

        # 받은 인증 코드로 네이버에게 access token을 요청
        authCode = request.args.get('code')
        clientID = 'f_nWrpPQdHxaUSGVuQZY'
        client_secret= 'fXdLG082wS'
        redirectUri = 'http://172.23.14.215:5000/user/Naver/callback'
        token_request = requests.post(f"https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id={clientID}&client_secret={client_secret}&redirect_uri={redirectUri}&code={authCode}&state={authCode}")
        token_request = token_request.json()

        # 얻은 access token으로 다시 네이버에게 회원 정보 요청
        try:
            accessToken = token_request['access_token']
            header = "Bearer " + accessToken
            url = "https://openapi.naver.com/v1/nid/me"
            NaverDataRequest = requests.get(
                        url, headers={"Authorization" : header}
                    )
            NaverDataRequest = NaverDataRequest.json()
            

        #     # 회원 정보 파싱
            NaverUserName = NaverDataRequest['response']['name']
            NaverUserEmail = NaverDataRequest['response']['email']
        except KeyError as e:
            return {"status" : "Failed", "message" : "There's no key " + str(e)}, 
            
        # # 현재 가입이 되어있는지 체크
        query = '''
            select * from users where email=(%s) and password=(%s);
        '''
        dbdata = db.executeOne(query, (NaverUserEmail, "Naver"))

        # # 가입이 되어있지 않다면 db에 insert후 토큰 발급
        if dbdata is None:
            query='''
                insert into users(email, password, name) values (%s, %s, %s);
            '''
            db.execute(query,(NaverUserEmail, "Naver", NaverUserName))
            db.commit()
        # # 가입이 되어있다면 바로 토큰 발급
        else:
            return {"status" : "Success", "access token" : create_access_token(identity = dbdata['email'])}

        return {"status" : "Success", "access token" : create_access_token(identity = NaverUserEmail)}


