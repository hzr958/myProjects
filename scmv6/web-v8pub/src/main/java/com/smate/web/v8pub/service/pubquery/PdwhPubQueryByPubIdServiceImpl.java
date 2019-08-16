package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.pdwh.PdwhPubIndexUrlDao;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.po.pdwh.PdwhPubIndexUrl;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubFullTextService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PubListResult;

/**
 * 通过成果id 查询基准库的成果
 * 
 * @author aijiangbin
 * @date 2018年8月14日
 */
@Transactional(rollbackFor = Exception.class)
public class PdwhPubQueryByPubIdServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubPdwhService pubPdwhService;
  @Resource
  private PdwhPubFullTextService pdwhPubFullTextService;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (pubQueryDTO.getSearchPubId() == null || pubQueryDTO.getSearchPubId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的成果searchPubId 不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    PubPdwhPO pdwhPO = pubPdwhService.get(pubQueryDTO.getSearchPubId());
    List<PubPO> list = new ArrayList<>();
    if (pdwhPO != null) {
      list.add(pdwhPO);
    }
    pubQueryDTO.setPubList(list);
  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult listResult = new PubListResult();
    List<PubPO> pubList = pubQueryDTO.getPubList();
    List<Long> pubIds = new ArrayList<>();
    pubQueryDTO.setPubIds(pubIds);
    if (pubList != null && pubList.size() > 0) {
      List<PubInfo> list = new ArrayList<>(pubList.size() + 1);
      for (PubPO pubPO : pubList) {
        PubPdwhPO pubPdwhPO = (PubPdwhPO) pubPO;
        PubInfo pubInfo = new PubInfo();
        PubPdwhDetailDOM detailDOM = pubPdwhDetailService.getByPubId(pubPdwhPO.getPubId());
        pubInfo.setPubId(pubPdwhPO.getPubId());
        pubInfo.setTitle(pubPO.getTitle());
        pubInfo.setBriefDesc(pubPO.getBriefDesc());
        pubInfo.setPubType(pubPdwhPO.getPubType());
        pubInfo.setAuthorNames(pubPO.getAuthorNames());
        if (pubPO.getPubType() == 5) {
          PatentInfoBean typeInfo = (PatentInfoBean) detailDOM.getTypeInfo();
          pubInfo.setApplicationNo(typeInfo.getApplicationNo());
          pubInfo.setPublicationOpenNo(typeInfo.getPublicationOpenNo());
        }
        if (pubPO.getPubType() == 12) {
          StandardInfoBean typeInfo = (StandardInfoBean) detailDOM.getTypeInfo();
          pubInfo.setStandardNo(typeInfo.getStandardNo());
        }
        if (pubPO.getPubType() == 13) {
          SoftwareCopyrightBean typeInfo = (SoftwareCopyrightBean) detailDOM.getTypeInfo();
          pubInfo.setRegisterNo(typeInfo.getRegisterNo());
        }
        pubIds.add(pubPdwhPO.getPubId());
        list.add(pubInfo);
      }
      buildPdwhfulltext(list, pubQueryDTO);
      buildPdwhPubIndexUrl(list, pubQueryDTO.getPubIds());
      listResult.setResultList(list);
      listResult.setTotalCount(NumberUtils.toInt(pubQueryDTO.getTotalCount().toString()));
    }

    return listResult;
  }

  public void buildPdwhfulltext(List<PubInfo> list, PubQueryDTO pubQueryDTO) {
    List<PdwhPubFullTextPO> fulltextList = pdwhPubFullTextService.queryPdwhFullTextByIds(pubQueryDTO);
    if (fulltextList != null && fulltextList.size() > 0) {
      for (PubInfo info : list) {
        for (PdwhPubFullTextPO pdwhPubFullTextPO : fulltextList) {
          if (info.getPubId().longValue() == pdwhPubFullTextPO.getPdwhPubId().longValue()) {
            // 不是短地址链接
            String fullTextDownloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.PDWH_FULLTEXT, info.getPubId());
            info.setFullTextDownloadUrl(fullTextDownloadUrl);
            info.setFullTextFieId(pdwhPubFullTextPO.getFileId());
            String fullTextImgUrl =
                StringUtils.isNotBlank(pdwhPubFullTextPO.getThumbnailPath()) ? pdwhPubFullTextPO.getThumbnailPath()
                    : V8pubConst.PUB_DEFAULT_FULLTEXT_IMG_NEW;
            info.setFullTextImgUrl(fullTextImgUrl);
            info.setHasFulltext(1);
            break;
          }
        }
        // 设置默认图片
        if (info.getHasFulltext().intValue() == 0) {
          info.setFullTextImgUrl(V8pubConst.PUB_DEFAULT_NOT_FULLTEXT_IMG_NEW);
        }

      }
    }
  }

  /**
   * 构建个基准库成果的短地址
   * 
   * @param list
   * @param pubIdList
   */
  public void buildPdwhPubIndexUrl(List<PubInfo> list, List<Long> pubIdList) {
    List<PdwhPubIndexUrl> urlList = pdwhPubIndexUrlDao.getByIds(pubIdList);
    if (urlList != null && list != null) {
      for (PubInfo info : list) {
        for (PdwhPubIndexUrl pubIndexUrl : urlList) {
          if (pubIndexUrl.getPubId().longValue() == info.getPubId().longValue()) {
            String indexUrl = this.domainscm + "/" + ShortUrlConst.S_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
            info.setPubIndexUrl(indexUrl);
            break;
          }
        }
        // 默认的url
        if (StringUtils.isBlank(info.getPubIndexUrl())) {
          String url =
              this.domainscm + "/pubweb/details/showpdwh?des3Id=" + Des3Utils.encodeToDes3(info.getPubId().toString());
          info.setPubIndexUrl(url);
        }

      }
    }
  }
}
