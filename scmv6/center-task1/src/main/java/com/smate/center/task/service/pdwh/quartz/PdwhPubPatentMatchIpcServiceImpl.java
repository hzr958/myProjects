package com.smate.center.task.service.pdwh.quartz;

import java.util.Date;
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

import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.pdwh.quartz.PubCategoryPatentDao;
import com.smate.center.task.dao.pdwh.quartz.PubCategoryPatentTempDao;
import com.smate.center.task.dao.pdwh.quartz.TmpJiangxiPubaddrDao;
import com.smate.center.task.dao.sns.pub.CategoryMapScmIpcDao;
import com.smate.center.task.model.pdwh.quartz.PdwhPubXml;
import com.smate.center.task.model.pdwh.quartz.PubCategoryPatent;
import com.smate.center.task.model.pdwh.quartz.PubCategoryPatentTemp;
import com.smate.center.task.model.pdwh.quartz.TmpJiangxiPubaddr;

/**
 * 
 * @author zzx
 *
 */
@Service("pdwhPubPatentMatchIpcService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubPatentMatchIpcServiceImpl implements PdwhPubPatentMatchIpcService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TmpJiangxiPubaddrDao tmpJiangxiPubaddrDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private CategoryMapScmIpcDao categoryMapScmIpcDao;
  @Autowired
  private PubCategoryPatentDao pubCategoryPatentDao;
  @Autowired
  private PubCategoryPatentTempDao pubCategoryPatentTempDao;

  @Override
  public List<PubCategoryPatentTemp> getList(int batchSize) throws Exception {
    return pubCategoryPatentTempDao.findList(batchSize);
  }

  @Override
  public void doHandle(PubCategoryPatentTemp one) throws Exception {
    // 通过pubId获取成果xml
    if (one.getPubId() != null) {
      String ipc = "";
      List<Long> categoryIds = null;
      String scmCategoryIds = "";
      String category_no = null;// xml中的没截取之前的ipc
      Integer dealStatus = null;// 1=根据成果xml查找不到category_no；2=根据category_no中截取不到ipc;3=根据ipc查找不到categoryId；4=未处理；0=正常处理;null=查找成果xml出错
      if (!pubCategoryPatentDao.findExist(one.getPubId())) {
        PdwhPubXml pdwhPubXml = pdwhPubXmlDao.get(one.getPubId());
        if (pdwhPubXml != null && StringUtils.isNotBlank(pdwhPubXml.getXml())) {
          try {
            // 解析ipc
            Document document = DocumentHelper.parseText(pdwhPubXml.getXml());
            Element root = document.getRootElement();
            Element publication = root.element("publication");
            if (publication != null) {
              category_no = publication.attributeValue("patent_main_category_no");
              if (StringUtils.isBlank(category_no)) {
                category_no = publication.attributeValue("patent_category_no");
              }
              if (StringUtils.isNotBlank(category_no)) {
                ipc = findIpcNo(category_no);
              } else {
                dealStatus = 1;
              }
              // 根据ipc获取categoryId
              if (StringUtils.isNotBlank(ipc)) {
                categoryIds = categoryMapScmIpcDao.findCategoryIdByIpc(ipc);
              } else if (dealStatus == null) {
                dealStatus = 2;
              }
              if (categoryIds != null && categoryIds.size() > 0) {
                for (Long categoryId : categoryIds) {
                  PubCategoryPatent pp = new PubCategoryPatent();
                  pp.setPubId(one.getPubId());
                  pp.setScmCategoryId(categoryId);
                  pubCategoryPatentDao.save(pp);
                  scmCategoryIds += categoryId.toString() + ";";
                }
                dealStatus = 0;
              } else if (dealStatus == null) {
                dealStatus = 3;
              }
            }
          } catch (Exception e) {
            logger.error("PdwhPubPatentMatchIpcTask解析xml出错", e);
          }
        }
      } else {
        dealStatus = 0;
      }
      // 保存任务记录
      one.setCategoryNo(category_no);
      one.setIpcCode(ipc);
      one.setScmCategoryIds(scmCategoryIds);
      one.setCreateDate(new Date());
      one.setDealStatus(dealStatus);
      pubCategoryPatentTempDao.save(one);
    }

  }

  private static String findIpcNo(String s) {
    int length = s.length();
    int status = 0;
    String result = "";
    for (int i = 0; i < length; i++) {
      if (Character.isLetter(s.charAt(i))) {
        status++;
      }
      if (status == 2) {
        result = s.substring(0, i);
        break;
      }
    }
    return result;
  }

  public static void main(String[] args) {
    System.out.println(findIpcNo("BJ20/06"));
  }
}
