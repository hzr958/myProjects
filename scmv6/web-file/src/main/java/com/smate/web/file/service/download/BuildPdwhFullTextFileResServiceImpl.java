package com.smate.web.file.service.download;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.statistics.dao.DownloadCollectStatisticsDao;
import com.smate.core.base.statistics.model.DownloadCollectStatistics;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.file.consts.FileConsts;
import com.smate.web.file.form.FileDownloadForm;
import com.smate.web.file.utils.FileNamePathParseUtil;

/**
 * 基准库成果全文下载
 * 
 * @author houchuanjie
 *
 */
public class BuildPdwhFullTextFileResServiceImpl extends BuildResServiceBase {


  @Autowired
  private DownloadCollectStatisticsDao downloadCollectStatisticsDao;

  @Override
  public void check(FileDownloadForm form) throws Exception {
    // 只有pubId 的时候 ，先查询fileId
    if (form.getPubId() != null) {
      String SERVER_URL = domainscm + FileConsts.QUERY_PUB_URL;
      PubQueryDTO pubQueryDTO = new PubQueryDTO();
      pubQueryDTO.setSearchPubId(form.getPubId());;
      pubQueryDTO.setServiceType(FileConsts.QUERY_PDWH_PUB);
      Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
      if (result.get("status").equals("success")) {
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("resultList");
        if (resultList != null && resultList.size() > 0) {
          Object fullTextFieId = resultList.get(0).get("fullTextFieId");
          Object fullTextPermission = resultList.get(0).get("fullTextPermission");
          if (fullTextFieId != null) {
            // 防止文件id为空
            form.setFileId(NumberUtils.toLong(fullTextFieId.toString()));
          }

        }
      }
    }
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
      // 更新solr
      Long pdwhPubId = form.getPubId();
      HashMap<String, Object> params = new HashMap<String, Object>();
      if (pdwhPubId != null && pdwhPubId != 0L) {
        params.put("des3PubId", Des3Utils.encodeToDes3(pdwhPubId + ""));
        restTemplate.postForObject(domainscm + "/data/pub/sorlupdate", params, Map.class);
      }
    } catch (Exception e) {
      logger.error("保存基准库下载或收藏记录出错！PsnId=" + form.getCurrentPsnId() + " dcPsnId=" + 0 + " actionKey=" + form.getFileId()
          + " actionType=1", e);
    }
  }

}
