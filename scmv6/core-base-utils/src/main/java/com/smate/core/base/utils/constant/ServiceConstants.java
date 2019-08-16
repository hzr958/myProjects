package com.smate.core.base.utils.constant;

/**
 * service常量.
 * 
 * @author lqh
 */
public class ServiceConstants {

  public static final String ENCRYPT_KEY = "asdf(ad&dsda4fsafas--34sfasf"; // 必须大于20位，否则报错。

  public static final String EGRANT_ENCRYPT_KEY = "asdf(ad@dsd*4fsafas-=34s"; // egrant的密钥

  public static final String ISIS_ENCRYPT_KEY = "asdf(scm@irissz*4fsafas-=34s"; // ISIS关键词确认

  /** 特殊加密. */
  public static final String SPE_ENCRYPT_KEY = "asdf(ad&dsd555safas--34sfasf"; // 必须大于20位，否则报错。

  /** 上传文件目录，只提供公共上传组件使用. */
  public static final String DIR_UPFILE = "upfile";

  public static final String DIR_ZIPFILE = "tmpfile";

  /** 上传LOGO目录，只提供公共上传组件使用. */
  public static final String DIR_LOGO_UPFILE = "inslogo";
  /** sie上传LOGO目录，只提供公共上传组件使用. */
  public static final String SIE_DIR_LOGO_UPFILE = "sielogo";
  /** RO 登录Logo，for session key. */
  public static final String ROL_LOGO_URL = "rolLogoUrl";

  /** 上传头像目录. */
  public static final String DIR_AVATARS_UPFILE = "avatars";
  /** 上传简历头像目录. */
  public static final String DIR_AVATARS_RESUME_UPFILE = "avatarsResume";

  /** 上传简历LOGO目录. */
  public static final String DIR_AVATARS_RESUME_LOGO_UPFILE = "avatarsResumeLogo";

  public static final String DIR_PROFILE = "profile";

  /** 默认男性头像. */
  public static final String DEFAULT_MAN_AVATARS = "/avatars/head_nan_photo.jpg";

  /** 默认女性头像. */
  public static final String DEFAULT_WOMAN_AVATARS = "/avatars/head_nv_photo.jpg";

  /** 上传群组图片目录. */
  public static final String DIR_GROUPIMG_UPFILE = "groupimg";

  /** 上传群组简介图片目录. */
  public static final String DIR_GROUP_BRIEF_IMG_UPFILE = "groupbriefimg";

  /** 上传群组讨论图片目录. */
  public static final String DIR_DISCUSS_UPFILE = "discuss";

  /** 默认群组图片. */
  public static final String DEFAULT_GROUPIMG = "/groupimg/group_no_photo.jpg";

  /** 上传站内文件目录. */
  public static final String DIR_STATIONFILES_UPFILE = "upfile";
  /** 成果导出目录. */
  public static final String DIR_PUBINFO_EXPORT = "export/pubinfo";

  /** 成果录入目录. */
  public static final String PUB_FORM_TEMP = "enter/";

  /** 个人主页右边栏公共组件的生成图片目录. */
  public static final String DIR_SIDEBAR_FILE = "personalGrap";

  /** 个人主页右边栏公共组件的生成图片目录. */
  public static final String DIR_INS_FAX = "fax";

  /** H指数优化图片文件夹 */
  public static final String DIR_PUB_HINDEX_IMG = "pubHindexGraph";

  public static final String PUB_HINDEX_IMG_PREFIX = "pub_h_index_img_";

  public static final String DIR_PUB_FULLTEXT_IMAGE = "pubFulltextImage";

  public static final String DIR_PAT_FULLTEXT_IMAGE = "patFulltextImage";

  public static final String DIR_PRJ_FULLTEXT_IMAGE = "prjFulltextImage";

  /** 个人成果模板默认ID. */
  public static final int SCHOLAR_PUB_FORM_ID = 1;

  /** 单位节点. */
  public static final int ROL_NODE_ID = 10000;
  /** BPO单位节点. */
  public static final int BPO_NODE_ID = 0;
  /** 基准库节点. */
  public static final int PDWH_NODE_ID = 40000;
  /** 广西科研在线节点. */
  public static final int GX_NODE_ID = 20000;
  /** 海南科研在线节点. */
  public static final int HN_NODE_ID = 20002;
  /** 中山科研在线节点. */
  public static final int ZS_NODE_ID = 20003;
  /** 集中邮件服务节点 */
  public static final int MAIL_SRV_ID = 5;

  /** SNS 1节点. */
  public static final int SCHOLAR_NODE_ID_1 = 1;
  /** 个人项目模板默认ID. */
  public static final int SCHOLAR_PRJ_FORM_ID = 1;

  /** 科研报表. */

  public static final int REPORT_NODE_ID = 30000;

  // id列表(,逗号分隔)匹配正则表达式
  public static final String IDPATTERN = "^(\\d+)(,\\d+)*$";

  // 系统一些下拉选择默认开始年（如生日、教育经历、工作经历）.
  public static final int START_YEAR = 1900;

  /** 项目XML目录. */
  public static final String DIR_PROJECT_XML = "projectxml";
  /** 院系科研RO角色ID. */
  public static final Integer UNIT_MANAGER_ROLE_ID = 5;
  /** 院系领导角色ID. */
  public static final Integer UNIT_CONTACT_ROLE_ID = 6;

  // NSFC INS_ID
  public static final Long NSFC_INS_2565 = 2565L;
  public static final Long NSFC_INS_2566 = 2566L;
  public static final Long NSFC_INS_2567 = 2567L;
  public static final Long[] NSFC_INS_IDS = {NSFC_INS_2565, NSFC_INS_2566, NSFC_INS_2567};

  public static final String PUBLCATION_IMPORT_FILE_PATH = "help/";

  // 群组导入人员文件的路径_MJG_SCM-4427.
  public static final String GROUP_MEMBER_IMPORT_FILE_PATH = "html/resource/";
  public static final String GROUP_MEMBER_IMPORT_FILE_NAME = "member";

  public static final String PROJECT_IMPORT_FILE_PATH = "help/SMate_Output_project.xls";

  public static final String PROJECT_DOWNLOAD_FILE_PATH = "template/";
  public static final String PUBLICATION_DOWNLOAD_FILE_PATH = "template/";
  public static final String ISI_DISK_FILE_TEMPLATE = "template/pub/DiskFileTemplate.xls";// 光盘数据文件说明模板
  // 以下是成果文献列表action调用服务使用的常数
  // pub
  public static final String TYPE_LIST = "typeList";
  public static final String FOLDER_LIST = "folderList";
  public static final String GROUP_LIST = "groupList";
  public static final String GROUP_ADD = "groupAdd";
  public static final String YEAR_LIST = "yearList";
  public static final String RECORD_LIST = "recordList";
  public static final String PENDING_STAT = "pendingStat";
  public static final String PUB_TOTAL_COUNT = "pubTotalCount";
  public static final String CFM_RECPUB_COUNT = "cfmRecommendPubCount";
  public static final String PUB_HINDEX = "pub_hindex";
  public static final String CITE_TOTAL_TIMES = "totalCiteTimes";
  public static final String PSN_CPT_COUNT = "psnByCptCount";
  public static final String CITE_DBURL = "cite_url";
  public static final String PUB_READ_COUNT = "pubReadCount";
  // ref
  public static final String REF_TOTAL_COUNT = "refTotalCount";
  // 成果高亮用
  public static final String PUB_TOP_CITE = "topCite";
  public static final String PUB_MID_CITE = "midCite";
  public static final String PUB_BOTTOM_CITE = "bottomCite";
  public static final String HIGHTLIGHT_INDEX = "hightlightIndex";
  // sns的单位id
  public static final String SNS_INS_ID = "0";
}
