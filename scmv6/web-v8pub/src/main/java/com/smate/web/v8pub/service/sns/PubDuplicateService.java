package com.smate.web.v8pub.service.sns;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubDuplicatePO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.service.BaseService;

public interface PubDuplicateService extends BaseService<Long, PubDuplicatePO> {

  /**
   * 判断成果是否被编辑，有字段更新的话，会产生一个新的DetailsHash
   *
   * @param pubId 成果id
   * @param newDetailsHash 新的成果hash值
   * @return true 代表成果内容被改变 false 代表成果内容没有被改变
   * @throws ServiceException
   */
  Boolean isPubChange(Long pubId, String newDetailsHash) throws ServiceException;

  /**
   * 通过doi进行查重
   *
   * @param hashDoi
   * @return
   * @throws ServiceException
   */
  List<Long> dupByDoi(Long hashDoi, Long hashCleanDoi, Long psnId, Long pubId) throws ServiceException;

  /**
   * 根据hashTP或者hashTPP进行成果查重
   *
   * @param hashTP
   * @param hashTPP
   * @return 查重出多个成果时，取第一个成果的成果id即可
   */
  List<Long> getPsnPubDuplicate(String hashTP, String hashTPP) throws ServiceException;

  /**
   * 通过doi进行查重
   *
   * @param hashDoi
   * @return
   * @throws ServiceException
   */
  List<Long> dupByDoi(Long hashDoi) throws ServiceException;

  /**
   * 通过sourceId进行查重
   *
   * @param hashSourceId
   * @return
   * @throws ServiceException
   */
  List<Long> dupBySourceId(Long hashSourceId, Long psnId, Long pubId) throws ServiceException;

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
  List<Long> dupByPatentInfo(Long hashApplicationNo, Long hashPublicationOpenNo, Long psnId, Long pubId)
      throws ServiceException;

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
  List<Long> dupByPubInfo(String hashTP, String hashTPP, Long psnId, Long pubId) throws ServiceException;

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
   * 通过标准号进行查重
   * 
   * @param hashStandardNo
   * @param psnId
   * @return
   */
  List<Long> dupByStandardNo(Long hashStandardNo, Long psnId, Long pubId) throws ServiceException;

  /**
   * 通过登记号进行查重
   * 
   * @param hashRegisterNo
   * @param psnId
   * @return
   */
  List<Long> dupByRegisterNo(Long hashRegisterNo, Long psnId, Long pubId) throws ServiceException;

  GroupPubPO getGroupDupPub(Long groupId, Long pubId) throws ServiceException;

  /**
   * 专利号和申请号查重不到数据时，排除表内有专利号和申请号的数据，通过hashTPP再查重一次
   * 
   * @param hashTP
   * @param hashTPP
   * @param psnId
   * @return
   */
  List<Long> dupByPubInfoNullAppNoAndOpenNo(String hashTP, String hashTPP, Long psnId, Long pubId)
      throws ServiceException;

  List<Long> dupByPubInfoNullStandardNo(String hashTP, String hashTPP, Long psnId, Long pubId) throws ServiceException;

  List<Long> dupByPubInfoNullRegisterNo(String hashTP, String hashTPP, Long psnId, Long pubId) throws ServiceException;

  List<Long> dupByPubInfoNullDoi(String hashTP, String hashTPP, Long psnId, Long pubId) throws ServiceException;

  List<Long> dupByPubInfoNullSourceId(String hashTP, String hashTPP, Long psnId, Long pubId) throws ServiceException;


}
