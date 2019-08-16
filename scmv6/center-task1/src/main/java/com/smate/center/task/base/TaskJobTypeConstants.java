package com.smate.center.task.base;

/**
 * center-task临时任务类型job_type常量表， 用于TmpTaskInfoRecord中读取临时处理数据
 * 
 * @author LJ
 *
 *         2017年9月1日
 */
public class TaskJobTypeConstants {
  public static Integer SplitPubAuthorInfoTask = 1;
  public static Integer UpdateAllPdwhPubCiteTimesTask = 2;
  public static Integer BatchFulltextPdfToImageTask = 3;
  public static Integer PdwhPubAuthorMatchSnsTask = 4;
  public static Integer GeneratePsnDefaultAvatarsTask = 5;
  public static Integer NsfcFullTextMatchTask = 6;
  public static Integer GeneratePersonNullNameTask = 7;
  public static Integer GenerateGroupCodeTask = 8;
  public static Integer GenerateIKAnalyerDictTask = 9;
  public static Integer SplitPatentCategoryTask = 10;
  public static Integer SnsDupPubGroupingTask = 11;
  public static Integer RenamePdwhFulltextFileTask = 12;
  public static Integer UpdatePdwhPubKeywordsTask = 20;
  public static Integer HandleISISEnameTask = 21;
  public static int[] SyncPmNameFromPersonTask = {101, 102, 103};
  public static Integer pdwhPubAssignInsTask = 23;
  public static Integer groupRcmdIncTask = 24;


}
