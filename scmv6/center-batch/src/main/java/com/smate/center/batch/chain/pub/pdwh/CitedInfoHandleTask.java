package com.smate.center.batch.chain.pub.pdwh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubSourceDbDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubXmlDao;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubSourceDb;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubXml;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;

/**
 * 保存基准库收录情况
 * 
 * @author LJ 2017-02-28
 *
 */
public class CitedInfoHandleTask implements PdwhPubHandleTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final String name = "cited_info_handle";

  @Autowired
  private PdwhPublicationService pdwhPublicationService;
  @Autowired
  private PdwhPubSourceDbDao pdwhPubSourceDbDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PdwhPublication pdwhPub, PdwhPubImportContext context) {
    return true;
  }

  @Override
  public boolean run(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception {
    try {
      Integer dbId = pdwhPub.getDbId();
      Assert.notNull(dbId, "CitedInfoHandleTask，DbId不能为空");
      /**
       * 更新：如果从context中获取dupPubId不为空&为update操作，则从数据库中获取pdwhPubXml， 用pdwhPub更新对应值，更新引用；
       * 新增：当context中获取为saveNew操作，如果获取到的pdwhPubXml值为空则新建，更新引用。
       */
      Long dupPubId = context.getDupPubId();
      if (dupPubId != null && context.getOperation() == "update") { // 更新
        PdwhPubXml pdwhPubXml = pdwhPubXmlDao.get(dupPubId);
        Assert.notNull(pdwhPubXml, "CitedInfoHandleTask，pdwhPubXml不能为空");
        pdwhPublicationService.updateCitedInfo(dbId, pdwhPub, pdwhPubXml);
        PdwhPubSourceDb pubSourceDb = pdwhPubSourceDbDao.get(dupPubId);
        if (pubSourceDb == null) {
          pubSourceDb = new PdwhPubSourceDb();
          pubSourceDb.setPubId(dupPubId);
        }
        // 更新引用次数
        pdwhPublicationService.handlePdwhCiteTimes(pdwhPub);
        this.handlePubSourceDb(dbId, pubSourceDb);

      } else if (context.getOperation() == "saveNew") { // 新导入
        PdwhPubSourceDb pubSourceDb = pdwhPublicationService.saveCitedInfo(pdwhPub);
        // 更新引用次数
        pdwhPublicationService.handlePdwhCiteTimes(pdwhPub);
        this.handlePubSourceDb(dbId, pubSourceDb);
      }

    } catch (Exception e) {
      logger.error("保存基准库收录情况CitedInfoHandleTask出错,pubId=" + pdwhPub.getPubId(), e);
    }

    return true;
  }

  private void handlePubSourceDb(Integer dbId, PdwhPubSourceDb pubSourceDb) {
    switch (dbId) {
      case 4:
        pubSourceDb.setCnki(1);
        break;
      case 14:
        pubSourceDb.setEi(1);
        break;
      case 15:
        pubSourceDb.setIstp(1);
        break;
      case 16:
        pubSourceDb.setSci(1);
        break;
      case 17:
        pubSourceDb.setSsci(1);
        break;
      case 21:
        pubSourceDb.setCnkiPat(1);
        break;
      case 31:
        pubSourceDb.setRainPat(1);
        break;
      case 32:
        pubSourceDb.setOalib(1);
        break;

      default:
        break;
    }
    pdwhPublicationService.savePubSourceDb(pubSourceDb);

  }

}
