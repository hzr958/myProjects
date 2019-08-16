package com.smate.center.open.service.publication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
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

import com.smate.center.open.dao.publication.PublicationDao;
import com.smate.center.open.exception.OpenNsfcException;
import com.smate.center.open.form.PublicationForm;
import com.smate.center.open.model.publication.PubMember;
import com.smate.center.open.service.nsfc.IrisExcludedPubService;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果、参考文献SERVICE. 增删改
 * 
 * @author ajb
 * 
 */
@Service("publicationService")
@Transactional(rollbackFor = Exception.class)
public class PublicationServiceImpl implements PublicationService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private IrisExcludedPubService irisExcludedPubService;
  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;
  @Value("${domainscm}")
  public String domainscm;
  @Resource(name = "restTemplate")
  protected RestTemplate restTemplate;

  /**
   * 获取成果.
   */
  @Override
  public Publication getPub(Long pubId) throws Exception {
    return this.publicationDao.get(pubId);
  }

  @Override
  public String buildFinalPrjAuthorName(Long pubId) throws Exception {
    StringBuilder authorNames = new StringBuilder();
    try {
      String splitStr = "、";
      String endStr = "……";
      List<PubMember> authors = publicationDao.getPubMembersByPubId(pubId);
      if (CollectionUtils.isNotEmpty(authors)) {
        for (int i = 0; i < authors.size(); i++) {
          PubMember item = authors.get(i);
          String name = StringUtils.trimToEmpty(item.getName());
          if ("".equals(name)) {
            continue;
          }
          Long psnId = item.getPsnId();
          if (1 == item.getAuthorPos()) {
            name = "*" + name; // 是通讯作者，则名称前加*号
          }

          if (null == psnId) {

            // 缩略
            if (authorNames.length() < 200
                && (authorNames.length() + splitStr.length() + name.length() + endStr.length()) > 200) {
              return authorNames.toString() + endStr;
            }
            if (authorNames.length() > 0) {
              authorNames.append(splitStr);
            }
            authorNames.append(name);
          } else {
            // 缩略
            name = String.format("<strong>%s</strong>", name);
            if (authorNames.length() < 200
                && (authorNames.length() + splitStr.length() + name.length() + endStr.length()) > 200) {
              return authorNames.toString() + endStr;
            }
            if (authorNames.length() > 0) {
              authorNames.append(splitStr);
            }
            authorNames.append(name); // 可以关联到psn_id,则加粗显示
          }
        }
      }
    } catch (Exception e) {
      logger.error("读取成果成员列表错误;pubId: " + pubId, e);
      throw new Exception(e);
    }
    return authorNames.toString();
  }

  @Override
  public Long getPsnPublicPubCount(Long psnId, String keywords, String excludedPubIds, List<Integer> permissions,
      String pubTypes) throws Exception {
    try {
      String uuid = null;
      if (StringUtils.isNotBlank(excludedPubIds)) {
        uuid = UUID.randomUUID().toString();
        this.irisExcludedPubService.saveIrisExcludedPub(ServiceUtil.splitStrToLong(excludedPubIds), uuid);
      }
      List<Integer> pubTypeList = ServiceUtil.splitStrToInteger(pubTypes);
      if (CollectionUtils.isEmpty(permissions)) {
        permissions = new ArrayList<Integer>();
        permissions.add(PsnCnfConst.ALLOWS);// 默认公开
      }

      /*
       * String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL; PubQueryDTO pubQueryDTO = new
       * PubQueryDTO(); pubQueryDTO.setSearchPsnId(psnId); pubQueryDTO.setIsAll(1);
       * pubQueryDTO.setPubType(pubTypes); pubQueryDTO.setPermissions(permissions);
       * pubQueryDTO.setUuid(uuid); pubQueryDTO.setSearchKey(keywords);
       * 
       * pubQueryDTO.setServiceType(V8pubQueryPubConst.OPEN_PSN_PUBLIC_PUB ); Map<String, Object> result =
       * (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL); Long totalCount = 0L; if
       * (result.get("status").equals("success")) { totalCount =
       * Long.parseLong(result.get("totalCount").toString()); }
       */
      // 查询关联权限表的公开成果数
      Long pubCount1 = this.publicationDao.queryPsnPublicPubCount(psnId, keywords, uuid, permissions, pubTypeList);

      if (StringUtils.isNotBlank(excludedPubIds)) {
        this.irisExcludedPubService.deleteIrisExcludedPub(uuid);
      }
      // 获取因其它导入方式在权限表没有记录的成果数，这些成果默认为公开
      Long pubCount2 = this.publicationDao.getPsnNotExistsResumePubCount(psnId);

      return (pubCount1 + pubCount2);
      // return totalCount;
    } catch (Exception e) {
      logger.error(String.format("查询用户psnId=${1}的公开成果数出现异常：", psnId), e);
      throw new Exception(e);
    }
  }

  @Override
  public Long getPsnPubCount(Long psnId, String keywords, String authors, String excludedPubIds, String pubTypes)
      throws Exception {
    try {
      String uuid = null;
      if (StringUtils.isNotBlank(excludedPubIds)) {
        uuid = UUID.randomUUID().toString();
        this.irisExcludedPubService.saveIrisExcludedPub(ServiceUtil.splitStrToLong(excludedPubIds), uuid);
      }
      List<Integer> pubTypeList = ServiceUtil.splitStrToInteger(pubTypes);
      Long pubCount = this.publicationDao.queryPsnPubCount(psnId, keywords, authors, uuid, pubTypeList);

      if (StringUtils.isNotBlank(excludedPubIds)) {
        this.irisExcludedPubService.deleteIrisExcludedPub(uuid);
      }

      return pubCount;
    } catch (Exception e) {
      logger.error(String.format("查询用户psnId=${1}的成果数出现异常：", psnId), e);
      throw new Exception(e);
    }
  }

  @Override
  public Page<Publication> getPsnPublicPubByPage(Long psnId, String keywords, String excludedPubIds,
      List<Integer> permissions, String sortType, Page<Publication> page, String pubTypes) throws Exception {
    try {
      String uuid = null;
      if (StringUtils.isNotBlank(excludedPubIds)) {
        uuid = UUID.randomUUID().toString();
        this.irisExcludedPubService.saveIrisExcludedPub(ServiceUtil.splitStrToLong(excludedPubIds), uuid);
      }
      List<Integer> pubTypeList = ServiceUtil.splitStrToInteger(pubTypes);
      if (CollectionUtils.isEmpty(permissions)) {
        permissions = new ArrayList<Integer>();
        permissions.add(PsnCnfConst.ALLOWS);// 默认公开
      }
      page =
          this.publicationDao.queryPsnPublicPubByPage(psnId, keywords, uuid, permissions, sortType, page, pubTypeList);

      if (StringUtils.isNotBlank(excludedPubIds)) {
        this.irisExcludedPubService.deleteIrisExcludedPub(uuid);
      }

      return page;
    } catch (Exception e) {
      logger.error(String.format("查询用户psnId=${1}的公开成果记录出现异常：", psnId), e);
      throw new OpenNsfcException(e);
    }
  }

  @Override
  public String getPubXmlById(Long pubId) throws Exception {
    PublicationForm form = new PublicationForm();
    form.setPubId(pubId);
    try {
      form = this.scholarPublicationXmlManager.loadXml(form);
    } catch (Exception e) {
      logger.error("没有找到pubId为" + form.getPubId() + "的xml数据");
    }
    return form.getPubXml();
  }

  @Override
  public Page<Publication> getPsnPubByPage(Long psnId, String keywords, String authors, String excludedPubIds,
      String sortType, Page<Publication> page, String pubTypes) throws Exception {
    try {
      String uuid = null;
      if (StringUtils.isNotBlank(excludedPubIds)) {
        uuid = UUID.randomUUID().toString();
        this.irisExcludedPubService.saveIrisExcludedPub(ServiceUtil.splitStrToLong(excludedPubIds), uuid);
      }
      List<Integer> pubTypeList = ServiceUtil.splitStrToInteger(pubTypes);
      page = this.publicationDao.queryPsnPubByPage(psnId, keywords, authors, uuid, sortType, page, pubTypeList);

      if (StringUtils.isNotBlank(excludedPubIds)) {
        this.irisExcludedPubService.deleteIrisExcludedPub(uuid);
      }

      return page;
    } catch (Exception e) {
      logger.error(String.format("查询用户psnId=${1}的成果记录出现异常：", psnId), e);
      throw new Exception(e);
    }
  }

  /**
   * 
   * @author liangguokeng
   */
  @Override
  public List<Publication> findPubIdsByPsnId(Long psnId) throws Exception {
    try {
      return publicationDao.findPubIdsByPsnId(psnId);
    } catch (Exception e) {
      logger.error("Open系统，查询成果Id出错", e);
    }
    return null;
  }

  /**
   * 查询远程成果的信息
   * 
   * @param pubQueryDTO
   * @param SERVER_URL
   * @return
   */
  public Object getRemotePubInfo(PubQueryDTO pubQueryDTO, String SERVER_URL) {
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity =
        new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(pubQueryDTO), requestHeaders);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    return object;
  }
}
