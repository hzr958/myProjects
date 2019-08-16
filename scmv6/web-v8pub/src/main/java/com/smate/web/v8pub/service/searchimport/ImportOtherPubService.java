package com.smate.web.v8pub.service.searchimport;

import java.util.Map;

import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.searchimport.PubImportVO;

/**
 * 导入他人成果服务类
 * 
 * @author aijiangbin
 * @date 2018年8月30日
 */
public interface ImportOtherPubService {


  /**
   * 导人成果
   * 
   * @param importVo
   * @return
   * @throws ServiceException
   */
  Map<String, String> importPub(PubImportVO importVo) throws ServiceException;


  /**
   * 导入成果到我的库中
   * 
   * @param map
   * @return
   * @throws ServiceException
   */
  Map<String, String> importPubToMyLib(PubSnsDetailDOM pubSnsDOM, PubImportVO importVo) throws ServiceException;
}
