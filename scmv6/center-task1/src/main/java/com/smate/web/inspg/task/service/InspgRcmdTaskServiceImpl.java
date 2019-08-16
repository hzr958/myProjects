package com.smate.web.inspg.task.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.exception.RcmdTaskException;
import com.smate.web.inspg.task.dao.InspgDao;
import com.smate.web.inspg.task.dao.InspgMembersDao;
import com.smate.web.inspg.task.dao.InspgRcmdFinalDao;
import com.smate.web.inspg.task.model.Inspg;
import com.smate.web.inspg.task.model.InspgRcmdFinal;

@Service("inspgRcmdTaskService")
@Transactional(rollbackFor = Exception.class)
public class InspgRcmdTaskServiceImpl implements InspgRcmdTaskService {
  @Autowired
  InspgDao inspgDao;
  @Autowired
  InspgMembersDao inspgMembersDao;
  @Autowired
  InspgRcmdFinalDao inspgRcmdFinalDao;

  @Override
  public List<Long> getPsnIdList(Long psnId, Integer size) throws RcmdTaskException {
    List<Long> psnIdList = new ArrayList<Long>();
    psnIdList = inspgMembersDao.getCandidateList(psnId, size);

    return psnIdList;
  }

  @Override
  public void rcmdInspgResult(List<Long> psnIds) throws RcmdTaskException {

    if (psnIds.size() != 0 || psnIds != null) {
      for (Long psnId : psnIds) {
        List<Inspg> list = inspgDao.getInspgByCreatedTime(psnId);
        for (Inspg single : list) {
          InspgRcmdFinal result = new InspgRcmdFinal();
          result.setPsnId(psnId);
          result.setInspgId(single.getId());
          result.setInsZhName(single.getZhName());
          result.setInsEnName(single.getEnName());
          result.setRcmdScore(0);
          result.setCreateTime(new Date());
          inspgRcmdFinalDao.save(result);
        }

      }
    }
  }



}
