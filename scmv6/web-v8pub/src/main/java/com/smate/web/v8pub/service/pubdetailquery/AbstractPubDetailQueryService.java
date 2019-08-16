package com.smate.web.v8pub.service.pubdetailquery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.pub.enums.PubLibraryEnum;
import com.smate.core.base.pub.enums.PubPatentTransitionStatusEnum;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.core.base.pub.vo.Accessory;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dao.sns.PubIndustryDAO;
import com.smate.web.v8pub.dao.sns.PubScienceAreaDAO;
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.ConferencePaperBean;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.MemberInsBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.PubDetailDOM;
import com.smate.web.v8pub.dom.PubMemberBean;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.dom.ThesisInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.IndustryDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.ScienceAreaDTO;
import com.smate.web.v8pub.dto.SoftwareCopyrightDTO;
import com.smate.web.v8pub.dto.StandardInfoDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;
import com.smate.web.v8pub.po.journal.JournalPO;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.po.pdwh.PdwhPubMemberPO;
import com.smate.web.v8pub.po.sns.PubAccessoryPO;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.po.sns.PubIndustryPO;
import com.smate.web.v8pub.po.sns.PubScienceAreaPO;
import com.smate.web.v8pub.service.journal.BaseJournalService;
import com.smate.web.v8pub.service.journal.JournalService;
import com.smate.web.v8pub.service.pdwh.PdwhPubFullTextService;
import com.smate.web.v8pub.service.pdwh.PdwhPubMemberService;
import com.smate.web.v8pub.service.pdwh.indexurl.PdwhPubIndexUrlService;
import com.smate.web.v8pub.service.sns.PubAccessoryService;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubMemberService;
import com.smate.web.v8pub.service.sns.indexurl.PubIndexUrlService;
import com.smate.web.v8pub.untils.PubDetailVoUtil;

/**
 * @author aijiangbin
 * @date 2018/9/22
 */
public abstract class AbstractPubDetailQueryService implements PubDetailQueryService {

  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private PubAccessoryService pubAccessoryService;

  @Autowired
  private FileDownloadUrlService fileDownloadUrlService;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Value("${domainscm}")
  public String domainscm;
  @Autowired
  private PubScienceAreaDAO pubScienceAreaDAO;
  @Autowired
  private PubIndustryDAO pubIndustryDAO;
  @Autowired
  private PdwhPubFullTextService pdwhPubFullTextService;
  @Autowired
  private PdwhPubIndexUrlService pdwhPubIndexUrlService;
  @Autowired
  private JournalService journalService;
  @Autowired
  private BaseJournalService baseJournalService;
  @Autowired
  private PubMemberService pubMemberService;
  @Autowired
  private PdwhPubMemberService pdwhPubMemberService;
  @Autowired
  private PubIndexUrlService pubIndexUrlService;

  /**
   * 构建期刊的影响因子
   * 
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public void buildSnsPubImpactFactors(PubDetailVO pubDetailVO, PubDetailDOM detailDOM, Integer publishYear) {

    Integer pubType = detailDOM.getPubType();
    if (pubType != null && pubType == 4) {
      JournalInfoBean journalInfoBean = (JournalInfoBean) detailDOM.getTypeInfo();
      if (journalInfoBean == null || NumberUtils.isNullOrZero(journalInfoBean.getJid())) {
        return;
      }
      JournalPO journalPO = journalService.getById(journalInfoBean.getJid());
      String impactFactors = "";
      if (journalPO != null && journalPO.getMatchBaseJnlId() != null) {
        impactFactors = baseJournalService.findPdwhPubImpactFactors(journalPO.getMatchBaseJnlId(), publishYear);
      }
      pubDetailVO.setImpactFactors(impactFactors);
    }
  }

  /**
   * 构建期刊的影响因子
   * 
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public void buildPdwhPubImpactFactors(PubDetailVO pubDetailVO, PubDetailDOM detailDOM, Integer publishYear) {

    Integer pubType = detailDOM.getPubType();
    if (pubType != null && pubType == 4) {
      JournalInfoBean journalInfoBean = (JournalInfoBean) detailDOM.getTypeInfo();
      if (NumberUtils.isNullOrZero(journalInfoBean.getJid())) {
        return;
      }
      pubDetailVO.setJid(journalInfoBean.getJid());
      String impactFactors = baseJournalService.findPdwhPubImpactFactors(journalInfoBean.getJid(), publishYear);
      pubDetailVO.setImpactFactors(impactFactors);
    }
  }

  /**
   * 构建附件
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void buildPubAccessory(PubDetailVO pubDetailVO, PubSnsDetailDOM detailDOM) {
    List<PubAccessoryPO> list = pubAccessoryService.findByPubId(detailDOM.getPubId());
    if (list != null && list.size() > 0) {
      for (PubAccessoryPO pubAccessoryPO : list) {
        Accessory accessory = new Accessory();
        pubDetailVO.getAccessorys().add(accessory);
        String des3fileId = Des3Utils.encodeToDes3(Objects.toString(pubAccessoryPO.getFileId()));
        accessory.setDes3fileId(des3fileId);
        accessory.setFileName(pubAccessoryPO.getFileName());
        accessory.setPermission(pubAccessoryPO.getPermission());
        String downloadUrl = fileDownloadUrlService.getDownloadUrl(FileTypeEnum.FULLTEXT_ATTACHMENT,
            pubAccessoryPO.getFileId(), pubAccessoryPO.getPubId());
        accessory.setFileUrl(downloadUrl);
        String simpleFileUrl =
            fileDownloadUrlService.getShortDownloadUrl(FileTypeEnum.ARCHIVE_FILE, pubAccessoryPO.getFileId());
        accessory.setSimpleFileUrl(simpleFileUrl);
      }
    }
  }

  /**
   * 构建全文
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public void buildPubFulltext(PubDetailVO pubDetailVO, PubSnsDetailDOM detailDOM) {
    // 全文逻辑id 主键唯一的
    PubFulltextDTO fullText = new PubFulltextDTO();
    PubFullTextPO fullTextPO = pubFullTextService.get(detailDOM.getPubId());
    if (fullTextPO != null) {
      String des3fileId = Des3Utils.encodeToDes3(Objects.toString(fullTextPO.getFileId()));
      fullText.setDes3fileId(des3fileId);
      fullText.setFileName(fullTextPO.getFileName());
      fullText.setPermission(fullTextPO.getPermission());
      if (StringUtils.isNotBlank(fullTextPO.getThumbnailPath())) {
        fullText.setThumbnailPath(this.domainscm + fullTextPO.getThumbnailPath());
      }
      pubDetailVO.setFullText(fullText);
      String downloadUrl = fileDownloadUrlService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT, fullTextPO.getFileId(),
          fullTextPO.getPubId());
      pubDetailVO.setFullTextDownloadUrl(downloadUrl);
      String simpleDownLoadUrl =
          fileDownloadUrlService.getShortDownloadUrl(FileTypeEnum.ARCHIVE_FILE, fullTextPO.getFileId());
      pubDetailVO.setSimpleDownLoadUrl(simpleDownLoadUrl);
    }
  }

  /**
   * 构建科技领域
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void buildScienciArea(PubDetailVO pubDetailVO, PubSnsDetailDOM detailDOM) {
    // 需要从科技领域数据库中获取，PubScienceAreaPO
    List<PubScienceAreaPO> pubScienceAreaList = pubScienceAreaDAO.getPubAreaList(pubDetailVO.getPubId());
    if (pubScienceAreaList != null && pubScienceAreaList.size() > 0) {
      ScienceAreaDTO pubScienceArea = null;
      List<ScienceAreaDTO> scienceAreas = pubDetailVO.getScienceAreas();
      for (PubScienceAreaPO area : pubScienceAreaList) {
        pubScienceArea = new ScienceAreaDTO();
        pubScienceArea.setScienceAreaId(area.getScienceAreaId());
        scienceAreas.add(pubScienceArea);
      }
    }
  }

  /**
   * 构建行业
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void buildIndustry(PubDetailVO pubDetailVO, PubSnsDetailDOM detailDOM) {
    // 需要从数据库中获取，PubIndustryPO
    List<PubIndustryPO> pubIndustryList = pubIndustryDAO.getPubIndustryList(pubDetailVO.getPubId());
    if (pubIndustryList != null && pubIndustryList.size() > 0) {
      IndustryDTO pubIndustry = null;
      List<IndustryDTO> industrys = pubDetailVO.getIndustrys();
      for (PubIndustryPO industry : pubIndustryList) {
        pubIndustry = new IndustryDTO();
        pubIndustry.setIndustryCode(industry.getIndustryCode());
        industrys.add(pubIndustry);
      }
    }
  }

  /**
   * 构建成果成员信息
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void buildPubMembersInfo(PubDetailVO pubDetailVO, PubSnsDetailDOM detailDOM) {
    List<PubMemberPO> members = pubMemberService.findByPubId(detailDOM.getPubId());
    if (members != null && members.size() > 0) {
      String oriEmail = "";
      for (PubMemberPO pubMemberPO : members) {
        PubMemberDTO pubMember = new PubMemberDTO();
        pubMember.setSeqNo(pubMemberPO.getSeqNo());
        pubMember.setPsnId(pubMemberPO.getPsnId());
        pubMember.setName(pubMemberPO.getName());
        pubMember.setInsNames(buildInsNames(pubMemberPO));
        pubMember.setEmail(pubMemberPO.getEmail());
        pubMember.setOwner(pubMemberPO.getOwner() == 1);
        pubMember.setCommunicable(pubMemberPO.getCommunicable() == 1);
        pubMember.setFirstAuthor(pubMemberPO.getFirstAuthor() == 1);
        pubDetailVO.getMembers().add(pubMember);
        if (StringUtils.isNotBlank(pubMemberPO.getEmail())) {
          oriEmail = oriEmail + ";" + pubMemberPO.getEmail();
        }
      }
      if (StringUtils.isNotBlank(oriEmail)) {
        pubDetailVO.setOriginalMail(oriEmail.substring(1, oriEmail.length()));
      }
    }
  }

  /**
   * 构建成果信息
   *
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public PubDetailVO buildPubTypeInfo(PubSnsDetailDOM detailDOM) {
    return PubDetailVoUtil.buildPubTypeInfo(detailDOM);
  }

  /**
   * 获取国家城市 国家 == 一级 城市 == 省份+市
   * 
   * @param pubDetailVO
   */
  @SuppressWarnings("rawtypes")
  public void buildCountryRegionId(PubDetailVO pubDetailVO) {
    if (pubDetailVO.getCountryId() == null) {
      return;
    }
    ConstRegion constRegion = constRegionDao.findRegionNameById(pubDetailVO.getCountryId());
    String firstRegionName = "";
    String secondRegionName = "";
    String thirdRegionName = "";
    if (constRegion != null) {
      firstRegionName = constRegion.getZhName();
      if (constRegion.getSuperRegionId() != null) {
        constRegion = constRegionDao.findRegionNameById(constRegion.getSuperRegionId());
        secondRegionName = constRegion.getZhName();
        if (constRegion.getSuperRegionId() != null) {
          constRegion = constRegionDao.findRegionNameById(constRegion.getSuperRegionId());
          thirdRegionName = constRegion.getZhName();
        }
      }
    }
    if (StringUtils.isNotBlank(firstRegionName) && StringUtils.isNotBlank(secondRegionName)
        && StringUtils.isNotBlank(thirdRegionName)) {
      pubDetailVO.setCityName(secondRegionName + firstRegionName);
      pubDetailVO.setCountryName(thirdRegionName);
      pubDetailVO.setCountriesRegions(thirdRegionName + "," + secondRegionName + "," + firstRegionName);
      return;
    }
    if (StringUtils.isNotBlank(firstRegionName) && StringUtils.isNotBlank(secondRegionName)) {
      pubDetailVO.setCountryName(secondRegionName);
      pubDetailVO.setCityName(firstRegionName);
      pubDetailVO.setCountriesRegions(secondRegionName + "," + firstRegionName);
      return;
    }
    if (StringUtils.isNotBlank(firstRegionName)) {
      pubDetailVO.setCountryName(firstRegionName);
      pubDetailVO.setCountriesRegions(firstRegionName);
      return;
    }
  }

  /**
   * 地区国际化显示
   *
   * @param reg
   * @return
   */
  public String buildRegionNameByLanguage(ConstRegion reg) {
    if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
      return StringUtils.isNotBlank(reg.getEnName()) ? reg.getEnName() : reg.getZhName();
    } else {
      return StringUtils.isNotBlank(reg.getZhName()) ? reg.getZhName() : reg.getEnName();
    }
  }

  public String getShortDownloadUrl(FileTypeEnum fileType, Long id) {
    return fileDownloadUrlService.getShortDownloadUrl(fileType, id);
  }

  /**
   *
   *
   *
   *
   *
   *
   *
   * 下面是 基准库的构建信息
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public PubDetailVO buildPubTypeInfo(PubPdwhDetailDOM detailDOM) {
    int pubType = detailDOM.getPubType();
    PubDetailVO pubDetailVO = null;
    PubTypeInfoBean pubTypeInfoBean = detailDOM.getTypeInfo();
    pubDetailVO = new PubDetailVO();
    switch (pubType) {
      case 1:
        AwardsInfoBean awardsInfoBean = (AwardsInfoBean) pubTypeInfoBean;
        if (awardsInfoBean != null) {
          AwardsInfoDTO awardsInfo = new AwardsInfoDTO();
          awardsInfo.setCategory(awardsInfoBean.getCategory());
          awardsInfo.setIssuingAuthority(awardsInfoBean.getIssuingAuthority());
          awardsInfo.setIssueInsId(awardsInfoBean.getIssueInsId());
          awardsInfo.setCertificateNo(awardsInfoBean.getCertificateNo());
          awardsInfo.setAwardDate(awardsInfoBean.getAwardDate());
          awardsInfo.setGrade(awardsInfoBean.getGrade());
          pubDetailVO.setPubTypeInfo(awardsInfo);
        }
        break;
      case 2:
      case 10:
        BookInfoBean bookInfoBean = (BookInfoBean) pubTypeInfoBean;
        if (bookInfoBean != null) {
          BookInfoDTO bookInfo = new BookInfoDTO();
          bookInfo.setName(bookInfoBean.getName());
          bookInfo.setSeriesName(bookInfoBean.getSeriesName());
          bookInfo.setEditors(bookInfoBean.getEditors());
          bookInfo.setISBN(bookInfoBean.getISBN());
          bookInfo.setPublisher(bookInfoBean.getPublisher());
          bookInfo.setTotalWords(bookInfoBean.getTotalWords());
          bookInfo.setType(bookInfoBean.getType());
          bookInfo.setTotalPages(bookInfoBean.getTotalPages());
          bookInfo.setChapterNo(bookInfoBean.getChapterNo());
          bookInfo.setPageNumber(bookInfoBean.getPageNumber());
          bookInfo.setLanguage(bookInfoBean.getLanguage());
          pubDetailVO.setPubTypeInfo(bookInfo);
        }

        break;
      case 3:
        ConferencePaperBean conferencePaperBean = (ConferencePaperBean) pubTypeInfoBean;
        if (conferencePaperBean != null) {
          ConferencePaperDTO conferencePaper = new ConferencePaperDTO();
          conferencePaper.setPaperType(conferencePaperBean.getPaperType());
          conferencePaper.setName(conferencePaperBean.getName());
          conferencePaper.setOrganizer(conferencePaperBean.getOrganizer());
          conferencePaper.setStartDate(conferencePaperBean.getStartDate());
          conferencePaper.setEndDate(conferencePaperBean.getEndDate());
          conferencePaper.setPageNumber(conferencePaperBean.getPageNumber());
          conferencePaper.setPapers(conferencePaperBean.getPapers());
          pubDetailVO.setPubTypeInfo(conferencePaper);
        }

        break;
      case 4:
        JournalInfoBean journalInfoBean = (JournalInfoBean) pubTypeInfoBean;
        if (journalInfoBean != null) {
          JournalInfoDTO journalInfo = new JournalInfoDTO();
          journalInfo.setJid(journalInfoBean.getJid());
          journalInfo.setName(journalInfoBean.getName());
          journalInfo.setVolumeNo(journalInfoBean.getVolumeNo());
          journalInfo.setIssue(journalInfoBean.getIssue());
          journalInfo.setPageNumber(journalInfoBean.getPageNumber());
          journalInfo.setPublishStatus(journalInfoBean.getPublishStatus());
          journalInfo.setISSN(journalInfoBean.getISSN());
          pubDetailVO.setPubTypeInfo(journalInfo);
        }

        break;
      case 5:
        PatentInfoBean patentInfoBean = (PatentInfoBean) pubTypeInfoBean;
        if (patentInfoBean != null) {
          PatentInfoDTO journalInfo = new PatentInfoDTO();
          journalInfo.setType(patentInfoBean.getType());
          journalInfo.setArea(patentInfoBean.getArea());
          journalInfo.setStatus(patentInfoBean.getStatus());
          journalInfo.setApplier(patentInfoBean.getApplier());
          journalInfo.setApplicationDate(patentInfoBean.getApplicationDate());
          journalInfo.setStartDate(patentInfoBean.getStartDate());
          journalInfo.setEndDate(patentInfoBean.getEndDate());
          journalInfo.setApplicationNo(patentInfoBean.getApplicationNo());
          journalInfo.setPublicationOpenNo(patentInfoBean.getPublicationOpenNo());
          journalInfo.setIPC(patentInfoBean.getIPC());
          journalInfo.setCPC(patentInfoBean.getCPC());
          PubPatentTransitionStatusEnum transitionStatusEnum = patentInfoBean.getTransitionStatus();
          journalInfo.setTransitionStatus(transitionStatusEnum);
          journalInfo.setPrice(patentInfoBean.getPrice());
          journalInfo.setIssuingAuthority(patentInfoBean.getIssuingAuthority());

          pubDetailVO.setPubTypeInfo(journalInfo);
        }

        break;
      case 7:
        break;
      case 8:
        ThesisInfoBean thesisInfoBean = (ThesisInfoBean) pubTypeInfoBean;
        if (thesisInfoBean != null) {
          ThesisInfoDTO thesisInfo = new ThesisInfoDTO();
          PubThesisDegreeEnum degreeEnum = thesisInfoBean.getDegree();
          thesisInfo.setDegree(degreeEnum);
          thesisInfo.setDefenseDate(thesisInfoBean.getDefenseDate());
          thesisInfo.setIssuingAuthority(thesisInfoBean.getIssuingAuthority());
          thesisInfo.setDepartment(thesisInfoBean.getDepartment());

          pubDetailVO.setPubTypeInfo(thesisInfo);
        }
        break;
      case 12:
        // 标准
        StandardInfoBean standardInfoBean = (StandardInfoBean) pubTypeInfoBean;
        if (standardInfoBean != null) {
          StandardInfoDTO standardInfo = new StandardInfoDTO();
          standardInfo.setPublishAgency(standardInfoBean.getPublishAgency());
          standardInfo.setStandardNo(standardInfoBean.getStandardNo());
          standardInfo.setTechnicalCommittees(standardInfoBean.getTechnicalCommittees());
          standardInfo.setType(standardInfoBean.getType());
          standardInfo.setIcsNo(standardInfoBean.getIcsNo());
          standardInfo.setDomainNo(standardInfoBean.getDomainNo());
          standardInfo.setImplementDate(standardInfoBean.getImplementDate());
          standardInfo.setObsoleteDate(standardInfoBean.getObsoleteDate());
          pubDetailVO.setPubTypeInfo(standardInfo);
        }
        break;
      case 13:
        // 软件著作权
        SoftwareCopyrightBean scBean = (SoftwareCopyrightBean) pubTypeInfoBean;
        if (scBean != null) {
          SoftwareCopyrightDTO scDto = new SoftwareCopyrightDTO();
          scDto.setRegisterNo(scBean.getRegisterNo());
          scDto.setAcquisitionType(scBean.getAcquisitionType());
          scDto.setScopeType(scBean.getScopeType());
          scDto.setCategoryNo(scBean.getCategoryNo());
          scDto.setFirstPublishDate(scBean.getFirstPublishDate());
          scDto.setPublicityDate(scBean.getPublicityDate());
          scDto.setRegisterDate(scBean.getRegisterDate());
          pubDetailVO.setPubTypeInfo(scDto);
        }
        break;
      default:
        break;
    }
    return pubDetailVO;
  }

  /**
   * 构建基准库全文
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public void buildPubFulltext(PubDetailVO pubDetailVO, PubPdwhDetailDOM detailDOM) {
    Long pdwhPubId = pubDetailVO.getPubId();
    PdwhPubFullTextPO fullTextPO = pdwhPubFullTextService.getPdwhPubfulltext(pdwhPubId);
    // 全文逻辑id 主键唯一的
    // Long fulltextId = detailDOM.getFulltextId();
    if (fullTextPO != null) {
      PubFulltextDTO fulltext = new PubFulltextDTO();
      fulltext.setFileId(fullTextPO.getFileId());
      fulltext.setFileName(fullTextPO.getFileName());
      // SCM-21167：解决群组成果认领时，出现无法将成果全文导入到个人成果库的问题
      fulltext.setDes3fileId(Des3Utils.encodeToDes3(fullTextPO.getFileId().toString()));
      pubDetailVO.setFullText(fulltext);
      if (StringUtils.isNotBlank(fullTextPO.getThumbnailPath())) {
        pubDetailVO.setFulltextImageUrl(this.domainscm + fullTextPO.getThumbnailPath());
        fulltext.setThumbnailPath(fullTextPO.getThumbnailPath());
      }
      pubDetailVO.setFullTextDownloadUrl(fileDownloadUrlService.getDownloadUrl(FileTypeEnum.PDWH_FULLTEXT, pdwhPubId));
      String simpleDownLoadUrl =
          fileDownloadUrlService.getShortDownloadUrl(FileTypeEnum.ARCHIVE_FILE, fullTextPO.getFileId());
      pubDetailVO.setSimpleDownLoadUrl(simpleDownLoadUrl);
    }
  }

  /**
   * 构建成果成员信息 因为MongoDB数据不完整，少了owne字段 所以才查询表的数据
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void buildPubMembersInfo(PubDetailVO pubDetailVO, PubPdwhDetailDOM detailDOM) {
    List<PdwhPubMemberPO> members = pdwhPubMemberService.findByPubId(detailDOM.getPubId());
    List<PubMemberBean> memberBeanList = detailDOM.getMembers();
    if (members != null && members.size() > 0) {
      for (PdwhPubMemberPO pdwhPubMemberPO : members) {
        PubMemberDTO pubMember = new PubMemberDTO();
        pubDetailVO.getMembers().add(pubMember);
        pubMember.setSeqNo(pdwhPubMemberPO.getSeqNo());
        pubMember.setPsnId(pdwhPubMemberPO.getPsnId());
        pubMember.setName(pdwhPubMemberPO.getName());
        if (memberBeanList != null && memberBeanList.size() > 0) {
          for (PubMemberBean bean : memberBeanList) {
            if (bean != null && bean.getSeqNo().equals(pdwhPubMemberPO.getSeqNo())) {
              pubMember.setInsNames(buildInsNames(bean));
            }
          }
        }
        pubMember.setEmail(pdwhPubMemberPO.getEmail());
        pubMember.setOwner(pdwhPubMemberPO.getOwner() == 1);
        pubMember.setCommunicable(pdwhPubMemberPO.getCommunicable() == 1);
        pubMember.setFirstAuthor(pdwhPubMemberPO.getFirstAuthor() == 1);
      }
    }
  }

  private List<MemberInsDTO> buildInsNames(PubMemberPO pubMemberPO) {
    List<MemberInsDTO> insNames = new ArrayList<MemberInsDTO>();
    MemberInsDTO memberIns = new MemberInsDTO();
    memberIns.setInsId(pubMemberPO.getInsId());
    memberIns.setInsName(pubMemberPO.getInsName());
    insNames.add(memberIns);
    return insNames;
  }

  private List<MemberInsDTO> buildInsNames(PubMemberBean memberBean) {
    List<MemberInsBean> memberInsBeans = memberBean.getInsNames();
    List<MemberInsDTO> insNames = new ArrayList<MemberInsDTO>();
    if (memberInsBeans != null && memberInsBeans.size() > 0) {
      MemberInsDTO memberIns = new MemberInsDTO();
      memberIns.setInsId(memberInsBeans.get(0).getInsId());
      memberIns.setInsName(memberInsBeans.get(0).getInsName());
      insNames.add(memberIns);
    }
    return insNames;
  }

  /**
   * 构建成果收录
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void buildPubSituation(PubDetailVO pubDetailVO, PubDetailDOM detailDOM) {
    Set<PubSituationBean> situations = detailDOM.getSituations();
    List<PubSituationDTO> list = new ArrayList<PubSituationDTO>();
    pubDetailVO.setSituations(list);
    PubSituationDTO sitation = null;
    if (situations != null && situations.size() > 0) {
      for (PubSituationBean pubSituationBean : situations) {
        sitation = new PubSituationDTO();
        String libraryName = PubLibraryEnum.parse(pubSituationBean.getLibraryName()).desc;
        sitation.setLibraryName(libraryName);
        sitation.setSitStatus(pubSituationBean.isSitStatus());
        sitation.setSrcId(pubSituationBean.getSrcId());
        sitation.setSitOriginStatus(pubSituationBean.isSitOriginStatus());
        sitation.setSrcUrl(pubSituationBean.getSrcUrl());
        sitation.setSrcDbId(pubSituationBean.getSrcDbId());
        list.add(sitation);
      }
    }

  }

  /**
   * 构建成果短地址
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public void buildPdwhPubIndex(PubDetailVO pubDetailVO, PubPdwhDetailDOM detailDOM) {

    String pubIndex = pdwhPubIndexUrlService.getIndexUrlByPubId(detailDOM.getPubId());
    if (StringUtils.isNotBlank(pubIndex)) {
      String pubShortIndex = this.domainscm + "/" + ShortUrlConst.S_TYPE + "/" + pubIndex;
      pubDetailVO.setPubIndexUrl(pubShortIndex);
    }
  }

  public void buildSnsPubIndexUrl(PubDetailVO pubDetailVO, PubSnsDetailDOM detailDOM) {
    PubIndexUrl pubIndexUrl = pubIndexUrlService.get(detailDOM.getPubId());
    if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
      pubDetailVO.setPubIndexUrl(domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
    }

  }

  public static void main(String[] args) {
    String des3fileId = Des3Utils.encodeToDes3("1000000267506");
    System.out.println(des3fileId);
  }

}
