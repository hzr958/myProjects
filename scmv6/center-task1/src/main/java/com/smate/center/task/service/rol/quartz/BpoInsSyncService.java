package com.smate.center.task.service.rol.quartz;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.bpo.InsInfo;
import com.smate.center.task.model.bpo.TmpInsInfo;
import com.smate.center.task.model.common.InsReg;


/**
 * bpo 单位同步服务
 * 
 * @author hd
 *
 */
public interface BpoInsSyncService {
  /**
   * 增加新单位或更新单位信息.
   * 
   * @param info
   * @param isBpo
   * @return
   * @throws Exception
   */
  public Long addIns(InsInfo info) throws ServiceException;

  /**
   * 增加域名配置.
   * 
   * @param info
   * @throws ServiceException
   */
  public void addInsPortal(InsInfo info) throws ServiceException;

  /**
   * 查询待同步的单位数据
   * 
   * @param maxsize
   * @return
   * @throws ServiceException
   */
  public List<TmpInsInfo> findSyncInsList(int maxsize) throws ServiceException;

  /**
   * 保存单位同步状态
   * 
   * @param id
   * @param synFlag
   * @throws ServiceException
   */
  public void updateTmpInsInfo(TmpInsInfo info) throws ServiceException;

  /**
   * 获取单位性质
   * 
   * @param name
   * @return
   */
  public Long getNatureByName(String name) throws ServiceException;

  /**
   * 向ins_regoin表中添加数据
   * 
   * @param insId
   * @param prvId
   * @param cyId
   * @param disId
   * @return
   * @throws ServiceException
   */
  public InsReg addInsReg(Long insId, Long prvId, Long cyId, Long disId) throws ServiceException;

}
