# logout.py
from flask_restx import Namespace, Resource, fields
from flask_jwt_extended import jwt_required, get_jwt
import database

Logout = Namespace(
    name='Logout',
    description='로그아웃을 위한 API'
)

# API문서 작성을 위한 것들
parser = Logout.parser()
parser.add_argument('Authorization', location='headers')
SuccessModel = Logout.model('return succeed json model', {"status" : fields.String(description="Success or Failed", example="Success")})
FailedModel = Logout.model('return succeed json model', {"status" : fields.String(description="Success or Failed", example="Failed")})
NoAuthModel = Logout.inherit('return Failed json model', FailedModel, {"message" : fields.String(description="message", example="Missing Authorization Header")})
RevokedTokenModel = Logout.inherit('return Failed json model', FailedModel, {"message" : fields.String(description="message", example="Token has been revoked")})


# logout 클래스, 현재 발급되어 있는 jwt토큰을 폐기하고 블랙리스트에 등록한다.
@Logout.route('/logout')
class userLogout(Resource):
    # jwt 토큰이 header에 존재해야 접근 가능
    @jwt_required()
    @Logout.expect(parser)
    @Logout.response(200, 'Success',SuccessModel)
    @Logout.response(403, 'Failed ( header에 jwt토큰이 존재하지 않을 때 )', NoAuthModel)
    @Logout.response(400, 'Failed ( 이미 blocklist에 등록된 토큰일 때 )', RevokedTokenModel)
    # DELETE method로 url에 접근했을 때
    def delete(self):
        """header의 Authorization fields에 JWT토큰을 포함해서 요청하면 해당 토큰을 blocklist에 등록한다."""
        # 받은 request의 header에서 jwt토큰을 분리해서 DB의 블랙리스트 테이블(revoked_tokens)에 등록한다.
        jti = get_jwt()['jti']
        db = database.DBClass()
        query = '''
            insert into revoked_tokens(jti) values(%s);
        '''
        db.execute(query, (jti,))
        db.commit()

        return {"status" : "Success"}, 200
