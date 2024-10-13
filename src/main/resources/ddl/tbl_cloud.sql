create table if not exists tables.tbl_cloud
(
    element_key        uuid      default gen_random_uuid() not null
        constraint tbl_cloud_pk
            primary key,
    parent_element_key uuid,
    name               varchar(255)                        not null,
    file_size          integer,
    reg_user_key       uuid                                not null,
    reg_date_time      timestamp default CURRENT_TIMESTAMP not null,
    mod_user_key       uuid,
    mod_date_time      timestamp,
    is_deleted         boolean   default false             not null
);

comment on table tables.tbl_cloud is '파일 경로 관리 테이블';

comment on column tables.tbl_cloud.element_key is '요소 고유 키';

comment on column tables.tbl_cloud.parent_element_key is '부모 요소 키 (null일 경우 최상위 폴더';

comment on column tables.tbl_cloud.name is '폴더 혹은 파일명';

comment on column tables.tbl_cloud.file_size is '파일 크기 (null일 경우 폴더)';

comment on column tables.tbl_cloud.reg_user_key is '최초 작성자';

comment on column tables.tbl_cloud.reg_date_time is '생성 시각';

comment on column tables.tbl_cloud.mod_user_key is '최근 수정 유저';

comment on column tables.tbl_cloud.mod_date_time is '최근 수정 시각';

comment on column tables.tbl_cloud.is_deleted is '삭제 여부';

alter table tables.tbl_cloud
    owner to postgres;

