package com.smate.web.management.service.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.service.MailHandleOriginalDataService;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.management.dao.mail.IrisszMailDetailDao;
import com.smate.web.management.dao.mail.bpo.IrisszMailDao;
import com.smate.web.management.model.mail.IrisszMailDetail;
import com.smate.web.management.model.mail.bpo.IrisEmailAddr;

@Service("irisszMailDetailService")
@Transactional(rollbackFor = Exception.class)
public class IrisszMailDetailServiceImpl implements IrisszMailDetailService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private IrisszMailDetailDao irisszMailDetailDao;
  @Autowired
  private IrisszMailDao irisszMailDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;

  @Override
  public void sendIrisMailDetail(IrisszMailDetail irisszMailDetail) throws Exception {
    try {
      if (irisszMailDetail == null || StringUtils.isBlank(irisszMailDetail.getDomain()))
        return;
      String domain = irisszMailDetail.getDomain().toLowerCase();
      domain = domain.replace("http://", "");
      domain = domain.substring(0, domain.indexOf("/"));
      irisszMailDetail.setDomain(domain);
      irisszMailDetail.setCreateDate(new Date());
      irisszMailDetailDao.save(irisszMailDetail);
      // 发送邮件,这个list里面的email是向公司人员发送的邮件地址
      List<IrisEmailAddr> irisEmails = irisszMailDao.getIrisEmails();
      if (CollectionUtils.isEmpty(irisEmails))
        return;

      for (IrisEmailAddr irisEmailAddr : irisEmails) {
        String email = irisEmailAddr.getEmail();
        newSendIrisszContactEmail(irisszMailDetail, email);
      }
    } catch (Exception e) {
      logger.error("接受公司官网 申请试用功能联系我们参数，发公司相关人员发送通知邮件出错", e);
    }
  }

  private void newSendIrisszContactEmail(IrisszMailDetail irisszMailDetail, String email) throws Exception {
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    Integer templateCode = 10113;// 模版标识，参考V_MAIL_TEMPLATE
    Long currentUserId = 0L;
    Long psnId = 0L;// 接收人psnId，0=非科研之友用户
    String msg = "客户咨询";// 描述
    List<Person> personList = personDao.getPersonByEmail(email);
    if (personList != null && personList.size() > 0) {
      psnId = personList.get(0).getPersonId();
    }
    mailHandleOriginalDataService.buildNecessaryParam(email, currentUserId, psnId, templateCode, msg, paramData);
    mailData.put("email", irisszMailDetail.getEmail());
    mailData.put("tel", irisszMailDetail.getTel());
    mailData.put("insName", irisszMailDetail.getInsName());
    mailData.put("address", irisszMailDetail.getAddress());
    mailData.put("remark", irisszMailDetail.getRemark());
    mailData.put("name", irisszMailDetail.getName());
    Integer type = irisszMailDetail.getType();// 类别 1.科技管理;2.成果推广;3.技术转移
    String typeDesc = "";
    if (type != null) {
      if (type == 1) {
        typeDesc = "科技管理";
      } else if (type == 2) {
        typeDesc = "成果推广";
      } else if (type == 3) {
        typeDesc = "技术转移";
      }
    }
    mailData.put("type", typeDesc);
    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(irisszMailDetail.getName());
    subjectParamLinkList.add(irisszMailDetail.getDomain());
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    Map<String, String> resutlMap = mailHandleOriginalDataService.doHandle(paramData);
    if ("success".equals(resutlMap.get("result"))) {
      // 构造邮件成功
    } else {
      // 构造邮件失败
      logger.error(resutlMap.get("msg"));
    }
  }

}
