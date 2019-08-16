package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.quartz.PdwhIndexPublication;
import com.smate.center.task.service.pdwh.quartz.PublicationAllService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.string.IrisStringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class CleanPdwhIndexPubDataTask extends TaskAbstract {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static final int SIZE = 100;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private PublicationAllService publicationAllService;
  public static String CLEAN_PUB_DATA = "clean_pub_data";

  public CleanPdwhIndexPubDataTask() {
    super();
  }

  public CleanPdwhIndexPubDataTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========CleanPdwhIndexPubDataTask已关闭==========");
      return;
    }
    try {
      Long lastId = (Long) cacheService.get(CLEAN_PUB_DATA, "clean_pub_data_pub_id");
      if (lastId == null) {
        lastId = 0L;
      }
      List<PdwhIndexPublication> pdwhIndexList = publicationAllService.getNeedCleanData(lastId, SIZE);
      if (pdwhIndexList == null || pdwhIndexList.size() == 0) {
        super.closeOneTimeTask();
      }
      for (PdwhIndexPublication pub : pdwhIndexList) {
        String result = this.parseWordToPinYin(pub.getPubTitle());
        publicationAllService.updatePdwhIndexPubZhTitleAndPubIndexSecondLevel(pub.getPubId(), result);
      }
      this.cacheService.put(CLEAN_PUB_DATA, 60 * 60 * 24, "clean_pub_data_pub_id",
          pdwhIndexList.get(pdwhIndexList.size() - 1).getPubId());
    } catch (Exception e) {
      logger.error("处理pdwh_index_publication表中文转拼音不成功问题", e);
    }

  }

  private String parseWordToPinYin(String zhTitleShort) {
    String result = "";
    try {
      HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
      format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
      char[] words = zhTitleShort.trim().toCharArray();
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
      logger.error("=====中文转拼音出错：" + zhTitleShort);
    }
    return result;
  }
}
