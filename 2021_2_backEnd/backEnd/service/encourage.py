# Encourage.py

from datetime import datetime, timedelta
from flask_jwt_extended.utils import get_jwt_identity
from flask_jwt_extended import jwt_required
from flask_request_validator import *
from pyfcm import FCMNotification
import database, swaggerModel, config
import pandas as pd;
# from sklearn.metrics import mean_squared_error
# from math import sqrt
from prophet import Prophet
from random import randint


push_service = FCMNotification(config.fcmServerKey)

def mytest():
    print("test")

# day값으로 어느 날짜를 예측할지 정할 수 있음
def predict(day=0):

    # 여기서부터는 시계열 코드
    
    db = database.DBClass()
    today = (datetime.now() - timedelta(days=day)).strftime('%Y-%m-%d')
    query = """select email from users;"""
        
    email_list = [i["email"] for i in db.executeAll(query)]
    
    # 모든 유저에 대해 예측값 만듦
    for email in email_list:

        # 한 유저의 운동데이터 불러오기
        query1 = f'''
        select Date, step_count from health_data where user_email = "{email}";
        '''
        data = db.executeAll(query1,{"a":"a"})

        # 시계열 데이터 만들기
        ds = []
        y=[]    
        for i in data:
            if today <= i["Date"].strftime('%Y-%m-%d'):
                continue
            ds.append(i["Date"].strftime('%Y-%m-%d'))
            y.append(i['step_count'])

        df = pd.DataFrame({"ds":ds, "y":y})
        df["cap"] = max(y) + (max(y)-min(y))//2
        df["floor"] = 0

        # 시계열 모델만들기
        # m = Prophet()
        m = Prophet(growth='logistic')
        m.daily_seasonality=True
        m.weekly_seasonality=True
        m.yearly_seasonality=True
        m.fit(df)

        # 예측값 설정하기
        future = m.make_future_dataframe(periods=1)
        future["cap"] = max(y) + (max(y)-min(y))//2
        future["floor"] = 0
        forecast = m.predict(future)
        step = int(forecast.tail(1)["yhat"])

        
        # DB에 저장
        query2 = f'''
        insert into predict_health values('{email}','{today}','{step}');
        '''
        db.execute_and_commit(query2)

def encourage(day=1):
    today = (datetime.now() - timedelta(days=day)).strftime('%Y-%m-%d')
    db = database.DBClass()

        
    query=f"""
        select u.email, u.name, u.fcmToken, p.Date, p.step_count as predict_data, h.step_count as data
        from predict_health as p
        inner join (select * from users where fcmToken is not null) as u on u.email = p.user_email
        inner join health_data as h on h.user_email = p.user_email
        where p.Date='{today}' and p.Date =h.Date
        ;
        """
    
    for i in db.executeAll(query):
        # print(i)
        if i['predict_data'] > i['data']:
            requestMessage={"title": f"칭찬해요~ {i['name']}님", "body":f"평소보다 많이 운동하셨어요~ 시간 : {today}","profilePhoto":config.baseUrl + '/service/image/default_profile.jpg'}
            push_service.single_device_data_message(registration_id=i["fcmToken"], data_message=requestMessage, android_channel_id='test123')
            print(requestMessage)

# 모든 유저의 어제 걸음수 빈칸 채우는 기능
def yesterday():
    db = database.DBClass()
    query = """select email from users;"""
    
    email_list = [i["email"] for i in db.executeAll(query)]
    
    for email in email_list:
        day = (datetime.now() - timedelta(days=1)).strftime('%Y-%m-%d')
        query = f"""select *
            from (select email from users where email = "{email}") as table_1
            inner join (select user_email, step_count from health_data where Date = "{day}") as table_2
            on table_1.email  = table_2.user_email;"""
        if not db.executeOne(query):
            query = f"""insert into health_data(user_email, Date, step_count) values ('{email}','{day}','{randint(1000,3000)}');"""
            db.execute_and_commit(query)
