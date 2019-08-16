package com.smate.sie.center.open.service.valisent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;
import com.smate.sie.core.base.utils.dao.validate.KpiPayProjectDao;
import com.smate.sie.core.base.utils.dao.validate.KpiPayValidateDao;
import com.smate.sie.core.base.utils.dao.validate.KpiPayValidateUserDao;
import com.smate.sie.core.base.utils.model.validate.KpiPayProject;
import com.smate.sie.core.base.utils.model.validate.KpiPayValidate;
import com.smate.sie.core.base.utils.model.validate.KpiPayValidateUser;

/**
 * 付费验证服务实现
 * 
 * @author xr
 * @date 2019年8月1日
 */
@Service("kpiPaymentValidateService")
@Transactional(rollbackFor = Exception.class)
public class KpiPaymentValidateServiceImpl implements KpiPaymentValidateService {

  @Autowired
  private KpiPayValidateDao kpiPayValidateDao;
  @Autowired
  private KpiPayValidateUserDao kpiPayValidateUserDao;
  @Autowired
  private KpiPayProjectDao kpiPayProjectDao;
  @Autowired
  private SieInsPersonDao sieInsPersonDao;

  /**
   * 开题分析付费验证
   */
  @Override
  public Map<String, Object> paymentAnalysisVarification(Long psnId, String ip) {
    // 目前开题分析是免费的,提供截止时间为2099-12-31 23:59:59 ROL-7521
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("status", "1");
    data.put("endTime", "2099-12-31 23:59:59");
    return data;
  }

  /**
   * 科研验证付费验证
   */
  @Override
  public Map<String, Object> paymentValidateVarification(Long psnId, String ip) {
    Map<String, Object> data = new HashMap<String, Object>();
    // 根据ip进行判断是否为已开通科研验证的单位下的访问ip
    if (StringUtils.isNotBlank(ip)) {
      Map<String, String> dataPermitIp = this.matchPermitIp(ip);
      if ("true".equals(dataPermitIp.get("flag"))) {
        data.put("status", "1");
        data.put("endTime", dataPermitIp.get("endTime"));
        return data;
      }
    }
    // 验证用户所在的单位是否开通科研验证
    List<Long> insIds = sieInsPersonDao.findPsnInsIds(psnId);
    if (CollectionUtils.isNotEmpty(insIds)) {
      List<KpiPayValidate> list = kpiPayValidateDao.judgementPaymentByInsIds(insIds);
      if (CollectionUtils.isNotEmpty(list)) {
        Date endTime = list.get(0).getEndDate();
        String smTimes = "";
        if (endTime != null) {
          DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          smTimes = format.format(endTime);
        }
        data.put("status", "1");
        data.put("endTime", smTimes);
        return data;
      }
    }
    // 验证科研验证个人付费表,该用户个人是否付费科研验证
    KpiPayValidateUser kpiPayValidateUser = kpiPayValidateUserDao.judgementPaymentListByPsnId(psnId);
    if (kpiPayValidateUser != null) {
      Date endTime = kpiPayValidateUser.getEndDate();
      String smTimes = "";
      if (endTime != null) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        smTimes = format.format(endTime);
      }
      data.put("status", "1");
      data.put("endTime", smTimes);
      return data;
    }
    data.put("status", "0");
    data.put("endTime", "");
    return data;
  }

  /**
   * 项目助理付费验证
   */
  @Override
  public Map<String, Object> paymentProjectVarification(Long psnId, String ip) {
    Map<String, Object> data = new HashMap<String, Object>();
    List<Long> insIds = sieInsPersonDao.findPsnInsIds(psnId);
    if (CollectionUtils.isNotEmpty(insIds)) {
      List<KpiPayProject> list = kpiPayProjectDao.judgementPaymentByInsIds(insIds);
      if (CollectionUtils.isNotEmpty(list)) {
        Date endTime = list.get(0).getEndDate();
        String smTimes = "";
        if (endTime != null) {
          DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          smTimes = format.format(endTime);
        }
        data.put("status", "1");
        data.put("endTime", smTimes);
        return data;
      }
    }
    data.put("status", "0");
    data.put("endTime", "");
    return data;
  }



  // 验证Ip是否允许 (true允许)
  private Map<String, String> matchPermitIp(String userIp) {
    Boolean flag = false;
    Map<String, String> data = new HashMap<String, String>();
    List<KpiPayValidate> listByIp = kpiPayValidateDao.judgementPaymentListByIP();
    if (CollectionUtils.isNotEmpty(listByIp)) {
      for (KpiPayValidate kpiPayValidate : listByIp) {
        if (StringUtils.isNotBlank(kpiPayValidate.getPermitIP())) {
          String[] ipList = kpiPayValidate.getPermitIP().split(";");
          for (String ip : ipList) {
            int num = this.countStr(ip, ".");
            if (num == 3) {
              if (userIp.contains(ip)) {
                flag = true;
                data.put("flag", "true");
                break;
              }
            } else if (num == 2) {
              if (userIp.contains(ip + ".")) {
                flag = true;
                data.put("flag", "true");
                break;
              }
            }
          }
          if (flag) {
            Date endTime = kpiPayValidate.getEndDate();
            String smTimes = "";
            if (endTime != null) {
              DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              smTimes = format.format(endTime);
            }
            data.put("endTime", smTimes);
            break;
          }
        }
      }
    } else {
      data.put("flag", "false");
      data.put("endTime", "");
    }
    return data;
  }

  private int countStr(String str, String sToFind) {
    int num = 0;
    while (str.contains(sToFind)) {
      str = str.substring(str.indexOf(sToFind) + sToFind.length());
      num++;
    }
    return num;
  }
}
