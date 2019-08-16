package com.smate.center.task.service.search;



import com.smate.center.task.exception.ServiceException;



/**
 * 站外检索定时器任务处理业务逻辑接口.
 * 
 * @author mjg
 * 
 */

public interface SystemSearchService {

  public final static String INDEX_PUB_KEY = "index_scm_pub_map";
  public final static String INDEX_JNL_KEY = "index_scm_jnl_map";
  public final static String INDEX_PSN_KEY = "index_scm_psn_map";
  public final static String INDEX_FUND_KEY = "index_scm_fund_map";
  final static Integer INDEX_QUERY_MAX_LIMIT = 1000;

  /**
   * 加载站外检索成果信息.
   */
  void loadIndexPubInfo() throws ServiceException;

  /**
   * 加载站外检索人员信息.
   */
  void loadIndexPsnInfo() throws ServiceException;


}
