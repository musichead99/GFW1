## select
select * from revoked_tokens;
select * from users;
select * from request_friend;
select * from friends;
select * from health_data;


## insert

# users table
insert into users(email, password, name) values ('rdd0426@gmail.com','test1234!', '김영우');
insert into users(email, password, name) values ('tjdrn5623@gmail.com','test1234!', '정성구');
insert into users(email, password, name) values ('32190153@dankook.ac.kr','test1234!', '고희아');
insert into users(email, password, name) values ('junvery2@gmail.com','test1234!', '이호준');
insert into users(email, password, name) values ('test1@gmail.com','test1234!', '김연경');
insert into users(email, password, name) values ('test2@gmail.com','test1234!', '김연아');
insert into users(email, password, name) values ('test3@gmail.com','test1234!', '류현진');
insert into users(email, password, name) values ('test4@gmail.com','test1234!', '홍길동');
insert into users(email, password, name) values ('test5@gmail.com','test1234!', '박지성');
insert into users(email, password, name) values ('test6@gmail.com','test1234!', '박찬호');
insert into users(email, password, name) values ('test7@gmail.com','test1234!', '손연재');
insert into users(email, password, name) values ('test8@gmail.com','test1234!', '손흥민');
insert into users(email, password, name) values ('test9@gmail.com','test1234!', '페이커');


#request_friend table
insert into request_friend(requester, acceptor) values ('test8@gmail.com','test1@gmail.com');
insert into request_friend(requester, acceptor) values ('test1@gmail.com','test4@gmail.com');

# friends table
insert into friends(user_email, user_friend_email) values ('test1@gmail.com', 'test9@gmail.com');
insert into friends(user_email, user_friend_email) values ('test9@gmail.com', 'test1@gmail.com');
insert into friends(user_email, user_friend_email) values ('test1@gmail.com', 'test3@gmail.com');
insert into friends(user_email, user_friend_email) values ('test3@gmail.com', 'test1@gmail.com');
insert into friends(user_email, user_friend_email) values ('test8@gmail.com', 'test9@gmail.com');
insert into friends(user_email, user_friend_email) values ('test9@gmail.com','test8@gmail.com');

# health_data table
insert into health_data values('test9@gmail.com','2021-10-01','10000','100','4000','60');
insert into health_data values('test9@gmail.com','2021-10-02','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-03','30000','300','12000','60');
insert into health_data values('test9@gmail.com','2021-10-04','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-05','10000','100','4000','60');
insert into health_data values('test9@gmail.com','2021-10-06','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-07','30000','300','12000','60');
insert into health_data values('test9@gmail.com','2021-10-08','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-09','10000','100','4000','60');
insert into health_data values('test9@gmail.com','2021-10-10','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-11','30000','300','12000','60');
insert into health_data values('test9@gmail.com','2021-10-12','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-13','10000','100','4000','60');
insert into health_data values('test9@gmail.com','2021-10-14','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-15','10000','100','4000','60');
insert into health_data values('test9@gmail.com','2021-10-16','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-17','30000','300','12000','60');
insert into health_data values('test9@gmail.com','2021-10-18','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-19','10000','100','4000','60');
insert into health_data values('test9@gmail.com','2021-10-20','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-21','30000','300','12000','60');
insert into health_data values('test9@gmail.com','2021-10-22','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-23','10000','100','4000','60');
insert into health_data values('test9@gmail.com','2021-10-24','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-25','30000','300','12000','60');
insert into health_data values('test9@gmail.com','2021-10-26','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-27','10000','100','4000','60');
insert into health_data values('test9@gmail.com','2021-10-28','20000','200','8000','120');
insert into health_data values('test9@gmail.com','2021-10-29','30000','300','12000','60');
insert into health_data values('test9@gmail.com','2021-10-30','20000','200','8000','120');

