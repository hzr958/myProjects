package com.smate.center.open.service.nsfc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.open.dao.nsfc.NsfcPubOtherInfoDao;
import com.smate.center.open.model.nsfc.NsfcPubOtherInfo;

/**
 * 成果其他信息服务实现.
 * 
 * @author zhanglingling
 *
 */
@Service(value = "nsfcPubOtherInfoService")
public class NsfcPubOtherInfoServiceImpl implements NsfcPubOtherInfoService {

  @Autowired
  private NsfcPubOtherInfoDao nsfcPubOtherInfoDao;

  @Override
  public String getNsfcPubSource(Long pubId) {
    return this.nsfcPubOtherInfoDao.getNsfcPubSource(pubId);
  }


  @Override
  public NsfcPubOtherInfo getNsfcPubOtherInfo(Long pubId) {
    return this.nsfcPubOtherInfoDao.get(pubId);
  }

}
