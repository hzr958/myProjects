package com.smate.center.open.service.nsfc.pub;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.nsfc.NsfcwsPubXmlDao;
import com.smate.center.open.dao.nsfc.pub.NsfcwsPublicationDao;
import com.smate.center.open.model.nsfc.NsfcwsPublication;
import com.smate.center.open.model.nsfc.project.NsfcwsPubXml;
import com.smate.center.open.service.nsfc.IrisExcludedPubService;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 成果在线 成果服务
 * 
 * @author tsz
 * 
 */
@Service("googlePubService")
@Transactional(rollbackFor = Exception.class)
public class NsfcwsPubServiceImpl implements NsfcwsPubService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  static final String GOOGLE_SCHOLAR = "google_scholar";

  @Autowired
  private NsfcwsPublicationDao nsfcwsPublicationDao;

  @Autowired
  private IrisExcludedPubService irisExcludedPubService;

  @Autowired
  private NsfcwsPubXmlDao nsfcwsPubXmlDao;


  @Override
  public Long getPsnPubCount(Long psnId, String keywords, String excludedPubIDS) throws Exception {
    try {
      // TODO ajb这个地方 需要调整 excludedPubIDS这个参数其实 不需要保存在数据库里面了
      String uuid = null;
      if (StringUtils.isNotBlank(excludedPubIDS)) {
        uuid = UUID.randomUUID().toString();
        this.irisExcludedPubService.saveIrisExcludedPub(ServiceUtil.splitStrToLong(excludedPubIDS), uuid);
      }


      Long pubCount = this.nsfcwsPublicationDao.queryPsnPubCount(psnId, keywords, uuid);
      if (StringUtils.isNotBlank(excludedPubIDS)) {
        this.irisExcludedPubService.deleteIrisExcludedPub(uuid);

      }
      return pubCount;
    } catch (Exception e) {
      logger.error(String.format("查询google人员psnId=${1}的成果数出现异常：", psnId), e);
      throw new Exception(e);
    }
  }

  @Override
  public Page<NsfcwsPublication> getPsnPubByPage(Long psnId, String keywords, String excludedPubIDS, String sortType,
      int pageSize, int pageNum) throws Exception {
    try {
      Page<NsfcwsPublication> page = new Page<NsfcwsPublication>();
      page.setIgnoreMin(true);
      page.setPageSize(pageSize);
      page.setPageNo(pageNum);
      page = this.nsfcwsPublicationDao.queryPsnPubByPage(psnId, keywords, ServiceUtil.splitStrToLong(excludedPubIDS),
          sortType, page);
      return page;
    } catch (Exception e) {
      logger.error(String.format("查询google人员psnId=${1}的成果记录出现异常：", psnId), e);
      throw new Exception(e);
    }
  }

  @Override
  public NsfcwsPublication getNsfcwsPublicationByPubId(Long pubId) throws Exception {
    try {
      return this.nsfcwsPublicationDao.get(pubId);
    } catch (Exception e) {
      logger.error(String.format("查询成果pubId={1}出现异常：", pubId), e);
      throw new Exception(e);
    }
  }

  @Override
  public String getPubXmlByPubId(Long pubId) throws Exception {
    NsfcwsPubXml xml = nsfcwsPubXmlDao.get(pubId);
    return xml.getXml();
  }


  @Override
  public String getPubCitedListByPubId(Long pubId) throws Exception {
    try {
      NsfcwsPublication publication = this.nsfcwsPublicationDao.get(pubId);
      if (publication != null) {
        return publication.getCitedList();
      }
      return null;
    } catch (Exception e) {
      logger.error(String.format("查询成果pubId={1}出现异常：", pubId), e);
      throw new Exception(String.format("查询成果pubId={1}出现异常：", pubId), e);
    }
  }

}
