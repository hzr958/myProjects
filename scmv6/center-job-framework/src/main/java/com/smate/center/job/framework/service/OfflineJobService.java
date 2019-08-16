package com.smate.center.job.framework.service;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobWeightEnum;
import com.smate.center.job.common.exception.ServiceException;
import com.smate.center.job.common.po.OfflineJobPO;
import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.center.job.web.support.jqgrid.PageModel;
import com.smate.center.job.web.vo.OfflineJobVO;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.sun.istack.internal.NotNull;
import java.util.List;

/**
 * 离线任务信息表服务类接口
 *
 * @author houchuanjie
 * @date 2017年12月28日 下午4:47:40
 */
public interface OfflineJobService {

  /**
   * 检查任务指定的业务表主键是否是唯一键
   *
   * @author houchuanjie
   * @date 2018年1月5日 上午9:46:03
   */
  boolean hasUniqueKey(OfflineJobDTO jobInfo);

  /**
   * 检查任务配置的业务表名是否正确
   */
  boolean hasTable(OfflineJobDTO jobInfo);

  /**
   * 获取离线任务需要处理的记录总数
   */
  long getRecordCount(OfflineJobDTO jobInfo) throws DAOException;

  /**
   * 根据任务信息，从相关业务表，对指定主键进行排序，查出rownum，筛选出rownum在seqList中的记录的主键。
   *
   * @param jobInfo 任务信息
   * @param seqList 要查询记录的rownum的集合
   * @return 指定rownum的业务表记录的主键集合
   */
  List<Long> getUniqueKeyList(OfflineJobDTO jobInfo, List<Long> seqList);

  /**
   * 获取所有任务列表
   *
   * @author houchuanjie
   * @date 2017年12月28日 下午4:51:05
   */
  List<OfflineJobPO> getAll();

  /**
   * 获取指定权重指定数量的、开启的、状态未执行或执行完毕任务列表，按优先级，修改时间升序排序
   *
   * @param weight 权重
   * @param size 获取的数量
   */
  List<OfflineJobPO> getEnableList(JobWeightEnum weight, int size);

  /**
   * 批量更新任务信息
   */
  void batchUpdateJobInfo(List<OfflineJobDTO> list);

  /**
   * 根据参数获取任务数据信息
   *
   * @author houchuanjie
   * @date 2018年2月28日 下午3:45:40
   * @param pageModel
   */
  PageModel<OfflineJobVO> search(PageModel<OfflineJobVO> pageModel);

  /**
   * 新增taskinfo记录
   *
   * @author houchuanjie
   * @date 2018年3月1日 上午11:43:55
   */
  void addJob(@NotNull OfflineJobVO offlineJobVO) throws ServiceException;

  /**
   * 新增保存offlineJobPO
   */
  void saveOrUpdate(@NotNull OfflineJobPO offlineJobPO) throws ServiceException;

  /**
   * 批量删除offlineJobInfo
   *
   * @author houchuanjie
   * @date 2018年3月1日 下午2:22:50
   */
  void batchDelete(List<String> idList) throws ServiceException;

  /**
   * 更新对象信息到数据库
   *
   * @author houchuanjie
   * @date 2018年3月1日 下午4:28:52
   */
  void updateJobInfo(@NotNull OfflineJobVO offlineJobVO) throws ServiceException;

  /**
   * 更新对象信息到数据库
   */
  public void updateJobInfo(@NotNull OfflineJobDTO offlineJobDTO);

  /**
   * 根据id列表获取对应的taskinfo列表，并从缓存中获取其最新状态进度信息
   *
   * @author houchuanjie
   * @date 2018年3月5日 下午4:17:32
   */
  List<OfflineJobVO> getNewestList(@NotNull List<String> idList);

  /**
   * 批量保存或更新任务
   */
  void batchUpdate(List<OfflineJobPO> offlineJobs);

  /**
   * 批量获取离线任务要处理的一批数据
   */
  List<?> getJobData(long begin, long end, int batchSize, Class<?> persistentClass, String jobId)
      throws ServiceException;

  /**
   * 开启/禁用任务
   *
   * @param jobId 任务id
   * @param enable 开启或禁用（true or false）
   * @return 更新后的任务页面数据对象OfflineJobVO
   * @throws ServiceException 更新数据出错时抛出此异常
   */
  OfflineJobVO enableJob(@NotNull String jobId, @NotNull boolean enable) throws ServiceException;

  /**
   * 更新离线任务信息，不过只更新值发生改变的字段属性，null值和默认值会被忽略
   *
   * @param offlineJobVO
   * @return
   * @throws ServiceException
   */
  OfflineJobVO updateWithOnlyChanged(OfflineJobVO offlineJobVO) throws ServiceException;

  /**
   * 通过id获取任务信息
   *
   * @param jobId
   * @return
   */
  OfflineJobPO getById(@NotNull String jobId);

  /**
   * 查询是否有配置对应的数据源
   *
   * @param dbSessionEnum
   * @return 有返回true，没有返回false
   */
  boolean hasDbSessionEnum(DBSessionEnum dbSessionEnum);

  /**
   * 查询任务是否已停止执行，此方法是查询数据库任务表记录状态
   *
   * @param jobId
   * @return 当enable为false或者enable为true且status为 {@link JobStatusEnum#FAILED}、{@link
   * JobStatusEnum#PROCESSED}时，返回true，否则返回false
   */
  boolean isStopped(String jobId) throws ServiceException;
}
