SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE USER_TABLE;
ALTER TABLE USER_TABLE ALTER COLUMN USER_SEQ RESTART WITH 1;

TRUNCATE TABLE PROJECT;
ALTER TABLE PROJECT ALTER COLUMN PROJECT_ID RESTART WITH 1;

TRUNCATE TABLE TEAM_MEMBER;

TRUNCATE TABLE KEY_RESULT;
ALTER TABLE KEY_RESULT ALTER COLUMN KEY_RESULT_ID RESTART WITH 1;

TRUNCATE TABLE FEEDBACK;
ALTER TABLE FEEDBACK ALTER COLUMN FEEDBACK_ID RESTART WITH 1;

TRUNCATE TABLE INITIATIVE;
ALTER TABLE INITIATIVE ALTER COLUMN INITIATIVE_ID RESTART WITH 1;

TRUNCATE TABLE GUEST;
ALTER TABLE GUEST ALTER COLUMN GUEST_SEQ RESTART WITH 1;

TRUNCATE TABLE REFRESH_TOKEN;
ALTER TABLE REFRESH_TOKEN ALTER COLUMN REFRESH_TOKEN_SEQ RESTART WITH 1;


SET REFERENTIAL_INTEGRITY TRUE;

insert into guest(guest_seq, guest_uuid, guest_id, guest_name, email, provider_type, profile_image)
values (99, 'guest-ttdxe', 'guestId', 'guestName', 'guest@email', 'GOOGLE', 'profileImage');


insert into user_table (user_seq, user_id,username, email, provider_type, job_field, profile_image, deleted )values
( 111,'testId1', 'testUser1','teamMemberTest@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
( 112,'testId2', 'testUser2','projectMasterTest@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
( 113,'testId3', 'testUser3','user1@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
( 114,'testId4', 'testUser4','user2@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
( 115,'testId5', 'testUser5','user3@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
( 116,'testId6', 'testUser6','user4@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
( 117,'testId7', 'testUser7','user5@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
( 118,'testId8', 'testUser8','user6@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
( 119,'testId9', 'testUser9','user7@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
( 10,'testId10', 'testUser10','user2222@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
(11,'testId11', 'testUser2','keyResultTest@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
(12,'testId12', 'testUser2','initiativeTest@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
(13,'testId13', 'testUser2','projectMasterRetrieveTest@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
(14,'testId14', 'testUser2','projectCalendarTest@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
(15,'testId15', 'testUser2','initiativeRetrieveTest@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
(16,'testId16', 'testUser2','notificationTest@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
(17,'testId17', 'testUser2','feedbackTest@naver.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
( 18,'testId11', 'testUser10','guest@email.com','GOOGLE','PRODUCER_CP','profile_image_url' ,false),
( 19,'effefee23', 'Unknown','guest@unknown.com',null,null,null ,false),
( 20,'appleId', 'appleUser','apple@apple.com','APPLE','WEB_SERVER_DEVELOPER','profile_image_url' ,false),
( 21,'fakeAppleId', 'fakeAppleName','fakeAppleEmail','APPLE','WEB_SERVER_DEVELOPER','fakeApplePic' ,false),
( 22,'fakeGoogleId', 'fakeGoogleName','fakeGoogleIdEmail','GOOGLE','WEB_SERVER_DEVELOPER','fakeGoogleIdPic',false )

;

insert into project
( project_id, created_date, last_modified_date, created_by, last_modified_by, project_edt, project_objective, progress, project_token, project_sdt, project_type, deleted, completed) values
(  99999, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', '2022-12-14', '프로젝트 1 멀티 프로젝트', 0.0, 'mst_Kiwqnp1Nq6lb4256', '2022-12-07', 'SINGLE', false,false),
(  99998, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', '3999-12-14', '팀 맴버 테스트용 프로젝트', 100.0, 'mst_Kiwqnp1Nq6lbTNn0', '2022-12-07', 'TEAM', false,false),
(  99997, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '3999-12-14', 'key result 테스트용 프로젝트', 0.0, 'mst_Kiwqnp1Nq6lb6421', '2000-12-12', 'TEAM', false,false),
(  99996, '2000-12-12', '2022-12-12', 'testUser1', 'testUser1', '2022-12-14', 'Initiative 테스트용 프로젝트', 11.0, 'mst_K4e8a5s7d6lb6421', '2000-12-12', 'TEAM', false,false),
(  99995, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '2023-12-14', '프로젝트 조회 테스트용 프로젝트', 0.0, 'mst_K4232g4g5rgg6421', '2000-12-12', 'TEAM', false,false),
(  99994, '2001-12-12', '2022-12-12', 'testUser1', 'testUser1', '3999-12-31', '프로젝트 조회 테스트용 프로젝트(프로젝트 완료)', 100.0, 'mst_as3fg34tgg6421', '2000-12-12', 'TEAM', false,false),
(  99993, '2002-12-12', '2023-12-12', 'testUser1', 'testUser1', '2033-12-12', '프로젝트 조회 테스트용 프로젝트(프로젝트 70)', 70.0, 'mst_3gbyy554frgg6421', '2000-12-12', 'TEAM', false,false),
(  99992, '2003-12-12', '2023-12-12', 'testUser1', 'testUser1', '2003-12-12', '프로젝트 조회 테스트용 프로젝트(프로젝트 60)', 60.0, 'mst_K4g4tfdaergg6421', '2000-12-12', 'TEAM', false,false),
(  99991, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2004-12-12', '프로젝트 조회 테스트용 프로젝트(프로젝트 완료2)', 100.0, 'mst_K42334fffrgg6421', '2000-12-12', 'TEAM', false,false),
(  99990, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-10-01', '프로젝트 for 달력 1',  100.0, 'mst_111334fffrgg6421', '2022-01-12', 'TEAM', false,false),
(  99989, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-11-12', '프로젝트 for 달력 2',  100.0, 'mst_K42h45dfd4gg6421', '2022-01-01', 'TEAM', false,false),
(  99988, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-09-12', '프로젝트 for 달력 3',  100.0, 'mst_ff4g34fffrgg6421', '2022-01-12', 'TEAM', false,false),
(  99987, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-12-12', '프로젝트 for 달력 4',  100.0, 'mst_aa344tg5dfgg6421', '2022-01-12', 'TEAM', false,false),
(  99986, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for 달력 5',  100.0, 'mst_qq2f4gbfeefe1gg6421', '2022-01-12', 'TEAM', false,false),
(  88888, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 1',  100.0, 'mst_qq2f4gbffrgg6421', '2022-01-12', 'TEAM', false,false),
(  88887, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 2',  100.0, 'mst_qsdzcxbffrgg6421', '2022-01-12', 'TEAM', false,false),
(  88886, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 3',  100.0, 'mst_qq2aaaaaaagg6421', '2022-01-12', 'TEAM', false,false),
(  88885, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 4',  100.0, 'mst_qq2f4gbfffffe421', '2022-01-12', 'TEAM', false,false),
(  88884, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2322-01-12', '프로젝트 for initiative 5',  100.0, 'mst_qq2f4gaawdeg6421', '2022-01-12', 'TEAM', false,false),
(  88883, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 3',  0.0, 'mst_qq2f4gaawdeg64fef', '2022-01-12', 'SINGLE', false,false),
(  88882, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 3',  0.0, 'mst_qfeeffeaawdeg64fef', '2022-01-12', 'TEAM', false,false),
(  77777, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 3',  0.0, 'mst_qfeeffefe64fef', '2022-01-12', 'TEAM', false,false),
(  77778, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '3333-01-15', '프로젝트 for initiative 3',  0.0, 'mst_qfeeffea223fef', '3333-01-12', 'TEAM', false,false),
(  77776, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '3333-01-15', '프로젝트 for initiative 3',  0.0, 'mst_qfef3sa23fef', '2022-01-12', 'TEAM', false,true)
;


;


insert into team_member
(created_date, last_modified_date, created_by, last_modified_by, is_new, project_role_type, project_id, user_seq, deleted) values
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99998, 2, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 77778, 2, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99998, 3, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99998, 4, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99997, 3, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99997, 11, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99996, 12, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99996, 3, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99996, 17, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'MEMBER', 99995, 13, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'MEMBER', 99994, 13, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99994, 2, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99994, 3, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99993, 13, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'MEMBER', 99992, 13, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99992, 9, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99992, 8, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'MEMBER', 99991, 13, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99990, 14, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99989, 14, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99988, 14, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99987, 14, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99986, 14, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88888, 15, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88888, 9, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88888, 16, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88887, 15, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88886, 15, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88885, 15, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88885, 2, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88884, 15, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88884, 7, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88888, 17, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 88883, 3, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88882, 17, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 88882, 3, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99999, 3, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 77777, 3, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, null, 77777, 19, false),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 77776, 12, false)
;



insert into key_result
(key_result_id, created_date, last_modified_date, created_by, last_modified_by, key_result_token, name, project_id, deleted) values
(99999, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6MX15WQ3DTzQMs', 'testKeyResult 1', 99998, false),
(99998, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6f45vWQ3DTzQMs', 'testKeyResult 2', 99998, false),
(99997, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6f45vWQ3DTzQaa', 'testKeyResult 3', 99998, false),
(99996, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6f45vWQaaazQaa', 'testKeyResult 3', 99997, false),
(99995, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6faaaaaaDTzQaa', 'testKeyResult 3', 99988, false),
(99994, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6faaaaaaDTzQ22', 'testKeyResult 3', 88884, false),
(99993, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6faaaaaafw4ef3', 'testKeyResult 3', 88883, false),
(99992, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6eeeeeeaafw4ef3', 'testKeyResult 3', 88885, false),
(77777, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_w11113ㄹeaafw4ef3', 'testKeyResult 3', 77777, false),
(77778, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wfefeefefeafw4ef3', 'testKeyResult 3', 77778, false),
(77776, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wfefe3sdfafw4ef3', 'testKeyResult 3', 77776, false)
;


insert into initiative
(initiative_id, created_date, last_modified_date, created_by, last_modified_by, initiative_detail, initiative_done, initiative_edt, initiative_token, key_result_id, initiative_name, initiative_sdt, project_id, user_seq, deleted) values
(99999, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-14', 'ini_ixYjj5nODqtb3AH8', 99999, 'ini name', '2000-12-12', 99998, 3, false),
(99998, '2022-12-10', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail222', false, '2023-12-14', 'ini_ixYjj5nODfeab3AH8', 99996, 'ini name222', '2000-12-12', 99997, 11, false),
(99996, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-16', 'ini_ixYjj5aaafeab3AH8', 99996, 'ini name333', '2023-12-15', 99997, 11, false),
(99995, '2022-12-15', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-14', 'ini_ixYjjnnnafeab3AH8', 99996, 'ini name', '2000-12-12', 99997, 3, false),
(99997, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', false, '2023-12-14', 'ini_ixYjj5na3fdab3AH8', 99995, 'ini name876', '2000-12-12', 99988, 14, false),
(99994, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-14', 'ini_iefefena3fdab3AH8', 99994, 'ini name', '2000-12-12', 88884, 15, false),
(99993, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', false, '2023-12-14', 'ini_iefefawef3fdab3AH8', 99994, 'ini name', '2000-12-12', 88884, 15, false),
(99992, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', false, '2023-12-14', 'ini_iefefawef3f23fdb3AH8', 99994, 'ini name', '2000-12-12', 88884, 15, false),
(99991, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', false, '2023-12-14', 'ini_iefefawef3awef2AH8', 99994, 'ini name', '2000-12-12', 88884, 15, false),
(99990, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', false, '2023-12-14', 'ini_ieaaaawef3awef2AH8', 99994, 'ini name', '2000-12-12', 88884, 7, false),
(99989, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-15', 'ini_ieaaugywef3awef2AH8', 99994, 'ini name', '2000-12-12', 88884, 15, false),
(99988, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-15', 'ini_ieefefwef3awef2AH8', 99993, 'ini name', '2000-12-12', 88883, 3, false),
(99987, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-15', 'ini_ieefeffef3awef2AH8', 99992, 'ini name', '2000-12-12', 88885, 2, false),
(99986, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', false, '2023-12-15', 'ini_iergrffef3awf2AH8', 99992, 'ini name', '2000-12-12', 88885,2 , false),
(99985, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detaieeeee', false, '2023-12-15', 'ini_iergefefewf2AH8', 77777, 'ini name', '2000-12-12', 77777,3 , false),
(99984, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative eeeeee', false, '2023-12-15', 'ini_iergrfefeff2AH8', 77777, 'ini name', '2000-12-12', 77777,3 , false)
;


insert into feedback (feedback_id, created_date, last_modified_date, created_by, last_modified_by, feedback_token, grade_mark, initiative_id, checked, opinion, project_id, user_seq, deleted) values
(9999, '2023-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_el6q34zazzSyWx9', 'BEST_RESULT', 99999, true, '정말 좋아요!!', 99998, 3, false),
(9998, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_aaaaaagawe3rfwa3', 'BURNING_PASSION', 99999, false, '정말 좋아요!!', 99998, 2, false),
(77777, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_aaaaafef3we3rfwa3', 'BURNING_PASSION', 99984, false, '정말 좋아요!!', 77777, 19, false)
;

