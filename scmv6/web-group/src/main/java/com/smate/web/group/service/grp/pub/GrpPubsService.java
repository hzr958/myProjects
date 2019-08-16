package com.smate.web.group.service.grp.pub;

import java.util.List;

import com.smate.web.group.action.grp.form.GrpPubForm;
import com.smate.web.group.action.grp.form.GrpPubShowInfo;
import com.smate.web.group.model.group.GrpPubs;

/**
 * 群组成果服务接口类
 * 
 * @author tsz
 *
 */
public interface GrpPubsService {

  /**
   * 导入群组成员成果
   * 
   * @throws Exception
   */
  public void importMemberPub(GrpPubForm form) throws Exception;

  /**
   * 保存群组成果
   * 
   * @throws Exception
   */
  public void saveGrpPubs(GrpPubs grpPubs) throws Exception;

  /**
   * 获取成果列表
   * 
   * @param form
   * @throws Exception
   */
  public void getGrpPubs(GrpPubForm form) throws Exception;

  /**
   * 获取成果列表筛选回调
   * 
   * @param form
   * @throws Exception
   */
  void getPubsCallBack(GrpPubForm form) throws Exception;

  /**
   * 获取成员成果列表
   * 
   * @param form
   * @throws Exception
   */
  public void getMemberPubs(GrpPubForm form) throws Exception;

  /**
   * 标记为项目成果或标记为文献
   * 
   * @param form
   * @throws Exception
   */
  public void markGrpPub(GrpPubForm form) throws Exception;

  /**
   * 删除群组成果
   * 
   * @param form
   * @throws Exception
   */
  public void deleteGrpPub(GrpPubForm form) throws Exception;

  /**
   * 判断群组成果是否是自己的
   * 
   * @return
   * @throws Exception
   */
  public boolean checkPubIsOwner(Long psnId, Long pubId) throws Exception;

  /**
   * 讨论页面，获取引用数最多的成果数
   * 
   * @param grpId
   * @return
   * @throws Exception
   */
  public List<GrpPubShowInfo> getFiveGrpPubsForDiscuss(Long grpId) throws Exception;

  /**
   * 讨论页面 选择成果 成果列表
   * 
   * @param form
   * @throws Exception
   */
  public void getSelectPubsList(GrpPubForm form) throws Exception;

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
  Integer getPubGroupShareKwCount(GrpPubs grPub, String pubKeyWords) throws Exception;

  /**
   * 获取计算isLabeled
   * 
   * @param grPub
   * @return
   * @throws Exception
   */
  Integer groupPubIsLabeled(GrpPubs grpPub) throws Exception;

  /**
   * 个人成果统计数
   * 
   * @param form
   * @throws Exception
   */
  public void getPsnPubsCallBack(GrpPubForm form) throws Exception;

  public Long getGroupPubCounts(Long grpId) throws Exception;
}
