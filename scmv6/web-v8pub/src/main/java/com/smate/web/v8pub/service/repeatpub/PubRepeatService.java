package com.smate.web.v8pub.service.repeatpub;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.PubDuplicateVO;
import com.smate.web.v8pub.vo.RepeatPubInfo;

public interface PubRepeatService {

  /**
   * 调用查重接口获取重复的成果pubId
   * 
   * @param dupVO
   * @return
   * @throws ServiceException
   */
  List<Long> listPubIdByCheckDup(PubDuplicateVO dupVO) throws ServiceException;

  /**
   * 通过pubId获取数据
   * 
   * @param dupPubIds
   * @return
   * @throws ServiceException
   */
  List<RepeatPubInfo> listRepeatPubDetail(List<Long> dupPubIds) throws ServiceException;

  /**
   * 通过DOI进行查重
   * 
   * @param DOI
   * @return
   * @throws ServiceException
   */
  Long getPdwhPubIdByCheckDup(String DOI) throws ServiceException;

}
