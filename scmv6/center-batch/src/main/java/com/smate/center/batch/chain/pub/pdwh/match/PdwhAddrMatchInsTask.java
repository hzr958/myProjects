package com.smate.center.batch.chain.pub.pdwh.match;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.pubimport.PdwhAddrMacthInsService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * 基准库成果地址和常量地址匹配任务
 *
 * @author LIJUN
 * @date 2018年3月15日
 */
public class PdwhAddrMatchInsTask implements PdwhPubMatchHandleTask {
  private final String name = "pdwhAddrMatchInsTask";
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhAddrMacthInsService pdwhAddrMacthInsService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PubPdwhDetailDOM pdwhPub, String context) {
    if (pdwhPub == null) {
      return false;
    }
    return true;
  }

  @Override
  public boolean run(PubPdwhDetailDOM pdwhPub, String context) throws Exception {
    Integer dbId = pdwhPub.getSrcDbId();
    String organization = pdwhPub.getOrganization();
    logger.info("----------------当前成果的单位地址：" + pdwhPub.getOrganization());
    if (dbId == null) {
      dbId = 99;
    }
    // 改成果单位信息由xml读取（pdwhpubaddr表存在数据不全问题）
    Set<String> pubAddrSet = new HashSet<String>();
    if (dbId == 4 || dbId == 14 || dbId == 21 || dbId == 11) {
      // cnki || ei || cnkipat的成果用这个方法去解析
      pubAddrSet = XmlUtil.parseCnkiPubAddrs(organization);
    } else if (dbId == 8 || dbId == 15 || dbId == 16 || dbId == 17 || dbId == 10) {
      // isi的成果用这个方法去解析
      pubAddrSet = XmlUtil.parseIsiPubAddrs(organization);
    } else if (dbId == 36) {
      pubAddrSet = XmlUtil.parseCrossrefPubAddrs(organization);
    } else {
      // 其他没有定义的都暂时用isi的拆分
      pubAddrSet = XmlUtil.parseIsiPubAddrs(organization);
    }
    if (CollectionUtils.isEmpty(pubAddrSet)) {
      logger.error("基准库成果地址匹配任务PdwhAddrMacthInsTask,获取不到成果地址，跳过匹配，pub_id：" + pdwhPub.getPubId());
      return true;
    }
    logger.info("-------------------拆分后的单位地址" + pubAddrSet);
    pdwhAddrMacthInsService.startMatchInsName(pubAddrSet, pdwhPub.getPubId(), context);
    return true;
  }

}
