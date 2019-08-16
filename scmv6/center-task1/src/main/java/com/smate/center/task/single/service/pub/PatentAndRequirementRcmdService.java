package com.smate.center.task.single.service.pub;

import java.util.List;

import com.smate.center.task.model.innocity.InnoCityPatRcmdForReq;
import com.smate.center.task.model.innocity.InnoCityReqRcmdForPat;

/**
 * 专利与需求书推荐
 */
public interface PatentAndRequirementRcmdService {
  public List<InnoCityPatRcmdForReq> getToHandleRequirementList();

  public String patRcmdForRequirement(InnoCityPatRcmdForReq req) throws Exception;

  public List<InnoCityReqRcmdForPat> getToHandlePatentList();

  public String reqRcmdForPatent(InnoCityReqRcmdForPat pat) throws Exception;

  public void updateReqRcmdForPatentStatus(InnoCityReqRcmdForPat req, Integer status, String extractKws);

  public void updatePatRcmdForReqStatus(InnoCityPatRcmdForReq req, Integer status, String extractKws);

  public void updatePatRcmdForReqStatus(InnoCityPatRcmdForReq req, Integer status);

  public void updateReqRcmdForPatentStatus(InnoCityReqRcmdForPat req, Integer status);

}
