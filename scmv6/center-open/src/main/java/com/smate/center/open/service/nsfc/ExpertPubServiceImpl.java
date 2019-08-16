package com.smate.center.open.service.nsfc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.open.dao.nsfc.NsfcExpertPubDao;
import com.smate.center.open.model.consts.ConstPubType;
import com.smate.center.open.model.nsfc.NsfcExpertPub;
import com.smate.center.open.model.nsfc.SyncProposalModel;
import com.smate.center.open.service.consts.ConstPubTypeService;

@Service(value = "expertPubService")
public class ExpertPubServiceImpl implements ExpertPubService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private NsfcExpertPubDao nsfcExpertPubDao;
  @Autowired
  private ConstPubTypeService constPubTypeService;

  @Override
  public List<NsfcExpertPub> loadExpertPubsByGuid(SyncProposalModel model) throws Exception {
    try {
      List<NsfcExpertPub> expertPubs = this.nsfcExpertPubDao.getMyExpertPubs(model.getPsnId());
      Map<Integer, String> map = new HashMap<Integer, String>();

      if (expertPubs != null) {
        List<ConstPubType> ret = getConstPubTypeService().getAll();
        if (ret != null) {
          for (ConstPubType pubType : ret) {
            map.put(pubType.getId(), pubType.getName());

          }

        }

        for (NsfcExpertPub expertPub : expertPubs) {

          expertPub.setPubTypeDes(map.get(expertPub.getPubType()));

        }

      }

      return expertPubs;

    } catch (Exception e) {

      logger.error("读取评议专家psnId={}的成果列表失败！", new Object[] {e, model.getPsnId()});

    }

    return null;
  }

  private ConstPubTypeService getConstPubTypeService() throws Exception {

    return this.constPubTypeService;
  }

}
