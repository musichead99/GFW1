# logout.py
from flask_restx import Namespace, Resource
from flask_jwt_extended import jwt_required, get_jwt
from .. import database

Logout = Namespace('Logout')

@Logout.route('/logout')
class userLogout(Resource):
    @jwt_required()
    def delete(self):

        jti = get_jwt()['jti']
        db = database.DBClass()
        query = '''
            insert into revoked_tokens(jti) values(%s);
        '''
        db.execute(query, (jti,))
        db.commit()

        return {'message' : 'Log Out Success'}
