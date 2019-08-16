package com.smate.web.prj.service.project.fileimport;

import com.smate.web.prj.form.fileimport.PrjInfoDTO;

import java.io.File;
import java.util.Map;

/**
 * 解析文件service
 */
public interface ExtractFileService {

  /**
   *
   *  resutlMap.put("list", "");   把文件提取成  list列表对象
   *  resutlMap.put("warnmsg", "");   错误信息
   *  resutlMap.put("count", 0);      条数
   * @return
   */
  public Map<String ,Object> extractFile(File file , String sourceFileFileName);


  /**
   * 构建单个 项目信息的xml
   * @param prjInfo
   * @return
   */
  public String buildPrjXmlData(PrjInfoDTO prjInfo);
}
