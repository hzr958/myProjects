-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--ROL-3161 通过sie登录，弹出脚本错 2017-7-20 ltl begin
insert into sys_role_authoritie (ROLE_ID, AUTHORITY_ID)
values (3, 12);
--ROL-3161 通过sie登录，弹出脚本错 2017-7-20 ltl end


--原因（增加文件主表字段） 2017-07-14 tsz begin

alter table archive_files ADD file_url varchar2(100)  Default 0;

comment on column archive_files.file_url is '文件路径，通过file_path算出，没有初始化的为0';

alter table archive_files ADD file_size number  Default 0;

comment on column archive_files.file_size is '文件大小(KB)，没有初始化的为0';


alter table archive_files ADD status number  Default 0;

comment on column archive_files.status is '文件状态 0正常 1 删除  默认0';

--原因（增加文件主表字段） 2017-07-14 tsz  end