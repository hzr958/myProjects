package com.smate.web.psn.service.profile.psnListView;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 人员列表服务实现类------根据服务类型调用具体服务
 *
 * @author wsn
 *
 */
@Transactional(rollbackFor = Exception.class)
public class PsnListViewServiceImpl extends PsnListViewBaseService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());


  /*
   * 人员列表服务Map，key为服务类型，value为具体的服务对象, 在applicationContext-server.xml中注入了
   */
  private Map<String, PsnListViewService> serviceMap;



  @Override
  public String doVerifyData(PsnListViewForm form) {
    // 校验是否有服务类型参数
    String verifyResult = null;
    if (StringUtils.isNotBlank(form.getServiceType())) {
      // 根据服务类型获取具体的服务对象
      PsnListViewService service = serviceMap.get(form.getServiceType());
      if (service == null) {
        verifyResult = "there is not such serviceType";
      }
    } else {
      verifyResult = "serviceType is miss";
    }
    return verifyResult;
  }

  @Override
  public void doGetPsnListViewInfo(PsnListViewForm form) throws ServiceException {
    // 调用上面的校验参数方法
    String verifyResult = doVerifyData(form);
    // 参数验证通过后根据服务类型参数获取具体的服务对象
    if (verifyResult == null) {
      // 调用具体服务对象的数据处理方法
      PsnListViewService service = serviceMap.get(form.getServiceType());
      if (service != null) {
        service.getPsnListViewInfo(form);
      } else {
        logger.error("没有找到对应的服务对象， serviceType=" + form.getServiceType());
        throw new ServiceException("没有找到对应的服务对象， serviceType=" + form.getServiceType());
      }
    } else {
      logger.error("参数校验出错：" + verifyResult);
      throw new ServiceException("参数校验出错：" + verifyResult);
    }
  }

  public Map<String, PsnListViewService> getServiceMap() {
    return serviceMap;
  }

  public void setServiceMap(Map<String, PsnListViewService> serviceMap) {
    this.serviceMap = serviceMap;
  }


}
