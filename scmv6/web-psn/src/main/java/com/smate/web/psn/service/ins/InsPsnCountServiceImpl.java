package com.smate.web.psn.service.ins;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.psn.dao.ins.InsPsnCountDao;
import com.smate.web.psn.model.ins.InsPsnCount;

@Service
@Transactional(rollbackFor = Exception.class)
public class InsPsnCountServiceImpl implements InsPsnCountService {

  @Autowired
  private InsPsnCountDao insPsnCountDao;

  @Override
  public void addPsnCount(String insName, Integer type) throws Exception {
    InsPsnCount InsPsnCount = insPsnCountDao.findByName(insName);
    if (Objects.isNull(InsPsnCount)) {
      // 不存在此机构名,可能由于自主填写,而非选择机构
      return;
    }
    if (type != null && type == 1) {
      InsPsnCount
          .setHistoryPsnCount(InsPsnCount.getHistoryPsnCount() == null ? 1 : (InsPsnCount.getHistoryPsnCount() + 1));
    } else if (type != null && type == 2) {
      InsPsnCount.setHistoryPsnCount(InsPsnCount.getHistoryPsnCount() != null && InsPsnCount.getHistoryPsnCount() > 0
          ? (InsPsnCount.getHistoryPsnCount() - 1)
          : 0);
    } else {
      InsPsnCount.setHistoryPsnCount(0);
    }
    insPsnCountDao.updateIns(insName, InsPsnCount.getHistoryPsnCount());
  }

}
