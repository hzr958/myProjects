package com.smate.sie.center.open.service.file;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(rollbackFor = Exception.class)
public class SieGetFilePathServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private FileDownloadUrlService fileDownloadUrlService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    // 没有 参数校验 直接返回成功
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    // 获取参数并执行校验
    Object fileId = serviceData.get("fileId");
    if (fileId == null || !NumberUtils.isDigits(fileId.toString())) {
      logger.error("获取通过fileId不能为空，必须为数字，fileId=" + paramet.get("fileId"));
      temp = super.errorMap(ErrorInfo.SIE_FileID.toString(), paramet, "fileId不能为空,且为数字 ！");
      return temp;
    }
    Object fileType = serviceData.get("fileType");
    if (fileType == null || !NumberUtils.isDigits(fileType.toString())) {
      logger.error("获取通过fileType不能为空，必须为数字，fileType=" + paramet.get("fileType"));
      temp = super.errorMap(ErrorInfo.SIE_FileType.toString(), paramet, "fileType不能为空,且为数字 ！");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> infoMap = new HashMap<String, Object>();
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    try {
      Integer fileTypeNum = Integer.valueOf(serviceData.get("fileType").toString());
      infoMap.put("download_url", fileDownloadUrlService.getDownloadUrl(FileTypeEnum.valueOf(fileTypeNum),
          Long.valueOf(serviceData.get("fileId").toString()), null));
      dataList.add(infoMap);
      temp.put(OpenConsts.RESULT_DATA, dataList);
      temp.put(OpenConsts.RESULT_MSG, "获取文件地址成功");
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    } catch (Exception e) {
      // 文件不存在
      logger.warn("文件路径：{}", e.getMessage());
    }
    return temp;
  }

  enum ErrorInfo {
    SIE_FileID(100, "OPEN接口错误信息，fileId不能为空！"), SIE_FileType(101, "OPEN接口错误信息，fileType不能为空！");
    private Integer value;
    private String description;

    private ErrorInfo(Integer val, String desc) {
      this.value = val;
      this.description = desc;
    }

    public String getValue() {
      return this.value.toString();
    }

    public String getDescription() {
      return this.description;
    }

    public String toString() {
      return getDescription();
    }
  }

}
