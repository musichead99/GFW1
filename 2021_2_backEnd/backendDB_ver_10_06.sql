# create tables
use backend_test;

-- Table structure for table `revoked_tokens`
DROP TABLE IF EXISTS revoked_tokens;
CREATE TABLE revoked_tokens (
  id int(11) NOT NULL AUTO_INCREMENT,
  jti varchar(500) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Table structure for table `users`
DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id int(11) NOT NULL AUTO_INCREMENT,
  email varchar(200) DEFAULT NULL,
  password varchar(200) DEFAULT NULL,
  name varchar(200) DEFAULT NULL,
  kakaoAccessToken varchar(200) DEFAULT NULL,
  kakaoRefreshToken varchar(200) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY `uniqueEmail` (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into users(email, password, name) values ('rdd0426@gmail.com','test1234!', '김영우');
insert into users(email, password, name) values ('tjdrn5623@gmail.com','test1234!', '정성구');
insert into users(email, password, name) values ('32190153@dankook.ac.kr','test1234!', '고희아');
insert into users(email, password, name) values ('junvery2@gmail.com','test1234!', '이호준');
insert into users(email, password, name) values ('test1@gmail.com','test1234!', '김김김');
insert into users(email, password, name) values ('test2@gmail.com','test1234!', '박박박');
insert into users(email, password, name) values ('test3@gmail.com','test1234!', '이이이');
insert into users(email, password, name) values ('test4@gmail.com','test1234!', '홍길동');


-- Table structure for table `request_friend`
DROP TABLE IF EXISTS request_friend;
CREATE TABLE request_friend (
  id int(11) NOT NULL AUTO_INCREMENT,
  requester varchar(200) DEFAULT NULL,
  acceptor varchar(200) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into request_friend(requester, acceptor) values ('test1@gmail.com','test4@gmail.com');
insert into request_friend(requester, acceptor) values ('test2@gmail.com','test4@gmail.com');


-- Table structure for table `friends`
DROP TABLE IF EXISTS friends;
CREATE TABLE friends (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_email varchar(200) DEFAULT NULL,
  user_friend_email varchar(200) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into friends(user_email, user_friend_email) values ('rdd0426@gmail.com', 'tjdrn5623@gmail.com');
insert into friends(user_email, user_friend_email) values ('tjdrn5623@gmail.com','rdd0426@gmail.com');
