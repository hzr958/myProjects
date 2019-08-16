package com.smate.center.task.single.service.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.v8pub.dao.sns.PubOriginalDataDAO;
import com.smate.web.v8pub.dom.pdwh.PubOriginalDataDOM;

@Service("pubOriginalDataService")
@Transactional(rollbackFor = Exception.class)
public class PubOriginalDataServiceImpl implements PubOriginalDataService {
  @Autowired
  private PubOriginalDataDAO pubOriginalDataDAO;

  @Override
  public void savePubOriginalData(Long id, String pubData) {
    PubOriginalDataDOM pubOriginalData = new PubOriginalDataDOM(id, pubData);
    pubOriginalDataDAO.save(pubOriginalData);
  }
}
