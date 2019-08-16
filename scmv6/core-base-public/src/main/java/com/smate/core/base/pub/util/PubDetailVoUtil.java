package com.smate.core.base.pub.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.OtherInfoDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.SoftwareCopyrightDTO;
import com.smate.web.v8pub.dto.StandardInfoDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;

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
        logger.error("json字符串，不能解析成PubDetailVoUtil.PubDetailVO");
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
    if (StringUtils.isBlank(date)) {
      return "";
    }
    date = StringUtils.trimToEmpty(date);
    String[] split = date.split("[-/]");
    return split != null && split.length > 0 ? split[0] : "";
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
    date = StringUtils.trimToEmpty(date);
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
    date = StringUtils.trimToEmpty(date);
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
   * @param pubElement
   */
  /*
   * public static void fillIsOwnerByPubDetailVO(Map<String, Object> dataMap, PubDetailVO pubDetailVO)
   * {
   * 
   * List<PubMemberDTO> pubMemberList = pubDetailVO.getMembers(); boolean flag = false; if
   * (pubMemberList != null && pubMemberList.size() > 0) { for (int j = 0; j < pubMemberList.size();
   * j++) { boolean owner = pubMemberList.get(j).isOwner(); if (owner) { flag = true; break; } }
   * List<Map<String, String>> authorsList = new ArrayList<Map<String, String>>(); for (int j = 0; j <
   * pubMemberList.size(); j++) { Map<String, String> authorMap = new HashMap<String, String>();
   * PubMemberDTO pubMember = pubMemberList.get(j); String psnName = pubMember.getName(); // 作者名
   * String orgName = ""; // 作者机构 String autoOrgName = pubMember.getInsName(); // 作者机构 String email =
   * pubMember.getEmail(); // 邮箱 int isMessage = pubMember.getCommunicable() == null ? 0 :
   * pubMember.getCommunicable(); // 第一通信 // 作者 int firstAuthor = pubMember.getFirstAuthor() == null ?
   * 0 : pubMember.getFirstAuthor(); // 第一作者 int isMime = pubMember.getOwner() == null ? 0 :
   * pubMember.getOwner(); // 是否本人 // 作者名 authorMap.put("psn_name", psnName);
   * 
   * if (StringUtils.isNotBlank(orgName)) { authorMap.put("org_name", orgName); } else {
   * authorMap.put("org_name", autoOrgName); } authorMap.put("email", email);
   * 
   * authorMap.put("is_message", isMessage == 1 ? "1" : "0"); authorMap.put("first_author",
   * firstAuthor == 1 ? "1" : "0"); authorMap.put("is_mine", isMime == 1 ? "是" : "否");
   * authorsList.add(authorMap); } dataMap.put("pub_member", authorsList); } dataMap.put("owner", flag
   * ? "1" : "0"); }
   */

  /**
   * 得到成果收录情况
   * 
   * @return
   */
  /*
   * public static void fillListInfo(PubDetailVO pubDetailVO, Map<String, Object> dataMap) {
   * List<Sitation> sitations = pubDetailVO.getSitations(); String listInfoListStr = ""; if (sitations
   * != null && sitations.size() > 0) { for (Sitation sitation : sitations) { if (sitation.included) {
   * listInfoListStr += sitation.getLibrary() + ","; } } if (StringUtils.isNotBlank(listInfoListStr))
   * { listInfoListStr = listInfoListStr.substring(0, listInfoListStr.length() - 1).toLowerCase(); } }
   * dataMap.put("list_info", listInfoListStr); }
   */

  /**
   * 返回收录情况
   * 
   * @param pubDetailVO
   * @return
   */
  /*
   * public static String fillListInfo(PubDetailVO pubDetailVO) { List<Sitation> sitations =
   * pubDetailVO.getSitations(); String listInfoListStr = ""; if (sitations != null &&
   * sitations.size() > 0) { for (Sitation sitation : sitations) { if (sitation.included) {
   * listInfoListStr += sitation.getLibrary() + ","; } } if (StringUtils.isNotBlank(listInfoListStr))
   * { listInfoListStr = listInfoListStr.substring(0, listInfoListStr.length() - 1).toLowerCase(); } }
   * return listInfoListStr; }
   */

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
   * 填充创建时间
   * 
   * @param pubElement
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
   * 获取成果更新时间
   * 
   * @param dataMap
   * @param pubId
   */
  public static void buildPubUpdateTime(PubDetailVO pubDetailVO, Map<String, Object> dataMap) {
    Date updateDate = pubDetailVO.getGmtModified();
    String updateDateStr = "";
    if (updateDate != null) {
      updateDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateDate);
    }
    dataMap.put("pub_update_time", updateDateStr);
  }

  public static void main(String[] args) {
    String str = "2015 -55 /5";
    str = str.replaceAll(" ", "");
    try {
      URLEncoder.encode("psn_id=1000000003147&psn_name=江斌 艾&ins_name=3   +<设置HH>{}&ldq&nature_id=1&domain=1231232131.scholarmate.com&url=&eco_code=E47,E48&csei_code=,&description=3<设置HH>{}“）+++++（”*&*&*》《3<设置HH>{}“）（”*&*&*》《3<设置HH>{}“）（”*&*&*》《3<设置HH>{}“）（”*&*&*》《3<设置HH>{}“）（”*&*&*》《3<设置HH>{}“）（”*&*&*》《&country_id=156&prv_id=&cy_id=", "UTF-8");

      System.out.println( URLEncoder.encode("艾江 斌+  +","utf-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

}
