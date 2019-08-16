package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubMemberService;

/**
 * 成果成员保存/更新
 * 
 * @author YJ
 *
 *         2018年7月24日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubMemberSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubMemberService pubMemberService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    PubMemberPO pubMemberPO = null;
    try {
      pubMemberService.deleteAllMember(pub.pubId);
      if (pub.members != null && pub.members.size() > 0) {
        List<PubMemberDTO> memberList =
            JacksonUtils.jsonToCollection(pub.members.toJSONString(), List.class, PubMemberDTO.class);
        if (memberList != null && memberList.size() > 0) {
          for (PubMemberDTO pubMemberDTO : memberList) {
            pubMemberPO = new PubMemberPO();
            String name = pubMemberDTO.getName();
            if (StringUtils.isBlank(name)) {
              continue;
            }
            pubMemberPO.setName(name);
            Long pubId = NumberUtils.isNullOrZero(pubMemberPO.getPubId()) ? pub.pubId : pubMemberPO.getPubId();
            pubMemberPO.setPubId(pubId);
            Integer seqNo = pubMemberDTO.getSeqNo();
            pubMemberPO.setFirstAuthor((seqNo != null && seqNo == 1) ? 1 : 0);
            pubMemberPO.setPsnId(pubMemberDTO.getPsnId());
            pubMemberPO.setEmail(pubMemberDTO.getEmail());
            pubMemberPO.setSeqNo(seqNo);
            pubMemberPO.setCommunicable(pubMemberDTO.isCommunicable() ? 1 : 0);
            List<MemberInsDTO> insList = pubMemberDTO.getInsNames();
            if (insList != null && insList.size() > 0) {
              pubMemberPO.setInsName(pubMemberDTO.getInsNames().get(0).getInsName());
              pubMemberPO.setInsId(pubMemberDTO.getInsNames().get(0).getInsId());
            }
            pubMemberPO.setOwner(pubMemberDTO.isOwner() ? 1 : 0);
            pubMemberService.saveMember(pubMemberPO);
            pubMemberDTO.setMemberId(pubMemberPO.getId());
          }
          pub.members = JSONArray.parseArray(JacksonUtils.jsonObjectSerializer(memberList));
        }
      }
    } catch (Exception e) {
      logger.error("成果成员服务：保存或者更新成果成员异常！members={}", pub.members, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库成果作者出错!", e);
    }
    return null;
  }

}
