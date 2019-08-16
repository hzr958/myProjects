package com.smate.web.group.service.group;



import com.smate.web.group.exception.GroupException;
import com.smate.web.group.form.GroupInfoForm;


/**
 * 群组信息接口
 * 
 * @author zk
 *
 */
public interface GroupInfoService {



  /**
   * 获取群组头部主要的信息
   * 
   * @return
   */
  void getGroupMain(GroupInfoForm form);

  /**
   * 获取群组简介模块数据
   * 
   * @return
   */
  void getGgroupIntroMain(GroupInfoForm form);

  /**
   * 获取群组文件
   * 
   * @param form
   * @throws GroupException
   */
  void findGroupFile(GroupInfoForm form) throws GroupException;

  /**
   * 获取当前人的群组角色
   * 
   * @param form
   * @throws GroupException
   */
  void getCurrentPsnGroupRole(GroupInfoForm form) throws GroupException;

  /**
   * 获取群组成员列表
   * 
   * @param form
   * @throws GroupException
   */
  void getMemberList(GroupInfoForm form) throws GroupException;

  /**
   * 获取群组成员待审核列表
   * 
   * @param form
   * @throws GroupException
   */
  void getPendingList(GroupInfoForm form) throws GroupException;

  /**
   * 根据groupCode获取Groupid
   * 
   * @param form
   * @throws GroupException
   */
  Long groupCodeGetGroupId(GroupInfoForm form) throws GroupException;

}
