package com.smate.web.v8pub.service.handler.assembly.dispose;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.ConferencePaperBean;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.OtherInfoBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.dom.ThesisInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.enums.DbIdPriorityEnum;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.utils.CopyPropertiesUtils;

/**
 * 基准库成果更新时，数据更新优先级处理 <br/>
 * 规则： <br/>
 * 1.dbId优先级ISI > EI > SCOPUS > CNKI > WANGFANG > CNKIPAT > CNIPR <br/>
 * 2.优先级高的替换优先级低的成果的信息<br/>
 * 3.优先级低的补充优先级高的不完善的成果字段信息<br/>
 * 
 * @author YJ
 *
 *         2018年9月5日
 */
@Transactional(rollbackFor = Exception.class)
public class ASDisposePdwhUpdatePriorityImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    if (NumberUtils.isNullOrZero(pub.pubId)) {
      throw new PubHandlerAssemblyException("更新优先级业务处理失败，pubId为null");
    }
    PubPdwhDetailDOM pdwhDetail = pubPdwhDetailService.getByPubId(pub.pubId);
    if (pdwhDetail == null) {
      throw new PubHandlerAssemblyException("更新优先级业务处理失败，成果数据不存在");
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      PubPdwhDetailDOM pdwhDetail = pubPdwhDetailService.getByPubId(pub.pubId);
      if (pdwhDetail != null) {
        boolean priorityFlag = getPriorityFlag(pdwhDetail.getSrcDbId(), pub.srcDbId);
        if (priorityFlag) {
          // 进行数据的替换，数据为空的话也要补全，以传过来的数据为准
          pub.title = StringUtils.isNotEmpty(pub.title) ? pub.title : pdwhDetail.getTitle();
          pub.summary = StringUtils.isNotEmpty(pub.summary) ? pub.summary : pdwhDetail.getSummary();
          pub.keywords = StringUtils.isNotEmpty(pub.keywords) ? pub.keywords : pdwhDetail.getKeywords();
          pub.countryId = NumberUtils.isNotNullOrZero(pub.countryId) ? pub.countryId : pdwhDetail.getCountryId();
          pub.citations = (pub.citations != null) ? pub.citations : pdwhDetail.getCitations();
          pub.briefDesc = StringUtils.isNotEmpty(pub.briefDesc) ? pub.briefDesc : pdwhDetail.getBriefDesc();
          pub.authorNames = StringUtils.isNotEmpty(pub.authorNames) ? pub.authorNames : pdwhDetail.getAuthorNames();
          pub.doi = StringUtils.isNotEmpty(pub.doi) ? pub.doi : pdwhDetail.getDoi();
          pub.fulltextId = NumberUtils.isNotNullOrZero(pub.fulltextId) ? pub.fulltextId : pdwhDetail.getFulltextId();
          pub.organization = StringUtils.isNotEmpty(pub.organization) ? pub.organization : pdwhDetail.getOrganization();
          pub.fundInfo = StringUtils.isNotEmpty(pub.fundInfo) ? pub.fundInfo : pdwhDetail.getFundInfo();
          pub.srcFulltextUrl =
              StringUtils.isNotEmpty(pub.srcFulltextUrl) ? pub.srcFulltextUrl : pdwhDetail.getSrcFulltextUrl();
          pub.publishDate = StringUtils.isNotEmpty(pub.publishDate) ? pub.publishDate : pdwhDetail.getPublishDate();

          pub.pubTypeInfo = buildPubTypeInfo(pub.pubTypeInfo, pub.pubType, pdwhDetail.getPubType(),
              pdwhDetail.getTypeInfo(), priorityFlag);

          // 数据的合并
          pub.situations = buildSituations(pub.situations, pdwhDetail.getSituations());

          pub.sourceId = StringUtils.isNotEmpty(pub.sourceId) ? pub.sourceId : pdwhDetail.getSourceId();
          pub.sourceUrl = StringUtils.isNotEmpty(pub.sourceUrl) ? pub.sourceUrl : pdwhDetail.getSourceUrl();
          pub.citedUrl = StringUtils.isNotEmpty(pub.citedUrl) ? pub.citedUrl : pdwhDetail.getCitedUrl();

          pub.OA = StringUtils.isNotEmpty(pub.OA) ? pub.OA : pdwhDetail.getOA();
          pub.HCP = (pub.HCP != null) ? pub.HCP : (pdwhDetail.isHCP() ? 1 : 0);
          pub.HP = (pub.HP != null) ? pub.HP : (pdwhDetail.isHP() ? 1 : 0);
        } else {
          // 现将低优先级的引用次数与dbId保存起来
          pub.bakDbId = pub.srcDbId;
          pub.bakCitations = pub.citations == null ? 0 : pub.citations;
          // 进行数据的补全，以数据库数据为准
          pub.title = StringUtils.isNotEmpty(pdwhDetail.getTitle()) ? pdwhDetail.getTitle() : pub.title;
          pub.summary = StringUtils.isNotEmpty(pdwhDetail.getSummary()) ? pdwhDetail.getSummary() : pub.summary;
          pub.keywords = StringUtils.isNotEmpty(pdwhDetail.getKeywords()) ? pdwhDetail.getKeywords() : pub.keywords;
          pub.countryId =
              NumberUtils.isNotNullOrZero(pdwhDetail.getCountryId()) ? pdwhDetail.getCountryId() : pub.countryId;
          pub.citations = (pdwhDetail.getCitations() != null) ? pdwhDetail.getCitations() : pub.citations;
          pub.briefDesc = StringUtils.isNotEmpty(pdwhDetail.getBriefDesc()) ? pdwhDetail.getBriefDesc() : pub.briefDesc;
          pub.authorNames =
              StringUtils.isNotEmpty(pdwhDetail.getAuthorNames()) ? pdwhDetail.getAuthorNames() : pub.authorNames;
          pub.doi = StringUtils.isNotEmpty(pdwhDetail.getDoi()) ? pdwhDetail.getDoi() : pub.doi;
          pub.fulltextId =
              NumberUtils.isNotNullOrZero(pdwhDetail.getFulltextId()) ? pdwhDetail.getFulltextId() : pub.fulltextId;
          pub.organization =
              StringUtils.isNotEmpty(pdwhDetail.getOrganization()) ? pdwhDetail.getOrganization() : pub.organization;
          pub.fundInfo = StringUtils.isNotEmpty(pdwhDetail.getFundInfo()) ? pdwhDetail.getFundInfo() : pub.fundInfo;
          pub.pubType = pdwhDetail.getPubType();
          pub.srcFulltextUrl = StringUtils.isNotEmpty(pdwhDetail.getSrcFulltextUrl()) ? pdwhDetail.getSrcFulltextUrl()
              : pub.srcFulltextUrl;
          pub.publishDate =
              StringUtils.isNotEmpty(pdwhDetail.getPublishDate()) ? pdwhDetail.getPublishDate() : pub.publishDate;

          pub.pubTypeInfo = buildPubTypeInfo(pub.pubTypeInfo, pub.pubType, pdwhDetail.getPubType(),
              pdwhDetail.getTypeInfo(), priorityFlag);
          // 以数据库存储的为主
          pub.members = JSONArray.parseArray(JacksonUtils.jsonObjectSerializer(pdwhDetail.getMembers()));
          // 数据的合并
          pub.situations = buildSituations(pub.situations, pdwhDetail.getSituations());

          pub.srcDbId = pdwhDetail.getSrcDbId();
          pub.sourceId = StringUtils.isNotEmpty(pdwhDetail.getSourceId()) ? pdwhDetail.getSourceId() : pub.sourceId;
          pub.sourceUrl = StringUtils.isNotEmpty(pdwhDetail.getSourceUrl()) ? pdwhDetail.getSourceUrl() : pub.sourceUrl;
          pub.citedUrl = StringUtils.isNotEmpty(pdwhDetail.getCitedUrl()) ? pdwhDetail.getCitedUrl() : pub.citedUrl;

          pub.OA = StringUtils.isNotEmpty(pdwhDetail.getOA()) ? pdwhDetail.getOA() : pub.OA;
          pub.HCP = pdwhDetail.isHCP() ? 1 : pub.HCP;
          pub.HP = pdwhDetail.isHP() ? 1 : pub.HP;
        }
      }
    } catch (Exception e) {
      logger.error("处理更新优先级的逻辑出错！", e);
      throw new PubHandlerAssemblyException(
          this.getClass().getSimpleName() + "处理基准库更新优先级的逻辑出错! title=" + pub.title + ",insId=" + pub.insId, e);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private static JSONArray buildSituations(JSONArray situations, Set<PubSituationBean> setBean) {
    // 传入的收录情况数据
    List<PubSituationDTO> sitList =
        JacksonUtils.jsonToCollection(situations.toJSONString(), List.class, PubSituationDTO.class);
    if (CollectionUtils.isEmpty(sitList)) {
      return JSONArray.parseArray(JacksonUtils.jsonObjectSerializer(setBean));
    }
    if (CollectionUtils.isEmpty(setBean)) {
      return situations;
    }
    List<String> sitName = sitList.stream().map(PubSituationDTO::getLibraryName).collect(Collectors.toList());
    for (PubSituationBean bean : setBean) {
      if (sitName.contains(bean.getLibraryName().toUpperCase())) {
        for (PubSituationDTO dto : sitList) {
          if (bean.getLibraryName().equalsIgnoreCase(dto.getLibraryName())) {
            dto.setSitOriginStatus(dto.isSitOriginStatus() ? dto.isSitOriginStatus() : bean.isSitOriginStatus());
            dto.setSitStatus(dto.isSitStatus() ? dto.isSitStatus() : bean.isSitStatus());
            dto.setSrcDbId(StringUtils.isNotEmpty(dto.getSrcDbId()) ? dto.getSrcDbId() : bean.getSrcDbId());
            dto.setSrcId(StringUtils.isNotEmpty(dto.getSrcId()) ? dto.getSrcId() : bean.getSrcId());
            dto.setSrcUrl(StringUtils.isNotEmpty(dto.getSrcUrl()) ? dto.getSrcUrl() : bean.getSrcUrl());
          }
        }
      } else {
        PubSituationDTO pubSituationDTO = new PubSituationDTO();
        pubSituationDTO.setLibraryName(bean.getLibraryName());
        pubSituationDTO.setSitOriginStatus(bean.isSitOriginStatus());
        pubSituationDTO.setSitStatus(bean.isSitStatus());
        pubSituationDTO.setSrcDbId(bean.getSrcDbId());
        pubSituationDTO.setSrcId(bean.getSrcId());
        pubSituationDTO.setSrcUrl(bean.getSrcUrl());
        sitList.add(pubSituationDTO);
      }
    }

    return JSONArray.parseArray(JacksonUtils.jsonObjectSerializer(sitList));
  }

  /**
   * 构建成果pubTypeInfo字段
   * 
   * @param newInfo 传入的数据
   * @param newPubType 传入的成果类型
   * @param oldPubType 原成果类型
   * @param oldBean 原成果数据
   * @param priorityFlag 优先级，true 以pubTypeInfo为主，false以oldBean为主
   * @return
   * 
   *         具体pubTypeInfo字段的优先级规则逻辑，见SCM-21513
   */
  private static JSONObject buildPubTypeInfo(JSONObject newInfo, Integer newPubType, Integer oldPubType,
      PubTypeInfoBean oldBean, boolean priorityFlag) {
    PubTypeInfoBean typeInfoBean = null;
    if (priorityFlag) {
      // 传入优先级高，以传入数据为主
      if (newPubType.equals(oldPubType)) {
        if (newInfo != null) {
          switch (newPubType) {
            case PublicationTypeEnum.AWARD:
              AwardsInfoBean newA = JacksonUtils.jsonObject(newInfo.toJSONString(), AwardsInfoBean.class);
              if (oldBean != null) {
                AwardsInfoBean oldA = (AwardsInfoBean) oldBean;
                CopyPropertiesUtils.MergePropertiesValue(newA, oldA);
                typeInfoBean = oldA;
              } else {
                typeInfoBean = newA;
              }
              break;
            case PublicationTypeEnum.BOOK:
            case PublicationTypeEnum.BOOK_CHAPTER:
              BookInfoBean newB = JacksonUtils.jsonObject(newInfo.toJSONString(), BookInfoBean.class);
              if (oldBean != null) {
                BookInfoBean oldB = (BookInfoBean) oldBean;
                CopyPropertiesUtils.MergePropertiesValue(newB, oldB);
                typeInfoBean = oldB;
              } else {
                typeInfoBean = newB;
              }
              break;
            case PublicationTypeEnum.CONFERENCE_PAPER:
              ConferencePaperBean newC = JacksonUtils.jsonObject(newInfo.toJSONString(), ConferencePaperBean.class);
              if (oldBean != null) {
                ConferencePaperBean oldC = (ConferencePaperBean) oldBean;
                CopyPropertiesUtils.MergePropertiesValue(newC, oldC);
                typeInfoBean = oldC;
              } else {
                typeInfoBean = newC;
              }
              break;
            case PublicationTypeEnum.JOURNAL_ARTICLE:
              JournalInfoBean newJ = JacksonUtils.jsonObject(newInfo.toJSONString(), JournalInfoBean.class);
              if (oldBean != null) {
                JournalInfoBean oldJ = (JournalInfoBean) oldBean;
                CopyPropertiesUtils.MergePropertiesValue(newJ, oldJ);
                typeInfoBean = oldJ;
              } else {
                typeInfoBean = newJ;
              }
              break;
            case PublicationTypeEnum.PATENT:
              PatentInfoBean newP = JacksonUtils.jsonObject(newInfo.toJSONString(), PatentInfoBean.class);
              if (oldBean != null) {
                PatentInfoBean oldP = (PatentInfoBean) oldBean;
                CopyPropertiesUtils.MergePropertiesValue(newP, oldP);
                typeInfoBean = oldP;
              } else {
                typeInfoBean = newP;
              }
              break;
            case PublicationTypeEnum.THESIS:
              ThesisInfoBean newT = JacksonUtils.jsonObject(newInfo.toJSONString(), ThesisInfoBean.class);
              if (oldBean != null) {
                ThesisInfoBean oldT = (ThesisInfoBean) oldBean;
                CopyPropertiesUtils.MergePropertiesValue(newT, oldT);
                typeInfoBean = oldT;
              } else {
                typeInfoBean = newT;
              }
              break;
            case PublicationTypeEnum.STANDARD:
              StandardInfoBean newS = JacksonUtils.jsonObject(newInfo.toJSONString(), StandardInfoBean.class);
              if (oldBean != null) {
                StandardInfoBean oldS = (StandardInfoBean) oldBean;
                CopyPropertiesUtils.MergePropertiesValue(newS, oldS);
                typeInfoBean = oldS;
              } else {
                typeInfoBean = newS;
              }
              break;
            case PublicationTypeEnum.SOFTWARE_COPYRIGHT:
              SoftwareCopyrightBean newSC =
                  JacksonUtils.jsonObject(newInfo.toJSONString(), SoftwareCopyrightBean.class);
              if (oldBean != null) {
                SoftwareCopyrightBean oldSC = (SoftwareCopyrightBean) oldBean;
                CopyPropertiesUtils.MergePropertiesValue(newSC, oldSC);
                typeInfoBean = oldSC;
              } else {
                typeInfoBean = newSC;
              }
              break;
            default:
              // 其他
              typeInfoBean = new OtherInfoBean();
          }
        } else {
          typeInfoBean = oldBean;
        }
      } else {
        // 两次传入的成果类型不同，直接保存高优先级的数据
        return newInfo;
      }
    } else {
      // 传入优先级低，以数据库数据为主
      if (newPubType.equals(oldPubType)) {
        if (oldBean != null) {
          switch (newPubType) {
            case PublicationTypeEnum.AWARD:
              AwardsInfoBean oldA = (AwardsInfoBean) oldBean;
              if (newInfo != null) {
                AwardsInfoBean newA = JacksonUtils.jsonObject(newInfo.toJSONString(), AwardsInfoBean.class);
                CopyPropertiesUtils.MergePropertiesValue(oldA, newA);
                typeInfoBean = newA;
              } else {
                typeInfoBean = oldA;
              }
              break;
            case PublicationTypeEnum.BOOK:
            case PublicationTypeEnum.BOOK_CHAPTER:
              BookInfoBean oldB = (BookInfoBean) oldBean;
              if (newInfo != null) {
                BookInfoBean newB = JacksonUtils.jsonObject(newInfo.toJSONString(), BookInfoBean.class);
                CopyPropertiesUtils.MergePropertiesValue(oldB, newB);
                typeInfoBean = newB;
              } else {
                typeInfoBean = oldB;
              }
              break;
            case PublicationTypeEnum.CONFERENCE_PAPER:
              ConferencePaperBean oldC = (ConferencePaperBean) oldBean;
              if (newInfo != null) {
                ConferencePaperBean newC = JacksonUtils.jsonObject(newInfo.toJSONString(), ConferencePaperBean.class);
                CopyPropertiesUtils.MergePropertiesValue(oldC, newC);
                typeInfoBean = newC;
              } else {
                typeInfoBean = oldC;
              }
              break;
            case PublicationTypeEnum.JOURNAL_ARTICLE:
              JournalInfoBean oldJ = (JournalInfoBean) oldBean;
              if (newInfo != null) {
                JournalInfoBean newJ = JacksonUtils.jsonObject(newInfo.toJSONString(), JournalInfoBean.class);
                CopyPropertiesUtils.MergePropertiesValue(oldJ, newJ);
                typeInfoBean = newJ;
              } else {
                typeInfoBean = oldJ;
              }
              break;
            case PublicationTypeEnum.PATENT:
              PatentInfoBean oldP = (PatentInfoBean) oldBean;
              if (newInfo != null) {
                PatentInfoBean newP = JacksonUtils.jsonObject(newInfo.toJSONString(), PatentInfoBean.class);
                CopyPropertiesUtils.MergePropertiesValue(oldP, newP);
                typeInfoBean = newP;
              } else {
                typeInfoBean = oldP;
              }
              break;
            case PublicationTypeEnum.THESIS:
              ThesisInfoBean oldT = (ThesisInfoBean) oldBean;
              if (newInfo != null) {
                ThesisInfoBean newT = JacksonUtils.jsonObject(newInfo.toJSONString(), ThesisInfoBean.class);
                CopyPropertiesUtils.MergePropertiesValue(oldT, newT);
                typeInfoBean = newT;
              } else {
                typeInfoBean = oldT;
              }
              break;
            case PublicationTypeEnum.STANDARD:
              StandardInfoBean oldS = (StandardInfoBean) oldBean;
              if (newInfo != null) {
                StandardInfoBean newS = JacksonUtils.jsonObject(newInfo.toJSONString(), StandardInfoBean.class);
                CopyPropertiesUtils.MergePropertiesValue(oldS, newS);
                typeInfoBean = newS;
              } else {
                typeInfoBean = oldS;
              }
              break;
            case PublicationTypeEnum.SOFTWARE_COPYRIGHT:
              SoftwareCopyrightBean oldSC = (SoftwareCopyrightBean) oldBean;
              if (newInfo != null) {
                SoftwareCopyrightBean newSC =
                    JacksonUtils.jsonObject(newInfo.toJSONString(), SoftwareCopyrightBean.class);
                CopyPropertiesUtils.MergePropertiesValue(oldSC, newSC);
                typeInfoBean = newSC;
              } else {
                typeInfoBean = oldSC;
              }
              break;
            default:
              // 其他
              typeInfoBean = new OtherInfoBean();
          }
        } else {
          return newInfo;
        }
      } else {
        // 两次传入的成果类型不同，直接保存高优先级的数据
        typeInfoBean = oldBean;
      }
    }
    return JSONObject.parseObject(JacksonUtils.jsonObjectSerializer(typeInfoBean));
  }

  public static void main(String[] args) {
    JournalInfoDTO journalDto = new JournalInfoDTO();
    journalDto.setISSN("ISSN-002");
    journalDto.setIssue("ISSUE-002");
    journalDto.setJid(102L);
    journalDto.setName("NAME-002");
    journalDto.setPageNumber("PAGE_NUMBER_002");
    journalDto.setPublishStatus("A");
    journalDto.setVolumeNo("VNO-002");
    // JSONObject.parseObject(JacksonUtils.jsonObjectSerializer(journalDto))
    JSONObject newInfo = null;
    Integer newPubType = 4;
    Integer oldPubType = 4;
    JournalInfoBean journal = new JournalInfoBean();
    journal.setISSN("ISSN-001");
    journal.setIssue("ISSUE-001");
    journal.setJid(100L);
    journal.setName("NAME-001");
    journal.setPageNumber("PAGE_NUMBER_001");
    journal.setPublishStatus("P");
    journal.setVolumeNo("VNO-001");
    PubTypeInfoBean oldBean = null;
    boolean priorityFlag = false;
    System.out.println(buildPubTypeInfo(newInfo, newPubType, oldPubType, oldBean, priorityFlag));
  }


  /**
   * 是否高优先级 <br/>
   * 优先级的判断规则：ISI > EI > SCOPUS > CNKI > WANFANG > CNKIPAT > CNIPR <br/>
   * dbId: 15 > 14 > 8 > 4 > 10 = 21 > 11 <br/>
   * 
   * @param oldDbId 原dbId
   * @param newDbId 新传入的dbId
   * @return
   */
  private boolean getPriorityFlag(Integer oldDbId, Integer newDbId) {
    // 如果原有oldDbId为空，则覆盖
    if (oldDbId == null) {
      return true;
    }
    // 如果新newDbId为空，则不覆盖
    if (newDbId == null) {
      return false;
    }
    // 进行比较优先级
    Integer oldPriority = DbIdPriorityEnum.getPriority(oldDbId);
    Integer newPriority = DbIdPriorityEnum.getPriority(newDbId);
    if (newPriority >= oldPriority) {
      return true;
    }
    return false;
  }

}
