package com.smate.web.prj.service.project.fileimport;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.project.service.ProjectService;
import com.smate.core.base.project.vo.ProjectInfo;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.date.DateStringSplitFormateUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.url.HttpRequestUtils;
import com.smate.core.base.utils.xss.XssUtils;
import com.smate.web.prj.consts.PrjXmlOperationEnum;
import com.smate.web.prj.dao.project.PrjImportOriginalDataDao;
import com.smate.web.prj.dao.project.ScmPrjXmlDao;
import com.smate.web.prj.dao.project.SnsProjectQueryDao;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.PrjImportForm;
import com.smate.web.prj.form.PrjXmlProcessContext;
import com.smate.web.prj.form.fileimport.PrjInfoDTO;
import com.smate.web.prj.form.fileimport.PrjMemberDTO;
import com.smate.web.prj.model.common.PrjImportOriginalData;
import com.smate.web.prj.model.common.ScmPrjXml;
import com.smate.web.prj.service.project.SnsPrjXmlService;
import com.smate.web.prj.service.project.search.ImportPrjXmlDealService;
import com.smate.web.prj.util.PrjUtils;
import com.smate.web.prj.util.ProjectHash;
import com.smate.web.prj.xml.PrjXmlDocument;
import com.smate.web.prj.xml.PrjXmlDocumentBuilder;
import com.smate.web.prj.xml.PrjXmlProcessor;

/**
 * 项目文件导入
 * 
 * @author aijiangbin
 * @create 2019-06-13 14:15
 **/

@Service("prjFileImportService")
@Transactional(rollbackOn = Exception.class)
public class PrjFileImportServiceImpl implements PrjFileImportService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "extractExcelFileService")
  private ExtractFileService extractExcelFileService;
  @Resource(name = "extractMdbFileService")
  private ExtractFileService extractMdbFileService;
  @Resource(name = "extractJXKJTExcelFileService")
  private ExtractFileService extractJXKJTExcelFileService;
  @Autowired
  private SnsProjectQueryDao snsProjectQueryDao;
  @Autowired
  private SnsPrjXmlService snsPrjXmlService;
  @Autowired
  private CacheService cacheService;
  private List<ImportPrjXmlDealService> xmlDealServiceList;
  @Autowired
  private ProjectService projectService;
  @Autowired
  private ScmPrjXmlDao scmPrjXmlDao;
  @Autowired
  private PrjImportOriginalDataDao prjImportOriginalDataDao;
  @Value("${domainscm}")
  private String domainscm;

  public List<ImportPrjXmlDealService> getXmlDealServiceList() {
    return xmlDealServiceList;
  }

  public void setXmlDealServiceList(List<ImportPrjXmlDealService> xmlDealServiceList) {
    this.xmlDealServiceList = xmlDealServiceList;
  }

  @Override
  public Map<String, Object> extractFileData(File file, String sourceType, String sourceFileFileName) {
    Map<String, Object> map = new HashMap<>();
    // 导入的文件类型 SCMIRIS SCMEXCEL
    if ("SCMIRIS".equalsIgnoreCase(sourceType)) {
      map = extractMdbFileService.extractFile(file, sourceFileFileName);
    } else if ("SCMEXCEL".equalsIgnoreCase(sourceType)) {
      map = extractExcelFileService.extractFile(file, sourceFileFileName);
    } else if ("JXKJT".equalsIgnoreCase(sourceType)) {
      map = extractJXKJTExcelFileService.extractFile(file, sourceFileFileName);
    }
    return map;
  }


  @Override
  public void buildPendingImportPrjByXml(PrjImportForm form) throws ServiceException {
    try {
      Long psnId = form.getPsnId();
      if (CollectionUtils.isNotEmpty(form.getFilePrjInfos())) {
        Locale locale = LocaleContextHolder.getLocale();
        List<PrjInfoDTO> prjInfoList = form.getFilePrjInfos();
        Integer recCount = prjInfoList.size();
        form.setCount(recCount);
        form.setFilePrjInfos(new ArrayList<>());
        for (int index = 0; index < recCount; index++) {
          PrjInfoDTO prjInfo = prjInfoList.get(index);
          // xss 过滤以及内容转义
          String prjJson = JacksonUtils.jsonObjectSerializer(prjInfo);
          prjJson = XssUtils.transferJson(prjJson);
          prjInfo = JacksonUtils.jsonObject(prjJson, PrjInfoDTO.class);
          // 进行数据校验
          checkPrjInfo(prjInfo);

          String ctitle = prjInfo.getZhTitle();
          String etitle = prjInfo.getEnTitle();
          if (StringUtils.isNotBlank(ctitle) || StringUtils.isNotBlank(etitle)) {
            Long dupPrjId = snsProjectQueryDao.getDupPrjId(ProjectHash.cleanTitleHash(ctitle),
                ProjectHash.cleanTitleHash(etitle), psnId);
            prjInfo.setDupValue(Objects.toString(dupPrjId, ""));
            // 重新构建项目xml
            // PrjXmlDocument xmlDocument = this.rebuildImportXml(pitem, psnId);
            // 构建待导入列表显示信息
            this.buildPrjInfo(prjInfo, form, locale);
          }
          form.getFilePrjInfos().add(prjInfo);
        }
        // 缓存待导入项目xml
        if (CollectionUtils.isNotEmpty(form.getFilePrjInfos())) {
          String key = "" + psnId;
          cacheService.put("fileImportPrjCache", 60 * 60, psnId.toString(), (Serializable) form.getFilePrjInfos());
          form.setCacheKey(key);
        }
      }
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  /**
   * 进行数据校验
   * 
   * @param prjInfo
   */
  private void checkPrjInfo(PrjInfoDTO prjInfo) {
    // 1.开始日期和结束日期，开始日期小于结束日期，否则将开结束日期置空
    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
    Map<String, String> startMap = DateStringSplitFormateUtil.split(prjInfo.getStartDate());
    Map<String, String> endMap = DateStringSplitFormateUtil.split(prjInfo.getEndDate());
    try {
      Date startDate = format.parse(startMap.get("fomate_date"));
      Date endDate = format.parse(endMap.get("fomate_date"));
      if (startDate.compareTo(endDate) == 1) {
        // startDate > endDate
        prjInfo.setEndDate("");
      }
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private PrjXmlDocument buildPrjXmlWithPrjInfo(PrjInfoDTO prjInfo, Long psnId) {
    String xmlData = extractJXKJTExcelFileService.buildPrjXmlData(prjInfo);
    if (StringUtils.isBlank(xmlData))
      return null;
    try {
      Document doc = DocumentHelper.parseText(xmlData);
      Node node = doc.selectSingleNode("/data");
      PrjXmlDocument xmlDocument = this.rebuildImportXml(node, psnId);

      return xmlDocument;
    } catch (Exception e) {
      logger.error("prjInfo解析xml 对象失败 xmlData=" + xmlData, e);
    }


    return null;
  }

  /**
   * .重新构建项目xml信息.
   *
   * @param pitem
   */
  private PrjXmlDocument rebuildImportXml(Node pitem, Long psnId) throws Exception {
    PrjXmlProcessContext xmlContext = snsPrjXmlService.buildXmlProcessContext(psnId);
    xmlContext.setCurrentAction(PrjXmlOperationEnum.Import);
    PrjXmlDocument xmlDocument = new PrjXmlDocument(pitem.asXML());
    for (ImportPrjXmlDealService service : xmlDealServiceList) {
      service.checkParameter(xmlContext);
      service.dealWithXml(xmlDocument, xmlContext);
    }
    new PrjXmlProcessor(xmlDocument).generateBrief();
    return xmlDocument;
  }


  /**
   * 构建页面待导入列表显示信息
   *
   * @param prjInfo
   * @param form
   * @param locale
   */
  private void buildPrjInfo(PrjInfoDTO prjInfo, PrjImportForm form, Locale locale) {
    List<ProjectInfo> prjList = form.getPrjInfoList();
    if (prjList == null) {
      prjList = new ArrayList<ProjectInfo>();
    }
    ProjectInfo info = new ProjectInfo();
    // 显示的标题信息
    info.setShowTitle(LocaleTextUtils.getLocaleText(locale, prjInfo.getZhTitle(), prjInfo.getEnTitle()));
    // 作者名信息
    String authorNames = buildAuthorNames(prjInfo.getMembers());
    info.setShowAuthorNames(authorNames);

    // 简短描述信息
    info.setShowBriefDesc(buildBriefDesc(prjInfo));
    info.setDes3DupPrjId(prjInfo.getDupValue());
    info.setExternalNo(prjInfo.getProjectNo());
    String dup_value = prjInfo.getDupValue();
    if (StringUtils.isNotBlank(dup_value) && NumberUtils.isDigits(dup_value)) {
      info.setDupPrjId(NumberUtils.toLong(dup_value));
    }
    prjList.add(info);
    form.setPrjInfoList(prjList);
  }


  private String buildAuthorNames(List<PrjMemberDTO> memberList) {
    StringBuffer authorNames = new StringBuffer();
    if (CollectionUtils.isEmpty(memberList)) {
      return null;
    }
    for (PrjMemberDTO p : memberList) {
      String name = StringUtils.trimToEmpty(p.getName());
      if (StringUtils.isBlank(name)) {
        continue;
      }
      if (getLocale(name).equals(Locale.CHINA)) {
        // 中文作者名数据，去除名字中间所有的空格
        name = name.replaceAll("\\s+", "");
      }
      if (authorNames.length() > 0) {
        authorNames.append("; ");
      }
      authorNames.append(name);
    }
    // 作者的长度超过400，则进行截取
    if (authorNames.length() >= 400) {
      authorNames.substring(0, 399);
    }

    return authorNames.toString();
  }

  public static Locale getLocale(String title) {
    if (StringUtils.isEmpty(title)) {
      return Locale.US;
    }
    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    if (p.matcher(title).find()) {
      return Locale.CHINA;
    }
    return Locale.US;
  }

  private String buildBriefDesc(PrjInfoDTO prjInfo) {
    String showBriefDesc = "";
    // 构建日期显示格式
    Map<String, String> startMap = DateStringSplitFormateUtil.split(prjInfo.getStartDate());
    String start_date = startMap.get("fomate_date");
    Map<String, String> endMap = DateStringSplitFormateUtil.split(prjInfo.getEndDate());
    String end_date = endMap.get("fomate_date");
    String amount = prjInfo.getPrjAmount();
    if (StringUtils.isNotBlank(start_date) && StringUtils.isNotBlank(end_date)) {
      showBriefDesc = start_date + " ~ " + end_date;
    } else if (StringUtils.isNotBlank(start_date)) {
      showBriefDesc = start_date;
    } else if (StringUtils.isNotBlank(start_date)) {
      showBriefDesc = end_date;
    }

    if (StringUtils.isNotBlank(amount)) {
      if (StringUtils.isNotBlank(showBriefDesc))
        showBriefDesc = showBriefDesc + ", " + amount;
      else
        showBriefDesc = amount;
    }
    return showBriefDesc;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void savePendingImportPrjs(PrjImportForm form) throws ServiceException {
    // 导入成功的项目数量
    int importSuccessNum = 0;
    try {
      List<PrjInfoDTO> prjInfos = (List<PrjInfoDTO>) cacheService.get("fileImportPrjCache", form.getPsnId().toString());
      if (prjInfos != null && CollectionUtils.isNotEmpty(form.getPrjJsonList())) {
        // 遍历处理项目XML
        for (int i = 0; i < prjInfos.size(); i++) {
          StringBuilder sb = new StringBuilder();
          Map<String, String> opt = (Map<String, String>) form.getPrjJsonList().get(i);
          prjInfos.get(i).setDupOpt(opt.get("dup_opt"));
          sb.append("prjData=" + PrjUtils.encode(JacksonUtils.jsonObjectSerializer(prjInfos.get(i))));
          sb.append("&psnId=" + form.getPsnId().toString());
          String result = HttpRequestUtils.sendPost(domainscm + PrjUtils.SAVA_PRJ_DATA_URL, sb.toString());
          if (StringUtils.isNotBlank(result) && JacksonUtils.isJsonString(result)) {
            Map<Object, Object> map = JacksonUtils.jsonToMap(result);
            if (map.get("result") != null && map.get("result").toString().equals("success")) {
              importSuccessNum = importSuccessNum + Integer.parseInt(map.get("importSuccessNum").toString());
            }
          }
        }
      }
    } catch (Exception e) {
      throw new ServiceException(e);
    }
    form.setImportSuccessNum(importSuccessNum);
    // 删除缓存的数据
    cacheService.remove("fileImportPrjCache", form.getPsnId().toString());
  }

  /**
   * 处理导入的项目xml及项目相关表数据
   *
   * @param doc
   * @param psnId
   * @param psnId
   * @param prjInfo
   * @return
   */
  protected Integer dealWithImportPrjXml(PrjXmlDocument doc, Long psnId, PrjInfoDTO prjInfo) throws Exception {
    // 构建PrjXmlProcessContext.
    PrjXmlProcessContext context = this.dealWithImportPrjXmlProcessContext(psnId, prjInfo);
    Long prjId = context.getCurrentPrjId();
    context.setPrjInfo(prjInfo);
    prjInfo.setPrjId(prjId);
    // 导入项目查到重后，选择更新操作要先合并xml
    int importSuccessNum = 0;
    if ("refresh".equals(context.getDupOperation())) {
      ScmPrjXml prjXml = scmPrjXmlDao.get(prjId);
      if (prjXml != null) {
        PrjXmlDocumentBuilder.mergeWhenImport(doc, prjXml.getPrjXml());
      }
    }
    if (NumberUtils.isNotNullOrZero(prjId)) {
      savaPrjInfoData(psnId, prjInfo);
      PrjXmlProcessor processor = new PrjXmlProcessor(doc);
      snsPrjXmlService.dealWithXmlByProcessor(context, doc, processor);
      importSuccessNum++;
    }
    return importSuccessNum;
  }

  private void savaPrjInfoData(Long psnId, PrjInfoDTO prjInfo) {
    PrjImportOriginalData data = prjImportOriginalDataDao.get(prjInfo.getPrjId());
    if (data == null) {
      data = new PrjImportOriginalData();
      data.setGmtGreate(new Date());
      data.setGmtUpdate(data.getGmtGreate());
    } else {
      data.setGmtUpdate(new Date());
    }
    data.setPrjId(prjInfo.getPrjId());
    data.setCreatePsnId(psnId);
    data.setPrjData(JacksonUtils.jsonObjectSerializer(prjInfo));
    data.setType(prjInfo.getTemplate().equalsIgnoreCase("JXKJT") ? 1 : 2);
    prjImportOriginalDataDao.save(data);
  }


  /**
   * 构建PrjXmlProcessContext.
   *
   * @param prjInfo
   * @return prjId
   */
  protected PrjXmlProcessContext dealWithImportPrjXmlProcessContext(Long psnId, PrjInfoDTO prjInfo)
      throws PrjException {
    // 默认新增一个项目ID
    Long prjId = projectService.createPrjId();
    String dupPrjOpt = "no_dup";
    // 项目是否重复，重复的项目选取的操作是什么（跳过、新增、更新）, 1:重复了，0：没重复，默认没重复
    String dupPrjId = prjInfo.getDupValue();
    if (StringUtils.isNotBlank(dupPrjId)) {
      // 获取重复项目选择的操作
      dupPrjOpt = prjInfo.getDupOpt();
    }
    // 查重后更新
    if ("refresh".equals(dupPrjOpt)) {
      prjId = NumberUtils.isCreatable(dupPrjId) ? NumberUtils.parseLong(dupPrjId) : prjId;
      // 查重后跳过
    } else if ("skip".equals(dupPrjOpt)) {
      prjId = 0L;
    }
    // 构建PrjXmlProcessContext
    PrjXmlProcessContext context = snsPrjXmlService.buildXmlProcessContext(psnId);
    context.setCurrentAction(PrjXmlOperationEnum.Import);
    context.setCurrentPrjId(prjId);
    context.setDupOperation(dupPrjOpt);
    return context;
  }

  @Override
  public int savePrjData(PrjInfoDTO prjInfoDTO, Long psnId) throws ServiceException {
    int importSuccessNum = 0;
    try {
      // 进行数据的校验
      checkoutPrjData(prjInfoDTO);
      PrjXmlDocument doc = buildPrjXmlWithPrjInfo(prjInfoDTO, psnId);
      if (doc != null) {
        importSuccessNum = this.dealWithImportPrjXml(doc, psnId, prjInfoDTO);
      }
    } catch (Exception e) {
      logger.error("保存prjData异常", e);
      throw new ServiceException(e);
    }
    return importSuccessNum;
  }

  /**
   * 进行数据的校验操作
   * 
   * @param prjInfoDTO
   */
  private void checkoutPrjData(PrjInfoDTO prjInfoDTO) {


  }
}
