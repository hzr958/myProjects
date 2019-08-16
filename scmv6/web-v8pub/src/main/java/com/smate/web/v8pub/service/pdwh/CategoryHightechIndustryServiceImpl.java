package com.smate.web.v8pub.service.pdwh;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.cache.SnsCacheService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dao.pdwh.CategoryHightechIndustryDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.CategoryHightechIndustry;
import com.smate.web.v8pub.vo.pdwh.PdwhPubIndustryVO;

/**
 * 产业分类服务
 * 
 * @author YJ
 *
 *         2019年5月30日
 */
@Service(value = "categoryHightechIndustryService")
@Transactional(rollbackFor = Exception.class)
public class CategoryHightechIndustryServiceImpl implements CategoryHightechIndustryService {

  @Autowired
  private CategoryHightechIndustryDAO categoryHightechIndustryDAO;
  @Autowired
  private SnsCacheService cacheService;

  private final String INDUSTRY_CACHE = "searchIndustrysCache";
  private final String INDUSTRY_KEY = "searchIndustrysCacheKey";


  @Override
  public String findAllIndustry() throws ServiceException {
    // 先从缓存中取行业的json数据
    String induJson = (String) cacheService.get(INDUSTRY_CACHE, INDUSTRY_KEY);
    // 获取成功，则直接返回使用
    if (StringUtils.isBlank(induJson)) {
      // 获取失败，则查询数据库进行构造json存在缓存中
      List<CategoryHightechIndustry> induList = categoryHightechIndustryDAO.findAllIndustry();
      List<PdwhPubIndustryVO> industrys = new ArrayList<>();
      if (CollectionUtils.isNotEmpty(induList)) {
        // 1.构建first级行业
        List<CategoryHightechIndustry> firstList =
            induList.stream().filter(in -> in.getCodeLevel() == null).collect(Collectors.toList());
        // 2.构建second级行业
        List<CategoryHightechIndustry> secondList = induList.stream()
            .filter(in -> in.getCodeLevel() != null && in.getCodeLevel().equals(2)).collect(Collectors.toList());
        // 3.构建third级行业
        List<CategoryHightechIndustry> thirdList = induList.stream()
            .filter(in -> in.getCodeLevel() != null && in.getCodeLevel().equals(3)).collect(Collectors.toList());
        // 4.构建fourth级行业
        List<CategoryHightechIndustry> fourthList = induList.stream()
            .filter(in -> in.getCodeLevel() != null && in.getCodeLevel().equals(4)).collect(Collectors.toList());
        // 构造json数据
        for (CategoryHightechIndustry first : firstList) {
          PdwhPubIndustryVO inVo1 = new PdwhPubIndustryVO();
          inVo1.setCode(first.getCode());
          inVo1.setName(first.getName());
          inVo1.setEname(first.getEname());
          inVo1.setEnd(first.getIsEnd() == 0);
          List<PdwhPubIndustryVO> children1 = new ArrayList<>();
          for (CategoryHightechIndustry second : secondList) {
            if (second.getParentCode().equals(first.getCode())) {
              PdwhPubIndustryVO inVo2 = new PdwhPubIndustryVO();
              inVo2.setCode(second.getCode());
              inVo2.setName(second.getName());
              inVo2.setEname(second.getEname());
              inVo2.setEnd(second.getIsEnd() == 0);
              children1.add(inVo2);
              List<PdwhPubIndustryVO> children2 = new ArrayList<>();
              for (CategoryHightechIndustry third : thirdList) {
                if (third.getParentCode().equals(second.getCode())) {
                  PdwhPubIndustryVO inVo3 = new PdwhPubIndustryVO();
                  inVo3.setCode(third.getCode());
                  inVo3.setName(third.getName());
                  inVo3.setEname(third.getEname());
                  inVo3.setEnd(third.getIsEnd() == 0);
                  children2.add(inVo3);
                  List<PdwhPubIndustryVO> children3 = new ArrayList<>();
                  for (CategoryHightechIndustry fourth : fourthList) {
                    if (fourth.getParentCode().equals(third.getCode())) {
                      PdwhPubIndustryVO inVo4 = new PdwhPubIndustryVO();
                      inVo4.setCode(fourth.getCode());
                      inVo4.setName(fourth.getName());
                      inVo4.setEname(fourth.getEname());
                      inVo4.setEnd(fourth.getIsEnd() == 0);
                      children3.add(inVo4);
                      inVo4.setChildren(new ArrayList<>());
                    }
                  }
                  inVo3.setChildren(children3);
                }
              }
              inVo2.setChildren(children2);
            }
          }
          inVo1.setChildren(children1);
          industrys.add(inVo1);
        }
        induJson = JacksonUtils.listToJsonStr(industrys);
        // 数据存储在缓存中，60s过期
        cacheService.put(INDUSTRY_CACHE, 60 * 60, INDUSTRY_KEY, induJson);
      }
    }
    return induJson;
  }


  @Override
  public List<PdwhPubIndustryVO> buildChooseIndustry(String codes) throws ServiceException {
    List<PdwhPubIndustryVO> industrys = new ArrayList<>();
    List<CategoryHightechIndustry> induList = new ArrayList<>();
    if (StringUtils.isNotBlank(codes)) {
      String[] codeArray = codes.split(",");
      induList = categoryHightechIndustryDAO.findIndustryInfo(codeArray);
    }
    if (CollectionUtils.isNotEmpty(induList)) {
      for (CategoryHightechIndustry in : induList) {
        PdwhPubIndustryVO industryVo = new PdwhPubIndustryVO();
        industryVo.setCode(in.getCode());
        industryVo.setName(in.getName());
        industryVo.setEname(in.getEname());
        industrys.add(industryVo);
      }
    }
    return industrys;
  }

}
