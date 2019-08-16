package com.smate.web.mobile.share.service;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import com.smate.web.mobile.consts.SmateShareConstant;
import com.smate.web.mobile.share.vo.ShareResShowInfo;
import com.smate.web.mobile.share.vo.SmateShareVO;
import com.smate.web.mobile.utils.RestUtils;

/**
 * 资源信息查找服务
 * 
 * @author wsn
 * @date May 31, 2019
 */
@Service("findResInfoService")
@Transactional(rollbackFor = Exception.class)
public class FindResInfoServiceImpl implements FindResInfoService {

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public void findResInfo(SmateShareVO vo) {
    if (StringUtils.isNotBlank(vo.getResType())) {
      switch (vo.getResType()) {
        // 资助机构
        case SmateShareConstant.SHARE_AGENCY_RES_TYPE:
          this.findAgencyInfo(vo);
          break;
        // 基金
        case SmateShareConstant.SHARE_FUND_RES_TYPE:
          this.findFundInfo(vo);
          break;
        // 个人成果
        case SmateShareConstant.SHARE_SNS_PUB_RES_TYPE:
          this.findSnsPubInfo(vo);
          break;
        // 基准库成果
        case SmateShareConstant.SHARE_PDWH_PUB_RES_TYPE:
          this.findPdwhPubInfo(vo);
          break;
        // 群组文件
        case SmateShareConstant.SHARE_GROUP_FILE_RES_TYPE:
          this.findGroupFileInfo(vo);
          break;
        // 个人文件
        case SmateShareConstant.SHARE_PSN_FILE_RES_TYPE:
          this.findPsnFileInfo(vo);
          break;
        // 项目
        case SmateShareConstant.SHARE_PRJ_RES_TYPE:
          this.findPrjInfo(vo);
          break;
        default:
          break;
      }
    }

  }


  /**
   * 获取项目信息
   * 
   * @param vo
   */
  @SuppressWarnings("unchecked")
  private void findPrjInfo(SmateShareVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("des3PrjId", StringUtils.trimToEmpty(vo.getDes3ResId()));
    Map<String, Object> result =
        restTemplate.postForObject(vo.getDomainMobile() + SmateShareConstant.FIND_SHARE_PRJ_INFO_URL,
            RestUtils.buildPostRequestEntity(params), Map.class);
    if (result != null && "success".equalsIgnoreCase(Objects.toString(result.get("status"), ""))) {
      ShareResShowInfo info = new ShareResShowInfo();
      info.setTitle(Objects.toString(result.get("title"), ""));
      info.setAuthorNames(Objects.toString(result.get("authorNames"), ""));
      info.setBriefDesc(HtmlUtils.htmlUnescape(Objects.toString(result.get("briefDesc"), "")));
      vo.setShowInfo(info);
    }
  }


  /**
   * 获取个人库成果详情
   * 
   * @param vo
   */
  @SuppressWarnings("unchecked")
  private void findSnsPubInfo(SmateShareVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("des3PubId", vo.getDes3ResId());
    params.add("type", "sns");
    Map<String, Object> result =
        restTemplate.postForObject(vo.getDomainScm() + SmateShareConstant.FIND_SHARE_PUB_INFO_URL,
            RestUtils.buildPostRequestEntity(params), Map.class);
    if (result != null && "success".equalsIgnoreCase(Objects.toString(result.get("status"), ""))) {
      ShareResShowInfo info = new ShareResShowInfo();
      info.setTitle(Objects.toString(result.get("title"), ""));
      info.setAuthorNames(Objects.toString(result.get("authorNames"), ""));
      info.setBriefDesc(HtmlUtils.htmlUnescape(Objects.toString(result.get("briefDesc"), "")));
      info.setHasFullText(Objects.toString(result.get("hasFullText"), ""));
      info.setImgSrc(Objects.toString(result.get("thumbnailPath"), ""));
      info.setPublishYear(Objects.toString(result.get("publishYear"), ""));
      vo.setShowInfo(info);
    }
  }



  /**
   * 获取基准库成果详情
   * 
   * @param vo
   */
  @SuppressWarnings("unchecked")
  private void findPdwhPubInfo(SmateShareVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("des3PubId", vo.getDes3ResId());
    params.add("type", "pdwh");
    Map<String, Object> result =
        restTemplate.postForObject(vo.getDomainScm() + SmateShareConstant.FIND_SHARE_PUB_INFO_URL,
            RestUtils.buildPostRequestEntity(params), Map.class);
    if (result != null && "success".equalsIgnoreCase(Objects.toString(result.get("status"), ""))) {
      ShareResShowInfo info = new ShareResShowInfo();
      info.setTitle(Objects.toString(result.get("title"), ""));
      info.setAuthorNames(Objects.toString(result.get("authorNames"), ""));
      info.setBriefDesc(HtmlUtils.htmlUnescape(Objects.toString(result.get("briefDesc"), "")));
      info.setHasFullText(Objects.toString(result.get("hasFullText"), ""));
      info.setImgSrc(Objects.toString(result.get("thumbnailPath"), ""));
      info.setPublishYear(Objects.toString(result.get("publishYear"), ""));
      vo.setShowInfo(info);
    }
  }


  /**
   * 获取基金信息
   * 
   * @param vo
   */
  @SuppressWarnings("unchecked")
  private void findFundInfo(SmateShareVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("des3FundId", vo.getDes3ResId());
    Map<String, Object> result =
        restTemplate.postForObject(vo.getDomainMobile() + SmateShareConstant.FIND_SHARE_FUND_INFO_URL,
            RestUtils.buildPostRequestEntity(params), Map.class);
    if (result != null && "success".equalsIgnoreCase(Objects.toString(result.get("status"), ""))) {
      ShareResShowInfo info = new ShareResShowInfo();
      info.setTitle(Objects.toString(result.get("title"), ""));
      info.setBriefDesc(HtmlUtils.htmlUnescape(Objects.toString(result.get("showDesc"), "")));
      info.setAgencyName(Objects.toString(result.get("agencyName"), ""));
      info.setImgSrc(Objects.toString(result.get("logo"), ""));
      // info.setPublishYear(Objects.toString(result.get("time"), ""));
      vo.setShowInfo(info);
    }
  }



  /**
   * 获取资助机构信息
   * 
   * @param vo
   */
  @SuppressWarnings("unchecked")
  private void findAgencyInfo(SmateShareVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("Des3FundAgencyId", vo.getDes3ResId());
    Map<String, Object> result =
        restTemplate.postForObject(vo.getDomainMobile() + SmateShareConstant.FIND_SHARE_AGENCY_INFO_URL,
            RestUtils.buildPostRequestEntity(params), Map.class);
    if (result != null && "success".equalsIgnoreCase(Objects.toString(result.get("status"), ""))) {
      ShareResShowInfo info = new ShareResShowInfo();
      info.setTitle(Objects.toString(result.get("zhName"), ""));
      info.setAddress(Objects.toString(result.get("address"), ""));
      info.setImgSrc(Objects.toString(result.get("logo"), "/resmod/smate-pc/img/logo_instdefault.png"));
      vo.setShowInfo(info);
    }
  }



  /**
   * 获取新闻信息
   * 
   * @param vo
   */
  @SuppressWarnings("unchecked")
  private void findNewsInfo(SmateShareVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("des3NewsId", vo.getDes3ResId());
    Map<String, Object> result =
        restTemplate.postForObject(vo.getDomainMobile() + SmateShareConstant.FIND_SHARE_NEWS_INFO_URL,
            RestUtils.buildPostRequestEntity(params), Map.class);
    if (result != null && "success".equalsIgnoreCase(Objects.toString(result.get("status"), ""))) {
      ShareResShowInfo info = new ShareResShowInfo();
      info.setTitle(Objects.toString(result.get("title"), ""));
      info.setBriefDesc(Objects.toString(result.get("brief"), ""));
      info.setImgSrc(Objects.toString(result.get("image"), ""));
      vo.setShowInfo(info);
    }
  }


  /**
   * 获取群组文件信息
   * 
   * @param vo
   */
  @SuppressWarnings("unchecked")
  private void findGroupFileInfo(SmateShareVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("des3GrpFileId", vo.getDes3ResId());
    Map<String, Object> result =
        restTemplate.postForObject(vo.getDomainMobile() + SmateShareConstant.FIND_SHARE_GROUP_FILE_INFO_URL,
            RestUtils.buildPostRequestEntity(params), Map.class);
    if (result != null && "200".equalsIgnoreCase(Objects.toString(result.get("status"), ""))) {
      ShareResShowInfo info = new ShareResShowInfo();
      info.setTitle(Objects.toString(result.get("fileName"), ""));
      info.setBriefDesc(Objects.toString(result.get("fileDesc"), ""));
      info.setImgSrc(Objects.toString(result.get("imgThumbUrl"), ""));
      info.setFileType(Objects.toString(result.get("fileType"), ""));
      info.setResDes3GrpId(Objects.toString(result.get("fileDes3GrpId"), ""));
      this.rebuildFileIconSrc(info);
      vo.setShowInfo(info);
    }
  }



  /**
   * 获取个人文件信息
   * 
   * @param vo
   */
  @SuppressWarnings("unchecked")
  private void findPsnFileInfo(SmateShareVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("des3GrpFileId", vo.getDes3ResId());
    Map<String, Object> result =
        restTemplate.postForObject(vo.getDomainMobile() + SmateShareConstant.FIND_SHARE_GROUP_FILE_INFO_URL,
            RestUtils.buildPostRequestEntity(params), Map.class);
    if (result != null && "200".equalsIgnoreCase(Objects.toString(result.get("status"), ""))) {
      ShareResShowInfo info = new ShareResShowInfo();
      info.setTitle(Objects.toString(result.get("fileName"), ""));
      info.setBriefDesc(Objects.toString(result.get("fileDesc"), ""));
      info.setImgSrc(Objects.toString(result.get("imgThumbUrl"), ""));
      info.setFileType(Objects.toString(result.get("fileType"), ""));
      this.rebuildFileIconSrc(info);
      vo.setShowInfo(info);
    }
  }

  /**
   * 重新构建文件图片地址
   * 
   * @param info
   */
  private void rebuildFileIconSrc(ShareResShowInfo info) {
    if (StringUtils.isNotBlank(info.getFileType())) {
      switch (info.getFileType()) {
        case "txt":
          info.setImgSrc("/resmod/smate-pc/img/fileicon_txt.png");
          break;
        case "ppt":
        case "pptx":
          info.setImgSrc("/resmod/smate-pc/img/fileicon_ppt.png");
          break;
        case "doc":
        case "docx":
          info.setImgSrc("/resmod/smate-pc/img/fileicon_doc.png");
          break;
        case "rar":
        case "zip":
          info.setImgSrc("/resmod/smate-pc/img/fileicon_zip.png");
          break;
        case "xls":
        case "xlsx":
          info.setImgSrc("/resmod/smate-pc/img/fileicon_xls.png");
          break;
        case "pdf":
          info.setImgSrc("/resmod/smate-pc/img/fileicon_pdf.png");
          break;
        case "":
          info.setImgSrc("/resmod/smate-pc/img/fileicon_default.png");
          break;
        default:
          break;
      }
    } else if (StringUtils.isBlank(info.getImgSrc())) {
      info.setImgSrc("/resmod/smate-pc/img/fileicon_default.png");
    }
  }



}
