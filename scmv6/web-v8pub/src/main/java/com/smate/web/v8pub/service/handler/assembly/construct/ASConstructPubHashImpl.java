package com.smate.web.v8pub.service.handler.assembly.construct;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.utils.PubDbUtils;

/**
 * 成果hash值的构建
 * 
 * @author YHX 2018年7月26日
 */
@Transactional(rollbackFor = Exception.class)
public class ASConstructPubHashImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

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
    try {
      String title = pub.title;
      if (StringUtils.isNotBlank(title)) {
        // pubType 和 title是必须有的，因此无论如何都需要计算hash值
        pub.hashTP = PubHashUtils.getTpHash(title, String.valueOf(pub.pubType));
        if (pub.publishYear != null && pub.publishYear != 0) {
          // pubYear 可能为null，为null是不计算三者合并的hash值
          pub.hashTPP = PubHashUtils.getTitleInfoHash(title, pub.pubType, pub.publishYear);
        }

        Long hashT = PubHashUtils.cleanTitleHash(title);
        pub.hashTitle = NumberUtils.isNullOrZero(hashT) ? "" : hashT + "";
      }
      // 注意：成果是可以被多个机构所收录的，谁收录了就计算谁的hash值
      if (StringUtils.isNotBlank(pub.doi)) {
        Long hash = PubHashUtils.cleanDoiHash(pub.doi);
        Long cleanHash = PubHashUtils.getDoiHashRemotePun(pub.doi);
        pub.hashDoi = NumberUtils.isNullOrZero(hash) ? "" : hash + "";
        pub.hashCleanDoi = NumberUtils.isNullOrZero(cleanHash) ? "" : cleanHash + "";
      }
      if (PubDbUtils.isCnkiDb(pub.srcDbId)) {
        Long hash = PubHashUtils.cleanDoiHash(pub.doi);
        Long cleanHash = PubHashUtils.getDoiHashRemotePun(pub.doi);
        pub.hashCnkiDoi = NumberUtils.isNullOrZero(hash) ? "" : hash + "";
        pub.hashCleanCnkiDoi = NumberUtils.isNullOrZero(cleanHash) ? "" : cleanHash + "";
      }
      // 构建EI和ISI的hash值
      constructEIAndISIHash(pub);

      // 构造专利信息的查重值
      constructPatentHash(pub);

      // 构造标准信息的查重值
      constructStandardHash(pub);

      // 构造软件著作权信息的查重值
      constructSoftwareCopyrightHash(pub);

      if (StringUtils.isNotBlank(pub.detailsJson)) {
        Long detailsHash = HashUtils.getStrHashCode(pub.detailsJson);
        pub.detailsHash = NumberUtils.isNullOrZero(detailsHash) ? "" : detailsHash + "";
      }
      logger.debug("sns库构建成果计算hash值参数成功");
    } catch (Exception e) {
      logger.error("成果计算hash值出错！", e);
      throw new PubHandlerAssemblyException("ASConstructPubHashImpl计算成果Hash值出错！", e);
    }
    return null;
  }

  private void constructSoftwareCopyrightHash(PubDTO pub) {
    if (pub.pubType == PublicationTypeEnum.SOFTWARE_COPYRIGHT) {
      try {
        SoftwareCopyrightBean p = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), SoftwareCopyrightBean.class);
        if (StringUtils.isNotBlank(p.getRegisterNo())) {
          Long hash = PubHashUtils.cleanPatentNoHash(p.getRegisterNo());
          pub.hashRegisterNo = NumberUtils.isNullOrZero(hash) ? "" : hash + "";
        }
      } catch (Exception e) {
        logger.error("成果hash值查重计算，软件著作权对象转化失败，pubTypeInfo={}", pub.pubTypeInfo);
      }
    }
  }

  private void constructStandardHash(PubDTO pub) {
    if (pub.pubType == PublicationTypeEnum.STANDARD) {
      try {
        StandardInfoBean p = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), StandardInfoBean.class);
        if (StringUtils.isNotBlank(p.getStandardNo())) {
          Long hash = PubHashUtils.cleanPatentNoHash(p.getStandardNo());
          pub.hashStandardNo = NumberUtils.isNullOrZero(hash) ? "" : hash + "";
        }
      } catch (Exception e) {
        logger.error("成果hash值查重计算，标准对象转化失败，pubTypeInfo={}", pub.pubTypeInfo);
      }
    }
  }

  /**
   * 构造EI和ISI的sourceId的hash值
   * 
   * @param pub
   */
  @SuppressWarnings("unchecked")
  private void constructEIAndISIHash(PubDTO pub) {
    try {
      // sns库收录情况
      List<PubSituationDTO> sitList =
          JacksonUtils.jsonToCollection(pub.situations.toJSONString(), List.class, PubSituationDTO.class);
      if (sitList == null) {
        return;
      }
      for (PubSituationDTO p : sitList) {
        if (StringUtils.isEmpty(p.getSrcDbId())) {
          continue;
        }
        Integer dbId = Integer.parseInt(p.getSrcDbId());
        if (PubDbUtils.isIsiDb(dbId)) {
          Long hash = PubHashUtils.cleanSourceIdHash(p.getSrcId());
          pub.hashIsiSourceId = NumberUtils.isNullOrZero(hash) ? "" : hash + "";
        }
        if (PubDbUtils.isEiDb(dbId)) {
          Long hash = PubHashUtils.cleanSourceIdHash(p.getSrcId());
          pub.hashEiSourceId = NumberUtils.isNullOrZero(hash) ? "" : hash + "";
        }
      }
    } catch (Exception e) {
      logger.error("计算EI和ISI的hash值出错", e);
    }
  }

  /**
   * 专利字段查重hash值计算 异常不需要处理，不影响整个hash计算的过程
   * 
   * @param pub
   */
  private void constructPatentHash(PubDTO pub) {
    if (pub.pubType == PublicationTypeEnum.PATENT) {
      try {
        PatentInfoBean p = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), PatentInfoBean.class);
        if (StringUtils.isNotBlank(p.getApplicationNo())) {
          Long hash = PubHashUtils.cleanPatentNoHash(p.getApplicationNo());
          pub.hashApplicationNo = NumberUtils.isNullOrZero(hash) ? "" : hash + "";
        }
        if (StringUtils.isNotBlank(p.getPublicationOpenNo())) {
          Long hash = PubHashUtils.cleanPatentNoHash(p.getPublicationOpenNo());
          pub.hashPublicationOpenNo = NumberUtils.isNullOrZero(hash) ? "" : hash + "";
        }
      } catch (Exception e) {
        logger.error("成果hash值查重计算：专利对象转化失败，pubTypeInfo={}", pub.pubTypeInfo);
      }

    }
  }

}
