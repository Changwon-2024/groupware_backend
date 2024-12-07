create table if not exists tbl_user
(
    user_key       uuid       default gen_random_uuid()      not null
        constraint tbl_user_pk
            primary key,
    email          varchar(100)                              not null,
    password       varchar(100)                              not null,
    name           varchar(10)                               not null,
    phone_number   varchar(50),
    address        varchar(100),
    birth_date     date,
    gender_flag    varchar(1) default 'M'::character varying not null,
    note           varchar(1000),
    role_level     integer    default 3                      not null,
    user_image_key uuid,
    reg_date_time  timestamp  default CURRENT_TIMESTAMP      not null,
    is_deleted     boolean    default false                  not null
);

comment on table tbl_user is '유저 테이블';

comment on column tbl_user.user_key is '유저 키';

comment on column tbl_user.email is '이메일';

comment on column tbl_user.password is '비밀번호';

comment on column tbl_user.name is '이름';

comment on column tbl_user.phone_number is '전화번호';

comment on column tbl_user.address is '주소';

comment on column tbl_user.birth_date is '생일';

comment on column tbl_user.gender_flag is '성별 구분 플래그(남성: M, 여성: F)';

comment on column tbl_user.note is '비고';

comment on column tbl_user.role_level is '권한 단계 (0: 어드민, 1: 편집, 2: 조회 및 다운로드, 3: 가입대기)';

comment on column tbl_user.user_image_key is '유저 이미지 키';

comment on column tbl_user.reg_date_time is '생성 시간';

comment on column tbl_user.is_deleted is '삭제 여부';

alter table tbl_user
    owner to postgres;

create table if not exists tbl_user_login
(
    user_key        uuid                                    not null
        constraint tbl_user_login_tbl_user_user_key_fk
            references tbl_user,
    client_ip       varchar(15) default '127.0.0.1'::bpchar not null,
    token_value     varchar(250)                            not null,
    login_date_time timestamp   default CURRENT_TIMESTAMP   not null,
    is_permitted    boolean     default true                not null,
    constraint tbl_user_login_pk
        primary key (user_key, client_ip)
);

comment on table tbl_user_login is '로그인 IP 기록 테이블';

comment on column tbl_user_login.user_key is '접속 유저 키';

comment on column tbl_user_login.client_ip is '접속 ip 주소';

comment on column tbl_user_login.token_value is '로그인 토큰 값';

comment on column tbl_user_login.login_date_time is '최초 접속 시간';

comment on column tbl_user_login.is_permitted is '접속 허용 여부';

alter table tbl_user_login
    owner to postgres;

create table if not exists tbl_cloud
(
    element_key        uuid          default gen_random_uuid()          not null
        constraint tbl_cloud_pk
            primary key,
    parent_element_key uuid,
    name               varchar(255)                                     not null,
    file_size          integer,
    reg_user_key       uuid                                             not null,
    reg_date_time      timestamp     default CURRENT_TIMESTAMP          not null,
    mod_user_key       uuid,
    mod_date_time      timestamp,
    is_deleted         boolean       default false                      not null,
    element_path       varchar(1000) default '/root'::character varying not null
);

comment on table tbl_cloud is '파일 경로 관리 테이블';

comment on column tbl_cloud.element_key is '요소 고유 키';

comment on column tbl_cloud.parent_element_key is '부모 요소 키 (null일경우 최상위 폴더)';

comment on column tbl_cloud.name is '폴더 혹은 파일명';

comment on column tbl_cloud.file_size is '파일 크기 (null 일경우 폴더)';

comment on column tbl_cloud.reg_user_key is '최초 작성자';

comment on column tbl_cloud.reg_date_time is '생성 시각';

comment on column tbl_cloud.mod_user_key is '최근 수정 유저';

comment on column tbl_cloud.mod_date_time is '최근 수정 시각';

comment on column tbl_cloud.is_deleted is '삭제 여부';

comment on column tbl_cloud.element_path is '경로';

alter table tbl_cloud
    owner to postgres;

create index if not exists tbl_cloud_parent_element_key_index
    on tbl_cloud (parent_element_key);

