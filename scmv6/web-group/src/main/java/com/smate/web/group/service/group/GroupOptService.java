package com.smate.web.group.service.group;

import java.util.List;

import com.smate.web.group.exception.GroupException;
import com.smate.web.group.form.GroupOptForm;
import com.smate.web.group.model.group.GroupBaseInfo;

/**
 * 群组操作服务类
 * 
 * @author zk
 *
 */
public interface GroupOptService {

  /**
   * 添加群组文件
   * 
   * @param form
   * @return
   */
  Boolean addGroupFile(GroupOptForm form);

  /**
   * 删除群组文件
   * 
   * @param form
   * @return
   * @throws GroupException
   */
  Boolean deleteGroupFile(GroupOptForm form) throws GroupException;

  /**
   * 删除群组成员
   * 
   * @param form
   * @return
   * @throws GroupException
   */
  Boolean deleteGroupMember(GroupOptForm form) throws GroupException;

  /**
   * 修改文件信息
   * 
   * @param form
   * @return
   * @throws GroupException
   */
  Boolean editGroupFile(GroupOptForm form) throws GroupException;

  /**
   * 判断是否为我的群组文件
   * 
   * @author lhd
   * @param form
   * @return
   * @throws GroupException
   */
  Boolean isMyGroupFile(GroupOptForm form) throws GroupException;

  /**
   * 判断是否为我的群组文件
   * 
   * @author lhd
   * @param form
   * @return
   * @throws GroupException
   */
  List<GroupBaseInfo> getGroupNames(GroupOptForm form) throws GroupException;


  /**
   * 获取当前操作人 与群组的关系
   * 
   */
  Integer getRelationWithGroup(Long psnId, Long groupId) throws GroupException;

}
