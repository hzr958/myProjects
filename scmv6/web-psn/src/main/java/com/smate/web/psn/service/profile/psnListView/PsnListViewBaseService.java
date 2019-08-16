package com.smate.web.psn.service.profile.psnListView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.friend.PsnListViewForm;


@Transactional(rollbackFor = Exception.class)
public abstract class PsnListViewBaseService implements PsnListViewService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 具体服务类各自实现-------校验必要的参数
   * 
   * @param parameterData
   * @return
   */
  public abstract String doVerifyData(PsnListViewForm form);

  /**
   * 具体服务类各自实现-------进行各自的逻辑处理
   * 
   * @param parameterData
   * @return
   * @throws PsnBuildException
   */

  public abstract void doGetPsnListViewInfo(PsnListViewForm form) throws ServiceException, PsnBuildException;

  @Override
  public void getPsnListViewInfo(PsnListViewForm form) throws ServiceException {
    try {
      String verifyResult = this.doVerifyData(form);
      // 必要的参数校验通过
      if (verifyResult == null) {
        // 进行数据处理
        this.doGetPsnListViewInfo(form);
      } else {
        // 必要的参数校验没有通过
        logger.error("缺少必要的参数");
        throw new ServiceException("缺少必要的参数，serviceType=" + form.getServiceType() + ", verifyResult=" + verifyResult);
      }
    } catch (Exception e) {
      logger.error("获取人员列表信息出错", e);
      throw new ServiceException(e);
    }
  }

}
