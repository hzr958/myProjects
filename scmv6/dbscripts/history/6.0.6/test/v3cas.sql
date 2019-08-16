-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end
--登录日志表登录类型字段添加注释  2017-7-31 WSN begin

comment on column SYS_USER_LOGIN_LOG.login_type
  is '用户登录方式,1:"ISIS"方式, 2:"CITE"方式, 3:"SHARE"方式, 4:"STIAS", 5:"INSTEAD", 6:"GX", 7:"HN",8:用户输入密码方式登录, 9:mini页面登录;(填写几位数的，对应的是egrant系统的insId，对应scholar2.egrant_rol表), 10:自动登录';

commit;

--登录日志表登录类型字段添加注释  2017-7-31 WSN end

--SCM-12978  2017-8-2 zzx begin
alter table SYS_USER_LOGIN_LOG add browser_info varchar(100);
alter table SYS_USER_LOGIN_LOG add system_info varchar(100);
comment on column SYS_USER_LOGIN_LOG.browser_info
  is '浏览器版本信息';
comment on column SYS_USER_LOGIN_LOG.system_info
  is '操作系统信息';
  commit;
  --SCM-12978  2017-8-2 zzx end