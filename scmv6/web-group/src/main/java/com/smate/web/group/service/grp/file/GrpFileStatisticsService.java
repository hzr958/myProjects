package com.smate.web.group.service.grp.file;

import java.util.List;
import java.util.Map;

import com.smate.web.group.action.grp.form.GrpFileForm;

/**
 * @description 群组文件统计service
 * @author xiexing
 * @date 2019年5月15日
 */
public interface GrpFileStatisticsService {
  /**
   * 查询群组文件分享数
   * 
   * @return
   * @throws Exception
   */
  List<Map<String, Object>> queryShareStatistics() throws Exception;

  /**
   * 统计分享数
   */
  public void countShare(GrpFileForm grpFileForm) throws Exception;
}
