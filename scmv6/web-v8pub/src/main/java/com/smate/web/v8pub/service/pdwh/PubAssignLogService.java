package com.smate.web.v8pub.service.pdwh;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubAssignLogPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;

/**
 * 成果认领的服务类
 * 
 * @author aijiangbin
 * @date 2018年7月23日
 */
public interface PubAssignLogService {

  /**
   * 查询未认领的成果pdwhPubId
   * 
   * @param pubQueryDTO
   * @return
   */
  public void queryPubConfirmIdList(PubQueryDTO pubQueryDTO);

  public void queryPubConfirmIdIgnoreStatusList(PubQueryDTO pubQueryDTO);

  /**
   * 更新自动认领的状态
   * 
   * @param confirmPubIds
   * @param psnId
   * @param pubGenre
   * @param pubId
   * @throws ServiceException
   */
  void updateAutoConfirmStatus(List<Long> dupPubIds, Long psnId, Long snsPubId, Integer pubGenre)
      throws ServiceException;

  /**
   * 根据pdwhPubId获取记录
   * 
   * @param pubQueryDTO
   * @return
   * @throws ServiceException
   */
  List<PubAssignLogPO> queryPubAssignLogByIds(PubQueryDTO pubQueryDTO) throws ServiceException;

  List<PubAssignLogPO> queryAllPubAssignLogByIds(PubQueryDTO pubQueryDTO) throws ServiceException;

  public void queryPubAssignListForOpen(PubQueryDTO pubQueryDTO) throws ServiceException;

  /**
   * 查找所有的认领成果
   * 
   * @param pubQueryDTO
   * @throws ServiceException
   */
  public void queryAllPubAssignListForOpen(PubQueryDTO pubQueryDTO) throws ServiceException;

  /**
   * 
   * @param pubConfirmId
   * @return
   * @throws ServiceException
   */
  PubAssignLogPO findByPubConfirmId(Long pubConfirmId) throws ServiceException;

  /**
   * 查询需要确认的成果总数
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public Long queryPubConfirmCount(Long psnId) throws ServiceException;

  /**
   * 更新基准库成果状态
   * 
   * @param pdwhPubId
   * @throws ServiceException
   */
  public void updateStatusByPdwhPubId(Long pdwhPubId, Integer status) throws ServiceException;

  public void psnPubCopartnerRcmd(Long pdwhPubId, Long psnId) throws ServiceException;
}
