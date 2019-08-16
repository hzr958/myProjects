package com.smate.web.group.service.group.pub;

import java.util.List;
import java.util.Map;

import com.smate.web.group.form.GroupPsnForm;
import com.smate.web.group.model.group.pub.ConstPubType;
import com.smate.web.group.model.group.pub.GroupFolder;
import com.smate.web.group.model.group.pub.GroupPubs;


/**
 * 群组成果服务类
 * 
 * @author tsz
 *
 */
public interface GroupPublicationService {

  /**
   * 构建群组成果页面数据接口
   */
  public void show(GroupPsnForm form);

  /**
   * 构建群组成果页面数据接口(改造)
   */
  void showGroup(GroupPsnForm form);

  List<GroupFolder> findGroupFolderList(Long groupId, String folderType);

  List<Map<String, String>> findJsonGroupFolderList(List<GroupFolder> groupFolderList);

  List<ConstPubType> getAll();

  Integer countGroupPubs(Long groupId, Integer pubType);

  List<Map<String, Object>> findPubRecordMap(Long groupId);

  Map<String, Object> queryPubYearsMap(Long groupId);

  /**
   * ajax获取群组成员成果信息列表
   * 
   * @param form
   * @return
   */
  void ajaxGroupMembers(GroupPsnForm form);

  /**
   * ajax获取群组成员成果信息列表(2016-10-10群组改造)
   * 
   * @param form
   * @return
   */
  void ajaxGroupPubMembers(GroupPsnForm form);

  public List<GroupPubs> getGroupPubsList(Long groupId);

  public void setRelevance(GroupPsnForm form);

  /**
   * 动态中加载群组成果
   * 
   * @param form
   */
  public void showGroupPubForDyn(GroupPsnForm form) throws Exception;
}
