package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PubSnsStatusEnum;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubSnsService;

/**
 * 个人库成果主表 保存/更新
 * 
 * @author YJ
 *
 *         2018年7月24日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubSnsSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubSnsService pubSnsService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    Map<String, String> map = new HashMap<>();
    // 构建参数
    PubSnsPO pubSnsPO = null;
    try {
      if (!NumberUtils.isNullOrZero(pub.pubId)) {
        pubSnsPO = pubSnsService.get(pub.pubId);
      }
      if (pubSnsPO == null) {
        pubSnsPO = new PubSnsPO();
        pubSnsPO.setCreatePsnId(pub.psnId);
        pubSnsPO.setGmtCreate(new Date());
        pubSnsPO.setStatus(PubSnsStatusEnum.DEFAULT);
        // 只有保存时记录来源，修改时不进行更改来源值
        pubSnsPO.setRecordFrom(pub.recordFrom);
      }
      String authorNames = XmlUtil.subStr500char(pub.authorNames);
      pubSnsPO.setAuthorNames(authorNames);
      String briefDesc = pub.briefDesc;
      if (briefDesc.length() > 500) {
        briefDesc = StringUtils.substring(briefDesc, briefDesc.length() - 500, briefDesc.length());
      }
      pubSnsPO.setBriefDesc(briefDesc);
      pubSnsPO.setCitations(pub.citations);
      pubSnsPO.setPublishYear(pub.publishYear);
      pubSnsPO.setPublishMonth(pub.publishMonth);
      pubSnsPO.setPublishDay(pub.publishDay);
      pubSnsPO.setPubType(pub.pubType);
      pubSnsPO.setRecordFrom(pub.recordFrom);
      pubSnsPO.setCountryId(pub.countryId);
      // 表里面最大只存1000
      String title = StringUtils.substring(pub.title, 0, 1000);
      pubSnsPO.setTitle(title);
      pubSnsPO.setGmtModified(new Date());
      pubSnsPO.setUpdateMark(pub.updateMark);
      pubSnsService.saveOrUpdate(pubSnsPO);
      pub.pubId = pubSnsPO.getPubId();
      map.put("des3PubId", Des3Utils.encodeToDes3(String.valueOf(pub.pubId)));
      logger.debug("更新或保存成果主表记录成功");
    } catch (Exception e) {
      logger.error("保存成果主表数据出错！PubSnsPO={}", pubSnsPO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库成果主表出错!", e);
    }
    return map;
  }

}
