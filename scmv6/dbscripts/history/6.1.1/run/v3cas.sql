-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end


--原因（    SCM-6595 解除QQ、） 2018-04-20 ajb start

-- Alter table 
alter table SYS_THIRD_USER
  storage
  (
    next 1
  )
;
-- Add/modify columns 
alter table SYS_THIRD_USER add nick_name varchar2(50);
-- Add comments to the columns 
comment on column SYS_THIRD_USER.nick_name
  is '第三方帐号的昵称';



-- Alter table 
alter table SYS_THIRD_USER
  storage
  (
    next 8
  )
;
-- Create/Recreate indexes 
create index UN_SYS_THIRD_USER_TYPE_PSNID on SYS_THIRD_USER (psn_id, type);

commit  ;
--原因（    SCM-6595 解除QQ、） 2018-04-20 ajb end