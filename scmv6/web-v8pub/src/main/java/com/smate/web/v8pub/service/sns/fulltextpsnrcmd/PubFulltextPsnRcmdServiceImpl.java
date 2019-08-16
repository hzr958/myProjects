package com.smate.web.v8pub.service.sns.fulltextpsnrcmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.exception.PubException;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDAO;
import com.smate.web.v8pub.dao.sns.PubFulltextPsnRcmdDao;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.enums.PubHandlerEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.po.sns.PubFulltextPsnRcmd;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.utils.RestTemplateUtils;
import com.smate.web.v8pub.vo.PubFulltextPsnRcmdVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 成果全文人员推荐ServiceImpl.
 * 
 * @author pwl
 * 
 */
@Service("pubFulltextPsnRcmdService")
@Transactional(rollbackFor = Exception.class)
public class PubFulltextPsnRcmdServiceImpl implements PubFulltextPsnRcmdService {

  private static final long serialVersionUID = -6701471050304758099L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubFulltextPsnRcmdDao pubFulltextPsnRcmdDao;
  @Autowired
  private PubFullTextService pubFulltextService;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private ArchiveFileService archiveFileService;
  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${pub.saveorupdate.url}")
  private String SAVEORUPDATE;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Resource
  private FileDownloadUrlService fileDownloadUrlService;
  @Autowired
  private ArchiveFileDao archiveFileDao;



  @SuppressWarnings("unlikely-arg-type")
  @Override
  public void getPubRcmdFulltext(PubOperateVO pubOperateVO) throws ServiceException {
    List<PubFulltextPsnRcmd> result =
        pubFulltextPsnRcmdDao.queryPubRcmdPubFulltext(pubOperateVO.getPsnId(), pubOperateVO.getPubId());
    Long totalCount =
        pubFulltextPsnRcmdDao.getPubRcmdPubFulltextCount(pubOperateVO.getPsnId(), pubOperateVO.getPubId());
    PubFulltextPsnRcmdVO psnRcmdForm = new PubFulltextPsnRcmdVO();
    try {
      for (PubFulltextPsnRcmd pubFulltextPsnRcmd : result) {
        PropertyUtils.copyProperties(psnRcmdForm, pubFulltextPsnRcmd);
        PubSnsPO pubSns = pubSnsDAO.get(pubFulltextPsnRcmd.getPubId());
        if (pubSns != null) {
          PubSnsPO pub = new PubSnsPO();
          PropertyUtils.copyProperties(pub, pubSns);
          psnRcmdForm.setPub(pub);
          break;
        }
      }
      buildPubFulltextPsnRcmd(psnRcmdForm);
      pubOperateVO.setPubRcmdft(psnRcmdForm);
      pubOperateVO.setTotalCount(totalCount);
    } catch (Exception e) {
      logger.error("pc全文认领-PropertyUtils.copyProperties出错", e);
    }
  }


  private void buildPubFulltextPsnRcmd(PubFulltextPsnRcmdVO psnRcmdForm) throws PubException {
    Long fulltextFileId = psnRcmdForm.getFulltextFileId();
    Long srcPubId = psnRcmdForm.getSrcPubId();
    String imagePath = getFullTextImag(fulltextFileId, srcPubId);
    psnRcmdForm.setFullTextImagePath(imagePath);
    // 下载链接
    String downLoadUrl = "";
    if (new Integer(1).equals(psnRcmdForm.getDbId())) {
      // 说明是基准库的成果全文
      downLoadUrl = pubFulltextService.getPdwhFullTextDownloadUrl(srcPubId, false);
    } else {
      downLoadUrl = pubFulltextService.getRcmdFullTextDownloadUrl(fulltextFileId, false);
    }
    psnRcmdForm.setDownloadUrl(downLoadUrl);
    String srcImagePath = getDesFullTextImag(imagePath, psnRcmdForm.getDownloadUrl(), fulltextFileId);
    psnRcmdForm.setSrcFullTextImagePath(srcImagePath);
    // 成果短地址
    PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(psnRcmdForm.getPubId());
    if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
      psnRcmdForm.setPubShortUrl(domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
    }
    PubSnsPO pubSnsPO = psnRcmdForm.getPub();
    if (pubSnsPO != null) {
      psnRcmdForm.setFulltextTitle(pubSnsPO.getTitle());
    }
  }

  @Override
  public void confirmPubft(Long id, Long psnId) throws ServiceException {
    try {
      PubFulltextPsnRcmd psnRcmd = this.pubFulltextPsnRcmdDao.get(id);
      if (psnRcmd != null) {
        List<Long> pubIdList = new ArrayList<>();
        pubIdList.add(psnRcmd.getPubId());
        List<PubFullTextPO> pubFulltext = pubFulltextService.findPubfulltextList(pubIdList);
        if (pubFulltext == null || pubFulltext.size() == 0) {// 全文不存在才保存全文信息
          /*
           * ArchiveFile archiveFile =
           * this.archiveFileService.getArchiveFileById(psnRcmd.getFulltextFileId()); if (archiveFile != null)
           * { PubFulltextDTO fulltext = new PubFulltextDTO();
           * fulltext.setDes3fileId(Des3Utils.encodeToDes3(String.valueOf(psnRcmd.getFulltextFileId())));
           * fulltext.setFileName(archiveFile.getFileName()); fulltext.setPermission(0);
           * savaPubFulltext(psnRcmd.getPubId(), fulltext); }
           */
          pubFullTextService.uploadPubFulltext(psnRcmd.getPubId(), psnRcmd.getFulltextFileId(), psnId);
        }
        psnRcmd.setStatus(1);
        pubFulltextPsnRcmdDao.save(psnRcmd);
      }
    } catch (Exception e) {
      logger.error("确认是这篇成果的全文出错,id=" + id, e);
    }

  }

  @Override
  public void rejectPubft(Long id) throws ServiceException {
    try {
      PubFulltextPsnRcmd psnRcmd = this.pubFulltextPsnRcmdDao.get(id);
      if (psnRcmd != null) {
        psnRcmd.setStatus(2);
        pubFulltextPsnRcmdDao.save(psnRcmd);
      }
    } catch (Exception e) {
      logger.error("确认不是这篇成果的全文出错,id=" + id, e);
    }
  }


  @SuppressWarnings("unused")
  private String existsPdwhPub(PubFulltextPsnRcmd psnRcmd) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (psnRcmd != null) {
      if (psnRcmd.getDbId().equals(1)) {
        PubPdwhPO pubPdwhPO = pubPdwhDAO.get(psnRcmd.getSrcPubId());
        if (pubPdwhPO == null || pubPdwhPO.getStatus().equals(PubPdwhStatusEnum.DELETED)) {
          map.put("result", "error");
        }
      }
    } else {
      map.put("result", "error");
    }

    return JacksonUtils.mapToJsonStr(map);
  }

  private void savaPubFulltext(Long pubId, PubFulltextDTO fulltext) {
    try {
      Map<String, Object> result = new HashMap<>();
      result.put("pubId", String.valueOf(pubId));
      result.put("des3PubId", Des3Utils.encodeToDes3(String.valueOf(pubId)));
      result.put("pubHandlerName", PubHandlerEnum.UPDATE_SNS_FULLTEXT.name);
      result.put("fullText", fulltext);
      RestTemplateUtils.post(restTemplate, SAVEORUPDATE, JacksonUtils.mapToJsonStr(result));
    } catch (Exception e) {
      logger.error("更新全文出错,pubId=" + pubId, e);
    }

  }

  // 获取全文高清图片
  public String getDesFullTextImag(String imagePath, String downloadUrl, Long fulltextFileId) throws PubException {
    ArchiveFile file = archiveFileDao.findArchiveFileById(fulltextFileId);
    if (file != null && StringUtils.isNoneBlank(file.getFileName())
        && file.getFileName().matches(".*(\\.jpg|\\.jpeg|\\.gif|\\.png|\\.bmp).*")) {
      return downloadUrl;
    } else if (StringUtils.isNoneBlank(imagePath)) {
      if (imagePath.contains(ArchiveFileUtil.THUMBNAIL_SUFFIX)) {
        return imagePath.replace(ArchiveFileUtil.THUMBNAIL_SUFFIX, ".png");
      } else if (imagePath.contains(ArchiveFileUtil.PDWH_THUMBNAIL_SUFFIX)) {
        return imagePath.replace(ArchiveFileUtil.PDWH_THUMBNAIL_SUFFIX, "");
      } else if (imagePath.contains(ArchiveFileUtil.PDWH_THUMBNAIL_SUFFIX0)) {
        return imagePath.replace(ArchiveFileUtil.PDWH_THUMBNAIL_SUFFIX0, "");
      }
    }

    return "";
  }

  // 获取全文图片
  public String getFullTextImag(Long fulltextFileId, Long srcPubId) throws PubException {
    String imagePath = V8pubConst.PUB_DEFAULT_FULLTEXT_IMG_NEW;
    if (fulltextFileId != null && srcPubId != null) {
      String imagePathTemp = pubFulltextService.getFulltextImageUrl(srcPubId);
      if (StringUtils.isNotBlank(imagePathTemp)) {
        imagePath = imagePathTemp;
      }
    }
    return imagePath;
  }

  @Override
  public Page getRcmdFulltext(PubOperateVO pubOperateVO, Page page) throws ServiceException {
    List<PubFulltextPsnRcmdVO> list = new ArrayList<PubFulltextPsnRcmdVO>();
    try {
      Integer isAll = pubOperateVO.getIsAll();
      pubOperateVO.setIsAll(1); // 先全部推出来再分页
      List<PubFulltextPsnRcmd> result = pubFulltextPsnRcmdDao.queryRcmdPubFulltext(pubOperateVO, page);
      // 页面上传递的值，不能变，否则会出问题。2018-11-30 ajb
      pubOperateVO.setIsAll(isAll);
      if (CollectionUtils.isNotEmpty(result)) {
        try {
          for (PubFulltextPsnRcmd pubFulltextPsnRcmd : result) {
            PubFulltextPsnRcmdVO obj = new PubFulltextPsnRcmdVO();
            // dbId为1的是基准库成果全文
            // if (pubFulltextPsnRcmd.getDbId().equals(1)) {
            // PubPdwhPO pdwhPubPO = pubPdwhDAO.get(pubFulltextPsnRcmd.getSrcPubId());
            // if (pdwhPubPO == null || pdwhPubPO.getStatus().equals(PubPdwhStatusEnum.DELETED)) {
            // continue;
            // }
            // }
            PropertyUtils.copyProperties(obj, pubFulltextPsnRcmd);
            PubSnsPO pubSns = pubSnsDAO.get(pubFulltextPsnRcmd.getPubId());
            if (pubSns != null) {
              PubSnsPO pub = new PubSnsPO();
              PropertyUtils.copyProperties(pub, pubSns);
              obj.setPub(pub);
              list.add(obj);
            }
          }
          list = getPageList(list, page, isAll);

        } catch (Exception e) {
          logger.error("pc全文认领-PropertyUtils.copyProperties出错", e);
        }
      }
      if (CollectionUtils.isNotEmpty(list)) {
        page.setTotalCount(list.size());
        for (PubFulltextPsnRcmdVO psnRcmdForm : list) {
          buildPubFulltextPsnRcmd(psnRcmdForm);
        }
      }
      page.setResult(list);
    } catch (Exception e) {
      logger.error("获取当前用户成果全文推荐出错", e);
    }
    return page;
  }

  // PC端getRcmdFulltext方法与移动端不能共用，此方法用于移动端全文认领详情
  public Page getRcmdFulltextDetails(PubOperateVO pubOperateVO, Page page) throws ServiceException {
    List<PubFulltextPsnRcmdVO> list = new ArrayList<PubFulltextPsnRcmdVO>();
    try {
      page.setPageNo(pubOperateVO.getDetailPageNo());
      List<PubFulltextPsnRcmd> result = pubFulltextPsnRcmdDao.queryRcmdPubFulltext(pubOperateVO, page);
      if (CollectionUtils.isNotEmpty(result)) {
        try {
          for (PubFulltextPsnRcmd pubFulltextPsnRcmd : result) {
            PubFulltextPsnRcmdVO obj = new PubFulltextPsnRcmdVO();
            PropertyUtils.copyProperties(obj, pubFulltextPsnRcmd);
            PubSnsPO pubSns = pubSnsDAO.get(pubFulltextPsnRcmd.getPubId());
            if (pubSns != null) {
              PubSnsPO pub = new PubSnsPO();
              PropertyUtils.copyProperties(pub, pubSns);
              obj.setPub(pub);
              list.add(obj);
            }
          }
          list = getPageList(list, page, pubOperateVO.getIsAll());

        } catch (Exception e) {
          logger.error("pc全文认领-PropertyUtils.copyProperties出错", e);
        }
      }
      if (CollectionUtils.isNotEmpty(list)) {
        page.setTotalCount(list.size());
        for (PubFulltextPsnRcmdVO psnRcmdForm : list) {
          buildPubFulltextPsnRcmd(psnRcmdForm);
        }
      }
      page.setResult(list);
    } catch (Exception e) {
      logger.error("获取当前用户成果全文推荐出错", e);
    }
    return page;
  }


  private List<PubFulltextPsnRcmdVO> getPageList(List<PubFulltextPsnRcmdVO> list, Page page, Integer isAll) {
    if (CollectionUtils.isNotEmpty(list)) {
      Integer listSize = list.size();
      Integer start = page.getFirst() - 1;
      Integer end = start + 1;
      if (isAll == 1) {
        return list;
      } else if (start < listSize && end < listSize) {
        return list.subList(start, end);
      } else {
        return list.subList(0, 1);
      }
    }
    return new ArrayList<PubFulltextPsnRcmdVO>();
  }

  @Override
  public Long getFulltextCount(Long currentUserId) throws PubException {
    return pubFulltextPsnRcmdDao.queryRcmdFulltextCount(currentUserId);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getPubFulltextList(PubOperateVO pubOperateVO) throws PubException {
    Page<PubFulltextPsnRcmd> page = new Page<PubFulltextPsnRcmd>();
    // pubFulltextPsnRcmdDao.queryFulltextList(pubOperateVO, page);
    if (pubOperateVO.getPageSize() != null) {
      page.setPageSize(pubOperateVO.getPageSize());
    }
    pubOperateVO.setIsAll(1);
    pubFulltextPsnRcmdDao.queryRcmdPubFulltext(pubOperateVO, page);

    List<PubFulltextPsnRcmdVO> list = new ArrayList<PubFulltextPsnRcmdVO>();
    pubOperateVO.setPubRcmdftList(list);
    pubOperateVO.setTotalCount(page.getTotalCount());
    List<PubFulltextPsnRcmd> result = page.getResult();
    if (CollectionUtils.isNotEmpty(result)) {
      try {
        for (PubFulltextPsnRcmd pubFulltextPsnRcmd : result) {
          PubFulltextPsnRcmdVO obj = new PubFulltextPsnRcmdVO();
          PropertyUtils.copyProperties(obj, pubFulltextPsnRcmd);
          PubSnsPO pubSnsPO = pubSnsDAO.get(pubFulltextPsnRcmd.getPubId());
          if (pubSnsPO != null) {
            PubSnsPO pub = new PubSnsPO();
            // 复制属性，防止操作数据库对象
            PropertyUtils.copyProperties(pub, pubSnsPO);
            obj.setPub(pub);
          }
          obj.setDes3Id(Des3Utils.encodeToDes3(ObjectUtils.toString(pubFulltextPsnRcmd.getPubId(), "")));
          // 全文信息
          Long fulltextFileId = pubFulltextPsnRcmd.getFulltextFileId();
          Long srcPubId = pubFulltextPsnRcmd.getSrcPubId();
          String imagePath = getFullTextImag(fulltextFileId, srcPubId);
          obj.setFullTextImagePath(imagePath);
          obj.setDownloadUrl(fileDownloadUrlService.getShortDownloadUrl(FileTypeEnum.RCMD_FULLTEXT, fulltextFileId));
          String srcImagePath = getDesFullTextImag(imagePath, obj.getDownloadUrl(), fulltextFileId);
          obj.setSrcFullTextImagePath(srcImagePath);
          list.add(obj);
        }
      } catch (Exception e) {
        logger.error("mobile全文认领-PropertyUtils.copyProperties出错", e);
      }
    }
  }

  @Override
  public PubFulltextPsnRcmd getPubFulltext(Long psnId, Long pubId) {
    return pubFulltextPsnRcmdDao.getPubFulltext(psnId, pubId);
  }

  @Override
  public void delePubFulltext(Long psnId, Long pubId) {
    pubFulltextPsnRcmdDao.delePubFulltext(psnId, pubId);

  }

  @Override
  public PubFulltextPsnRcmd get(Long id) throws ServiceException {
    return pubFulltextPsnRcmdDao.get(id);
  }

  @Override
  public void save(PubFulltextPsnRcmd entity) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void update(PubFulltextPsnRcmd entity) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOrUpdate(PubFulltextPsnRcmd entity) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(PubFulltextPsnRcmd entity) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteBySnsPubId(Long srcPubId) throws PubException {
    pubFulltextPsnRcmdDao.deleteBySnsPubId(srcPubId);
  }

  @Override
  public PubFulltextPsnRcmd getPubFulltextPsnRcmd(Long pubId) throws PubException {
    return pubFulltextPsnRcmdDao.get(pubId);

  }

}
