package com.smate.core.base.statistics;

import com.smate.core.base.psn.model.PsnStatistics;

/**
 * 人员信息统计表,属性为null值的保存为0
 * 
 * @author lhd
 *
 */
public class PsnStatisticsUtils {

  public static void buildZero(PsnStatistics p) {
    // 1.成果总数
    if (p.getPubSum() == null) {
      p.setPubSum(0);
    }
    // 2.成果引用次数总数
    if (p.getCitedSum() == null) {
      p.setCitedSum(0);
    }
    // 3.hindex指数
    if (p.getHindex() == null) {
      p.setHindex(0);
    }
    // 4.中文成果数
    if (p.getZhSum() == null) {
      p.setZhSum(0);
    }
    // 5.英文成果数
    if (p.getEnSum() == null) {
      p.setEnSum(0);
    }
    // 6.项目总数
    if (p.getPrjSum() == null) {
      p.setPrjSum(0);
    }
    // 7.好友总数
    if (p.getFrdSum() == null) {
      p.setFrdSum(0);
    }
    // 8.群组总数
    if (p.getGroupSum() == null) {
      p.setGroupSum(0);
    }
    // 9.成果被赞的总数
    if (p.getPubAwardSum() == null) {
      p.setPubAwardSum(0);
    }
    // 10.专利数
    if (p.getPatentSum() == null) {
      p.setPatentSum(0);
    }
    // 11.待认领成果数
    if (p.getPcfPubSum() == null) {
      p.setPcfPubSum(0);
    }
    // 12.成果全文推荐数
    if (p.getPubFullTextSum() == null) {
      p.setPubFullTextSum(0);
    }
    // 13.公开成果总数
    if (p.getOpenPubSum() == null) {
      p.setOpenPubSum(0);
    }
    // 14.公开项目总数
    if (p.getOpenPrjSum() == null) {
      p.setOpenPrjSum(0);
    }
    // 15.访问总数
    if (p.getVisitSum() == null) {
      p.setVisitSum(0);
    }
  }

}
