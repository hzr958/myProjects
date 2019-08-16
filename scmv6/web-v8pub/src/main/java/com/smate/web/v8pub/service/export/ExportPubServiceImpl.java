package com.smate.web.v8pub.service.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.pub.enums.PubBookTypeEnum;
import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.pub.enums.PubPatentAreaEnum;
import com.smate.core.base.pub.enums.PubPatentTransitionStatusEnum;
import com.smate.core.base.pub.enums.PubSCAcquisitionTypeEnum;
import com.smate.core.base.pub.enums.PubSCScopeTypeEnum;
import com.smate.core.base.pub.enums.PubStandardTypeEnum;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.v8pub.restTemp.service.PubRestemplateService;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.OtherInfoDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.SoftwareCopyrightDTO;
import com.smate.web.v8pub.dto.StandardInfoDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.vo.exportfile.ExportPubTemp;

@Service("exportPubService")
@Transactional(rollbackFor = Exception.class)
public class ExportPubServiceImpl implements ExportPubService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubSnsDetailService pubSnsDetailService;
  @Autowired
  private PubRestemplateService pubRestemplateService;


  @Override
  public List<ExportPubTemp> queryExportPubTemp(List<Long> pubIds) throws Exception {
    List<ExportPubTemp> exportList = new ArrayList<ExportPubTemp>();
    try {
      // 保持导出顺序一致
      ExportPubTemp exportPubTemp = null;
      for (Long pubId : pubIds) {
        exportPubTemp = this.getExportPubTemp(pubId);
        exportList.add(exportPubTemp);
      }
      return exportList;
    } catch (Exception e) {
      logger.error("查询成果错误。psnIds={}", pubIds, e);
      throw new Exception(e);
    }
  }

  @SuppressWarnings("rawtypes")
  private ExportPubTemp getExportPubTemp(Long pubId) throws Exception {
    ExportPubTemp ep = new ExportPubTemp();
    PubDetailVO pubShow = null;
    String pubJson = pubRestemplateService.snsEditPubQuery(Des3Utils.encodeToDes3(pubId.toString()));// 调用接口查询成果
    pubShow = this.getPubDetailVoJson(pubJson);
    if (pubShow == null) {
      return ep;
    }
    Integer pubType = pubShow.getPubType();
    String locale = LocaleContextHolder.getLocale().toString();
    // 标题
    ep.setTitle(HtmlUtils.htmlUnescape(pubShow.getTitle()));
    ep.setAuthornames(HtmlUtils.htmlUnescape(getAuthorName(pubShow.getAuthorNames())));// 作者
    ep.setInsnames(HtmlUtils.htmlUnescape(pubShow.getInsNames()));// 单位名称
    ep.setPublishdate(pubShow.getPublishDate());// 发表日期
    ep.setPublishyear(Objects.toString(pubShow.getPublishYear(), ""));// 发表年份
    ep.setFundinfo(pubShow.getFundInfo());// 基金标注
    ep.setCitations(Objects.toString(pubShow.getCitations(), ""));// 引用数
    ep.setDoi(pubShow.getDoi());
    ep.setSummary(HtmlUtils.htmlUnescape(pubShow.getSummary()));// 摘要
    ep.setKeywords(HtmlUtils.htmlUnescape(pubShow.getKeywords()));// 关键词
    ep.setFulltexturl(HtmlUtils.htmlUnescape(pubShow.getSrcFulltextUrl()));// 全文链接
    ep.setCountry(pubShow.getCountryName());// 国家或地区
    ep.setPubtype(pubType);// 成果类型
    switch (pubType) {
      case 1: // 奖励
        AwardsInfoDTO awardsInfo = (AwardsInfoDTO) pubShow.getPubTypeInfo();
        ep.setIssueinsname(awardsInfo.getIssuingAuthority());// 授奖机构
        ep.setCertificateno(awardsInfo.getCertificateNo());// 证书编号
        ep.setAwarddate(awardsInfo.getAwardDate());// 授奖日期
        ep.setAwardgrade(awardsInfo.getGrade());// 授奖等级
        ep.setAwardcategory(awardsInfo.getCategory());// 授奖种类
        ep.setSheetIndex(7 + "");
        break;
      case 2:// 书籍
        BookInfoDTO book = (BookInfoDTO) pubShow.getPubTypeInfo();
        ep.setSeriesname(HtmlUtils.htmlUnescape(book.getSeriesName()));// 丛书名称
        PubBookTypeEnum type = book.getType();
        ep.setBooktype(type != null ? type.getZhDescription() : "");// 专著类别
        ep.setIsbn(book.getISBN());// ISBN
        ep.setEditors(HtmlUtils.htmlUnescape(book.getEditors()));// 编辑
        ep.setPublisher(book.getPublisher());// 出版社
        ep.setLanguage(book.getLanguage());// 语种
        ep.setTotalpages(Objects.toString(book.getTotalPages(), ""));// 总页数
        ep.setTotalwords(Objects.toString(book.getTotalWords(), ""));// 总字数
        ep.setSheetIndex(6 + "");
        break;
      case 3:// 会议论文
        ConferencePaperDTO conferencePaper = (ConferencePaperDTO) pubShow.getPubTypeInfo();
        PubConferencePaperTypeEnum paperType = conferencePaper.getPaperType();
        ep.setPapertype(paperType != null ? paperType.getZhDescription() : "");// 会议类别
        ep.setConferencename(HtmlUtils.htmlUnescape(conferencePaper.getName()));// 会议名称
        ep.setOrganizer(conferencePaper.getOrganizer());// 会议组织者
        ep.setPapersname(HtmlUtils.htmlUnescape(conferencePaper.getPapers()));// 论文集名
        ep.setStartdate(conferencePaper.getStartDate());// 开始日期
        ep.setEnddate(conferencePaper.getEndDate());// 结束日期
        ep.setPagenumber(conferencePaper.getPageNumber());// 页码
        setStartAndEndPage(ep, conferencePaper.getPageNumber());// 开始和结束页码
        setInclude(ep, pubShow);// 收录情况
        ep.setSheetIndex(2 + "");
        break;
      case 4:// 期刊论文
        JournalInfoDTO ournalInfo = (JournalInfoDTO) pubShow.getPubTypeInfo();
        ep.setJournalname(HtmlUtils.htmlUnescape(ournalInfo.getName()));// 期刊名称
        ep.setIssn(ournalInfo.getISSN());// issn
        ep.setPublishstatus(getJournalStatus(ournalInfo.getPublishStatus()));// 状态
        ep.setVolume(ournalInfo.getVolumeNo());// 卷号
        ep.setIssue(ournalInfo.getIssue());// 期号
        ep.setPagenumber(ournalInfo.getPageNumber());// 起止页码或文章号
        setInclude(ep, pubShow);// 收录情况
        ep.setSheetIndex(1 + "");
        break;
      case 5:// 专利
        PatentInfoDTO patentInfo = (PatentInfoDTO) pubShow.getPubTypeInfo();
        PubPatentAreaEnum area = patentInfo.getArea();
        ep.setPatentarea(area != null ? area.getDescription() : "");// 专利国家
        ep.setPatenttype(getPatentType(patentInfo.getType()));// 专利类型
        ep.setPatentstatus(getPatentStatus(Objects.toString(patentInfo.getStatus())));// 专利状态，0:申请/1:授权
        ep.setApplicationno(patentInfo.getApplicationNo());// 申请号
        ep.setPatentopenno(patentInfo.getPublicationOpenNo());// 专利公开（公告）号
        ep.setPatentapplier(patentInfo.getPatentee());// 专利权人 (专利为授权状态)
        ep.setIpc(patentInfo.getIPC());// IPC
        ep.setCpc(patentInfo.getCPC());// CPC
        ep.setApplicationdate(patentInfo.getApplicationDate());// 申请(公开)日期
        ep.setStartdate(patentInfo.getStartDate());// 开始日期
        ep.setEnddate(patentInfo.getEndDate());// 结束日期
        PubPatentTransitionStatusEnum transitionStatus = patentInfo.getTransitionStatus();
        ep.setPatentchangestatus(getPatenTransitionStatus(transitionStatus));// 专利转化状态
        ep.setPatentprice(patentInfo.getPrice());// 交易金额
        ep.setSheetIndex(4 + "");
        break;
      case 7:// 其他
        ep.setSheetIndex(10 + "");
        break;
      case 8:// 学位论文
        ThesisInfoDTO thesisInfo = (ThesisInfoDTO) pubShow.getPubTypeInfo();
        PubThesisDegreeEnum degree = thesisInfo.getDegree();
        ep.setDegree(degree != null ? degree.getZhDescription() : "");// 学位
        ep.setDefensedate(thesisInfo.getDefenseDate());// 答辩日期
        ep.setThesisinsname(thesisInfo.getIssuingAuthority());// 颁发单位
        ep.setDepartment(thesisInfo.getDepartment());// 部门
        ep.setSheetIndex(3 + "");
        break;
      case 10:// 书籍章节
        BookInfoDTO bookInfo = (BookInfoDTO) pubShow.getPubTypeInfo();
        ep.setBookname(HtmlUtils.htmlUnescape(bookInfo.getName()));// 书名
        ep.setIsbn(bookInfo.getISBN());// ISBN
        ep.setPagenumber(bookInfo.getPageNumber());// 页码
        setStartAndEndPage(ep, bookInfo.getPageNumber());// 开始和结束页码
        ep.setChapterno(bookInfo.getChapterNo());// 章节号码
        ep.setEditors(HtmlUtils.htmlUnescape(bookInfo.getEditors()));// 编辑
        ep.setPublisher(bookInfo.getPublisher());// 出版社
        ep.setSheetIndex(5 + "");
        break;
      case 12:// 标准
        StandardInfoDTO standardInfo = (StandardInfoDTO) pubShow.getPubTypeInfo();
        ep.setStandardno(standardInfo.getStandardNo());// 标准号
        PubStandardTypeEnum pubStandardType = standardInfo.getType();
        ep.setStandardtype(pubStandardType != null ? pubStandardType.getDescription() : "");// 标准类型
        String sdtitution = StringUtils.isNoneBlank(standardInfo.getPublishAgency()) ? standardInfo.getPublishAgency()
            : standardInfo.getTechnicalCommittees();
        ep.setStandardintitution(sdtitution);// 公布机构（归口单位）
        ep.setSheetIndex(8 + "");
        break;
      case 13:// 软件著作权
        SoftwareCopyrightDTO softwareCopyright = (SoftwareCopyrightDTO) pubShow.getPubTypeInfo();
        ep.setRegisterno(softwareCopyright.getRegisterNo());// 登记号
        PubSCAcquisitionTypeEnum pubSCAcquisitionType = softwareCopyright.getAcquisitionType();
        ep.setAcquisitiontype(pubSCAcquisitionType != null ? pubSCAcquisitionType.getDescription() : "");// 权利获得方式
        PubSCScopeTypeEnum pubSCScopeType = softwareCopyright.getScopeType();
        ep.setScopetype(pubSCScopeType != null ? pubSCScopeType.getDescription() : "");// 权利范围
        ep.setSheetIndex(9 + "");
        break;
    }
    return ep;
  }

  /**
   * 作者名称去掉*号和<strong>标签
   * 
   * @param name
   * @return
   */
  private String getAuthorName(String name) {
    name = Objects.toString(name, "");
    return name.replaceAll("\\*|(?i)</?strong>", "");
  }

  /**
   * 专利转化状态
   * 
   * @param t
   * @return
   */
  private String getPatenTransitionStatus(PubPatentTransitionStatusEnum t) {
    String status = t != null ? t.getDescription() : "";
    if ("无".equals(status)) {
      status = "未转让";
    }
    return status;
  }

  /**
   * 获取专利类别 51-发明专利 52-实用专利 53-外观专利 54-植物专利
   * 
   * @return
   */
  private String getPatentType(String type) {
    String typeStr = "";
    switch (type) {
      case "51":
        typeStr = "51-发明专利";
        break;
      case "52":
        typeStr = "52-实用专利";
        break;
      case "53":
        typeStr = "53-外观专利";
        break;
      case "54":
        typeStr = "54-植物专利";
        break;
      default:
        break;
    }
    return typeStr;
  }

  /**
   * 处理专利状态，0:申请/1:授权
   * 
   * @return
   */
  private String getPatentStatus(String status) {
    if ("0".equals(status)) {
      return "0-申请";
    } else if ("1".equals(status)) {
      return "1-授权";
    }
    return "";
  }

  /**
   * 处理期刊的状态 P-已发表 A-已接收未发表
   * 
   * @param status
   */
  private String getJournalStatus(String status) {
    String journalStatus = "";
    if ("P".equals(status)) {
      journalStatus = "P-已发表";
    } else if ("A".equals(status)) {
      journalStatus = "A-已接收未发表";
    }
    return journalStatus;

  }

  /**
   * 处理开始和结束页码
   * 
   * @param ep
   * @param pageNumber
   */
  private void setStartAndEndPage(ExportPubTemp ep, String pageNumber) {
    String startPage = "";
    String endPage = "";
    if (StringUtils.isNotBlank(pageNumber)) {
      startPage = pageNumber.replaceAll("-[0-9]*$", "");
      endPage = pageNumber.replaceAll("^[0-9]*-?", "");
    }
    ep.setStartpage(startPage);
    ep.setEndpage(endPage);
  }

  /**
   * 获取收录情况
   * 
   * @param ep
   * @param pubShow
   */
  private void setInclude(ExportPubTemp ep, PubDetailVO pubShow) {
    List<PubSituationDTO> situations = pubShow.getSituations();
    for (PubSituationDTO sit : situations) {
      switch (sit.getLibraryName()) {
        case "SCIE":
          if (sit.isSitStatus()) {
            ep.setListscie("是");
          }
          break;
        case "SSCI":
          if (sit.isSitStatus()) {
            ep.setListssci("是");
          }
          break;
        case "ISTP":
          if (sit.isSitStatus()) {
            ep.setLististp("是");
          }
          break;
        case "EI":
          if (sit.isSitStatus()) {
            ep.setListei("是");
          }
          break;
        default:
          break;
      }
    }
  }

  @SuppressWarnings("rawtypes")
  public PubDetailVO getPubDetailVoJson(String pubJson) {
    PubDetailVO pub = new PubDetailVO();
    Map<String, Object> map = null;
    try {
      map = JacksonUtils.json2Map(pubJson);
    } catch (IOException e) {
      logger.error("进入编辑页面转化json出错 ,pubJson={}", pubJson, e);
      throw new ServiceException();
    }

    Integer pubType = Integer.valueOf(Objects.toString(map.get("pubType")));
    switch (pubType) {
      case 1: // 奖励
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<AwardsInfoDTO>>() {});
        break;
      case 2:// 书籍
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<BookInfoDTO>>() {});
        break;
      case 3:// 会议论文
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<ConferencePaperDTO>>() {});
        break;
      case 4:// 期刊论文
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<JournalInfoDTO>>() {});
        break;
      case 5:// 专利
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<PatentInfoDTO>>() {});
        break;
      case 7:// 其他
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<OtherInfoDTO>>() {});
        break;
      case 8:// 学位论文
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<ThesisInfoDTO>>() {});
        break;
      case 10:// 书籍章节
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<BookInfoDTO>>() {});
        break;
      case 12:// 标准
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<StandardInfoDTO>>() {});
        break;
      case 13:// 软件著作权
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<SoftwareCopyrightDTO>>() {});
        break;
    }
    return pub;
  }
}
