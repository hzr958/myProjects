package com.smate.center.task.v8pub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.v8pub.dao.pdwh.PdwhDataTaskDAO;
import com.smate.center.task.v8pub.dao.sns.PubDataTaskDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.sns.po.PubDataTaskPO;

@Service("dataTaskService")
@Transactional(rollbackFor = Exception.class)
public class DataTaskServiceImpl implements DataTaskService {

  @Autowired
  private PdwhDataTaskDAO pdwhDataTaskDAO;
  @Autowired
  private PubDataTaskDAO pubDataTaskDAO;

  @Override
  public void save(PdwhDataTaskPO pdwhData) {
    pdwhDataTaskDAO.saveOrUpdate(pdwhData);
  }

  @Override
  public void save(PubDataTaskPO pubData) {
    pubDataTaskDAO.saveOrUpdate(pubData);
  }


  @Override
  public List<PubDataTaskPO> findNeedDeal(Long startId, Long endId, Integer size) {
    return pubDataTaskDAO.findPubId(startId, endId, size);
  }

  @Override
  public List<PdwhDataTaskPO> findPdwhNeedDeal(Long startId, Long endId, Integer size) {
    return pdwhDataTaskDAO.findPdwhPubId(startId, endId, size);
  }

}
