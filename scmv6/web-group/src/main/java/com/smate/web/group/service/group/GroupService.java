package com.smate.web.group.service.group;

import java.util.List;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.web.group.form.GroupPsnForm;
import com.smate.web.group.model.group.GroupPsn;
import com.smate.web.group.model.group.invit.GroupInvitePsn;


/**
 * 群组服务类
 * 
 * @author tsz
 *
 */
public interface GroupService {

  final String GROUP_CATEGORY = "group";

  /**
   * 判断群组 是否存在
   * 
   * @param groupId
   * @return
   */
  public Boolean checkGroup(Long groupId);

  /**
   * 获取群组 显示的详细信息
   * 
   * @param groupId
   * @param currentPsnId
   * @return
   */
  public void getGroupPsn(GroupPsnForm form);

  /**
   * 创建群组并保存第三方系统项目信息
   * 
   * @param form
   * @throws Exception
   */
  public boolean createGroupInfo(GroupPsnForm form) throws Exception;

  public List<ConstDictionary> findConstGroupList() throws Exception;

  /**
   * 获取构造好的groupPsn对象
   * 
   * @param groupId
   * @param nodeId
   * @return
   * @throws Exception
   */
  public GroupPsn findMyGroup(Long groupId, Integer nodeId) throws Exception;

  /**
   * 实体类到表单.
   * 
   * @param dataGroupPsn
   * @return
   */
  GroupPsnForm modelToForm(GroupPsn dataGroupPsn);

  /**
   * 获取学科名称.
   * 
   * @param disciplineList
   * 
   * @return
   * 
   * @throws ServiceException
   */
  public String findDisciplineNameList(List<Long> disciplineList) throws Exception;

  /**
   * 获取群组个人信息.
   * 
   * @param groupId
   * @return
   * @throws ServiceException
   */
  public GroupInvitePsn findGroupInvitePsn(Long groupId) throws Exception;

  /**
   * 获取群组个人信息.
   * 
   * @param groupId
   * @return
   * @throws ServiceException
   */
  public GroupInvitePsn findGroupInvitePsn(Long groupId, boolean isActivity) throws Exception;

  /**
   * 表单到实体类.
   * 
   * @param groupPsn
   * @return
   */
  public GroupPsn formToModel(GroupPsnForm groupPsn);

  public void updateGroupPsn(GroupPsn groupPsn) throws Exception;

}
