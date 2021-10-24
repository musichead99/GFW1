# swaggerModel.py

from flask_restx import fields, Model
from flask_restx import Namespace

SwaggerModel = Namespace('SwaggerModel')

# base success or failed model
BaseSuccessModel = SwaggerModel.model('Base Success Model', {
    "status" : fields.String(description="Success or Failed", example="Success")
})

BaseFailedModel = SwaggerModel.model('Base Failed Model', {
    "status" : fields.String(description="Success or Failed", example="Failed")
})

BaseProfileModel = SwaggerModel.model('Base Profile Model', {
    "name" : fields.String(description="user name", example="testName"),
    "profilePhoto" : fields.String(description="your profilePhoto", example="(imagefile with base64 Encoded code)")
})

# errorhandler에서 정의된 에러 return model
NoAuthModel = SwaggerModel.inherit('No Auth Model', BaseFailedModel, {
    "message" : fields.String(description="message", example="Missing Authorization Header")
})

RevokedTokenModel = SwaggerModel.inherit('Revoked Token Model', BaseFailedModel, {
    "message" : fields.String(description="message", example="Token has been revoked")
})

ExpiredTokenModel = SwaggerModel.inherit('Expired Token Model', BaseFailedModel, {
    "message" : fields.String(description="message", example="Token has expired")
})

MethodNotAllowedModel = SwaggerModel.inherit('Method Not Allowed Model', BaseFailedModel, {
    "message" : fields.String(description="message", example="The method is not allowed for the requested URL.")
})

InvalidRequestModel = SwaggerModel.inherit('Invalid Request Error Model', BaseFailedModel, {
    "message" : fields.String(description="message", example="invalid JSON parameters")
})
