#!/bin/sh
#此脚本在dbscript目录下执行
cd /home/oracle/scmv6/dbscripts
#获取最新的版本号文件夹名
newdirname=''
 for i in `ls -tr |grep 6`;
do
        newdirname=$i;
done;

#最新版本号文件夹路径
sqlpath=/home/oracle/scmv6/dbscripts/$newdirname

#样例sql文件目录
exsqlpath=/home/oracle/scmv6/dbscripts/example

#执行增量SQL脚本
#1
sqlplus bpo2test/$bpotest <<EOF
@/home/oracle/scmv6/dbscripts/$newdirname/run/v3bpo_inc.sql
exit;
EOF
sleep 2

#sql到此执行完毕


#sql执行完后进行的操作

#for i in `ls $sqlpath/dev`;
#do
#   echo 正在操作$i;
#将生存执行完的增量文件内容追加到相应项目的sql文件中
  sed -n '10,10000p' $sqlpath/run/v3bpo_inc.sql >> $sqlpath/run/v3bpo.sql
#执行完增量sql后,删除增量sql文件
  git rm $sqlpath/run/v3bpo_inc.sql
#done;

#svn 进行内容提交
#cd $sqlpath && 
git add $sqlpath/run/v3bpo.sql && sleep 3 && git commit -m "测试机执行生产增量sql.." && sleep 3 && git push









