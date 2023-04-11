SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE USER_TABLE;
ALTER TABLE USER_TABLE ALTER COLUMN USER_SEQ RESTART WITH 1;
SET REFERENTIAL_INTEGRITY TRUE;

insert into user_table ( user_seq  , user_id, username , email , profile_image , provider_type , role_type, password,job_field)
values ( 1, 'appleId' , 'appleUser', 'apple@apple.com' , 'appleProfileImage' , 'APPLE', 'ADMIN', 'pass','WEB_SERVER_DEVELOPER');
insert into user_table ( user_seq  , user_id, username , email, profile_image , provider_type, role_type ,job_field)
values ( 998 , 'fakeAppleId', 'fakeAppleName' , 'fakeAppleEmail', 'fakeApplePic', 'APPLE', 'ADMIN','WEB_SERVER_DEVELOPER');
insert into user_table ( user_seq, user_id, username, email, profile_image, provider_type, role_type,job_field)
values ( 997, 'fakeGoogleId', 'fakeGoogleName', 'fakeGoogleIdEmail', 'fakeGoogleIdPic', 'GOOGLE', 'ADMIN','WEB_SERVER_DEVELOPER');
insert into user_table ( user_seq, user_id, username, email, profile_image, provider_type, role_type,job_field)
values ( 996, 'testId', 'guest', 'guest@email.com', 'pic', 'GOOGLE', 'USER','WEB_SERVER_DEVELOPER');