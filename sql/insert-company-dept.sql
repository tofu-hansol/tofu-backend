use tofu_db;

insert into company(name)
values ('한솔PNS'), ('한솔인티큐브'), ('한솔PNSITS'), ('한솔홀딩스'), ('한솔케미칼'), ('한솔테크닉스'), ('한솔제지'), ('한솔홈데코');

insert into dept(name, company_id)
values ('RM팀', 1), ('지즐', 1), ('소싱담당', 1),
       ('신규사업추진팀', 3), ('DT개발팀', 3), ('스마트기술연구소', 3), ('IT전략사업기술팀', 3),
       ('환경안전담당', 8), ('생산혁신팀', 8), ('R&D사업부', 2), ('AICC사업부', 2), ('R&D사업부', 2);

insert into member(email, password, name, profile_url, dept_id, position, mbti)
values ('hj@hansol.com', '1234', '박현준', '', 5, '선임', 'ESTJ')
      , ('mah@hansol.com', '1234', '서민아', '', 6, '선임', 'ENFP')
    ,  ('ke@hansol.com', '1234', '김가을', '', 7, '선임', '');


insert into category (name)
values ('운동'),
       ('스터디'),
       ('투자/금융'),
       ('음식'),
       ('문화/예술'),
       ('인문학/책'),
       ('기타');

insert into club (name, description, category_id)
values ('오르다', '클라이밍 동호회', 1),
       ('제빵', '제빵 동호회', 4),
       ('낚시', '낚시 동호회', 7);


insert into club_member (club_role, member_id, club_id)
values
    ('PRESIDENT', 1, 1),    -- 박현준 / 클라이밍동호회 / 회장
    ('PRESIDENT', 2, 2),    -- 서민아 / 제빵 동호회 / 회장
    ('PRESIDENT', 3, 3),    -- 김가을 / 낚시 동호회 / 회장
    ('MANAGER', 4, 2),      -- 뭉치 / 제빵 동호회 / 총무
    ('MEMBER', 1, 3),       -- 박현준 / 낚시 동호회 / 회원
    ('MEMBER', 4, 1),       -- 뭉치 / 클라이밍 동호회 / 회원
    ('MEMBER', 4, 3);       -- 뭉치 / 낚시 동호회 / 회원