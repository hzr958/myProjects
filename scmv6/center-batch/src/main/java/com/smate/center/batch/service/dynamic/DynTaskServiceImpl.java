package com.smate.center.batch.service.dynamic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.dynamic.InspgDynamicRefreshDao;
import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;



/**
 * 动态刷新ServiceImpl
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
@Transactional(rollbackFor = Exception.class)
public class DynTaskServiceImpl implements DynTaskService {

  private List<ExecuteTaskService> executeChain = null; // 动态刷新链(找匹配的执行)

  @Autowired
  private InspgDynamicRefreshDao inspgDynamicRefreshDao;


  @Override
  public void executeTask(Integer size) throws Exception {

  }

  /**
   * 获取动态刷新列表
   */
  public List<InspgDynamicRefresh> getRefreshListBySize(Integer size) throws Exception {
    List<InspgDynamicRefresh> result = inspgDynamicRefreshDao.getDynList(size);
    return result;
  }

  /**
   * 动态出错
   */
  public void saveErrorData(InspgDynamicRefresh refresh) throws Exception {
    inspgDynamicRefreshDao.save(refresh);
  }

  /**
   * 执行动态刷新(数据构建保存)
   */
  @Override
  public void executeDyn(InspgDynamicRefresh object) throws Exception {
    if (executeChain != null) {
      for (ExecuteTaskService chain : executeChain) {
        if (chain.isThisDyn(object)) {
          chain.build(object);
          return;
        }
      }
    }
  }

  public List<ExecuteTaskService> getExecuteChain() {
    return executeChain;
  }

  public void setExecuteChain(List<ExecuteTaskService> executeChain) {
    this.executeChain = executeChain;
  }

}
