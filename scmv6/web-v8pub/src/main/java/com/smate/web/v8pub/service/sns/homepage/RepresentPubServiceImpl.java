package com.smate.web.v8pub.service.sns.homepage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.v8pub.restTemp.service.PubRestemplateService;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.sns.PubLikeDAO;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.dao.sns.representpub.RepresentPubDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.representpub.RepresentPub;
import com.smate.web.v8pub.po.representpub.RepresentPubPk;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.RepresentPubVO;

/**
 * 个人首页，代表成果service
 * 
 * @author aijiangbin
 * @date 2018年8月9日
 */

@Service("representPubService")
@Transactional(rollbackFor = Exception.class)
public class RepresentPubServiceImpl implements RepresentPubService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public final static String SNS_QUERY_PUB = "/data/pub/query/list";

  @Value("${domainscm}")
  private String domainscm;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private RepresentPubDao representPubDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubLikeDAO pubLikeDAO;
  @Autowired
  private PubRestemplateService pubRestemplateService;

  @SuppressWarnings("unchecked")
  @Override
  public void findPsnOpenPubList(PubListVO pubListVO) throws ServiceException {

    String SERVER_URL = domainscm + SNS_QUERY_PUB;
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity =
        new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(pubListVO.getPubQueryDTO()), requestHeaders);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    if (object != null && object.get("status").equals(V8pubConst.SUCCESS)) {
      List<Map<String, Object>> resultList = (List<Map<String, Object>>) object.get("resultList");
      Object totalCount = object.get("totalCount");
      pubListVO.setTotalCount(NumberUtils.toInt(totalCount.toString()));
      List<PubInfo> list = new ArrayList<>(16);
      pubListVO.setResultList(list);
      if (resultList != null && resultList.size() > 0) {
        for (Map<String, Object> map : resultList) {
          PubInfo pubInfo = new PubInfo();
          list.add(pubInfo);
          try {
            BeanUtils.populate(pubInfo, map);
          } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("复制属性异常", e);
          }

        }
      }

    }

  }

  @Override
  public void findPsnRepresentPub(RepresentPubVO representPubVO) throws Exception {
    List<PubInfo> sortList = restTemplatePost(representPubVO.getCurrentPsnId(), representPubVO.getDes3PsnId());
    for (PubInfo pub : sortList) {
      // 自己是否点赞过(Long pubId, Long psnId)
      Long award = 0L;
      if (SecurityUtils.getCurrentUserId() > 0) {
        award = pubLikeDAO.getLikeRecord(pub.getPubId(), representPubVO.getPsnId());
      }
      if (award > 0) {
        pub.setIsAward(1);
      } else {
        pub.setIsAward(0);
      }
    }
    representPubVO.setPubInfoList(sortList);
  }

  /**
   * 查找个人代表成果
   * 
   * @param psnId
   * @return
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  private List<PubInfo> restTemplatePost(Long currentPsnId, String des3PsnId) throws Exception {
    String pubJson = pubRestemplateService.psnRepresentPubList(currentPsnId, des3PsnId);
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(pubJson);
    List<PubInfo> list = new ArrayList<>();
    if (resultMap.get("resultList") != null) {
      list = new ObjectMapper().readValue(JacksonUtils.jsonObjectSerializer(resultMap.get("resultList")),
          new TypeReference<List<PubInfo>>() {});
    }
    return list;
  }

  @Override
  public void savePsnRepresentPub(Long psnId, String encodePubIds) {
    // 先将当前的代表性成果都置为无效状态
    representPubDao.updatePsnRepresentPubStatus(psnId, 1);
    if (StringUtils.isNotBlank(encodePubIds)) {
      // 保存更新代表性成果
      String[] pubIdArr = encodePubIds.split(",");
      if (pubIdArr != null && pubIdArr.length > 0) {
        int seqNo = 0;
        for (String des3PubId : pubIdArr) {
          // 记得加密pubId
          Long pubId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(des3PubId));
          // Long pubId = NumberUtils.toLong(des3PubId);
          if (pubId != 0) {
            boolean isOwner = pubSnsDAO.isOwnerOfPub(psnId, pubId);
            if (isOwner) {
              RepresentPub repPub = representPubDao.findPsnRepresentPub(psnId, pubId);
              if (repPub == null) {
                repPub = new RepresentPub();
              }
              repPub.setRepPsnId(psnId);
              repPub.setRepPubId(pubId);
              repPub.setStatus(0);
              repPub.setSeqNo(seqNo++);
              representPubDao.save(repPub);
            }
          }
        }
      }
    }
  }

  @Override
  public void deleteByPsnIdAndPubId(Long pubId, Long psnId) throws ServiceException {
    try {
      if (NumberUtils.isNotNullOrZero(psnId) && NumberUtils.isNotNullOrZero(pubId)) {
        RepresentPub repPub = representPubDao.findPsnRepresentPub(psnId, pubId);
        if (repPub != null && !CommonUtils.compareIntegerValue(repPub.getStatus(), 1)) {
          RepresentPubPk pkId = new RepresentPubPk();
          pkId.setPsnId(psnId);
          pkId.setPubId(pubId);
          representPubDao.delete(pkId);
        }
      }
    } catch (Exception e) {
      logger.error("删除人员代表性成果出错！pubId={},psnId={}", pubId, psnId, e);
      throw new ServiceException(e);
    }
  }
}
