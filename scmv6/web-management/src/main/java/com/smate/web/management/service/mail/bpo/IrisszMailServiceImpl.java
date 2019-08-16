package com.smate.web.management.service.mail.bpo;

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
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.management.dao.mail.bpo.IrisszMailDao;
import com.smate.web.management.model.mail.bpo.IrisEmailAddr;
import com.smate.web.management.model.mail.bpo.IrisszMail;

@Service("irisszMailService")
@Transactional(rollbackFor = Exception.class)
public class IrisszMailServiceImpl implements IrisszMailService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private IrisszMailDao irisszMailDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;

  @Override
  public void sendIrisMail(IrisszMail irisszMail) throws Exception {
    try {
      if (irisszMail == null || StringUtils.isBlank(irisszMail.getDomain()))
        return;
      String domain = irisszMail.getDomain().toLowerCase();
      domain = domain.replace("http://", "");
      domain = domain.substring(0, domain.indexOf("/"));
      irisszMail.setDomain(domain);
      irisszMail.setCreateDate(new Date());
      irisszMailDao.save(irisszMail);
      // zk发送邮件,这个list里面的email是向公司人员发送的邮件地址
      List<IrisEmailAddr> irisEmails = irisszMailDao.getIrisEmails();
      if (CollectionUtils.isEmpty(irisEmails))
        return;

      for (IrisEmailAddr irisEmailAddr : irisEmails) {
        // if (domain.indexOf("citinethk") != -1 && irisEmailAddr.getEmail().indexOf("citinethk") != -1) {
        // email = irisEmailAddr.getEmail();
        // } else if (domain.indexOf("citinethk") == -1 && irisEmailAddr.getEmail().indexOf("iris") != -1) {
        String email = irisEmailAddr.getEmail();
        // }
        newSendIrisszContactEmail(irisszMail, email);
      }
    } catch (Exception e) {
      logger.error("接受irissz站点联系我们参数，发公司相关人员发送通知邮件出错", e);
    }
  }

  private void newSendIrisszContactEmail(IrisszMail irisszMail, String email) throws Exception {
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    Integer templateCode = 10058;// 模版标识，参考V_MAIL_TEMPLATE
    Long currentUserId = 0L;
    Long psnId = 0L;// 接收人psnId，0=非科研之友用户
    String msg = "客户咨询";// 描述
    List<Person> personList = personDao.getPersonByEmail(email);
    if (personList != null && personList.size() > 0) {
      psnId = personList.get(0).getPersonId();
    }
    mailHandleOriginalDataService.buildNecessaryParam(email, currentUserId, psnId, templateCode, msg, paramData);
    mailData.put("email", irisszMail.getEmail());
    mailData.put("tel", irisszMail.getTel());
    mailData.put("insName", irisszMail.getInsName());
    mailData.put("insScale", irisszMail.getInsScale());
    mailData.put("position", irisszMail.getPosition());
    mailData.put("address", irisszMail.getAddress());
    mailData.put("city", irisszMail.getCity());
    mailData.put("country", irisszMail.getCountry());
    mailData.put("remark", irisszMail.getRemark());
    mailData.put("from_zh_CN", "客户咨询");
    String name = irisszMail.getLastName() + " " + irisszMail.getFirstName();
    if (XmlUtil.isChinese(name)) {
      name = irisszMail.getFirstName() + " " + irisszMail.getLastName();
      name = name.replace(" ", "");
    }
    mailData.put("name", name);
    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(name);
    subjectParamLinkList.add(irisszMail.getDomain());
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
