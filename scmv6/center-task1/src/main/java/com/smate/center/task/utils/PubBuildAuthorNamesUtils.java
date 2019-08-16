package com.smate.center.task.utils;

import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.smate.center.task.utils.data.PubLocaleUtils;
import com.smate.center.task.v8pub.sns.po.PubMemberPO;
import com.smate.web.v8pub.dto.PubMemberDTO;

/**
 * 成果作者名构建工具
 * 
 * @author YJ
 *
 *         2018年8月2日
 */
public class PubBuildAuthorNamesUtils {

  /**
   * 重构成果作者
   * 
   * @param members
   * @return authorNames
   */
  public static String buildPdwhPubAuthorNames(List<String> authors) {
    StringBuffer authorNames = new StringBuffer();
    if (CollectionUtils.isEmpty(authors)) {
      return null;
    }
    for (String name : authors) {
      if (StringUtils.isBlank(name)) {
        continue;
      }
      if (PubLocaleUtils.getLocale(name).equals(Locale.CHINA)) {
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

  public static String buildSnsPubAuthorNames(List<PubMemberDTO> memberList) {
    StringBuffer authorNames = new StringBuffer();
    if (CollectionUtils.isEmpty(memberList)) {
      return null;
    }
    for (PubMemberDTO p : memberList) {
      String name = StringUtils.trimToEmpty(p.getName());
      if (StringUtils.isBlank(name)) {
        continue;
      }
      if (p.isCommunicable()) {
        name = "*" + name; // 是通讯作者，则名称前加*号
      }
      if (p.isOwner()) {
        // 是拥有者,则加粗显示
        name = String.format("<strong>%s</strong>", name);
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

  public static String buildAuthorNames(List<PubMemberPO> memberList) {
    StringBuffer authorNames = new StringBuffer();
    if (CollectionUtils.isEmpty(memberList)) {
      return null;
    }
    for (PubMemberPO p : memberList) {
      String name = StringUtils.trimToEmpty(p.getName());
      if (StringUtils.isBlank(name)) {
        continue;
      }
      if (p.getCommunicable() == 1) {
        name = "*" + name; // 是通讯作者，则名称前加*号
      }
      if (p.getOwner() == 1) {
        // 是拥有者,则加粗显示
        name = String.format("<strong>%s</strong>", name);
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

}
