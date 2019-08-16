package com.smate.center.task.service.group;

import java.util.List;
import java.util.Map;

import com.smate.center.task.model.grp.GrpBaseinfo;
import com.smate.center.task.model.grp.GrpKwDisc;
import com.smate.center.task.model.grp.GrpPubInit;
import com.smate.center.task.model.pdwh.quartz.PubFundingInfo;
import com.smate.center.task.model.sns.quartz.GroupFundInfo;

/**
 * 群组后台任务接口
 * 
 * @author lj
 *
 */

public interface GrpService {

  public List<PubFundingInfo> getPubFundingInfoByFundingNo(String fundingNo);

  public void saveOpResult(GroupFundInfo groupInfo, Integer status);

  void importPdwhPubIntoGroup(GrpPubInit grpPubInit, Long ownerPsnId);

  public List<GroupFundInfo> getGroupFundInfo(Integer size, Long startGroupId, Long endGroupId);

  public List<Map<String, String>> getInstrestGrpInfo();

  public List<Long> getRcmdPdwhPubIds(String nsfcCatId);

  public void insertIntoRcmdPdwh(Long pdwhPubId, Long grpId);

  public List<GrpBaseinfo> getInstrestGroup();

  public List<GrpPubInit> getGrpPubInit(Long grpId, int year, int number);


  public Boolean grpHasInit(Long grpId);

  public List<Long> getPsnIdByCatId(Integer secondCatId);

  public Integer getSameKeywords(List<String> grpKwList, Long psnId);

  public List<Long> getGrpPubAuthor(Long grpId);

  public void saveGrpRcmd(Long psnId, Long grpId, Integer rcmdScore, Boolean isGrpmember);

  public List<GrpBaseinfo> getNeedRcmdGroup(Long lastGrpId, Integer size);

  public GrpKwDisc getGrpKwDisc(Long grpId);

  public List<Long> getGrpMemberIdByGrpId(Long grpId);

  public void upAppSettingConstant(String grpRcmdStart, Long grpId);

  public List<Long> getbatchhandleIdList(Integer size) throws Exception;

  public void updateTaskStatus(Long grpId, int status, String message) throws Exception;

  public GrpBaseinfo getGrpBaseInfo(Long grpId);

  public void updateGrpRcmd(Long grpId);

  public void deleteGrpRcmd(Long grpId);



}
