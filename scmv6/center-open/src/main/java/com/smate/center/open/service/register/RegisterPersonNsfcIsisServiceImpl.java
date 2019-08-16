package com.smate.center.open.service.register;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.nsfc.NsfcInsRoleDao;
import com.smate.center.open.dao.nsfc.NsfcPsnInsDao;
import com.smate.center.open.model.nsfc.NsfcInsRole;
import com.smate.center.open.model.nsfc.NsfcInsRoleId;
import com.smate.center.open.model.nsfc.NsfcPsnIns;
import com.smate.center.open.model.nsfc.NsfcPsnInsPk;
import com.smate.center.open.model.register.PersonRegister;
import com.smate.core.base.utils.dao.security.SysRolUserDao;
import com.smate.core.base.utils.model.cas.security.SysRolUser;

/**
 * isis成果在线 人员同步 数据处理
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class RegisterPersonNsfcIsisServiceImpl implements RegisterPersonNsfcService {

  @Autowired
  private SysRolUserDao sysRolUserDao;
  @Autowired
  private NsfcInsRoleDao nsfcInsRoleDao;
  @Autowired
  private NsfcPsnInsDao nsfcPsnInsDao;

  @Override
  public void handleNsfcData(PersonRegister person) {

    // 在cas端增加同步标识
    if (StringUtils.isNotBlank(person.getGuid())) {
      SysRolUser sysRolUser = new SysRolUser(person.getPersonId(), person.getGuid(), person.getInsId());
      sysRolUser.setFlag(sysRolUser.FLAG_SNS_SYNC_NSFC_PSN);
      sysRolUser.setLastDate(new Date());
      sysRolUserDao.save(sysRolUser);
    }
    // 保存成果在线权限信息
    saveNsfcInsRole(person.getPersonId());

    // 保存成果在线帐号信息
    saveNsfcIns(person.getPersonId(), person.getInsId(), person.getName(), person.getEmail());
  }

  private void saveNsfcIns(Long psnId, Long insId, String name, String email) {
    /*
     * NsfcPsnIns rolPsnIns = new NsfcPsnIns(); rolPsnIns.setPk(new NsfcPsnInsPk(psnId, insId));
     * rolPsnIns.setZhName(name); rolPsnIns.setPsnEmail(email); nsfcPsnInsDao.save(rolPsnIns);
     */
    NsfcPsnIns nsfcPsnIns = new NsfcPsnIns();
    nsfcPsnIns.setPk(new NsfcPsnInsPk(psnId, 2566L));
    nsfcPsnIns.setZhName(name);
    nsfcPsnIns.setPsnEmail(email);
    nsfcPsnInsDao.save(nsfcPsnIns);
    NsfcPsnIns scmPsnIns = new NsfcPsnIns();
    scmPsnIns.setPk(new NsfcPsnInsPk(psnId, 2565L));
    scmPsnIns.setZhName(name);
    scmPsnIns.setPsnEmail(email);
    nsfcPsnInsDao.save(scmPsnIns);
  }

  private void saveNsfcInsRole(Long psnId) {
    NsfcInsRole userRole = new NsfcInsRole();
    NsfcInsRoleId id = new NsfcInsRoleId(psnId, 2566L, 3L);// 成果在线研究人员
    userRole.setId(id);
    nsfcInsRoleDao.save(userRole);

    NsfcInsRole userRole1 = new NsfcInsRole();
    NsfcInsRoleId id1 = new NsfcInsRoleId(psnId, 2565L, 3L);// 科研在线研究人员
    userRole1.setId(id1);
    nsfcInsRoleDao.save(userRole1);
  }

}
