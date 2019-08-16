package com.smate.center.open.service.publication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.publication.PublicationListDao;
import com.smate.center.open.model.publication.PublicationList;

/**
 * 成果收录情况.
 * 
 * @author LY
 * 
 */
@Service("publicationListService")
@Transactional(rollbackFor = Exception.class)
public class PublicationListServiceImpl implements PublicationListService {

  private static Logger logger = LoggerFactory.getLogger(PublicationListServiceImpl.class);
  @Autowired
  private PublicationListDao publicationListDao;

  @Override
  public PublicationList getPublicationList(Long pubId) throws Exception {

    return this.publicationListDao.get(pubId);
  }

  @Override
  public String convertPubListToString(PublicationList pubList) {
    if (pubList == null) {
      return null;
    }
    StringBuilder retStr = new StringBuilder();
    if (pubList.getListEi() != null && pubList.getListEi() == 1) {
      retStr.append(",EI");
    }
    if (pubList.getListSci() != null && pubList.getListSci() == 1) {
      retStr.append(",SCI");
    }
    if (pubList.getListIstp() != null && pubList.getListIstp() == 1) {
      retStr.append(",ISTP");
    }
    if (pubList.getListSsci() != null && pubList.getListSsci() == 1) {
      retStr.append(",SSCI");
    }
    return retStr.length() > 0 ? retStr.substring(1) : retStr.toString();
  }


}
