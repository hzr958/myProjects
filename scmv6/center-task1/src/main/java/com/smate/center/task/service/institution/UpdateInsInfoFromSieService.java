package com.smate.center.task.service.institution;

import java.util.List;

import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.rol.quartz.InsStatisticsRol;
import com.smate.center.task.model.rol.quartz.InstitutionRol;

/**
 * 从SIE更新机构信息到SNS接口
 * 
 * @author wsn
 *
 */
public interface UpdateInsInfoFromSieService {

  /**
   * 批量获取ROL机构统计信息
   * 
   * @param Size
   * @param startInsId
   * @param endInsId
   * @return
   * @throws TaskException
   */
  public List<InsStatisticsRol> findInsStatisticsRolList(Integer size, Long startInsId, Long endInsId)
      throws TaskException;

  /**
   * 批量获取ROL机构信息
   * 
   * @param Size
   * @param startInsId
   * @param endInsId
   * @return
   * @throws TaskException
   */
  public List<InstitutionRol> findInstitutionRolList(Integer size, Long startInsId, Long endInsId) throws TaskException;

  /**
   * 更新机构信息
   * 
   * @param insList
   * @throws TaskException
   */
  public void updateInsInfoFromROL(InstitutionRol insRol) throws TaskException;
}
