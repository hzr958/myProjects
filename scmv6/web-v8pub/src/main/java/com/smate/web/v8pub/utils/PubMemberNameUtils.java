package com.smate.web.v8pub.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.web.v8pub.po.pdwh.PdwhPubMemberPO;

/**
 * ISIS获取成果XML的，成果作者格式化工具类
 * 
 * @author ajb
 * 
 */
public class PubMemberNameUtils {



  /**
   * 所有处理英文名字的公共的判断方法 如Mclin Steve P 则取Maclin, S. P. zhang yi-shan/zhang yishan 则zhang,Y.S 中文名字原样输出
   * 缩写名称
   * 
   * @param name
   * @return
   */
  public static String getName(String name) {
    StringBuilder nameDescs = new StringBuilder();
    if (!isContainChinese(name)) {
      name = name.trim().replace(" ", ",").replaceAll(",+", ",");
      String nameDesc[] = name.split(",|-| ");
      if (nameDesc.length > 1) {
        for (int i = 0; i < nameDesc.length; i++) {
          if (i == 0) {
            nameDescs.append(nameDesc[i]).append(", ");
          } else {
            nameDescs.append(nameDesc[i].substring(0, 1).toUpperCase()).append(".");
          }
        }
        return nameDescs.toString();
      } else {
        return name;
      }
    }
    return name;
  }

  /**
   * 获取不缩写的作者名称用逗号分隔
   * 
   * @param name
   * @return
   */
  public static String getFullName(String name) {
    if (!isContainChinese(name)) {
      name = name.trim().replace(" ", ",").replaceAll(",+", ", ");
    }
    return name;
  }

  /**
   * 获取MLA格式的基准库成果作者名
   * 
   * @param auPosMemberList
   * @return
   */
  public static String getPdwhMLAname(List<PdwhPubMemberPO> auPosMemberList) {
    if (CollectionUtils.isEmpty(auPosMemberList)) {
      return null;
    }
    StringBuilder pubName = new StringBuilder();
    String name = auPosMemberList.get(0).getName();
    // 取排第一的那个作者 如果英文名字里面已经有逗号防止出现两个
    pubName.append(name);
    // 判断中英文,中文用"等. ",英文用"el al. " 且名字数量大于1
    if (auPosMemberList.size() > 1) {
      if (isContainChinese(pubName.toString())) {
        pubName.append(", 等");
      } else {
        pubName.append(", et al");
      }
    }

    return pubName.toString();
  }

  /**
   * 获取MLA格式的成果作者名
   * 
   * @param auPosMemberList
   * @return
   */
  public static String getMLAname(List<PubMemberPO> pubMemberList) {
    if (CollectionUtils.isEmpty(pubMemberList)) {
      return null;
    }
    StringBuilder pubName = new StringBuilder();
    String name = pubMemberList.get(0).getName();
    // 取排第一的那个作者 如果英文名字里面已经有逗号防止出现两个
    pubName.append(getFullName(name));
    // 判断中英文,中文用"等. ",英文用"el al. " 且名字数量大于1
    if (pubMemberList.size() > 1) {
      if (isContainChinese(pubName.toString())) {
        pubName.append(", 等");
      } else {
        pubName.append(", et al");
      }
    }
    return pubName.toString();
  }

  /**
   * 获取APA格式的基准库成果作者名
   * 
   * @param pubMemberList
   * @return
   */
  public static String getPdwhAPAname(List<PdwhPubMemberPO> pubMemberList) {
    if (CollectionUtils.isEmpty(pubMemberList)) {
      return null;
    }
    StringBuilder pubName = new StringBuilder();
    int length = pubMemberList.size();
    for (int i = 0; i < length; i++) {
      String name = getName(pubMemberList.get(i).getName());
      if (i > 0 && i == length - 1) {
        pubName.append(" & ").append(name);
      } else if (length == 1) {
        pubName.append(name);
      } else {
        pubName.append(name).append(", ");
      }
      if (length > 7 && i == 5) {
        pubName.append("…& ");
        pubName.append(getName(pubMemberList.get(length - 1).getName()));
        break;
      }
    }
    // 最后一个名字如果是有英文缩写则把最后那个.去掉 防止与句尾. 成为两个..
    String pubNamestr = pubName.toString();
    int lastIndex = pubNamestr.lastIndexOf(".");
    if (lastIndex > 0) {
      pubNamestr = pubNamestr.substring(0, lastIndex);
    }
    return pubName.toString();
  }



  /**
   * 获取APA格式的成果作者名
   * 
   * @param pubMemberList
   * @return
   */
  public static String getAPAname(List<PubMemberPO> pubMemberList) {
    if (CollectionUtils.isEmpty(pubMemberList)) {
      return null;
    }
    StringBuilder pubName = new StringBuilder();
    int length = pubMemberList.size();
    for (int i = 0; i < length; i++) {
      String name = getName(pubMemberList.get(i).getName());
      if (i > 0 && i == length - 1) {
        pubName.append(" & ").append(name);
      } else if (length == 1) {
        pubName.append(name);
      } else {
        pubName.append(name).append(", ");
      }
      if (length > 7 && i == 5) {
        pubName.append("…& ");
        pubName.append(getName(pubMemberList.get(length - 1).getName()));
        break;
      }
    }
    // 最后一个名字如果是有英文缩写则把最后那个.去掉 防止与句尾. 成为两个..
    String pubNamestr = pubName.toString();
    int lastIndex = pubNamestr.lastIndexOf(".");
    if (lastIndex > 0) {
      pubNamestr = pubNamestr.substring(0, lastIndex);
    }
    return pubNamestr;
  }



  /**
   * 获取Chicago格式的基准库成果作者名
   * 
   * @param pubMemberList
   * @return
   */
  public static String getPdwhChicagoName(List<PdwhPubMemberPO> pubMemberList) {
    if (CollectionUtils.isEmpty(pubMemberList)) {
      return null;
    }
    StringBuilder pubName = new StringBuilder();
    int length = pubMemberList.size();
    if (length <= 7) {
      for (int i = 0; i < length - 1; i++) {
        pubName.append(getFullName(pubMemberList.get(i).getName())).append(", ");
      }
      // 最后一个不加逗号
      if (length > 1) {
        pubName.append(" and ");
      }
      pubName.append(getFullName(pubMemberList.get(length - 1).getName()));
    } else {
      for (int i = 0; i < 6; i++) {
        pubName.append(getFullName(pubMemberList.get(i).getName())).append(", ");
      }
      pubName.append(getFullName(pubMemberList.get(6).getName()));
      pubName.append(" et al ");
    }
    return pubName.toString();
  }

  /**
   * 获取Chicago格式的成果作者名
   * 
   * @param pubMemberList
   * @return
   */
  public static String getChicagoName(List<PubMemberPO> pubMemberList) {
    if (CollectionUtils.isEmpty(pubMemberList)) {
      return null;
    }
    StringBuilder pubName = new StringBuilder();
    int length = pubMemberList.size();
    if (length <= 7) {
      for (int i = 0; i < length - 1; i++) {
        pubName.append(getFullName(pubMemberList.get(i).getName())).append(", ");
      }
      // 最后一个不加逗号
      if (length > 1) {
        pubName.append(" and ");
      }
      pubName.append(getFullName(pubMemberList.get(length - 1).getName()));
    } else {
      for (int i = 0; i < 6; i++) {
        pubName.append(getFullName(pubMemberList.get(i).getName())).append(", ");
      }
      pubName.append(getFullName(pubMemberList.get(6).getName()));
      pubName.append(" et al ");
    }
    return pubName.toString();
  }

  /**
   * 获取GBT7714-2015格式的基准库成果作者名
   * 
   * @author lhd
   * @param pubMemberList
   * @return
   */
  public static String getPdwhGBTname(List<PdwhPubMemberPO> pubMemberList) {
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
      if (StringUtils.isNotBlank(pubName)) {
        String lastChar = pubName.substring(pubName.length() - 1);
        if (".".equals(lastChar)) {
          pubName.append(" ");
        } else {
          pubName.append(". ");
        }
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
   * 获取GBT7714-2015格式的成果作者名
   * 
   * @author lhd
   * @param pubMemberList
   * @return
   */
  public static String getGBTname(List<PubMemberPO> pubMemberList) {
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
  public static String getSNSPubMemberName(List<PubMemberPO> PubMemberList, Integer pubType, String currentPsnName) {
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
  public static String getSNSAwardPubMemberName(List<PubMemberPO> PubMemberList, String currentPsnName) {
    // 先用名字匹配
    if (StringUtils.isNotBlank(currentPsnName)) {
      for (PubMemberPO PubMemberPO : PubMemberList) {
        if (currentPsnName.equals(PubMemberPO.getName())) {
          return "<strong>" + currentPsnName + "</strong>" + "(" + PubMemberPO.getSeqNo() + "/" + PubMemberList.size()
              + ")";
        }
      }
    }
    // 找不到的话，再用成果本人
    for (PubMemberPO PubMemberPO : PubMemberList) {
      if (PubMemberPO.getOwner() == 1) {
        return "<strong>" + PubMemberPO.getName() + "</strong>" + "(" + PubMemberPO.getSeqNo() + "/"
            + PubMemberList.size() + ")";
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
  public static String getSNSArticlePubMemberName(List<PubMemberPO> pubMemberList, String currentPsnName) {
    StringBuilder autors = new StringBuilder();
    int i = 1;
    for (PubMemberPO PubMemberPO : pubMemberList) {
      if (StringUtils.isNotBlank(currentPsnName) && currentPsnName.equals(PubMemberPO.getName())
          || PubMemberPO.getOwner() == 1) {
        autors.append("<strong>");
        autors.append(PubMemberPO.getName());
        autors.append("</strong>");
      } else {
        autors.append(PubMemberPO.getName());
      }
      if (PubMemberPO.getFirstAuthor() != null && PubMemberPO.getFirstAuthor() == 1) {
        autors.append("<sup><sup>(#)</sup></sup>");
      }
      if (PubMemberPO.getCommunicable() != null && PubMemberPO.getCommunicable() == 1) {
        autors.append("<sup>(*)</sup>");
      }
      if (i < pubMemberList.size()) {
        autors.append(", ");
      }
      i++;
    }
    return autors.toString();
  }

  /**
   * 获取会议论文和期刊论文的作者格式
   * 
   * @param pubMemberList
   * @return
   */
  /**
   * 获取默认的成果格式
   * 
   * @param pubMemberList
   * @param currentPsnName
   * @return
   */
  public static String getSNSDefaultPubMemberName(List<PubMemberPO> pubMemberList, String currentPsnName) {
    StringBuilder autors = new StringBuilder();
    int i = 1;
    for (PubMemberPO PubMemberPO : pubMemberList) {
      if (StringUtils.isNotBlank(currentPsnName) && currentPsnName.equals(PubMemberPO.getName())
          || PubMemberPO.getOwner() == 1) {
        autors.append("<strong>");
        autors.append(PubMemberPO.getName());
        autors.append("</strong>");
      } else {
        autors.append(PubMemberPO.getName());
      }
      if (i < pubMemberList.size()) {
        autors.append(", ");
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
    for (PubMemberPO PubMemberPO : pubMemberList) {
      autors.append(PubMemberPO.getName());
      if (i < pubMemberList.size()) {
        autors.append(", ");
      }
      i++;
    }
    return autors.toString();
  }

}
