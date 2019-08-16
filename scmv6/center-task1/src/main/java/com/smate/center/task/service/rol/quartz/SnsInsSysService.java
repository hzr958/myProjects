package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.bpo.InsInfo;
import com.smate.center.task.model.common.InsReg;
import com.smate.core.base.utils.model.security.Person;

/**
 * sns 单位同步服务
 * 
 * @author hd
 *
 */
public interface SnsInsSysService {
  /**
   * 添加单位信息
   * 
   * @param info
   * @return
   * @throws ServiceException
   */
  Long addIns(InsInfo info) throws ServiceException;

  /**
   * 添加单位域名
   * 
   * @param info
   * @throws ServiceException
   */
  void addInsPortal(InsInfo info) throws ServiceException;

  /**
   * 获取新的psnId
   * 
   * @return
   * @throws ServiceException
   */
  Long findNewPsnId() throws ServiceException;

  /**
   * 向person表中添加数据
   * 
   * @param info
   * @return
   * @throws ServiceException
   */
  Long addPersonSns(InsInfo info) throws ServiceException;

  /**
   * 增加人员角色表
   * 
   * @param psnId
   * @param roleId
   * @param insId
   * @throws ServiceException
   */
  void addSysUserRole(Long psnId, Long roleId, Long insId) throws ServiceException;

  /**
   * 增加人员与单位的关系
   * 
   * @param info
   * @throws ServiceException
   */
  void addPsnIns(InsInfo info) throws ServiceException;

  /**
   * 同步失败，清除脏数据
   * 
   * @param psnId
   * @param insId
   */
  void deleteSns(Long psnId, Long insId);

  /**
   * 查询region_id
   * 
   * @param regionName
   * @return
   * @throws ServiceException
   */
  Long findRegionId(String regionName) throws ServiceException;

  /**
   * 向ins_regoin表中添加数据
   * 
   * @param insId
   * @param prvId
   * @param cyId
   * @param disId
   * @throws ServiceException
   */
  InsReg addInsReg(Long insId, Long prvId, Long cyId, Long disId) throws ServiceException;

  /**
   * 向psn_sid表中添加数据
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Long addPsnSid(Long psnId) throws ServiceException;

  /**
   * 获取person
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Person getPersonById(Long psnId) throws ServiceException;

}
