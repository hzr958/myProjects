package com.smate.web.file.service.download;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.statistics.dao.DownloadCollectStatisticsDao;
import com.smate.core.base.statistics.model.DownloadCollectStatistics;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.file.form.FileDownloadForm;
import com.smate.web.file.utils.FileNamePathParseUtil;

/**
 * SIE调用基准库成果全文下载
 * 
 * @author 叶星源
 *
 */
public class BuildSiePdwhFullTextFileResServiceImpl extends BuildResServiceBase {

  @Autowired
  private DownloadCollectStatisticsDao downloadCollectStatisticsDao; // sns库

  @Override
  public void check(FileDownloadForm form) throws Exception {
    if (form.getFileId() != null) {
      super.checkArchiveFile(form.getFileId(), form);
    }
  }

  @Override
  public void buildResUrl(FileDownloadForm form) throws Exception {
    // 构建文件真实地址
    // 构建下载资源
    String url = "/" + basicPath + FileNamePathParseUtil.parseFileNameDir(form.getArchiveFile().getFilePath());
    form.setResPath(url);
  }

  @Override
  public void extend(FileDownloadForm form) throws Exception {
    try {
      Date nowDate = new Date();
      long formateDate = DateUtils.getDateTime(nowDate);
      DownloadCollectStatistics dcs =
          downloadCollectStatisticsDao.findRecord(0L, form.getFileId(), 1, form.getCurrentPsnId(), formateDate);
      if (dcs == null) {
        dcs = new DownloadCollectStatistics();
        dcs.setActionKey(form.getFileId());
        dcs.setActionType(1);
        dcs.setDcdDate(nowDate);
        dcs.setDcPsnId(form.getCurrentPsnId());
        dcs.setFormateDate(formateDate);
        dcs.setPsnId(0L);
        dcs.setIp(Struts2Utils.getRemoteAddr());
      } else {
        dcs.setDcdDate(nowDate);
        dcs.setFormateDate(formateDate);
      }
      dcs.setDcount(dcs.getDcount() + 1l);// 下载次数
      downloadCollectStatisticsDao.save(dcs);
    } catch (Exception e) {
      logger.error("保存基准库下载或收藏记录出错！PsnId=" + form.getCurrentPsnId() + " dcPsnId=" + 0 + " actionKey=" + form.getFileId()
          + " actionType=1", e);
    }
  }

}
