package com.smate.center.batch.service.pub;

import com.smate.center.batch.dao.sns.pub.PubAssignLogDao;
import com.smate.center.batch.dao.sns.pub.PubDataStoreDao;
import com.smate.center.batch.dao.sns.pub.PubSimpleDao;
import com.smate.center.batch.dao.sns.pub.PubSimpleHashDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubDataStore;
import com.smate.center.batch.model.sns.pub.PubSimple;
import com.smate.center.batch.model.sns.pub.PubSimpleHash;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.pubtopubsimple.PubSimpleSaveBatchService;
import com.smate.center.batch.util.pub.PubXmlObjectUtil;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
  @Autowired
  private PubAssignLogDao pubAssignLogDao;
  @Autowired
  private PubFolderService pubFolderService;
  @Autowired
  private PublicationGroupService publicationGroupService;
  @Autowired
  private PubSimpleSaveBatchService pubSimpleSaveBatchService;

  @Override
  public PubSimple queryPubSimpleAndXml(Long pubId) {
    PubSimple pubSimple = pubSimpleDao.get(pubId);
    if (pubSimple != null) {
      PubDataStore pubDataStore = pubDataStoreDao.get(pubId);
      pubSimple.setPubDataStore(pubDataStore);
    }
    return pubSimple;
  }

  @Override
  public PubSimple queryPubSimple(Long pubId) {
    PubSimple pubSimple = pubSimpleDao.get(pubId);
    return pubSimple;
  }

  @Override
  public void delPubSimpleData(PubSimple pubSimple) {
    Integer articleType = pubSimple.getArticleType();
    // SCM-11320 互联互通获取成果接口改进SCM-11340 ,2017-01-12-ajb 不需要删除pubSimple表相关数据
    // 把已加入文件夹的成果记录删除
    this.pubFolderService.removePublicationFromPubFolder(pubSimple.getPubId().toString(), articleType);
    // 同步到群组节点
    this.publicationGroupSyncMessage(pubSimple);
    // pubSimpleHashDao.deleteData(pubSimple.getPubId());
    // pubDataStoreDao.deleteData(pubSimple.getPubId());
    // 2017-10-18 ajb因为成果的状态可能没有更新
    pubSimpleDao.updateStatus(1, pubSimple.getPubId());
  }

  /**
   * 新增成果、编辑成果同步到小组.
   * <p/>
   * action='del' 删除.
   * <p/>
   * action="add" 新增.
   * <p/>
   * action="edit" 编辑.
   * 
   * @param doc
   * @param pub
   * @param context
   * @param action
   * @throws ServiceException
   */
  private void publicationGroupSyncMessage(PubSimple pubSimple) throws ServiceException {
    this.publicationGroupService.delPublication(pubSimple.getPubId(), pubSimple.getOwnerPsnId());
  }

  @Override
  public void save(PubSimple pubSimple) {
    pubSimpleDao.save(pubSimple);
  }

  @Override
  public List<Long> getDupPubIdsFromSimpleHash(Long pubId, String enHashCode, String zhHashCode) {

    List<Long> dupPubIds = pubSimpleHashDao.getSimpleDupPubIds(pubId, enHashCode, zhHashCode);

    return dupPubIds;
  }

  @Override
  public boolean pubSimpleDupCheck(Long pubId, String enHashCode, String zhHashCode) {

    return pubSimpleHashDao.pubSimpleDupCheck(pubId, enHashCode, zhHashCode);

  }

  @Override
  public List<PubDataStore> getXmlPubMemberRefreshList(Integer size, Long lastPubId, Long startPubId, Long endPubId) {

    return pubDataStoreDao.getRefreshList(size, lastPubId, startPubId, endPubId);
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
    pubSimple.setPubType(pub.getTypeId());
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
    // 保存导入任务
    pubSimpleSaveBatchService.savePubImportBatch(pubSimple.getPubId(), pubSimple.getSimpleVersion(), groupId);

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
    // pub.getTypeId(), pub.getPublishYear());
    // String enHashCode = PubHashUtils.getTitleInfoHash(pub.getEnTitle(),
    // pub.getTypeId(), pub.getPublishYear());
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

  @Override
  public PubSimpleHash getPubSimpleHash(Long pubId) {
    return pubSimpleHashDao.get(pubId);
  }

}
