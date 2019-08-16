package com.smate.web.v8pub.service.sns.resume;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.v8pub.restTemp.service.PubRestemplateService;
import com.smate.web.v8pub.dao.resume.CVModuleInfoDao;
import com.smate.web.v8pub.dao.resume.PsnResumeDao;
import com.smate.web.v8pub.dao.resume.ResumeModuleDao;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.sns.PubMemberService;
import com.smate.web.v8pub.service.sns.indexurl.PubIndexUrlService;
import com.smate.web.v8pub.utils.PubMemberNameUtils;
import com.smate.web.v8pub.vo.PersonalResumePubVO;
import com.smate.web.v8pub.vo.sns.newresume.CVModuleInfo;
import com.smate.web.v8pub.vo.sns.newresume.CVPubInfo;
import com.smate.web.v8pub.vo.sns.newresume.PsnResume;
import com.smate.web.v8pub.vo.sns.newresume.ResumeModule;

/**
 * 个人简历-----成果服务实现类
 * 
 * @author WSN
 *
 */
@Service("personalResumePubService")
@Transactional(rollbackFor = Exception.class)
public class PersonalResumePubServiceImpl implements PersonalResumePubService {

  private static Logger logger = LoggerFactory.getLogger(PersonalResumePubServiceImpl.class);
  @Autowired
  private PsnResumeDao psnResumeDao;
  @Autowired
  private ResumeModuleDao resumeModuleDao;
  @Autowired
  private CVModuleInfoDao cVModuleInfoDao;
  @Autowired
  private PubRestemplateService pubRestemplateService;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubIndexUrlService pubIndexUrlService;
  @Value("${domainscm}")
  public String domainscm;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PubMemberService pubMemberService;

  @Override
  public boolean isOwnerOfCV(Long cvId, Long psnId) throws Exception {
    boolean isOwner = false;
    PsnResume resume = psnResumeDao.findPsnResumeByPsnIdAndResumeId(psnId, cvId);
    if (resume != null) {
      isOwner = true;
    }
    return isOwner;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<PubInfo> findPsnCVSelectedPub(Long cvId, Long psnId, Integer moduleId) throws Exception {
    List<PubInfo> pubInfoList = new ArrayList<PubInfo>();
    try {
      Long moduleInfoId = resumeModuleDao.findCVModuleInfoId(cvId, moduleId);
      if (moduleInfoId != null) {
        CVModuleInfo info = cVModuleInfoDao.get(moduleInfoId);
        if (info != null && StringUtils.isNotBlank(info.getModuleInfo())) {
          // json转list
          List<CVPubInfo> cvPubList =
              JacksonUtils.jsonToCollection(info.getModuleInfo(), ArrayList.class, CVPubInfo.class);
          if (CollectionUtils.isNotEmpty(cvPubList)) {
            Map<Long, Integer> seqMap = new HashMap<Long, Integer>();
            // 获取选中的成果ID
            List<Long> pubIds = new ArrayList<Long>();
            for (CVPubInfo pub : cvPubList) {
              pubIds.add(pub.getPubId());
              seqMap.put(pub.getPubId(), pub.getSeqNo());
            }
            // 获取选中成果信息
            pubInfoList = restTemplatePost(pubIds, psnId);
            if (CollectionUtils.isNotEmpty(pubInfoList)) {
              for (PubInfo pub : pubInfoList) {
                pub.setSeqNo(seqMap.get(pub.getPubId()));
              }
            }
            Collections.sort(pubInfoList);
          }
        }
      }
    } catch (Exception e) {
      logger.error("构建人员简历中选中的成果信息出错，cvId = " + cvId + ", psnId=" + psnId + ", moduleId=" + moduleId, e);
      throw new Exception(e);
    }
    return pubInfoList;
  }

  private List<PubInfo> getInfoList(List<Long> pubIds, Long psnId) {
    List<PubInfo> pubInfoList = new ArrayList<PubInfo>();
    String locale = LocaleContextHolder.getLocale().toString();
    List<PubPO> pubList = pubSnsDAO.queryPubPOByPubIds(pubIds);
    if (CollectionUtils.isNotEmpty(pubList)) {
      for (PubPO pubPo : pubList) {
        PubSnsPO pubSnsPO = (PubSnsPO) pubPo;
        PubInfo pubInfo = new PubInfo();
        pubInfo.setPubId(pubSnsPO.getPubId());
        pubInfo.setPubType(pubSnsPO.getPubType());
        List<PubMemberPO> menbers = pubMemberService.findByPubId(pubSnsPO.getPubId());
        Person psn = this.personProfileDao.getPsnAllName(psnId);
        String currentPsnName = "zh_CN".equals(locale) ? psn.getZhName() : psn.getEname();
        String authors = PubMemberNameUtils.getSNSPubMemberName(menbers, pubInfo.getPubType(), currentPsnName);
        pubInfo.setAuthorNames(authors);

        String awardAuthorList = PubMemberNameUtils.getSNSPubMemberNameList(menbers, currentPsnName);
        String authorList = awardAuthorList.replaceAll("sup", "sub");
        pubInfo.setAwardAuthorList(authorList);// 奖励作者

        buildSnsPubIndexUrl(pubInfo, psnId);// 构建短地址
        pubInfo.setTitle(pubSnsPO.getTitle());
        pubInfo.setBriefDesc(pubSnsPO.getBriefDesc());
        pubInfo.setPubTypeName(findPubTypeName(pubInfo.getPubType(), locale));
        pubInfoList.add(pubInfo);
      }
    }
    return pubInfoList;
  }

  public void buildSnsPubIndexUrl(PubInfo pubInfo, Long psnId) {
    PubIndexUrl pubIndexUrl = pubIndexUrlService.get(pubInfo.getPubId());
    if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
      pubInfo.setPubIndexUrl(domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
    }

  }

  /**
   * 查找成果
   * 
   * @param psnId
   * @return
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  private List<PubInfo> restTemplatePost(List<Long> pubIds, Long psnId) throws Exception {
    String pubJson = pubRestemplateService.pubListQueryByPubIds(pubIds, psnId);
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(pubJson);
    List<PubInfo> list = new ArrayList<>();
    if (resultMap.get("resultList") != null) {
      list = new ObjectMapper().readValue(JacksonUtils.jsonObjectSerializer(resultMap.get("resultList")),
          new TypeReference<List<PubInfo>>() {});
    }
    return list;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void buildPsnResumePubInfo(PersonalResumePubVO form) throws Exception {
    try {
      boolean hasPubInfo = false;
      // 查找简历成果代表性成果模块信息
      Long moduleInfoId = resumeModuleDao.findCVModuleInfoId(form.getCvId(), form.getModuleId());
      if (moduleInfoId != null) {
        CVModuleInfo module = cVModuleInfoDao.get(moduleInfoId);
        if (module != null && StringUtils.isNotBlank(module.getModuleInfo())) {
          String moduleInfo = module.getModuleInfo();
          List<CVPubInfo> pubInfoList =
              (List<CVPubInfo>) JacksonUtils.jsonToCollection(moduleInfo, ArrayList.class, CVPubInfo.class);
          if (CollectionUtils.isNotEmpty(pubInfoList)) {
            hasPubInfo = true;
            List<Long> pubIds = new ArrayList<Long>();
            Map<Long, Integer> seqMap = new HashMap<Long, Integer>();
            for (CVPubInfo pubInfo : pubInfoList) {
              pubIds.add(pubInfo.getPubId());
              seqMap.put(pubInfo.getPubId(), pubInfo.getSeqNo());
            }
            form.setPubInfoList(getInfoList(pubIds, form.getPsnId()));// 构建pubInfoList
          }
        }
      }
      if (!hasPubInfo) {
        form.setPubInfoList(null);
      }
    } catch (Exception e) {
      logger.error("构建人员简历成果信息出错， resumeId = " + form.getResumeId() + ", moduleId=" + form.getModuleId(), e);
      throw new Exception(e);
    }
  }

  /**
   * 获取论文类别名称
   * 
   * @param pubType
   * @param locale
   * @return
   */
  private String findPubTypeName(Integer pubType, String locale) {
    // TODO 国际化
    switch (pubType.intValue()) {
      case PublicationTypeEnum.AWARD:
        return "zh_CN".equals(locale) ? "奖励" : "Award";
      case PublicationTypeEnum.CONFERENCE_PAPER:
        return "zh_CN".equals(locale) ? "会议论文" : "Conference Paper";
      case PublicationTypeEnum.JOURNAL_ARTICLE:
        return "zh_CN".equals(locale) ? "期刊论文" : "Journal Article";
      case PublicationTypeEnum.PATENT:
        return "zh_CN".equals(locale) ? "专利" : "Patent";
      case PublicationTypeEnum.BOOK:
        return "zh_CN".equals(locale) ? "书/著作" : "Book/Monograph";
      case PublicationTypeEnum.BOOK_CHAPTER:
        return "zh_CN".equals(locale) ? "书籍章节" : "Book Chapter";
      case PublicationTypeEnum.THESIS:
        return "zh_CN".equals(locale) ? "学位论文" : "Thesis";
      case PublicationTypeEnum.STANDARD:
        return "zh_CN".equals(locale) ? "标准" : "Standard";
      case PublicationTypeEnum.SOFTWARE_COPYRIGHT:
        return "zh_CN".equals(locale) ? "软件著作权" : "Software Copyright";
      case PublicationTypeEnum.OTHERS:
        return "zh_CN".equals(locale) ? "其他" : "Others";
      default:
        return "";
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean checkPubInfo(PersonalResumePubVO form) throws Exception {
    boolean result = true;
    try {
      if (StringUtils.isNotBlank(form.getModuleInfo())) {
        List<CVPubInfo> pubInfoList =
            (List<CVPubInfo>) JacksonUtils.jsonToCollection(form.getModuleInfo(), ArrayList.class, CVPubInfo.class);
        if (CollectionUtils.isNotEmpty(pubInfoList)) {
          List<Long> pubIds = new ArrayList<Long>();
          for (CVPubInfo info : pubInfoList) {
            info.setPubId(NumberUtils.toLong(Des3Utils.decodeFromDes3(info.getDes3PubId())));
            pubIds.add(info.getPubId());
          }
          // 校验全部的成果是否是当前用户的
          Long count = pubSnsDAO.getPsnOwnerPubCount(pubIds, SecurityUtils.getCurrentUserId());
          if (count != pubInfoList.size()) {
            result = false;
          }
        }
        form.setModuleInfo(JacksonUtils.listToJsonStr(pubInfoList));
      }
    } catch (Exception e) {
      throw new Exception(e);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void savePsnResumeModule(PersonalResumePubVO form) throws Exception {
    try {
      Long cvId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3ResumeId()));
      Integer moduleId = form.getModuleId();
      // 先判断是否是简历拥有者
      if (isPsnResumeOwner(cvId)) {
        // 保存对应的模块信息
        ResumeModule module = resumeModuleDao.findResumeModuleByResumeIdAndModuleId(cvId, moduleId);
        if (module == null) {
          module = new ResumeModule();
          module.setResumeId(cvId);
          module.setModuleId(moduleId);
        }
        // 保存模块json信息
        CVModuleInfo info = null;
        if (module.getModuleInfoId() != null) {
          info = cVModuleInfoDao.get(module.getModuleInfoId());
        }
        if (info == null) {
          info = new CVModuleInfo();
        }
        info.setModuleInfo(form.getModuleInfo());
        cVModuleInfoDao.save(info);
        // 保存模块基本信息
        module.setModuleSeq(form.getModuleSeq());
        module.setModuleTitle(form.getModuleTitle());
        module.setUpdateTime(new Date());
        module.setStatus(0);
        module.setModuleInfoId(info.getId());
        resumeModuleDao.save(module);
        psnResumeDao.updatePsnResumeUpdateTime(cvId);
      }
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /**
   * 是否是简历拥有者
   * 
   * @param resumeId
   * @return
   */
  public boolean isPsnResumeOwner(Long resumeId) throws Exception {
    boolean isOwner = false;
    PsnResume resume = psnResumeDao.findPsnResumeByPsnIdAndResumeId(SecurityUtils.getCurrentUserId(), resumeId);
    if (resume != null) {
      isOwner = true;
    }
    return isOwner;
  }
}
