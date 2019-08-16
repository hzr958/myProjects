package com.smate.sie.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.utils.SiePubXMLToJsonUtils;
import com.smate.center.task.utils.XSSUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.center.task.model.SieDataPubXml2JsonRefresh;
import com.smate.sie.center.task.service.SieFixPubXml2JsonService;
import com.smate.sie.core.base.utils.model.pub.SiePatXml;
import com.smate.sie.core.base.utils.model.pub.SiePubXml;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 数据服务：生成单位成果数据。注意：每次调用之前必须先手动清空表DATA_PUBXML_TO_JSON。
 * 
 * @author lijianming
 * @Date 20190325
 */
public class SieFixPubXml2JsonTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 100;// 一次最多更新数量

  @Autowired
  private SieFixPubXml2JsonService sieFixPubXml2JsonService;

  public SieFixPubXml2JsonTask() {
    super();
  }

  public SieFixPubXml2JsonTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      while (true) {
        // 获取需要更新的数据
        List<SieDataPubXml2JsonRefresh> list = this.sieFixPubXml2JsonService.getNeedRefreshData(BATCH_SIZE);
        if (list == null || list.size() == 0) {
          return;
        }
        for (SieDataPubXml2JsonRefresh refresh : list) {
          try {
            SiePubXml pubXml = null;
            SiePatXml patXml = null;
            String pubJson = null;
            // 得到需要转化Json数据的xml
            if (refresh.getPubType() == 5) {
              patXml = sieFixPubXml2JsonService.checkPatXmlAndJsonIsExist(refresh.getPubId());
              if (patXml != null) {
                // 转化数据
                pubJson = SiePubXMLToJsonUtils.dealWithXML(patXml.getPatXml());
              } else { // 找不到pubxml和存在json数据的按1处理
                refresh.setStatus(1);
                sieFixPubXml2JsonService.savePubXml2JsonRefresh(refresh);
                continue;
              }
            } else {
              pubXml = sieFixPubXml2JsonService.checkPubXmlAndJsonIsExist(refresh.getPubId());
              if (pubXml != null) {
                // 转化数据
                pubJson = SiePubXMLToJsonUtils.dealWithXML(pubXml.getPubXml());
              } else { // 找不到pubxml和存在json数据的按1处理
                refresh.setStatus(1);
                sieFixPubXml2JsonService.savePubXml2JsonRefresh(refresh);
                continue;
              }
            }
            // 数据转义
            pubJson = XSSUtils.xssReplace(pubJson);
            PubJsonDTO pub = JacksonUtils.jsonObject(pubJson, PubJsonDTO.class);
            // 数据保存
            if (refresh.getPubType() == 5) { // 保存到pat_Json表
              sieFixPubXml2JsonService.savePatJsonData(pub, refresh.getPubId());
            } else { // 保存到pub_Json表
              sieFixPubXml2JsonService.savePubJsonData(pub, refresh.getPubId());
            }
            // 转化并保存成功
            refresh.setStatus(1);
            sieFixPubXml2JsonService.savePubXml2JsonRefresh(refresh);
          } catch (Exception e) {
            logger.error("数据服务：转化成果数据失败：", e);
            sieFixPubXml2JsonService.saveHandleFailRefresh(refresh);
          }
        }
      }
    } catch (Exception e) {
      logger.error("数据服务：成果或专利XML数据转化为Json数据错误：", e);
    }
  }

}
