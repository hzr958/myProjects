package com.smate.web.v8pub.service.match;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.pub.util.AuthorNamesUtils;
import com.smate.core.base.pub.util.PubAuthorNameMatchUtil;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.service.sns.psnconfigpub.PersonService;
import com.smate.web.v8pub.utils.PubInfoVerifyUtil;

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

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonService personService;


  @Override
  public Integer isMatch(String authorNames, Long psnId) throws ServiceException {
    try {
      Integer isMatch = 0;
      if (StringUtils.isNotBlank(authorNames) && !NumberUtils.isNullOrZero(psnId)) {
        Person person = personService.getPersonName(psnId);
        Set<String> nameList = buildNameList(person);
        List<String> nameArray = AuthorNamesUtils.parsePubAuthorNames(authorNames);
        if (nameArray != null && CollectionUtils.isNotEmpty(nameList) && nameArray.size() > 0) {
          for (String psnName : nameArray) {
            psnName = AuthorNamesUtils.cleanAuthorName(psnName);
            if (nameList.contains(psnName.toLowerCase())) {
              isMatch = 1;
            }
          }
        }
      }
      return isMatch;
    } catch (Exception e) {
      logger.error("拆分authorName匹配当前人出错！authorName={},psnId={}", authorNames, psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void authorMatch(List<PubMemberDTO> memberList, Long psnId) throws ServiceException {
    try {
      Person person = personService.getPersonName(psnId);
      Set<String> nameList = buildNameList(person);
      if (CollectionUtils.isNotEmpty(memberList)) {
        for (PubMemberDTO member : memberList) {
          String psnName = AuthorNamesUtils.cleanAuthorNameExceptComma(member.getName());
          // 有逗号的数据
          member.setName(psnName);
          // 清除逗号进行匹配
          psnName = XmlUtil.getCleanAuthorName5(psnName);
          if (nameList.contains(psnName.toLowerCase())) {
            member.setOwner(true);
            member.setDes3PsnId(Des3Utils.encodeToDes3(String.valueOf(psnId)));
            member.setPsnId(psnId);
            member.setEmail(person.getEmail());
            member.setInsNames(buildInsNames(person));
            // 匹配第一个就直接返回
            break;
          } else {
            member.setOwner(false);
          }
        }
      }
    } catch (Exception e) {
      logger.error("成果作者匹配当前人出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取person里面的name，ename，firstName+lastName，对这三种数据进行简称拆分
   * 
   * @param person
   * @return
   */
  private Set<String> buildNameList(Person person) {
    Set<String> nameList = new HashSet<>();
    if (person != null) {
      String name = person.getName();
      String eName = person.getEname();
      String uName = person.getFirstName() + " " + person.getLastName();

      if (StringUtils.isNotBlank(name)) {
        name = XmlUtil.getCleanAuthorName5(name);
        nameList.addAll(PubInfoVerifyUtil.buildName(name));
      }
      if (StringUtils.isNotBlank(eName)) {
        eName = XmlUtil.getCleanAuthorName5(eName);
        nameList.addAll(PubInfoVerifyUtil.buildName(eName));
      }
      if (StringUtils.isNotBlank(uName)) {
        uName = XmlUtil.getCleanAuthorName5(uName);
        nameList.addAll(PubInfoVerifyUtil.buildName(uName));
      }
    }
    return nameList;
  }

  /**
   * 重新构建单位机构名
   * 
   * @param person
   * @return
   */
  private List<MemberInsDTO> buildInsNames(Person person) {
    List<MemberInsDTO> insNames = new ArrayList<>();
    if (StringUtils.isNotBlank(person.getInsName())) {
      MemberInsDTO memberInsDTO = new MemberInsDTO();
      memberInsDTO.setInsId(person.getInsId());
      memberInsDTO.setInsName(person.getInsName());
      insNames.add(memberInsDTO);
    }
    return insNames;
  }


  @Override
  public List<PubMemberDTO> perfectMembers(String authorNames, List<PubMemberDTO> authors) {
    // 拆分authorNames
    List<String> nameList = new ArrayList<>();
    List<PubMemberDTO> memberList = buildByAuthorName(authorNames, nameList);
    // 作者信息匹配完善逻辑
    if (CollectionUtils.isNotEmpty(memberList) && CollectionUtils.isNotEmpty(authors)
        && CollectionUtils.isNotEmpty(nameList)) {
      // 第一层循环：进行全称匹配
      for (int j = 0; j < authors.size(); j++) {
        PubMemberDTO author = authors.get(j);
        if (author == null) {
          continue;
        }
        String authorName = author.getName();
        authorName = AuthorNamesUtils.cleanAuthorName(authorName);
        // 名字包含在总的nameList中，说明匹配成功
        if (nameList.contains(authorName)) {
          // 名字在数组中的位置
          int nameIndex = nameList.indexOf(authorName);
          if (nameIndex < memberList.size()) {
            PubMemberDTO member = memberList.get(nameIndex);
            Integer matchCount = author.getMatchCount();
            author.setMatchCount(++matchCount);
            perfect(member, author);
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
   * @param nameList
   * @return
   */
  protected List<PubMemberDTO> buildByAuthorName(String authorNames, List<String> nameList) {
    List<PubMemberDTO> memberList = new ArrayList<>();
    if (StringUtils.isNotBlank(authorNames)) {
      PubMemberDTO member = null;
      List<String> nameArray = AuthorNamesUtils.parsePubAuthorNames(authorNames);
      int i = 1;
      if (nameArray != null && nameArray.size() > 0) {
        for (String psnName : nameArray) {
          member = new PubMemberDTO();
          nameList.add(psnName);
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
        }
        pubMemberDTO.getDepts().add(dept);
      }
      // 单位机构需要补充上
      List<MemberInsDTO> insList = xmlAuthor.getInsNames();
      if (CollectionUtils.isNotEmpty(insList)) {
        pubMemberDTO.setInsNames(insList);
      }
    }
  }
}
