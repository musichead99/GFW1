#Naver.py
from flask_restx import Namespace, Resource, fields
from flask import request
from flask_jwt_extended import create_access_token
import requests, database, config, swaggerModel

Naver = Namespace(name='Naver', description="네이버 소셜 로그인을 처리하는 API")


NaverAuthGetSuccessResponse = Naver.inherit('5-1. Naver auth success model', swaggerModel.BaseSuccessModel, {
    "link" : fields.String(description="link", example="https://kauth.Naver.com/oauth/authorize?client_id=b7f91f6772db2d633fb992e80d06827d&redirect_uri=http://182.212.194.105:5000/user/Naver/callback&response_type=code")
    })

NaverAuthCallbackSuccessResponse = Naver.inherit('5-2. Naver auth callback success model', swaggerModel.BaseSuccessModel, {
    "access token" : fields.String(description="access token", example="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTYzMDI4NzE3MiwianRpIjoiMjcwNjNjODctYWNhYS00NDJhLTk1M2UtMWM1MWQ3YmNjNTJkIiwidHlwZSI6ImFjY2VzcyIsInN1YiI6Imp1bnZlcnkyQG5hdmVyLmNvbSIsIm5iZiI6MTYzMDI4NzE3Mn0.QnyKfFmjHWNg6ShGsgRRawCzgWzoSDT3YjUzTtg2_Yg")
    })

NaverAuthCallbackKeyErrorResponse = Naver.inherit('5-3 Naver auth callback keyerror model', swaggerModel.BaseSuccessModel,{
    "message" : fields.String(description="message", example="there's no key 'email'")
    })

@Naver.route('/')
@Naver.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class NaverAuth(Resource):
    @Naver.response(200, 'Success(로그인 페이지 링크 반환)', NaverAuthGetSuccessResponse)
    def get(self):
        '''클라이언트가 접속 시 네이버 로그인 페이지 링크를 반환 한다'''
        clientID = config.naverClientID
        redirectUri = config.baseUrl + '/user/Naver/callback'
        Naver_oauthurl = f"https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id={clientID}&redirect_uri={redirectUri}&response_type=code"

        return {"status" : "Success", "link": Naver_oauthurl}, 200

@Naver.route('/callback')
@Naver.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class NaverAuthCallback(Resource):
    @Naver.response(200, 'Success(접근 토큰 반환)', NaverAuthCallbackSuccessResponse)
    @Naver.response(400, 'Failed( 네이버에게 받은 response에 특정 key들이 존재하지 않을 경우 )', NaverAuthCallbackKeyErrorResponse)
    def get(self):
        '''네이버에게 인증 코드를 받아 회원가입, 로그인 처리를 하고 access용 jwt 토큰을 반환한다.'''
        db = database.DBClass()

        # 받은 인증 코드로 네이버에게 access token을 요청
        authCode = request.args.get('code')
        clientID = config.naverClientID
        client_secret= config.naver_client_secret
        redirectUri = config.baseUrl + '/user/Naver/callback'
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
            db.close()
        # # 가입이 되어있다면 바로 토큰 발급
        else:
            return {"status" : "Success", "access token" : create_access_token(identity = dbdata['email'])}

        return {"status" : "Success", "access token" : create_access_token(identity = NaverUserEmail)}


