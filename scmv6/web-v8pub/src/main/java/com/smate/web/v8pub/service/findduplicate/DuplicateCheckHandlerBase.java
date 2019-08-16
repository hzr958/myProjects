package com.smate.web.v8pub.service.findduplicate;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.web.v8pub.exception.DuplicateCheckParameterException;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.utils.PubDbUtils;

/**
 * 成果查重 基类
 * 
 * @author tsz
 *
 * @date 2018年8月16日
 */
public abstract class DuplicateCheckHandlerBase implements DuplicateCheckHandlerService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 参数校验
   * 
   * @throws Exception
   */
  public abstract void checkParameter(PubDuplicateDTO dup) throws DuplicateCheckParameterException;

  /**
   * 处理查重
   * 
   * @throws Exception
   */
  public abstract Map<String, String> handleDup(PubDuplicateDTO dup) throws ServiceException;

  /**
   * 执行
   */
  @Override
  public Map<String, String> excute(PubDuplicateDTO dup) throws ServiceException {
    Map<String, String> map = new HashMap<String, String>();
    boolean isHandleDup = false;
    // 执行参数检验方法
    try {
      this.checkParameter(dup);
      isHandleDup = true;
    } catch (DuplicateCheckParameterException e) {
      logger.error("成果查重参数存在问题", e);
      throw new ServiceException(e);
    }
    // 参数检验不过，不执行具体查重业务方法
    try {
      if (isHandleDup) {
        constructPubHash(dup);
        map = this.handleDup(dup);
      }
      if (CollectionUtils.isEmpty(map)) {
        map.put("msg", "error");
      }
      return map;
    } catch (Exception e) {
      logger.error("进行成果查重发生异常！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 构造hash值
   * 
   * @param dup
   */
  private void constructPubHash(PubDuplicateDTO dup) {
    String title = dup.title;
    title = HtmlUtils.htmlUnescape(title);
    title = HtmlUtils.htmlEscape(title);
    dup.hashTP = PubHashUtils.getTpHash(title, String.valueOf(dup.pubType));
    Long titleHash = PubHashUtils.cleanTitleHash(title);
    dup.hashT = titleHash != null ? titleHash.toString() : "";
    if (dup.pubYear != null && dup.pubYear != 0) {
      dup.hashTPP = PubHashUtils.getTitleInfoHash(title, dup.pubType, dup.pubYear);
    }
    // doi字段
    if (StringUtils.isNotBlank(dup.doi) || PubDbUtils.isCnkiDb(dup.srcDbId)) {
      // 计算未去除标点符号的hash值
      dup.hashDoi = PubHashUtils.cleanDoiHash(dup.doi);
      // 计算去除标点符号的hash值
      dup.hashCleanDoi = PubHashUtils.getDoiHashRemotePun(dup.doi);
    }
    // sourceId字段
    if (PubDbUtils.isIsiDb(dup.srcDbId) || PubDbUtils.isEiDb(dup.srcDbId)) {
      dup.hashSourceId = PubHashUtils.cleanSourceIdHash(dup.sourceId);
    }
    // 专利字段
    if (StringUtils.isNotBlank(dup.applicationNo)) {
      dup.hashApplicationNo = PubHashUtils.cleanPatentNoHash(dup.applicationNo);
    }
    if (StringUtils.isNotBlank(dup.publicationOpenNo)) {
      dup.hashPublicationOpenNo = PubHashUtils.cleanPatentNoHash(dup.publicationOpenNo);
    }
    // 标准号
    if (StringUtils.isNotBlank(dup.standardNo)) {
      dup.hashStandardNo = PubHashUtils.cleanPatentNoHash(dup.standardNo);
    }
    // 登记号
    if (StringUtils.isNotBlank(dup.registerNo)) {
      dup.hashRegisterNo = PubHashUtils.cleanPatentNoHash(dup.registerNo);
    }
  }


  public static void main(String[] args) {
    String title = "字交叉型π共轭分子3,6-二苯-1,2,4,5-(2',2\"-二苯基)-苯并二噁唑的电子结构和电荷传输性质的理论研究";
    title = HtmlUtils.htmlUnescape(title);
    title = HtmlUtils.htmlEscape(title);
    System.out.println(title);
  }

}
