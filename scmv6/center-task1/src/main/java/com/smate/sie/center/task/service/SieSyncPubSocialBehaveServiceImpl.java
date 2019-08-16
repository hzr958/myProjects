package com.smate.sie.center.task.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.model.sns.pub.ConstRegion;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.dao.consts.SieConstRegionDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.consts.SieConstRegion;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.sie.center.task.dao.SieCityIpDao;
import com.smate.sie.center.task.dao.SiePubStatDao;
import com.smate.sie.center.task.dao.SnsPdwhPubLikeDAO;
import com.smate.sie.center.task.dao.SnsPdwhPubShareDAO;
import com.smate.sie.center.task.dao.SnsPdwhPubViewDAO;
import com.smate.sie.center.task.dao.SnsPubLikeDAO;
import com.smate.sie.center.task.dao.SnsPubShareDAO;
import com.smate.sie.center.task.dao.SnsPubViewDAO;
import com.smate.sie.center.task.model.PubPdwhSieRelation;
import com.smate.sie.center.task.model.PubStat;
import com.smate.sie.center.task.model.SieCityIp;
import com.smate.sie.center.task.otherlibrary.model.SnsPdwhPubLikePO;
import com.smate.sie.center.task.otherlibrary.model.SnsPdwhPubSharePO;
import com.smate.sie.center.task.otherlibrary.model.SnsPdwhPubViewPO;
import com.smate.sie.center.task.otherlibrary.model.SnsPubLikePO;
import com.smate.sie.center.task.otherlibrary.model.SnsPubSharePO;
import com.smate.sie.center.task.otherlibrary.model.SnsPubViewPO;
import com.smate.sie.core.base.utils.dao.pub.SiePublicationDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhAwardDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhReadDao;
import com.smate.sie.core.base.utils.dao.statistics.SieBhShareDao;
import com.smate.sie.core.base.utils.model.pub.SiePublication;
import com.smate.sie.core.base.utils.model.statistics.BhConsts;
import com.smate.sie.core.base.utils.model.statistics.SieBhAward;
import com.smate.sie.core.base.utils.model.statistics.SieBhRead;
import com.smate.sie.core.base.utils.model.statistics.SieBhShare;

/**
 * 同步成果社交化行为服务实现
 * 
 * @author hd
 *
 */
@Service("sieSyncPubSocialBehaveService")
@Transactional(rollbackFor = Exception.class)
public class SieSyncPubSocialBehaveServiceImpl implements SieSyncPubSocialBehaveService {
  Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 1000;// 一次最多处理数量
  @Autowired
  private SiePublicationDao siePublicationDao;
  @Autowired
  private PubPdwhSieRealtionSerivce pubPdwhSieRealtionSerivce;
  @Autowired
  private SnsPdwhPubShareDAO pdwhPubShareDAO;
  @Autowired
  private SnsPubShareDAO snsPubShareDAO;
  @Autowired
  private SieBhShareDao sieBhShareDao;
  @Autowired
  private PersonDao snsPersonDao;
  @Autowired
  private ConstRegionDao snsConstRegionDao;
  @Autowired
  private SnsPdwhPubLikeDAO pdwhPubLikeDAO;
  @Autowired
  private SnsPubLikeDAO snsPubLikeDAO;
  @Autowired
  private SnsPubViewDAO snsPubViewDAO;
  @Autowired
  private SieBhAwardDao sieBhAwardDao;
  @Autowired
  private SieBhReadDao sieBhReadDao;
  @Autowired
  private SiePubStatDao siePubStatDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private SnsPdwhPubViewDAO pdwhPubViewDAO;
  @Autowired
  private SieCityIpDao sieCityIpDao;
  @Autowired
  private SieConstRegionDao sieConstRegionDao;


  @Override
  public void doSync(Long insId) throws ServiceException {
    try {
      Page<SiePublication> tempPage = new Page<SiePublication>(BATCH_SIZE);
      tempPage = siePublicationDao.findPublicationByInsId(insId, tempPage);
      if (tempPage.getTotalCount() > 0) {
        Long tempTotalPage = tempPage.getTotalPages();
        for (int j = 1; j <= tempTotalPage; j++) {
          if (j > 1) {
            tempPage.setPageNo(j);
            tempPage = siePublicationDao.findPublicationByInsId(insId, tempPage);
          }
          for (SiePublication pub : tempPage.getResult()) {
            Long pubId = pub.getPubId();
            PubPdwhSieRelation pubPdwhSieRelation = pubPdwhSieRealtionSerivce.getBySiePubId(pubId);
            if (pubPdwhSieRelation != null) {
              Integer shareNum = 0, awardNum = 0, readNum = 0;
              /* 同步基准库分享记录 */
              shareNum += syncPDWHPubShare(pubId, pubPdwhSieRelation.getPdwhPubId());
              /* 同步基准库赞记录 */
              awardNum += syncPDWHPubAward(pubId, pubPdwhSieRelation.getPdwhPubId());
              /* 同步基准库阅读记录 */
              readNum += syncPDWHPubRead(pubId, pubPdwhSieRelation.getPdwhPubId());
              List<Long> snsPubIdList =
                  pubPdwhSnsRelationDAO.getSnsPubIdListByPdwhId(pubPdwhSieRelation.getPdwhPubId());
              /* 同步SNS库分享 记录 */
              shareNum += syncSNSPubShare(pubId, snsPubIdList);
              /* 同步SNS库赞记录 */
              awardNum += syncSNSPubAward(pubId, snsPubIdList);
              /* 同步SNS库阅读记录 */
              readNum += syncSNSPubRead(pubId, snsPubIdList);
              /* 更新统计数 */
              doStatistics(pubId, awardNum.longValue(), shareNum.longValue(), readNum.longValue());

            }

          }
        }
      }
    } catch (Exception e) {
      logger.error("导入基准库成果社交化操作记录异常！insId :{} ", new Object[] {insId, e});
      throw new ServiceException("导入基准库成果社交化操作记录异常！insId : " + insId, e);
    }

  }

  @Override
  public Integer syncPDWHPubShare(Long siePubId, Long pdwhPubId) throws ServiceException {
    /* 同步记录总数 */
    Integer sync = 0;
    try {
      // 删除导入的所有的分享记录
      deleteShareByPubIdAndType(siePubId, BhConsts.PDWH_PUB);
      /* 导入基准库社交化记录 */
      List<SnsPdwhPubSharePO> pdwhList = pdwhPubShareDAO.getShareRecords(pdwhPubId);
      if (pdwhList != null && pdwhList.size() > 0) {
        /* 基准库社交化记录 */
        for (SnsPdwhPubSharePO pdwh : pdwhList) {
          Long optPsnId = pdwh.getPsnId();
          Long regionId = getPsnRegionId(optPsnId);
          SieBhShare share = new SieBhShare(siePubId, optPsnId, BhConsts.PDWH_PUB, pdwh.getGmtCreate(),
              DateUtils.getDateTime(pdwh.getGmtCreate()), null, regionId);
          /*
           * Map<String, String> result = convertLocation(optPsnId);
           * share.setIpCountry(result.get("country")); share.setIpProv(result.get("prov"));
           * share.setIpCity(result.get("city"));
           */
          sieBhShareDao.save(share);
          sync += 1;
        }
      }

    } catch (Exception e) {
      logger.error("导入基准库成果分享操作记录异常！siePubId :{}; pdwhPubId :{} ", new Object[] {siePubId, pdwhPubId, e});
      throw new ServiceException("导入基准库成果分享操作记录异常！siePubId : " + siePubId + ";pdwhPubId : " + pdwhPubId, e);
    }
    return sync;

  }

  @Override
  public Integer syncSNSPubShare(Long siePubId, List<Long> snsPubIds) throws ServiceException {
    /* 同步记录总数 */
    Integer sync = 0;
    try {
      // 删除导入的所有的分享记录
      deleteShareByPubIdAndType(siePubId, BhConsts.SNS_PUB);
      if (snsPubIds != null && snsPubIds.size() > 0) {
        for (Long snsPubId : snsPubIds) {
          List<SnsPubSharePO> snsList = snsPubShareDAO.getShareRecords(snsPubId);
          if (snsList != null && snsList.size() > 0) {
            /* 导入SNS库社交化记录 */
            for (SnsPubSharePO pub : snsList) {
              Long optPsnId = pub.getPsnId();
              Long regionId = getPsnRegionId(optPsnId);
              SieBhShare share = new SieBhShare(siePubId, optPsnId, 4, pub.getGmtCreate(),
                  DateUtils.getDateTime(pub.getGmtCreate()), null, regionId);
              sieBhShareDao.save(share);
              sync += 1;
            }

          }
        }

      }

    } catch (Exception e) {
      logger.error("导入SNS库成果分享操作记录异常！siePubId : {}; snsPubIds : {}", new Object[] {siePubId, snsPubIds, e});
      throw new ServiceException("导入SNS库成果分享操作记录异常！siePubId : " + siePubId + ";snsPubIds : " + snsPubIds, e);
    }
    return sync;


  }

  @Override
  public Integer syncPDWHPubAward(Long siePubId, Long pdwhPubId) throws ServiceException {
    /* 同步记录总数 */
    Integer sync = 0;
    try {
      // 删除导入的所有的赞记录
      deleteAwardByPubIdAndType(siePubId, BhConsts.PDWH_PUB);
      /* 导入记录 */
      List<SnsPdwhPubLikePO> pdwhPubLikeList = pdwhPubLikeDAO.findByPubId(pdwhPubId);
      if (pdwhPubLikeList != null && pdwhPubLikeList.size() > 0) {
        for (SnsPdwhPubLikePO po : pdwhPubLikeList) {
          Long optPsnId = po.getPsnId();
          Long regionId = getPsnRegionId(optPsnId);
          SieBhAward award = new SieBhAward(siePubId, optPsnId, BhConsts.PDWH_PUB, po.getGmtCreate(),
              DateUtils.getDateTime(po.getGmtCreate()), null, regionId);
          sieBhAwardDao.save(award);
          sync += 1;
        }
      }

    } catch (Exception e) {
      logger.error("导入基准库成果赞操作记录异常！siePubId : {} ;pdwhPubId : {}", new Object[] {siePubId, pdwhPubId, e});
      throw new ServiceException("导入基准库成果赞操作记录异常！siePubId : " + siePubId + ";pdwhPubId : " + pdwhPubId, e);
    }
    return sync;

  }

  @Override
  public Integer syncSNSPubAward(Long siePubId, List<Long> snsPubIds) throws ServiceException {
    /* 同步记录总数 */
    Integer sync = 0;
    try {
      // 删除导入的所有的赞记录
      deleteAwardByPubIdAndType(siePubId, BhConsts.SNS_PUB);
      if (snsPubIds != null && snsPubIds.size() > 0) {
        for (Long snsPubId : snsPubIds) {
          List<SnsPubLikePO> snsPubLikeList = snsPubLikeDAO.findByPubId(snsPubId);
          if (snsPubLikeList != null && snsPubLikeList.size() > 0) {
            for (SnsPubLikePO po : snsPubLikeList) {
              Long optPsnId = po.getPsnId();
              Long regionId = getPsnRegionId(optPsnId);
              SieBhAward award = new SieBhAward(siePubId, optPsnId, BhConsts.SNS_PUB, po.getGmtCreate(),
                  DateUtils.getDateTime(po.getGmtCreate()), null, regionId);
              sieBhAwardDao.save(award);
              sync += 1;
            }
          }


        }
      }


    } catch (Exception e) {
      logger.error("导入SNS库成果赞操作记录异常！siePubId : {} ;snsPubIds : {}", new Object[] {siePubId, snsPubIds, e});
      throw new ServiceException("导入SNS库成果赞操作记录异常！siePubId : " + siePubId + ";snsPubIds : " + snsPubIds, e);
    }
    return sync;

  }

  @Override
  public Integer syncSNSPubRead(Long siePubId, List<Long> snsPubIds) throws ServiceException {
    Integer sync = 0;
    try {
      deleteReadByPubIdAndType(siePubId, BhConsts.SNS_PUB);
      if (snsPubIds != null && snsPubIds.size() > 0) {
        for (Long snsPubId : snsPubIds) {
          List<SnsPubViewPO> snsPubViewList = snsPubViewDAO.getPubView(snsPubId);
          if (snsPubViewList != null && snsPubViewList.size() > 0) {
            for (SnsPubViewPO po : snsPubViewList) {
              Long total = po.getTotalCount() == null ? 1L : po.getTotalCount();
              if (total != null) {
                Long regionId = getPsnRegionId(po.getViewPsnId());
                for (int i = 0; i < total.intValue(); i++) {
                  SieBhRead read = new SieBhRead(po.getViewPsnId(), siePubId, BhConsts.SNS_PUB, po.getGmtCreate(),
                      DateUtils.getDateTime(po.getGmtCreate()), po.getIp(), regionId);
                  // ip解析
                  Map<String, String> ipParse = parseIP2(po.getIp());
                  if (ipParse != null) {
                    read.setIpCountry(ipParse.get(ParseIpUtils.STR_COUNTRY));
                    read.setIpProv(ipParse.get(ParseIpUtils.STR_PRVO));
                    read.setIpCity(ipParse.get(ParseIpUtils.STR_CITY));
                  }
                  sieBhReadDao.save(read);
                  sync += 1;
                }
              }

            }

          }

        }
      }


    } catch (Exception e) {
      logger.error("导入SNS库成果阅读操作记录异常！siePubId : {} ;snsPubIds : {}", new Object[] {siePubId, snsPubIds, e});
      throw new ServiceException("导入SNS库成果阅读操作记录异常！siePubId : " + siePubId + ";snsPubIds : " + snsPubIds, e);
    }
    return sync;
  }

  @Override
  public Integer syncPDWHPubRead(Long siePubId, Long pdwhPubId) throws ServiceException {
    Integer sync = 0;
    try {
      deleteReadByPubIdAndType(siePubId, BhConsts.PDWH_PUB);
      /* 导入记录 */
      List<SnsPdwhPubViewPO> pdwhPubViewList = pdwhPubViewDAO.findByPubId(pdwhPubId);
      for (SnsPdwhPubViewPO po : pdwhPubViewList) {
        Long total = po.getTotalCount() == null ? 1L : po.getTotalCount();
        if (total != null) {
          Long regionId = getPsnRegionId(po.getViewPsnId());
          for (int i = 0; i < total.intValue(); i++) {
            SieBhRead read = new SieBhRead(po.getViewPsnId(), siePubId, BhConsts.PDWH_PUB, po.getGmtCreate(),
                DateUtils.getDateTime(po.getGmtCreate()), po.getIp(), regionId);
            // ip解析
            Map<String, String> ipParse = parseIP2(po.getIp());
            if (ipParse != null) {
              read.setIpCountry(ipParse.get(ParseIpUtils.STR_COUNTRY));
              read.setIpProv(ipParse.get(ParseIpUtils.STR_PRVO));
              read.setIpCity(ipParse.get(ParseIpUtils.STR_CITY));
            }
            sieBhReadDao.save(read);
            sync += 1;
          }
        }
      }

    } catch (Exception e) {
      logger.error("导入基准库成果阅读操作记录异常！siePubId : {} ;pdwhPubId : {}", new Object[] {siePubId, pdwhPubId, e});
      throw new ServiceException("导入基准库成果阅读操作记录异常！siePubId : " + siePubId + ";pdwhPubId : " + pdwhPubId, e);
    }
    return sync;
  }

  private void deleteAwardByPubIdAndType(Long pubId, Integer type) throws ServiceException {
    try {
      List<SieBhAward> list = sieBhAwardDao.getByPubIdAndType(pubId, type);
      if (list != null) {
        for (SieBhAward award : list) {
          sieBhAwardDao.delete(award);
        }
      }

    } catch (Exception e) {
      logger.error("删除赞记录异常！pubId : {} ; type : {}", new Object[] {pubId, type, e});
      throw new ServiceException("删除赞记录异常！pubId：" + pubId + "; type:" + type, e);
    }
  }

  private void deleteShareByPubIdAndType(Long pubId, Integer type) throws ServiceException {
    try {
      List<SieBhShare> list = sieBhShareDao.getByPubIdAndType(pubId, type);
      if (list != null) {
        for (SieBhShare share : list) {
          sieBhShareDao.delete(share);
        }
      }

    } catch (Exception e) {
      logger.error("删除分享记录异常！pubId : {} ; type : {}", new Object[] {pubId, type, e});
      throw new ServiceException("删除分享记录异常！pubId：" + pubId + "; type:" + type, e);
    }
  }

  private void deleteReadByPubIdAndType(Long pubId, Integer type) throws ServiceException {
    try {
      List<SieBhRead> list = sieBhReadDao.getByPubIdAndType(pubId, type);
      if (list != null) {
        for (SieBhRead read : list) {
          sieBhReadDao.delete(read);
        }
      }
    } catch (Exception e) {
      logger.error("删除阅读记录异常！pubId : {} ; type : {}", new Object[] {pubId, type, e});
      throw new ServiceException("删除阅读记录异常！pubId：" + pubId + "; type:" + type, e);
    }
  }

  /**
   * 根据regionId获取地址信息
   * 
   * @param optPsnId
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unused")
  private Map<String, String> convertLocation(Long optPsnId) throws ServiceException {
    Map<String, String> result = new HashMap<String, String>();
    String country = "", prov = "", city = "";
    try {
      if (optPsnId != null) {
        Long regionId = snsPersonDao.getPsnRegionIdByObjectId(optPsnId);
        if (regionId != null) {// 香港、澳门、台湾特殊处理，视为省份
          if (regionId.equals(158L)) {
            country = "中国";
            prov = "台湾";
          } else if (regionId.equals(446L)) {
            country = "中国";
            prov = "澳门";
          } else if (regionId.equals(344L)) {
            country = "中国";
            prov = "香港";
          } else {
            ConstRegion regionOne = snsConstRegionDao.get(regionId);
            if (regionOne.getSuperRegionId() == null) {// regionId是国家
              country = regionOne.getZhName();
            } else {
              ConstRegion regionTwo = snsConstRegionDao.get(regionOne.getSuperRegionId());
              if (regionTwo.getSuperRegion() == null) {// regionId是省份
                country = regionTwo.getZhName();
                prov = regionOne.getZhName();
              } else {
                ConstRegion regionThree = snsConstRegionDao.get(regionTwo.getSuperRegionId());
                if (regionThree.getSuperRegion() == null) {// regionId城市
                  country = regionThree.getZhName();
                  prov = regionTwo.getZhName();
                  city = regionOne.getZhName();
                }
              }
            }

          }

        }
      }
      result.put("country", country);
      result.put("prov", prov);
      result.put("city", city);
    } catch (Exception e) {
      logger.error("获取人员国家、省份、城市异常 ！psnId : " + optPsnId, e);
      throw new ServiceException("获取人员国家、省份、城市异常 ！psnId : " + optPsnId, e);
    }
    return result;

  }

  private Long getPsnRegionId(Long optPsnId) throws ServiceException {
    Long regionId = null;
    try {
      if (optPsnId != null) {
        regionId = snsPersonDao.getPsnRegionIdByObjectId(optPsnId);
      }
    } catch (Exception e) {
      logger.error("获取人员region_id异常 ！psnId : " + optPsnId, e);
      throw new ServiceException("获取人员region_id异常  ！psnId : " + optPsnId, e);
    }
    return regionId;
  }

  @Override
  public void doStatistics(Long siePubId, Long awardNum, Long shareNum, Long readNum) throws ServiceException {
    try {
      PubStat pubSt = siePubStatDao.get(siePubId);
      if (pubSt == null) {
        pubSt = new PubStat();
      }
      awardNum += sieBhAwardDao.countAward(siePubId, BhConsts.SIE_PUB);
      shareNum += sieBhShareDao.countShare(siePubId, BhConsts.SIE_PUB);
      readNum += sieBhReadDao.countRead(siePubId, BhConsts.SIE_PUB);
      pubSt.setPubId(siePubId);
      pubSt.setAwardNum(awardNum);
      pubSt.setReadNum(readNum);
      pubSt.setShareNum(shareNum);
      siePubStatDao.save(pubSt);
    } catch (Exception e) {
      logger.error("更新ST_PUB异常 ！siePubId :{};awardNum:{};shareNum:{};readNum:{} ",
          new Object[] {siePubId, awardNum, shareNum, readNum, e});
      throw new ServiceException("更新ST_PUB异常 ！siePubId : " + siePubId, e);
    }

  }

  private SieCityIp parseIP(String ipStr) throws Exception {
    SieCityIp cityIp = null;
    try {
      Long ip = buildIPParam(ipStr);
      cityIp = sieCityIpDao.findByIp(ip);

    } catch (Exception e) {
      logger.error("解析IP异常 ！ipStr :{}", new Object[] {ipStr, e});
      throw new ServiceException("解析IP异常 ！ipStr :" + ipStr, e);
    }
    return cityIp;

  }

  private Map<String, String> parseIP2(String ipStr) throws SysServiceException {
    Map<String, String> result = null;
    try {
      if (ipStr.trim().startsWith("192.168") || ipStr.trim().equals("127.0.0.1")) {
        return result;
      }
      result = ParseIpUtils.getAddresses(ipStr.trim(), "utf-8");
      if (result == null) {
        SieCityIp cityIp = this.parseIP(ipStr.trim());
        if (cityIp != null) {
          result = new HashMap<String, String>();
          result.put(ParseIpUtils.STR_COUNTRY, "中国");
          result.put(ParseIpUtils.STR_PRVO, cityIp.getProvince());
          result.put(ParseIpUtils.STR_CITY, getFormatCity(cityIp.getProvince(), cityIp.getCity()));
        }

      } else {
        String cityIdStr = result.get("city_id");
        if (cityIdStr != null && NumberUtils.isDigits(cityIdStr)) {
          Long cityId = NumberUtils.parseLong(cityIdStr);
          SieConstRegion region = sieConstRegionDao.get(cityId);
          if (region != null) {
            result.put(ParseIpUtils.STR_CITY, region.getZhName());
          }

        }
        String regionIdStr = result.get("region_id");
        if (regionIdStr != null && NumberUtils.isDigits(regionIdStr)) {
          Long regionId = NumberUtils.parseLong(regionIdStr);
          SieConstRegion region = sieConstRegionDao.get(regionId);
          if (region != null) {
            result.put(ParseIpUtils.STR_PRVO, region.getZhName());
          }
        }
      }
      if (result != null && StringUtils.isNotBlank(result.get(ParseIpUtils.STR_CITY))) {
        SieConstRegion region = sieConstRegionDao.getConstRegionByName(result.get(ParseIpUtils.STR_CITY).trim());
        if (region == null) {
          result.put(ParseIpUtils.STR_CITY, null);
        }
      }

    } catch (Exception e) {
      logger.error("解析IP异常 ！ipStr :{}", new Object[] {ipStr, e});
      throw new SysServiceException("解析IP异常 ！ipStr :" + ipStr, e);
    }
    return result;
  }

  /**
   * 构建IP地址参数.
   * 
   * @param ip
   * @return
   */
  private Long buildIPParam(String ip) throws Exception {
    if (ip == null) {
      return 0L;
    }
    /**
     * 判断IP格式和范围
     */
    String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    Pattern pat = Pattern.compile(rexp);
    Matcher mat = pat.matcher(ip);
    if (!mat.find()) {// 不是ip地址
      return 0L;
    }
    String[] ipArray = ip.split("\\.");
    Long result = 0l;
    for (int i = 0; i < ipArray.length; i++) {
      Double ipData = (Long.valueOf(ipArray[i]) * Math.pow(256, 3 - i));
      result = result + ipData.longValue();
    }
    return result;
  }

  private String getFormatCity(String prvo, String city) {
    switch (prvo.trim()) {
      case "上海市":
        return "上海市";
      case "北京市":
        return "北京市";
      case "天津市":
        return "天津市";
      case "重庆市":
        return "重庆市";
      default:
        return city;
    }
  }



}
