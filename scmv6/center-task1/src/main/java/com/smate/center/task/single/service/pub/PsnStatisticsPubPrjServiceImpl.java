package com.smate.center.task.single.service.pub;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.PsnStatisticsPubPrjDao;
import com.smate.center.task.model.sns.pub.PsnStatisticsPubPrj;

@Service("psnStatisticsPubPrjService")
@Transactional(rollbackFor = Exception.class)
public class PsnStatisticsPubPrjServiceImpl implements PsnStatisticsPubPrjService {
  @Autowired
  private PsnStatisticsPubPrjDao psnStatisticsPubPrjDao;

  @Override
  public List<PsnStatisticsPubPrj> getPsnStatisticsList(Integer size) {
    return psnStatisticsPubPrjDao.getPsnStatisticsList(size);
  }

  @Override
  public void savePsnStatisticsPubPrj(PsnStatisticsPubPrj p) {
    psnStatisticsPubPrjDao.save(p);
  }

}
