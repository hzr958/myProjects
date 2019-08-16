package com.smate.center.job.framework.service;

import com.smate.center.job.common.exception.ServiceException;
import com.smate.center.job.common.po.OnlineJobConfigPO;
import com.smate.center.job.web.support.jqgrid.PageModel;
import com.smate.center.job.web.vo.OnlineJobConfigVO;
import java.util.List;

/**
 * 在线任务配置类相关服务接口
 *
 * @author houchuanjie
 * @date 2018/04/28 15:15
 */
public interface OnlineJobConfigService {

  /**
   * 批量更新或保存
   *
   * @param list
   */
  void batchUpdate(List<OnlineJobConfigPO> list);

  /**
   * 更新或保存
   *
   * @param onlineJobConfigPO
   */
  void saveOrUpdate(OnlineJobConfigPO onlineJobConfigPO) throws ServiceException;

  /**
   * 页面jqGrid分页查询
   *
   * @param page
   * @return
   */
  PageModel<OnlineJobConfigVO> search(
      PageModel<OnlineJobConfigVO> page);

  /**
   * 新增在线任务配置
   *
   * @param onlineJobConfigVO
   */
  void addJob(OnlineJobConfigVO onlineJobConfigVO) throws ServiceException;

  /**
   * 通过id批量删除
   *
   * @param idList
   */
  void batchDelete(List<String> idList) throws ServiceException;

  /**
   * 更新任务信息
   *
   * @param onlineJobConfigVO
   */
  void updateJobInfo(OnlineJobConfigVO onlineJobConfigVO) throws ServiceException;
}
