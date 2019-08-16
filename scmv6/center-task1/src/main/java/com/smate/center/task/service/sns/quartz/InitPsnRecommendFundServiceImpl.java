package com.smate.center.task.service.sns.quartz;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.dao.sns.quartz.ApplicationQuartzSettingDao;
import com.smate.core.base.utils.dao.security.PersonDao;

@Service("initPsnRecommendFundService")
@Transactional(rollbackFor = Exception.class)
public class InitPsnRecommendFundServiceImpl implements InitPsnRecommendFundService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonDao personDao;
  @Autowired
  private ApplicationQuartzSettingDao applicationQuartzSettingDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;

  @Override
  public Integer getApplicationQuartzSettingValue(String name) {

    return applicationQuartzSettingDao.getAppValue(name);
  }

  @Override
  public void closeQuartzApplication(String name) {
    applicationQuartzSettingDao.closeApplication(name);
  }

  @Override
  public List<Long> getPsnIds(Long lastPsnId) {
    return personDao.getPsnIdsByStart(lastPsnId);
  }

  @Override
  public void initPsnFundRecommend(List<Long> psnIdList) throws Exception {
    if (CollectionUtils.isNotEmpty(psnIdList)) {
      try {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        String psnIds = psnIdList.stream().map(id -> id.toString()).collect(Collectors.joining(","));
        params.add("psnIds", psnIds);
        postUrl(params, domainMobile + "/prjdata/initpsnrecommedfund");
      } catch (Exception e) {
        logger.error("InitPsnRecommendFundTask调用远程接口出错", e);
        return;
      }
    }
    return;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private Map<String, Object> postUrl(MultiValueMap param, String url) {
    HttpEntity<MultiValueMap> httpEntity = this.getEntity(param);// 创建头部信息
    return restTemplate.postForObject(url, httpEntity, Map.class);
  }

  @SuppressWarnings("rawtypes")
  private HttpEntity getEntity(MultiValueMap param) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap> HttpEntity = new HttpEntity<MultiValueMap>(param, headers);
    return HttpEntity;
  }
}
