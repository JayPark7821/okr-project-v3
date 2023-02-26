INSERT INTO user_table ( user_seq
                       , user_id
                       , username
                       , email
                       , profile_image
                       , provider_type
                       , role_type
                       , password)
VALUES ( 1
       , 'appleId'
       , 'appleUser'
       , 'apple@apple.com'
       , 'appleProfileImage'
       , 'APPLE'
       , 'ADMIN'
       , 'pass');
INSERT INTO user_table ( user_seq
                       , user_id
                       , username
                       , email
                       , profile_image
                       , provider_type
                       , role_type)
VALUES ( 2
       , 'fakeAppleId'
       , 'fakeAppleName'
       , 'fakeAppleEmail'
       , 'fakeApplePic'
       , 'APPLE'
       , 'ADMIN');
INSERT INTO user_table ( user_seq
                       , user_id
                       , username
                       , email
                       , profile_image
                       , provider_type
                       , role_type)
VALUES ( 3
       , 'fakeGoogleId'
       , 'fakeGoogleName'
       , 'fakeGoogleIdEmail'
       , 'fakeGoogleIdPic'
       , 'GOOGLE'
       , 'ADMIN');
INSERT INTO user_table ( user_seq
                       , user_id
                       , username
                       , email
                       , profile_image
                       , provider_type
                       , role_type)
VALUES ( 4
       , 'testId'
       , 'guest'
       , 'guest@email.com'
       , 'pic'
       , 'GOOGLE'
       , 'USER');