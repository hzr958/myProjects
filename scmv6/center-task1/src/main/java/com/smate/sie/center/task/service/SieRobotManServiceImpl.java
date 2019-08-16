package com.smate.sie.center.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.dao.SieRobotManConfigDao;
import com.smate.sie.center.task.dao.SieRobotManInsDao;
import com.smate.sie.center.task.model.SieRobotMan;
import com.smate.sie.center.task.model.SieRobotManConfig;
import com.smate.sie.center.task.model.SieRobotManIns;


/**
 * 机器人配置Service
 * 
 * @author 叶星源
 */
@Service("sieRobotManService")
@Transactional(rollbackOn = Exception.class)
public class SieRobotManServiceImpl implements SieRobotManService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SieRobotManConfigDao sieRobotManConfigDao;

  @Autowired
  private SieCityIpService sieCityIpService;
  @Autowired
  private SieRobotManInsDao sieRobotManInsDao;

  public SieRobotManServiceImpl() {
    super();
  }

  /*
   * @Override public void errorInfo(SieRobotManConfig sieRobotManConfig) {
   * sieRobotManConfig.setStatus(99L); sieRobotManConfigDao.save(sieRobotManConfig); }
   */

  /**
   * 初始化机器人配置。问题：性能会受sieCityIpService影响。这个地方可以考虑是否用多线程来提高性能。
   */
  @Override
  public List<SieRobotMan> getSieRobotMan(SieRobotManConfig sieRobotManConfig) {
    List<SieRobotMan> newRobotManList = new ArrayList<SieRobotMan>();
    try {
      // 给机器人队列填充数据
      Integer robotNum = sieRobotManConfig.getRobotNum().intValue();
      for (int i = 0; i < robotNum; i++) {

        SieRobotMan robot = new SieRobotMan(sieRobotManConfig);
        // 填充ip对应的地址
        String manIp = robot.getIp();
        // ip解析
        Map<String, String> ipParse = null;
        try {
          ipParse = sieCityIpService.parseIP2(manIp);
        } catch (SysServiceException e) {
          e.printStackTrace();
        }
        if (ipParse != null) {
          robot.setIpCountry(ipParse.get(ParseIpUtils.STR_COUNTRY));
          robot.setIpProv(ipParse.get(ParseIpUtils.STR_PRVO));
          robot.setIpCity(ipParse.get(ParseIpUtils.STR_CITY));
        }
        newRobotManList.add(robot);
      }
    } catch (Exception e) {
      logger.error("机器人随机访问机构，给机器人队列填充数据失败！");
      e.printStackTrace();
    }
    return newRobotManList;
  }

  /**
   * 获取所有单位,从所有单位里取N条数据出栈,然后存入缓存中,下次再从缓存中取单位
   */
  @Override
  public List<SieRobotManIns> getSubInsList() {
    List<SieRobotManIns> insList = new ArrayList<SieRobotManIns>();
    try {
      insList = sieRobotManInsDao.getSubInsList();
    } catch (Exception e) {
      logger.error("机器人随机访问机构任务，获取社交机器人需访问的机构失败！", e);
      e.printStackTrace();
    }
    return insList;
  }

  /**
   * 获取机器人配置文件信息。若是配置文件不正确，则会新建一个配置文件。若是配置文件不存在，则会新建一个配置文件。
   */
  @Override
  public SieRobotManConfig getNormalReflush(Integer configId) {
    SieRobotManConfig sieRobotManConfig = null;
    try {
      sieRobotManConfig = sieRobotManConfigDao.getNormalModel(configId);
    } catch (Exception e) {
      logger.error("机器人随机访问机构任务，获取该单位的机器人访问配置信息失败！", e);
      e.printStackTrace();
    }
    return sieRobotManConfig;
  }


}
