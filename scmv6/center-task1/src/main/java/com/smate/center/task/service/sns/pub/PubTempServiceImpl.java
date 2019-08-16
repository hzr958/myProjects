package com.smate.center.task.service.sns.pub;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.psn.PubTempDao;
import com.smate.center.task.model.sns.pub.PubTemp;

@Service
@Transactional
public class PubTempServiceImpl implements PubTempService {

  @Autowired
  private PubTempDao pubTempDao;

  @Override
  public void update(PubTemp pubTemp) throws Exception {
    // TODO Auto-generated method stub
    pubTempDao.update(pubTemp);
  }

  @Override
  public List<Long> getIds(Integer size) {
    // TODO Auto-generated method stub
    return pubTempDao.getIds(size);
  }

}
