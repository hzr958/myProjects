package com.smate.center.task.service.email;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.sys.ScmSystemUtil;

/**
 * 推荐研究领域的业务逻辑实现类<获取封装发送推荐邮件的参数逻辑>.
 * 
 * @author mjg
 * 
 */
@Service("basePubRecmdService")
public class BaseRaRecmdServiceImpl implements BaseRaRecmdService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PromoteMailInitDataService promoteMailInitDataService;
  @Autowired
  private ScmSystemUtil scmSystemUtil;
  /**
   * 推荐研究领域.
   */
  final static String RESEARCHAREA_RECOMMEND = "mailEventLogByPubRecommend";

  /**
   * 封装邮件记录信息.
   * 
   * @param person
   * @param refListSize
   * @param params
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  @Override
  public void buildMailLogEntity(Person person, List<String> rKwList)
      throws ServiceException, UnsupportedEncodingException {
    try {
      Map params = this.assemMailParams(person, rKwList);
      promoteMailInitDataService.saveMailInitData(params);
    } catch (Exception e) {
      logger.error("封装邮件记录信息", e);
      throw new ServiceException("封装邮件记录信息", e);
    }
  }

  /**
   * 封装邮件模版需要的参数.
   * 
   * @param person
   * @param reRefList
   * @return
   * @throws UnsupportedEncodingException
   * @throws ServiceException
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map assemMailParams(Person person, List<String> rKwList)
      throws UnsupportedEncodingException, ServiceException {
    Map params = new HashMap();
    params.put(MAIL_PARAM_KEY_PSN_ID, person.getPersonId());
    // params.put(EMAIL_RECEIVEEMAIL, person.getEmail());
    params.put(EMAIL_RECEIVEEMAIL, "zzx123@qq.com");
    params.put(MAIL_PARAM_KEY_PSN_NAME,
        StringUtils.isBlank(person.getName()) ? person.getFirstName() + " " + person.getLastName() : person.getName());
    String email2Log = getEmail2Log(RESEARCHAREA_RECOMMEND, this.getEmail2LogParam(person.getPersonId()));
    String operateUrl = scmSystemUtil.getSysDomain() + "/psnweb/homepage/show?menuId=1200&email2log="
        + ServiceUtil.encodeToDes3(email2Log);
    params.put(MAIL_PARAM_KEY_OPERAT_URL, operateUrl);
    String template = RESEACHAREA_RECMD_TEMPLATE + "_" + LOCALE_ZH_CN + TEMPLATE_SUFFIX;
    params.put(EMAIL_TEMPLATE_KEY, template);
    String kwSize = String.valueOf(rKwList.size() >= 5 ? 5 : rKwList.size());
    String subject = MAIL_TITLE_CON.replace("{0}", kwSize);
    subject = subject.replace("{name}", person.getName());
    params.put(EMAIL_SUBJECT_KEY, subject);
    params.put(MAIL_PARAM_LIST_KEY, rKwList.subList(0, rKwList.size() > 5 ? 5 : rKwList.size()));
    params.put(MAIL_PARAM_LIST_SIZE, rKwList.size());
    return params;
  }

  /**
   * 组成: a、(AutologinUrl)&service=(目标url[带menuId]+email2log参数)
   * b、email2log参数（用于记录登录日志，每个邮件模板都有独自的类进行日志处理） 组成: b1:beanName|psnid=xxx,urlid=xxx
   * 
   * 注意
   */
  @SuppressWarnings("rawtypes")
  public String getEmail2Log(String beanName, Map logParams) throws ServiceException {

    // beanName为空，logParams为空，直接返回null
    if (StringUtils.isBlank(beanName) || logParams == null || logParams.isEmpty())
      return null;
    Iterator it = logParams.keySet().iterator();
    StringBuffer sb = new StringBuffer();
    // 获取beanName
    sb.append(beanName + "|");

    while (it.hasNext()) {
      Object obj = it.next();
      sb.append(obj + "=" + logParams.get(obj) + ",");
    }

    return sb.toString();
  }

  /**
   * 获取封装email2Log所需参数.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  private Map<String, Object> getEmail2LogParam(Long personId) {
    Map<String, Object> emailLogParams = MapBuilder.getInstance().getMap();
    emailLogParams.put("psnid", personId);
    emailLogParams.put("urlid", 1);
    return emailLogParams;
  }

}
