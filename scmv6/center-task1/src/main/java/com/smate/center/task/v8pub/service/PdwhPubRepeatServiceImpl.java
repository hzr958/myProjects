package com.smate.center.task.v8pub.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.dao.pdwh.PdwhDataTaskDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubRepeatRecordDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubRepeatRecordPO;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;


/**
 * 基准库重复成果分组
 * 
 * @author YJ
 *
 *         2019年6月17日
 */

@Service("pdwhPubRepeatService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubRepeatServiceImpl implements PdwhPubRepeatService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String scmDomain;
  @Autowired
  private PdwhDataTaskDAO pdwhDataTaskDAO;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PdwhPubRepeatRecordDAO pdwhPubRepeatRecordDAO;

  @Override
  public List<PdwhDataTaskPO> findPdwhId(Integer size, Long startId, Long endId) throws ServiceException {
    return pdwhDataTaskDAO.findPdwhPubId(startId, endId, size);
  }

  @Override
  public void save(PdwhDataTaskPO pubData) throws ServiceException {
    pdwhDataTaskDAO.save(pubData);
  }

  @Override
  public void dealWithRepeatPub(PdwhDataTaskPO pubData) throws ServiceException {
    try {
      PubPdwhDetailDOM detailDom = pubPdwhDetailDAO.findById(pubData.getPubId());
      if (detailDom != null) {
        // 进行查重
        Map<String, Object> dupMap = buildDupMap(detailDom);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(dupMap), headers);
        String dupUrl = scmDomain + V8pubQueryPubConst.PUB_DUPCHECK_URL;
        String res = restTemplate.postForObject(dupUrl, entity, String.class);
        // 处理查重结果
        saveDupResult(pubData, res);
      }
    } catch (Exception e) {
      logger.error("处理基准库成果分组出错！", e);
      throw new ServiceException(e);
    }
  }

  private void saveDupResult(PdwhDataTaskPO pubData, String res) {
    // 先删除记录
    Long pubId = pubData.getPubId();
    pdwhPubRepeatRecordDAO.deleteByPdwhPubId(pubId);
    Map<Object, Object> dupMap = JacksonUtils.jsonToMap(res);
    if ("SUCCESS".equals(dupMap.get("status").toString())) {
      if (dupMap.get("msgList") != null) {
        String[] dupPubIds = dupMap.get("msgList").toString().split(",");
        for (String dupPubId : dupPubIds) {
          if (dupPubId.equals(String.valueOf(pubId)))
            continue;
          // 保存重复成果数据
          Long dPubId = NumberUtils.toLong(dupPubId);
          if (dPubId != null && dPubId != 0L) {
            PdwhPubRepeatRecordPO repeatRecord = pdwhPubRepeatRecordDAO.findRecord(dPubId, pubId);
            if (repeatRecord == null) {
              repeatRecord = new PdwhPubRepeatRecordPO();
              repeatRecord.setPdwhPubId(pubId);
              repeatRecord.setDupPubId(dPubId);
              pdwhPubRepeatRecordDAO.save(repeatRecord);
            }
          }
        }
      } else {
        pubData.setError("无重复成果");
      }
    }

  }

  private Map<String, Object> buildDupMap(PubPdwhDetailDOM detailDom) {
    String publishDate = detailDom.getPublishDate();
    Map<String, Object> dupMap = new HashMap<String, Object>();
    dupMap.put("pubGener", 3);
    dupMap.put("title", detailDom.getTitle());
    dupMap.put("pubType", detailDom.getPubType());
    dupMap.put("doi", detailDom.getDoi());
    dupMap.put("srcDbId", detailDom.getSrcDbId());
    dupMap.put("sourceId", detailDom.getSourceId());
    if (5 == detailDom.getPubType()) {
      PatentInfoBean infoBean = (PatentInfoBean) detailDom.getTypeInfo();
      if (infoBean != null) {
        dupMap.put("applicationNo", infoBean.getApplicationNo());
        dupMap.put("publicationOpenNo", infoBean.getPublicationOpenNo());
        Integer status = infoBean.getStatus();
        if (status != null && status == 1) {
          // 授权状态
          publishDate = infoBean.getStartDate();
        } else {
          // 申请状态
          publishDate = infoBean.getApplicationDate();
        }
      }
    }
    if (12 == detailDom.getPubType()) {
      StandardInfoBean infoBean = (StandardInfoBean) detailDom.getTypeInfo();
      if (infoBean != null) {
        dupMap.put("standardNo", infoBean.getStandardNo());
      }
    }
    if (13 == detailDom.getPubType()) {
      SoftwareCopyrightBean infoBean = (SoftwareCopyrightBean) detailDom.getTypeInfo();
      if (infoBean != null) {
        dupMap.put("registerNo", infoBean.getRegisterNo());
      }
    }
    dupMap.put("pubYear", PubDetailVoUtil.parseDateForYear(publishDate));
    return dupMap;
  }

}
