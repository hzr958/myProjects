package com.smate.center.task.service.sns.quartz;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubIndexUrlDao;
import com.smate.center.task.dao.sns.quartz.GrpIndexUrlDao;
import com.smate.center.task.dao.sns.quartz.GrpPubIndexUrlDao;
import com.smate.center.task.dao.sns.quartz.PubIndexUrlDao;
import com.smate.center.task.model.pdwh.quartz.PdwhPubIndexUrl;
import com.smate.center.task.model.sns.quartz.GrpIndexUrl;
import com.smate.center.task.model.sns.quartz.GrpPubIndexUrl;
import com.smate.center.task.model.sns.quartz.PubIndexUrl;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.ServiceUtil;

@Service("shortUrlInitService")
@Transactional(rollbackFor = Exception.class)
public class ShortUrlInitServiceImpl implements ShortUrlInitService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private GrpIndexUrlDao grpIndexUrlDao;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private GrpPubIndexUrlDao grpPubIndexUrlDao;

  @Override
  public List<Long> getNeedInitPsnId(Integer index, Integer batchSize) throws Exception {
    List<Long> needInitPsnId = psnProfileUrlDao.getNeedInitPsnId(index, batchSize);
    return needInitPsnId;
  }

  @Override
  public List<Long> getNeedInitGroupId(Integer index, Integer batchSize) throws Exception {
    List<Long> needInitGrpId = grpIndexUrlDao.getNeedInitGrpId(index, batchSize);
    return needInitGrpId;
  }

  @Override
  public List<Long> getNeedInitAPubId(Integer index, Integer batchSize) throws Exception {
    List<Long> needInitGrpId = pubIndexUrlDao.getNeedInitPubId(index, batchSize);
    return needInitGrpId;
  };

  @Override
  public List<Long> getNeedInitSPubId(Integer index, Integer batchSize) throws Exception {
    List<Long> needInitGrpId = pdwhPubIndexUrlDao.getNeedInitPubId(index, batchSize);
    return needInitGrpId;
  };

  @Override
  public List<GrpPubIndexUrl> getNeedInitBPubId(int index, int batchSize) throws Exception {
    return grpPubIndexUrlDao.getNeedInitBPubId(index, batchSize);
  }

  @Override
  public List<Long> getNeedInitATPubId(int index, int batchSize) throws Exception {
    List<Long> needInitGrpId = pubIndexUrlDao.getLongNeedInitPubId(index, batchSize);
    return needInitGrpId;
  }

  /**
   * 构建参数
   * 
   * @param Id
   * @param type
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> buildParameters(Long BId, Long Id, String type) {
    Map<String, Object> map = new HashedMap();
    Map<String, Object> dataMap = new HashedMap();
    Map<String, Object> shortUrlParametMap = new HashedMap();

    map.put("openid", "99999999");
    map.put("token", "00000000sht22url");

    dataMap.put("createPsnId", "0");
    dataMap.put("type", type);
    if (ShortUrlConst.P_TYPE.equals(type)) {
      shortUrlParametMap.put("des3PsnId", ServiceUtil.encodeToDes3(Id.toString()));
    } else if (ShortUrlConst.G_TYPE.equals(type)) {
      shortUrlParametMap.put("des3GrpId", ServiceUtil.encodeToDes3(Id.toString()));
    } else if (ShortUrlConst.A_TYPE.equals(type) || ShortUrlConst.S_TYPE.equals(type)) {
      shortUrlParametMap.put("des3PubId", ServiceUtil.encodeToDes3(Id.toString()));
    } else if (ShortUrlConst.AT_TYPE.equals(type) || ShortUrlConst.AT_TYPE.equals(type)) {
      shortUrlParametMap.put("des3PubId", ServiceUtil.encodeToDes3(Id.toString()));
    } else if (ShortUrlConst.B_TYPE.equals(type)) {
      shortUrlParametMap.put("des3GrpId", ServiceUtil.encodeToDes3(BId.toString()));
      shortUrlParametMap.put("des3PubId", ServiceUtil.encodeToDes3(Id.toString()));

    }

    dataMap.put("shortUrlParamet", JacksonUtils.mapToJsonStr(shortUrlParametMap));
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));

    return map;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void initUrlData(Long BId, Long Id, String type) throws Exception {
    String shortUrl = "";
    // 构建参数
    Map<String, Object> parameters = this.buildParameters(BId, Id, type);
    // 访问Open系统接口获取ShortUrl
    Object obj = restTemplate.postForObject(this.SERVER_URL, parameters, Object.class);
    // 接口返回数据处理
    Map<String, Object> objMap = JacksonUtils.jsonToMap(obj.toString());
    // 获取短地址值
    if (objMap.get("result") != null) {
      List<Map<String, Object>> list = (List<Map<String, Object>>) objMap.get("result");
      if (list != null && list.size() > 0 && list.get(0).get("shortUrl") != null) {
        shortUrl = list.get(0).get("shortUrl").toString();
      }
    }

    if (ShortUrlConst.P_TYPE.equals(type)) {
      this.savePsnUrl(Id, shortUrl);

    } else if (ShortUrlConst.G_TYPE.equals(type)) {
      this.saveGroupUrl(Id, shortUrl);

    } else if (ShortUrlConst.A_TYPE.equals(type)) {
      this.saveAPubUrl(Id, shortUrl);

    } else if (ShortUrlConst.AT_TYPE.equals(type)) {
      this.saveATPubUrl(Id, shortUrl);

    } else if (ShortUrlConst.S_TYPE.equals(type)) {
      this.saveSPubUrl(Id, shortUrl);

    } else if (ShortUrlConst.B_TYPE.equals(type)) {
      this.saveBPubUrl(BId, Id, shortUrl);

    }
  }

  /**
   * 保存PSN短地址
   * 
   * @param psnId
   * @param shortUrl
   */
  public void savePsnUrl(Long psnId, String shortUrl) {
    PsnProfileUrl psnProfileUrl;
    try {
      psnProfileUrl = psnProfileUrlDao.find(psnId);
      psnProfileUrl.setUpdateDate(new Date());
      psnProfileUrl.setPsnIndexUrl(shortUrl);
      psnProfileUrlDao.save(psnProfileUrl);
    } catch (Exception e) {
      logger.error("保存PSN短地址出错！psnId=" + psnId, e);
    }

  };

  /**
   * 保存Grp短地址
   * 
   * @param grpId
   * @param shortUrl
   */
  public void saveGroupUrl(Long grpId, String shortUrl) {
    GrpIndexUrl grpIndexUrl;
    try {
      grpIndexUrl = grpIndexUrlDao.find(grpId);
      grpIndexUrl.setGrpIndexUrl(shortUrl);
      grpIndexUrl.setUpdateDate(new Date());
      grpIndexUrlDao.save(grpIndexUrl);
    } catch (Exception e) {
      logger.error("保存Grp短地址,grpId=" + grpId, e);
    }

  };

  /**
   * 保存Pub短地址(个人)
   * 
   * @param pubId
   * @param shortUrl
   */
  public void saveAPubUrl(Long pubId, String shortUrl) {
    try {
      PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pubId);
      pubIndexUrl.setPubIndexUrl(shortUrl);
      pubIndexUrl.setUpdateDate(new Date());
      pubIndexUrlDao.save(pubIndexUrl);
    } catch (Exception e) {
      logger.error("保存个人成果短地址出错,PubId=" + pubId);
    }

  }

  private void saveATPubUrl(Long pubId, String shortUrl) {
    try {
      PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pubId);
      pubIndexUrl.setPubLongIndexUrl(shortUrl);
      pubIndexUrl.setUpdateDate(new Date());
      pubIndexUrlDao.save(pubIndexUrl);
    } catch (Exception e) {
      logger.error("保存个人成果32位短地址出错,PubId=" + pubId);
    }
  }

  /**
   * 保存Pub短地址(基准库)
   * 
   * @param pubId
   * @param shortUrl
   */
  public void saveSPubUrl(Long pubId, String shortUrl) {
    try {
      PdwhPubIndexUrl pubIndexUrl = pdwhPubIndexUrlDao.get(pubId);
      pubIndexUrl.setPubIndexUrl(shortUrl);
      pubIndexUrl.setUpdateDate(new Date());
      pdwhPubIndexUrlDao.save(pubIndexUrl);
    } catch (Exception e) {
      logger.error("保存PDWH成果短地址出错,PubId=" + pubId);
    }

  }

  /**
   * 保存Pub短地址(群组)
   * 
   * @param grpId
   * @param pubId
   * @param shortUrl
   */
  public void saveBPubUrl(Long grpId, Long pubId, String shortUrl) {
    try {
      GrpPubIndexUrl gpu = grpPubIndexUrlDao.findByGrpIdAndPubId(grpId, pubId);
      gpu.setPubIndexUrl(shortUrl);
      gpu.setUpdateDate(new Date());
      grpPubIndexUrlDao.save(gpu);
    } catch (Exception e) {
      logger.error("保存群组成果短地址出错,PubId=" + pubId + "grpId:" + grpId, e);
    }

  }

  @Override
  public void insertData() {
    /*
     * // 1.同步人员数据 if (psnProfileUrlDao.isNeedInsertData()) { psnProfileUrlDao.insertInitData(); }
     * 
     * // 2.同步snspub if (pubIndexUrlDao.isNeedInsertData()) { pubIndexUrlDao.insertInitData(); }
     */

  }

}
