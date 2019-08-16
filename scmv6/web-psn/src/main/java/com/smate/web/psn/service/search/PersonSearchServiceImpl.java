package com.smate.web.psn.service.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.web.psn.action.search.PersonSearchForm;
import com.smate.web.psn.build.factory.PsnInfoBuildFactory;
import com.smate.web.psn.build.factory.PsnInfoEnum;
import com.smate.web.psn.dao.profile.PsnDisciplineKeyDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.psninfo.PsnInfo;
import com.smate.web.psn.service.profile.PsnProfileUrlService;
import com.smate.web.psn.service.statistics.PsnStatisticsService;

/**
 * 人员检索服务实现
 * 
 * @author zk
 *
 */
@Service("personSearchService")
@Transactional(rollbackOn = Exception.class)
public class PersonSearchServiceImpl implements PersonSearchService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private PsnInfoBuildFactory psnInfoBuildFactory;
  @Autowired
  private PsnProfileUrlService psnProfileUrlService;
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Value("${domainscm}")
  private String serviceName;

  @Override
  public void searchPerson(PersonSearchForm form) throws PsnException {
    try {
      List<Long> psnIdList = psnDisciplineKeyDao.findPsnIdsForSearch(form);
      if (CollectionUtils.isNotEmpty(psnIdList)) {
        List<PsnInfo> psnInfoList = new ArrayList<PsnInfo>();
        for (Long psnId : psnIdList) {
          PsnInfo psnInfo = new PsnInfo(personDao.findPerson(psnId));
          if (psnInfo.getPerson() == null) {
            logger.warn("检索人员时，查询的结果psnId=" + psnId + "未在Person表找到对应记录");
            continue;
          }
          psnInfo.setPsnStatistics(psnStatisticsService.getPsnStatistics(psnId));
          psnInfo.setPrimaryIns(workHistoryDao.getPrimaryInsInfoByPsnId(psnId));
          psnInfoBuildFactory.buildPsnInfo(PsnInfoEnum.personSearch.toInt(), psnInfo);
          PsnProfileUrl obj = this.psnProfileUrlDao.find(psnId);
          if (obj != null) {
            psnInfo.setProfileUrl(serviceName + "/P/" + obj.getPsnIndexUrl());
          }
          // psnInfo.setProfileUrl(psnProfileUrlService.findUrl(psnId));
          // 清空原始数据
          psnInfo.setPerson(null);
          psnInfo.setPsnStatistics(null);
          psnInfo.setPrimaryIns(null);
          psnInfoList.add(psnInfo);
        }
        if (psnInfoList.size() > 0) {
          form.setPsnInfoList(psnInfoList);
        }
      }
    } catch (Exception e) {
      logger.error("人员检索整理人员数据出错,searchName=" + form.getSearchName() + ",searchKey=" + form.getSearchKey(), e);
    }
  }

  @Override
  public Map<String, Set<String>> getExtractUserAndInsName(String str) throws Exception {
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    str = str.replaceAll(";|,|，|；", " ");
    str = str.toLowerCase().trim().replaceAll("\\s+", "空格");
    // 直接使用，在服务器启动时加载
    Result kwRs = DicAnalysis.parse(str);
    Set<String> name = new TreeSet<String>();
    Set<String> ins = new TreeSet<String>();
    Map<String, Set<String>> mp = new HashMap<String, Set<String>>();
    for (Term t : kwRs.getTerms()) {
      if (t == null) {
        continue;
      }
      if ("scm_user_name".equals(t.getNatureStr()) || "nr".equals(t.getNatureStr())) {
        if (StringUtils.isNotEmpty(t.getName())) {
          name.add(t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim());
        }
      }
      if ("scm_ins_name".equals(t.getNatureStr())) {
        if (StringUtils.isNotEmpty(t.getName())) {
          ins.add(t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim());
        }
      }
    }
    if (name.size() > 0) {
      mp.put("scm_user_name", name);
    }
    if (ins.size() > 0) {
      mp.put("scm_ins_name", ins);
    }
    return mp;
  }

  public static void main(String[] args) {
    PersonSearchServiceImpl pss = new PersonSearchServiceImpl();
    String str =
        "来自天津大学的刘玉娇同学想找南开大学的jianyan huang同学问下关于刘玉存数据挖掘方面的问题,还有来自上海交通大学闵行校区的jianyan hu也说想要见一下中国农业大学的刘玉; 马建ma jian博士, Cornell Univeristy  City University of HongKong";
    String str1 = "ma jian, city university of hongkong";
    String str2 =
        "[Ye, Zeng Jie; Liang, Mu Zi; Li, Peng Fei] Guangzhou Univ Chinese Med, Guangzhou 510006, Guangdong, Peoples R China. [Liang, Mu Zi] Guangdong Acad Populat Dev, Guangzhou 510600, Guangdong, Peoples R China. [Zhang, Hao Wei] Harbin Med Univ Daqing, Daqing 163319, Heilongjiang, Peoples R China. [Ouyang, Xue Ren] Guangzhou Univ Chinese Med, Affiliated Hosp 1, Guangzhou 510405, Guangdong, Peoples R China. [Yu, Yuan Liang] South China Univ Technol, Guangzhou 510641, Guangdong, Peoples R China. [Liu, Mei Ling] Sun Yat Sen Univ, Ctr Canc, Guangzhou 510060, Guangdong, Peoples R China. [Qiu, Hong Zhong] Guangzhou Univ Chinese Med, Coll Econ %26 Management, Guangzhou 510006, Guangdong, Peoples R China. Ye, ZJ (reprint author), Guangzhou Univ Chinese Med, Guangzhou 510006, Guangdong, Peoples R China.; Qiu, HZ (reprint author), Guangzhou Univ Chinese Med, Coll Econ %26 Management, Guangzhou 510006, Guangdong, Peoples R China.";
    String str3 =
        "yang qiang,qiang yang, HongKong University of Science and Technology;;; ma jian, city university of hongkong, jian ma";
    try {
      Map mp = pss.getExtractUserAndInsName(str);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
