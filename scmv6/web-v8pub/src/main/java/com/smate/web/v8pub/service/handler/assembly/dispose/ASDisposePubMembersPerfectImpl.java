package com.smate.web.v8pub.service.handler.assembly.dispose;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.match.PubAuthorMatchService;

/**
 * 具体匹配规则可见SCM-22636
 * 
 * @author YJ 2018年10月22日
 */

@Transactional(rollbackFor = Exception.class)
public class ASDisposePubMembersPerfectImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAuthorMatchService pubAuthorMatchService;


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
      List<PubMemberDTO> xmlAuthors = new ArrayList<>();
      // 进行拆分authorNames，通过pub_author转出的数据进行补全完善信息的逻辑，只在基准库保存或更新的时候走
      if (pub.members != null && pub.members.size() > 0) {
        xmlAuthors = JacksonUtils.jsonToCollection(pub.members.toJSONString(), List.class, PubMemberDTO.class);
      }
      List<PubMemberDTO> memberList = pubAuthorMatchService.perfectMembers(pub.authorNames, xmlAuthors);
      pub.members = JSONArray.parseArray(JacksonUtils.jsonObjectSerializer(memberList));
    } catch (Exception e) {
      logger.error("完善members数据出错！", e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }


}
