package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.model.pdwh.quartz.PdwhIndexPublication;
import com.smate.center.task.single.dao.solr.PdwhIndexPublicationDao;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDAO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.utils.string.IrisStringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@Service
@Transactional(rollbackFor = Exception.class)
public class PdwhPubUpdateImportPublicationServiceImpl implements PdwhPubUpdateImportPublicationService {

  // private final static Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PdwhIndexPublicationDao pdwhIndexPublicationDao;

  @Override
  public void savePdwhIndexPublication(PubPdwhPO pubPdwhPO) {
    String pubTitle = pubPdwhPO.getTitle();

    int length = pubTitle.length();
    // 只取500长度
    if (length > 500) {
      pubTitle = pubTitle.substring(0, 500);
    }
    String shortTitle = "";
    String onechar = pubTitle.substring(0, 1);
    shortTitle = parseWordToPinYin(onechar);
    if (length > 1) { // 如果大于等于两个字
      onechar = pubTitle.substring(0, 2);
      shortTitle = parseWordToPinYin(onechar);
    }
    PdwhIndexPublication pdwhIndexPublication = new PdwhIndexPublication(pubPdwhPO.getPubId(), pubTitle, shortTitle);
    pdwhIndexPublicationDao.saveOrUpdate(pdwhIndexPublication);
  }

  /**
   * 截取标题两个字符
   * 
   * @param shortTitle
   * @param title
   * @param start
   * @param end
   */
  private void subTitle(StringBuilder shortTitle, String title, int start, int end) {
    String onechar = title.substring(start, end);
    Character c1 = onechar.toCharArray()[0];
    if (c1 >= 0x0391 && c1 <= 0xFFE5) { // 是中文
      // 中文转拼音
      shortTitle.append(parseWordToPinYin(onechar));
    } else {
      shortTitle.append(c1);
    }
  }



  private static String parseWordToPinYin(String title) {
    String result = "";
    try {
      HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
      format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
      char[] words = title.trim().toCharArray();
      if (words.length > 0) {
        for (int i = 0; i < words.length; i++) {
          String[] pinyin = null;
          boolean isChinese = true;
          if (StringUtils.isNotBlank(words[i] + "") && IrisStringUtils.isChineseChar(words[i])) {
            pinyin = PinyinHelper.toHanyuPinyinStringArray(words[i], format);
          } else {
            pinyin = new String[] {words[i] + ""};
            isChinese = false;
          }
          if (i == 0) {
            if (pinyin == null) {
              result += words[0];
            } else {
              result += pinyin[0];
            }
          } else {
            if (pinyin == null) {
              result += words[i];
            } else if (isChinese) {
              result += pinyin[0];
            } else {
              result += pinyin[0];
            }
          }
        }
      }
    } catch (BadHanyuPinyinOutputFormatCombination e) {
      // logger.error("=====中文转拼音出错：" + title);
    }
    return result;
  }

  public static void main(String[] args) {
    System.out.println(parseWordToPinYin("a张"));
  }


  @Override
  public void deleteNotExist() {
    pdwhIndexPublicationDao.deleteNotExist();

  }


  @Override
  public List<PubPdwhPO> getUpdatePdwhMonth(Integer startIndex, Integer size) {
    return pubPdwhDAO.getUpdatePdwhMonth(startIndex, size);
  }



}
