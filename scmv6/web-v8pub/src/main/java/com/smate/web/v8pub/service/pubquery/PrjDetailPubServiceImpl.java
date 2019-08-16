package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.project.dao.PrjPubDao;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDAO;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubFullTextService;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PubListResult;

@Transactional(rollbackFor = Exception.class)
public class PrjDetailPubServiceImpl extends AbstractPubQueryService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Resource
  private PdwhPubFullTextService pdwhpubFullTextService;
  @Autowired
  private PrjPubDao prjPubDao;


  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (pubQueryDTO.getSearchPsnId() == null || pubQueryDTO.getSearchPsnId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的人员psnId不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    List<PubPdwhPO> PdwhPubs = new ArrayList<PubPdwhPO>();
    // 基准库成果
    List<Long> pdwhPubIds = prjPubDao.getPrjPubIdsByPrjId(pubQueryDTO.getSearchPrjId(), 0);
    if (CollectionUtils.isNotEmpty(pdwhPubIds)) {
      List<PubPdwhPO> orderPdwhPubs = pubPdwhDAO.getPrjPubs(pubQueryDTO, pdwhPubIds);
      PdwhPubs.addAll(orderPdwhPubs);
    }
    // 个人库成果
    List<PubSnsPO> orderSnsPubs = pubSnsDAO.getPrjPubs(pubQueryDTO);
    pubQueryDTO.setPubSnsList(orderSnsPubs);
    pubQueryDTO.setPubPdwhList(PdwhPubs);
  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult listResult = new PubListResult();
    List<PubInfo> list = new ArrayList<>();
    // 基准库成果
    if (CollectionUtils.isNotEmpty(pubQueryDTO.getPubPdwhList())) {
      for (PubPdwhPO pubPdwh : pubQueryDTO.getPubPdwhList()) {
        PubInfo pubInfo = new PubInfo();
        pubInfo.setPubId(pubPdwh.getPubId());
        pubInfo.setDes3PubId(Des3Utils.encodeToDes3(pubPdwh.getPubId().toString()));
        pubInfo.setTitle(pubPdwh.getTitle());
        pubInfo.setBriefDesc(pubPdwh.getBriefDesc());
        pubInfo.setAuthorNames(pubPdwh.getAuthorNames());
        pubInfo.setCitations(pubPdwh.getCitations());
        pubInfo.setPubDb(PubDbEnum.PDWH);
        pubInfo.setPublishYear(pubPdwh.getPublishYear());
        pubInfo.setPublishMonth(pubPdwh.getPublishMonth());
        pubInfo.setPublishDay(pubPdwh.getPublishDay());
        // 成果短地址
        buildPdwhPubIndexUrl(pubInfo, pubQueryDTO.getSearchPsnId());
        // 收录情况
        builPdwhPubStatistics(pubInfo, pubQueryDTO.getSearchPsnId());
        // 构建全文信息
        buildPdwhPubFulltext(pubInfo, pubQueryDTO.getSearchPsnId());
        list.add(pubInfo);
      }
    }
    // 个人库成果
    if (CollectionUtils.isNotEmpty(pubQueryDTO.getPubSnsList())) {
      for (PubSnsPO pubSns : pubQueryDTO.getPubSnsList()) {
        PubInfo pubInfo = new PubInfo();
        pubInfo.setPubId(pubSns.getPubId());
        pubInfo.setDes3PubId(Des3Utils.encodeToDes3(pubSns.getPubId().toString()));
        pubInfo.setTitle(pubSns.getTitle());
        pubInfo.setBriefDesc(pubSns.getBriefDesc());
        pubInfo.setAuthorNames(pubSns.getAuthorNames());
        pubInfo.setCitations(pubSns.getCitations());
        pubInfo.setPubDb(PubDbEnum.SNS);
        pubInfo.setPublishYear(pubSns.getPublishYear());
        pubInfo.setPublishMonth(pubSns.getPublishMonth());
        pubInfo.setPublishDay(pubSns.getPublishDay());

        // 成果短地址
        buildSnsPubIndexUrl(pubInfo, pubQueryDTO.getSearchPsnId());
        // 收录情况
        builSnsPubStatistics(pubInfo, pubQueryDTO.getSearchPsnId());
        // 构建全文信息
        buildSnsPubFulltext(pubInfo, pubQueryDTO.getSearchPsnId());
        list.add(pubInfo);
      }
    }
    if (list != null && list.size() > 0) {
      setAuthorName(list);// 作者显示做下处理
      sortPubs(list, pubQueryDTO.getOrderBy());// 重新排序
      listResult.setTotalCount(list.size());
      Integer pageNo = pubQueryDTO.getPageNo();
      list = list.stream().skip((pageNo - 1) * 10).limit(10).parallel().collect(Collectors.toList());//
      // 分页
      listResult.setResultList(list);
    }
    return listResult;
  }

  private void setAuthorName(List<PubInfo> list) {
    if (CollectionUtils.isNotEmpty(list)) {
      for (PubInfo pub : list) {
        String authors = pub.getAuthorNames();
        if (StringUtils.isNotBlank(authors)) {
          authors = authors.replaceAll(" ", "");
          authors = authors.replaceAll(",", ";");
          authors = authors.replaceAll("，", ";");
          while (authors.lastIndexOf(";;") >= 0) {// 去掉多个;;
            authors = authors.replaceAll(";;", ";");
          }
          if (authors.lastIndexOf(";") == (authors.length() - 1)) {
            authors = authors.substring(0, authors.lastIndexOf(";"));
          }
          authors = authors.replaceAll(";", "; ");
          pub.setAuthorNames(authors);
        }
      }
    }
  }

  /**
   * 集合排序
   * 
   * @param showPubs
   * @param orderBy
   */
  private void sortPubs(List<PubInfo> showPubs, String orderBy) {
    // 都为降序
    if ("readTimes".equals(orderBy)) {
      showPubs.sort(new Comparator<PubInfo>() {
        @Override
        public int compare(PubInfo o1, PubInfo o2) {
          if (o1.getReadCount() == null) {
            return o2.getReadCount() == null ? 0 : o2.getReadCount() - 0;
          } else {
            return o2.getReadCount() == null ? 0 - o1.getReadCount() : o2.getReadCount() - o1.getReadCount();
          }
        }
      });
    } else if ("citedTimes".equals(orderBy)) {
      showPubs.sort(new Comparator<PubInfo>() {

        @Override
        public int compare(PubInfo o1, PubInfo o2) {
          if (o1.getCitations() == null) {
            return o2.getCitations() == null ? 0 : o2.getCitations() - 0;
          } else {
            return o2.getCitations() == null ? 0 - o1.getCitations() : o2.getCitations() - o1.getCitations();
          }
        }
      });
    } else if ("publishYear".equals(orderBy)) {
      showPubs.sort(new Comparator<PubInfo>() {
        @Override
        public int compare(PubInfo o1, PubInfo o2) {
          if (o1.getPublishYear() == null && o2.getPublishYear() == null) {
            return 0;
          } else if (o1.getPublishYear() == null && o2.getPublishYear() != null) {
            return o2.getPublishYear() - 0;
          } else if (o1.getPublishYear() != null && o2.getPublishYear() == null) {
            return 0 - o1.getPublishYear();
          } else {
            if (o1.getPublishYear().equals(o2.getPublishYear())) {
              // 年份相等 对比月
              if (o1.getPublishMonth() == null && o2.getPublishMonth() == null) {
                return 0;
              } else if (o1.getPublishMonth() == null && o2.getPublishMonth() != null) {
                return 1;
              } else if (o1.getPublishMonth() != null && o2.getPublishMonth() == null) {
                return -1;
              } else {
                if (o1.getPublishMonth().equals(o2.getPublishMonth())) {
                  // 月份相等对比日
                  if (o1.getPublishDay() == null && o2.getPublishDay() == null) {
                    return 0;
                  } else if (o1.getPublishDay() == null && o2.getPublishDay() != null) {
                    return 1;
                  } else if (o1.getPublishDay() != null && o2.getPublishDay() == null) {
                    return -1;
                  } else {
                    return o2.getPublishDay() - o1.getPublishDay();
                  }
                } else {
                  return o2.getPublishMonth() - o1.getPublishMonth();
                }
              }
            } else {
              return o2.getPublishYear() - o1.getPublishYear();
            }

          }


        }
      });
    } else {
      // 默认排序
      showPubs.sort(new Comparator<PubInfo>() {
        @Override
        public int compare(PubInfo o1, PubInfo o2) {
          if (o1.getCitations() == null) {
            return o2.getCitations() == null ? 0 : o2.getCitations() - 0;
          } else {
            return o2.getCitations() == null ? 0 - o1.getCitations() : o2.getCitations() - o1.getCitations();
          }
        }
      });
    }

  }


}
