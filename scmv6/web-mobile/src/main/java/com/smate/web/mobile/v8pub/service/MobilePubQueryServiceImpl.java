package com.smate.web.mobile.v8pub.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.mobile.v8pub.consts.V8pubConst;
import com.smate.web.mobile.v8pub.dao.CategoryScmDao;
import com.smate.web.mobile.v8pub.sns.po.CategoryScm;
import com.smate.web.mobile.v8pub.vo.CategoryScmVO;
import com.smate.web.mobile.v8pub.vo.PubListVO;

/**
 * 移动端成果查询服务
 * 
 * @author wsn
 * @date 2018年9月3日
 */
@Service("mobilePubQueryService")
@Transactional(rollbackFor = Exception.class)
public class MobilePubQueryServiceImpl implements MobilePubQueryService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private CategoryScmDao categoryScmDao;

  @SuppressWarnings("unchecked")
  @Override
  public void buildPubListVO(PubListVO VO, Class<?> class1) throws ServiceException {
    List<Map<String, Object>> resultList = new ArrayList<>();
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(VO.getRestfulUrl(), VO.getPubQueryDTO(), class1);
    if (object != null && object.get("status").equals(V8pubConst.SUCCESS)) {
      resultList = (List<Map<String, Object>>) object.get("resultList");
      Object totalCount = object.get("totalCount");
      VO.setTotalCount(NumberUtils.toInt(totalCount.toString()));
      Page page = VO.getPage();
      page.setTotalCount(NumberUtils.toLong(totalCount.toString()));
      if (page.getTotalCount() % page.getPageSize() == 0) {
        VO.getPage().setTotalPages(page.getTotalCount() / page.getPageSize());
      } else {
        VO.getPage().setTotalPages(page.getTotalCount() / page.getPageSize() + 1);
      }
      List<PubInfo> list = new ArrayList<>(16);
      VO.setResultList(list);
      // 这个属性是APP那边想要为空的值就不用返回过去了
      // VO.setMapList(resultList);
      if (resultList != null && resultList.size() > 0) {
        for (Map<String, Object> map : resultList) {
          PubInfo pubInfo = new PubInfo();
          try {
            if (map.get("pubDb") != null && map.get("pubDb").toString().equalsIgnoreCase("PDWH")) {
              map.put("pubDb", PubDbEnum.PDWH);
            } else {
              map.put("pubDb", PubDbEnum.SNS);
            }
            BeanUtils.populate(pubInfo, map);
          } catch (Exception e) {
            logger.error("PubInfo复制属性失败", e);
          }
          this.dealPubFulltextInfo(pubInfo, VO.getCurrentLoginPsnId());
          pubInfo.setDes3OwnerPsnId(Des3Utils.encodeToDes3(Objects.toString(pubInfo.getOwnerPsnId())));
          list.add(pubInfo);
        }
      }
    }
  }

  // 获取全部的我科技领域
  @Override
  public List<Map<String, Object>> getAllScienceArea() throws ServiceException {
    List<Map<String, Object>> allScienceAreaList = new ArrayList<Map<String, Object>>();
    List<CategoryScm> firstAreaList = categoryScmDao.findFirstScienceArea();
    Map<String, Object> areaItemMap = null;
    for (CategoryScm item : firstAreaList) {
      areaItemMap = new HashMap<String, Object>();
      // 一级科技领域
      Long areaId = item.getCategoryId();
      CategoryScmVO firstVO = new CategoryScmVO(areaId, item.getCategoryZh(), item.getCategoryEn(),
          item.getParentCategroyId(), Des3Utils.encodeToDes3(areaId.toString()));
      areaItemMap.put("first", firstVO);
      // 二级科技领域
      List<CategoryScm> secondAreaList = categoryScmDao.findSecondScienceArea(areaId);
      List<CategoryScmVO> secondAreas = new ArrayList<CategoryScmVO>();
      if (CollectionUtils.isNotEmpty(secondAreaList)) {
        for (CategoryScm area : secondAreaList) {
          CategoryScmVO secondVO = new CategoryScmVO(area.getCategoryId(), area.getCategoryZh(), area.getCategoryEn(),
              area.getParentCategroyId(), Des3Utils.encodeToDes3(area.getCategoryId().toString()));
          secondAreas.add(secondVO);
        }
      }
      areaItemMap.put("second", secondAreas);
      allScienceAreaList.add(areaItemMap);
    }
    return allScienceAreaList;
  }

  @Override
  public void dealPubFulltextInfo(PubInfo pubInfo, Long currentLoginPsnId) throws ServiceException {
    if (pubInfo != null) {
      // 处理全文下载权限 1200000038979
      boolean isOwner = CommonUtils.compareLongValue(pubInfo.getOwnerPsnId(), currentLoginPsnId);
      boolean hasFulltext = CommonUtils.compareIntegerValue(pubInfo.getHasFulltext(), 1);
      boolean isPublicFulltext = CommonUtils.compareIntegerValue(pubInfo.getFullTextPermission(), 0);
      pubInfo.setCanDownloadFulltext((hasFulltext && (isOwner || isPublicFulltext)) ? 1 : 0);
    }
  }

}
