package com.smate.web.mobile.v8pub.service;

import java.util.List;
import java.util.Map;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.web.mobile.v8pub.vo.PubListVO;

/**
 * 移动端成果查询服务
 * 
 * @author wsn
 * @date 2018年9月3日
 */
public interface MobilePubQueryService {

  /**
   * 构建PubListVO对象
   * 
   * @param vo
   * @param class1
   * @throws ServiceException
   */
  void buildPubListVO(PubListVO vo, Class<?> class1) throws ServiceException;

  /**
   * 获取所有的科技领域
   * 
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> getAllScienceArea() throws ServiceException;

  /**
   * 处理成果全文信息
   * 
   * @param pubInfo
   * @throws ServiceException
   */
  void dealPubFulltextInfo(PubInfo pubInfo, Long currentLoginPsnId) throws ServiceException;
}
