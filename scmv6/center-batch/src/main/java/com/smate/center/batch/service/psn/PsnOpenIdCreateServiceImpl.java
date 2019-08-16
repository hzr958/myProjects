package com.smate.center.batch.service.psn;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.institution.OpenUserCreateByInsDao;
import com.smate.center.batch.dao.sns.wechat.OpenUserUnionDao;
import com.smate.center.batch.exception.pub.OpenException;
import com.smate.center.batch.model.sns.pub.OpenUserCreateByIns;
import com.smate.core.base.utils.dao.security.SysRolUserDao;
import com.smate.core.base.utils.model.cas.security.SysRolUser;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.random.RandomNumber;

@Service("psnOpenIdCreateService")
@Transactional(rollbackFor = Exception.class)
public class PsnOpenIdCreateServiceImpl implements PsnOpenIdCreateService {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  public static final Long SYSTEM_OPENID = 99999999L;

  @Autowired
  private OpenUserCreateByInsDao openUserCreateByInsDao;

  @Autowired
  private OpenUserUnionDao openUserUnionDao;

  @Autowired
  private SysRolUserDao sysRolUserDao;

  @Override
  public List<OpenUserCreateByIns> getInsList(Integer status) {
    List<OpenUserCreateByIns> rsList = this.openUserCreateByInsDao.getInsInfoByStatus(status);
    return rsList;
  }

  /**
   * 取openId
   * 
   * @param form
   * @param user
   * @return
   * @throws OpenException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  public Long generateOpenId(String token, Long psnId, int createType) throws Exception {
    Long openId = openUserUnionDao.getOpenIdByPsnId(psnId);
    if (openId == null) {
      openId = RandomNumber.getRandomNumber(8);
      // 查重
      while (true) {
        // 99999999 表示 没有真实用户的 数据交互
        if (openId == SYSTEM_OPENID) {
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

  @Override
  public List<SysRolUser> getPsnIdByInsId(Long insId) {
    List<SysRolUser> userList = this.sysRolUserDao.getSysRolUserListByInsId(insId);
    return userList;
  }

}
