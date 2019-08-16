package com.smate.sie.center.task.pdwh.json.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.insunit.SieInsUnitService;
import com.smate.sie.center.task.pdwh.service.SiePatentLogService;
import com.smate.sie.center.task.pub.enums.PublicationOperationEnum;
import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;
import com.smate.sie.core.base.utils.dao.pub.SiePatMemberDao;
import com.smate.sie.core.base.utils.model.pub.SiePatMember;
import com.smate.sie.core.base.utils.pub.dto.PubMemberDTO;
import com.smate.sie.core.base.utils.pub.exception.ServiceException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

@Service("patentMemberService")
@Transactional(rollbackFor = Exception.class)
public class PatentMemberServiceImpl implements PatentMemberService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private SiePatMemberDao patMemberDao;
  @Autowired
  private SiePatentLogService patentLogService;
  @Autowired
  private SieInsPersonDao sieInsPersonDao;
  @Autowired
  private SieInsUnitService sieInsUnitService;

  @Override
  public void deletePatMemberByPatId(PubJsonDTO patJson) throws ServiceException {
    Long patId = patJson.pubId;
    Long currentInsId = patJson.insId;
    try {
      // 操作人id
      Long opPsnId = SecurityUtils.getCurrentUserId();
      List<Long> pmIds = patMemberDao.getPmIdByPatId(patId);
      for (Long pmId : pmIds) {
        SiePatMember pm = patMemberDao.getPatMemberById(pmId);
        patMemberDao.deletePatMember(pm);
        Map<String, String> opDetail = new HashMap<String, String>();
        opDetail.put("pmId", pm.getPmId().toString());
        opDetail.put("author", StringUtils.substring(pm.getMemberName(), 0, 30));
        opDetail.put("seqNo", pm.getSeqNo().toString());
        patentLogService.logOp(patId, opPsnId, currentInsId, PublicationOperationEnum.RemovePubMember, opDetail);
      }
    } catch (Exception e) {
      logger.error("deletePatMember专利发明人出错 ，专利Id = {}", patId, e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void savePatMember(PubJsonDTO patJson) throws ServiceException {
    Long patId = patJson.pubId;
    this.deletePatMemberByPatId(patJson);
    // 拼接authorNames节点值
    List<PubMemberDTO> memberDTOs = new ArrayList<PubMemberDTO>();
    if (patJson.members != null && patJson.members.size() > 0) {
      List<PubMemberDTO> memberList =
          JacksonUtils.jsonToCollection(patJson.members.toJSONString(), List.class, PubMemberDTO.class);
      if (memberList != null && memberList.size() > 0) {
        for (PubMemberDTO patMemberDTO : memberList) {
          if (StringUtils.isBlank(patMemberDTO.getName())) {
            continue;
          }
          SiePatMember pm = new SiePatMember();
          pm.setAuthorPos(patMemberDTO.isCommunicable() ? 1L : 0L);
          pm.setInsName(StringUtils.substring(patMemberDTO.getInsName(), 0, 100));
          pm.setInsId(patMemberDTO.getInsId());
          pm.setMemberName(StringUtils.substring(patMemberDTO.getName(), 0, 100));
          pm.setPatId(patId);
          pm.setSeqNo(patMemberDTO.getSeqNo().longValue());
          // 保存数据
          try {
            patMemberDao.savePatMember(pm);
          } catch (Exception e) {
            logger.error("savePatMember保存成员出错  专利id:{}", patId, e);
            throw new ServiceException(e);
          }
          patMemberDTO.setPmId(pm.getPmId());
          memberDTOs.add(patMemberDTO);
        }
      }
    }
    String memberJsonStr = JacksonUtils.jsonObjectSerializerNoNull(memberDTOs);
    patJson.members = JSONArray.parseArray(memberJsonStr);
  }

}
