-- 数据库脚本登记示例

--原因（更新注释） 2017-09-13 LJ begin

comment on column TMP_TASK_INFO_RECORD.job_type
  is '任务类别编号:SplitPubAuthorInfoTask = 1;
 UpdateAllPdwhPubCiteTimesTask = 2;
 BatchFulltextPdfToImageTask = 3;
 PdwhPubAuthorMatchSnsTask = 4;
 GeneratePsnDefaultAvatarsTask = 5;
 NsfcFullTextMatchTask=6
 (对应v_quartz_cron_expression表中相应的CRON_TRIGGER_BEAN)';

--原因（更新注释） 2017-09-13 LJ end