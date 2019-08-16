package com.smate.web.v8pub.service.sns;

import java.util.List;
import java.util.Map;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.BaseService;
import com.smate.web.v8pub.service.query.PubQueryDTO;

/**
 * 个人成果基础信息服务类
 * 
 * @author houchuanjie
 * @date 2018/06/01 17:45
 */
public interface PubSnsService extends BaseService<Long, PubSnsPO> {

  /**
   * 查询成果的权限
   * 
   * @param psnId
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Integer queryPubPermission(Long psnId, Long pubId) throws ServiceException;

  /**
   * 查询成果的作者id
   *
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Long queryPubPsnId(Long pubId) throws ServiceException;

  /**
   * 查询成果信息
   *
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public PubSnsPO queryPubSns(Long pubId);

  /**
   * 查询个人成果列表
   * 
   * @param pubQueryDTO
   */
  public void queryPubList(PubQueryDTO pubQueryDTO);

  /**
   * 查询群组成果列表
   * 
   * @param pubQueryDTO
   */
  public void queryGrpPubList(PubQueryDTO pubQueryDTO);

  /**
   * 查询动态我的成果
   * 
   * @param pubQueryDTO
   */
  public void queryDynMyPub(PubQueryDTO pubQueryDTO);

  /**
   * 查询
   * 
   * @param pubQueryDTO
   */
  public void queryOpenPubList(PubQueryDTO pubQueryDTO);

  /**
   * 查询个人库的成果统计
   * 
   * @param pubQueryDTO
   */
  public List<Map<String, Object>> querySnsPubCount(PubQueryDTO pubQueryDTO, int type);

  /**
   * 查询群组库的成果统计
   * 
   * @param pubQueryDTO
   */
  public List<Map<String, Object>> queryGrpPubCount(PubQueryDTO pubQueryDTO, int type);

  /**
   * 只是查询成果的统计数
   * 
   * @param pubQueryDTO
   * @return
   */
  public Long findPubCount(PubQueryDTO pubQueryDTO);

  /**
   * 查询个人成果列表给center-open系统调用
   * 
   * @param pubQueryDTO
   */
  public void queryPubListForOpen(PubQueryDTO pubQueryDTO);

  /**
   * 查询群组成果列表给center-open系统调用
   * 
   * @param pubQueryDTO
   */
  public void queryGrpPubListForOpen(PubQueryDTO pubQueryDTO);

  /**
   * 查询个人成果(不分页)给center-open系统调用
   * 
   * @param pubQueryDTO
   */
  public void queryAllPubListForOpen(PubQueryDTO pubQueryDTO);

  /**
   * 通过pubids 查询成果
   * 
   * @param pubQueryDTO
   */
  public void queryByPubIds(PubQueryDTO pubQueryDTO);

  /**
   * 查询人员公开的成果给center-open系统调用
   * 
   * @param pubQueryDTO
   */
  public void queryPsnPublicPubForOpen(PubQueryDTO pubQueryDTO);

  public Long getPsnNotExistsResumePubCount(Long psnId) throws ServiceException;

  /**
   * 查询最后更新的成果
   *
   * @param pubQueryDTO
   */
  public void queryLastUpdatePub(PubQueryDTO pubQueryDTO);

  /**
   * 通过成果id 查询最近更新的成果id
   *
   * @param pubQueryDTO
   */
  public void queryLastUpdatePubByPubIds(PubQueryDTO pubQueryDTO);


  /**
   * 是否存在标题相同的重复成果 1=是
   *
   * @param title
   * @param grpId
   * @return
   */
  public Integer existRepGrpPub(String title, Long grpId);


  /**
   * 查询个人成果列表给中山系统调用
   *
   * @param pubQueryDTO
   */
  public void queryPubListForZS(PubQueryDTO pubQueryDTO);

  /**
   * 成果
   * 
   * @param pubQueryDTO
   * @return
   */
  public List<Integer> queryPublishYearList(PubQueryDTO pubQueryDTO);

  /**
   * 成果
   * 
   * @param pubQueryDTO
   * @return
   */
  public void getAllGrpPubRcmd(PubQueryDTO pubQueryDTO);

  /**
   * 获取总的查询出的成果数量和指定成果在查询结果中的位置（第几个）
   * 
   * @param pubQueryDTO
   */
  public void queryPubIndexAndTotalCount(PubQueryDTO pubQueryDTO);

}
