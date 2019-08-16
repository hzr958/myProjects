package com.smate.center.oauth.service.psnregister;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javassist.expr.NewArray;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.dao.profile.ScmRegisterVerifCodeDao;
import com.smate.center.oauth.model.profile.PersonRegisterForm;
import com.smate.center.oauth.model.profile.ScmRegisterVerifCode;
import com.smate.core.base.email.dao.MailInitDataDao;
import com.smate.core.base.email.model.MailInitData;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.dao.msg.MessageLogDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.service.msg.MessageSendService;

/**
 * 
 * @author LJ 科研之友APP客户端注册业务实现 2017年6月21日
 */
@Service("appPersonRegisterService")
@Transactional(rollbackFor = Exception.class)
public class AppPersonRegisterServiceImpl implements AppPersonRegisterService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailInitDataDao mailInitDataDao;
  @Autowired
  ScmRegisterVerifCodeDao scmRegisterVerifCodeDao;
  @Autowired
  private UserDao userDao;
  @Autowired
  PersonRegisterService personRegisterService;
  @Autowired
  private MessageSendService messageSendService;
  @Autowired
  MessageLogDao messageLogDao;
  private int countlimit = 3;// 短信发送次数限制
  private int timelimit = 30;// 验证码有效期限制 （分钟）

  @Override
  public String sendverifiMail(String emailAddress) throws Exception {
    Map<String, Object> params = new HashMap<String, Object>();
    String code = this.generateVerifCode();
    params.put("code", code);
    params.put("timelimit", timelimit + "分钟");
    getInfo(params, emailAddress);
    saveMailInitData(params, emailAddress);

    // 保存或更新验证码
    ScmRegisterVerifCode verifi = scmRegisterVerifCodeDao.getInfoByAccount(emailAddress);
    if (verifi == null) {
      verifi = new ScmRegisterVerifCode();
      verifi.setAccount(emailAddress);
      verifi.setCode(code);
      verifi.setCreatedDate(new Date());
    } else {
      verifi.setUpdateDate(new Date());
      verifi.setCode(code);
    }
    scmRegisterVerifCodeDao.save(verifi);

    return code;

  }

  @Override
  public String sendverifiMessage(String phonenumber) throws Exception {
    String code = this.generateVerifCode();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String content = "【科研之友】您的科研之友账号注册验证码为：" + code + ",有效期" + timelimit + "分钟（生效时间：" + sdf.format(new Date()) + "）。";
    // 保存或更新验证码
    ScmRegisterVerifCode verifi = scmRegisterVerifCodeDao.getInfoByAccount(phonenumber);
    int sendCount = messageLogDao.getSendCount(phonenumber);
    // 判断发送次数
    if (sendCount >= countlimit) {
      return "limitcount";
    }
    try {
      messageSendService.sendMessage(phonenumber, content);

    } catch (Exception e) {
      throw new Exception("验证短信发送失败！", e);
    }
    if (verifi == null) {
      verifi = new ScmRegisterVerifCode();
      verifi.setAccount(phonenumber);
      verifi.setCode(code);
      verifi.setCreatedDate(new Date());
    } else {
      verifi.setUpdateDate(new Date());
      verifi.setCode(code);
    }
    scmRegisterVerifCodeDao.save(verifi);

    return code;
  }

  /**
   * 保存邮件信息
   * 
   * @param dataMap
   * @param emailAddress
   */
  public void saveMailInitData(Map<String, Object> dataMap, String emailAddress) {
    MailInitData mid = new MailInitData();
    mid.setCreateDate(new Date());
    mid.setFromNodeId(1);
    mid.setMailData(JacksonUtils.jsonObjectSerializer(dataMap));
    mid.setStatus(1);
    mid.setToAddress(emailAddress);
    mailInitDataDao.saveMailData(mid);
  }

  /**
   * 获取邮件数据需要的基本信息
   * 
   * @param params
   * @param emailAddress
   * @throws Exception
   */
  private void getInfo(Map<String, Object> params, String emailAddress) throws Exception {
    String subject = "";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    subject = "科研之友用户注册验证(" + sdf.format(new Date()) + ")";
    params.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
    // 暂时未发送
    String template = "SCM_User_Register_zh_CN.ftl";

    params.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, emailAddress);
    params.put(EmailConstants.EMAIL_TEMPLATE_KEY, template);
    params.put(EmailConstants.EMAIL_PRIOR_CODE_KEY, 9);
  }



  /**
   * 生成注册验证码
   * 
   * @return
   * @throws ServiceException
   */
  private String generateVerifCode() throws Exception {
    try {
      char[] numAndLetterStr = "0123456789".toCharArray();
      StringBuffer strBuffer = new StringBuffer();
      Random random = new Random();
      for (int i = 0; i < 6; i++) {
        strBuffer.append(numAndLetterStr[random.nextInt(9)]);
      }
      return strBuffer.toString();
    } catch (Exception e) {
      logger.error("生成注册验证码出现异常", e);
      throw new Exception("生成注册验证码出现异常", e);
    }
  }

  @Override
  public ScmRegisterVerifCode getInfoByAccount(String email) throws Exception {
    return scmRegisterVerifCodeDao.getInfoByAccount(email);
  }

  @Override
  public Boolean checkUserName(String lowerCase) throws Exception {
    return personRegisterService.findIsEmail(lowerCase);
  }

  @Override
  public int checkCode(String email, String verifCode) throws Exception {
    ScmRegisterVerifCode codeinfo = scmRegisterVerifCodeDao.getInfoByAccount(email);
    codeinfo.setCount(codeinfo.getCount() + 1);

    if (codeinfo.getUpdateDate() == null) {
      // 首次验证
      if (checkExpired(codeinfo.getCreatedDate()) == true) {
        codeinfo.setStatus(2);
        scmRegisterVerifCodeDao.save(codeinfo);
        return 2;// 验证码已过期
      } else if (verifCode.equals(codeinfo.getCode())) {
        codeinfo.setStatus(1);
        scmRegisterVerifCodeDao.save(codeinfo);
        return 1;// 验证码正确
      } else {
        codeinfo.setStatus(0);
        scmRegisterVerifCodeDao.save(codeinfo);
        return 0;// 验证码错误
      }
    } else {
      // 再次验证
      if (checkExpired(codeinfo.getUpdateDate()) == true) {
        codeinfo.setStatus(2);
        scmRegisterVerifCodeDao.save(codeinfo);
        return 2;// 验证码已过期
      } else if (verifCode.equals(codeinfo.getCode())) {
        codeinfo.setStatus(1);
        scmRegisterVerifCodeDao.save(codeinfo);
        return 1;// 验证码正确
      }
    }

    codeinfo.setStatus(0);
    scmRegisterVerifCodeDao.save(codeinfo);
    return 0;

  }

  /**
   * 验证时间
   * 
   * @param rectime
   * @return
   */
  public boolean checkExpired(Date rectime) {
    Long time = new Date().getTime();
    Long updatetime = rectime.getTime();
    Long minute = (time - updatetime) / (1000 * 60);
    if (minute > timelimit) {
      return true;// 验证码已过期
    }
    return false;

  }

  @Override
  public long saveUserInfo(PersonRegisterForm form) throws Exception {

    return personRegisterService.registerPerson(form);

  }

  @Override
  public void checkUserInfo(PersonRegisterForm form) throws Exception {

    form.setName("科研之友");
    form.setIsRegisterR(true);
    form.setEmail(form.getEmail().toLowerCase());
    boolean IsEmail = personRegisterService.findIsEmail(form.getEmail());
    if (IsEmail == false) {
      throw new Exception("账户已存在！");
    }

    // 对密码进行MD5加密，后面调用center-open的人员注册服务传参需要
    form.setNewpassword(DigestUtils.md5Hex(form.getNewpassword()));
    ScmRegisterVerifCode codeinfo = scmRegisterVerifCodeDao.getInfoByAccount(form.getEmail());
    Boolean flag;
    // 判断当前验证码是否已经过期
    if (codeinfo.getUpdateDate() == null) {
      flag = checkExpired(codeinfo.getCreatedDate());
    } else {
      flag = checkExpired(codeinfo.getUpdateDate());
    }
    // 获取验证状态
    Integer statusByAccount = codeinfo.getStatus();
    if (statusByAccount != 1 || flag == true) {
      throw new Exception("验证码验证状态异常！");
    }

  }

}
