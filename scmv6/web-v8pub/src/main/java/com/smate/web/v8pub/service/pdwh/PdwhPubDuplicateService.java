package com.smate.web.v8pub.service.pdwh;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubDuplicatePO;
import com.smate.web.v8pub.service.BaseService;

public interface PdwhPubDuplicateService extends BaseService<Long, PdwhPubDuplicatePO> {


  /**
   * 根据hashTPP查找相同的成果id
   * 
   * @param hashTPP
   * @return
   * @throws ServiceException
   */
  List<Long> listDuplicatePubByTPP(String hashTPP) throws ServiceException;

  /**
   * 在表中doi数据不为空的前提下，通过doi进行查重
   * 
   * @param hashDoi
   * @return
   * @throws ServiceException
   */
  List<Long> dupByNotNullDoi(Long hashDoi, Long hashCleanDoi) throws ServiceException;

  /**
   * 在表中数据doi为空的前提下，通过hashTPP和hashTP进行查重
   * 
   * @param hashTP
   * @param hashTPP
   * @return
   * @throws ServiceException
   */
  List<Long> dupByPubInfoNullDoi(String hashTP, String hashTPP) throws ServiceException;

  /**
   * 通过sourceId进行查重
   * 
   * @param hashSourceId
   * @return
   * @throws ServiceException
   */
  List<Long> dupBySourceId(Long hashSourceId) throws ServiceException;

  /**
   * 通过专利信息进行查重
   * 
   * @param hashApplicationNo
   * @param hashPublicationOpenNo
   * @return
   * @throws ServiceException
   */
  List<Long> dupByPatentInfo(Long hashApplicationNo, Long hashPublicationOpenNo) throws ServiceException;

  /**
   * 通过成果的title pubType pubYear进行查重
   * 
   * @param hashTP
   * @param hashTPP
   * @return
   * @throws ServiceException
   */
  List<Long> dupByPubInfo(String hashTP, String hashTPP) throws ServiceException;

  /**
   * 通过成果的title
   * 
   * @param hashT
   * @return
   * @throws ServiceException
   */
  List<Long> dupByPubInfo(String hashT) throws ServiceException;

  /**
   * 通过成果标准号
   * 
   * @param hashStandardNo
   * @return
   * @throws ServiceException
   */
  List<Long> dupByStandardNo(Long hashStandardNo) throws ServiceException;

  /**
   * 通过成果登记号
   * 
   * @param hashRegisterNo
   * @return
   * @throws ServiceException
   */
  List<Long> dupByRegisterNo(Long hashRegisterNo) throws ServiceException;

  /**
   * 专利申请号，公开号查重不到，再进行一次hashTPP的查重，且需要排除掉有专利号和申请号的数据
   * 
   * @param hashTP
   * @param hashTPP
   * @return
   */
  List<Long> dupByPubInfoNullAppNoAndOpenNo(String hashTP, String hashTPP);

  /**
   * 排除掉有标准号的数据，进行TPP查重
   * 
   * @param hashTP
   * @param hashTPP
   * @return
   * @throws ServiceException
   */
  List<Long> dupByPubInfoNullStandardNo(String hashTP, String hashTPP) throws ServiceException;

  /**
   * 排除掉有登记号的数据，进行TPP查重
   * 
   * @param hashTP
   * @param hashTPP
   * @return
   * @throws ServiceException
   */
  List<Long> dupByPubInfoNullRegisterNo(String hashTP, String hashTPP) throws ServiceException;

  /**
   * 排除掉有souceId的数据，进行TPP查重
   * 
   * @param hashSourceId
   * @return
   */
  List<Long> dupByPubInfoNullSourceId(String hashTP, String hashTPP) throws ServiceException;

}
