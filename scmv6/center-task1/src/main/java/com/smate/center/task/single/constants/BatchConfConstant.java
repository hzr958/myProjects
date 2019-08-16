package com.smate.center.task.single.constants;

/**
 * Batch设置常量.
 * 
 * @author hzr
 *
 */
public class BatchConfConstant {

  /**
   * 任务权重系数设定
   */
  public static final double TASK_WEIGHT_A_RATIO = 0.56;
  public static final double TASK_WEIGHT_B_RATIO = 0.3;
  public static final double TASK_WEIGHT_C_RATIO = 0.12;
  public static final double TASK_WEIGHT_D_RATIO = 0.02;

  /**
   * 任务权重种类设定
   */
  public static final String TASK_WEIGHT_A = "A";
  public static final String TASK_WEIGHT_B = "B";
  public static final String TASK_WEIGHT_C = "C";
  public static final String TASK_WEIGHT_D = "D";

  /**
   * T表读取数据数量设定
   */
  public static final Integer T_ITEMS_READ = 200;

  /**
   * 任务返回状态
   */
  public static final Integer JOB_ERROR = 3;
  public static final Integer JOB_SUCCESS = 1;

  /*
   * Batch JobParameters
   * 
   */
  public static final String JOB_CREATE_TIME = "job_create_time"; // 任务创建时间
  public static final String JOB_CONTENT = "job_content"; // 任务内容，json格式，提供需要解析的参数
  public static final String JOB_STRATEGY = "job_strategy"; // 任务种类，8位代码，供decider分配任务使用
  public static final String JOB_ID = "job_id"; // V_BATCH_JOB 中Id
  public static final String JOB_WITHOUT_DATA = "datafree"; // 不需要读取原始数据的任务

  /**
   * Batch Quartz 时间单位设置
   */
  public static final String SECOND = "s"; // 秒
  public static final String MINUTE = "m"; // 分钟
  public static final String HOUR = "h"; // 小时
  public static final String DAY = "d"; // 天
}
