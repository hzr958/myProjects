-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end

--原因（ 手机号注册功能） 22018-12-17 ajb start

-- Add/modify columns
alter table SYS_USER add MOBILE_NUMBER NUMBER(18);
-- Add comments to the columns
comment on column SYS_USER.MOBILE_NUMBER
  is '手机号';
-- Create/Recreate indexes
create unique index sys_user_uni_mobile on SYS_USER (mobile_number);

alter table SYS_USER modify MOBILE_NUMBER VARCHAR2(20);

--原因（ 手机号注册功能） 22018-12-17 ajb end


--原因（    SCM-21947 注册加手机号） 2018-12-25  ajb start
-- Add comments to the columns
comment on column SYS_USER_LOGIN_LOG.LOGIN_TYPE
  is '用户登录方式,1:"ISIS"方式, 2:"CITE"方式, 3:"SHARE"方式, 4:"STIAS", 5:"INSTEAD", 6:"GX", 7:"HN",8:用户输入密码方式登录, 9:mini页面登录;(填写几位数的，对应的是egrant系统的insId，对应scholar2.egrant_rol表), 10:自动登录, 11:没有权限的时候通过直接取cookie里面的AID，构建权限进入系统;15=手机号登录';


--原因（    SCM-21947 注册加手机号） 2018-12-25  ajb end
