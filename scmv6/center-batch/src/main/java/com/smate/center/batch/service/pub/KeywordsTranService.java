package com.smate.center.batch.service.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.KeywordsEnTranZh;
import com.smate.center.batch.model.sns.pub.KeywordsZhTranEn;

/**
 * 关键词翻译服务.
 * 
 * @author liqinghua
 * 
 */
public interface KeywordsTranService extends Serializable {

  /**
   * 查找英文翻译中文的关键词.
   * 
   * @param enKw
   * @return
   */
  public KeywordsEnTranZh findEnTranZhKw(String enKw) throws ServiceException;

  /**
   * 查找中文翻译英文的关键词.
   * 
   * @param enKw
   * @return
   */
  public KeywordsZhTranEn findZhTranEnKw(String zhKw) throws ServiceException;
}
