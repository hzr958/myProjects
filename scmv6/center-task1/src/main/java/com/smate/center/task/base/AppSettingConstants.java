package com.smate.center.task.base;

/**
 * 系统配置常量.
 * 
 * @author liqinghua
 * 
 */
public interface AppSettingConstants {

  /**
   * SCMROL是否启用成果指派发送到SNS端.
   */
  String PUB_ASSIGN_SEND_ENABLED = "pub_assign_send_enabled";
  /**
   * SCMROL一次发送成果指派发送到SNS端最大条数.
   */
  String PUB_ASSIGN_SEND_MAX_SIZE = "pub_assign_send_max_size";
  /**
   * SCMROL一次查询发送成果指派发送到SNS端最大条数.
   */
  String PUB_ASSIGN_SEND_BATCH_SIZE = "pub_assign_send_batch_size";
  /**
   * SCMROL是否允许执行本地消息.
   */
  String LOCALE_MSG_ENABLED = "local_msg_enabled";
  /**
   * SCMROL本地消息并行运行缓存池大小.
   */
  String LOCALE_MSG_POOL_SIZE = "local_msg_pool_size";
  /**
   * SCMROL是否允许更新人员的成果统计数字(有时候为了提高性能可以关闭).
   */
  String PUBPSN_STATREF_ENABLED = "pubpsn_statref_enabled";
  /**
   * SCMROL是否允许更新成果统计冗余任务运行.
   */
  String KPI_REFRESH_PUB_ENABLED = "kpi_refresh_pub_enabled";
  /**
   * SCMROL更新成果统计冗余任务运行一次最多更新量.
   */
  String KPI_REFRESH_PUB_MAX_SIZE = "kpi_refresh_pub_max_size";
  /**
   * SCMROL是否允许更新部门统计冗余任务运行.
   */
  String KPI_REFRESH_UNIT_ENABLED = "kpi_refresh_unit_enabled";
  /**
   * SCMROL是否允许更新机构统计冗余任务运行.
   */
  String KPI_REFRESH_INS_ENABLED = "kpi_refresh_ins_enabled";
  /**
   * SCMROL是否允许更新地区统计冗余任务运行.
   */
  String KPI_REFRESH_CITY_ENABLED = "kpi_refresh_city_enabled";
  /**
   * SCMROL是否允许更新市管辖区统计冗余任务运行.
   */
  String KPI_REFRESH_DISTRICT_ENABLED = "kpi_refresh_district_enabled";
  /**
   * SCMROL是否允许更新省份统计冗余任务运行.
   */
  String KPI_REFRESH_PRV_ENABLED = "kpi_refresh_prv_enabled";
  /**
   * SCMROL是否允许重构成果作者单位.
   */
  String KPI_REBUILD_PM_ENABLED = "kpi_rebuild_pm_enabled";
  /**
   * SCMROL,SNS重构单位项目xml资金中的（单位）.
   */
  String PRJ_AMOUNTUNIT_ENABLED = "prj_amountunit_enabled";
  /**
   * SCMROL是否允许开启报表统计缓存.
   */
  String KPI_XML_ENABLE_CACHE = "kpi_xml_enable_cache";
  /**
   * SCMROL是否允许重构成果KPI统计完整性.
   */
  String KPI_REBUILD_KPIVALID = "kpi_rebuild_kpivalid";

  /**
   * puball重构索引。
   */
  String PUBALL_INDEX_ENABLED = "puball_index_enabled";
  /**
   * SCMROL是否启用人员后台加入科研在线.
   */
  String PSNINS_BACK_SYNC_ENABLED = "psnins_back_sync_enabled";
  /**
   * SCMROL是否启用任务提示统计数缓存.
   */
  String ROL_TASK_NOTICE_CACHE = "rol_task_notice_cache";
  /**
   * 成果XML使用数据库.
   */
  String ROL_PUB_XML_USE_DB = "pub_xml_use_db";
  /**
   * 是否同步成果文件到数据库中.
   */
  String ROL_SYNC_PUB_FILE_DB = "sync_pub_file_db";
  /**
   * 项目XML使用数据库.
   */
  String ROL_PRJ_XML_USE_DB = "prj_xml_use_db";
  /**
   * 是否同步项目文件到数据库中.
   */
  String ROL_SYNC_PRJ_FILE_DB = "sync_prj_file_db";
  /**
   * 成果认领发送合作者认领通知邮件分钟间隔.
   */
  String ROL_PUB_CFMCP_MAIL_MININTERV = "pub_cfmcp_mail_mininterv";
  /**
   * 成果认领发送合作者认领通知邮件开关.
   */
  String ROL_PUB_CFMCP_MAIL_ENABLED = "pub_cfmcp_mail_enabled";
  /**
   * SCMBPO发送匹配到单位的ISI成果XML到单位一次最多发送数量.
   */
  String ISI_PCSEND_MAX_SIZE = "isi_pubcache_send_max_size";
  /**
   * SCMBPO查询匹配到单位的ISI成果XML到单位一次最多查询数量 .
   */
  String ISI_PCSELECT_BATCH_SIZE = "isi_pubcache_select_batch_size";
  /**
   * SCMBPO是否启用发送匹配到单位的ISI成果XML到单位.
   */
  String ISI_PCSEND_ENABLED = "isi_pubcache_send_enabled";
  /**
   * SCMBPO发送匹配到单位的SCOPUS成果XML到单位一次最多发送数量.
   */
  String SPS_PCSEND_MAX_SIZE = "sps_pubcache_send_max_size";
  /**
   * SCMBPO查询匹配到单位的SCOPUS成果XML到单位一次最多查询数量 .
   */
  String SPS_PCSELECT_BATCH_SIZE = "sps_pubcache_select_batch_size";
  /**
   * SCMBPO是否启用发送匹配到单位的SCOPUS成果XML到单位.
   */
  String SPS_PCSEND_ENABLED = "sps_pubcache_send_enabled";
  /**
   * SCMBPO发送匹配到单位的CNKI成果XML到单位一次最多发送数量.
   */
  String CNKI_PCSEND_MAX_SIZE = "cnki_pubcache_send_max_size";
  /**
   * SCMBPO查询匹配到单位的CNKI成果XML到单位一次最多查询数量.
   */
  String CNKI_PCSELECT_BATCH_SIZE = "cnki_pubcache_select_batch_size";
  /**
   * SCMBPO是否启用发送匹配到单位的CNKI成果XML到单位.
   */
  String CNKI_PCSEND_ENABLED = "cnki_pubcache_send_enabled";

  /**
   * SCMBPO发送匹配到单位的CNKI成果XML到单位一次最多发送数量.
   */
  String CNIPR_PCSEND_MAX_SIZE = "cnipr_pubcache_send_max_size";
  /**
   * SCMBPO查询匹配到单位的CNIPR成果XML到单位一次最多查询数量.
   */
  String CNIPR_PCSELECT_BATCH_SIZE = "cnipr_pubcache_select_batch_size";
  /**
   * SCMBPO是否启用发送匹配到单位的CNIPR成果XML到单位.
   */
  String CNIPR_PCSEND_ENABLED = "cnipr_pubcache_send_enabled";

  /**
   * SCMBPO发送匹配到单位cnkipat成果XML到单位一次最多发送数量.
   */
  String CNKIPAT_PCSEND_MAX_SIZE = "cnkipat_pubcache_send_max_size";
  /**
   * SCMBPO查询匹配到单位的cnkipat成果XML到单位一次最多查询数量.
   */
  String CNKIPAT_PCSELECT_BATCH_SIZE = "cnkipat_pubcache_select_batch_size";
  /**
   * SCMBPO是否启用发送匹配到单位的cnkipat成果XML到单位.
   */
  String CNKIPAT_PCSEND_ENABLED = "cnkipat_pubcache_send_enabled";

  /**
   * SCMBPO是否启用ISI成果抓取数据匹配单位任务.
   */
  String ISI_MTPC_ENABLED = "isi_match_pubcache_enabled";
  /**
   * SCMBPO是否启用SCOUPS成果抓取数据匹配单位任务.
   */
  String SPS_MTPC_ENABLED = "sps_match_pubcache_enabled";
  /**
   * SCMBPO是否启用pubmed成果抓取数据匹配单位任务.
   */
  String PUBMED_MTPC_ENABLED = "pubmed_match_pubcache_enabled";
  /**
   * SCMBPO是否启用CNKI成果抓取数据匹配单位任务.
   */
  String CNKI_MTPC_ENABLED = "cnki_match_pubcache_enabled";
  /**
   * SCMBPO是否启用CNIPR成果抓取数据匹配单位任务.
   */
  String CNIPR_MTPC_ENABLED = "cnipr_match_pubcache_enabled";
  /**
   * SCMBPO是否启用CNKIPAT成果抓取数据匹配单位任务.
   */
  String CNKIPAT_MTPC_ENABLED = "cnkipat_match_pubcache_enabled";

  /**
   * SCMBPO发送匹配到单位的pubmed成果XML到单位一次最多发送数量.
   */
  String PUBMED_PCSEND_MAX_SIZE = "pubmed_pubcache_send_max_size";
  /**
   * SCMBPO查询匹配到单位的pubmed成果XML到单位一次最多查询数量 .
   */
  String PUBMED_PCSELECT_BATCH_SIZE = "pubmed_pubcache_select_batch_size";
  /**
   * SCMBPO是否启用发送匹配到单位的pubmed成果XML到单位.
   */
  String PUBMED_PCSEND_ENABLED = "pubmed_pubcache_send_enabled";

  /**
   * CAS是否启用人员检索创建索引任务.
   */
  String USER_SEARCH_ENABLED = "user_search_enabled";
  /**
   * 同步cas用户登录信息到sns,Person表.
   */
  String USERLOGIN_TO_PERSON_ENABLED = "userlogin_to_person_enabled";
  /**
   * SNS是否允许重构成果关键词.
   */
  String PUB_REBUILD_KEYWORDS = "pub_rebuild_keywords";

  /**
   * SNS同步好友工作单位，教育经历，国家信息.
   */
  String SNS_SYNC_FIREND_TO_PERSON = "sync_firend_to_person";
  /**
   * SNS智能好友推荐_任务状态.
   */
  String SNS_AUTO_RECOMMEND_FRIEND = "auto_recommend_friend_enabled";
  /**
   * SNS推荐人员，人员工作经历，教育经历所选单位_任务状态.
   */
  String SNS_PSN_WORK_EDU = "psn_work_edu_enabled";

  /**
   * SNS期刊匹配基础期刊.
   */
  String JNL_MATCH_BASEJNL = "jnl_match_basejnl_enabled";
  /**
   * 期刊推荐-已发表期刊.
   */
  String SNSJNL_PUBLISHED = "jnl_published_enabled";
  /**
   * 期刊推荐.
   */
  String SNS_PSN_JNL_COMMEND = "psn_jnl_commend_enabled";
  /**
   * 人员基准文献推荐.
   */
  String PSN_REF_RECOMMEND_ENABLED = "psn_ref_recommend_enabled";
  /**
   * SNS智能好友推荐_开始psnId.
   */
  String SNS_AUTO_RECOMMEND_FRIEND_START = "auto_recommend_friend_start";
  /**
   * SNS智能好友推荐_开始psnId.
   */
  String SNS_PSN_PUB_COPARTNER_START = "sns_psn_pub_copartner_start";
  /**
   * SNS智能好友推荐_开始psnId.
   */
  String SNS_PSN_PRJ_COPARTNER_START = "sns_psn_prj_copartner_start";

  /**
   * 群组推荐开始grpId
   */
  String GRP_RCMD_START = "grp_rcmd_start";
  /**
   * SNS智能好友推荐_停止推荐.
   */
  String SNS_AUTO_RECOMMEND_FRIEND_STOP = "auto_recommend_friend_stop";

  /**
   * 同步个人学科关键词.
   */
  String SNS_SYNC_PSN_DISC_KEY = "sync_psn_disc_key";
  /**
   * 成果XML使用数据库.
   */
  String SNS_PUB_XML_USE_DB = "pub_xml_use_db";
  /**
   * 是否同步成果文件到数据库中.
   */
  String SYNC_PUB_FILE_DB = "sync_pub_file_db";

  /**
   * 项目XML使用数据库.
   */
  String SNS_PRJ_XML_USE_DB = "prj_xml_use_db";
  /**
   * SNS成果自动确认任务.
   */
  String SNS_PUB_AUTO_CONFIRM = "pub_auto_confirm";
  /**
   * 个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认.
   */
  String SNS_PUB_RECONFIRM = "pub_reconfirm";
  /**
   * 是否同步项目文件到数据库中.
   */
  String SYNC_PRJ_FILE_DB = "sync_prj_file_db";

  String KEY_FRIEND_VOTE = "key_friend_vote";
  String MAIL_BOX_REC = "mail_box_receiver";
  String JOURNAL_FRIEND_VOTE = "journal_friend_vote";
  /**
   * 基金推荐刷新.
   */
  String FUND_COMMENT_REFRESH = "fund_comment_refresh";
  /**
   * 个人检索计分刷新.
   */
  String PSN_SCORE_REFRESH = "psn_score_refresh";

  String PSN_SCORE_INIT = "psn_score_init";
  /**
   * 站外检索信息.
   */
  String INDEX_INFO_INIT = "index_info_init_enabled";
  /**
   * 相关关键词刷新.
   */
  String DISCKEY_RELATED_REFRESH = "disckey_related_refresh";

  /**
   * 同步保存puball并拆分关键词.
   */
  String SYNC_PUBALL_KEYWORDS_ENABLED = "sync_puball_keywords_enabled";
  /**
   * /** 动态清理.
   */
  String DYNAMIC_BACKUP = "dynamic_backup";

  String DYNAMIC_CLEAN_NUM = "dynamic_clean_num";

  /**
   * 老动态数据同步.
   */
  String DYN_OLD_SYNC = "dynamic_old_sync";

  /** 成果影响因子排序. */
  String PUB_IMPACT_FACTORS_SORT = "pub_impact_factors_sort";

  /** 成果来源重构. */
  String PUB_BRIEF_DESC = "pub_brief_desc_enabled";

  /** 项目来源重构. */
  String PRJ_BRIEF_DESC = "prj_brief_desc_enabled";

  /**
   * 邮件监控节点.
   */
  String BPO_EMAIL_MONITOR_NODE = "bpo_email_monitor_node";
  /**
   * 邮件监控临界值.
   */
  String BPO_EMAIL_MONITOR_MAXMAILCOUNT = "bpo_email_monitor_maxmailcount";
  /**
   * 最多向管理员发送的邮件数量.
   */
  String BPO_EMAIL_MONITOR_MAXSENDCOUNT = "bpo_email_monitor_maxsendcount";

  /**
   * 文献基准库是否启用成果抓取数据拆分并匹配单位任务.
   */
  String PUBWH_EXPANDDC_ENABLED = "expand_dbcache_enabled";

  /**
   * 是否启用成果个人抓取数据拆分到基准库.
   */
  String PUBWH_EXPANDPC_ENABLED = "expand_pfdbcache_enabled";

  /**
   * 文献基准库是否启用批量成果抓取数据拆分并匹配单位任务.
   */
  String PUBWH_EXPANDBPC_ENABLED = "expand_bfdbcache_enabled";

  /**
   * 人员关键词变化自动更新人员期刊关键词匹配任务.
   */
  String PUBWH_PSNKEY_TO_JNLKEY_ENABLED = "psnkey_to_jnlkey_enabled";

  /**
   * rol、sns是否启用获取基准库ID.
   */
  String GET_PDWHID_ENABLED = "get_pdwhid_enabled";
  /**
   * rol、sns是否启用发送在线抓取XML到基准库.
   */
  String POST_PFETCHPUB_ENABLED = "post_pfetchpub_enabled";

  /**
   * pdwh是否开启计算推荐关键词的Hash值.
   */
  String KEYWORD_CMDHASH_ENABLED = "keyword_cmdhash_enabled";
  /**
   * pdwh是否启用人员成果关键词Hash值计算.
   */
  String KEYWORD_PSNPUBHASH_ENABLED = "keyword_psnpubhash_enabled";
  /**
   * pdwh是否启用人员熟悉的领域关键词Hash值计算.
   */
  String KEYWORD_PSNDSCHASH_ENABLED = "keyword_psndschash_enabled";

  /**
   * pdwh是否启用人员项目关键词Hash值计算.
   */
  String KEYWORD_PSNPRJHASH_ENABLED = "keyword_psnprjhash_enabled";
  /**
   * 个人特征词同步ROL.
   */
  String KEYWORD_PSN_FEATURE_ENABLED = "keyword_psn_feature_enabled";

  /**
   * 期刊推荐是否启用基准库的推荐数据.
   */
  String JNL_RECOMMEND_ENABLED = "jnl_recommend_pdwh_enabled";

  /**
   * SCM-1533人员公开信息默认设置调整.
   */
  String SNS_PERSON_CONFIG = "sns_person_config_enabled";

  /**
   * SCM-1533人员公开信息默认设置调整.
   */
  String SNS_PERSON_CONFIG_STOP = "sns_person_config_stop";

  /**
   * 执行基准期刊同步.
   */
  String BASE_JOURNAL_SYNC_ROL_ENABLED = "base_journal_sync_rol_enabled";

  /**
   * 执行基准期刊同步到Rol一次最多发送数量.
   */
  String BASE_JOURNAL_SYNC_ROL_SEND_MAX_SIZE = "base_journal_sync_rol_send_max_size";

  /**
   * 执行基准期刊同步到Rol一次最多查询数量.
   */
  String BASE_JOURNAL_SYNC_ROL_SELECT_BATCH_SIZE = "base_journal_sync_rol_select_batch_size";

  /**
   * 执行期刊基准信息更新信息同步.
   */
  String JOURNALJNL_SYNC_ROL_ENABLED = "journaljnl_sync_rol_enabled";

  /**
   * 执行期刊基准信息更新信息同步到Rol一次最多发送数量.
   */
  String JOURNALJNL_SYNC_ROL_SEND_MAX_SIZE = "journaljnl_sync_rol_send_max_size";

  /**
   * 执行期刊基准信息更新信息同步到Rol一次最多查询数量.
   */
  String JOURNALJNL_SYNC_ROL_SELECT_BATCH_SIZE = "journaljnl_sync_rol_select_batch_size";

  /**
   * 执行期刊统计刷新数据.
   */
  String JRPT_PUB_REFRESH_ENABLED = "jrpt_pub_refresh_enabled";
  /**
   * 更新期刊统计数据任务运行一次最多更新量
   */
  String JRPT_PUB_REFRESH_MAX_SIZE = "jrpt_pub_refresh_max_size";
  /**
   * 执行同步发送科研之友期刊类型文献数据到ROL
   */
  String PUB_REF_SYNC_ROL_ENABLED = "pub_ref_sync_rol_enabled";
  /**
   * 执行文献期刊统计刷新数据.
   */
  String JRPT_REF_REFRESH_ENABLED = "jrpt_ref_refresh_enabled";
  /**
   * 更新文献期刊统计数据任务运行一次最多更新量.
   */
  String JRPT_REF_REFRESH_MAX_SIZE = "jrpt_ref_refresh_max_size";
  /**
   * 简历XML拆分任务
   */
  String CV_XML_SPLIT_TASK_ENABLE = "cv_xml_split_task_enable";
  /**
   * 简历推荐任务
   */
  String CV_RECOMMEND_TASK_ENABLE = "cv_recommend_task_enable";

  /**
   * 动态综合任务合并间隔.
   */

  String DYNAMIC_MERGING_TIME = "dynamic_merging_time";
  /**
   * 人员合并任务
   */
  String PERSON_MERGE_TASK_ENABLE = "person_merge_task_enable";

  /**
   * 有新认领成果时，邮件通知好友任务
   */
  String ETEMPLATE_NEW_PUB_TO_FRIENDS = "etemplate_new_pub_to_friends";

  /**
   * 科研影响力月度统计分析任务
   */
  String ETEMPLATE_INFLUENCE_STATISTICAL = "etemplate_influence_statistical";

  /**
   * 成果引用更新邮件任务
   */
  String PUB_CITE_UPDATE = "pub_cite_update_for_email";

  /**
   * 新加好友后，邮件通知好友任务
   */
  String ETEMPLATE_NEW_ADD_FRIEND = "etemplate_new_add_friend";

  /**
   * 更新工作经历后，发送邮件好友任务
   */
  String ETEMPLATE_UPDATE_WORKHISTORY_LOG = "etemplate_update_workHistory";

  /**
   * 更新研究领域后，发送邮件给好友任务
   */
  String ETEMPLATE_UPDATE_DISC = "etemplate_update_disc";

  /**
   * 新加入群组后，给好友发邮件任务
   */
  String ETEMPLATE_NEW_JOIN_GROUP = "etemplcate_new_join_group";
  /**
   * 给有好友推荐的人员发邮件任务
   */
  String ETEMPLATE_PSN_FRD_RECOMMEND = "etemplate_psn_frd_recommend";

  /**
   * 个人主页配置重跑
   */
  String RESUME_CONFIG_RERUN = "resume_config_task_enable";

  /**
   * ISI成果拆分任务_MJG_SCM-2917.
   */
  String ISI_PUB_EXPAND = "isi_pubexpand_enabled";
  /**
   * CNKI成果拆分任务_MJG_SCM-2917.
   */
  String CNKI_PUB_EXPAND = "cnki_pubexpand_enabled";
  /**
   * 基准库成果匹配任务_MJG_SCM-2917.
   */
  String PUB_MATCH_SCORE = "pub_match_enabled";
  /**
   * 文件分享-邮件发送
   */
  String FILE_SHARE_EMAIL_ENABLE = "file_share_email_enable";

  /**
   * 成果认领邮件改版，给成果合作者发邮件
   */
  String ETEMPLATE_PUB_CFM_DETEAIL = "etemplate_pub_cfm_detail";

  /**
   * 人员合并任务(合并动态任务)
   */
  String PERSON_MERGE_DYNAMIC_TASK_ENABLE = "person_merge_dynamic_task_enable";

  /**
   * 成果合作者推广邮件发送任务
   */
  String ETEMPLATE_COOP_RECMD_SIX = "etemplate_coop_recmd_six";

  /**
   * 成果合作者推广邮件数据整理任务
   **/
  String ETEMPLATE_COOP_RECMD_DATA = "etemplate_coop_recmd_data";

  /**
   * 邮件日志任务
   */
  String MAIL_LOG_PROMOTE_TASK_ENABLE = "mail_log_promote";
  /*
   * 成果认领推广邮件
   */
  String ETEMPLATE_PUB_CONFIRM_PROMOTE = "etemplate_pub_confirm_promote";

  /**
   * 处理分享发件箱老数据.
   */
  String DEAL_SHARE_MAILBOX_OLD_DATA_ENABLE = "deal_share_mailbox_old_data_enable";

  /**
   * 处理分享收件箱老数据.
   */
  String DEAL_SHARE_INBOX_OLD_DATA_ENABLE = "deal_share_inbox_old_data_enable";

  /**
   * 人员基金推荐.
   */
  String PSN_FUND_RECOMMEND_ENABLE = "psn_fund_recommend_enable";

  /**
   * IRIS业务系统和SNS关联人员的验证码有效期
   */
  String IRIS_SNS_CODE_VALIDITY = "iris_sns_code_validity";

  /**
   * bpo邮件发送刷新任务
   */
  String BPO_SEND_EMAIL_REFRESH_TASK = "bpo_email_send_refresh_task";

  String SYNC_NSFCWS_TO_SNS = "sync_nsfcws_to_sns";

  /**
   * bpo邮件发送管理任务
   */
  String BPO_SEND_EMAIL_MANAGER_TASK = "bpo_email_send_manager_task";

  /**
   * 成果全文转换成图片.
   */
  String PUB_FULLTEXT_TO_IMAGE = "pub_fulltext_to_image";

  /**
   * 项目全文转换成图片.
   */
  String PRJ_FULLTEXT_TO_IMAGE = "prj_fulltext_to_image";

  /**
   * 计算个人特征关键词个数任务，用来反推个人特征关键词
   */
  String CALCULATE_PSN_KW_RMC_COUNT = "calculate_psn_kw_rmc_count";

  /**
   * 是否启用获取论文作者
   */
  String FIND_PUB_AUTHOR_ENABLED = "find_pub_author_enabled";

  /**
   * 是否启用成果相关文献推荐任务
   */
  String PUB_RELATED_ENABLED = "pub_related_enabled";

  /**
   * 是否启用成果读者推荐任务
   */
  String PUB_READER_ENABLED = "pub_reader_enabled";

  String IRIS_PSN_INS_REFRESH = "iris_psn_ins_refresh";
  /**
   * 基金、论文合作者推荐任务
   */
  String PSN_MAY_COOPERATOR_RUN = "psn_may_cooperator_task_enable";

  String PSN_MERGE_EMAIL_NOTICE = "psn_merge_email_notice";

  /**
   * 是否重构基准库publication_all表来源字段
   */
  String PUBALL_BRIEF_ENABLED = "puball_brief_enabled";

  /**
   * 集中邮件服务发送邮件任务开头
   */
  String MAILSRV_SEND_EMAIL_ENABLED = "mailsrv_send_email_enabled";

  /**
   * 集中邮件服务 生成邮件数据辅助任务开关
   */
  String START_GENERATE_MAIL_ASSIST_ENABLED = "start_generate_mail_assist_enabled";

  /**
   * 集中邮件服务 发送邮件辅助任务开关
   */
  String SEND_MAIL_ASSIST_ENABLED = "send_mail_assist_enabled";

  /**
   * 集中邮件服务 发送推广邮件任务
   */
  String SEND_PROMOTE_MAIL_ENABLED = "send_promote_mail_enabled";

  String TASK_STATUS_MONITOR_MAILING = "task_status_monitor_mailing";

  String TASK_STATUS_MONITOR_MAILING_TO = "task_status_monitor_mailing_to";

  String TASK_STATUS_LOG_CLEAN = "task_status_log_clean";

  /**
   * 构建项目HTML任务开关_MJG_2013-08-25.
   */
  String ROL_PRJ_BUILD_HTML_ENABLED = "rol_prj_build_html";

  /**
   * 成果影响力数据预处理计算定时器任务开关_MJG_SCM-5978.
   */
  String SNS_PUB_EFFECT_STATISTICS_ENABLED = "pub_effect_statistics_task";

  /**
   * 个人动态构建HTML任务开关_MJG_SCM-5912.
   */
  String SNS_DYN_HTML_ENABLED = "dynamic_content_enabled";

  /**
   * 群组动态构建HTML任务开关_MJG_SCM-5912.
   */
  String SNS_GROUP_DYN_HTML_ENABLED = "dynamic_group_content_enabled";

  /**
   * 系统消息旧数据迁移任务开关_MJG_SCM-5910.
   */
  String SNS_MESSAGE_DATA_ENABLED = "message_box_enabled";

  /**
   * 群组人员数据迁移同步任务_MJG_SCM-6000.
   */
  String SNS_GROUP_PSN_DATA_ENABLED = "group_psn_data_enabled";
}
