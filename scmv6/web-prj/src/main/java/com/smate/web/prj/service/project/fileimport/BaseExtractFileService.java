package com.smate.web.prj.service.project.fileimport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.consts.service.InstitutionService;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.xss.XssUtils;
import com.smate.web.prj.form.fileimport.PrjInfoDTO;
import com.smate.web.prj.form.fileimport.PrjMemberDTO;

/**
 * @author aijiangbin
 * @create 2019-06-13 14:20
 **/
public abstract class BaseExtractFileService implements ExtractFileService {


  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected static final String splitNameReg = "，|；|;| and ";
  protected static final String splitKeywordReg = "；|;";
  @Autowired
  private InstitutionService institutionService;

  /**
   *
   * resutlMap.put("list", ""); 把文件提取成 list列表对象 resutlMap.put("warnmsg", ""); 错误信息
   * resutlMap.put("count", 0); 条数
   * 
   * @return
   */
  @Override
  public Map<String, Object> extractFile(File file, String sourceFileFileName) {
    Map<String, Object> checkFile = checkFile(file, sourceFileFileName);
    Map<String, Object> resutlMap = new HashMap<>();
    if (checkFile == null) {
      resutlMap = extractData(file);
    } else {
      resutlMap.put("count", 0);
      resutlMap.put("warnmsg", resutlMap.get("warnmsg"));
    }
    return resutlMap;
  }

  /**
   * 检查文件 正确直接返回 null resutlMap.put("warnmsg", ""); 错误信息
   * 
   * @param file
   * @return
   */
  public abstract Map<String, Object> checkFile(File file, String sourceFileFileName);

  public abstract List<PrjInfoDTO> parseFile(File file);


  /**
   *
   * resutlMap.put("list", ""); 把文件提取成 list列表对象 resutlMap.put("warnmsg", ""); 错误信息
   * resutlMap.put("count", 0); 条数
   * 
   * @return
   */
  public Map<String, Object> extractData(File file) {
    List<PrjInfoDTO> list = parseFile(file);
    Map<String, Object> resutlMap = new HashMap<>();
    if (CollectionUtils.isNotEmpty(list)) {
      // buildPrjXml(list, resutlMap);
      resutlMap.put("list", list);
      resutlMap.put("count", list.size());
    } else {
      resutlMap.put("count", 0);
    }
    return resutlMap;
  }

  /**
   * 构建项目xml
   * 
   * @param list
   * @param resutlMap
   */
  public void buildPrjXml(List<PrjInfoDTO> list, Map<String, Object> resutlMap) {
    try {

      XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
      ByteArrayOutputStream is1 = new ByteArrayOutputStream();
      XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(is1, "Utf-8");
      xmlWriter.writeStartDocument("Utf-8", "1.0");
      xmlWriter.writeStartElement("scholarWorks");
      Integer seqNo = 0;
      for (PrjInfoDTO prjInfo : list) {

        escapeObj(prjInfo);
        xmlWriter.writeStartElement("data");

        xmlWriter.writeStartElement("prj_meta");
        xmlWriter.writeAttribute("schema_version", "3.0");
        xmlWriter.writeEndElement();

        xmlWriter.writeStartElement("prj_fulltext");
        xmlWriter.writeAttribute("fulltext_url", prjInfo.getFullLink());
        xmlWriter.writeEndElement();
        if (CollectionUtils.isNotEmpty(prjInfo.getMembersList())) {
          buildMembersList(xmlWriter, prjInfo);
        }
        xmlWriter.writeStartElement("project");
        xmlWriter.writeAttribute("file_import", "true");
        if (CollectionUtils.isEmpty(prjInfo.getMembersList())) {
          buildAuthors(prjInfo);
        }
        xmlWriter.writeAttribute("notify_author_name", prjInfo.getLeader());
        xmlWriter.writeAttribute("file_fulltext_url", prjInfo.getFullLink());
        xmlWriter.writeAttribute("source_db_code", prjInfo.getSourceDbCode());
        xmlWriter.writeAttribute("funding_year", prjInfo.getFundingYear());
        xmlWriter.writeAttribute("remark", prjInfo.getRemark());
        xmlWriter.writeAttribute("prj_state", prjInfo.getPrjStatus());
        xmlWriter.writeAttribute("prj_no", prjInfo.getProjectNo());
        xmlWriter.writeAttribute("start_date", DateUtils.formateDateString(prjInfo.getStartDate()));
        xmlWriter.writeAttribute("end_date", DateUtils.formateDateString(prjInfo.getEndDate()));
        xmlWriter.writeAttribute("ctitle", prjInfo.getZhTitle());
        xmlWriter.writeAttribute("etitle", prjInfo.getEnTitle());
        xmlWriter.writeAttribute("organization", prjInfo.getInsName());
        xmlWriter.writeAttribute("eabstract", prjInfo.getEnAbstract());
        xmlWriter.writeAttribute("cabstract", prjInfo.getZhAbstract());
        xmlWriter.writeAttribute("ekeywords", dealKeyword(prjInfo.getEnKeywords()));
        xmlWriter.writeAttribute("ckeywords", dealKeyword(prjInfo.getZhkeywords()));
        xmlWriter.writeAttribute("amount", prjInfo.getPrjAmount());
        xmlWriter.writeAttribute("amount_unit", prjInfo.getAmountUnit());
        xmlWriter.writeAttribute("prj_scheme_agency_name", prjInfo.getAgency());
        xmlWriter.writeAttribute("prj_scheme_name", prjInfo.getScheme());
        // 项目类型,1：内部项目，0:外部项目
        String prj_type = "";
        if ("内部项目".equals(prjInfo.getPrjType())) {
          prj_type = "1";
        } else if ("外部项目".equals(prjInfo.getPrjType())) {
          prj_type = "0";
        }
        xmlWriter.writeAttribute("prj_type", prj_type);
        xmlWriter.writeAttribute("brief_desc", prjInfo.getZhAbstract());
        xmlWriter.writeAttribute("author_names", prjInfo.getPrjMembers());
        xmlWriter.writeAttribute("science_area", prjInfo.getScienceArea());
        xmlWriter.writeAttribute("subject_code1", prjInfo.getSubject_code1());
        xmlWriter.writeAttribute("subject_code2", prjInfo.getSubject_code2());
        xmlWriter.writeEndElement();

        xmlWriter.writeEndElement();
      }
      xmlWriter.writeEndElement();
      xmlWriter.writeEndDocument();
      is1.flush();
      is1.close();
      xmlWriter.flush();
      xmlWriter.close();
      resutlMap.put("xmlData", is1.toString());
    } catch (Exception e) {
      logger.error("构建xml数据异常", e);
    }
  }

  private void buildMembersList(XMLStreamWriter xmlWriter, PrjInfoDTO prjInfo) throws XMLStreamException {
    Map<String, String> authorInsIds = new HashMap<String, String>();// 缓存匹配到的ID
    xmlWriter.writeStartElement("prj_members_temp");
    for (PrjMemberDTO member : prjInfo.getMembers()) {
      xmlWriter.writeStartElement("prj_member");
      xmlWriter.writeAttribute("seq_no", member.getSeqNo());
      xmlWriter.writeAttribute("member_psn_name", member.getName());
      xmlWriter.writeAttribute("ins_name1", member.getInsName());
      xmlWriter.writeAttribute("email", member.getEmail());
      // 手机号暂时先不加
      // xmlWriter.writeAttribute("mobile_phone", member.getMobile());
      xmlWriter.writeAttribute("open_id", member.getOpenId());
      xmlWriter.writeAttribute("notify_author", member.getIsLeader().equals("是") ? "1" : "0");
      // ID为空，名字不为空
      String insName = member.getInsName();
      String insId = "";
      if (StringUtils.isNotBlank(insName)) {
        // 匹配单位ID
        if (authorInsIds.containsKey(insName)) {
          insId = authorInsIds.get(insName);
        } else {
          // 匹配单位ID
          Optional<Institution> optIns = institutionService.findByName(insName);
          insId = optIns.map(ins -> Objects.toString(ins.getId())).orElse(null);
          authorInsIds.put(insName, insId);
        }
      }
      xmlWriter.writeAttribute("ins_id1", StringUtils.isBlank(insId) ? "" : insId);
      xmlWriter.writeEndElement();
    }
    xmlWriter.writeEndElement();
  }

  /**
   * 反转义数据
   * 
   * @param prjInfo
   */
  private void escapeObj(PrjInfoDTO prjInfo) {
    try {
      Field[] fields = PrjInfoDTO.class.getDeclaredFields();
      for (Field field : fields) {
        field.setAccessible(true);
        Object o = field.get(prjInfo);
        Class<?> type = field.getType();
        if ("java.lang.String".equals(type.getName()) && StringUtils.isNotBlank(o.toString())) {
          String val = StringEscapeUtils.escapeHtml4(o.toString());
          val = XssUtils.filterByXssStr(val);
          field.set(prjInfo, val);
        }
      }
    } catch (Exception e) {
      logger.error("反转义数据异常", e);
    }

  }


  /**
   * 处理作者
   * 
   * @param prjInfo
   */
  private void buildAuthors(PrjInfoDTO prjInfo) {
    if (StringUtils.isNotBlank(prjInfo.getLeader()) && StringUtils.isNotBlank(prjInfo.getPrjMembers())) {
      String[] split = prjInfo.getPrjMembers().split(splitNameReg);
      boolean flag = false;
      for (String name : split) {
        if (prjInfo.getLeader().equals(name)) {
          flag = true;
          break;
        }
      }
      if (!flag) {
        prjInfo.setPrjMembers(prjInfo.getLeader() + ";" + prjInfo.getPrjMembers());
      }
    } else if (StringUtils.isNotBlank(prjInfo.getLeader())) {
      prjInfo.setPrjMembers(prjInfo.getLeader());
    }
  }

  /**
   * 处理 关键词
   * 
   * @param keywords
   * @return
   */
  private String dealKeyword(String keywords) {
    if (StringUtils.isBlank(keywords))
      return "";
    StringBuilder sb = new StringBuilder();
    String[] split = keywords.split(splitKeywordReg);
    int count = 0;
    List<String> keyList = new ArrayList<>();
    for (String key : split) {
      if (!keyList.contains(key) && count < 5) {
        count++;
        sb.append(key);
        keyList.add(key);
        sb.append(";");
      }
    }
    return sb.toString();
  }

  /**
   * 构建单个项目xml
   * 
   * @param prjInfo
   */
  public String buildPrjXmlData(PrjInfoDTO prjInfo) {
    try {
      XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
      ByteArrayOutputStream is1 = new ByteArrayOutputStream();
      XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(is1, "Utf-8");
      xmlWriter.writeStartDocument("Utf-8", "1.0");
      escapeObj(prjInfo);
      xmlWriter.writeStartElement("data");

      xmlWriter.writeStartElement("prj_meta");
      xmlWriter.writeAttribute("schema_version", "3.0");
      xmlWriter.writeEndElement();

      xmlWriter.writeStartElement("prj_fulltext");
      xmlWriter.writeAttribute("fulltext_url", prjInfo.getFullLink());
      xmlWriter.writeEndElement();
      if (CollectionUtils.isNotEmpty(prjInfo.getMembers())) {
        buildMembersList(xmlWriter, prjInfo);
      }
      xmlWriter.writeStartElement("project");
      xmlWriter.writeAttribute("file_import", "true");

      xmlWriter.writeAttribute("notify_author_name", prjInfo.getLeader());
      xmlWriter.writeAttribute("file_fulltext_url", prjInfo.getFullLink());
      xmlWriter.writeAttribute("source_db_code", prjInfo.getSourceDbCode());
      xmlWriter.writeAttribute("funding_year", prjInfo.getFundingYear());
      xmlWriter.writeAttribute("remark", prjInfo.getRemark());
      xmlWriter.writeAttribute("prj_state", prjInfo.getPrjStatus());
      xmlWriter.writeAttribute("prj_no", prjInfo.getProjectNo());
      xmlWriter.writeAttribute("start_date", DateUtils.formateDateString(prjInfo.getStartDate()));
      xmlWriter.writeAttribute("end_date", DateUtils.formateDateString(prjInfo.getEndDate()));
      xmlWriter.writeAttribute("ctitle", prjInfo.getZhTitle());
      xmlWriter.writeAttribute("etitle", prjInfo.getEnTitle());
      xmlWriter.writeAttribute("organization", prjInfo.getInsName());
      xmlWriter.writeAttribute("eabstract", prjInfo.getEnAbstract());
      xmlWriter.writeAttribute("cabstract", prjInfo.getZhAbstract());
      xmlWriter.writeAttribute("ekeywords", dealKeyword(prjInfo.getEnKeywords()));
      xmlWriter.writeAttribute("ckeywords", dealKeyword(prjInfo.getZhkeywords()));
      xmlWriter.writeAttribute("amount", prjInfo.getPrjAmount());
      xmlWriter.writeAttribute("amount_unit", prjInfo.getAmountUnit());
      xmlWriter.writeAttribute("prj_scheme_agency_name", prjInfo.getAgency());
      xmlWriter.writeAttribute("prj_scheme_name", prjInfo.getScheme());
      // 项目类型,1：内部项目，0:外部项目
      String prj_type = "";
      if ("内部项目".equals(prjInfo.getPrjType())) {
        prj_type = "1";
      } else if ("外部项目".equals(prjInfo.getPrjType())) {
        prj_type = "0";
      }
      xmlWriter.writeAttribute("prj_type", prj_type);
      xmlWriter.writeAttribute("brief_desc", prjInfo.getZhAbstract());
      // xmlWriter.writeAttribute("author_names", prjInfo.getPrjMembers());
      xmlWriter.writeAttribute("science_area", prjInfo.getScienceArea());
      xmlWriter.writeAttribute("subject_code1", prjInfo.getSubjectCode());
      xmlWriter.writeAttribute("dup_value", ObjectUtils.toString(prjInfo.getDupValue(), ""));
      xmlWriter.writeEndElement();

      xmlWriter.writeEndElement();
      xmlWriter.writeEndDocument();
      is1.flush();
      is1.close();
      xmlWriter.flush();
      xmlWriter.close();
      return is1.toString();
    } catch (Exception e) {
      logger.error("构建xml数据异常", e);
    }
    return "";
  }

}
