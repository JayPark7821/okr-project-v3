insert into user_table (user_seq, user_id,username, password, email, provider_type, role_type, job_field, profile_image )values
( 1,'testId1', 'testUser1','password','teamMemberTest@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
( 2,'testId2', 'testUser2','password','projectMasterTest@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
(11,'testId11', 'testUser2','password','keyResultTest@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
(12,'testId12', 'testUser2','password','initiativeTest@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
(13,'testId13', 'testUser2','password','projectMasterRetrieveTest@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
(14,'testId14', 'testUser2','password','projectCalendarTest@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
(15,'testId15', 'testUser2','password','initiativeRetrieveTest@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
(16,'testId16', 'testUser2','password','notificationTest@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
(17,'testId17', 'testUser2','password','feedbackTest@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
( 3,'testId3', 'testUser3','password','user1@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
( 4,'testId4', 'testUser4','password','user2@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
( 5,'testId5', 'testUser5','password','user3@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
( 6,'testId6', 'testUser6','password','user4@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
( 7,'testId7', 'testUser7','password','user5@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
( 8,'testId8', 'testUser8','password','user6@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
( 9,'testId9', 'testUser9','password','user7@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
( 10,'testId10', 'testUser10','password','user2222@naver.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
( 18,'testId11', 'testUser10','password','guest@email.com','GOOGLE','USER','PRODUCER_CP','profile_image_url' ),
( 19,'effefee23', 'Unknown','password','guest@unknown.com',null,null,null,null )
;

insert into project
( project_id, created_date, last_modified_date, created_by, last_modified_by, project_edt, project_objective, progress, project_token, project_sdt, project_type, deleted) values
(  99999, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', '2022-12-14', '프로젝트 1 멀티 프로젝트', 0.0, 'mst_Kiwqnp1Nq6lb4256', '2022-12-07', 'SINGLE', false),
(  99998, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', '3999-12-14', '팀 맴버 테스트용 프로젝트', 100.0, 'mst_Kiwqnp1Nq6lbTNn0', '2022-12-07', 'TEAM', false),
(  99997, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '3999-12-14', 'key result 테스트용 프로젝트', 0.0, 'mst_Kiwqnp1Nq6lb6421', '2000-12-12', 'TEAM', false),
(  99996, '2000-12-12', '2022-12-12', 'testUser1', 'testUser1', '2022-12-14', 'Initiative 테스트용 프로젝트', 11.0, 'mst_K4e8a5s7d6lb6421', '2000-12-12', 'TEAM', false),
(  99995, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '2023-12-14', '프로젝트 조회 테스트용 프로젝트', 0.0, 'mst_K4232g4g5rgg6421', '2000-12-12', 'TEAM', false),
(  99994, '2001-12-12', '2022-12-12', 'testUser1', 'testUser1', '3999-12-31', '프로젝트 조회 테스트용 프로젝트(프로젝트 완료)', 100.0, 'mst_as3fg34tgg6421', '2000-12-12', 'TEAM', false),
(  99993, '2002-12-12', '2023-12-12', 'testUser1', 'testUser1', '2033-12-12', '프로젝트 조회 테스트용 프로젝트(프로젝트 70)', 70.0, 'mst_3gbyy554frgg6421', '2000-12-12', 'TEAM', false),
(  99992, '2003-12-12', '2023-12-12', 'testUser1', 'testUser1', '2003-12-12', '프로젝트 조회 테스트용 프로젝트(프로젝트 60)', 60.0, 'mst_K4g4tfdaergg6421', '2000-12-12', 'TEAM', false),
(  99991, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2004-12-12', '프로젝트 조회 테스트용 프로젝트(프로젝트 완료2)', 100.0, 'mst_K42334fffrgg6421', '2000-12-12', 'TEAM', false),
(  99990, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-10-01', '프로젝트 for 달력 1',  100.0, 'mst_111334fffrgg6421', '2022-01-12', 'TEAM', false),
(  99989, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-11-12', '프로젝트 for 달력 2',  100.0, 'mst_K42h45dfd4gg6421', '2022-01-01', 'TEAM', false),
(  99988, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-09-12', '프로젝트 for 달력 3',  100.0, 'mst_ff4g34fffrgg6421', '2022-01-12', 'TEAM', false),
(  99987, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-12-12', '프로젝트 for 달력 4',  100.0, 'mst_aa344tg5dfgg6421', '2022-01-12', 'TEAM', false),
(  99986, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for 달력 5',  100.0, 'mst_qq2f4gbfeefe1gg6421', '2022-01-12', 'TEAM', false),
(  88888, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 1',  100.0, 'mst_qq2f4gbffrgg6421', '2022-01-12', 'TEAM', false),
(  88887, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 2',  100.0, 'mst_qsdzcxbffrgg6421', '2022-01-12', 'TEAM', false),
(  88886, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 3',  100.0, 'mst_qq2aaaaaaagg6421', '2022-01-12', 'TEAM', false),
(  88885, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 4',  100.0, 'mst_qq2f4gbfffffe421', '2022-01-12', 'TEAM', false),
(  88884, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2322-01-12', '프로젝트 for initiative 5',  100.0, 'mst_qq2f4gaawdeg6421', '2022-01-12', 'TEAM', false),
(  88883, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 3',  0.0, 'mst_qq2f4gaawdeg64fef', '2022-01-12', 'SINGLE', false),
(  88882, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 3',  0.0, 'mst_qfeeffeaawdeg64fef', '2022-01-12', 'TEAM', false),
(  77777, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 3',  0.0, 'mst_qfeeffefe64fef', '2022-01-12', 'TEAM', false);


;


insert into team_member
(created_date, last_modified_date, created_by, last_modified_by, is_new, project_role_type, project_id, user_seq, deleted) values
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99998, 2, false),
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
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, null, 77777, 19, false)
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
(77777, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_w11113ㄹeaafw4ef3', 'testKeyResult 3', 77777, false)
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

insert into notification (notification_id, created_date, last_modified_date, checked, message, notification_token, user_seq, deleted) values
(9999, '2022-12-17', '2022-12-16', false, '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_111fey1SERx',  3, false),
(9998, '2022-12-16', '2022-12-16', false, '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_e2222y1SERx',  16, false),
(9997, '2022-12-18', '2022-12-16', false, '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_aaaaaMoZey1SERx', 16, false),
(9996, '2022-12-19', '2022-12-16', false, '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_e3eeddoZey1SERx',  16, false),
(9995, '2022-12-20', '2022-12-16', false, '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_e144441Zey1SERx', 16, false),
(9994, '2022-12-21', '2022-12-16', false, '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_eK5555535ey1SERx', 3, false),
(9993, '2022-12-22', '2022-12-16', false, '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_6666yhger5tx', 2, false);

