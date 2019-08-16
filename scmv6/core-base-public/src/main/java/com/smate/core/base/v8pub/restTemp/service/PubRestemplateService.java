package com.smate.core.base.v8pub.restTemp.service;

import java.util.List;


/**
 * 成果接口
 * 
 * @author ltl
 *
 */
public interface PubRestemplateService {
  /**
   * 根据成果id，获取成果信息
   * 
   * @param pubIds
   * @param psnId
   * @return
   * @throws Exception
   */
  public String pubListQueryByPubIds(List<Long> pubIds, Long psnId) throws Exception;

  /**
   * 获取个人代表成果方法
   * 
   * @param pubIds
   * @param psnId
   * @return
   * @throws Exception
   */
  public String psnRepresentPubList(Long currentPsnId, String des3PsnId) throws Exception;

  /**
   * 删除个人成果
   * 
   * @param pubId
   * @return
   * @throws Exception
   */
  public String delPsnPub(Long pubId, Long psnId) throws Exception;

  /**
   * 调用接口更新或保存成果json
   */
  public String saveOrUpdatePubJson(String pubJson);

  /**
   * 调用接口查询成果编辑页面的成果信息
   */
  public String snsEditPubQuery(String des3PubId);

  /**
   * 查询个人的代表成果和其他成果
   * 
   * @param psnId
   * @param moduleId
   * @return
   * @throws Exception
   */
  public String psnResumePubInfo(Long psnId, Long resumeId, Integer moduleId);
}
