from flask_restx import Namespace, Resource
from flask_jwt_extended import jwt_required, get_jwt
import database

LoginTest = Namespace('LoginTest')

@LoginTest.route('/logintest')
class LGTest(Resource):
    @jwt_required()
    def get(self):
        
        db = database.DBClass()
        query = '''
            select * from revoked_tokens where jti=%s;
        '''
        jti = get_jwt()['jti']

        dbdata = db.executeOne(query, (jti,))

        if dbdata is None:
            return {"message":"user_only page, hello"}
        else:
            return {"message" : "wrong token"}, 403