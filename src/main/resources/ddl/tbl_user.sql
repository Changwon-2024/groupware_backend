create table if not exists tables.tbl_user
(
    user_key        uuid      default gen_random_uuid() not null
        constraint tbl_user_pk
            primary key,
    email           varchar(100)                        not null,
    password        varchar(100)                        not null,
    name            varchar(10)                         not null,
    phone_number    varchar(50),
    address         varchar(100),
    birth_date      date,
    department_key  integer,
    position_key    integer,
    role_key        integer,
    user_image_key  uuid,
    stamp_image_key uuid,
    reg_date_time   timestamp default CURRENT_TIMESTAMP not null,
    is_deleted      boolean   default false             not null
);

comment on table tables.tbl_user is '유저 테이블';

comment on column tables.tbl_user.user_key is '유저 키';

comment on column tables.tbl_user.email is '이메일';

comment on column tables.tbl_user.password is '비밀번호';

comment on column tables.tbl_user.name is '이름';

comment on column tables.tbl_user.phone_number is '전화번호';

comment on column tables.tbl_user.address is '주소';

comment on column tables.tbl_user.birth_date is '생일';

comment on column tables.tbl_user.department_key is '부서 키';

comment on column tables.tbl_user.position_key is '직위 키';

comment on column tables.tbl_user.role_key is '직책 및 권한 키';

comment on column tables.tbl_user.user_image_key is '유저 이미지 키';

comment on column tables.tbl_user.stamp_image_key is '날인 이미지 키';

comment on column tables.tbl_user.reg_date_time is '생성 시간';

comment on column tables.tbl_user.is_deleted is '삭제 여부';

alter table tables.tbl_user
    owner to postgres;

