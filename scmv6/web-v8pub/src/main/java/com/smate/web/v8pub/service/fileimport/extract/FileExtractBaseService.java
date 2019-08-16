package com.smate.web.v8pub.service.fileimport.extract;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.pub.enums.PubLibraryEnum;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.pub.enums.PubStandardTypeEnum;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.xss.XssUtils;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.dto.SoftwareCopyrightDTO;
import com.smate.web.v8pub.dto.StandardInfoDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;
import com.smate.web.v8pub.vo.PendingImportPubVO;

/**
 * 文件解析抽象类
 * 
 * @author aijiangbin
 * @date 2018年7月30日
 */
@Transactional(rollbackFor = Exception.class)
public abstract class FileExtractBaseService implements FileExtractService {

  public static final int BUFFER_SIZE = 15 * 1024;
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ConstRegionDao constRegionDao;

  @Override
  public PubSaveDataForm extract(MultipartFile sourceFile) throws Exception {
    // A: 解析文件--》map对象
    List<Map<String, String>> list = fileExtractToMap(sourceFile);
    // A1:对文件内容，做xss过滤
    filterXss(list);
    // B: map对象 转换成 --》 pojo对象
    List<PubFileInfo> pubFileInfoList = processData(list);
    // C: pojo对象---转换成果统一的成果保存格式
    List<PendingImportPubVO> saveDataList = buildPubSaveData(pubFileInfoList);
    PubSaveDataForm form = new PubSaveDataForm();
    form.setList(saveDataList);
    return form;
  }

  private void filterXss(List<Map<String, String>> list) {
    if (list != null && list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {
        Map<String, String> map = list.get(i);
        Set<String> keySet = map.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
          final String key = iterator.next();
          if (StringUtils.isNotBlank(map.get(key))) {
            map.put(key, XssUtils.filterByXssStr(map.get(key)));
          }
        }
      }
    }
  }

  /**
   * 将传入的英文月份字符串转成对应的月份. 参考：ExtractFileDriverUtil
   * 
   * @param engMonth
   * @return
   */
  public static String engMonth2Int(String engMonth) {
    String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    for (Integer i = 1; i <= months.length; i++) {
      if (months[i - 1].equals(engMonth.substring(0, 3).toUpperCase()))
        return i.toString();
    }
    return "";
  }

  /**
   * 解析字符串为Int 解析异常返回为空
   * 
   * @param str
   * @return
   */
  public static Integer parserStringToInt(String str) {
    if (StringUtils.isBlank(str)) {
      return null;
    }
    if (NumberUtils.isNumber(str)) {
      try {
        int num = Integer.parseInt(str);
        return num;
      } catch (Exception e) {
      }
    }
    return null;
  }

  /**
   * 截取最大的字符串长度
   * 
   * @param str
   * @param length
   * @return
   */
  public static String substring(String str, int length) {
    if (StringUtils.isBlank(str)) {
      return "";
    }
    if (str.length() > length) {
      str = str.substring(0, length);
    }
    return str;
  }

  /**
   * 文件解析成果map对象数据
   * 
   * @param sourceFile
   * @return
   */
  public abstract List<Map<String, String>> fileExtractToMap(MultipartFile sourceFile);

  /**
   * 加工转换的map数据
   * 
   * @param list
   * @return
   */
  public abstract List<PubFileInfo> processData(List<Map<String, String>> list);

  /**
   * 统一构建 成果保存需要的格式数据
   * 
   * @param list
   * @return
   * @throws Exception
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<PendingImportPubVO> buildPubSaveData(List<PubFileInfo> list) throws Exception {
    List<PendingImportPubVO> pendingImportPubList = new ArrayList<PendingImportPubVO>();
    if (list != null && list.size() > 0) {
      for (PubFileInfo pubInfo : list) {
        PendingImportPubVO pendingPubVo = new PendingImportPubVO();
        // dataList.add(pubSaveData);
        pendingPubVo.setSeqNo(pubInfo.getSeqNo());
        pendingPubVo.setTitle(pubInfo.getTitle());
        pendingPubVo.setPublishDate(pubInfo.getPubyear());//
        // TODO country?
        pendingPubVo.setFundInfo(pubInfo.getFundInfo());//
        pendingPubVo.setCitations(pubInfo.getCitations());//
        pendingPubVo.setDoi(pubInfo.getDoi());
        pendingPubVo.setSummary(pubInfo.getCabstract());
        pendingPubVo.setKeywords(pubInfo.getKeywords());
        pendingPubVo.setSrcFulltextUrl(pubInfo.getSrcFulltextUrl());
        pendingPubVo.setPubType(pubInfo.getPubType());
        pendingPubVo.setRecordFrom(PubSnsRecordFromEnum.IMPORT_FORM_FILE);// 默认值
                                                                          // 2,
                                                                          // "文件导入"
        pendingPubVo.setRemarks(pubInfo.getRemarks());
        pendingPubVo.setOrganization(pubInfo.getOrganization());
        pendingPubVo.setEmail(pubInfo.getEmail());
        pendingPubVo.setUnitInfo(pubInfo.getUnitInfo());
        pendingPubVo.setAuthorNames(pubInfo.getAuthorNames());
        pendingPubVo.setAuthorNamesAbbr(pubInfo.getAuthorNamesAbbr());
        String country = StringUtils.isNotBlank(pubInfo.getCountry()) ? pubInfo.getCountry() : pubInfo.getCity();
        pendingPubVo.setCountryId(buildCountyArea(country));
        // 构建成果类型 需要的数据
        buildPubTypeInfo(pendingPubVo, pubInfo);
        // 保留一份原始数据，以便于进行成果类型切换时，成果类型的转换
        pendingPubVo.setPubFileInfo(pubInfo);
        pendingPubVo.setSituations(buildSituations(pubInfo.getSitationJson()));
        setSituationsShow(pendingPubVo);
        pendingImportPubList.add(pendingPubVo);

      }

    }

    return pendingImportPubList;
  }

  private Long buildCountryId(String country) {
    Long countryId = null;
    List<ConstRegion> regionList = constRegionDao.findAll();
    if (StringUtils.isNotEmpty(country)) {
      countryId = PubParamUtils.matchCountryId(regionList, country);
    }
    return countryId;
  }

  /**
   * 匹配国家地区
   * 
   * @param country
   * @return
   */
  private Long buildCountyArea(String country) {
    Long countryId = null;
    List<ConstRegion> regionList = constRegionDao.findAll();
    if (StringUtils.isNotEmpty(country)) {
      countryId = PubParamUtils.matchCountryId(regionList, country);
    }
    return countryId;
    /**
     * 以下是之前的匹配逻辑
     */
    // if (StringUtils.isBlank(country)) {
    // return null;
    // }
    // String[] areaArray = country.split("(?<=国)|[省市]");
    // if (areaArray.length > 1) {
    // List<ConstRegion> areaList = new ArrayList<ConstRegion>();
    // String searchKey = "";
    // if (areaArray.length > 2) {// 有三级地区(中国)
    // areaList = constRegionDao.getAcregion(areaArray[1], null, 1);
    // searchKey = areaArray[2];
    // } else {// 二级地区
    // areaList = constRegionDao.getAcregion(areaArray[0], null, 1);
    // searchKey = areaArray[1];
    // }
    // if (CollectionUtils.isEmpty(areaList)) {
    // return null;
    // }
    // Long superAreaId = Optional.ofNullable(areaList).map(list ->
    // list.get(0)).map(ConstRegion::getId).orElse(null);
    // List<Map<String, Object>> AreaMap = constRegionDao.getRegionNameList(searchKey, superAreaId);
    // if (CollectionUtils.isNotEmpty(AreaMap)) {
    // return NumberUtils.toLong(Objects.toString(AreaMap.get(0).get("code")));
    // } else {
    // return null;
    // }
    // }
    // ConstRegion constRegion = constRegionDao.getConstRegionByName(country);
    // return Optional.ofNullable(constRegion).map(ConstRegion::getId).orElse(null);
  }

  private void setSituationsShow(PendingImportPubVO vo) {
    List<PubSituationDTO> importSituations = vo.getSituations();
    if (CollectionUtils.isNotEmpty(importSituations)) {
      for (PubSituationDTO sitDTO : importSituations) {
        String desc = sitDTO.getLibraryName();
        if ("EI".equalsIgnoreCase(desc)) {
          vo.setEIIncluded(sitDTO.isSitStatus());
          continue;
        }
        if ("ISTP".equalsIgnoreCase(desc)) {
          vo.setISTPIncluded(sitDTO.isSitStatus());
          continue;
        }
        if ("SCIE".equalsIgnoreCase(desc)) {
          vo.setSCIEIncluded(sitDTO.isSitStatus());
          continue;
        }
        if ("SSCI".equalsIgnoreCase(desc)) {
          vo.setSSCIIncluded(sitDTO.isSitStatus());
          continue;
        }
      }
    }
  }

  private List<PubSituationDTO> buildSituations(List<SitationInfo> sitationInfoList) {
    List<PubSituationDTO> situations = new ArrayList<PubSituationDTO>();
    if (sitationInfoList == null) {
      return situations;
    }
    for (SitationInfo sitationInfo : sitationInfoList) {
      PubSituationDTO sit = new PubSituationDTO();
      sit.setLibraryName(PubLibraryEnum.parse(sitationInfo.getLibraryName()).desc);
      sit.setSitStatus(sitationInfo.getSitStatus() == 1 ? true : false);
      situations.add(sit);
    }
    return situations;
  }

  /**
   * 构建成果类型对象 这里是统一的构建方法， 如果出现错误字段
   * 
   * 这个方法不能修改，只能修改该抽象类---》子类的字段值
   * 
   * 成果类型 1:奖励；2:书/著作；3:会议论文；4:期刊论文；5:专利；7:其他；8:学位论文；10:书籍章节
   * 
   * @param pubSaveData
   * @param pubInfo
   * @throws ParseException
   */
  private void buildPubTypeInfo(PendingImportPubVO<PubTypeInfoDTO> pubSaveData, PubFileInfo pubInfo)
      throws ParseException {
    int pubType = pubSaveData.getPubType();
    switch (pubType) {
      case 1:
        AwardsInfoDTO awardsInfo = new AwardsInfoDTO();
        awardsInfo.setCategory(pubInfo.getAwardCategory());// 奖项种类
        awardsInfo.setIssuingAuthority(pubInfo.getIssueInsName());// 授奖机构
        awardsInfo.setIssueInsId(Long.getLong(pubInfo.getIssue()));// 授奖机构id
        awardsInfo.setCertificateNo(pubInfo.getCertificateNo());// 证书编号
        awardsInfo.setAwardDate(changeStringToDate(pubInfo.getAwardDate()));// 授奖日期
        awardsInfo.setGrade(pubInfo.getAwardGrade());// 奖项等级
        pubSaveData.setPubTypeInfo(awardsInfo);
        break;
      case 2:
      case 10:
        // 书籍 取booktitle 书著 取 title
        BookInfoDTO bookInfo = new BookInfoDTO();
        pubSaveData.setTitle(pubInfo.getTitle());// 书籍名
        bookInfo.setName(pubInfo.getBookTitle());// 书名
        bookInfo.setSeriesName(pubInfo.getSeriesName());// 丛书名称
        bookInfo.setEditors(pubInfo.getEditors());// 编辑
        bookInfo.setISBN(pubInfo.getISBN());// 国际标准图书编号
        bookInfo.setPublisher(pubInfo.getPublisher());// 出版社
        bookInfo.setTotalPages(pubInfo.getTotalPages());// 总页数
        bookInfo.setPageNumber(pubInfo.getPageNumber()); // 起止页码或者文章号
        bookInfo.setType(pubInfo.getCategoryValue());// 书籍/著作类型
        bookInfo.setTotalWords(pubInfo.getTotalWords());// 总字数
        bookInfo.setChapterNo(pubInfo.getChapterNo());// 章节号;
        bookInfo.setLanguage(pubInfo.getLanguage());
        pubSaveData.setPubTypeInfo(bookInfo);
        break;
      case 3:
        ConferencePaperDTO conferencePaper = new ConferencePaperDTO();
        // 论文类别// 国际学术会议/国内学术会议
        conferencePaper.setPaperType(pubInfo.getPaperTypeValue());
        conferencePaper.setName(pubInfo.getConfName());// 会议名称
        conferencePaper.setPapers(pubInfo.getPapersName());// 会议集名confName
        conferencePaper.setOrganizer(pubInfo.getOrganizer());// 会议组织者
        conferencePaper.setStartDate(changeStringToDate(pubInfo.getStartDate()));// 开始日期
        conferencePaper.setEndDate(changeStringToDate(pubInfo.getEndDate()));// 结束日期
        conferencePaper.setPageNumber(pubInfo.getPageNumber());// 起止页码或者文章号
        pubSaveData.setPubTypeInfo(conferencePaper);
        break;
      case 4:
        JournalInfoDTO journalInfo = new JournalInfoDTO();
        journalInfo.setJid(null);// 期刊id
        journalInfo.setName(pubInfo.getOriginal());// 期刊名称
        journalInfo.setVolumeNo(pubInfo.getVolumeNo());// 卷号
        journalInfo.setIssue(pubInfo.getIssue());// 期号
        journalInfo.setISSN(pubInfo.getIssn());
        journalInfo.setPageNumber(pubInfo.getPageNumber());// 起止页码或者文章号
        journalInfo.setPublishStatus(pubInfo.getPublicationStatus());// 发表状态(P已发表/A已接收)
        pubSaveData.setPubTypeInfo(journalInfo);
        break;
      case 5:
        PatentInfoDTO patentInfo = new PatentInfoDTO();
        patentInfo.setType(pubInfo.getPatenType());// 专利类别，发明专利51/实用新型52/外观设计53
        patentInfo.setArea(pubInfo.getPatentArea());// 专利国家
                                                    // 中国专利/美国专利/欧洲专利/WIPO专利/日本专利/其他
        if ("1".equals(pubInfo.getPatentStatus()) || "授权".equals(pubInfo.getPatentStatus())) {
          patentInfo.setStatus(1);// 专利状态，申请/授权
        } else {
          patentInfo.setStatus(0);// 专利状态，申请/授权
        }
        patentInfo.setApplier(pubInfo.getApplier());// 申请人 (专利为申请状态)
        String patentDate = "";
        if (StringUtils.isNotBlank(pubInfo.getApplicationDate())) {
          patentDate = pubInfo.getApplicationDate();
        } else {
          patentDate = pubInfo.getPubyear();
        }
        patentInfo.setApplicationDate(changeStringToDate(patentDate));// 申请（公开）日期
        patentInfo.setIPC(pubInfo.getIpc());
        patentInfo.setCPC(pubInfo.getCpc());
        patentInfo.setPatentee(pubInfo.getApplier());// 专利权人 (专利为授权状态)
        patentInfo.setStartDate(changeStringToDate(pubInfo.getStartDate()));// 专利生效起始日期
                                                                            // (专利为授权状态)
        patentInfo.setEndDate(changeStringToDate(pubInfo.getEndDate()));// 专利失效结束日期
                                                                        // (专利为授权状态)
        String applicationNo =
            StringUtils.isNotBlank(pubInfo.getApplicationNo()) ? pubInfo.getApplicationNo() : pubInfo.getPatentNo();
        patentInfo.setApplicationNo(applicationNo);// 申请号
        patentInfo.setPublicationOpenNo(pubInfo.getPatentOpenNo());// 专利公开（公告）号
        patentInfo.setTransitionStatus(pubInfo.getPatentChangeStatus());// 专利转换状态
        patentInfo.setPrice(pubInfo.getPatentPrice());// 交易金额
        patentInfo.setIssuingAuthority(pubInfo.getIssuingAuthority());// 专利授权组织，颁发机构

        pubSaveData.setPubTypeInfo(patentInfo);
        break;
      case 8:
        ThesisInfoDTO thesisInfo = new ThesisInfoDTO();
        thesisInfo.setDegree(pubInfo.getDegree());// 学位
        thesisInfo.setDefenseDate(changeStringToDate(pubInfo.getDefenseDate()));// 答辩日期
        thesisInfo.setIssuingAuthority(pubInfo.getIssuingAuthority());// 颁发单位
        thesisInfo.setDepartment(pubInfo.getDepartment());// 部门
        thesisInfo.setISBN(pubInfo.getISBN());// 国际标准图书编号
        pubSaveData.setPubTypeInfo(thesisInfo);
        break;
      case 12:
        StandardInfoDTO standardInfo = new StandardInfoDTO();
        standardInfo.setStandardNo(pubInfo.getStandardNo());// 标准号
        PubStandardTypeEnum type = pubInfo.getType();
        standardInfo.setType(type);// 标准类型
        standardInfo.setPublishAgency(pubInfo.getPublishAgency());// 公布机构
        standardInfo.setTechnicalCommittees(pubInfo.getTechnicalCommittees());// 归口单位
        pubSaveData.setPubTypeInfo(standardInfo);
        break;
      case 13:
        SoftwareCopyrightDTO softwareCopyright = new SoftwareCopyrightDTO();
        softwareCopyright.setRegisterNo(pubInfo.getRegisterNo());// 登记号
        softwareCopyright.setAcquisitionType(pubInfo.getAcquisitionType());// 获得方式
        softwareCopyright.setScopeType(pubInfo.getScopeType());// 权利范围
        pubSaveData.setPubTypeInfo(softwareCopyright);
        break;
      default:
        break;
    }

  }

  private String changeStringToDate(String dateStr) {
    if (StringUtils.isEmpty(dateStr)) {
      return "";
    }
    dateStr = dateStr.replace("/", "-");
    dateStr = dateStr.replace(".", "-");
    return dateStr;
  }
}
