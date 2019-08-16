package com.smate.center.open.service.publication;

import java.util.List;

import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.model.Page;

/**
 * 成果SERVICE接口.
 * 
 * @author ajb
 * 
 */
public interface PublicationService {
  /**
   * 获取成果.
   * 
   * @param pubId
   * @return
   */
  Publication getPub(Long pubId) throws Exception;

  /**
   * 查询某人的公开成果数（IRIS业务系统接口使用）.
   * 
   * @param psnId
   * @param keywords
   * @param excludedPubIds 以,号分割的pubId字符串.
   * @param pubTypes
   * @return
   */
  Long getPsnPublicPubCount(Long psnId, String keywords, String excludedPubIds, List<Integer> permissions,
      String pubTypes) throws Exception;

  /**
   * 进展、结题报告的成果作者.
   * 
   * @param pubId
   * @return
   */
  String buildFinalPrjAuthorName(Long pubId) throws Exception;

  /**
   * 查询某人的成果数（IRIS业务系统接口使用）,应华师要求增加作者参数.
   * 
   * @param psnId
   * @param keywords
   * @param excludedPubIds
   * @param pubTypes TODO
   * @return
   */
  Long getPsnPubCount(Long psnId, String keywords, String authors, String excludedPubIds, String pubTypes)
      throws Exception;

  /**
   * 分页获取指定用户公开成果记录（IRIS业务系统接口使用）.
   * 
   * @param psnId
   * @param keywords
   * @param excludedPubIds 以,号分割的pubId字符串.
   * @param permissions
   * @param sortType
   * @param pubTypes TODO
   * @return
   */
  Page<Publication> getPsnPublicPubByPage(Long psnId, String keywords, String excludedPubIds, List<Integer> permissions,
      String sortType, Page<Publication> page, String pubTypes) throws Exception;

  /**
   * 通过pubid获取成果XML.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String getPubXmlById(Long pubId) throws Exception;

  /**
   * 分页获取指定用户成果记录（IRIS业务系统接口使用）.
   * 
   * @param psnId
   * @param keywords
   * @param excludedPubIds 以,号分割的pubId字符串.
   * @param sortType
   * @param pubTypes TODO
   * @return
   */
  Page<Publication> getPsnPubByPage(Long psnId, String keywords, String authors, String excludedPubIds, String sortType,
      Page<Publication> page, String pubTypes) throws Exception;

  List<Publication> findPubIdsByPsnId(Long psnId) throws Exception;
}
