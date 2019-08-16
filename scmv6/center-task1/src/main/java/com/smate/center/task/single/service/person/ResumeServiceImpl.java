package com.smate.center.task.single.service.person;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.ResumeDao;
import com.smate.core.base.utils.exception.SysServiceException;

@Transactional(rollbackFor = Exception.class)
@Service("resumeService")
public class ResumeServiceImpl implements ResumeService {

  /**
   * 
   */
  private static final long serialVersionUID = 8417560315987705264L;

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ResumeDao resumeDao;

  @SuppressWarnings("rawtypes")
  @Override
  public String getWorkAuthority(Long workId) throws SysServiceException {
    try {
      List arList = resumeDao.findWorkAuthorityList(workId);
      return getAuthExtracted(arList);
    } catch (Exception e) {
      logger.error("获取工作经历权限出错", e);
    }
    return "0";
  }

  @SuppressWarnings("rawtypes")
  private String getAuthExtracted(List arList) {
    String authority = "0";
    try {
      if (CollectionUtils.isNotEmpty(arList)) {
        int[] arr = new int[arList.size()];
        for (int i = 0; i < arList.size(); i++) {
          Map map = (Map) arList.get(i);
          arr[i] = NumberUtils.toInt(ObjectUtils.toString(map.get("TMP_ID")));
        }
        if (ArrayUtils.contains(arr, 2)) {// 公开
          authority = "0";
        } else if (ArrayUtils.contains(arr, 3)) {// 好友
          authority = "1";
        } else if (ArrayUtils.contains(arr, 5)) {// 本人
          authority = "2";
        }
      }
    } catch (Exception e) {
      logger.error("", e);
    }
    return authority;
  }

}
