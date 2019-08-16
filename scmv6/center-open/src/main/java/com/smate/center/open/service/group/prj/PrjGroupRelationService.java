package com.smate.center.open.service.group.prj;


/**
 * 项目-群组关系
 * 
 * @author xieyushou
 * 
 */

public interface PrjGroupRelationService {

  /**
   * 建立项目-群组关系
   * 
   * @param prjId
   * @param groupId
   * @throws ServiceException
   */
  public void buildPrjGroupRelation(Long prjId, Long groupId) throws Exception;

  /**
   * 根据项目id查找群组id
   * 
   * @param prjId
   * @return
   * @throws ServiceException
   */
  public Long findGroupIdByPrjId(Long prjId) throws Exception;


}
