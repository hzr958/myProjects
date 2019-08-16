package com.smate.web.v8pub.service.authorname;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pub.util.AuthorNamesUtils;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.utils.PubBuildAuthorNamesUtils;
import com.smate.web.v8pub.utils.PubLocaleUtils;

@Service(value = "pubAuthorNameService")
@Transactional(rollbackFor = Exception.class)
public class PubAuthorNameServiceImpl implements PubAuthorNameService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @SuppressWarnings("unchecked")
  @Override
  public String disposeAuthorName(JSONArray members, String authorNames) throws ServiceException {
    try {
      String dealAuthorNames = authorNames;
      if (StringUtils.isBlank(authorNames)) {
        return "";
      }
      // 处理符合条件的成果作者
      if (isDealWith(authorNames)) {
        // 中英文作者名分开处理
        boolean isChina = PubLocaleUtils.getLocale(authorNames).equals(Locale.CHINA);
        if (isChina) {
          // 是中文的话直接进行拆分
          // 使用，或者, 进行拆分作者名
          authorNames = authorNames.replaceAll("[\\d]", ",");
          List<String> authors = parseAuthorNames(authorNames);
          dealAuthorNames = PubBuildAuthorNamesUtils.buildPubAuthorNames(authors);

        } else {
          // 英文作者且只有一个分号的作者使用下面的处理方式进行处理
          if (members != null && members.size() > 0) {
            List<PubMemberDTO> memberList =
                JacksonUtils.jsonToCollection(members.toJSONString(), List.class, PubMemberDTO.class);
            Integer validAuthorCount = countValidAuthors(memberList);
            boolean isExist = existAuthors(memberList, authorNames);

            if (validAuthorCount != null && validAuthorCount > 1 && !isExist) {
              // 使用，或者, 进行拆分作者名
              List<String> authors = parseAuthorNames(authorNames);
              dealAuthorNames = PubBuildAuthorNamesUtils.buildPubAuthorNames(authors);
            } else {
              // 不进行拆分作者名
              dealAuthorNames = AuthorNamesUtils.cleanAuthorNameExceptComma(authorNames);
            }
          }
        }
      } else {
        List<String> authorList = AuthorNamesUtils.parsePubAuthorNames(authorNames);
        dealAuthorNames = PubBuildAuthorNamesUtils.buildPubAuthorNames(authorList);
        // dealAuthorNames = AuthorNamesUtils.cleanAuthorName(dealAuthorNames);
      }
      return dealAuthorNames;
    } catch (Exception e) {
      logger.error("处理基准库成果作者名数据出错！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 使用authorName 直接全称匹配pub_author数据，存在则返回true
   * 
   * @param dataMap
   * @param authorName
   * @return
   */
  private boolean existAuthors(List<PubMemberDTO> members, String authorName) {
    boolean isExist = false;
    if (CollectionUtils.isNotEmpty(members)) {
      for (PubMemberDTO pubMemberBean : members) {
        // 先处理作者名
        authorName = getCleanAuthorName(authorName);
        String authorN = getCleanAuthorName(pubMemberBean.getName());
        if (authorName.equalsIgnoreCase(authorN)) {
          isExist = true;
        }
      }
    }
    return isExist;
  }

  private String getCleanAuthorName(String authorName) {
    if (StringUtils.isBlank(authorName)) {
      return "";
    }
    authorName = AuthorNamesUtils.cleanAuthorName(authorName);
    // 去除空格转换成小写
    authorName = authorName.replaceAll("\\s+", "").toLowerCase();
    return authorName;
  }

  /**
   * 拆分作者名，通过中英文逗号进行拆分
   * 
   * @param authorNames
   * @return
   */
  private List<String> parseAuthorNames(String authorNames) {
    List<String> list = new ArrayList<String>();
    if (StringUtils.isBlank(authorNames)) {
      return list;
    }
    String[] authors = authorNames.split("[,，]");
    if (authors != null && authors.length > 0) {
      for (String authorName : authors) {
        authorName = AuthorNamesUtils.cleanAuthorNameExceptComma(authorName);
        if (StringUtils.isNotBlank(authorName)) {
          list.add(authorName);
        }
      }
    }
    return list;
  }

  /**
   * 计算有效作者的数量 <br/>
   * 有效作者满足两个条件：作者名不为空，不为通讯作者
   * 
   * @param dataMap
   * @return
   */
  private Integer countValidAuthors(List<PubMemberDTO> members) {
    Integer count = 0;
    List<String> sameName = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(members)) {
      for (PubMemberDTO member : members) {
        if (StringUtils.isNotBlank(member.getName()) && !member.isCommunicable()
            && !sameName.contains(member.getName())) {
          sameName.add(member.getName());
          count++;
        }
      }
    }
    return count;
  }

  /**
   * 此作者名是否进行处理<br/>
   * 需要处理的作者名的条件：<br/>
   * 1.作者名中不含有分号（中英文分号都不允许有）<br/>
   * 2.作者名中只存在有一个逗号（中英文逗号总和只有一个）<br/>
   * 
   * @param authorNames
   * @return
   */
  private boolean isDealWith(String authorNames) {
    if (StringUtils.isNotBlank(authorNames)) {
      if (PubParamUtils.countStr(authorNames, ";") == 0 && PubParamUtils.countStr(authorNames, "；") == 0) {
        boolean isChina = PubLocaleUtils.getLocale(authorNames).equals(Locale.CHINA);
        Integer count = PubParamUtils.countStr(authorNames, ",") + PubParamUtils.countStr(authorNames, "，");
        if (isChina) {
          return true;
        } else {
          return (count == 1);
        }
      }
    }
    return false;
  }

}
