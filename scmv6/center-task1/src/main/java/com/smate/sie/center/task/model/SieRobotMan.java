package com.smate.sie.center.task.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 社交化机器人表单
 * 
 * @author 叶星源
 */
@SuppressWarnings("serial")
public class SieRobotMan implements Serializable {

  // 配置文件
  private JSONObject jsonConfig;
  // 机器人的属性：
  private Long psnId;// 机器人的id
  private String ip;// IP地址
  private String ipCountry;// IP所在国家
  private String ipProv;// IP所在省
  private String ipCity;// IP所在市
  // 机器人的操作时间
  private Date controlTime;
  // 机器人的任务：
  private int interviewPrj; // 要访问的项目数
  private int interviewPat; // 要访问的专利数
  private int interviewPub; // 要访问的成果数

  // 机器人访问的概率:
  private int insShareProb; // 机构分享的概率
  private int readProb; // 成果阅读概率
  private int downloadProb;// 成果下载概率
  private int shareProb;// 成果分享概率
  private int quoteProb;// 成果引用概率


  // 初始化
  public SieRobotMan() {
    super();
  }

  public SieRobotMan(SieRobotManConfig sieRobotManConfig) {
    this.psnId = 0L;
    this.ip = getRandomIp();
    // 访问的数量
    this.interviewPat = sieRobotManConfig.getViewPat().intValue();
    this.interviewPub = sieRobotManConfig.getViewPub().intValue();
    this.interviewPrj = sieRobotManConfig.getViewPrj().intValue();
    // 概率
    this.quoteProb = sieRobotManConfig.getRateCitation().intValue();
    this.downloadProb = sieRobotManConfig.getRateDownload().intValue();
    this.insShareProb = sieRobotManConfig.getRateIndexShare().intValue();
    this.readProb = sieRobotManConfig.getRateRead().intValue();
    this.shareProb = sieRobotManConfig.getRateShare().intValue();
  }

  public JSONObject getJsonConfig() {
    return jsonConfig;
  }

  public Long getPsnId() {
    return psnId;
  }

  public String getIp() {
    return ip;
  }

  public String getIpCountry() {
    return ipCountry;
  }

  public String getIpProv() {
    return ipProv;
  }

  public String getIpCity() {
    return ipCity;
  }

  public Date getControlTime() {
    if (controlTime == null) {
      controlTime = new Date();
    }
    // 每次获取时间都获取一个过去一个月时间之内的一个值
    Long newDate = new Date().getTime() - RandomUtils.nextLong(0L, 1000L * 3600 * 24 * 30);
    controlTime = new Date(newDate);
    return controlTime;
  }

  public int getInterviewPrj() {
    return interviewPrj;
  }

  public int getInterviewPat() {
    return interviewPat;
  }

  public int getInterviewPub() {
    return interviewPub;
  }

  public int getInsShareProb() {
    return insShareProb;
  }

  public int getReadProb() {
    return readProb;
  }

  public int getDownloadProb() {
    return downloadProb;
  }

  public int getShareProb() {
    return shareProb;
  }

  public int getQuoteProb() {
    return quoteProb;
  }

  public void setJsonConfig(JSONObject jsonConfig) {
    this.jsonConfig = jsonConfig;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public void setIpCountry(String ipCountry) {
    this.ipCountry = ipCountry;
  }

  public void setIpProv(String ipProv) {
    this.ipProv = ipProv;
  }

  public void setIpCity(String ipCity) {
    this.ipCity = ipCity;
  }

  public void setControlTime(Date controlTime) {
    this.controlTime = controlTime;
  }

  public void setInterviewPrj(int interviewPrj) {
    this.interviewPrj = interviewPrj;
  }

  public void setInterviewPat(int interviewPat) {
    this.interviewPat = interviewPat;
  }

  public void setInterviewPub(int interviewPub) {
    this.interviewPub = interviewPub;
  }

  public void setInsShareProb(int insShareProb) {
    this.insShareProb = insShareProb;
  }

  public void setReadProb(int readProb) {
    this.readProb = readProb;
  }

  public void setDownloadProb(int downloadProb) {
    this.downloadProb = downloadProb;
  }

  public void setShareProb(int shareProb) {
    this.shareProb = shareProb;
  }

  public void setQuoteProb(int quoteProb) {
    this.quoteProb = quoteProb;
  }

  /**
   * 获取随机IP地址工具类
   */
  public static String getRandomIp() {
    // ip范围
    int[][] range = {{607649792, 608174079}, // 36.56.0.0-36.63.255.255
        {1038614528, 1039007743}, // 61.232.0.0-61.237.255.255
        {1783627776, 1784676351}, // 106.80.0.0-106.95.255.255
        {2035023872, 2035154943}, // 121.76.0.0-121.77.255.255
        {2078801920, 2079064063}, // 123.232.0.0-123.235.255.255
        {-1950089216, -1948778497}, // 139.196.0.0-139.215.255.255
        {-1425539072, -1425014785}, // 171.8.0.0-171.15.255.255
        {-1236271104, -1235419137}, // 182.80.0.0-182.92.255.255
        {-770113536, -768606209}, // 210.25.0.0-210.47.255.255
        {-569376768, -564133889}, // 222.16.0.0-222.95.255.255
    };
    Random rdint = new Random();
    int index = rdint.nextInt(10);
    String ip = num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
    return ip;
  }

  public static String num2ip(int ip) {
    int[] b = new int[4];
    String x = "";
    b[0] = (int) ((ip >> 24) & 0xff);
    b[1] = (int) ((ip >> 16) & 0xff);
    b[2] = (int) ((ip >> 8) & 0xff);
    b[3] = (int) (ip & 0xff);
    x = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "."
        + Integer.toString(b[3]);
    return x;
  }

  // 随机生成首页分享平台
  public Integer getsieBhIndexSharePlatForm() {
    return new Integer((int) (Math.random() * 6 + 1));
  }

  // 随机生成分享平台
  public Integer getsieBhSharePlatForm() {
    return new Integer((int) (Math.random() * 4 + 1));
  }

}
