package com.smate.sie.center.task.pdwh.json.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.insunit.SieInsUnitService;
import com.smate.sie.center.task.pdwh.service.SiePublicationLogService;
import com.smate.sie.center.task.pub.enums.PublicationOperationEnum;
import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;
import com.smate.sie.core.base.utils.dao.pub.SiePubMemberDao;
import com.smate.sie.core.base.utils.model.pub.SiePubMember;
import com.smate.sie.core.base.utils.pub.dto.PubMemberDTO;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

@Service("publicationMemberService")
@Transactional(rollbackFor = Exception.class)
public class PublicationMemberServiceImpl implements PublicationMemberService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private SiePubMemberDao pubMemberDao;
  @Autowired
  private SieInsPersonDao sieInsPersonDao;
  @Autowired
  private SieInsUnitService sieInsUnitService;
  /**
   * 操作日志
   */
  @Autowired
  private SiePublicationLogService publicationLogService;

  @SuppressWarnings("unchecked")
  @Override
  public void savePubMember(PubJsonDTO pubJson) throws ServiceException {
    Long pubId = pubJson.pubId;
    this.deletePubMemberByPubId(pubJson);
    // 拼接authorNames节点值
    // StringBuffer sbAuthor = new StringBuffer();
    List<PubMemberDTO> memberDTOs = new ArrayList<PubMemberDTO>();
    if (pubJson.members != null && pubJson.members.size() > 0) {
      List<PubMemberDTO> memberList =
          JacksonUtils.jsonToCollection(pubJson.members.toJSONString(), List.class, PubMemberDTO.class);
      if (memberList != null && memberList.size() > 0) {
        for (PubMemberDTO pubMemberDTO : memberList) {
          if (StringUtils.isBlank(pubMemberDTO.getName())) {
            continue;
          }
          SiePubMember pm = new SiePubMember();
          pm.setAuthorPos(pubMemberDTO.isCommunicable() ? 1 : 0);
          if (CollectionUtils.isNotEmpty(pubMemberDTO.getInsNames())) {
            pm.setInsName(StringUtils.substring(pubMemberDTO.getInsNames().get(0).getInsName(), 0, 100)); // 取第一条单位信息的单位名称
            pm.setInsId(pubMemberDTO.getInsNames().get(0).getInsId());
          }
          pm.setMemberName(StringUtils.substring(pubMemberDTO.getName(), 0, 100));
          pm.setEmail(StringUtils.substring(pubMemberDTO.getEmail(), 0, 50));
          pm.setPubId(pubId);
          pm.setSeqNo(pubMemberDTO.getSeqNo().longValue());
          // pubMemberDTO 实体中的 firstAuthor值取值于基准库scmpdwh.v_pub_pdwh_member.first_author值转换后
          pm.setFirstAuthor(pubMemberDTO.isFirstAuthor() ? 1 : 0);
          try {
            pubMemberDao.savePubMember(pm);
          } catch (Exception e) {
            logger.error("savePubMember保存成员出错  成果id:{}", pubId, e);
            throw new ServiceException(e);
          }
          pubMemberDTO.setPmId(pm.getPmId());
          memberDTOs.add(pubMemberDTO);
          // sbAuthor.append(pubMemberDTO.getName() + ";");

        }
      }
      // String authorsStr = sbAuthor.toString();
      // String authorNames = authorsStr.substring(0, authorsStr.lastIndexOf(";"));
      // pubJson.authorNames = authorNames;
    }
    String memberJsonStr = JacksonUtils.jsonObjectSerializerNoNull(memberDTOs);
    pubJson.members = JSONArray.parseArray(memberJsonStr);
  }

  @Override
  public void deletePubMemberByPubId(PubJsonDTO pubJson) throws ServiceException {
    Long pubId = pubJson.pubId;
    Long currentInsId = pubJson.insId;
    try {
      // 操作人id
      Long opPsnId = SecurityUtils.getCurrentUserId();
      List<Long> pmIds = pubMemberDao.getPmIdByPubId(pubId);
      for (Long pmId : pmIds) {
        SiePubMember pm = pubMemberDao.getPubMemberById(pmId);
        pubMemberDao.deletePubMember(pm);
        Map<String, String> opDetail = new HashMap<String, String>();
        opDetail.put("pmId", pm.getPmId().toString());
        opDetail.put("author", pm.getMemberName());
        opDetail.put("seqNo", pm.getSeqNo().toString());
        publicationLogService.logOp(pubId, opPsnId, currentInsId, PublicationOperationEnum.RemovePubMember, opDetail);
      }
    } catch (Exception e) {
      logger.error("deletePubMember成果成员出错，成果Id = {}", pubId, e);
      throw new ServiceException(e);
    }
  }
}
