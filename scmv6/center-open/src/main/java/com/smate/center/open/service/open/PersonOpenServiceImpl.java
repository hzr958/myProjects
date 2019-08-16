package com.smate.center.open.service.open;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.dao.open.PersonOpenDao;
import com.smate.center.open.model.open.PersonOpen;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

/**
 * personOpen服务实现
 * 
 * @author tsz
 *
 */
@Service("personOpenService")
@Transactional(rollbackFor = Exception.class)
public class PersonOpenServiceImpl implements PersonOpenService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonOpenDao personOpenDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;

  @Override
  public Long createOpenId(Long psnId) throws Exception {
    PersonOpen personOpen = personOpenDao.getPersonOpenByPsnId(psnId);
    Long openId = null;
    if (personOpen == null) {
      // 获取新的openId
      Long newOpenId = personOpenDao.getNewOpenId();
      // 判断openId是不是被使用过
      int i = 0;
      while (true) {
        i++;
        if (i > 9999) {
          // 循环10000 次 还没取到就不取了; 避免死循环
          return null;
        }
        personOpen = personOpenDao.getPersonOpenByOpenId(newOpenId);
        if (personOpen == null) {
          break;
        } else {
          newOpenId = personOpenDao.getNewOpenId();
          continue;

        }
      }
      // 保存关系
      personOpen = new PersonOpen(psnId, newOpenId);
      personOpenDao.save(personOpen);
      return newOpenId;
    } else {
      return openId;
    }

  }

  @Override
  public Long getOpenIdByPsnId(Long psnId) throws Exception {
    // 传进来的psnid必须是正确的
    PersonOpen personOpen = personOpenDao.getPersonOpenByPsnId(psnId);
    Long openId = null;
    if (personOpen == null) {
      // 为空 重新生成openId
      openId = createOpenId(psnId);
      // 默认关联sns 跟sie
      OpenUserUnion openUserUnion = openUserUnionDao.getOpenUserUnion(openId, OpenConsts.SMATE_TOKEN);
      if (openUserUnion == null) {
        openUserUnion = new OpenUserUnion();
        openUserUnion.setOpenId(openId);
        openUserUnion.setPsnId(psnId);
        openUserUnion.setToken(OpenConsts.SMATE_TOKEN);
        openUserUnion.setCreateDate(new Date());
        openUserUnion.setCreateType(OpenConsts.OPENID_CREATE_TYPE_2);
        openUserUnionDao.saveOpenUserUnion(openUserUnion);
      }
      OpenUserUnion openUserUnion1 = openUserUnionDao.getOpenUserUnion(openId, OpenConsts.SIE_TOKEN);
      if (openUserUnion1 == null) {
        openUserUnion1 = new OpenUserUnion();
        openUserUnion1.setOpenId(openId);
        openUserUnion1.setPsnId(psnId);
        openUserUnion1.setToken(OpenConsts.SIE_TOKEN);
        openUserUnion1.setCreateDate(new Date());
        openUserUnion1.setCreateType(OpenConsts.OPENID_CREATE_TYPE_2);
        openUserUnionDao.saveOpenUserUnion(openUserUnion1);
      }
    } else {
      openId = personOpen.getOpenId();
    }
    return openId;
  }

}
