package com.smate.sie.web.application.service.validate;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.sie.core.base.utils.dao.validate.KpiPayValidateUserDao;
import com.smate.sie.core.base.utils.model.validate.KpiPayValidateUser;
import com.smate.sie.web.application.dao.validate.KpiPayValidateInvoiceDao;
import com.smate.sie.web.application.dao.validate.KpiPayValidateUserLogDao;
import com.smate.sie.web.application.model.validate.KpiPayValidateInvoice;
import com.smate.sie.web.application.model.validate.KpiPayValidateUserLog;
import com.smate.sie.web.application.model.validate.KpiValidateVipVo;

/**
 * 人员拥有的付费记录服务
 * 
 * @author wsn
 * @date Feb 26, 2019
 */
@Service("kpiValidatePayService")
@Transactional(rollbackFor = Exception.class)
public class KpiValidatePayServiceImpl implements KpiValidatePayService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private KpiPayValidateUserDao kpiPayValidateUserDao;
  @Autowired
  private KpiPayValidateUserLogDao kpiPayValidateUserLogDao;
  @Autowired
  private KpiPayValidateInvoiceDao kpiPayValidateInvoiceDao;
  @Autowired
  private PersonDao personDao;

  @Override
  public void saveOrUpdatePayInfo(String orderNum) throws ServiceException {
    KpiPayValidateUserLog log = this.savePayLog(orderNum);
    if (log != null) {
      Long psnId = log.getPsnId();
      KpiPayValidateUser info = kpiPayValidateUserDao.get(psnId);
      if (info == null) {
        info = new KpiPayValidateUser();
        info.setPsnId(psnId);
      }
      Calendar endDate = Calendar.getInstance();
      Date currentDate = new Date();
      endDate.setTime(currentDate);
      // 持续时间
      switch (log.getGrade()) {
        case "A":
          endDate.add(Calendar.MONTH, 12);
          break;
        case "B":
          endDate.add(Calendar.MONTH, 24);
          break;
        case "C":
          endDate.add(Calendar.MONTH, 1);
          break;
        default:
          break;
      }
      info.setStartDate(currentDate);
      info.setEndDate(endDate.getTime());
      info.setGrade(log.getGrade());
      kpiPayValidateUserDao.save(info);
    }

  }

  @Override
  public boolean checkPayStatus(String orderNum) throws ServiceException {
    KpiPayValidateUserLog log = kpiPayValidateUserLogDao.findPayLogByOrderNum(orderNum);
    if (log != null && log.getStatus() == 1) {
      return true;
    }
    return false;
  }

  protected KpiPayValidateUserLog savePayLog(String orderNum) {
    // 根据订单号查询订单状态，如果还未支付，则更新为已支付，并升级用户账号
    KpiPayValidateUserLog log = kpiPayValidateUserLogDao.findPayLogByOrderNum(orderNum);
    if (log != null && log.getStatus() == 0) {
      log.setStatus(1);
      log.setPayDate(new Date());
      kpiPayValidateUserLogDao.save(log);
    }
    return log;
  }

  @Override
  public String savePrePayLog(KpiValidateVipVo vo) {
    String orderNum = vo.getOrderNum();
    KpiPayValidateUserLog log = kpiPayValidateUserLogDao.findPayLogByOrderNum(orderNum);
    if (log == null) {
      log = new KpiPayValidateUserLog();
      log.setCreateDate(new Date());
      log.setOrderNum(orderNum);
      log.setPsnId(vo.getPsnId());
      log.setTradeType(vo.getTradeType());
      log.setStatus(0);
      log.setGrade(vo.getGrade());
      log.setOrderTotalAmount(vo.getPrice());
      log.setNeedInvoices(vo.getNeedInvoices() != null ? vo.getNeedInvoices() : 0);
      kpiPayValidateUserLogDao.save(log);
      vo.setLogId(log.getId());
      return "success";
    }
    return "error";
  }

  @Override
  public boolean checkOrderTotalFee(String orderNum, double returnTotalFee) {
    KpiPayValidateUserLog log = kpiPayValidateUserLogDao.findPayLogByOrderNum(orderNum);
    if (log != null) {
      double orderFee = log.getOrderTotalAmount();
      if (NumberUtils.compare(orderFee, returnTotalFee) == 0) {
        return true;
      }
    }
    return false;
  }



  @Override
  public void savePayInvoicesInfo(KpiValidateVipVo Vo) {
    KpiPayValidateInvoice invoices = new KpiPayValidateInvoice();
    invoices.setInvoicesType(Vo.getInvoicesType());
    invoices.setTitle(Vo.getTitle());
    invoices.setUniformId(Vo.getUniformId());
    invoices.setLogId(Vo.getLogId());
    if (CommonUtils.compareIntegerValue(Vo.getInvoicesType(), 2)) {
      invoices.setBank(Vo.getBank());
      invoices.setBankNO(Vo.getBankNO());
      invoices.setAddr(Vo.getAddr());
      invoices.setTel(Vo.getTel());
    }
    invoices.setStatus(0);
    invoices.setHasPaied(0);
    kpiPayValidateInvoiceDao.save(invoices);
  }

  @Override
  public void updatePayInvoicesPaied(String orderNum, Integer hasPaied) {
    KpiPayValidateUserLog log = kpiPayValidateUserLogDao.findPayLogByOrderNum(orderNum);
    if (log != null) {
      KpiPayValidateInvoice invoices = kpiPayValidateInvoiceDao.findKpiPayValidateInvoiceByLogId(log.getId());
      if (invoices != null) {
        invoices.setHasPaied(hasPaied);
      }
      kpiPayValidateInvoiceDao.save(invoices);
    }
  }

  @Override
  public Map<String, Object> getINvoiceNotificationInfo(String orderNum) {
    Map<String, Object> hashInput = null;
    KpiPayValidateUserLog log = kpiPayValidateUserLogDao.findPayLogByOrderNum(orderNum);
    if (log != null) {
      // 判断是否需要开发票并且是否已付款
      if (log.getNeedInvoices().equals(1) && log.getStatus().equals(1)) {
        hashInput = new HashMap<String, Object>();
        Long psnId = log.getPsnId();
        if (personDao.existsPerson(psnId) != null) {
          Person person = personDao.getPeronsForEmail(psnId);
          // String emailLanguageVersion = personDao.getEmailLanguByPsnId(psnId);
          KpiPayValidateInvoice invoice = kpiPayValidateInvoiceDao.findKpiPayValidateInvoiceByLogId(log.getId());
          // 订单金额
          Double amount = log.getOrderTotalAmount();
          // 内容参数
          hashInput.put("psn_name", person.getName());
          hashInput.put("email", person.getEmail());
          hashInput.put("invoice_type", invoice.getInvoicesType());
          hashInput.put("amt", amount);
          hashInput.put("title", invoice.getTitle());
          hashInput.put("uniform_id", invoice.getUniformId());
          hashInput.put("bank", invoice.getBank());
          hashInput.put("bank_no", invoice.getBankNO());
          hashInput.put("addr", invoice.getAddr());
          hashInput.put("tel", invoice.getTel());
          return hashInput;
        }
      }
    }
    return null;
  }

  @Override
  public void updatePayValidateInvoicesStutus(String orderNum, int status) {
    KpiPayValidateUserLog log = kpiPayValidateUserLogDao.findPayLogByOrderNum(orderNum);
    if (log != null) {
      KpiPayValidateInvoice invoices = kpiPayValidateInvoiceDao.findKpiPayValidateInvoiceByLogId(log.getId());
      if (invoices != null) {
        invoices.setStatus(status);
      }
      kpiPayValidateInvoiceDao.save(invoices);
    }

  }

}

