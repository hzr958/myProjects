package com.smate.web.group.service.grp.pub;

import java.util.Map;

import com.smate.web.group.action.grp.form.GrpPubRcmdForm;

/**
 * 群组成果推荐服务接口类
 * 
 * @author tsz
 *
 */
public interface GrpPubRcmdService {
  /**
   * 获取群组待确认的成果数量
   * 
   * @param grpId
   * @return
   */
  public Long getPendingConfirmedCount(Long grpId);

  /**
   * 获取单条群组成果推荐数据
   * 
   * @param form
   * @throws Exception
   */
  public void getOneRcmdPub(GrpPubRcmdForm form) throws Exception;

  /**
   * 获取多条群组成果推荐数据
   * 
   * @param form
   * @throws Exception
   */
  public void getAllRcmdPub(GrpPubRcmdForm form) throws Exception;

  /**
   * 操作群组推荐成果 确认或忽略
   * 
   * @param form
   * @throws Exception
   */
  public void optionGrpPubRcmd(GrpPubRcmdForm form) throws Exception;

  /**
   * 获取群组推荐成果详细信息
   * 
   * @param pubId
   * @throws Exception
   */
  public Map<String, Object> getRcmdPubDetailsFromPdwh(Long pubId) throws Exception;

  /**
   * 拒绝群组推荐成果
   * 
   * @param Long grpId, Long pubId, Long psnId
   * @throws Exception
   */
  public void rejectGrpPubRcmd(Long grpId, Long pubId, Long psnId) throws Exception;

  /**
   * 导入群组推荐成果
   * 
   * @param Long grpId, Long pubId, Long psnId
   * @throws Exception
   */
  public Long acceptGrpPubRcmd(Long grpId, Long pubId, Long psnId) throws Exception;

}
