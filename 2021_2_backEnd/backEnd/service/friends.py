# Friends.py

from flask import request
from flask_restx import Resource, Namespace
from flask_request_validator import *
import database

Friends = Namespace("Friends")

@Friends.route("/friends")
class AppFriend(Resource):
    # 친구요청 보내기
    def post(self, *args, **kwargs):
        data = request.json

        db = database.DBClass()
        query = '''
        SELECT id as id FROM request_friend where requester=%(requester)s and acceptor=%(acceptor)s
        UNION ALL 
        SELECT id as id FROM friends where user_email=%(requester)s and user_friend_email=%(acceptor)s;
        '''
        if db.executeAll(query, data):
            return {"status" : "Failed", "message" : "already requested or exist"}, 400
        
        query = '''
        insert into request_friend(requester, acceptor) values (%(requester)s, %(acceptor)s);
        '''
        db.execute_and_commit(query, data)
        return {"status" : "success", "message" : "Your request message has been delivered"}, 200
    
    # 친구요청 수락, 거절
    def put(self, *args, **kwargs):
        data = request.json
        db = database.DBClass()
        
        # 입력이 잘못 된 경우
        query = '''
        SELECT * FROM request_friend where requester=%(requester)s and acceptor=%(acceptor)s
        '''
        if not db.executeOne(query, data):
            return {"status" : "Failed", "message" : "wrong email"}, 400

        # 친구요청을 거절하는 경우
        if data["accept"].lower() in ["no", "false"]:
            query = '''
            delete FROM request_friend where requester=%(requester)s and acceptor=%(acceptor)s
            '''
            db.execute_and_commit(query, data)
            return {"status" : "success", "message" : "Friend request has been denied"}, 200

        # 친구 요청을 수락하는 경우
        query1 = '''
            delete FROM request_friend where requester=%(requester)s and acceptor=%(acceptor)s
            '''
        db.execute_and_commit(query1, data)
        
        query2 = '''
        SELECT * FROM friends 
        where user_email=%(requester)s and user_friend_email=%(acceptor)s
        or user_email=%(acceptor)s and user_friend_email=%(requester)s
        ;
        '''
       ## 이미 친구인 경우
        if db.executeAll(query2, data):   
            return {"status" : "failed", "message" : "aleardy friend"}, 400

        ## 친구 요청 수락
        query3 ='''
        insert into friends(`user_email`, `user_friend_email`) values (%s,%s);
        '''
        db.execute_and_commit(query3, (data['requester'], data['acceptor']))
        db.execute_and_commit(query3, (data['acceptor'], data['requester']))
        return {"status" : "success", "message" : "Friend request has been accepted"}, 200
    
    #친구 삭제
    def delete(self, *args, **kwargs):
        data = request.json
        db = database.DBClass()
        # 입력이 잘못 된 경우
        query = '''
        SELECT * FROM friends 
        where user_email=%(user)s and user_friend_email=%(user_friend)s
        or user_email=%(user_friend)s and user_friend_email=%(user)s;
        '''
        if not db.executeOne(query, data):
            return {"status" : "Failed", "message" : "wrong email"}, 400
        
        # 친구 삭제
        query1 = '''
        SET SQL_SAFE_UPDATES=0;
        '''
        query2 = '''
        delete from friends where user_email=%(user)s and user_friend_email=%(user_friend)s
        or user_email=%(user_friend)s and user_friend_email=%(user)s;
        '''
        db.execute_and_commit(query1)
        db.execute_and_commit(query2, data)
        return {"status" : "success", "message" : "Your friend has been deleted"}, 200


@Friends.route("/friendsList/<string:userEmail>/page=<string:page>")
class AppFriendList(Resource):
    def get(self, *args, **kwargs):
        userEmail = kwargs['userEmail']
        page = int(kwargs['page'])

        db = database.DBClass()
        query = '''
            select user_friend_email from friends where user_email = (%s);
        '''

        result = [i["user_friend_email"] for i in db.executeAll(query,(userEmail))]
        if result:
            return result[100*(page-1):100*page]
        else:
            return "No friends"

@Friends.route("/friendRequestList/<string:userEmail>/page=<string:page>")
class AppFriendRequestList(Resource):
    def get(self, *args, **kwargs):
        userEmail = kwargs['userEmail']
        page = int(kwargs['page'])

        db = database.DBClass()
        query = '''
            select requester from request_friend where acceptor = (%s);
        '''

        result = [i["requester"] for i in db.executeAll(query,(userEmail))]
        if result:
            return result[100*(page-1):100*page]
        else:
            return "No request"
