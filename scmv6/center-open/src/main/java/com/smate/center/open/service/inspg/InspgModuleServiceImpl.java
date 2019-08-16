package com.smate.center.open.service.inspg;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.inspg.InspgAdminDao;
import com.smate.center.open.dao.inspg.InspgDao;
import com.smate.center.open.model.inspg.Inspg;

@Service("inspgModuleService")
@Transactional(rollbackFor = Exception.class)
public class InspgModuleServiceImpl implements InspgModuleService {

  @Autowired
  private InspgDao inspgDao;
  @Autowired
  private InspgAdminDao inspgAdminDao;


  @Override
  public List<Inspg> ManagerInspgList(Long psnId) {



    // 获取管理的机构ID
    List<Long> inspgAdminIds = inspgAdminDao.getInspgByPsnId(psnId);

    // 通过机构ID 获取机构信息
    List<Inspg> inspgList = inspgDao.getInspgByInspgIds(inspgAdminIds);


    return inspgList;
  }

}
