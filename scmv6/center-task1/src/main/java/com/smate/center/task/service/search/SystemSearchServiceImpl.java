package com.smate.center.task.service.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.quartz.PdwhIndexPublication;
import com.smate.center.task.model.pub.seo.PubIndexFirstLevel;
import com.smate.center.task.model.search.UserSearchDataForm;
import com.smate.center.task.model.search.UserSearchResultForm;
import com.smate.center.task.service.pdwh.quartz.PublicationAllService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.service.person.PersonManager;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.MapBuilder;

/**
 * 站外检索定时器任务处理业务逻辑实现类.
 * 
 * @author mjg
 * 
 */
@Service("systemSearchService")
@Transactional(rollbackFor = Exception.class)
public class SystemSearchServiceImpl implements SystemSearchService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SystemSearchBaseService systemSearchBaseService;
  @Value("${indexfile.root}")
  private String fileRoot;// 获取文件根路经
  @Autowired
  private PersonManager personManager;
  @Autowired
  private SystemSeoSearch systemSeoSearch;
  @Autowired
  private PublicationAllService publicationAllService;
  @Autowired
  private UserSearchService userSearchService;
  @Autowired
  private TaskMarkerService taskMarkerService;

  /**
   * 处理站外成果检索的数据
   */
  @Override
  public void loadIndexPubInfo() throws ServiceException {
    try {
      boolean isDel = false;
      // 判断是否要删除三张目录表 删除之后 之后的每次删除可以取消
      if (taskMarkerService.getApplicationQuartzSettingValue("indexInfoInitTask") == 1) {
        systemSeoSearch.delDir();
        isDel = true;
      }
      // 获取26个英文字母列表.
      List<String> codeList = systemSearchBaseService.getCodeList();
      for (String code : codeList) {
        if (logger.isDebugEnabled()) {
          logger.debug("_______________________code开始:___________________" + code);
        }
        // 防止事务执行时间过长导致远程服务链接中断.
        Long count = publicationAllService.getCount(code);
        // 每页显示200个成果
        Integer num = (int) Math.ceil((double) count / 200);// 有余数向上取整
        for (int i = 0; i < 200; i++) {
          List<PdwhIndexPublication> iPubList = publicationAllService.getPubByIndexCode(code, i * num, num);
          if (iPubList.size() <= 0) // 结束循环
            break;
          try {
            systemSeoSearch.pubFilter(code, i + 1, iPubList, num, isDel);
          } catch (Exception e) {
            logger.error("处理站外成果检索的数据-----code" + code, e);
          }
        }
        List<PubIndexFirstLevel> codePubList = systemSeoSearch.getPubByCode(code);
        // 构建站外检索成果内容.
        systemSearchBaseService.buildPubPageCon(fileRoot, code, codePubList);
      }
    } catch (Exception e) {
      logger.error("处理站外成果检索的数据", e);
      throw new ServiceException("处理站外成果检索的数据", e);
    }
  }

  /**
   * 加载站外检索人员信息_MJG_SCM-2792.
   * 
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  @Override
  public void loadIndexPsnInfo() throws ServiceException {
    // 获取用户列表，并存入缓存.
    Map<String, List<UserSearchResultForm>> userZhMap = MapBuilder.getInstance().getMap();
    Map<String, List<UserSearchResultForm>> userEnMap = MapBuilder.getInstance().getMap();
    // 获取26个英文字母列表.
    List<String> codeList = systemSearchBaseService.getCodeList();
    try {
      for (String code : codeList) {
        // 防止事务执行时间过长导致远程服务链接中断.
        // 获取用户列表.
        List<UserSearchResultForm> iUserZhList = this.getIndexUser(code, SystemSearchBaseService.LOCALE_ZH);
        userZhMap.put(code, iUserZhList);
        List<UserSearchResultForm> iUserEnList = this.getIndexUser(code, SystemSearchBaseService.LOCALE_EN);
        userEnMap.put(code, iUserEnList);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException("加载站外检索人员信息出现错误!", e);
    }
    // 构建站外检索人员内容.
    systemSearchBaseService.buildPsnPageCon(fileRoot, SystemSearchBaseService.LOCALE_ZH, userZhMap);
    systemSearchBaseService.buildPsnPageCon(fileRoot, SystemSearchBaseService.LOCALE_EN, userEnMap);
  }

  /**
   * 封装获取站外检索人员的信息列表.
   * 
   * @param code
   * @return
   * @throws ServiceException
   */
  private List<UserSearchResultForm> getIndexUser(String code, String locale) throws ServiceException {
    List<UserSearchResultForm> iResultList = null;
    // 获取人员信息列表.
    List<UserSearchDataForm> iUserList = userSearchService.findIndexUserList(code, locale, INDEX_QUERY_MAX_LIMIT);
    if (CollectionUtils.isNotEmpty(iUserList)) {
      iResultList = new ArrayList<UserSearchResultForm>();
      List<Long> psnIdList = new ArrayList<Long>();
      for (int i = 0, size = iUserList.size(); i < size; i++) {
        UserSearchDataForm dataForm = iUserList.get(i);
        Long psnId = dataForm.getPsnId();
        psnIdList.add(psnId);
      }

      // 获取检索人员的基本信息.
      UserSearchResultForm searchResultForm = null;
      // 获取遍历节点上的对应人员记录.
      List<Person> personList = personManager.findPersonList(psnIdList);
      for (Person person : personList) {
        searchResultForm = new UserSearchResultForm();
        searchResultForm.setPsnId(person.getPersonId());
        searchResultForm.setZhInfo(getPsnNameByLang(person, 1));
        searchResultForm.setEnInfo(getPsnNameByLang(person, 2));
        iResultList.add(searchResultForm);
      }
    }
    return iResultList;
  }

  /**
   * 获取姓名信息.
   * 
   * @param person
   * @param langFlag
   * @return
   */
  private String getPsnNameByLang(Person person, int langFlag) {
    if (person == null) {
      return null;
    }
    String psnName = "";
    if (langFlag == 1) {
      psnName = person.getName();
      if (StringUtils.isBlank(psnName)) {
        psnName = person.getFirstName() + " " + person.getLastName();
      }
    } else {
      psnName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) && StringUtils.isBlank(person.getLastName())) {
        psnName = person.getName();
      }
    }
    return psnName;
  }
}
