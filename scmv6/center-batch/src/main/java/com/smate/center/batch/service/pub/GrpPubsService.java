package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GrpPubs;

/**
 * 群组成果服务接口类
 * 
 * @author tsz
 *
 */
public interface GrpPubsService {
  /**
   * 保存群组成果
   * 
   * @throws Exception
   */
  public void saveGrpPubs(GrpPubs grpPubs) throws Exception;

  /**
   * 获取群组关键词
   * 
   * @param grpId
   * @return
   * @throws Exception
   */

  List<String> getGroupKw(Long grpId) throws Exception;

  /**
   * 
   * 计算关键词分享次数
   * 
   * @param pubKw
   * @param groupKws
   * @return
   * @throws Exception
   */
  Integer getShareKwCount(String pubKw, List<String> groupKws) throws Exception;

  /**
   * 获取计算relevance
   * 
   * @param grPub
   * @return
   * @throws Exception
   */
  Integer getPubGroupShareKwCount(GrpPubs grPub) throws Exception;

  /**
   * 获取群组成果
   * 
   * @param groupId
   * @return
   */
  List<GrpPubs> getGrpPubsList(Long groupId) throws Exception;

  /**
   * 获取计算isLabeled
   * 
   * @param groupId
   * @return
   */
  Integer groupPubIsLabeled(GrpPubs grpPub) throws Exception;

  void addPublicationToGroup(Long psnId, String des3PubIds, String groupIds, Integer articleType, Long folderId,
      int dynFlag) throws ServiceException;

  boolean isAddGroupByPubId(Long pubId, Long groupId, Integer articleType) throws ServiceException;

  /**
   * 获取成果详情
   * 
   * @param pubId
   * @return
   * @throws Exception
   */
  Map<String, Object> getPubDetails(Long pubId) throws Exception;

  /**
   * 获取计算relevance
   * 
   * @param grPub
   * @param map
   * @return
   * @throws Exception
   */
  Integer getNewPubGroupShareKwCount(GrpPubs grPub, Map<String, Object> map) throws Exception;

  /**
   * 获取计算isLabeled
   * 
   * @param grpPub
   * @param map
   * @return
   * @throws Exception
   */
  Integer groupNewPubIsLabeled(GrpPubs grpPub, Map<String, Object> map) throws Exception;

}
