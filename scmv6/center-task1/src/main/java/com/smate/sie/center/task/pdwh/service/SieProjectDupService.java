package com.smate.sie.center.task.pdwh.service;

import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.core.base.utils.json.prj.ProjectJsonDTO;

/**
 * 
 * @author yxs
 * @descript 查重接口
 */
public interface SieProjectDupService {

  /**
   * 列出有相同标题的一个项目.
   * 
   * @param zhTitle
   * @param enTitle
   * @return
   */
  public List<Long> getDupPrjId(String zhTitle, String enTitle, Long ownerInsId, String externalNo, String prjFromName);

  public Map<Integer, List<Long>> getDupPrjByImportPrj(ProjectJsonDTO prj, Long insId) throws SysServiceException;
}
