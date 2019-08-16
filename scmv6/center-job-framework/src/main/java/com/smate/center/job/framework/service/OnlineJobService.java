package com.smate.center.job.framework.service;

import com.smate.center.job.common.enums.JobWeightEnum;
import com.smate.center.job.common.exception.ServiceException;
import com.smate.center.job.common.po.OnlineJobPO;
import com.smate.center.job.framework.dto.OnlineJobDTO;
import com.smate.center.job.web.support.jqgrid.PageModel;
import com.smate.center.job.web.vo.OnlineJobVO;
import java.util.List;

/**
 * 在线任务服务类接口
 *
 * @author houchuanjie
 * @date 2018年4月24日 下午5:57:10
 */
public interface OnlineJobService {

  /**
   * 获取指定权重指定数目的、开启的、未执行的在线任务列表，按优先级，修改时间升序排序
   *
   * @param weight 权重
   * @param size 要获取的任务数
   * @return
   */
  List<OnlineJobPO> getEnableList(JobWeightEnum weight, int size);

  /**
   * 批量更新任务信息
   *
   * @param list
   */
  void batchUpdateUnprocessed(List<OnlineJobDTO> list);

  /**
   * 批量更新已处理完毕的任务信息
   * @param list
   */
  void batchUpdateProcessed(List<OnlineJobPO> list);

  /**
   * 批量保存或更新任务
   *
   * @param list
   */
  void batchUpdate(List<OnlineJobPO> list);

  /**
   * 批量保存
   *
   * @param list
   */
  void batchSave(List<OnlineJobPO> list);

  /**
   * 页面jqgrid的分页查询
   *
   * @param pageModel
   * @return
   */
  PageModel<OnlineJobVO> searchUnprocessed(PageModel<OnlineJobVO> pageModel);

  /**
   * 分页查询执行历史
   *
   * @param pageModel
   * @return
   */
  PageModel<OnlineJobVO> searchHistory(PageModel<OnlineJobVO> pageModel);

  /**
   * 分页查询执行失败的任务记录
   *
   * @param pageModel
   * @return
   */
  PageModel<OnlineJobVO> searchFailed(PageModel<OnlineJobVO> pageModel);

  /**
   * 批量删除执行失败或没有处理完成的 OnlineJobPO
   *
   * @param idList
   */
  void batchDelete(List<String> idList);

  /**
   * 批量删除已经处理完成的
   *
   * @param idList
   */
  void batchDeleteHistory(List<String> idList);

  /**
   * 新增或修改保存
   *
   * @param onlineJobVO
   * @throws ServiceException
   */
  OnlineJobVO saveOrUpdate(OnlineJobVO onlineJobVO) throws ServiceException;
}
