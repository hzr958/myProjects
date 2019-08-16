package com.smate.center.task.service.sns.quartz;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.open.OpenUserUnionDao;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.random.RandomNumber;

@Service("registerIsisgetOpenIdService")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class RegisterIsisgetOpenIdServiceImpl implements RegisterIsisgetOpenIdService {
  @Autowired
  private OpenUserUnionDao openUserUnionDao;

  @Override
  public Long getOpenId(String token, Long psnId, int createType) throws Exception {
    Long openId = openUserUnionDao.getOpenIdByPsnId(psnId);
    if (openId == null) {
      openId = RandomNumber.getRandomNumber(8);
      // 查重
      while (true) {
        // 99999999 表示 没有真实用户的 数据交互
        if (openId == 99999999L) {
          continue;
        }
        OpenUserUnion temp = openUserUnionDao.getOpenUserUnionByOpenId(openId);
        if (temp == null) {
          break;
        } else {
          openId = RandomNumber.getRandomNumber(8);
        }
      }
    }
    // 判断是否 有关联的第三方系统记录
    OpenUserUnion openUserUnion = openUserUnionDao.getOpenUserUnion(openId, token);
    if (openUserUnion == null) {
      openUserUnion = new OpenUserUnion();
      openUserUnion.setOpenId(openId);
      openUserUnion.setPsnId(psnId);
      openUserUnion.setToken(token);
      openUserUnion.setCreateDate(new Date());
      openUserUnion.setCreateType(createType);
      openUserUnionDao.saveOpenUserUnion(openUserUnion);
    }
    return openId;
  }

}
