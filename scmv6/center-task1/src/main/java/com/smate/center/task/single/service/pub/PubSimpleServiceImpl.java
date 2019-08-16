package com.smate.center.task.single.service.pub;

import com.smate.center.task.dao.sns.quartz.PubDataStoreDao;
import com.smate.center.task.dao.sns.quartz.PubSimpleDao;
import com.smate.center.task.dao.sns.quartz.PubSimpleHashDao;
import com.smate.center.task.model.sns.quartz.PubDataStore;
import com.smate.center.task.model.sns.quartz.PubSimple;
import com.smate.center.task.model.sns.quartz.PubSimpleHash;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.center.task.single.constants.PsnCnfConst;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.util.pub.PubXmlObjectUtil;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 
 * @author lxz
 *
 */
@Service("pubSimpleService")
@Transactional(rollbackFor = Exception.class)
public class PubSimpleServiceImpl implements PubSimpleService {

  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private PubDataStoreDao pubDataStoreDao;
  @Autowired
  private PubSimpleHashDao pubSimpleHashDao;

  /*
   * @Autowired private PubSimpleSaveBatchService pubSimpleSaveBatchService;
   */
  @Override
  public PubSimple queryPubSimple(Long pubId) {
    PubSimple pubSimple = pubSimpleDao.get(pubId);
    return pubSimple;
  }

  @Override
  public void save(PubSimple pubSimple) {
    pubSimpleDao.save(pubSimple);
  }

  @Override
  public void savePubDataStore(PubDataStore pub) {
    this.pubDataStoreDao.save(pub);
  }

  /**
   * 用于pdwh成果导入群组成果库，没有查重处理
   * 
   */
  @Override
  public Long savePubSimpleData(Publication pub, PubXmlDocument doc) throws Exception {
    // 1,查找pubsimple
    String dupPubId = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dup_pub_id");
    String dupAction = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dup_action");
    String needReplaceFullText = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "need_replace_fulltext");
    String fileId = doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id");
    Long pubId = null;
    PubSimple pubSimple = null;
    // 当重复成果ID不为空且导入选中替换时
    if (StringUtils.isNotBlank(dupPubId) && "1".equals(dupAction)) {
      pubId = Long.valueOf(dupPubId);
      pubSimple = pubSimpleDao.get(pubId);
    }
    if (pubSimple == null) {
      pubSimple = new PubSimple();
      pubSimple.setSimpleTask(0);
    }
    String authorNames = pubSimple.getAuthorNames();
    PubSimpleHash pubSimpleHash = new PubSimpleHash();
    pubSimple = convertToPubSimple(pub, pubSimple);
    pubSimple.setPubType(pub.getArticleType());
    pubSimple.setPubId(pubId);
    // 选择替换时不替换作者名
    if (pubId != null && "1".equals(dupAction)) {
      pubSimple.setAuthorNames(authorNames);
    }
    pubSimple.setOwnerPsnId(doc.getExpandPsnId());
    pubSimpleHash = convertToPubSimpleHash(pub, pubSimpleHash);

    PubDataStore pubDataStore = new PubDataStore();
    // 保存v_pub_simple
    Long groupId = 0L;
    if (pub.getGroupId() != null && pub.getGroupId() != 0L) {
      groupId = pub.getGroupId();
      pubSimple.setStatus(5);
    } else {
      pubSimple.setStatus(0);
    }
    pubSimpleDao.save(pubSimple);
    // 保存v_pub_simple_hash
    pubSimpleHash.setPubId(pubSimple.getPubId());// scm-8481
    // hash表记录的pubId要与pubSimple的一致，否则导入到我的成果库时查重不成功
    pubSimpleHashDao.save(pubSimpleHash);
    pubDataStore.setPubId(pubSimple.getPubId());

    // 为导入的pubxml加入meta pubid；
    doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id", String.valueOf(pubSimple.getPubId()));

    // 保证pub_mate节点authority有值,用于后台任务辨别是否是个人新增的成果(无:新增,有:其他) SCM-8473
    String authority = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "authority");
    if (StringUtils.isBlank(authority)) {
      doc.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "authority", PsnCnfConst.ALLOWS_SELF.toString());
    }
    // 选择替换时不替换作者名称
    if (pubId != null && "1".equals(dupAction)) {
      doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names", authorNames);
      doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_author_names", authorNames);
    }
    PubXmlObjectUtil.getPubDataStoreFromXml(doc.getXmlString(), pubDataStore);

    // 复制全文记录
    // 当执行替换操作，且需要替换全文时，或者是新增操作且全文文件Id不为空时（由于前面程序中已对导入的成果全文结点进行了重构，全文不能导入进来的已经被删除全文结点）
    /*
     * if ((pubId != null && "1".equals(dupAction) && "true".equals(needReplaceFullText)) ||
     * (!"1".equals(dupAction) && StringUtils.isNotBlank(fileId))) {
     * copyPubFulltext(pubSimple.getPubId(), pub.getId()); }
     */
    // 保存v_pub_data_store
    pubDataStoreDao.save(pubDataStore);
    /*
     * // 保存导入任务 pubSimpleSaveBatchService.savePubImportBatch(pubSimple.getPubId(),
     * pubSimple.getSimpleVersion(), groupId);
     */
    return pubSimple.getPubId();
  }

  /**
   * 构造PubSimple
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pub
   * @param pubSimple
   * @return
   * @throws Exception
   */
  private PubSimple convertToPubSimple(Publication pub, PubSimple pubSimple) throws Exception {
    // 转换数据
    PropertyUtils.copyProperties(pubSimple, pub);
    // 初始化数据
    pubSimple.setPubId(null);
    pubSimple.setSimpleStatus(0L);
    pubSimple.setSimpleTask(0);
    pubSimple.setSimpleVersion(new Date().getTime());
    pubSimple.setCreateDate(new Date());
    // 返回
    return pubSimple;
  }

  /**
   * 构造PubSimpleHash
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pub
   * @param pubSimpleHash
   * @return
   */
  private PubSimpleHash convertToPubSimpleHash(Publication pub, PubSimpleHash pubSimpleHash) {
    // 转换数据
    // String zhHashCode = PubHashUtils.getTitleInfoHash(pub.getZhTitle(),
    // pub.getArticleType(), pub.getPublishYear());
    // String enHashCode = PubHashUtils.getTitleInfoHash(pub.getEnTitle(),
    // pub.getArticleType(), pub.getPublishYear());
    // pubSimpleHash.setPubId(pub.getId());
    // pubSimpleHash.setZhHashCode(zhHashCode);
    // pubSimpleHash.setEnHashCode(enHashCode);
    // 查重规则有更改
    String zhTitle = pub.getZhTitle();
    String enTitle = pub.getEnTitle();
    String pubType = pub.getTypeId() == null ? "" : pub.getTypeId() + "";
    String pubYear = pub.getPublishYear() == null ? "" : pub.getPublishYear() + "";
    String tpHashZh = PubHashUtils.getTpHash(zhTitle, pubType);
    String tpHashEn = PubHashUtils.getTpHash(enTitle, pubType);
    String zhHashCode = PubHashUtils.getTitleInfoHash(zhTitle, pubType, pubYear);
    String enHashCode = PubHashUtils.getTitleInfoHash(enTitle, pubType, pubYear);
    pubSimpleHash.setPubId(pub.getId());
    pubSimpleHash.setTpHashZh(tpHashZh);
    pubSimpleHash.setTpHashEn(tpHashEn);
    pubSimpleHash.setZhHashCode(zhHashCode);
    pubSimpleHash.setEnHashCode(enHashCode);
    // 返回
    return pubSimpleHash;
  }
}
