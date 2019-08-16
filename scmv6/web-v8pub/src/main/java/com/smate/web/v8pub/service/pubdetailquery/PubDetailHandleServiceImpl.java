package com.smate.web.v8pub.service.pubdetailquery;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.fulltext.CheckHasAgreeRecordDao;
import com.smate.web.v8pub.dao.sns.PubFullTextDAO;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.service.sns.PubFullTextService;

/**
 * 
 * @author aijiangbin
 * @date 2018年8月3日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PubDetailHandleServiceImpl implements PubDetailHandleService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private FileDownloadUrlService fileDownloadUrlService;
  @Value("${domainscm}")
  public String domainscm;
  @Autowired
  private PubFullTextDAO pubFullTextDAO;
  @Autowired
  private CheckHasAgreeRecordDao checkHasAgreeRecordDao;

  public HashMap<String, PubDetailQueryService> serviceMap;

  @Override
  public Map<String, String> checkParmas(Map<String, Object> params) {
    Map map = new HashMap<>();
    if (params.get(V8pubConst.DESC_PUB_ID) == null) {
      map.put(V8pubConst.ERROR_MSG, "DESC_PUB_ID不能为空");
      return map;
    }
    if (params.get(V8pubConst.SERVICE_TYPE) == null
        || serviceMap.get(params.get(V8pubConst.SERVICE_TYPE).toString()) == null) {
      map.put(V8pubConst.ERROR_MSG, "serviceType 错误");
      return map;
    }

    return null;
  }

  @Override
  public PubDetailVO queryPubDetail(Map<String, Object> params) {
    PubDetailQueryService service = serviceMap.get(params.get(V8pubConst.SERVICE_TYPE).toString());
    Long grpId = null;
    if (params.get("des3GrpId") != null) {
      grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(params.get("des3GrpId").toString()));
    }
    PubDetailVO pubDetail = null;
    if (params.get(V8pubConst.DESC_PUB_ID) != null) {
      pubDetail = service.queryPubDetail(
          NumberUtils.toLong(Des3Utils.decodeFromDes3(params.get(V8pubConst.DESC_PUB_ID).toString())), grpId);
    }
    if (pubDetail == null) {
      // 远程获取成果详情时 null 会报错
      pubDetail = new PubDetailVO();
    }
    /**
     * SCM-23246,全文逻辑调整,由于psnId在接口中无法获取,故将构造全文逻辑放置此处
     */
    buildPubFulltext(pubDetail,
        NumberUtils.toLong(Des3Utils.decodeFromDes3(String.valueOf(params.get(V8pubConst.DESC_PUB_ID)))),
        NumberUtils.toLong(String.valueOf(params.get("psnId"))));
    return pubDetail;
  }


  /**
   * 构建全文
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public void buildPubFulltext(PubDetailVO pubDetailVO, Long pubId, Long reqPsnId) {
    // 全文逻辑id 主键唯一的
    PubFulltextDTO fullText = new PubFulltextDTO();
    PubFullTextPO fullTextPO = pubFullTextService.get(pubId);
    if (fullTextPO != null) {
      String des3fileId = Des3Utils.encodeToDes3(Objects.toString(fullTextPO.getFileId()));
      fullText.setDes3fileId(des3fileId);
      fullText.setFileName(fullTextPO.getFileName());
      /**
       * 检查当前请求全文是否有同意记录
       */
      boolean hasAgreeRecord = checkHasAgreeRecordDao.checkHasAgreeRecord(reqPsnId, pubId, PubDbEnum.SNS);
      if (hasAgreeRecord) {
        fullText.setPermission(0);
      } else {
        fullText.setPermission(fullTextPO.getPermission());
      }
      if (StringUtils.isNotBlank(fullTextPO.getThumbnailPath())) {
        fullText.setThumbnailPath(this.domainscm + fullTextPO.getThumbnailPath());
      }
      pubDetailVO.setFullText(fullText);
      String downloadUrl = fileDownloadUrlService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT, fullTextPO.getFileId(),
          fullTextPO.getPubId());
      pubDetailVO.setFullTextDownloadUrl(downloadUrl);
      String simpleDownLoadUrl =
          fileDownloadUrlService.getShortDownloadUrl(FileTypeEnum.ARCHIVE_FILE, fullTextPO.getFileId());
      pubDetailVO.setSimpleDownLoadUrl(simpleDownLoadUrl);
    }
  }

  public HashMap<String, PubDetailQueryService> getServiceMap() {
    return serviceMap;
  }

  public void setServiceMap(HashMap<String, PubDetailQueryService> serviceMap) {
    this.serviceMap = serviceMap;
  }
}
