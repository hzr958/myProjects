package com.smate.web.v8pub.service.handler.assembly.construct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;

/**
 * 构造成果作者邮件信息
 * 
 * @author YJ
 *
 *         2019年1月8日
 */
@Transactional(rollbackFor = Exception.class)
public class ASConstructPubMemberEmailImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      Set<String> emails = new HashSet<>();
      if (pub.members != null && pub.members.size() > 0) {
        List<PubMemberDTO> memberList =
            JacksonUtils.jsonToCollection(pub.members.toJSONString(), List.class, PubMemberDTO.class);
        if (CollectionUtils.isNotEmpty(memberList)) {
          for (PubMemberDTO pubMemberDTO : memberList) {
            List<String> eList = parseEmail(pubMemberDTO.getEmail());
            if (CollectionUtils.isNotEmpty(eList)) {
              for (String email : eList) {
                if (StringUtils.isNotBlank(email)) {
                  emails.add(email);
                }
              }
            }
          }
        }
      }
      pub.emailList = emails;
    } catch (Exception e) {
      logger.error("构建基准库成果作者邮件信息出错！", e);
      throw new PubHandlerAssemblyException("构建基准库成果作者邮件信息出错！", e);
    }
    return null;
  }


  private List<String> parseEmail(String emails) {
    if (StringUtils.isBlank(emails)) {
      return new ArrayList<>();
    }
    String[] eList = emails.split("[; ]");
    List<String> list = new ArrayList<String>();
    for (String email : eList) {
      email = email.trim();
      email = StringUtils.trimToEmpty(email);
      if (StringUtils.isNotBlank(email)) {
        list.add(email);
      }
    }
    return list;
  }

}
