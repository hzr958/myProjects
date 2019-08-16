package com.smate.center.task.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.quartz.PubToPubSimpleErrorLog;
import com.smate.center.task.model.sns.quartz.ScmPubXml;
import com.smate.center.task.service.sns.quartz.PublicationToPubSimpleService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.service.pub.ScmPubXmlService;
import com.smate.core.base.utils.cache.CacheService;

@Deprecated
public class PubToPubsimpleTask10 extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 5000; // 每次刷新获取的个数

  /*
   * private Long startPubId = 1000010500000L;
   * 
   * private Long endPubId = 1000012000000L;
   */
  private Long startPubId;

  private Long endPubId;


  public Long getStartPubId() {
    return startPubId;
  }

  public void setStartPubId(Long startPubId) {
    this.startPubId = startPubId;
  }

  public Long getEndPubId() {
    return endPubId;
  }

  public void setEndPubId(Long endPubId) {
    this.endPubId = endPubId;
  }


  public PubToPubsimpleTask10() {
    super();
  }

  public PubToPubsimpleTask10(String beanName) {
    super(beanName);
  }


  @Autowired
  private ScmPubXmlService scmPubXmlService;

  @Autowired
  private PublicationToPubSimpleService publicationToPubSimpleService;

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private CacheService cacheService;

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      logger.info("=========PubToPubSimpleTask10已关闭==========");
      return;
    }

    if (taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask_removePubCache10") == 1) {
      cacheService.remove("PubToPubsimpleTask10", "last_pub_id");
    }

    try {
      Long lastPubId = (Long) cacheService.get("PubToPubsimpleTask10", "last_pub_id");
      if (lastPubId == null) {
        lastPubId = startPubId;
      }
      logger.debug("===========================================PubToPubSimpleTask10=========开始1");

      List<Long> pubList = publicationToPubSimpleService.getSnsPubSimpleIds(SIZE, lastPubId, endPubId);

      // List<Long> pubList =
      // publicationToPubSimpleService.getPubsByPsnId(1000000002544L);
      if (CollectionUtils.isEmpty(pubList)) {
        logger.info(
            "===========================================PubToPubSimpleTask10  没有获取到pubSimple表数据!!!!============, time = "
                + new Date());
        return;
      }

      Integer lastIndex = pubList.size() - 1;
      Long lastId = pubList.get(lastIndex);
      this.cacheService.put("PubToPubsimpleTask10", 60 * 60 * 24, "last_pub_id", lastId);

      for (Long pubId : pubList) {
        try {
          ScmPubXml scmPubXml = scmPubXmlService.getPubXml(pubId);
          if (scmPubXml == null) {
            throw new Exception("PubId = " + pubId + " 的xml为空");
          }
          this.publicationToPubSimpleService.copyPubXmlToDataStore(scmPubXml);
        } catch (Exception e) {
          PubToPubSimpleErrorLog error = new PubToPubSimpleErrorLog();
          error.setPubId(pubId);
          String errorMsg = e.toString();
          error.setErrorMsg(errorMsg);
          this.publicationToPubSimpleService.saveError(error);
          logger.debug("BatchPubToPubSimpleTask10出错==============", e);
        }
      }

    } catch (Exception e) {
      logger.error("pubToPubsimpleTask10,运行异常", e);
    }

  }

  public static Integer randomRoll(Integer num) {
    Random rd = new Random();
    Integer rm = rd.nextInt(5);
    System.out.println("Dice number is :" + num + ", and its value is :" + rm + "! ");
    return rm;
  }

  public static void main(String[] str) {
    Integer fiveBuffTimes = 0;
    Integer twoBuffTimes = 0;
    Integer oneBuffTimes = 0;
    Integer cdBuffTimes = 0;
    Integer rollTimes = 1000;
    for (Integer i = 1; i <= rollTimes; i++) {
      HashMap<String, Integer> rs = new HashMap<String, Integer>();
      for (Integer j = 0; j < 5; j++) {
        String dice = String.valueOf(randomRoll(j));
        Integer counts = rs.get(dice);
        if (counts == null) {
          rs.put(dice, 1);
        } else {
          counts++;
          rs.put(dice, counts);
        }
      }

      Integer size = rs.size();
      if (size == 5) {
        System.out.println("Congras! U get 5 buffs: hast crit extra cooldown energy!");
        fiveBuffTimes++;
        cdBuffTimes++;
      } else if (size <= 4) {
        StringBuilder outPut = new StringBuilder();
        for (Entry<String, Integer> en : rs.entrySet()) {
          Integer count = en.getValue();
          if (count > 1) {
            String num = en.getKey();
            switch (num) {
              case ("0"):
                outPut.append("hast ");
                break;
              case ("1"):
                outPut.append("crit ");
                break;
              case ("2"):
                outPut.append("extra ");
                break;
              case ("3"):
                outPut.append("cooldown ");
                break;
              case ("4"):
                outPut.append("energy ");
                break;
              default:
                break;
            }
          }
        }
        String buffString = StringUtils.trim(outPut.toString());
        if ("cooldown".indexOf(buffString) > -1) {
          cdBuffTimes++;
        }

        String[] bs = buffString.split(" ");
        if (bs.length > 1) {
          System.out.println("U get 2 buffs: " + buffString + "!");
          twoBuffTimes++;
        } else {
          System.out.println("U get 1 buff: " + buffString + "!");
          oneBuffTimes++;
        }
      }

    }

    System.out.println("Combat stops! You've got 5 buffs " + fiveBuffTimes + " times, 2 buffs " + twoBuffTimes
        + " times, 1 buff " + oneBuffTimes + " times. In total, " + cdBuffTimes + " buffs included cooldown.");
  }

}
