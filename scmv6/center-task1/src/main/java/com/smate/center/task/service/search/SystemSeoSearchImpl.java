package com.smate.center.task.service.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pub.seo.PubSeoFristLevelSerachDao;
import com.smate.center.task.dao.pub.seo.PubSeoSecondLevelSerachDao;
import com.smate.center.task.dao.pub.seo.PubSeoThirdLevelSerachDao;
import com.smate.center.task.model.pdwh.quartz.PdwhIndexPublication;
import com.smate.center.task.model.pub.seo.PubIndexFirstLevel;
import com.smate.center.task.model.pub.seo.PubIndexSecondLevel;
import com.smate.center.task.model.pub.seo.PubIndexThirdLevel;
import com.smate.center.task.model.search.PubSearchForm;
import com.smate.core.base.utils.model.Page;

@Service("systemSeoSearch")
@Transactional(rollbackFor = Exception.class)
public class SystemSeoSearchImpl implements SystemSeoSearch {

  /**
   * 
   */
  private static final long serialVersionUID = 3523616844712760937L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSeoFristLevelSerachDao pubSeoFristLevelSerachDao;
  @Autowired
  private PubSeoSecondLevelSerachDao pubSeoSecondLevelSerachDao;
  @Autowired
  private PubSeoThirdLevelSerachDao pubSeoThirdLevelSerachDao;

  @Override
  public void pubFilter(String code, int secondGroup, List<PdwhIndexPublication> iPubList, int num, boolean isDel) {
    if ("other".equals(code)) {
      code = "0";
    }
    if (!isDel)
      pubSeoFristLevelSerachDao.deletePubIndexFirstLevel(code, secondGroup);
    String t1 = null, t2 = null, label = null;
    if (iPubList != null && iPubList.size() > 0) {
      t1 = iPubList.get(0).getPubTitle();
      // 如果刚好只有一个
      t2 = iPubList.size() == 1 ? "" : iPubList.get(iPubList.size() - 1).getPubTitle();
      // 第一级分组标签
      String str1 = t1.length() > 25 ? t1.substring(0, 25) : t1;
      String str2 = t2.length() > 25 ? t2.substring(0, 25) : t2;
      label = str1 + "......" + str2;
      PubIndexFirstLevel pubIndexFirstLevel = new PubIndexFirstLevel();
      pubIndexFirstLevel.setFirstGroup(code);
      pubIndexFirstLevel.setFirstLabel(label);
      pubIndexFirstLevel.setSecondGroup(secondGroup);
      try {
        pubSeoFristLevelSerachDao.savePubSeoFristLevel(pubIndexFirstLevel);
      } catch (Exception e) {
        logger.error("首页论文seo第一层级分组出错！", e);
      }
      // 生成第二级目录
      generatorSecondDir(iPubList, code, secondGroup, isDel);
    }
  }

  private void generatorSecondDir(List<PdwhIndexPublication> iPubList, String code, int secondGroup, boolean isDel) {

    try {
      if (!isDel)
        // 没有删除 才删除第二级目录
        pubSeoSecondLevelSerachDao.deletePubIndexSecondLevel(code, secondGroup);
      String t1 = null, t2 = null, label = null, str1 = null, str2 = null;
      int secondAllCount = iPubList.size();
      // 计算第二级分组SecondLabel头尾标题取值
      Double sqrt = Math.sqrt(secondAllCount);
      Integer secondLabelCount = (int) Math.ceil(sqrt);// 算出的值向上取整
      // 最大每页显示200
      secondLabelCount = secondLabelCount > 200 ? 200 : secondLabelCount;
      boolean isGo = true;// 循环次数控制
      for (int i = 0; i < secondLabelCount && isGo; i++) {
        int startIndex = i * secondLabelCount;
        int endIndex = (i + 1) * secondLabelCount - 1;
        if (endIndex >= secondAllCount) { // 处理边界问题
          // 最后一个
          t2 = iPubList.size() == 1 ? "" : iPubList.get(secondAllCount - 1).getPubTitle();
          endIndex = secondAllCount - 1;
          isGo = false;
        } else {
          t2 = iPubList.size() == 1 ? "" : iPubList.get(endIndex).getPubTitle();
        }
        if (startIndex >= secondAllCount)
          break;
        t1 = iPubList.get(startIndex).getPubTitle();
        // 第二级分组标签
        str1 = t1.length() > 25 ? t1.substring(0, 25) : t1;
        str2 = t2.length() > 25 ? t2.substring(0, 25) : t2;
        label = str1 + "......" + str2;
        PubIndexSecondLevel pubIndexSecondLevel = new PubIndexSecondLevel();
        pubIndexSecondLevel.setFirstGroup(code);
        pubIndexSecondLevel.setSecondGroup(secondGroup);
        pubIndexSecondLevel.setSecondLabel(label);
        pubIndexSecondLevel.setThirdGroup(i + 1);
        // 生成第三级目录
        generatorThirdDir(startIndex, endIndex, iPubList, secondGroup, code, i + 1, isDel);
        // 保存
        pubSeoSecondLevelSerachDao.savePubSeoSecondLevel(pubIndexSecondLevel);
      }
    } catch (Exception e) {
      logger.error("首页论文seo第二层级分组出错！", e);
    }
  }



  /**
   * 生成第三级目录
   * 
   * @param startIndex 第三级第一个开始位置索引
   * @param endIndex 第三级第一个结束位置索引
   * @param iPubList
   * @param secondGroup 第二级标识
   * @param code 第一级分组
   * @param thirdGroup 第三级标识
   */
  private void generatorThirdDir(int startIndex, int endIndex, List<PdwhIndexPublication> iPubList, int secondGroup,
      String code, int thirdGroup, boolean isDel) {
    if (!isDel)
      // 删除第三级目录
      pubSeoThirdLevelSerachDao.deletePubSeoThirdLevel(code, secondGroup, thirdGroup);
    // 保存第三级
    for (int j = startIndex; j <= endIndex; j++) {
      PdwhIndexPublication pp = iPubList.get(j);
      String putTitle = pp.getPubTitle();
      String shortTitle = pp.getShortTitle();
      PubIndexThirdLevel pubIndexThirdLevel = pubSeoThirdLevelSerachDao.fundPubIndexThirdLevel(pp.getPubId());
      if (pubIndexThirdLevel == null) {
        pubIndexThirdLevel = new PubIndexThirdLevel(pp.getPubId());
      }
      pubIndexThirdLevel.setTitle(putTitle);
      pubIndexThirdLevel.setFirstLetter(code);
      // 成果名长度小于4的
      if (shortTitle.length() < 4) {
        if (shortTitle.length() == 1) {
          pubIndexThirdLevel.setOrderMark(shortTitle);
        } else {
          pubIndexThirdLevel.setOrderMark(shortTitle.substring(1, shortTitle.length()));
        }
      } else {
        pubIndexThirdLevel.setOrderMark(shortTitle.substring(1, 4));
      }
      pubIndexThirdLevel.setSecondGroup(secondGroup);
      pubIndexThirdLevel.setThirdGroup(thirdGroup);
      try {
        pubSeoThirdLevelSerachDao.savePubIndexThirdLevel(pubIndexThirdLevel);
      } catch (Exception e) {
        logger.error("首页论文seo成果分组出错,成果ID:" + pp.getPubId(), e);
      }
    }
  }


  @Override
  public List<PubIndexFirstLevel> getPubByCode(String code) {
    return pubSeoFristLevelSerachDao.getFirstLevel(code);
  }

  public List<PubIndexFirstLevel> getPubByCodeAndLabel(String code) {
    return pubSeoFristLevelSerachDao.getFirstLevelByLabel(code);
  }

  /**
   * 根据字母 第二级分组查找 成果
   */

  @Override
  public Page<PubIndexSecondLevel> getPubByLabel(String code, Integer label, Page<PubIndexSecondLevel> page) {
    if ("other".equalsIgnoreCase(code)) {
      code = "0";
    }
    pubSeoSecondLevelSerachDao.getFirstLevel(code, label, page);
    return page;
  }

  @Override
  public PubSearchForm findPubByName(PubSearchForm form) {
    pubSeoSecondLevelSerachDao.findPubByName(form);
    return form;
  }

  /**
   * 删除目录
   */
  public void delDir() {
    // 删除表数据 pub_index_first_level
    pubSeoFristLevelSerachDao.truncateFirstLevel();
    // 删除表数据 pub_index_second_level
    pubSeoSecondLevelSerachDao.truncateSecondLevel();
    // 删除表数据 pub_index_third_level
    pubSeoThirdLevelSerachDao.truncateThirdLevel();
  }
}
