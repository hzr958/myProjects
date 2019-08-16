package com.smate.center.task.service.pdwh.quartz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhCitedRelationDao;
import com.smate.center.task.model.pdwh.pub.PdwhCitedRelation;
import com.smate.center.task.single.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.task.single.service.pub.PubOriginalDataService;
import com.smate.center.task.single.util.pub.ImportPubXmlUtils;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubSituationDAO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.exception.DAOException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;

@Service("updatePubCiteTimesService")
@Transactional(rollbackFor = Exception.class)
public class UpdatePubCiteTimesServiceImpl implements UpdatePubCiteTimesService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${updatesource.xmlfile.dir}")
  private String xmlDir;
  @Autowired
  private HandlePdwhPubXmlService handlepdwhPubXmlService;
  @Autowired
  private PdwhCitedRelationDao pdwhCitedRelationDao;
  @Autowired
  private PdwhPubSituationDAO pdwhPubSituationDAO;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubOriginalDataService pubOriginalDataService;

  /**
   * 获取xml文件
   * 
   * @throws Exception
   */
  @Override
  public File getfile() throws Exception {

    File file = new File(xmlDir);

    if (!file.exists()) {
      logger.error("目录:" + xmlDir + " 不存在,将自动创建");
    }

    // 目录不存在则创建
    if (!file.getParentFile().exists()) {
      try {
        file.mkdirs();
      } catch (Exception e) {
        throw new Exception("目录:" + xmlDir + " 创建失败");
      }
    }
    return file;

  }

  @Override
  public void handleXML(File file) throws Exception {
    // 通过文件名获取sourceId
    String name = file.getName();
    // 文件名： 20150507_2015_32768_1_1_000207594500011.xml
    String sourceId = null;
    if (StringUtils.isNoneBlank(name)) {
      // 以"."拆分需要转义
      try {
        sourceId = name.split("_")[5].split("\\.")[0];
      } catch (Exception e) {
        logger.error("文件名拆分获取sourceId处理出错,filename:" + name);
        changeName(file, file.getName(), 1);
        return;
      }
    }
    // 通过sourceId到查重表查询pubId
    Long currentPubId = pdwhPubSituationDAO.getPubIdBySrcId(sourceId);
    // 匹配不上跳过当前文件
    if (currentPubId == null) {
      logger.info("系统不存在此条成果记录：sourceId=" + sourceId);
      changeName(file, file.getName(), 1);
      return;
    }
    int fileciteTimes = 0;
    try {
      fileciteTimes = this.getCurrentCiteTimes(currentPubId, file);
    } catch (Exception e) {
      logger.error("通过XML文件获取当前的引用次数出错！pubId:" + currentPubId, e);
      changeName(file, file.getName(), 1);
    }
    // 调用接口更新引用次数
    String status = handlepdwhPubXmlService.updateCitedTimes(currentPubId, fileciteTimes);
    Map resultMap = JacksonUtils.jsonToMap(status);
    if ("SUCCESS".equals(resultMap.get("status").toString())) {
      // 处理完成
      this.changeName(file, file.getName(), 0);// 成功
      // 同时要更新solr中的引用数
      handlepdwhPubXmlService.updatePubInfoInSolr(currentPubId);
      // 同时要更新与当前成果有关联关系的个人成果
      List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsPubIdListByPdwhId(currentPubId);
      for (Long pubId : snsPubIds) {
        handlepdwhPubXmlService.updateSnsPubCitedTimes(pubId, fileciteTimes);
      }
    } else {
      changeName(file, file.getName(), 1);
    }

  }

  /**
   * 通过XML文件获取当前的引用次数
   * 
   * @param filePubId
   * @param file
   * @return
   */
  public int getCurrentCiteTimes(Long filePubId, File file) throws Exception {
    // 获取要处理XML数据
    String xmldata = this.getXMLdata(file);
    String seqNo = null;
    // 拆分data节点字段
    List<String> pubXmls = ImportPubXmlUtils.splitOnlineXml(xmldata);
    if (CollectionUtils.isEmpty(pubXmls)) {
      throw new Exception("xml文件data节点为空");
    }
    int citeTimes = pubXmls.size();
    Long tmpId;
    // 获取XML的data节点数据处理
    for (String pubData : pubXmls) {
      try {
        // 清理XML数据
        pubData = pubData.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
        seqNo = ImportPubXmlUtils.getSeqNo(pubData);// 与sourceId相同
        ImportPubXmlDocument doc = new ImportPubXmlDocument(pubData);

        // 调用工具类将xml转成json
        Map<String, Object> map = handlepdwhPubXmlService.XmlToJsonData(pubData);
        try {
          // 先调用查重接口
          String dupResult = handlepdwhPubXmlService.getPubDupucheckStatus(map);
          Map dupResultMap = JacksonUtils.jsonToMap(dupResult);
          if ("SUCCESS".equals(dupResultMap.get("status").toString())) {
            if (dupResultMap.get("msg") != null) {// 调用更新基准库成果的接口
              // 有就不保存，直接建立联系
              this.savePdwhPubCitedRelation(filePubId, Long.valueOf(dupResultMap.get("msg").toString()));
            } else {// 调用接口保存基准库成果
              Long id = handlepdwhPubXmlService.saveOriginalPdwhPubRelation();
              pubOriginalDataService.savePubOriginalData(id, pubData);
              String dataString = handlepdwhPubXmlService.saveNewPdwhPub(map, null, 2L);
              Map resultMap = JacksonUtils.jsonToMap(dataString);
              if ("SUCCESS".equals(resultMap.get("status")) && resultMap.get("de3PubId") != null) {
                Long pdwhPubId = Long.valueOf(Des3Utils.decodeFromDes3(resultMap.get("de3PubId").toString()));
                handlepdwhPubXmlService.updateOriginalPdwhPubRelation(id, pdwhPubId, 2);// 保存成功后要把关联上的pubid补上
                this.savePdwhPubCitedRelation(filePubId, pdwhPubId);
              } else {
                handlepdwhPubXmlService.updateOriginalPdwhPubRelation(id, null, 9);// 保存失败修改处理状态
              }
            }
          }
        } catch (Exception e) {
          logger.error("保存或更新xml成果data节点数据！filename:" + map.get("title") + "seqNo:" + seqNo, e);
        }
      } catch (Exception e) {
        logger.info("xml成果data节点数据拆分出错！filename:" + file.getName() + "seqNo:" + seqNo, e);
        throw new Exception("xml成果data节点数据拆分出错");
      }
    }
    return citeTimes;
  }

  private void savePdwhPubCitedRelation(Long filePubId, Long citedPubId) {
    PdwhCitedRelation pubRelation = pdwhCitedRelationDao.getPdwhCitedRelation(filePubId, citedPubId);
    if (pubRelation == null) {// 重复数据不插入
      try {
        pubRelation = new PdwhCitedRelation();
        pubRelation.setPdwdPubId(filePubId);
        pubRelation.setPdwhCitedPubId(citedPubId);
        pdwhCitedRelationDao.save(pubRelation);
      } catch (DAOException e) {
        logger.error("保存成果引用关系出错，pubId={},citedPubId={}", filePubId, citedPubId, e);
      }
    }
  }

  /**
   * 获取XML数据
   * 
   * @param file
   * @return
   * @throws Exception
   */
  public String getXMLdata(File file) throws Exception {

    String encoding = "UTF-8";
    String xmldata = null;
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
      String str = null;
      while ((str = reader.readLine()) != null) {
        xmldata = xmldata + str;
      }
      reader.close();
    } catch (Exception e) {
      logger.info("文件数据读取出错！，文件名：" + file.getName(), e);
      this.changeName(file, file.getName(), 1);// 失败
      return null;
    }
    return xmldata;

  }

  /**
   * @throws Exception 文件处理后标记处理
   * 
   * @param file
   * @param fileName @throws
   */
  private void changeName(File file, String fileName, int flag) throws Exception {
    String prefix = null;
    if (flag == 0) {
      prefix = "finish";
    }
    if (flag == 1) {
      prefix = "error";
    }

    File newFile = new File(file.getPath().replace(fileName, "") + prefix + "_" + fileName);
    try {
      FileUtils.moveFile(file, newFile);
    } catch (Exception e) {
      logger.error("文件命名失败" + fileName + "path:" + file.getPath(), e);
      throw new Exception("文件命名失败：" + "path:" + file.getPath(), e);

    }
  }

}
