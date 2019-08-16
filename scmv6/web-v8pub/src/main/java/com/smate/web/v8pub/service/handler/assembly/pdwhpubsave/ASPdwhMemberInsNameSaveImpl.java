package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PdwhMemberInsNamePO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PdwhMemberInsNameService;

@Transactional(rollbackFor = Exception.class)
public class ASPdwhMemberInsNameSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhMemberInsNameService pdwhMemberInsNameService;

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
      // 成果作者单位数据，迁至成果作者删除业务之前执行PdwhPubMemberServiceImpl
      // pdwhMemberInsNameService.deleteAll(pub.pubId);
      if (pub.members != null && pub.members.size() > 0) {
        List<PubMemberDTO> memberList =
            JacksonUtils.jsonToCollection(pub.members.toJSONString(), List.class, PubMemberDTO.class);
        PdwhMemberInsNamePO memeberInsName = null;
        for (PubMemberDTO pubMemberDTO : memberList) {
          Set<String> depts = pubMemberDTO.getDepts();
          List<MemberInsDTO> insList = pubMemberDTO.getInsNames();
          if (CollectionUtils.isNotEmpty(depts)) {
            for (String dept : depts) {
              memeberInsName = new PdwhMemberInsNamePO();
              memeberInsName.setMemberId(pubMemberDTO.getMemberId());
              dept = StringUtils.trimToEmpty(dept);
              dept = StringUtils.substring(dept, 0, 3500);
              memeberInsName.setDept(dept);
              pdwhMemberInsNameService.saveOrUpdate(memeberInsName);
            }
          } else if (CollectionUtils.isNotEmpty(insList)) {
            for (MemberInsDTO memberInsDTO : insList) {
              memeberInsName = new PdwhMemberInsNamePO();
              memeberInsName.setMemberId(pubMemberDTO.getMemberId());
              memeberInsName.setDept(memberInsDTO.getInsName());
              memeberInsName.setInsId(memberInsDTO.getInsId());
              memeberInsName.setInsName(memberInsDTO.getInsName());
              pdwhMemberInsNameService.saveOrUpdate(memeberInsName);
            }
          }
        }


      }

    } catch (Exception e) {
      logger.error("基准库成果成员服务：保存或者更新成果作者单位异常！members={}", pub.members.toJSONString(), e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库保存或更新成果作者单位记录出错！", e);
    }
    return null;
  }

}
