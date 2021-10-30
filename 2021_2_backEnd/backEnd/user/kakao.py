#kakao.py
from flask_restx import Namespace, Resource, fields
from flask import request
from flask_jwt_extended import create_access_token
import requests, database, config, swaggerModel

Kakao = Namespace(name='Kakao', description="카카오 소셜 로그인을 처리하는 API")

KakaoAuthGetSuccessResponse = Kakao.inherit('4-1 kakao auth success model', swaggerModel.BaseSuccessModel, {
    "link" : fields.String(description="link", example="https://kauth.kakao.com/oauth/authorize?client_id=b7f91f6772db2d633fb992e80d06827d&redirect_uri=http://182.212.194.105:5000/user/kakao/callback&response_type=code")
    })

KakaoAuthCallbackSuccessResponse = Kakao.inherit('4-2 kakao auth callback success model', swaggerModel.BaseSuccessModel, {
    "access token" : fields.String(description="access jwt token", example="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTYzMDI4NzE3MiwianRpIjoiMjcwNjNjODctYWNhYS00NDJhLTk1M2UtMWM1MWQ3YmNjNTJkIiwidHlwZSI6ImFjY2VzcyIsInN1YiI6Imp1bnZlcnkyQG5hdmVyLmNvbSIsIm5iZiI6MTYzMDI4NzE3Mn0.QnyKfFmjHWNg6ShGsgRRawCzgWzoSDT3YjUzTtg2_Yg")
    })

KakaoAuthCallbackKeyErrorResponse = Kakao.inherit('4-3 kakao auth callback keyerror model', swaggerModel.BaseFailedModel, {
    "message" : fields.String(description="오류 메시지", example="there's no key 'access_token'")
    })

@Kakao.route('/')
@Kakao.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class KakaoAuth(Resource):
    @Kakao.response(200, 'Success', KakaoAuthGetSuccessResponse)
    def get(self):
        '''클라이언트가 접속 시 카카오 로그인 페이지 링크를 반환한다.'''
        clientID = config.kakaoClientId
        redirectUri = config.baseUrl +'/user/kakao/callback'
        kakao_oauthurl = f"https://kauth.kakao.com/oauth/authorize?client_id={clientID}&redirect_uri={redirectUri}&response_type=code"

        return {"status" : "Success", "link": kakao_oauthurl}, 200

@Kakao.route('/callback')
@Kakao.response(500, 'Failed(서버 관련 이슈)', swaggerModel.InternalServerErrorModel)
class KakaoAuthCallback(Resource):
    @Kakao.response(200, 'Success', KakaoAuthCallbackSuccessResponse)
    @Kakao.response(400, 'Failed( 카카오로부터 받은 response에 특정 key들이 존재하지 않을 경우 )', KakaoAuthCallbackKeyErrorResponse)
    def get(self):
        '''카카오로부터 인증 코드를 받아 회원가입, 로그인 처리를 하고 access용 jwt 토큰을 반환한다.'''
        db = database.DBClass()

        # 받은 인증 코드로 카카오에게 access token을 요청
        authCode = request.args.get('code')
        clientID = config.kakaoClientId
        redirectUri = config.baseUrl +'/user/kakao/callback'
        token_request = requests.post(f"https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id={clientID}&redirect_uri={redirectUri}&code={authCode}")
        token_request = token_request.json()

        # 얻은 access token으로 다시 카카오에게 회원 정보 요청
        try:
            accessToken = token_request['access_token']
            print(accessToken)
            kakaoDataRequest = requests.get(
                        "https://kapi.kakao.com/v2/user/me", headers={"Authorization" : f"Bearer {accessToken}"}
                    )
            kakaoDataRequest = kakaoDataRequest.json()

            # 회원 정보 파싱
            kakaoUserName = kakaoDataRequest['kakao_account']['profile']['nickname']
            kakaoUserEmail = kakaoDataRequest['kakao_account']['email']
            if kakaoDataRequest['kakao_account']['profile']['is_default_image'] is False: 
                kakaoUserPhoto = kakaoDataRequest['kakao_account']['profile']['profile_image_url']
            else:
                kakaoUserPhoto = None
        except KeyError as e:
            return {"status" : "Failed", "message" : "There's no key " + str(e)}, 
            
        
        # 현재 가입이 되어있는지 체크
        query = '''
            select * from users where email=(%s) and password=(%s);
        '''
        dbdata = db.executeOne(query, (kakaoUserEmail, "kakao"))

        # 가입이 되어있지 않다면 db에 insert후 토큰 발급
        if dbdata is None:
            query='''
                insert into users(email, password, name, profilePhoto) values (%s, %s, %s, %s);
            '''
            db.execute(query,(kakaoUserEmail, "kakao", kakaoUserName, kakaoUserPhoto))
            db.commit()
            db.close()
        # 가입이 되어있다면 카카오 엑세스 토큰을 갱신하고 바로 토큰 발급
        else:
            query='''
                update users set kakaoAccessToken=(%s) where email = (%s);
            '''
            db.execute(query,(accessToken, kakaoUserEmail))
            db.commit()
            db.close()
            return {"status" : "Success", "access token" : create_access_token(identity = dbdata['email'])}

        return {"status" : "Success", "access token" : create_access_token(identity = kakaoUserEmail)}
