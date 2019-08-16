package com.smate.web.v8pub.service.statistic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.statistics.dao.DownloadCollectStatisticsDao;
import com.smate.core.base.statistics.model.DownloadCollectStatistics;
import com.smate.core.base.utils.dao.security.SysUserLoginDao;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.web.v8pub.exception.ServiceException;

/**
 * 下载收藏统计
 * 
 * @author aijiangbin
 * @date 2018年8月30日
 */
@Service("downloadCollectStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class DownloadCollectStatisticsServiceImpl implements DownloadCollectStatisticsService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public static final int RES_TYPE_PUB = 1;// 成果
  public static final int RES_TYPE_REF = 2;// 文献
  public static final int RES_TYPE_PRJ = 4;// 项目
  public final static String DOWNLOAD_COUNT = "downloadCount"; // 下载统计
  public final static String PUB_IMPORT = "pubImport"; // 导入成果库
  public final static String REF_IMPORT = "refImport"; // 导入文献库
  private static List addRecordType = new ArrayList();

  static {
    addRecordType.add(RES_TYPE_PUB);
    addRecordType.add(RES_TYPE_REF);
    addRecordType.add(RES_TYPE_PRJ);
  }
  @Autowired
  private DownloadCollectStatisticsDao downloadCollectStatisticsDao;

  @Override
  public void addRecord(Long psnId, Long actionKey, Integer actionType, Long dcPsnId, String countType,
      Integer resNodeId, String ip) throws ServiceException {
    try {
      if (!addRecordType.contains(actionType)) {
        logger.warn("不需要记录的类型，actionKey=" + actionKey + ",actionType=" + actionType);
        return;
      }
      // 通过psnId 查找用户登录时的Ip
      // ip = Struts2Utils.getRemoteAddr();
      ip = "";
      Date nowDate = new Date();
      long formateDate = DateUtils.getDateTime(nowDate);
      DownloadCollectStatistics dcs =
          downloadCollectStatisticsDao.findRecord(psnId, actionKey, actionType, dcPsnId, formateDate);
      if (dcs == null) {
        dcs = new DownloadCollectStatistics();
        dcs.setActionKey(actionKey);
        dcs.setActionType(actionType);
        dcs.setDcdDate(nowDate);
        dcs.setDcPsnId(dcPsnId);
        dcs.setFormateDate(formateDate);
        dcs.setPsnId(psnId);
      } else {
        dcs.setDcdDate(nowDate);
        dcs.setFormateDate(formateDate);
      }
      dcs.setIp(ip);
      if (PUB_IMPORT.equals(countType)) {
        dcs.setCpubCount(dcs.getCpubCount() + 1l);
      }
      if (REF_IMPORT.equals(countType)) {
        dcs.setCrefCount(dcs.getCrefCount() + 1l);
      }
      if (DOWNLOAD_COUNT.equals(countType)) {
        dcs.setDcount(dcs.getDcount() + 1l);
      }
      downloadCollectStatisticsDao.save(dcs);
    } catch (Exception e) {
      logger.error("保存下载或收藏记录出错！PsnId=" + psnId + " dcPsnId=" + dcPsnId + " actionKey=" + actionKey + " actionType"
          + actionType + " countType=" + countType, e);
      throw new ServiceException(e);
    }
  }
}
