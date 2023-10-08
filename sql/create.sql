CREATE TABLE company (
     id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
     name	varchar(50)	NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE dept (
      id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
      name	varchar(100)	NOT NULL,
      company_id	int unsigned	NOT NULL,
      constraint fk_dept_company
          foreign key (company_id) references company (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE member (
      id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
      email	varchar(100)	NOT NULL,
      password	TEXT	NOT NULL,
      name	varchar(100)	NOT NULL,
      role	varchar(25)	NULL	DEFAULT 'ROLE_USER' COMMENT 'ROLE_USER, ROLE_ADMIN',
      profile_url	TEXT	NULL,
      created_at   datetime                       not null default CURRENT_TIMESTAMP,
      updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
      status	varchar(25)	NOT NULL	DEFAULT 'PENDING' COMMENT 'ACTIVATE, DORMANT, PENDING',
      dept_id	int unsigned	NOT NULL,
      position	varchar(50)	NULL,
      mbti	char(4)	NULL,

      constraint fk_member_dept
      foreign key (dept_id) references dept (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE notification (
      id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
      content	TINYTEXT	NOT NULL,
      notified_at	datetime	NULL,
      member_id	int unsigned	NOT NULL,
      read_at	datetime	NULL,

      constraint fk_notification_member
      foreign key (member_id) references member (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE category (
      id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
      name	VARCHAR(255)	NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE club (
        id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
        name	varchar(100)	NOT NULL,
        profile_url	TEXT	NULL,
        background_url	TEXT	NULL,
        description	TEXT	NULL,
        created_at   datetime                       not null default CURRENT_TIMESTAMP,
        updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
        status	varchar(25)	NOT NULL	DEFAULT 'DORMANT' COMMENT 'DORMANT, ACTIVATED, DELETED',
        account_number	varchar(45)	NULL,
        fee	smallint unsigned	NULL DEFAULT '0',
        category_id	int unsigned	NOT NULL,

        constraint fk_club_category
            foreign key (category_id) references category (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE budget (
        id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
        content	varchar(200)	NULL,
        amount	int	NOT NULL,
        transaction_date	datetime	NOT NULL,
        created_at   datetime                       not null default CURRENT_TIMESTAMP,
        updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
        status	varchar(25)	NOT NULL,
        club_id	int unsigned	NOT NULL,

        constraint fk_budget_club
            foreign key (club_id) references club (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE club_member (
         id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
         club_role	varchar(25)	NOT NULL	DEFAULT 'MEMBER' COMMENT 'PRESIDENT, MANAGER, MEMBER',
         member_id	int unsigned	NOT NULL,
         club_id	int unsigned	NOT NULL,
         created_at   datetime                       not null default CURRENT_TIMESTAMP,
         updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
         status varchar(25) NOT NULL DEFAULT 'WAITING' COMMENT 'WAITING, APPROVED, REJECTED',

            constraint fk_club_member_member
                foreign key (member_id) references member (id),
            constraint fk_club_member_club
                foreign key (club_id) references club (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE member ADD UNIQUE KEY unique_member_email(email);

CREATE TABLE club_schedule (
       id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
       event_at	datetime	NOT NULL,
       title	varchar(250)	NOT NULL,
       content	MEDIUMTEXT	NULL,
       club_id	int unsigned	NOT NULL,
       created_at   datetime                       not null default CURRENT_TIMESTAMP,
       updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
       status	varchar(25)	NOT NULL	DEFAULT 'RECRUITING' COMMENT 'RECRUITING, CLOSED, DELETED',

       constraint fk_club_schedule_club
              foreign key (club_id) references club (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE applicant (
       id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
       club_schedule_id	int unsigned	NOT NULL,
       member_id	int unsigned	NOT NULL,
       created_at   datetime                       not null default CURRENT_TIMESTAMP,
       updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,

       constraint fk_applicant_club_schedule
               foreign key (club_schedule_id) references club_schedule (id),
       constraint fk_applicant_member
              foreign key (member_id) references member (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE board (
         id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
         content	MEDIUMTEXT	NOT NULL,
         title	varchar(250)	NOT NULL,
         created_at   datetime                       not null default CURRENT_TIMESTAMP,
         updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
         status	varchar(25)	NOT NULL	DEFAULT 'PUBLISHED' COMMENT 'PUBLISHED, DELETED, FEATURED',
         member_id	int unsigned	NOT NULL,
         club_id	int unsigned	NOT NULL,

         constraint fk_board_member
             foreign key (member_id) references member (id),
         constraint fk_board_club
             foreign key (club_id) references club (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE club_photo (
        id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
        board_id	int unsigned	NOT NULL,
        is_main_photo	tinyint(1)	NOT NULL	DEFAULT True,
        image_url	TEXT	NOT NULL,
        created_at   datetime                       not null default CURRENT_TIMESTAMP,
        updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,

        constraint fk_club_photo_board
            foreign key (board_id) references board (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE comment (
           id	int unsigned	PRIMARY KEY NOT NULL AUTO_INCREMENT,
           content	TEXT	NOT NULL,
           created_at   datetime                       not null default CURRENT_TIMESTAMP,
           updated_at    datetime                       null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
           status	varchar(25)	NULL,
           board_id	int unsigned	NOT NULL,
           member_id int unsigned NOT NULL,

           constraint fk_comment_board
             foreign key (board_id) references board (id),
           constraint fk_comment_member
             foreign key (member_id) references member (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;









