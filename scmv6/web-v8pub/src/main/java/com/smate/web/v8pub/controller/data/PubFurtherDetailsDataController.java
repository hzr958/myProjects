package com.smate.web.v8pub.controller.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.pub.vo.Accessory;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubStatisticsService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import com.smate.web.v8pub.service.pubdetailquery.PubDetailHandleService;
import com.smate.web.v8pub.service.region.ConstRegionService;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.service.sns.PubStatisticsService;
import com.smate.web.v8pub.service.sns.fulltext.PubUrlService;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 成果详情完整数据Controller
 * 
 * @author LIJUN
 * @date 2018年8月25日
 */
@RestController
public class PubFurtherDetailsDataController {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubDetailHandleService pubDetailHandleService;
  @Autowired
  private PubStatisticsService newPubStatisticsService;
  @Autowired
  private PsnPubService psnPubService;
  @Autowired
  private PubSnsDetailService pubSnsDetailService;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private ConstRegionService ConstRegionService;
  @Autowired
  private PdwhPubStatisticsService newPdwhPubStatisticsService;
  @Autowired
  private PubUrlService pubURlService;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;
  @Autowired
  private GroupPubService groupPubService;
  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private PubPdwhService pubPdwhService;
  @Value("${domainscm}")
  private String domain;

  /**
   * 个人库成果详情页面 {"des3PubId":"******"}
   * 
   * @return
   */
  @RequestMapping("/data/pub/details/sns")
  public Object pubDetails(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> result = new HashMap<>();
    Map<String, Object> params = new HashMap<>();
    PubDetailVO pubDetailVO = new PubDetailVO<>();
    params.put(V8pubConst.DESC_PUB_ID, pubOperateVO.getDes3PubId());
    params.put("serviceType", "snsPub");
    if (pubOperateVO.getDes3GrpId() != null) {
      params.put("des3GrpId", pubOperateVO.getDes3GrpId());
    }
    params.put("psnId", pubOperateVO.getPsnId());
    if (StringUtils.isBlank((String) params.get(V8pubConst.DESC_PUB_ID))) {
      result.put("status", "not exists");
    }
    Long psnId = pubOperateVO.getPsnId();
    try {
      if (psnId != null && psnId > 0L) {
        pubDetailVO.setIsLogin(true);
      }
      pubDetailVO = pubDetailHandleService.queryPubDetail(params);
      if (pubDetailVO != null) {
        // 还要获取成果状态， 0：正常，1：被删除
        PubSnsPO pub = pubSnsService.get(pubDetailVO.getPubId());
        if (pub == null || (pub != null && pub.getStatus().getValue() == 1)) {
          result.put("status", "hasDeleted");
        } else {
          pubDetailVO.setPsnId(psnId);
          Long pubOwnerPsnId = buildOwnerPsnId(pubDetailVO, pubOperateVO);
          if (psnId.equals(pubOwnerPsnId)) {
            pubDetailVO.setOwner(true);
          }
          pubSnsDetailService.checkPubAuthority(pubDetailVO);
          boolean shareView = false;
          if (StringUtils.isNotBlank(pubOperateVO.getDes3relationid())) {// 分享id不为空，用于判断隐私分享
            shareView = pubSnsDetailService.sharePubView(pubOperateVO.getDes3relationid(), pub.getPubId(),
                pubOwnerPsnId, psnId);
          }
          // 登录且非本人 隐私成果： "该论文由于个人隐私设置，无法查看。请查看其它论文"
          if (pubDetailVO.getPermission() == 4 && pubDetailVO.getIsOwner() != true && !shareView) {
            result.put("status", "no permission");
          } else {
            // 初始化统计信息
            newPubStatisticsService.initStatistics(pubDetailVO);
            // 当前登录用户头像
            pubSnsDetailService.getPsnAvatars(pubDetailVO);
            pubDetailVO.setDoiUrl(buildDoiUrl(pubDetailVO.getDoi()));
            // 初始化收藏信息
            buildPubCollect(pubDetailVO, PubDbEnum.SNS);
            // 获取拥有者信息
            pubSnsDetailService.getPubOwnerInfo(pubDetailVO);
            // 获取评论数
            /*
             * //SCM-24685 Long comentCount = pubSnsDetailService.getCommentNumber(pubDetailVO.getPubId()); if
             * (comentCount != null) { pubDetailVO.setCommentCount(comentCount.intValue()); }
             */
            // 构建附件信息
            fulltextAttachment(pubDetailVO);
            // 构建全文下载链接
            String fullTextDownloadUrl =
                pubFullTextService.getSnsFullTextDownloadUrl(pubDetailVO.getPubId(), psnId, true);
            pubDetailVO.setFullTextDownloadUrl(fullTextDownloadUrl);
            // 其他类似全文条数
            Long snsSimilarCount = pubFullTextService.getSimilarCount(pubDetailVO.getPubId());
            pubDetailVO.setSnsSimilarCount(snsSimilarCount);
            result.put("status", "success");
          }
        }
      } else {
        result.put("status", "not exists");
      }
      result.put("result", pubDetailVO);
    } catch (Exception e) {
      result.put("status", "error");
      logger.error("个人库成果详情页面获取数据出错,pubId=" + pubOperateVO.getPubId(), e);
    }
    return result;
  }

  /**
   * 基准库成果详情页面{"pubId":"21000000021"}
   * 
   * @return
   */
  @RequestMapping("/data/pub/details/pdwh")
  public Object pubPdwhDetails(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> result = new HashMap<>();
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    PubDetailVO pubDetailVO = new PubDetailVO<>();
    params.put(V8pubConst.DESC_PUB_ID, pubOperateVO.getDes3PubId());
    params.put("serviceType", "pdwhPub");
    if (StringUtils.isBlank(V8pubConst.DESC_PUB_ID) || !checkPdwhPubId(pubOperateVO.getPubId())) {
      result.put("result", "no info");
    }
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (psnId != null && psnId > 0L) {
        pubDetailVO.setIsLogin(true);
        pubDetailVO = pubDetailHandleService.queryPubDetail(params);
        if (pubDetailVO != null) {
          pubDetailVO.setPsnId(psnId);
          pubSnsDetailService.getPsnAvatars(pubDetailVO);
          pubDetailVO.setDoiUrl(buildDoiUrl(pubDetailVO.getDoi()));
          // 构建作者信息
          pubPdwhDetailService.buildPdwhPsnInfo(pubDetailVO);
          // 初始化统计信息
          newPdwhPubStatisticsService.pdwhInitStatistics(pubDetailVO);
          pubDetailVO.setPubSum(pubOperateVO.getPubSum());
          // 初始化收藏信息
          buildPubCollect(pubDetailVO, PubDbEnum.PDWH);
          // 其他类似全文条数
          Long pdwhSimilarCount = pubFullTextService.getPdwhSimilarCount(pubDetailVO.getPubId());
          pubDetailVO.setPdwhSimilarCount(pdwhSimilarCount);
          result.put("result", pubDetailVO);
          view.addObject("pubDetailVO", pubDetailVO);
        }
      }
    } catch (Exception e) {
      logger.error("基准库库成果详情页面获取数据出错,pubId=" + pubOperateVO.getPubId(), e);
    }
    return result;
  }

  // 校验基准库成果是否存在
  public boolean checkPdwhPubId(Long pdwhPubId) {
    PubPdwhPO pdwhPub = pubPdwhService.get(pdwhPubId);
    if (pdwhPub == null || pdwhPub.getStatus().equals(PubPdwhStatusEnum.DELETED)) {
      return false;
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  private void fulltextAttachment(PubDetailVO pubDetailVO) {
    List<Accessory> accessorys = pubDetailVO.getAccessorys();
    Long psnId = pubDetailVO.getPsnId();
    if (CollectionUtils.isNotEmpty(accessorys)) {
      for (int i = 0; i < accessorys.size(); i++) {
        Accessory as = accessorys.get(i);
        String fileExt = FileUtils.getFileNameExtensionStr(as.getFileName());
        String fileType = FileUtils.SYMBOL_DOT + fileExt;
        as.setFileType(fileType);
        if (as.getPermission() == null) {
          as.setPermission(0);
        }
        if (psnId != 0 && psnId.equals(pubDetailVO.getPubOwnerPsnId()) && !"0".equals(as.getPermission().toString())) {
          if (as.getFileId() != null && as.getPubId() != null) {
            as.setFileUrl(fileDownUrlService.getDownloadAttachmentUrl(FileTypeEnum.FULLTEXT_ATTACHMENT, as.getFileId(),
                pubDetailVO.getPubId()));
          }
          as.setIsUpdown(true);
        } else if ("0".equals(as.getPermission().toString())) {
          as.setFileUrl(fileDownUrlService.getShortDownloadUrl(FileTypeEnum.FULLTEXT_ATTACHMENT, as.getFileId()));
          as.setIsUpdown(true);
        }
        if (!as.getIsUpdown()) {
          accessorys.remove(i);
        }

      }
    }
  }

  // 成果拥有者
  private Long buildOwnerPsnId(PubDetailVO pubDetailVO, PubOperateVO pubOperateVO) {
    Long pubOwnerPsnId = psnPubService.getPubOwnerId(pubDetailVO.getPubId());
    if (!NumberUtils.isNullOrZero(pubOperateVO.getGrpId())) {
      GroupPubPO groupPub = groupPubService.getByGrpIdAndPubId(pubOperateVO.getGrpId(), pubDetailVO.getPubId());
      if (groupPub != null) {
        pubOwnerPsnId = groupPub.getOwnerPsnId();
      }
    }
    if (NumberUtils.isNullOrZero(pubOwnerPsnId)) {
      GroupPubPO groupPub = groupPubService.getByPubId(pubDetailVO.getPubId());
      if (groupPub != null) {
        pubOwnerPsnId = groupPub.getOwnerPsnId();
      }
    }
    if (!NumberUtils.isNullOrZero(pubOwnerPsnId)) {
      pubDetailVO.setDes3PsnId(Des3Utils.encodeToDes3(String.valueOf(pubOwnerPsnId)));
    }
    pubDetailVO.setPubOwnerPsnId(pubOwnerPsnId);
    return pubOwnerPsnId;
  }

  /**
   * 是否已经收藏
   * 
   * @param form
   */
  private void buildPubCollect(PubDetailVO pubDetailVO, PubDbEnum pubDb) {
    try {
      boolean isCollect = newPubStatisticsService.isCollectedPub(pubDetailVO.getPsnId(), pubDetailVO.getPubId(), pubDb);
      pubDetailVO.setIsCollection(isCollect == true ? 1 : 0);
    } catch (Exception e) {
      logger.error("查询成果是否已经收藏出错 pubId=" + pubDetailVO.getPubId() + ",psnId=" + pubDetailVO.getPsnId());
    }
  }

  private String buildDoiUrl(String doi) {
    String resultDoi = "";
    if (StringUtils.isNotBlank(doi)) {
      resultDoi = "http://dx.doi.org/" + doi;
    }
    return resultDoi;
  }
}
