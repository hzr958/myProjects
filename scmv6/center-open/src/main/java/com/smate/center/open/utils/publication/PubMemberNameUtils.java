package com.smate.center.open.utils.publication;


import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.smate.center.open.model.publication.NsfcPubMember;
import com.smate.core.base.pub.enums.PublicationTypeEnum;

/**
 * ISIS获取成果XML的，成果作者格式化工具类
 * 
 * @author ajb
 * 
 */
public class PubMemberNameUtils {

  /**
   * 获取成果作者格式
   * 
   * @param nsfcPubMemberList
   * @param pubType
   * @param currentPsnName
   * @return
   */
  public static String getPubMemberName(List<NsfcPubMember> nsfcPubMemberList, Integer pubType, String currentPsnName) {
    if (CollectionUtils.isEmpty(nsfcPubMemberList)) {
      return "";
    }
    switch (pubType.intValue()) {
      case PublicationTypeEnum.AWARD:
        return getAwardPubMemberName(nsfcPubMemberList, currentPsnName);
      case PublicationTypeEnum.CONFERENCE_PAPER:
        return getArticlePubMemberName(nsfcPubMemberList, currentPsnName);
      case PublicationTypeEnum.JOURNAL_ARTICLE:
        return getArticlePubMemberName(nsfcPubMemberList, currentPsnName);
      default:
        return getDefaultPubMemberName(nsfcPubMemberList, currentPsnName);
    }
  }

  /**
   * 奖励的作者格式
   * 
   * @param pubMemberList
   * @param currentPsnName
   * @return
   */
  public static String getAwardPubMemberName(List<NsfcPubMember> nsfcPubMemberList, String currentPsnName) {
    // 先用名字匹配
    if (StringUtils.isNotBlank(currentPsnName)) {
      for (NsfcPubMember nsfcPubMember : nsfcPubMemberList) {
        if (currentPsnName.equals(nsfcPubMember.getName())) {
          return "<b>" + currentPsnName + "</b>" + "(" + nsfcPubMember.getSeqNo() + "/" + nsfcPubMemberList.size()
              + ")";
        }
      }
    }
    // 找不到的话，再用成果本人
    for (NsfcPubMember nsfcPubMember : nsfcPubMemberList) {
      if (nsfcPubMember.getOwner() == 1) {
        return "<b>" + nsfcPubMember.getName() + "</b>" + "(" + nsfcPubMember.getSeqNo() + "/"
            + nsfcPubMemberList.size() + ")";
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
  public static String getArticlePubMemberName(List<NsfcPubMember> nsfcPubMemberList, String currentPsnName) {
    StringBuilder autors = new StringBuilder();
    int i = 1;
    for (NsfcPubMember nsfcPubMember : nsfcPubMemberList) {
      if (StringUtils.isNotBlank(currentPsnName) && currentPsnName.equals(nsfcPubMember.getName())
          || nsfcPubMember.getOwner() == 1) {
        autors.append("<b>");
        autors.append(nsfcPubMember.getName());
        autors.append("</b>");
      } else {
        autors.append(nsfcPubMember.getName());
      }
      if (nsfcPubMember.getFirstAuthor() != null && nsfcPubMember.getFirstAuthor() == 1) {
        autors.append("(#)");
      }
      if (nsfcPubMember.getAuthorPos() != null && nsfcPubMember.getAuthorPos() == 1) {
        autors.append("(*)");
      }
      if (i < nsfcPubMemberList.size()) {
        autors.append("，");
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
  public static String getDefaultPubMemberName(List<NsfcPubMember> nsfcPubMemberList, String currentPsnName) {
    StringBuilder autors = new StringBuilder();
    int i = 1;
    for (NsfcPubMember nsfcPubMember : nsfcPubMemberList) {
      if (StringUtils.isNotBlank(currentPsnName) && currentPsnName.equals(nsfcPubMember.getName())
          || nsfcPubMember.getOwner() == 1) {
        autors.append("<b>");
        autors.append(nsfcPubMember.getName());
        autors.append("</b>");
      } else {
        autors.append(nsfcPubMember.getName());
      }
      if (i < nsfcPubMemberList.size()) {
        autors.append("，");
      }
      i++;
    }
    return autors.toString();
  }


}
