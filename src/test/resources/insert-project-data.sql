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
( 19,'effefee23', 'Unknown','password','guest@unknown.com','UNREGISTERED','UNREGISTERED',null,null )
;

insert into project
( project_id, created_date, last_modified_date, created_by, last_modified_by, project_edt, project_objective, progress, project_token, project_sdt, project_type) values
(  99999, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', '2022-12-14', '프로젝트 1 멀티 프로젝트', 0.0, 'mst_Kiwqnp1Nq6lb4256', '2022-12-07', 'SINGLE'),
(  99998, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', '3999-12-14', '팀 맴버 테스트용 프로젝트', 100.0, 'mst_Kiwqnp1Nq6lbTNn0', '2022-12-07', 'TEAM'),
(  99997, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '3999-12-14', 'key result 테스트용 프로젝트', 0.0, 'mst_Kiwqnp1Nq6lb6421', '2000-12-12', 'TEAM'),
(  99996, '2000-12-12', '2022-12-12', 'testUser1', 'testUser1', '2022-12-14', 'Initiative 테스트용 프로젝트', 11.0, 'mst_K4e8a5s7d6lb6421', '2000-12-12', 'TEAM'),
(  99995, '2000-12-12', '2023-12-12', 'testUser1', 'testUser1', '2023-12-14', '프로젝트 조회 테스트용 프로젝트', 0.0, 'mst_K4232g4g5rgg6421', '2000-12-12', 'TEAM'),
(  99994, '2001-12-12', '2022-12-12', 'testUser1', 'testUser1', '3999-12-31', '프로젝트 조회 테스트용 프로젝트(프로젝트 완료)', 100.0, 'mst_as3fg34tgg6421', '2000-12-12', 'TEAM'),
(  99993, '2002-12-12', '2023-12-12', 'testUser1', 'testUser1', '2033-12-12', '프로젝트 조회 테스트용 프로젝트(프로젝트 70)', 70.0, 'mst_3gbyy554frgg6421', '2000-12-12', 'TEAM'),
(  99992, '2003-12-12', '2023-12-12', 'testUser1', 'testUser1', '2003-12-12', '프로젝트 조회 테스트용 프로젝트(프로젝트 60)', 60.0, 'mst_K4g4tfdaergg6421', '2000-12-12', 'TEAM'),
(  99991, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2004-12-12', '프로젝트 조회 테스트용 프로젝트(프로젝트 완료2)', 100.0, 'mst_K42334fffrgg6421', '2000-12-12', 'TEAM'),
(  99990, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-10-01', '프로젝트 for 달력 1',  100.0, 'mst_111334fffrgg6421', '2022-01-12', 'TEAM'),
(  99989, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-11-12', '프로젝트 for 달력 2',  100.0, 'mst_K42h45dfd4gg6421', '2022-01-01', 'TEAM'),
(  99988, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-09-12', '프로젝트 for 달력 3',  100.0, 'mst_ff4g34fffrgg6421', '2022-01-12', 'TEAM'),
(  99987, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-12-12', '프로젝트 for 달력 4',  100.0, 'mst_aa344tg5dfgg6421', '2022-01-12', 'TEAM'),
(  99986, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for 달력 5',  100.0, 'mst_qq2f4gbfeefe1gg6421', '2022-01-12', 'TEAM'),
(  88888, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 1',  100.0, 'mst_qq2f4gbffrgg6421', '2022-01-12', 'TEAM'),
(  88887, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 2',  100.0, 'mst_qsdzcxbffrgg6421', '2022-01-12', 'TEAM'),
(  88886, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 3',  100.0, 'mst_qq2aaaaaaagg6421', '2022-01-12', 'TEAM'),
(  88885, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 4',  100.0, 'mst_qq2f4gbfffffe421', '2022-01-12', 'TEAM'),
(  88884, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2322-01-12', '프로젝트 for initiative 5',  100.0, 'mst_qq2f4gaawdeg6421', '2022-01-12', 'TEAM'),
(  88883, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 3',  0.0, 'mst_qq2f4gaawdeg64fef', '2022-01-12', 'SINGLE'),
(  88882, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 3',  0.0, 'mst_qfeeffeaawdeg64fef', '2022-01-12', 'TEAM'),
(  77777, '2004-12-12', '2023-12-12', 'testUser1', 'testUser1', '2022-01-12', '프로젝트 for initiative 3',  0.0, 'mst_qfeeffefe64fef', '2022-01-12', 'TEAM');


;


insert into team_member
(created_date, last_modified_date, created_by, last_modified_by, is_new, project_role_type, project_id, user_seq) values
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99998, 2),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99998, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99998, 4),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99997, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99997, 11),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99996, 12),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99996, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99996, 17),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'MEMBER', 99995, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'MEMBER', 99994, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99994, 2),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99994, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99993, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'MEMBER', 99992, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99992, 9),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99992, 8),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', false, 'MEMBER', 99991, 13),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99990, 14),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99989, 14),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99988, 14),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99987, 14),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 99986, 14),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88888, 15),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88888, 9),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88888, 16),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88887, 15),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88886, 15),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88885, 15),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88885, 2),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88884, 15),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88884, 7),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88888, 17),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 88883, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 88882, 17),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 88882, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 99999, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'LEADER', 77777, 3),
('2022-12-12', '2022-12-12', 'testUser1', 'testUser1', true, 'MEMBER', 77777, 19)
;



insert into key_result
(key_result_id, created_date, last_modified_date, created_by, last_modified_by, key_result_token, name, project_id) values
(99999, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6MX15WQ3DTzQMs', 'testKeyResult 1', 99998),
(99998, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6f45vWQ3DTzQMs', 'testKeyResult 2', 99998),
(99997, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6f45vWQ3DTzQaa', 'testKeyResult 3', 99998),
(99996, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6f45vWQaaazQaa', 'testKeyResult 3', 99997),
(99995, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6faaaaaaDTzQaa', 'testKeyResult 3', 99988),
(99994, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6faaaaaaDTzQ22', 'testKeyResult 3', 88884),
(99993, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6faaaaaafw4ef3', 'testKeyResult 3', 88883),
(99992, '2022-12-12', '2022-12-12', 'testUser1', 'testUser1', 'key_wV6eeeeeeaafw4ef3', 'testKeyResult 3', 88885)
;


insert into initiative
(initiative_id, created_date, last_modified_date, created_by, last_modified_by, initiative_detail, initiative_done, initiative_edt, initiative_token, key_result_id, initiative_name, initiative_sdt, project_id, user_seq) values
(99999, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-14', 'ini_ixYjj5nODqtb3AH8', 99999, 'ini name', '2000-12-12', 99998, 3),
(99998, '2022-12-10', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail222', false, '2023-12-14', 'ini_ixYjj5nODfeab3AH8', 99996, 'ini name222', '2000-12-12', 99997, 11),
(99996, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-16', 'ini_ixYjj5aaafeab3AH8', 99996, 'ini name333', '2023-12-15', 99997, 11),
(99995, '2022-12-15', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-14', 'ini_ixYjjnnnafeab3AH8', 99996, 'ini name', '2000-12-12', 99997, 3),
(99997, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', false, '2023-12-14', 'ini_ixYjj5na3fdab3AH8', 99995, 'ini name876', '2000-12-12', 99988, 14),
(99994, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-14', 'ini_iefefena3fdab3AH8', 99994, 'ini name', '2000-12-12', 88884, 15),
(99993, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', false, '2023-12-14', 'ini_iefefawef3fdab3AH8', 99994, 'ini name', '2000-12-12', 88884, 15),
(99992, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', false, '2023-12-14', 'ini_iefefawef3f23fdb3AH8', 99994, 'ini name', '2000-12-12', 88884, 15),
(99991, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', false, '2023-12-14', 'ini_iefefawef3awef2AH8', 99994, 'ini name', '2000-12-12', 88884, 15),
(99990, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', false, '2023-12-14', 'ini_ieaaaawef3awef2AH8', 99994, 'ini name', '2000-12-12', 88884, 7),
(99989, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-15', 'ini_ieaaugywef3awef2AH8', 99994, 'ini name', '2000-12-12', 88884, 15),
(99988, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-15', 'ini_ieefefwef3awef2AH8', 99993, 'ini name', '2000-12-12', 88883, 3),
(99987, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', true, '2023-12-15', 'ini_ieefeffef3awef2AH8', 99992, 'ini name', '2000-12-12', 88885, 2),
(99986, '2022-12-14', '2022-12-14', 'testUser2', 'testUser2', 'initiative detail1', false, '2023-12-15', 'ini_iergrffef3awf2AH8', 99992, 'ini name', '2000-12-12', 88885, 2)
;


insert into feedback (feedback_id, created_date, last_modified_date, created_by, last_modified_by, feedback_token, grade_mark, initiative_id, checked, opinion, project_id, user_seq) values
(9999, '2023-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_el6q34zazzSyWx9', 'BEST_RESULT', 99999, true, '정말 좋아요!!', 99998, 3),
(9998, '2022-12-16', '2022-12-16', 'testUser2', 'testUser2', 'feedback_aaaaaagawe3rfwa3', 'BURNING_PASSION', 99999, false, '정말 좋아요!!', 99998, 2)
;

insert into notification (notification_id, created_date, last_modified_date, checked, message, notification_token, type, user_seq) values
(9999, '2022-12-17', '2022-12-16', 'NEW', '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_111fey1SERx', 'NEW_TEAM_MATE', 3),
(9998, '2022-12-16', '2022-12-16', 'DELETED', '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_e2222y1SERx', 'NEW_TEAM_MATE', 16),
(9997, '2022-12-18', '2022-12-16', 'NEW', '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_aaaaaMoZey1SERx', 'NEW_TEAM_MATE', 16),
(9996, '2022-12-19', '2022-12-16', 'DELETED', '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_e3eeddoZey1SERx', 'NEW_TEAM_MATE', 16),
(9995, '2022-12-20', '2022-12-16', 'CHECKED', '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_e144441Zey1SERx', 'NEW_TEAM_MATE', 16),
(9994, '2022-12-21', '2022-12-16', 'NEW', '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_eK5555535ey1SERx', 'NEW_TEAM_MATE', 3),
(9993, '2022-12-22', '2022-12-16', 'NEW', '<>님이 <> 팀원이 되셨습니다!👏👏', 'noti_6666yhger5tx', 'NEW_TEAM_MATE', 2);

