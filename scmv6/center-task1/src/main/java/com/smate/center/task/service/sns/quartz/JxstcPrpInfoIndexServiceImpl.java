package com.smate.center.task.service.sns.quartz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.JxkjtPrpInfoDao;
import com.smate.center.task.dao.pdwh.quartz.JxkjtPrpInfoTempDao;
import com.smate.center.task.dao.pdwh.quartz.JxstcPrpInfoDao;
import com.smate.center.task.dao.pdwh.quartz.JxstcPrpInfoTempDao;
import com.smate.center.task.model.pdwh.pub.JxkjtPrpInfo;
import com.smate.center.task.model.pdwh.pub.JxkjtPrpInfoTemp;
import com.smate.center.task.model.pdwh.pub.JxstcPrpInfo;
import com.smate.center.task.model.pdwh.pub.JxstcPrpInfoTemp;
import com.smate.core.base.utils.string.IrisStringUtils;

/**
 * 
 * @author zzx
 *
 */
@Service("jxstcPrpInfoIndexService")
@Transactional(rollbackFor = Exception.class)
public class JxstcPrpInfoIndexServiceImpl implements JxstcPrpInfoIndexService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private JxstcPrpInfoTempDao jxstcPrpInfoTempDao;
  @Autowired
  private JxstcPrpInfoDao jxstcPrpInfoDao;
  @Autowired
  private JxkjtPrpInfoTempDao jxkjtPrpInfoTempDao;
  @Autowired
  private JxkjtPrpInfoDao jxkjtPrpInfoDao;



  @Override
  public List<JxstcPrpInfo> findList(int batchSize) throws Exception {
    return jxstcPrpInfoDao.findList(batchSize);
  }

  @Override
  public JxstcPrpInfoTemp dohandle(JxstcPrpInfo one) throws Exception {
    JxstcPrpInfoTemp j = new JxstcPrpInfoTemp();
    j.setPosCode(one.getPosCode());
    j.setPrpCode(one.getPrpCode());
    if (StringUtils.isNotBlank(one.getPrpXml())) {
      try {
        Document document = DocumentHelper.parseText(one.getPrpXml());
        Element root = document.getRootElement();
        Element proposal = root.element("proposal");
        if (proposal != null) {
          String zh_title = null;
          String key_words = null;
          String summary_0 = null;
          // 获取zh_title,兼容获取ctitle
          zh_title = proposal.attributeValue("zh_title");
          if (zh_title == null) {
            zh_title = proposal.attributeValue("ctitle");
          }
          // 获取key_words,兼容获取keyword
          key_words = proposal.attributeValue("key_words");
          if (key_words == null) {
            key_words = proposal.attributeValue("keyword");
          }
          if (key_words == null) {
            key_words = proposal.attributeValue("keywords");
          }
          // 获取summary_0
          Element goal = proposal.element("goal");
          if (goal != null) {
            summary_0 = goal.attributeValue("summary_0");
            if (summary_0 == null) {
              summary_0 = goal.attributeValue("summary_1");
            }
          }
          j.setKeyWords(checkStr(key_words != null ? key_words : ""));
          j.setSummary(checkStr(summary_0 != null ? summary_0 : ""));
          j.setZhTitle(checkStr(zh_title != null ? zh_title : ""));
        }
      } catch (Exception e) {
        logger.error("解析xml异常，JxstcPrpInfo.posCode=" + one.getPosCode(), e);
      }
    }
    return j;
  }

  @Override
  public void saveJxstcPrpInfoTemp(JxstcPrpInfoTemp jxstcPrpInfoTemp) throws Exception {
    jxstcPrpInfoTempDao.save(jxstcPrpInfoTemp);
  }

  @Override
  public List<JxkjtPrpInfo> findList2(int batchSize) throws Exception {
    return jxkjtPrpInfoDao.findList(batchSize);
  }

  @Override
  public JxkjtPrpInfoTemp dohandle2(JxkjtPrpInfo one) throws Exception {
    JxkjtPrpInfoTemp j = new JxkjtPrpInfoTemp();
    j.setPosCode(one.getPosCode());
    j.setPrpCode(one.getPrpCode());
    if (StringUtils.isNotBlank(one.getPrpXml())) {
      try {
        Document document = DocumentHelper.parseText(one.getPrpXml());
        Element root = document.getRootElement();
        Element proposal = root.element("proposal");
        if (proposal != null) {
          Element zh_title = null;
          Element key_words = null;
          Element summary_0 = null;
          // 获取zh_title,兼容获取ctitle
          zh_title = proposal.element("zh_title");
          if (zh_title == null) {
            zh_title = proposal.element("ctitle");
          }
          // 获取key_words,兼容获取keyword、keywords
          key_words = proposal.element("key_words");
          if (key_words == null) {
            key_words = proposal.element("keyword");
          }
          if (key_words == null) {
            key_words = proposal.element("keywords");
          }
          // 获取summary_0兼容获取summary_1
          Element goal = proposal.element("goal");
          if (goal != null) {
            summary_0 = goal.element("summary_0");
            if (summary_0 == null) {
              summary_0 = goal.element("summary_1");
            }
          }
          j.setKeyWords(checkStr(key_words != null ? key_words.getText() : ""));
          j.setSummary(checkStr(summary_0 != null ? summary_0.getText() : ""));
          j.setZhTitle(checkStr(zh_title != null ? zh_title.getText() : ""));
        }
      } catch (Exception e) {
        logger.error("解析xml异常，JxstcPrpInfo.posCode=" + one.getPosCode(), e);
      }
    }
    return j;
  }

  @Override
  public void saveJxkjtPrpInfoTemp(JxkjtPrpInfoTemp jxkjtPrpInfoTemp) throws Exception {
    jxkjtPrpInfoTempDao.save(jxkjtPrpInfoTemp);
  }

  private String checkStr(String str) {
    if (str == null || "".equals(str.trim())) {
      return "";
    }
    String full2Half = IrisStringUtils.full2Half(str);
    if (full2Half.length() > 1300) {
      full2Half = full2Half.substring(0, 1300);
    }
    return full2Half;
  }

}
