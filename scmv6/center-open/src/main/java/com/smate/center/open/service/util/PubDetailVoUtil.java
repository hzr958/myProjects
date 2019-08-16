package com.smate.center.open.service.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.dto.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * 成果详情转换接口
 * 
 * @author aijiangbin
 * @date 2018年8月14日
 */
public class PubDetailVoUtil {
  private static Logger logger = LoggerFactory.getLogger(PubDetailVoUtil.class);

  public static PubDetailVO getBuilPubDetailVO(String pubJson) throws Exception {
    PubDetailVO pub = new PubDetailVO();
    Map<String, Object> map = null;
    try {
      map = JacksonUtils.json2Map(pubJson);
      if (map.get("pubId") == null) {
        logger.info("json字符串，不能解析成PubDetailVoUtil.PubDetailVO");
        return null;
      }
    } catch (IOException e) {
      logger.error("获取成果详情转化json出错", e);
      throw new Exception();
    }

    Integer pubType = Integer.valueOf(map.get("pubType") == null ? "7" : Objects.toString(map.get("pubType")));
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

  /**
   * 得到年份
   * 
   * @param date
   * @return
   */
  public static String parseDateForYear(String date) {
    if (date == null) {
      return "";
    }
    String[] split = date.split("[-/]");
    return split[0];
  }

  /**
   * 得到月份
   * 
   * @param date
   * @return
   */
  public static String parseDateForMonth(String date) {
    if (date == null) {
      return "";
    }
    String[] split = date.split("[-/]");
    if (split.length > 1) {
      return split[1];
    }
    return "";
  }

  /**
   * 得到天数
   * 
   * @param date
   * @return
   */
  public static String parseDateForDay(String date) {
    if (date == null) {
      return "";
    }
    String[] split = date.split("[-/]");
    if (split.length > 2) {
      return split[2];
    }
    return "";
  }

  /**
   * 得到年份
   * 
   * @param date
   * @return
   */
  public static String parseDateForYear(Date date) {
    if (date == null) {
      return "";
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(calendar.YEAR);
    return "" + year;
  }

  /**
   * 得到月份
   * 
   * @param date
   * @return
   */
  public static String parseDateForMonth(Date date) {
    if (date == null) {
      return "";
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int month = calendar.get(calendar.MONTH) + 1;
    return "" + month;
  }

  /**
   * 得到天数
   * 
   * @param date
   * @return
   */
  public static String parseDateForDay(Date date) {
    if (date == null) {
      return "";
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int day = calendar.get(calendar.DAY_OF_MONTH);
    return "" + day;
  }

  /**
   * 填充是否是本人 {\\\"is_mine\\\":\\\"否\\\",\\\"is_message\\\":\\\"0
   * \\\",\\\"first_author\\\":\\\"1\\\",\\\"psn_name\\\":\\\"张含卓 \\\",\\\"email\\\":\\\"\\\"}
   * 
   * @param dataMap
   */
  public static void fillIsOwnerByPubDetailVO(Map<String, Object> dataMap, PubDetailVO pubDetailVO) {

    List<PubMemberDTO> pubMemberList = pubDetailVO.getMembers();
    boolean flag = false;
    List<Map<String, String>> authorsList = new ArrayList<Map<String, String>>();
    if (pubMemberList != null && pubMemberList.size() > 0) {
      for (int j = 0; j < pubMemberList.size(); j++) {
        boolean owner = pubMemberList.get(j).isOwner();
        if (owner) {
          flag = true;
          break;
        }
      }
      for (int j = 0; j < pubMemberList.size(); j++) {
        Map<String, String> authorMap = new HashMap<String, String>();
        PubMemberDTO pubMember = pubMemberList.get(j);
        String psnName = pubMember.getName(); // 作者名
        String orgName = ""; // 作者机构
        String autoOrgName = "";
        // lamda表达式空指针异常
        if (pubMember.getInsNames() != null && pubMember.getInsNames().size() > 0) {
          autoOrgName =
              Optional.ofNullable(pubMember.getInsNames()).map(list -> list.get(0)).map(s -> s.getInsName()).orElse(""); // 作者机构
        }

        String email = pubMember.getEmail(); // 邮箱
        boolean isMessage = pubMember.isCommunicable(); // 第一通信
        // 作者
        boolean firstAuthor = pubMember.isFirstAuthor(); // 第一作者
        boolean isMime = pubMember.isOwner(); // 是否本人
        // 作者名
        authorMap.put("psn_name", psnName);

        if (StringUtils.isNotBlank(orgName)) {
          authorMap.put("org_name", orgName);
        } else {
          authorMap.put("org_name", autoOrgName);
        }
        authorMap.put("email", email);

        authorMap.put("is_message", isMessage ? "1" : "0");
        authorMap.put("first_author", firstAuthor ? "1" : "0");
        authorMap.put("is_mine", isMime ? "是" : "否");
        authorsList.add(authorMap);
      }
      dataMap.put("pub_member", authorsList);
    } else {
      dataMap.put("pub_member", authorsList);
    }
    dataMap.put("owner", flag ? "1" : "0");
  }


  /**
   * //互联互通需要 authors 字段 但是和前面的字段authors 冲突了先注释调-2018-09-19-ajb 填充是否是本人
   * {\\\"is_mine\\\":\\\"否\\\",\\\"is_message\\\":\\\"0
   * \\\",\\\"first_author\\\":\\\"1\\\",\\\"psn_name\\\":\\\"张含卓 \\\",\\\"email\\\":\\\"\\\"}
   * 
   * @param dataMap
   */
  public static void fillIsOwnerWithAuhtors(Map<String, Object> dataMap, PubDetailVO pubDetailVO) {

    List<PubMemberDTO> pubMemberList = pubDetailVO.getMembers();
    boolean flag = false;
    if (pubMemberList != null && pubMemberList.size() > 0) {
      for (int j = 0; j < pubMemberList.size(); j++) {
        boolean owner = pubMemberList.get(j).isOwner();
        if (owner) {
          flag = true;
          break;
        }
      }
      List<Map<String, String>> authorsList = new ArrayList<Map<String, String>>();
      for (int j = 0; j < pubMemberList.size(); j++) {
        Map<String, String> authorMap = new HashMap<String, String>();
        PubMemberDTO pubMember = pubMemberList.get(j);
        String psnName = pubMember.getName(); // 作者名
        String orgName = ""; // 作者机构
        String autoOrgName = "";
        // lamda表达式空指针异常
        if (pubMember.getInsNames() != null && pubMember.getInsNames().size() > 0) {
          autoOrgName =
              Optional.ofNullable(pubMember.getInsNames()).map(list -> list.get(0)).map(s -> s.getInsName()).orElse(""); // 作者机构
        }

        String email = pubMember.getEmail(); // 邮箱
        boolean isMessage = pubMember.isCommunicable(); // 第一通信
        // 作者
        boolean firstAuthor = pubMember.isFirstAuthor(); // 第一作者
        boolean isMime = pubMember.isOwner(); // 是否本人
        // 作者名
        authorMap.put("psn_name", escapeXMLStr(psnName));

        if (StringUtils.isNotBlank(orgName)) {
          authorMap.put("org_name", escapeXMLStr(orgName));
        } else {
          authorMap.put("org_name", escapeXMLStr(autoOrgName));
        }
        authorMap.put("email", email);

        authorMap.put("is_message", isMessage ? "1" : "0");
        authorMap.put("first_author", firstAuthor ? "1" : "0");
        authorMap.put("is_mine", isMime ? "是" : "否");
        authorsList.add(authorMap);
      }
      dataMap.put("authors", authorsList);
    }
    dataMap.put("owner", flag ? "1" : "0");
  }

  /**
   * sie提供出去取成果专用
   *
   * @param dataMap
   * @param pubDetailVO
   */
  public static void buildPubMember(Map<String, Object> dataMap, PubDetailVO pubDetailVO) {

    List<PubMemberDTO> pubMemberList = pubDetailVO.getMembers();
    List<Map<String, String>> authorsList = new ArrayList<Map<String, String>>();
    if (pubMemberList != null && pubMemberList.size() > 0) {
      for (int j = 0; j < pubMemberList.size(); j++) {
        Map<String, String> authorMap = new HashMap<String, String>();
        PubMemberDTO pubMember = pubMemberList.get(j);
        String psnName = pubMember.getName(); // 作者名
        String orgName = "";
        // lamda表达式空指针异常
        if (pubMember.getInsNames() != null && pubMember.getInsNames().size() > 0) {
          orgName =
              Optional.ofNullable(pubMember.getInsNames()).map(list -> list.get(0)).map(s -> s.getInsName()).orElse(""); // 作者机构
        }
        authorMap.put("seq_no", Integer.toString(j + 1));
        // 作者名
        authorMap.put("member_psn_name", StringUtils.isNotBlank(psnName) ? psnName.trim() : "");
        authorMap.put("ins_name", StringUtils.isNotBlank(orgName) ? orgName : "");
        authorMap.put("owner", pubMember.isOwner() ? "1":"0");
        authorsList.add(authorMap);
      }
    }
    dataMap.put("pub_members", authorsList);
  }

  /**
   * 得到成果收录情况 这个返回来所有的 收录情况
   * 
   * @return
   */
  public static void fillListInfo(PubDetailVO pubDetailVO, Map<String, Object> dataMap) {
    List<PubSituationDTO> sitations = pubDetailVO.getSituations();
    String listInfoListStr = "";
    String eiid = "";
    String isiid = "";
    if (sitations != null && sitations.size() > 0) {
      for (PubSituationDTO sitation : sitations) {
        if (sitation.isSitStatus()) {
          listInfoListStr += sitation.getLibraryName() + ",";
        }
        if (sitation.getLibraryName().toLowerCase().equals("ei")) {
          eiid = sitation.getSrcId();
        } else if (sitation.getLibraryName().toLowerCase().equals("scie")
            || sitation.getLibraryName().toLowerCase().equals("ssci")
            || sitation.getLibraryName().toLowerCase().equals("istp")) {
          if (StringUtils.isNotBlank(sitation.getSrcId())) {
            isiid = sitation.getSrcId();
          }
        }
      }
      if (StringUtils.isNotBlank(listInfoListStr)) {
        listInfoListStr = listInfoListStr.substring(0, listInfoListStr.length() - 1).toLowerCase();
      }
    }
    dataMap.put("list_info", listInfoListStr);
  }

  /**
   * 得到成果收录情况 添加 srcId 这个返回来所有的 收录情况
   *
   * @return
   */
  public static void fillListInfoAddSrcId(PubDetailVO pubDetailVO, Map<String, Object> dataMap) {
    List<PubSituationDTO> sitations = pubDetailVO.getSituations();
    String listInfoListStr = "";
    String eiid = "";
    String isiid = "";
    if (sitations != null && sitations.size() > 0) {
      for (PubSituationDTO sitation : sitations) {
        if (sitation.isSitStatus()) {
          listInfoListStr += sitation.getLibraryName() + ",";
        }
        if (sitation.getLibraryName().toLowerCase().equals("ei")) {
          eiid = sitation.getSrcId();
        } else if (sitation.getLibraryName().toLowerCase().equals("scie")
            || sitation.getLibraryName().toLowerCase().equals("ssci")
            || sitation.getLibraryName().toLowerCase().equals("istp")) {
          if (StringUtils.isNotBlank(sitation.getSrcId())) {
            isiid = sitation.getSrcId();
          }
        }
      }
      if (StringUtils.isNotBlank(listInfoListStr)) {
        listInfoListStr = listInfoListStr.substring(0, listInfoListStr.length() - 1).toLowerCase();
      }
    }
    dataMap.put("list_info", listInfoListStr);
    dataMap.put("eiid", eiid);
    dataMap.put("isiid", isiid);
  }

  /**
   * 得到成果收录情况 老的互联互通 这个返回来所有的 收录情况
   *
   * @return
   */
  public static void fillListInfoForUnion(PubDetailVO pubDetailVO, Map<String, Object> dataMap) {
    List<PubSituationDTO> sitations = pubDetailVO.getSituations();
    String listInfoListStr = "";
    if (sitations != null && sitations.size() > 0) {
      for (PubSituationDTO sitation : sitations) {
        switch (sitation.getLibraryName()) {
          case "EI":
            dataMap.put("list_ei", sitation.isSitStatus() ? "1" : "0");
            dataMap.put("list_ei_source", sitation.isSitOriginStatus() ? "1" : "0");
            break;
          case "SCIE":
            dataMap.put("list_sci", sitation.isSitStatus() ? "1" : "0");
            dataMap.put("list_sci_source", sitation.isSitOriginStatus() ? "1" : "0");
            break;
          case "SSCI":
            dataMap.put("list_ssci", sitation.isSitStatus() ? "1" : "0");
            dataMap.put("list_ssci_source", sitation.isSitOriginStatus() ? "1" : "0");
            break;
          case "ISTP":
            dataMap.put("list_istp", sitation.isSitStatus() ? "1" : "0");
            dataMap.put("list_istp_source", sitation.isSitOriginStatus() ? "1" : "0");
            break;
          default:
            break;
        }

      }
    } else {
      dataMap.put("list_ei", "0");
      dataMap.put("list_ei_source", "0");
      dataMap.put("list_sci", "0");
      dataMap.put("list_sci_source", "0");
      dataMap.put("list_ssci", "0");
      dataMap.put("list_ssci_source", "0");
      dataMap.put("list_istp", "0");
      dataMap.put("list_istp_source", "0");
    }
  }

  /**
   * 返回收录情况
   * 
   * @param pubDetailVO
   * @return
   */
  public static String fillListInfo(PubDetailVO pubDetailVO) {
    List<PubSituationDTO> sitations = pubDetailVO.getSituations();
    String listInfoListStr = "";
    if (sitations != null && sitations.size() > 0) {
      for (PubSituationDTO sitation : sitations) {
        if (sitation.isSitStatus()) {
          listInfoListStr += sitation.getLibraryName() + ",";
        }
      }
      if (StringUtils.isNotBlank(listInfoListStr)) {
        listInfoListStr = listInfoListStr.substring(0, listInfoListStr.length() - 1).toLowerCase();
      }
    }
    return listInfoListStr;
  }

  /**
   * 判断是否认证
   * 
   * @param dataMap
   */
  public static void isOrNotAuthenticated(Map<String, Object> dataMap, PubDetailVO pubDetailVO) {
    Integer dbId = pubDetailVO.getSourceDbId();
    String authenticated = "0";
    if (dbId != null) {
      if (dbId > 0) {
        authenticated = "1";
      }
    }
    dataMap.put("authenticated", authenticated);

  }

  /**
   * 填充创建时间 统一：yyyy/MM/dd HH:mm:ss
   * 
   * @param pubDetailVO
   */
  public static void fillCreateTime(PubDetailVO pubDetailVO, Map<String, Object> dataMap) {
    if (pubDetailVO.getGmtCreate() != null) {
      SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      String format = sf.format(pubDetailVO.getGmtCreate());
      dataMap.put("create_date", format);
    } else {
      dataMap.put("create_date", "");
    }
  }

  /**
   * 获取成果更新时间 统一，yyyy/MM/dd HH:mm:ss
   * 
   * @param dataMap
   */
  public static void buildPubUpdateTime(PubDetailVO pubDetailVO, Map<String, Object> dataMap) {
    Date updateDate = pubDetailVO.getGmtModified();
    String updateDateStr = "";
    if (updateDate != null) {
      updateDateStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(updateDate);
    }
    dataMap.put("pub_update_time", updateDateStr);
  }

  /**
   * xml转译
   *
   * @param s
   * @return
   */
  public static String escapeXMLStr(String s) {
    if (StringUtils.isBlank(s)) {
      return "";
    }
    // 2018-12-27-反转义
    // s = StringEscapeUtils.unescapeHtml4(s);
    // s = StringEscapeUtils.escapeXml11(s);
    return s;
  }

  /**
   * wos检索号 取 istp,sci,ssci的source_id sci == SCIE
   *
   * @param dataMap
   * @param pubDetailVO
   */
  public static void buildWos(Map dataMap, PubDetailVO pubDetailVO) {
    List<PubSituationDTO> situations = pubDetailVO.getSituations();
    String wos = "";
    if (CollectionUtils.isNotEmpty(situations)) {
      for (PubSituationDTO s : situations) {
        if (StringUtils.isNotBlank(s.getSrcId())
            && (s.getLibraryName().equalsIgnoreCase("istp") || s.getLibraryName().equalsIgnoreCase("sci")
                || s.getLibraryName().equalsIgnoreCase("ssci") || s.getLibraryName().equalsIgnoreCase("scie"))) {
          wos = s.getSrcId();
          break;
        }
      }
    }
    dataMap.put("wos", wos);
  }

  /**
   * 处理作者信息
   *
   * @param authorNames
   */
  public static String  dealAuthorNames(String authorNames) {
    authorNames = StringEscapeUtils.unescapeHtml4(authorNames);
    //%26apos;  SCM-23390
    authorNames = authorNames.replaceAll("%26apos;","'");
    authorNames = authorNames.trim().replaceAll("；",";").replaceAll("，",",");
    authorNames = XmlUtil.beforeCompareAurhorName(authorNames);
    authorNames = authorNames.replaceAll("\\[.*?\\]","");
    //处理逗号的情况下
    authorNames = dealDotAuthornames(authorNames);
    return authorNames;
  }

  /**
   * 2007/6/21或2007/6
   * @param dateStr
   * @return
   */
  public static String dealDateFormat(String dateStr){
    if(StringUtils.isBlank(dateStr)){
      return "";
    }
    String result ="";
    String[] split = dateStr.split("[-/]");
    switch (split.length){
      case 1 : result = split[0];
                break;
      case 2 : result = split[0];
        split[1] = split[1].replaceAll("^0","");
        result = split[0]+"/"+split[1];
        break;
      default :
        split[1] = split[1].replaceAll("^0","");
        split[2] = split[2].replaceAll("^0","");
        result = split[0]+"/"+split[1]+"/"+split[2];
    }
    return result ;
  }

  /**
   * 没有分号的情况.
   * 处理逗号的情况下
   *
   * @param an
   * @return
   */
  public static String  dealDotAuthornames(String an) {
    if(com.smate.core.base.utils.string.StringUtils.isBlank(an) || an.contains(";")){
      return an ;
    }
    StringBuffer s = new StringBuffer();
    // 如果全是中文名字 就把逗号替换成分号
    if (ServiceUtil.isChineseStr(
        XmlUtil.getCleanAuthorName5(an).replaceAll("&", " ").replaceAll("\\s{2,}", " ").replaceAll(",", ""))) {
      s.append(an.replaceAll(",", ";"));
      return s.toString();
    }
    String[] ana = an.split(",");
    if (ana.length > 0) {
      // 如果每个名字中间存在空格那么直接把都好替换成分号
      boolean blank = false;
      for (int i = 0; i < ana.length; i++) {
        if (ana[i].trim().indexOf(" ") > 0) {
          blank = true;
          break;
        }
      }
      if (blank) {
        s.append(an.replaceAll(",", ";"));
        return s.toString();
      }
      for (int i = 0; i < ana.length; i++) {
        if ((i + 1) % 2 != 0) {
          s.append(ana[i]);
          if (i != ana.length - 1) {
            s.append(",");
          }
        } else {
          s.append(ana[i]);
          if (i != ana.length - 1) {
            s.append(";");
          }
        }
      }
    }
    return s.toString();
  }


  public static boolean compareName(Object name , Object compareName){
    if(name == null || compareName == null){
      return false ;
    }
    if(StringUtils.isBlank(name.toString()) || StringUtils.isBlank(compareName.toString())){
      return false ;
    }
    String nameS = XmlUtil.getCleanAuthorName6(name.toString());
    String nameC = XmlUtil.getCleanAuthorName6(compareName.toString());
    return nameS.equals(nameC) ;
  }

  /**
   * 对成果数据 反转义
   * @param list
   * @return
   */
  public static List<Map<String, Object>>  unescapeList(List<Map<String, Object>> list){
    if(CollectionUtils.isEmpty(list)) return  list;
    List<Map<String, Object>> resList = new ArrayList<>();
    for(Map map  : list){
      resList.add(unescapeMap(map));
    }
    return  resList;
  }
  public static Map unescapeMap(Map map){
    if(map == null || map.size() == 0) return map ;
    Map resMap = new HashMap();
    map.forEach((key,val) ->{
      if(val instanceof  String){
        resMap.put(key,StringEscapeUtils.unescapeHtml4(((String) val)));
      }else if(val instanceof  List){
        List<Map<String ,Object>> list = (List)val;
        List<Map<String, Object>> maps = unescapeList(list);
        resMap.put(key,maps);
      }else{
        resMap.put(key,val);
      }
    });
    return resMap ;
  }
  public static void main(String[] args) throws Exception {
    String name = "*,._-   (#) (*)<b></b>爱(#)   金币   NNN" ;
    String name2 = "*,._-  爱   金   币nnn (#)    (*)<  b></b>";
     compareName(name , name2);
    String  raw = "11";
    String  raw2 = "22";
    String str = String.format("%s + %s", raw ,raw2 );
    System.out.println(str);
    NumberFormat format =  NumberFormat.getIntegerInstance();
    //format.setParseIntegerOnly(true);
    format.setMaximumIntegerDigits(4);
    String resutl = format.format(11999.222);
    System.out.println(resutl);
    System.out.println(format.parse("11.22"));
  }

}
