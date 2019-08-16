package com.smate.center.open.service.pubinfo;


/**
 * 成果信息获取WS接口
 * 
 * @author Scy
 * 
 */
public interface PubInfoService {

  /**
   * 根据人员ID返回该人员公开的成果记录总数
   * 
   * @param psnID SCM人员ID
   * @param keywords 搜索关键词
   * @param excludedPubIDS 排除的成果ID列表
   * @param psnGuidID 业务系统人员GUID（如果没有可置空）
   * @param putTypes 成果类型（如果没有可置空）
   * @return 成果总数
   */
  int searchPubsCountByPsn(String psnID, String keywords, String excludedPubIDS, String psnGuidID, String putTypes);

  /**
   * 根据成果ID返回成果详情(需要判断成果是否公开)
   * 
   * @param pubIDS
   * @param psnID
   * @return 操作成功后返回值格式请参见附件：(searchPubDetailByPubID.xml)
   *         返回值示例请参见附件(searchPubDetailByPubID_sample.xml)
   */
  String searchPubDetail(String pubIDS, String psnID);

  /**
   * 根据人员ID返回该人员公开的成果。
   * 
   * @param psnID SCM人员ID
   * @param keywords 搜索关键词
   * @param excludedPubIDS 排除的成果ID列表
   * @param psnGuidID 业务系统人员GUID（如果没有可置空）
   * @param sortType 排序类型
   * @param pageSize 每页大小
   * @param pageNum 页码
   * @param putTypes 成果类型（如果没有可置空）
   * @param xmlType 返回xml格式：1为列表格式，2为详情格式
   * @return 操作成功后返回值格式请参见附件：(searchPubsListByPsn.xml) 返回值示例请参见附件(searchPubsListByPsn_sample.xml)
   */
  String searchPubsListByPsn(String psnID, String keywords, String excludedPubIDS, String psnGuidID, String sortType,
      Integer pageSize, Integer pageNum, String putType, Integer xmlType);

  /**
   * 返回该人员个人成果库中所有的成果记录总数
   * 
   * @param psnGuidID
   * @param psnID
   * @param keywords
   * @param excludedPubIDS
   * @param putTypes 成果类型（如果没有可置空）
   * @return con_not_connected:帐号未关联 成果总数
   */
  String searchPubsCountByConnectedPsn(String psnGuidID, String psnID, String keywords, String excludedPubIDS,
      String putTypes);

  /**
   * 据已关联的人员ID返回该人员所有的成果。
   * 
   * @param psnGuidID
   * @param psnID
   * @param keywords
   * @param excludedPubIDS
   * @param sortType
   * @param pageSize
   * @param pageNum
   * @param putTypes 成果类型（如果没有可置空）
   * @param xmlType 返回xml格式：1为列表格式，2为详情格式
   * @return con_not_connected:帐号未关联 操作成功后返回值格式请参见附件：(searchPubsListByConnectedPsn.xml)
   *         返回值示例请参见附件(searchPubsListByConnectedPsn_sample.xml)
   */
  String searchPubsListByConnectedPsn(String psnGuidID, String psnID, String keywords, String excludedPubIDS,
      String sortType, int pageSize, int pageNum, String putType, Integer xmlType);

  /**
   * 根据成果ID返回成果详情
   * 
   * @param pubIDS
   * @param psnGuidID
   * @param psnID
   * @return
   */
  String searchPubDetailByConnectedPsn(String pubIDS, String psnGuidID, String psnID);


}
