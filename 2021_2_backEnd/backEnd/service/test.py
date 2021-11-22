# ranking.py
# 테스트를 위한 기능들


from flask import request
from flask_restx import Resource, Namespace, fields
from flask_jwt_extended.utils import get_jwt_identity, get_jwt
from flask_jwt_extended import jwt_required
from flask_request_validator import *
import database, swaggerModel, config
import datetime
from random import randint
from service.encourage import predict, encourage



Test = Namespace(name = "test", description="테스트를 위한 기능들 API")

@Test.route("/step")
class AppFriend(Resource):
    def get(self, *args, **kwargs):
        """ 모든 유저의 30일의 걸음수 빈칸 채우는 기능(2일전까지)"""
        db = database.DBClass()
        query = """select email from users;"""
        
        email_list = [i["email"] for i in db.executeAll(query)]
        
        for email in email_list:
            for i in range(2, 32):
                day = (datetime.datetime.now() - datetime.timedelta(days=i)).strftime('%Y-%m-%d')
                query = f"""select *
                from (select email from users where email = "{email}") as table_1
                inner join (select user_email, step_count from health_data where Date = "{day}") as table_2
                on table_1.email  = table_2.user_email;"""
                if not db.executeOne(query):
                    query = f"""insert into health_data(user_email, Date, step_count) values ('{email}','{day}','{randint(1000,3000)}');"""
                    db.execute_and_commit(query)
        return {"status" : "success"}

@Test.route("/step/yesterday")
class AppFriend(Resource):
    def get(self, *args, **kwargs):
        """ 모든 유저의 어제 걸음수 빈칸 채우는 기능"""
        db = database.DBClass()
        query = """select email from users;"""
        
        email_list = [i["email"] for i in db.executeAll(query)]
        
        for email in email_list:
            day = (datetime.datetime.now() - datetime.timedelta(days=1)).strftime('%Y-%m-%d')
            query = f"""select *
                from (select email from users where email = "{email}") as table_1
                inner join (select user_email, step_count from health_data where Date = "{day}") as table_2
                on table_1.email  = table_2.user_email;"""
            if not db.executeOne(query):
                query = f"""insert into health_data(user_email, Date, step_count) values ('{email}','{day}','{randint(1000,3000)}');"""
                db.execute_and_commit(query)
        return {"status" : "success"}

@Test.route("/step/today")
class AppFriend(Resource):
    def get(self, *args, **kwargs):
        """ 모든 유저의 오늘 걸음수 빈칸 채우는 기능"""
        db = database.DBClass()
        query = """select email from users;"""
        
        email_list = [i["email"] for i in db.executeAll(query)]
        
        for email in email_list:
            day = datetime.datetime.now().strftime('%Y-%m-%d')
            query = f"""select *
                from (select email from users where email = "{email}") as table_1
                inner join (select user_email, step_count from health_data where Date = "{day}") as table_2
                on table_1.email  = table_2.user_email;"""
            if not db.executeOne(query):
                query = f"""insert into health_data(user_email, Date, step_count) values ('{email}','{day}','{randint(1000,3000)}');"""
                db.execute_and_commit(query)
        return {"status" : "success"}
       


@Test.route("/predict")
class AppFriend(Resource):
    def get(self, *args, **kwargs):
        """오늘의 걸음수를 예측하여 그 값을 DB에 저장하는 기능"""
        predict()
        return {"status" : "success"}

@Test.route("/encourage")
class AppFriend(Resource):
    def get(self, *args, **kwargs):
        """실제 걸음수와 예측 걸음 수를 비교해서 실제 걸음수가 더 많으면 칭찬메세지를 보내는 기능. 날짜 기준은 어제이다"""
        encourage()
        return {"status" : "success"}

@Test.route("/predict/<string:day>")
class AppFriend(Resource):
    def get(self, day):
        """걸음수를 예측하여 그 값을 예측하는 기능. 오늘을 기준으로 day값을 뺀 날을 기준으로 한다. day=0이면 오늘, day=1이면 어제이다."""
        predict(int(day))
        return {"status" : "success"}

@Test.route("/encourage/<string:day>")
class AppFriend(Resource):
    def get(self, day):
        """실제 걸음수와 예측 걸음 수를 비교해서 실제 걸음수가 더 많으면 칭찬메세지를 보내는 기능. 어제인 day=1이 기본값이며, 그저께는 day=2로 설정할 수 있다."""
        encourage(int(day))
        return {"status" : "success"}
        