drop database if exists backend_test;
create database backend_test;

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
  email varchar(200) not NULL,
  password varchar(200) DEFAULT NULL,
  name varchar(200) DEFAULT NULL,
  fcmToken varchar(200) DEFAULT NULL,
  profilePhoto varchar(2000) DEFAULT NULL,
  abode varchar(20) DEFAULT NULL,
  dateOfBirth date DEFAULT NULL,
  PRIMARY KEY (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Table structure for table `request_friend`
DROP TABLE IF EXISTS request_friend;
CREATE TABLE request_friend (
  id int(11) NOT NULL AUTO_INCREMENT,
  requester varchar(200) DEFAULT NULL,
  acceptor varchar(200) DEFAULT NULL,
  PRIMARY KEY (id),
  foreign key(requester) REFERENCES users(email) on update cascade on delete cascade,
  foreign key(acceptor) REFERENCES users(email) on update cascade on delete cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Table structure for table `friends`
DROP TABLE IF EXISTS friends;
CREATE TABLE friends (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_email varchar(200) DEFAULT NULL,
  user_friend_email varchar(200) DEFAULT NULL,
  PRIMARY KEY (id),
  foreign key(user_email) REFERENCES users(email) on update cascade on delete cascade,
  foreign key(user_friend_email) REFERENCES users(email) on update cascade on delete cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Table structure for table `health_data`
DROP TABLE IF EXISTS health_data;
CREATE TABLE health_data (
  user_email varchar(200) not NULL,
  Date date not null,
  step_count int default 0,				# 단위: 걸음
  carories int default 0,				# 단위: kcal
  distance int default 0,				# 단위: m
  time int default 0,					# 단위: 분  
  
  PRIMARY KEY (user_email, Date),
  foreign key(user_email) REFERENCES users(email) on update cascade on delete cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Table structure for table `predict_health`
DROP TABLE IF EXISTS `predict_health`;
CREATE TABLE predict_health(
  user_email varchar(200) not NULL,
  Date date not null,
  step_count int default 0,				# 단위: 걸음
    
  PRIMARY KEY (user_email, Date),
  foreign key(user_email) REFERENCES users(email) on update cascade on delete cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8;