package com.smate.web.v8pub.service.pdwh;

import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubFulltextPsnRcmd;
import com.smate.web.v8pub.service.BaseService;
import com.smate.web.v8pub.service.query.PubQueryDTO;

/**
 * 基准成果基础信息服务接口
 * 
 * @author houchuanjie
 * @date 2018/06/01 18:10
 */
public interface PubPdwhService extends BaseService<Long, PubPdwhPO> {

  /**
   * 通过ids 查找成果集合
   * 
   * @return
   */
  public void findByIds(PubQueryDTO pubQueryDTO);
  /**
   * 通过ids 查找成果集合 ， sie 专用
   *
   * @return
   */
  public void findByIdsforSie(PubQueryDTO pubQueryDTO);

  /**
   * 通过ids 查找成果集合  包括删除的
   *
   * @return
   */
  public void findAllByIds(PubQueryDTO pubQueryDTO);

  /**
   * 是否已收藏过该基准库成果
   * 
   * @param psnId
   * @param pdwhPubId
   * @return true: 已收藏， false：未收藏
   */
  public boolean hasCollectedPdwhPub(Long psnId, Long pdwhPubId);

  /**
   * 获取基准库成果短地址
   * 
   * @param pdwhPubId
   */
  public String buildPdwhPubIndexUrl(Long pdwhPubId);

  /**
   * 获取全文推荐
   * 
   * @param pdwhId
   * @return
   */
  public PubFulltextPsnRcmd getPubFulltextPsnRcmdById(Long pdwhId);

  /**
   * 更新基准库成果状态
   * 
   * @param pdwhPubId
   * @throws ServiceException
   */
  public void updateStatusByPubId(Long pdwhPubId, PubPdwhStatusEnum status) throws ServiceException;
}
