package com.smate.web.v8pub.service.findduplicate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.enums.PubHandlerStatusEnum;
import com.smate.web.v8pub.exception.DuplicateCheckParameterException;

/***
 * 成果查重服务实现
 * 
 * @author tsz
 *
 * @date 2018年8月16日
 */
public class PubDuplicateCheckServiceImpl implements PubDuplicateCheckService {

  private HashMap<Integer, DuplicateCheckHandlerBase> dupMap;

  // 成果常量值
  private static final Integer[] PUB_TYPE_CONSTANTS = new Integer[] {PublicationTypeEnum.AWARD,
      PublicationTypeEnum.BOOK, PublicationTypeEnum.CONFERENCE_PAPER, PublicationTypeEnum.JOURNAL_ARTICLE,
      PublicationTypeEnum.PATENT, PublicationTypeEnum.OTHERS, PublicationTypeEnum.THESIS,
      PublicationTypeEnum.BOOK_CHAPTER, PublicationTypeEnum.STANDARD, PublicationTypeEnum.SOFTWARE_COPYRIGHT};

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public Map<String, String> dupCheck(PubDuplicateDTO dup) {
    Map<String, String> map = new HashMap<>();
    try {
      // 1.先检查公共参数：title和pubType，检查不通过，不执行后面代码
      checkNecessityParamter(dup);
      // 2.取指定的查重对象
      DuplicateCheckHandlerBase duplicateCheckHandlerBase = dupMap.get(dup.pubGener);
      // 3.执行查重
      map = duplicateCheckHandlerBase.excute(dup);
    } catch (DuplicateCheckParameterException e) {
      logger.error("成果查重参数校验失败！", e);
      map.put("status", PubHandlerStatusEnum.ERROR.getValue());
      map.put("msg", e.getMessage());
    } catch (Exception e) {
      logger.error("成果查重异常！", e);
      map.put("status", PubHandlerStatusEnum.ERROR.getValue());
      map.put("msg", e.getMessage());
    }
    return map;
  }

  /**
   * 检查查重必要参数
   * 
   * @param dup
   * @return
   */
  private void checkNecessityParamter(PubDuplicateDTO dup) throws DuplicateCheckParameterException {
    if (dup.pubGener != null && !checkPubGener(dup.pubGener)) {
      logger.error("pubGener不合法，只能为[1,2,3]");
      throw new DuplicateCheckParameterException("pubGener不合法,pubGener" + dup.pubGener);
    }
    if (StringUtils.isEmpty(dup.title)) {
      logger.error("个人成果查重失败，title为空");
      throw new DuplicateCheckParameterException("个人成果查重失败，title为空");
    }
    if (dup.pubType != null && !checkPubType(dup.pubType)) {
      logger.error("pubType不合法,pubType=" + dup.pubType);
      throw new DuplicateCheckParameterException("pubType不合法，pubType=" + dup.pubType);
    }
    if (dup.pubGener == PubGenreConstants.PSN_PUB) {
      if (StringUtils.isEmpty(dup.des3PsnId)) {
        logger.error("个人成果查重des3PsnId必须不能为空");
        throw new DuplicateCheckParameterException("个人成果查重des3PsnId必须不能为空");
      } else {
        try {
          Long psnId = Long.valueOf(Des3Utils.decodeFromDes3(dup.des3PsnId));
          if (NumberUtils.isNullOrZero(psnId)) {
            logger.error("个人成果查重des3PsnId不合法，解密出来为null或者0L");
            throw new DuplicateCheckParameterException("个人成果查重des3PsnId不合法，解密出来为null或者0L");
          }
        } catch (Exception e) {
          logger.error("个人成果查重des3PsnId不合法，无法解密");
          throw new DuplicateCheckParameterException("个人成果查重des3PsnId不合法，无法解密");
        }
      }
    }
    // 基准库查重：专利类型，如果专利号和申请号均为空，不进行查重 此逻辑不需要
    // if (dup.pubGener == PubGenreConstants.PDWH_SNS_PUB) {
    // if (dup.pubType.equals(5)) {
    // if (StringUtils.isBlank(dup.applicationNo) && StringUtils.isBlank(dup.publicationOpenNo)) {
    // logger.error("基准库成果查重，专利类型中申请号和公开号均为空");
    // throw new DuplicateCheckParameterException("基准库成果查重，专利类型中申请号和公开号均为空");
    // }
    // }
    // }
  }

  private boolean checkPubType(Integer pubType) {
    List<Integer> pubTypeList = Arrays.asList(PUB_TYPE_CONSTANTS);
    Long count = pubTypeList.stream().filter((p) -> p.equals(pubType)).count();
    if (NumberUtils.isNullOrZero(count)) {
      return false;
    }
    return true;
  }

  private static boolean checkPubGener(Integer pubGener) {
    // 个人成果查重
    if (PubGenreConstants.PSN_PUB == pubGener) {
      return true;
    }
    // 基准库成果查重
    if (PubGenreConstants.PDWH_SNS_PUB == pubGener) {
      return true;
    }
    // 群组成果查重
    if (PubGenreConstants.GROUP_PUB == pubGener) {
      return true;
    }
    // 验证论文
    if (PubGenreConstants.VERIFY_PDWH_SNS_PUB == pubGener) {
      return true;
    }
    return false;
  }

  public HashMap<Integer, DuplicateCheckHandlerBase> getDupMap() {
    return dupMap;
  }

  public void setDupMap(HashMap<Integer, DuplicateCheckHandlerBase> dupMap) {
    this.dupMap = dupMap;
  }



}
