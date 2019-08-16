package com.smate.center.open.service.nsfc;

import java.util.List;

import com.smate.center.open.model.nsfc.NsfcExpertPub;
import com.smate.center.open.model.nsfc.SyncProposalModel;

public interface ExpertPubService {
  /**
   * 加载评议专家成果
   * 
   * @param model
   * @return
   * @throws ServiceException
   */
  List<NsfcExpertPub> loadExpertPubsByGuid(SyncProposalModel model) throws Exception;

}
