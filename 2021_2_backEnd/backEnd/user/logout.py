# logout.py
from flask_restx import Namespace, Resource
from flask_jwt_extended import jwt_required, get_jwt
from .. import database

Logout = Namespace('Logout')

# logout 클래스, 현재 발급되어 있는 jwt토큰을 폐기하고 블랙리스트에 등록한다.
@Logout.route('/logout')
class userLogout(Resource):
    # jwt 토큰이 header에 존재해야 접근 가능
    @jwt_required()
    # DELETE method로 url에 접근했을 때
    def delete(self):

        # 받은 request의 header에서 jwt토큰을 분리해서 DB의 블랙리스트 테이블(revoked_tokens)에 등록한다.
        jti = get_jwt()['jti']
        db = database.DBClass()
        query = '''
            insert into revoked_tokens(jti) values(%s);
        '''
        db.execute(query, (jti,))
        db.commit()

        return {'message' : 'Logout Success'}
