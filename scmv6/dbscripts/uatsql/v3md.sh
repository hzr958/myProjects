#!/bin/sh
#此脚本在dbscript目录下执行
cd /home/oracle/dbscripts
#获取最新的版本号文件夹名
newdirname=''
 for i in `ls -tr |grep 6`;
do
        newdirname=$i;
done;

#最新版本号文件夹路径
sqlpath=/home/oracle/dbscripts/$newdirname

#样例sql文件目录
exsqlpath=/home/oracle/dbscripts/example

#执行SQL脚本
#

#7
sqlplus uatmd/$mduat <<EOF
@/home/oracle/dbscripts/$newdirname/test/v3md.sql
exit;
EOF
sleep 2

#sql到此执行完毕


#sql执行完后进行的操作

#for i in `ls $sqlpath/dev`;
#do
#   echo 正在操作$i;
#将/dev目录下的.sql文件内容追加一份到/test目录下的相应文件内
  sed -n '10,10000p' $sqlpath/test/v3md.sql >> $sqlpath/uat/v3md.sql
#将/dev/目录下的.sql文件恢复到默认内容
  cp -rf $exsqlpath/test/v3md.sql $sqlpath/test/v3md.sql
#done;

#svn 进行内容提交
cd $sqlpath && svn ci -m "uat执行开发sql.."









