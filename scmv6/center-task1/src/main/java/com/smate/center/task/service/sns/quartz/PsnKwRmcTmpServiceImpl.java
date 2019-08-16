package com.smate.center.task.service.sns.quartz;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.PsnKwNsfcprjDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcExtDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcGidDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcTmpDao;
import com.smate.center.task.dao.sns.quartz.PsnKwZtDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.PsnKwRmcTmp;

@Service("psnKwRmcTmpService")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class PsnKwRmcTmpServiceImpl implements PsnKwRmcTmpService {
  @Autowired
  private PsnKwRmcTmpDao psnKwRmcTmpDao;
  @Autowired
  private PsnKwNsfcprjDao psnKwNsfcprjDao;
  @Autowired
  private PsnKwZtDao psnKwZtDao;
  @Autowired
  private PsnKwRmcDao psnKwRmcDao;
  @Autowired
  private PsnKwRmcExtDao psnKwRmcExtDao;
  @Autowired
  private PsnKwRmcGidDao psnKwRmcGidDao;

  @Override
  public void HandlePsnKwRmcTmp(Long psnId) throws ServiceException {
    savePubKeyWord(psnId);
    // 插入项目关键词到临时表
    savePrjKwNsfc(psnId);
    // 插入
    saveZtKw(psnId);

    List<PsnKwRmcTmp> psnKwRmcTmpLists = psnKwRmcTmpDao.getPsnKwEptTemp(psnId);
    for (PsnKwRmcTmp psnKwRmcTmp : psnKwRmcTmpLists) {
      psnKwRmcTmpDao.updatePsnKwEptTemp(psnKwRmcTmp.getId(), this.getKwNum(psnKwRmcTmp.getKeyWordTxt()));
    }
    // 补充项目关键词个数
    psnKwRmcTmpDao.updatePrjKwNum();
    // 补充自填关键词标记
    psnKwRmcTmpDao.updateZtKwNum();
    // --标记等级
    psnKwRmcTmpDao.updateGrade1();
    psnKwRmcTmpDao.updateGrade2();
    psnKwRmcTmpDao.updateGrade3();
    // 在这里先删除这三个表中psnId为当前值的记录。后面就不会报唯一约束的问题（会影响数据获取，迁移到数据持久化时判断）
    /*
     * psnKwRmcDao.deleteByPsnId(psnId); psnKwRmcExtDao.deleteBypsnId(psnId);
     * psnKwRmcGidDao.deleteByPsnId(psnId);
     */
  }

  public void saveZtKw(Long psnId) {
    psnKwRmcTmpDao.addZhZtToPubKwEptTmp(psnId);
    psnKwRmcTmpDao.updateNewZtZhkwByPsnId(psnId);
    psnKwRmcTmpDao.addEnZtToPubKwEptTmp(psnId);
    psnKwRmcTmpDao.updateNewZtEnkwByPsnId(psnId);

  }

  public void savePrjKwNsfc(Long psnId) {
    // 插入中文关键词
    psnKwRmcTmpDao.addZhPrjToPubKwEptTmp(psnId);
    psnKwRmcTmpDao.updateNewPrjZhkwByPsnId(psnId);
    psnKwRmcTmpDao.addEnPrjToPubKwEptTmp(psnId);
    psnKwRmcTmpDao.updateNewPrjEnkwByPsnId(psnId);
  }

  public void savePubKeyWord(Long psnId) {
    psnKwRmcTmpDao.addNewZhToPubKwEptTmp(psnId);
    psnKwRmcTmpDao.updateNewPubZhkwByPsnId(psnId);
    psnKwRmcTmpDao.addNewEnToPubKwEptTmp(psnId);
    psnKwRmcTmpDao.updateNewPubEnkwByPsnId(psnId);
  }

  public int getKwNum(String keywords) {
    int type = 2;
    int blankNum = 0;
    int wordNum = 1;
    if (StringUtils.isBlank(keywords)) {
      return 0;
    }
    if (keywords.getBytes().length != keywords.length()) {// 包含中文字符
      type = 1;
    }
    if (type == 1) {
      Pattern pattern = Pattern.compile("\\s+");
      Matcher matcher = pattern.matcher(keywords);
      while (matcher.find()) {
        blankNum++;
      }
      if (blankNum > 0) {
        wordNum = blankNum + 1;
      }
    } else if (type == 2) {
      Pattern pattern = Pattern.compile("([a-z]|[0-9]|\\-){2,}");
      Matcher matcher = pattern.matcher(keywords);
      while (matcher.find()) {
        wordNum++;
      }
      int length = keywords.replaceAll("([a-z]|[0-9]|\\-){2,}", "").length();
      wordNum = wordNum + length;
    }
    return wordNum;

  }

}
