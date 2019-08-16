package com.smate.web.group.service.grp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.group.dao.grp.grpbase.GrpControlDao;
import com.smate.web.group.model.grp.grpbase.GrpControl;

@Service
public class GrpControlServiceImpl implements GrpControlService {
  @Autowired
  private GrpControlDao grpControlDao;

  @Transactional
  @Override
  public GrpControl getCurrGrpControl(Long grpId) {
    // TODO Auto-generated method stub
    return grpControlDao.getCurrGrpControl(grpId);
  }
}
