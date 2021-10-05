# decorator.py

from functools import wraps
from datetime import datetime, timezone, timedelta
import database, requests, config

# 카카오톡 access token이 유효한지 확인하고 만료되었다면 재발급
def kakaoJWT_check(func):
    def wrapper(self, *args, **kwargs):

        ClientID = config.kakaoClientId

        # 인자로 전달받은 이메일이 존재하는지 체크
        userEmail = kwargs['userEmail']
        db = database.DBClass()
        query = '''
            select * from users where email = (%s);
        '''
        data = db.executeOne(query, (userEmail,))

        if data is None :
            return {"status" : "Failed", "message" : "Email not registered"}, 400
        # 존재한다면 현재 카카오톡 access token이 유효한지 체크
        else:
            kakaoAccessTokenInfo = requests.get('https://kapi.kakao.com/v1/user/access_token_info', headers={"Authorization" : f"Bearer {data['kakaoAccessToken']}"})
            
            statusCode = kakaoAccessTokenInfo.status_code
            tokenInfoDict = kakaoAccessTokenInfo.json()

            # statusCode = 400 -> 문제 있음, 401 -> access token이 만료되었으므로 refresh token을 사용해 재발급, 200 -> 문제 없음
            if statusCode == 400 :
                return {"status" : "Failed", "message" : tokenInfoDict['msg']}, 400
            elif statusCode == 401 :
                token = data['kakaoRefreshToken']
                refreshedData = requests.post(f'https://kauth.kakao.comc/oauth/token?grant_type=refresh_token&client_id={ClientID}&refresh_token={token}').json()

                # refresh token의 유효 기간은 2달이고, 유효기간이 1달 미만으로 남을 때부터 같이 갱신 가능
                if 'refresh_token' in refreshedData:
                    query = '''
                        update users set kakaoAccessToken=(%s), kakaoRefreshedToken=(%s) where email = (%s)
                    '''
                    inputList = (refreshedData['access_token'],refreshedData['refresh_token'], userEmail)
                else:
                    query = '''
                        update users set kakaoAccessToken=(%s)where email = (%s)
                    '''
                    inputList = (refreshedData['access_token'], userEmail)
                
                db.execute(query,inputList)
                db.close()
        return func(self, *args, **kwargs)
    return wrapper