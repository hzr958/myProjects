package com.smate.center.open.service.psnSynch;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.dao.psn.sysch.ThirdPersonDao;
import com.smate.center.open.model.third.psn.ThirdPsnInfo;


/**
 * 同步isis人员具体服务
 * 
 * @author AiJiangBin
 *
 */
public class SynchronousIsisPersonNsfcService implements SynchronousPersonNsfcService {

  @Autowired
  private ThirdPersonDao thirdPersonDao;

  @Override
  public void handleNsfcData(ThirdPsnInfo thirdPsnInfo) {
    Long id = thirdPersonDao.getId(thirdPsnInfo.getPsnId(), thirdPsnInfo.getFromSys());
    if (id != null) {
      thirdPsnInfo.setId(id);
    }

    thirdPersonDao.save(thirdPsnInfo);
  }

}
