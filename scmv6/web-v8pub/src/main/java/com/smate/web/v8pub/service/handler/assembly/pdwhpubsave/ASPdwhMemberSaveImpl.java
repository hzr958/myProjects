package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PdwhPubMemberPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PdwhPubMemberService;

@Transactional(rollbackFor = Exception.class)
public class ASPdwhMemberSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubMemberService pdwhPubMemberService;

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
      pdwhPubMemberService.deleteAllMember(pub.pubId);
      if (pub.members != null && pub.members.size() > 0) {
        List<PubMemberDTO> memberList =
            JacksonUtils.jsonToCollection(pub.members.toJSONString(), List.class, PubMemberDTO.class);
        PdwhPubMemberPO pdwhpubMemberPO = null;
        if (memberList != null && memberList.size() > 0) {
          for (PubMemberDTO p : memberList) {
            if (StringUtils.isBlank(p.getName())) {
              continue;
            }
            pdwhpubMemberPO = new PdwhPubMemberPO();
            pdwhpubMemberPO.setPdwhPubId(pub.pubId);
            pdwhpubMemberPO.setCommunicable(p.isCommunicable() ? 1 : 0);
            pdwhpubMemberPO.setEmail(p.getEmail());
            pdwhpubMemberPO.setFirstAuthor(p.isFirstAuthor() ? 1 : 0);
            if (p.getInsNames() != null && p.getInsNames().size() > 0) {
              pdwhpubMemberPO.setInsCount(p.getInsNames().size());
            } else {
              pdwhpubMemberPO.setInsCount(0);
            }
            pdwhpubMemberPO.setName(p.getName());
            pdwhpubMemberPO.setOwner(p.isOwner() ? 1 : 0);
            pdwhpubMemberPO.setPsnId(p.getPsnId());
            pdwhpubMemberPO.setSeqNo(p.getSeqNo());
            pdwhPubMemberService.saveMember(pdwhpubMemberPO);
            p.setMemberId(pdwhpubMemberPO.getId());
          }
        }
        pub.members = JSONArray.parseArray(JacksonUtils.jsonObjectSerializer(memberList));
      }
    } catch (Exception e) {
      logger.error("基准库成果成员服务：保存或者更新成果成员异常！members={}", pub.members.toJSONString(), e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库保存或更新成果作者记录出错！", e);
    }
    return null;
  }

}
