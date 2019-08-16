-- 数据库脚本登记示例

--原因（有CQ号带上CQ号） 2016-12-8 WSN begin


--中间放sql语句


--原因（有CQ号带上CQ号） 2016-12-8 WSN end



--登录类型字段添加注释 2018-8-30 WSN begin
comment on column SYS_USER_LOGIN_LOG.login_type
  is '用户登录方式,1:"ISIS"方式, 2:"CITE"方式, 3:"SHARE"方式, 4:"STIAS", 5:"INSTEAD", 6:"GX", 7:"HN",8:用户输入密码方式登录, 9:mini页面登录;(填写几位数的，对应的是egrant系统的insId，对应scholar2.egrant_rol表), 10:自动登录, 11:没有权限的时候通过直接取cookie里面的AID，构建权限进入系统';
--登录类型字段添加注释 2018-8-30 WSN begin