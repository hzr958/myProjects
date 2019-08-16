package com.smate.center.task.service.sns.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.GroupFundInfoMembersDao;
import com.smate.center.task.dao.sns.quartz.IsisPrjMemeberMatchDao;
import com.smate.center.task.model.sns.quartz.GroupFundInfoMembers;
import com.smate.center.task.model.sns.quartz.IsisMatchedPrjMembers;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;

/**
 * IsisPrjMemberMatchTask 任务实现类
 * 
 * @author LJ
 *
 */

@Service("isisPrjMemberMatchService")
@Transactional(rollbackFor = Exception.class)
public class IsisPrjMemberMatchServiceImpl implements IsisPrjMemberMatchService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  IsisPrjMemeberMatchDao isisPrjMemeberMatchDao;
  @Autowired
  PersonDao personDao;
  @Autowired
  GroupFundInfoMembersDao groupFundInfoMembersDao;

  @Override
  public List<GroupFundInfoMembers> getGroupFundInfoMembers(Integer size, Long startGroupId, Long endGroupId) {
    List<GroupFundInfoMembers> toDoList = groupFundInfoMembersDao.getToHandleList(size, startGroupId, endGroupId);
    return toDoList;
  }


  @Override
  public void handleGroupMemberInfo(GroupFundInfoMembers groupFundInfoMembers) throws Exception {
    if (groupFundInfoMembers == null || StringUtils.isEmpty(groupFundInfoMembers.getMembers())
        || groupFundInfoMembers.getGroupId() == null) {
      return;
    }

    String[] members = StringUtils.split(groupFundInfoMembers.getMembers(), ",");
    for (String member : members) {
      if (StringUtils.isEmpty(member)) {
        continue;
      }
      this.matchExistingUser(member);

    }

  }

  /**
   * 匹配成员
   * 
   * @param member
   * @throws Exception
   */
  private void matchExistingUser(String member) throws Exception {
    String[] strs = StringUtils.split(member, "-");
    if (strs == null || strs.length != 3) { // 不符合要求数据的不处理
      return;
    }
    String name = strs[0];
    String email = strs[1];
    String company = strs[2];
    if (StringUtils.isEmpty(name) || StringUtils.isEmpty(email)) {
      return;
    }
    name = name.toLowerCase().trim();
    email = email.toLowerCase().trim();
    Long psnId = null;

    List<Person> sysPsns = personDao.getPersonByNameAndEmail(name, email);
    if (CollectionUtils.isEmpty(sysPsns)) {
      return;
    }
    // 如果记录数有变，删除所有记录在重新插入；记录数相同则跳过
    List<IsisMatchedPrjMembers> list = isisPrjMemeberMatchDao.getMatchedByNameAndEmail(name, email);
    if (CollectionUtils.isNotEmpty(list)) {
      if (sysPsns.size() == list.size()) {
        return;
      } else {
        for (IsisMatchedPrjMembers matched : list) {
          isisPrjMemeberMatchDao.delete(matched);
        }
      }
    }
    // 保存记录
    for (Person psn : sysPsns) {
      IsisMatchedPrjMembers matched = new IsisMatchedPrjMembers();
      psnId = psn.getPersonId();

      if (psnId == null) {
        matched.setPsnId(999999L);
      } else {
        matched.setPsnId(psnId);
      }
      matched.setCompany(company);
      matched.setEmail(email);
      matched.setName(psn.getName());
      isisPrjMemeberMatchDao.save(matched);
    }

  }

  @Override
  public void saveOpResult(GroupFundInfoMembers groupFundInfoMembers, int i) {
    groupFundInfoMembers.setStatus(i);
    groupFundInfoMembersDao.save(groupFundInfoMembers);
  }

}
