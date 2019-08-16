package com.smate.center.open.service.nsfc.pub;

import com.smate.center.open.model.nsfc.NsfcwsPublication;
import com.smate.core.base.utils.model.Page;


/**
 * 基金委webservice集成smate成果接口获取GoogleScholar库成果信息服务.
 * 
 * @author tsz
 * 
 */
public interface NsfcwsPubService {

  /**
   * 按条件分页查询指定google人员成果数据.
   * 
   * @param psnId
   * @param keywords
   * @param sortType
   * @param pageSize
   * @param pageNum
   * @return
   * @throws Exception
   */
  Page<NsfcwsPublication> getPsnPubByPage(Long psnId, String keywords, String excludedPubIDS, String sortType,
      int pageSize, int pageNum) throws Exception;

  /**
   * 按条件查找指定google人员的成果总数.
   * 
   * @param psnId
   * @param keywords
   * @return
   * @throws Exception
   */
  Long getPsnPubCount(Long psnId, String keywords, String excludedPubIDS) throws Exception;

  NsfcwsPublication getNsfcwsPublicationByPubId(Long pubId) throws Exception;

  /**
   * 通过成果pubId获取成果xml数据.
   * 
   * @param pubId
   * @return
   * @throws Exception
   */
  String getPubXmlByPubId(Long pubId) throws Exception;

  /**
   * 获取成果收录.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  String getPubCitedListByPubId(Long pubId) throws Exception;
}
