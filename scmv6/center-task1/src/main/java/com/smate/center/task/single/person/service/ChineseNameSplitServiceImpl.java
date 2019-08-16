package com.smate.center.task.single.person.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.NameSplitDao;
import com.smate.center.task.model.sns.pub.NameSplit;

@Service("chineseNameSplitService")
@Transactional(rollbackFor = Exception.class)
public class ChineseNameSplitServiceImpl implements ChineseNameSplitService {
  private static final List<String> FU_XING = new ArrayList<String>();
  static {
    FU_XING.add("欧阳");
    FU_XING.add("太史");
    FU_XING.add("端木");
    FU_XING.add("上官");
    FU_XING.add("司马");
    FU_XING.add("东方");
    FU_XING.add("独孤");
    FU_XING.add("南宫");
    FU_XING.add("万俟");
    FU_XING.add("闻人");
    FU_XING.add("夏侯");
    FU_XING.add("诸葛");
    FU_XING.add("尉迟");
    FU_XING.add("公羊");
    FU_XING.add("赫连");
    FU_XING.add("澹台");
    FU_XING.add("皇甫");
    FU_XING.add("宗政");
    FU_XING.add("濮阳");
    FU_XING.add("公冶");
    FU_XING.add("太叔");
    FU_XING.add("申屠");
    FU_XING.add("公孙");
    FU_XING.add("慕容");
    FU_XING.add("仲孙");
    FU_XING.add("钟离");
    FU_XING.add("长孙");
    FU_XING.add("宇文");
    FU_XING.add("司徒");
    FU_XING.add("鲜于");
    FU_XING.add("司空");
    FU_XING.add("闾丘");
    FU_XING.add("子车");
    FU_XING.add("亓官");
    FU_XING.add("司寇");
    FU_XING.add("巫马");
    FU_XING.add("公西");
    FU_XING.add("颛孙");
    FU_XING.add("壤驷");
    FU_XING.add("公良");
    FU_XING.add("漆雕");
    FU_XING.add("乐正");
    FU_XING.add("宰父");
    FU_XING.add("谷梁");
    FU_XING.add("拓跋");
    FU_XING.add("夹谷");
    FU_XING.add("轩辕");
    FU_XING.add("令狐");
    FU_XING.add("段干");
    FU_XING.add("百里");
    FU_XING.add("呼延");
    FU_XING.add("东郭");
    FU_XING.add("南门");
    FU_XING.add("羊舌");
    FU_XING.add("微生");
    FU_XING.add("公户");
    FU_XING.add("公玉");
    FU_XING.add("公仪");
    FU_XING.add("梁丘");
    FU_XING.add("公仲");
    FU_XING.add("公上");
    FU_XING.add("公门");
    FU_XING.add("公山");
    FU_XING.add("公坚");
    FU_XING.add("左丘");
    FU_XING.add("公伯");
    FU_XING.add("西门");
    FU_XING.add("公祖");
    FU_XING.add("第五");
    FU_XING.add("公乘");
    FU_XING.add("贯丘");
    FU_XING.add("公皙");
    FU_XING.add("南荣");
    FU_XING.add("东里");
    FU_XING.add("东宫");
    FU_XING.add("仲长");
    FU_XING.add("子书");
    FU_XING.add("子桑");
    FU_XING.add("即墨");
    FU_XING.add("达奚");
    FU_XING.add("褚师");
  }

  @Autowired
  private NameSplitDao nameSplitDao;

  @Override
  public List<NameSplit> getToHandleList(Integer size) {

    List<NameSplit> nameList = this.nameSplitDao.getNameSplit(0, size);
    return nameList;
  }

  @Override
  public void getLastNameAndFirstName(NameSplit name) throws Exception {
    if (StringUtils.isEmpty(name.getNameZh())) {
      name.setStatus(9);
      this.nameSplitDao.save(name);
      return;
    }

    String zhName = name.getNameZh();

    // 如果不全是中文，就不拆分了；
    if (!isChinese(zhName)) {
      if (zhName.getBytes().length == zhName.length()) {// 纯英文
        name.setStatus(7);
        this.nameSplitDao.save(name);
      } else {// 中英文混杂
        name.setStatus(8);
        this.nameSplitDao.save(name);
      }
      return;
    }

    String[] nameString = this.getLastNameAndFirstNam(zhName);
    name.setLastNameZh(StringUtils.trimToEmpty(nameString[0]));
    name.setFirstNameZh(StringUtils.trimToEmpty(nameString[1]));
    name.setStatus(1);
    this.nameSplitDao.save(name);
  }

  @Override
  public void updateNameListStatus(NameSplit name, Integer status) {
    name.setStatus(status);
    this.nameSplitDao.save(name);
  }

  private String[] getLastNameAndFirstNam(String zhName) {
    if (StringUtils.isEmpty(zhName)) {
      return null;
    }
    String lastName = "";
    String firstName = "";
    if (zhName.length() <= 2) {
      lastName = zhName.substring(0, 1);
      firstName = zhName.substring(1);
    } else {
      for (String fuXing : FU_XING) {
        if (zhName.startsWith(fuXing)) {
          lastName = zhName.substring(0, 2);
          firstName = zhName.substring(2);
          return new String[] {lastName, firstName};
        }
      }
      lastName = zhName.substring(0, 1);
      firstName = zhName.substring(1);
    }
    return new String[] {lastName, firstName};
  }

  private boolean isChinese(String str) {
    if (str == null) {
      return false;
    }
    Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
    // pattern.matcher(str.trim()).matches()包含中文
    return pattern.matcher(str.trim()).matches();
  }
}
