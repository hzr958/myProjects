package com.smate.center.task.service.sns.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.pub.util.AuthorNamesUtils;
import com.smate.core.base.pub.util.PubAuthorNameMatchUtil;
import com.smate.web.v8pub.dto.PubMemberDTO;

/**
 * 成果作者名拆分匹配服务
 * 
 * @author YJ
 *
 *         2018年10月22日
 */

@Service(value = "pubAuthorMatchService")
@Transactional(rollbackFor = Exception.class)
public class PubAuthorMatchServiceImpl implements PubAuthorMatchService {

  @Override
  public List<PubMemberDTO> perfectMembers(String authorNames, List<PubMemberDTO> authors) {
    List<PubMemberDTO> memberList = buildByAuthorName(authorNames);
    // 作者信息匹配完善逻辑
    if (CollectionUtils.isNotEmpty(memberList) && CollectionUtils.isNotEmpty(authors)) {
      // 第一层循环：进行全称匹配
      for (int j = 0; j < authors.size(); j++) {
        Integer matchCount = 1;
        for (int i = 0; i < memberList.size(); i++) {
          // 逐个匹配
          PubMemberDTO author = authors.get(j);
          PubMemberDTO member = memberList.get(i);
          if (member != null && author != null) {
            if (PubAuthorNameMatchUtil.compareByFullName(member.getName(), author.getName())) {
              author.setMatchCount(matchCount++);
              perfect(member, author);
            }
          }
        }
      }

      // 去除已经匹配完善过的authors
      List<PubMemberDTO> newAuthors =
          authors.stream().filter(author -> author.getMatchCount() == 0).collect(Collectors.toList());

      // 第二层循环：进行简称匹配
      if (CollectionUtils.isNotEmpty(newAuthors)) {
        for (int j = 0; j < newAuthors.size(); j++) {
          Integer memberLocation = null, authorLocation = null;
          Integer matchCount = 0;
          for (int i = 0; i < memberList.size(); i++) {
            // 逐个匹配
            PubMemberDTO author = newAuthors.get(j);
            PubMemberDTO member = memberList.get(i);
            if (member != null && author != null) {
              if (PubAuthorNameMatchUtil.compareNames(member.getName(), author.getName())) {
                memberLocation = i;
                authorLocation = j;
                matchCount++;
              }
            }
          }
          if (matchCount == 1) {
            if (memberLocation != null && authorLocation != null) {
              perfect(memberList.get(memberLocation), newAuthors.get(authorLocation));
            }
          }
        }
      }

    }
    return memberList;
  }

  /**
   * 拆分authorNames数据
   * 
   * @param authorNames
   * @return
   */
  protected List<PubMemberDTO> buildByAuthorName(String authorNames) {
    List<PubMemberDTO> memberList = new ArrayList<>();
    if (StringUtils.isNotBlank(authorNames)) {
      PubMemberDTO member = null;
      List<String> nameArray = AuthorNamesUtils.parsePubAuthorNames(authorNames);
      int i = 1;
      if (nameArray != null && nameArray.size() > 0) {
        for (String psnName : nameArray) {
          member = new PubMemberDTO();
          if (StringUtils.isNotBlank(psnName)) {
            psnName = HtmlUtils.htmlEscape(psnName);
            member.setName(psnName);
            member.setPsnId(null);
            member.setDes3PsnId("");
            member.setEmail("");
            member.setDept("");
            member.setInsNames(new ArrayList<>());

            member.setOwner(false);
            member.setCommunicable(false);
            if (i == 1) {
              member.setFirstAuthor(true);
            }
            member.setSeqNo(i++);
            memberList.add(member);
          }
        }
      }
    }
    return memberList;
  }

  /**
   * 数据的补全
   * 
   * @param pubMemberDTO
   * @param xmlAuthor
   * @return
   */
  private void perfect(PubMemberDTO pubMemberDTO, PubMemberDTO xmlAuthor) {
    if (pubMemberDTO != null && xmlAuthor != null) {
      // 用xmlAuthor中的信息补全newAuthor信息
      // 补全设置为通讯作者
      boolean communicable = pubMemberDTO.isCommunicable() | xmlAuthor.isCommunicable();
      pubMemberDTO.setCommunicable(communicable);
      if (StringUtils.isBlank(pubMemberDTO.getEmail())) {
        // 补全邮箱，邮箱已经有值的话，不进行设置（唯一的情况：此人已经匹配上当前人）
        String email = StringUtils.trimToEmpty(xmlAuthor.getEmail());
        pubMemberDTO.setEmail(email);
      }
      if (StringUtils.isBlank(pubMemberDTO.getDept())) {
        // 补全dept部门
        String dept = StringUtils.trimToEmpty(xmlAuthor.getDept());
        pubMemberDTO.setDept(dept);
      }
      // 处理一个作者存在多个部门的数据
      if (StringUtils.isNotBlank(xmlAuthor.getDept())) {
        String dept = StringUtils.trimToEmpty(xmlAuthor.getDept());
        if (StringUtils.isNotBlank(dept)) {
          // 去除尾后的所有点
          dept = dept.replaceAll("[.。 ]*$", "");
          pubMemberDTO.getDepts().add(dept);
        }
      }
    }
  }
}
