package com.smate.web.psn.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.web.psn.model.pub.PubMember;

/**
 * ISIS获取成果XML的，成果作者格式化工具类
 * 
 * @author ajb
 * 
 */
public class PubMemberNameUtils {

  /**
   * 获取MLA格式的成果作者名
   * 
   * @param pubMemberList
   * @return
   */
  public static String getMLAname(List<PubMember> pubMemberList) {
    if (CollectionUtils.isEmpty(pubMemberList)) {
      return null;
    }
    StringBuilder pubName = new StringBuilder();
    // 取排第一的那个作者
    pubName.append(pubMemberList.get(0).getName());
    if (pubMemberList.size() > 1) {
      pubName.append(", et al");
    }
    return pubName.toString();
  }

  /**
   * 获取APA格式的成果作者名
   * 
   * @param pubMemberList
   * @return
   */
  public static String getAPAname(List<PubMember> pubMemberList) {
    if (CollectionUtils.isEmpty(pubMemberList)) {
      return null;
    }
    StringBuilder pubName = new StringBuilder();
    if (pubMemberList.size() <= 7) {
      if (pubMemberList.size() == 1) {
        pubName.append(pubMemberList.get(0).getName());
      } else {
        int length = pubMemberList.size();
        for (int i = 0; i < length - 1; i++) {
          pubName.append(pubMemberList.get(i).getName() + ", ");
        }
        pubName.append(" & ");
        pubName.append(pubMemberList.get(length - 1).getName());
      }
    } else {
      for (int i = 0; i < 6; i++) {
        pubName.append(pubMemberList.get(i).getName() + ", ");
      }
      pubName.append("…& ");
      pubName.append(pubMemberList.get(pubMemberList.size() - 1).getName());
    }

    return pubName.toString();
  }

  /**
   * 获取Chicago格式的成果作者名
   * 
   * @param pubMemberList
   * @return
   */
  public static String getChicagoName(List<PubMember> pubMemberList) {
    if (CollectionUtils.isEmpty(pubMemberList)) {
      return null;
    }
    StringBuilder pubName = new StringBuilder();
    if (pubMemberList.size() <= 7) {
      if (pubMemberList.size() == 1) {
        pubName.append(pubMemberList.get(0).getName());
      } else {
        int length = pubMemberList.size();
        for (int i = 0; i < length - 1; i++) {
          pubName.append(pubMemberList.get(i).getName() + ", ");
        }
        pubName.append(" and ");
        pubName.append(pubMemberList.get(length - 1).getName());
      }
    } else {
      for (int i = 0; i < 6; i++) {
        pubName.append(pubMemberList.get(i).getName() + ", ");
      }
      pubName.append(pubMemberList.get(6).getName());
      pubName.append(" et al ");
    }

    return pubName.toString();
  }

  /**
   * 获取GBT7714-2015格式的成果作者名
   * 
   * @author lhd
   * @param pubMemberList
   * @return
   */
  public static String getGBTname(List<PubMember> pubMemberList) {
    if (CollectionUtils.isEmpty(pubMemberList)) {
      return "";
    }
    StringBuilder pubName = new StringBuilder();
    if (pubMemberList.size() <= 3) {// 小于等于3
      if (pubMemberList.size() == 1) {// 等于1
        String oldName = pubMemberList.get(0).getName();
        String newName = "";
        newName = removeComma(oldName, newName);// 把作者名中的逗号去掉
        pubName.append(newName);
      } else {// 大于1小于等于3
        int length = pubMemberList.size();
        for (int i = 0; i < length; i++) {
          String oldName = pubMemberList.get(i).getName();
          String newName = "";
          newName = removeComma(oldName, newName);// 把作者名中的逗号去掉
          pubName.append(newName + ", ");
        }
        // 删除末尾的", "
        pubName.delete(pubName.length() - 2, pubName.length());
      }
      // 小于等于3的结尾
      String lastChar = pubName.substring(pubName.length() - 1);
      if (".".equals(lastChar)) {
        pubName.append(" ");
      } else {
        pubName.append(". ");
      }
    } else {// 大于3
      for (int i = 0; i < 3; i++) {
        String oldName = pubMemberList.get(i).getName();
        String newName = "";
        newName = removeComma(oldName, newName);// 把作者名中的逗号去掉
        pubName.append(newName + ", ");
      }
      // 判断中英文,中文用"等. ",英文用"el al. "
      if (isContainChinese(pubName.toString())) {
        pubName.append("等. ");
      } else {
        pubName.append("et al. ");
      }
    }
    return pubName.toString();
  }

  /**
   * GBT7714-2015格式-专利-把作者名中的逗号去掉
   * 
   * @author lhd
   * @param oldName
   * @param newName
   * @return
   */
  private static String removeComma(String oldName, String newName) {
    if (StringUtils.isNotBlank(oldName)) {
      if (oldName.contains(",")) {
        newName = oldName.replace(",", "");
      } else {
        newName = oldName;
      }
    }
    return newName;
  }

  /**
   * 判断字符串是否包含中文
   * 
   * @param str
   * @return
   */
  private static boolean isContainChinese(String str) {
    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    Matcher m = p.matcher(str);
    if (m.find()) {
      return true;
    }
    return false;
  }

  /**
   * 获取成果作者格式
   * 
   * @param pubMemberList
   * @param pubType
   * @param currentPsnName
   * @return
   */
  public static String getSNSPubMemberName(List<PubMember> PubMemberList, Integer pubType, String currentPsnName) {
    if (CollectionUtils.isEmpty(PubMemberList)) {
      return "";
    }
    switch (pubType.intValue()) {
      case PublicationTypeEnum.AWARD:
        return getSNSAwardPubMemberName(PubMemberList, currentPsnName);
      case PublicationTypeEnum.CONFERENCE_PAPER:
        return getSNSArticlePubMemberName(PubMemberList, currentPsnName);
      case PublicationTypeEnum.JOURNAL_ARTICLE:
        return getSNSArticlePubMemberName(PubMemberList, currentPsnName);
      case PublicationTypeEnum.BOOK:
        return getSNSBookPubMemberName(PubMemberList, currentPsnName);
      case PublicationTypeEnum.PATENT:
        return getSNSPatentPubMemberName(PubMemberList, currentPsnName);
      default:
        return getSNSDefaultPubMemberName(PubMemberList, currentPsnName);
    }
  }

  /**
   * 奖励的作者格式
   * 
   * @param pubMemberList
   * @param currentPsnName
   * @return
   */
  public static String getSNSAwardPubMemberName(List<PubMember> PubMemberList, String currentPsnName) {
    StringBuffer author = new StringBuffer();
    // 先用名字匹配
    if (StringUtils.isNotBlank(currentPsnName)) {
      for (PubMember pubMember : PubMemberList) {
        if (currentPsnName.equals(pubMember.getName())) {
          author.append("<strong>" + currentPsnName + "</strong>");
          if (pubMember.getFirstAuthor() != null && pubMember.getFirstAuthor() == 1) {
            author.append("<sup><sup>(#)</sup></sup>");
          }
          if (pubMember.getAuthorPos() != null && pubMember.getAuthorPos() == 1) {
            author.append("<sup><sup>(*)</sup></sup>");
          }
          author.append("(" + pubMember.getSeqNo() + "/" + PubMemberList.size() + ")");
          return author.toString();
        }
      }
    }
    // 找不到的话，再用成果本人
    for (PubMember pubMember : PubMemberList) {
      if (pubMember.getOwner() == 1) {
        author.append("<strong>" + pubMember.getName() + "</strong>");
        if (pubMember.getFirstAuthor() != null && pubMember.getFirstAuthor() == 1) {
          author.append("<sup><sup>(#)</sup></sup>");
        }
        if (pubMember.getAuthorPos() != null && pubMember.getAuthorPos() == 1) {
          author.append("<sup><sup>(*)</sup></sup>");
        }
        author.append("(" + pubMember.getSeqNo() + "/" + PubMemberList.size() + ")");
        return author.toString();
      }
    }
    // 还是找不到就用本人（没有排名）
    return currentPsnName;
  }

  /**
   * 获取会议论文和期刊论文的作者格式
   * 
   * @param pubMemberList
   * @param currentPsnName
   * @return
   */
  public static String getSNSArticlePubMemberName(List<PubMember> pubMemberList, String currentPsnName) {
    StringBuilder autors = new StringBuilder();
    int i = 1;
    for (PubMember pubMember : pubMemberList) {
      if (StringUtils.isNotBlank(currentPsnName) && currentPsnName.equals(pubMember.getName())
          || pubMember.getOwner() == 1) {
        autors.append("<strong>");
        autors.append(pubMember.getName());
        autors.append("</strong>");
      } else {
        autors.append(pubMember.getName());
      }
      if (pubMember.getFirstAuthor() != null && pubMember.getFirstAuthor() == 1) {
        autors.append("<sup><sup>(#)</sup></sup>");
      }
      if (pubMember.getAuthorPos() != null && pubMember.getAuthorPos() == 1) {
        autors.append("<sup><sup>(*)</sup></sup>");
      }
      if (i < pubMemberList.size()) {
        autors.append("; ");
      }
      i++;
    }
    return autors.toString();
  }

  /**
   * 获取专利的作者格式
   * 
   * @param pubMemberList
   * @return
   */
  public static String getSNSPatentPubMemberName(List<PubMember> pubMemberList, String currentPsnName) {
    StringBuilder autors = new StringBuilder();
    int i = 1;
    for (PubMember pubMember : pubMemberList) {
      if (StringUtils.isNotBlank(currentPsnName) && currentPsnName.equals(pubMember.getName())
          || pubMember.getOwner() == 1) {
        autors.append("<strong>");
        autors.append(pubMember.getName());
        autors.append("</strong>");
      } else {
        autors.append(pubMember.getName());
      }
      if (pubMember.getFirstAuthor() != null && pubMember.getFirstAuthor() == 1) {
        autors.append("<sup><sup>(#)</sup></sup>");
      }
      if (pubMember.getAuthorPos() != null && pubMember.getAuthorPos() == 1) {
        autors.append("<sup><sup>(*)</sup></sup>");
      }
      if (i < pubMemberList.size()) {
        autors.append("; ");
      }
      i++;
    }
    return autors.toString();
  }

  /**
   * 获取书籍的作者格式
   * 
   * @param pubMemberList
   * @return
   */
  public static String getSNSBookPubMemberName(List<PubMember> pubMemberList, String currentPsnName) {
    StringBuilder autors = new StringBuilder();
    int i = 1;
    for (PubMember pubMember : pubMemberList) {
      if (StringUtils.isNotBlank(currentPsnName) && currentPsnName.equals(pubMember.getName())
          || pubMember.getOwner() == 1) {
        autors.append("<strong>");
        autors.append(pubMember.getName());
        autors.append("</strong>");
      } else {
        autors.append(pubMember.getName());
      }
      if (pubMember.getFirstAuthor() != null && pubMember.getFirstAuthor() == 1) {
        autors.append("<sup><sup>(#)</sup></sup>");
      }
      if (pubMember.getAuthorPos() != null && pubMember.getAuthorPos() == 1) {
        autors.append("<sup><sup>(*)</sup></sup>");
      }
      if (i < pubMemberList.size()) {
        autors.append("; ");
      }
      i++;
    }
    return autors.toString();
  }

  /**
   * 获取默认的成果格式
   * 
   * @param pubMemberList
   * @param currentPsnName
   * @return
   */
  public static String getSNSDefaultPubMemberName(List<PubMember> pubMemberList, String currentPsnName) {
    StringBuilder autors = new StringBuilder();
    int i = 1;
    for (PubMember pubMember : pubMemberList) {
      if (StringUtils.isNotBlank(currentPsnName) && currentPsnName.equals(pubMember.getName())
          || pubMember.getOwner() == 1) {
        autors.append("<strong>");
        autors.append(pubMember.getName());
        autors.append("</strong>");
      } else {
        autors.append(pubMember.getName());
      }
      if (i < pubMemberList.size()) {
        autors.append("; ");
      }
      i++;
    }
    return autors.toString();
  }

  /**
   * 获取成果作者，本人不加粗显示
   * 
   * @param pubMemberList
   * @param currentPsnName
   * @return
   */
  public static String getSNSPubMemberNameList(List<PubMemberPO> pubMemberList, String currentPsnName) {
    StringBuilder autors = new StringBuilder();
    int i = 1;
    for (PubMemberPO pubMember : pubMemberList) {
      if (StringUtils.isNotBlank(currentPsnName) && currentPsnName.equals(pubMember.getName())
          || pubMember.getOwner() == 1) {
        autors.append("<strong>");
        autors.append(pubMember.getName());
        autors.append("</strong>");
      } else {
        autors.append(pubMember.getName());
      }
      if (pubMember.getFirstAuthor() != null && pubMember.getFirstAuthor() == 1) {
        autors.append("<sup><sup>(#)</sup></sup>");
      }
      if (pubMember.getCommunicable() != null && pubMember.getCommunicable() == 1) {
        autors.append("<sup><sup>(*)</sup></sup>");
      }
      if (i < pubMemberList.size()) {
        autors.append("; ");
      }
      i++;
    }
    return autors.toString();
  }

}
