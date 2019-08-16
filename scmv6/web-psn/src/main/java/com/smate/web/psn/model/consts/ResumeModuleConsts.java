package com.smate.web.psn.model.consts;

/**
 * 简历常量类
 * 
 * @author wsn
 *
 */
public class ResumeModuleConsts {
  // 模块ID请保持和V_CV_MODULE_TYPE表一致，如有改动，请更新V_CV_MODULE_TYPE表
  // ----------------------简历模块ID-------------------------
  public static final int BASE_INFO = 1; // 个人基本信息模块
  public static final int EDU_INFO = 2; // 教育经历信息模块
  public static final int WORK_INFO = 3; // 工作经历信息模块
  public static final int PRJ_INFO = 4; // 项目信息模块
  public static final int REPRESENT_PUB_INFO = 5; // 代表性成果信息模块
  public static final int OTHER_PUB_INFO = 6; // 其他成果信息模块
  public static final int BRIEF_INFO = 7; // 简介信息
  // ----------------------简历模块ID-------------------------

  public static final int NFSC_TYPE = 1;// 基金简历类型
  public static final int SEQ_CV_PSNINFO = 1; // 个人信息模块的排序
  public static final int SEQ_CV_EDU = 2; // 教育经历模块的排序
  public static final int SEQ_CV_WORK = 3; // 工作经历模块的排序
  public static final int SEQ_CV_PRJ = 4; // 项目模块的排序
  public static final int SEQ_CV_REPRESENT_PUB = 5; // 10篇代表性成果模块的排序
  public static final int SEQ_CV_OTHER_PUB = 6; // 其他代表性成果模块的排序
  public static final String TITLE_CV_PSNINFO = "个人信息"; // 个人信息模块的标题
  public static final String TITLE_CV_EDU = "教育经历"; // 教育经历模块的标题
  public static final String TITLE_CV_WORK = "科研与学术经历"; // 工作经历模块的标题
  public static final String TITLE_CV_PRJ = "主持或参加科研项目(课题)及人才计划项目情况"; // 项目模块的标题
  public static final String TITLE_CV_REPRESENT_PUB = "10篇以内代表性论著"; // 10篇代表性成果模块的标题
  public static final String TITLE_CV_OTHER_PUB = "论著之外的代表性研究成果和学术奖励"; // 其他代表性成果模块的标题
  public static final int NFSC_MAX_MODULE_ID = 6; // 个人简介信息模块数量
}
