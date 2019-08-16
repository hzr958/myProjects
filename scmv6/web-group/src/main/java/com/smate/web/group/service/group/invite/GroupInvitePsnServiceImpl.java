package com.smate.web.group.service.group.invite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.dao.group.invit.GroupInvitePsnDao;
import com.smate.web.group.form.GroupPsnForm;
import com.smate.web.group.model.group.invit.GroupInvitePsn;

/**
 * 群组邀请人员关系服务实现类
 * 
 * @author tsz
 *
 */
@Service("groupInvitePsnService")
@Transactional(rollbackFor = Exception.class)
public class GroupInvitePsnServiceImpl implements GroupInvitePsnService {

  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnConfigDao psnConfigDao;

  @Autowired
  private PsnStatisticsDao psnStatisticsDao;

  /**
   * 检查群组对于 人 是否开放
   * 
   * @param form
   */
  @Override
  public void checkGroupIsOpenForPsn(GroupPsnForm form) {
    GroupInvitePsn groupInvitePsn = findGroupInvitePsn(form.getGroupId(), form.getPsnId());
    form.setGroupInvitePsn(groupInvitePsn);
    if ((groupInvitePsn == null || (groupInvitePsn != null && !groupInvitePsn.getIsAccept().equals("1")))
        && !form.getGroupPsn().getOpenType().equals("O")) {
      // throw new GroupNoAccessException("没有访问权限");
    } else if (groupInvitePsn != null && groupInvitePsn.getIsAccept() != null
        && form.getGroupPsn().getOpenType().equals("O")) {
      form.setIsReadyJoin(true);
      Struts2Utils.getRequest().setAttribute("isReadyJoin", true);
    }
  }

  @Override
  public GroupInvitePsn findGroupInvitePsn(Long groupId, Long psnId) {

    GroupInvitePsn groupInvitePsn = groupInvitePsnDao.findGroupInvitePsn(groupId, psnId);

    /*
     * if (groupInvitePsn != null) { Person person = personManager.getPersonByRecommend(psnId);
     * groupInvitePsn.setPsnName(personManager.getPsnName(person));
     * groupInvitePsn.setPsnFirstName(person.getFirstName());
     * groupInvitePsn.setPsnLastName(person.getLastName()); }
     */
    return groupInvitePsn;
  }

  @Override
  public void findGroupInvitePsnForLeft(GroupPsnForm form) {
    List<GroupInvitePsn> GroupInvitePsnList = groupInvitePsnDao.findGroupInvitePsnForLeft(form);
    for (GroupInvitePsn member : GroupInvitePsnList) {
      Person psn = personDao.findPersonBase(member.getPsnId());
      Long openPubSum = psnConfigDao.findOpenPubSum(member.getPsnId());
      // GroupInvitePsn groupPsn = new GroupInvitePsn();
      // groupPsn.setAvatars(psn.getAvatars());
      // groupPsn.setPsnFirstName(psn.getFirstName());
      // groupPsn.setPsnLastName(psn.getLastName());
      // groupPsn.setPsnName(psn.getName());
      member.setOpenPubSum(openPubSum);
      member.setAvatars(psn.getAvatars());
      form.getGroupInvitePsnList().add(member);
    }

  }

  @Override
  public void findGroupPubInvitePsnForLeft(GroupPsnForm form) {
    List<GroupInvitePsn> GroupInvitePsnList = groupInvitePsnDao.findGroupInvitePsnForGroupRole(form);
    for (GroupInvitePsn member : GroupInvitePsnList) {
      Person psn = personDao.findPersonBase(member.getPsnId());
      // Long openPubSum =
      // psnConfigDao.findTotalPubSum(member.getPsnId());
      Long openPubSum = 0L;
      // 取冗余表数据
      Integer temp = psnStatisticsDao.get(member.getPsnId()).getPubSum();
      if (temp != null) {
        openPubSum = temp.longValue();
      }
      member.setOpenPubSum(openPubSum);
      member.setAvatars(psn.getAvatars());
      form.getGroupInvitePsnList().add(member);
    }

  }

}
