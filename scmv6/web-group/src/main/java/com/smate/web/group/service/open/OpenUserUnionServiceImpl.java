package com.smate.web.group.service.open;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.random.RandomNumber;
import com.smate.web.group.dao.open.OpenUserUnionDao;

/**
 * open账号关联服务
 * 
 * @author tsz
 *
 */

@Service("openUserUnionService")
@Transactional(rollbackFor = Exception.class)
public class OpenUserUnionServiceImpl implements OpenUserUnionService {

  @Autowired
  private OpenUserUnionDao openUserUnionDao;

  /**
   * 获取openid
   */
  @Override
  public Long getOpenIdByPsnId(Long psnId) throws Exception {

    return openUserUnionDao.getOpenIdByPsnId(psnId);
  }


  /**
   * 构建openId
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  public Long buildOpenId(Long psnId, String token) throws Exception {

    Long openId = this.getOpenIdByPsnId(psnId);
    if (openId == null) {
      openId = this.getOpenId(token, psnId, 7);
    } else {
      // tsz 如果 openid 不为null就直接判断 当前openId 与当前 token能不能匹配到关联数据 如果不能 就添加一条数据
      OpenUserUnion temp = openUserUnionDao.getOpenUserUnion(openId, token);
      if (temp == null) {
        temp = new OpenUserUnion();
        temp.setCreateDate(new Date());
        temp.setOpenId(openId);
        temp.setCreateType(7);
        temp.setToken(token);
        temp.setPsnId(psnId);
        openUserUnionDao.save(temp);
      }
    }
    return openId;
  }


  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  public Long getOpenId(String token, Long psnId, int createType) throws Exception {
    Long openId = openUserUnionDao.getOpenIdByPsnId(psnId);
    if (openId == null) {
      openId = RandomNumber.getRandomNumber(8);
      // 查重
      while (true) {
        // 99999999 表示 没有真实用户的 数据交互
        if (openId.equals(99999999L)) {
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
      openUserUnionDao.save(openUserUnion);
    }
    return openId;
  }


}
