# ReadMe

1. 프로젝트 설명
2. 기술 스택
3. Extensions
4. 설치 및 환경설정



## 프로젝트 설명

 본 프로젝트 GFW(RunningMate)는 Go For a Walk를 줄임으로 사용자에게 **건강정보(걸음수, 소모 칼로리, 이동 거리, 이동 시간)**를 제공하며 산책을 독려하도록 하는 안드로이드 모바일 앱 프로젝트이다. 애플리케이션은 Client-Server 구조로 제작되었는데, 백엔드는 Python의 micro web framework인 **Flask**를 사용하였고 추가적으로 **Flask-RESTX** extension을 기반으로 작성하였다. 

 서버의 API들은 `Swagger`를 통해 제공되는 API문서를 통해 확인할 수 있다. (http://127.0.0.1:5000. 단, 서버를 직접 실행시켜야 확인할 수 있다.)

![API문서 샷](https://user-images.githubusercontent.com/76652013/183241225-65915cc9-8e59-4aa5-9776-6973c2f17c7a.png)

`Swagger`를 이용한 API문서는 단순히 API들의 종류만을 보여 주는 것 외에도 API들의 **기능**과 특히 **어떠한 형식의 데이터를 주고받는지**를 확인할 수 있다. 자세한 내용은 `Swagger`를 통해 확인할 수 있지만 간단하게 제공되는 API들과 그 설명을 정리하면 다음과 같다.

 * Register
   - 앱 자체 회원 서비스에 가입하거나 회원 탈퇴를 위한 API

 * Email
   * 클라이언트로부터 이메일을 입력받아 해당 이메일이 DB상에 존재하는지를 확인하기 위한 API
   * 회원 가입 시 이메일 중복 여부를 체크할 때 사용됨
 * Auth
   * 자체 회원 서비스로 가입한 클라이언트가 로그인, 로그아웃을 하기 위한 API
 * Kakao
   * 카카오 소셜 로그인 서비스를 제공하는 API
 * Naver
   * 네이버 소셜 로그인 서비스를 제공하는 API
 * Profile
   * 사용자(자기자신 또는 다른 사용자)의 프로필 정보들을 불러오거나 수정하기 위한 API
   * 이메일, 이름, 프로필사진, 생년월일, 거주지, 걸음 수 등을 제공함
 * Friends
   * 친구 요청, 수락, 거절, 친구목록, 친구요청을 보낸 목록, 친구요청을 받은 목록 등을 제공하는 API
 * FcmToken
   * `FCM`을 통해 푸시 메시지를 보내기 위한 API
 * Notification
   * 공지글을 불러오는 API
 * Image
   * 서버에 저장되어 있는 이미지를 반환하는 API
   * 프로필 사진 불러오기에 사용된다
 * HealthData
   * 걸음 수, 운동 거리, 소모 칼로리, 운동 시간 등의 건강 데이터들을 갱신하거나 그 데이터들을 요청할 때 사용한다
 * Ranking
   * 사용자의 친구들의 어제의 걸음수를 기준으로 랭킹을 산출하여 반환하는 API
 * Test
   * 테스트 시 더미 데이터를 추가하기 위한 API
   * 유저들의 어제, 오늘, 2일전부터 30일 전 까지의 걸음수 데이터를 추가하는 데에 사용한다



## 기술 스택



#### Flask

<p align="center"><img src="https://user-images.githubusercontent.com/76652013/183241254-9fdd6b0d-1dea-4b19-8e04-75186b9d5d64.png"></p>

 `Flask`는 `Python`기반의 마이크로 웹 프레임워크이다. 또 다른 `Python`기반인 웹 프레임워크 `Django`와 달리 `Flask`는 웹 개발에 필요한 최소한의 도구들만을 지원한다. 대신 단 몇 줄의 코드만으로 웹 서버를 작성할 수 있고, 직접 여러 가지 extension들을 찾아보며 기호에 맞게 취사선택할 수 있었기 때문에 더 좋은 배움의 기회가 되었다.


nbsp;
#### REST API

<p align="center"><img src="https://user-images.githubusercontent.com/76652013/183241268-8844c203-6367-477e-9fb9-222fa4ce0061.png" width="438px" height="245px"></p>

 **REST API**는 2000년 로이 필딩의 박사논문에서 소개된 소프트웨어 아키텍쳐이다. 엄밀히 말하면 REST API는 기술 스택은 아니지만 최근 GraphQL등과 같은 여러 새로운 클라이언트-서버 사이의 통신 규약들이 많이 발표되고 있기 때문에 '우리 서버는 REST API 아키텍쳐를 기반으로 만들었다'라고 명시하고 싶었기 때문에 기술 스택 문단에 작성한다. REST API는 **자원**의 정의와 **자원에 대해 주소를 지정하는 방법** 방법 전반을 일컫는다. HTTP URL을 통해 자원을 명시하고, HTTP method를 통해 해당 자원에 대한 행위를 정의한다. ex) https://baseurl/student/scores (GET) -> 전체 학생의 점수를 읽어온다.


nbsp;
#### MySQL

<p align="center"><img src="https://user-images.githubusercontent.com/76652013/183241293-042f3b65-116a-4615-b3bb-f936b67199db.png" width="438px" height="245px"></p>

 **MySQL**은 가장 유명하고, 또 가장 널리 쓰이는 RDBMS(관계형 데이터베이스)이다. 프로젝트 진행 시점에서 유일하게 사용 흉내라도 낼 수 있는 DB였기 때문에 이를 사용하기로 하였다.


nbsp;
#### FCM

<p align="center"><img src="https://user-images.githubusercontent.com/76652013/183241302-96554a43-4af7-4519-9d1b-64956cdc0924.jpg" width="438px" height="235px"></p>

 **FCM**은 구글의 데이터베이스 `Firebase`를 이용한 클라우드 메시징 서비스이다. 앱 자체에서 사용자들끼리 푸시 알람을 보내고 받을 수 있도록 하기 위해서 사용하게 되었다. 일반적인 HTTP 통신은 클라이언트가 request를 보냈을 때만 서버가 response를 반환하는 방식이라 일반적인 REST API로는 푸시 알람을 구현할 수 없었는데, 마침 FCM은 안드로이드 프론트엔드 부분에서도 좋은 호환성을 보여주었기 때문에 사용하게 되었다.



## Extensions



* Flask-RESTX

  * `Flask`에서도 API 서버를 보다 쉽게 만들도록 도와주는 extension
  * [Flask와 Flask-RESTX를 사용할 때의 차이점](https://velog.io/@musichead99/1.-Flask%EC%99%80-Flask-RESTX)
  * API문서 작성을 위한 `Swagger` 연동을 지원한다.

  
nbsp;
* Flask-JWT-Extended

  * 이 프로젝트에서는 사용자 인증/인가에 JWT토큰을 사용한다.
  * 이를 위해 JWT토큰을 발급하고 토큰의 유효성 검증, 파기 등을 돕는 extension이다.

  
nbsp;
* flask-request-validator

  * 클라이언트로부터 전달받는 파라미터들에 대한 검증에 사용되는 extension
  * 정규식 기반으로 파라미터를 검증한다. ex) 이메일 형식 검증, 비밀번호 형식 검증 등

  
nbsp;
* PyMySQL

  * `Flask` 애플리케이션과 MySQL사이의 통신을 담당하는 connector를 제공한다.
  * `Flask`에서 쿼리문과 파라미터를 커넥터를 통해 전송하면 쿼리에 해당하는 데이터를 DB에서 서치해서 Python의 자료형으로 변환해준다.


nbsp;
* pyfcm
  * `Flask` 애플리케이션에서 FCM을 보다 쉽게 연동할 수 있도록 도와주는 extension



## 설치 및 환경설정



> 해당 설치 과정은 윈도우에서의 설치 과정입니다.



0. `Python`, `pip`, `MySQL`등을 설치한다.
   * 해당 프로젝트를 진행할 때 Python의 버전은 3.9.6이다.


&nbsp;
1. 프로젝트를 설치할 폴더에 github에서 소스코드를 내려받는다.

```
git clone https://github.com/musichead99/GFW1.git
```


nbsp;
2. `Python`의 가상 환경 생성 도구인 `virtualenv`를 사용해서 가상환경을 생성한다.

```
python -m venv venv
```

<p align="center"><img src="https://user-images.githubusercontent.com/76652013/183241329-80de022d-1abd-4302-b7fe-e9ba6c7ac935.png"></p>

​	이 예시에서는 프로젝트 내의 `2021_2_backEnd` 폴더에 설치했다.


nbsp;
3. 터미널을 통해서 가상환경으로 진입한다. (cmd, vscode의 터미널 등)

```
cd GFW1/2021_2_backEnd/venv/Scripts

activate
```

​	정상적으로 가상환경에 진입했다면 아래와 같이 프롬프트 맨 앞에 (가상환경 이름)이 보여진다.

<p align="center"><img src="https://user-images.githubusercontent.com/76652013/183241376-f4ce2799-558d-458a-8a06-3c2fdfab748b.png"></p>


nbsp;
4. 프로젝트 내의 `requirements.txt`파일을 이용해서 패키지들을 설치한다.
   * requirements.txt는 pip에서 파이썬 패키지들을 관리하는 방법이다.

```
pip install -r requirements.txt
```


nbsp;
5. 데이터베이스를 생성하고 거기에 `.sql`파일들을 사용해서 MySQL에 테이블과 더미 데이터들을 생성한다.

```
mysql -u root -p
비밀번호 입력

create database backend_test;
quit;
```

```
mysql -u root -p backend_test < backendDB_table.sql
mysql -u root -p backend_test < backendDB_test.sql
```

​	정상적으로 완료하였다면 다음과 같은 테이블들을 확인할 수 있다.

<p align="center"><img src="https://user-images.githubusercontent.com/76652013/183241416-e7140cfe-e930-46c3-97d2-553cb9da7a9a.png"></p>


nbsp;
6. `database.py` 파일을 수정한다.

```python
...(생략)

class DBClass():
    # mysql 계정 정보, 퀴리문 실행 후 리턴값은 dictionary형식으로 반환 
    def __init__(self):
        self.db = pymysql.connect(
            user        = 'root', 
            passwd      = 'password',
            host        = '127.0.0.1',
            db          = 'backend_test',
            charset     = 'utf8',
            cursorclass = pymysql.cursors.DictCursor
        )
        self.cursor = self.db.cursor()


...(생략)
```

​	여기서 `user`, `passwd`를 수정해야 한다. `user`는 사용할 mysql의 계정, `passwd`는 해당 계정의 비밀번호이다. 

​	만약 다른 `backend_test`가 아닌 다른 이름으로 데이터베이스를 생성했다면 `db`를 생성한 데이터베이스의 이름으로 수정해야 한다.


nbsp;
7. `config.py` 파일을 backEnd 폴더에 생성하고 아래의 내용을 작성한다.

```python
# config.py
# 여러 중요 정보들을 기록 #
import os

basePath = os.getcwd()

jwt_key= '' # jwt encoding을 위한 secret key

# 카카오 소셜 로그인을 사용하기 위해 필요한 정보들
kakaoClientId=''
baseUrl=''

# fcm을 사용하기 위한 서버 키
fcmServerKey='' 

# 네이버 소셜 로그인을 사용하기 위해 필요한 정보들
naverClientID=''
naver_client_secret=''
```

​	카카오 소셜 로그인, 네이버 소셜 로그인은 추가적으로 개발자 페이지에서 설정이 필요하다. 


nbsp;
8. 애플리케이션을 실행한다.

```
python __init__.py
```


nbsp;
9. test api를 사용해서 전날의 걸음수를 채워 준다.

<p align="center"><img src="https://user-images.githubusercontent.com/76652013/183241440-ed9669ef-a7c2-4b19-b74b-74ff30c8e856.png"></p>


​	`Swagger`문서로 들어가서 test api를 실행해준다.(try it out)

