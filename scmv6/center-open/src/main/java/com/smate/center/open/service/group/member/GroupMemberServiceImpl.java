package com.smate.center.open.service.group.member;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.group.GroupMemberDao;
import com.smate.center.open.model.group.GroupInvitePsn;
import com.smate.center.open.model.group.GroupMember;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 群组成员相关信息检索的业务逻辑实现类
 * 
 * @author lhd
 *
 */
@Service("groupMemberService")
@Transactional(rollbackFor = Exception.class)
public class GroupMemberServiceImpl implements GroupMemberService {
  @Autowired
  private GroupMemberDao groupMemberDao;

  /**
   * 构建人员信息到群组人员关系表
   * 
   * @author lhd
   */
  @Override
  public GroupInvitePsn buildGroupInvitePsn(GroupInvitePsn groupInvitePsn) throws Exception {
    // 根据ID检索对应的人员记录
    GroupMember groupMember = groupMemberDao.getGroupMemberInfo(groupInvitePsn.getCreatePsnId());
    if (groupMember != null) {
      groupInvitePsn.setAvatars(groupMember.getAvatars());// 头像图片地址
      groupInvitePsn.setEmail(groupMember.getEmail());
      groupInvitePsn.setQqNo(groupMember.getQqNo());
      groupInvitePsn.setTel(groupMember.getTel());
      groupInvitePsn.setMobile(groupMember.getMobile());
      groupInvitePsn.setMsnNo(groupMember.getMsnNo());
      groupInvitePsn.setPosition(groupMember.getPosition()); // 职称.
      groupInvitePsn.setTitolo(groupMember.getTitolo()); // 头衔.
      String psnName = groupMember.getPsnName();
      // 如果人员名称为空则拼装人员的first_name和last_name字段值为人员名称.
      if (StringUtils.isBlank(psnName)) {
        psnName = groupMember.getFirstName() + " " + groupMember.getLastName();
      }
      // 如果人员名字包含中文，则解析其拼音内容并赋值给firstName和lastName.
      if (StringUtils.isNotBlank(psnName)) {
        String firstName = "";
        String lastName = "";
        String lang = ServiceUtil.getStrLang(psnName);// 获取psnName的语言版本
        if (lang.equals(ServiceUtil.ZH_LANG)) {// 语言版本为中文.
          Map<String, String> nameMap = ServiceUtil.parsePinYin(psnName);
          if (nameMap != null && nameMap.size() > 0) {
            firstName = nameMap.get("firstName");
            lastName = nameMap.get("lastName");
          }
        } else {
          firstName = groupMember.getFirstName();
          lastName = groupMember.getLastName();
        }
        groupInvitePsn.setPsnFirstName(firstName); // first name
        groupInvitePsn.setPsnLastName(lastName); // last name
      }
      groupInvitePsn.setPsnName(psnName); // 中文名字
    }
    return groupInvitePsn;
  }


}
