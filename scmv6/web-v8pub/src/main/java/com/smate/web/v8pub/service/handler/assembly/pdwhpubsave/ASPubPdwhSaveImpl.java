package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;

/**
 * 基准库成果主表 保存/更新
 * 
 * @author YJ
 *
 *         2018年7月25日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubPdwhSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhService pubPdwhService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    PubPdwhPO pubPdwhPO = null;
    try {
      if (!NumberUtils.isNullOrZero(pub.pubId)) {
        pubPdwhPO = pubPdwhService.get(pub.pubId);
      }
      if (pubPdwhPO == null) {
        pubPdwhPO = new PubPdwhPO();
        pubPdwhPO.setCreatePsnId(pub.psnId);
        pubPdwhPO.setGmtCreate(new Date());
        // 基准库默认是1
        pubPdwhPO.setUpdateMark(1);
        // 成果状态默认为0
        pubPdwhPO.setStatus(PubPdwhStatusEnum.DEFAULT);
      }
      String authorNames = XmlUtil.subStr500char(pub.authorNames);
      pubPdwhPO.setAuthorNames(authorNames);
      pubPdwhPO.setBriefDesc(pub.briefDesc);
      Integer citations = PubParamUtils.resetCitation(pub.srcDbId, pub.citations);
      // 系统引用次数大则更新为系统的
      citations = PubParamUtils.maxCitations(pubPdwhPO.getCitations(), citations);
      pubPdwhPO.setCitations(citations);
      pubPdwhPO.setGmtModified(new Date());
      pubPdwhPO.setPublishYear(pub.publishYear);
      pubPdwhPO.setPublishMonth(pub.publishMonth);
      pubPdwhPO.setPublishDay(pub.publishDay);
      pubPdwhPO.setPubType(pub.pubType);
      pubPdwhPO.setCountryId(pub.countryId);
      pubPdwhPO.setTitle(pub.title);
      pubPdwhService.saveOrUpdate(pubPdwhPO);
      pub.pubId = pubPdwhPO.getPubId();
    } catch (Exception e) {
      logger.error("保存基准库成果主表数据异常！", pubPdwhPO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库保存或更新成果主表出错！", e);
    }
    Map<String, String> map = new HashMap<String, String>();
    if (!NumberUtils.isNullOrZero(pub.pubId)) {
      map.put("de3PubId", Des3Utils.encodeToDes3(pub.pubId.toString()));
    }
    return map;
  }

}
