create table if not exists tables.tbl_user_login
(
    user_key        uuid                                  not null
        constraint tbl_user_login_tbl_user_user_key_fk
            references tables.tbl_user,
    client_ip       char(15)  default '127.0.0.1'::bpchar not null,
    token_value     char(175)                             not null,
    login_date_time timestamp default CURRENT_TIMESTAMP   not null,
    is_permitted    boolean   default true                not null,
    constraint tbl_user_login_pk
        primary key (user_key, client_ip)
);

comment on table tables.tbl_user_login is '로그인 IP 기록 테이블';

comment on column tables.tbl_user_login.user_key is '접속 유저 키';

comment on column tables.tbl_user_login.client_ip is '접속 ip 주소';

comment on column tables.tbl_user_login.token_value is '로그인 토큰 값';

comment on column tables.tbl_user_login.login_date_time is '최초 접속 시간';

comment on column tables.tbl_user_login.is_permitted is '접속 허용 여부';

alter table tables.tbl_user_login
    owner to postgres;

