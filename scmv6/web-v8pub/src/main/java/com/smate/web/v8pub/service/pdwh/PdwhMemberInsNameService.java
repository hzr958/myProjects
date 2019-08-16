package com.smate.web.v8pub.service.pdwh;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhMemberInsNamePO;
import com.smate.web.v8pub.service.BaseService;

/**
 * 基准库成果作者单位机构服务
 * 
 * @author YJ
 *
 *         2018年12月27日
 */
public interface PdwhMemberInsNameService extends BaseService<Long, PdwhMemberInsNamePO> {

  /**
   * 删除指定pubId的成果作者单位信息
   * 
   * @param pdwhPubId
   * @throws ServiceException
   */
  void deleteAll(Long pdwhPubId) throws ServiceException;

}
