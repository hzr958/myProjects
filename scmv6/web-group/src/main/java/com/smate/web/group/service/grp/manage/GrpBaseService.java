package com.smate.web.group.service.grp.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smate.core.base.consts.model.ConstKeyDisc;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.keywords.model.KeywordsHot;
import com.smate.web.group.action.grp.form.GrpDiscussForm;
import com.smate.web.group.action.grp.form.GrpMainForm;
import com.smate.web.group.action.grp.form.PrjInfo;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.model.grp.grpbase.GrpControl;

/**
 * 群组信息业务处理service
 * 
 * @author zzx
 *
 */
public interface GrpBaseService {
  // 群组推荐关键词JSON的key值.
  static final String GROUP_RECOMMEND_KEYWORD_ID = "id";
  static final String GROUP_RECOMMEND_KEYWORD = "keywords";
  static final String GROUP_RECOMMEND_KEYWORDTXT = "kwTxt";
  static final String RECOMMEND_KW_KEY_ZH = "zh";
  static final String RECOMMEND_KW_KEY_EN = "en";

  /**
   * 检查我的群组
   * 
   * @param form
   */
  void checkMyGrp(GrpMainForm form);

  /**
   * 设置群组置顶
   * 
   * @param form
   */
  void setGrpTop(GrpMainForm form);

  /**
   * 获取群组信息
   * 
   * @param form
   */
  void getGrpInfo(GrpMainForm form);

  /**
   * 检查群组是否存在
   * 
   * @param grpId
   * @return
   */
  boolean checkGrpIsExist(Long grpId);

  /**
   * 获取我的群组列表信息
   * 
   * @param form
   */
  void getMyGrpInfoList(GrpMainForm form);

  /**
   * 我的群组列表-回显
   * 
   * @param form
   */
  void getMyGrpInfoListCallBack(GrpMainForm form);

  /**
   * 获取我的群组简介信息
   * 
   * @param form
   */
  void getGrpDesc(GrpDiscussForm form);

  /**
   * 根据grpId获取群组简介信息
   * 
   * @param grpId
   */
  String getGrpDesc(Long grpId);

  /**
   * 获取我的群组类别
   * 
   * @param grpId
   */
  Integer getGrpCategory(Long grpId) throws Exception;

  /**
   * 删除群组
   * 
   * @param form
   * @throws Exception
   */
  void delMyGrp(GrpMainForm form) throws Exception;

  /**
   * 
   * @param form
   * @throws Exception
   */
  void modifyGrpPermissions(GrpMainForm form) throws Exception;

  /**
   * 获取我的群组列表
   * 
   * @param form
   * @throws Exception
   */
  public void getAllMyGrp(GrpMainForm form) throws Exception;

  /**
   * 获取群组名列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<GrpBaseinfo> getGroupNames(GrpMainForm form) throws Exception;

  /**
   * 获取学科关键词列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<ConstKeyDisc> getConstKeyDiscs(GrpMainForm form) throws Exception;

  /**
   * 根据一级学科获取二级学科.
   * 
   * @param topCategoryId
   * @return
   * @throws Exception
   */
  void getSecondDisciplineListById(GrpMainForm form) throws Exception;

  /**
   * 获取待同意的群组邀请信息
   * 
   * @param form
   * @throws Exception
   */
  void getHasGrpIviteInfo(GrpMainForm form) throws Exception;

  /**
   * 保存群组短地址
   * 
   * @param form
   * @throws Exception
   */
  void saveGrpShortUrl(GrpMainForm form) throws Exception;

  /**
   * 获取待同意的群组邀请列表
   * 
   * @param form
   * @throws Exception
   */
  void getHasIviteGrpList(GrpMainForm form) throws Exception;

  /**
   * 获取群组id
   * 
   * @param groupCode
   * @return
   * @throws Exception
   */
  Long getGrpIdByGroupCode(String groupCode) throws Exception;

  void grpListForMain(GrpMainForm form) throws Exception;

  void queryGrpReq(GrpMainForm form) throws Exception;

  void queryGrpInvite(GrpMainForm form) throws Exception;

  /**
   * 根据群组标题和简介推荐关键词
   * 
   * @param form
   * @return
   * @throws Exception
   */
  Map<String, List<KeywordsHot>> getGroupRcmdKeywords(GrpMainForm form) throws Exception;

  /**
   * 关键词领域
   * 
   * @param form
   */
  public void buildCategoryMapBaseInfo(GrpMainForm form);

  /**
   * 查询当前群组详情
   */
  public GrpBaseinfo getCurrGrp(Long grpId);

  /**
   * 当前群组权限
   */
  public GrpControl getCurrGrpControl(Long grpId);

  /**
   * 是否展示群组首页详情
   */
  public String grpIsShowIndexOpen(GrpBaseinfo grpBaseinfo, GrpControl grpControl);

  /**
   * 获取群组申请人数
   */
  public Integer getProposerCount(Long grpId);

  public PrjInfo dealDate(HashMap<String, Object> resultMap);

  public HashMap<String, Object> getGrpPrjInfo(String des3GrpId);

  /**
   * 获取我已加入的群组信息
   * 
   * @param form
   * @throws ServiceException
   */
  public void searchMyJoinedGrp(GrpMainForm form) throws ServiceException;

  void saveTmpTaskInfoRecord(Long grpId);
}
