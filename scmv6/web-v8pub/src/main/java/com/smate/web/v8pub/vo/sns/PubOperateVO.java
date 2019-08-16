package com.smate.web.v8pub.vo.sns;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.vo.PubFulltextPsnRcmdVO;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class PubOperateVO {
  private Long psnId;
  private Long pubId;
  private String des3PubId;
  private Integer operate; // 赞操作 0:取消赞 1:点赞
  private String content; // 评论内容
  private String comment;// 分享成果时的评论
  private Integer platform;// 分享平台 微信、微博等 参照SharePlatformEnum
  private Long grpId; // 群组id
  private Long readPsnId;// 被阅读人id
  private String des3ReadPsnId;
  private String pubDb;// 成果所属的库，0-基准库，1-个人库 pubDb ：基准库：PDWH，个人：SNS
  private Integer pubSum;// 个人成果列表总数，用于控制滑动论文详情尾页的判断
  private Integer collectOperate; // 收藏操作： 0：收藏， 1：取消收藏
  private String ownerDes3PsnId;
  private String des3PsnId;
  private PubFulltextPsnRcmdVO pubRcmdft;
  private Integer isAll = 0;// 个人主页-成果模块-全文认领-查看全部
  private Integer fulltextCount = 0;// 操作了的全文成果数
  private String des3FileId;
  private List<PubFulltextPsnRcmdVO> pubRcmdftList;
  private Long totalCount = 0L;
  private String sharePsnGroupIds;// 被分享人或群组Id
  private Long sharePsnGroupId;
  private Integer shareCount;
  public String des3GrpId; // 加密群组ID
  private String ids; // PUB_FULLTEXT_PSN_RCMD表id，多个逗号隔开
  private Integer detailPageNo; // 全文推荐详情开始的顺序
  private boolean needLikeStatus = false; // 是否要查出赞状态（已赞过还是未赞过）
  private boolean needCollectStatus = false; // 是否要查出收藏状态（已收藏过还是未收藏过）
  private String des3relationid;// 消息关系表V_MSG_RELATION id
  private String des3Id; // 加密的ID(随机数 + | + 成果ID字符串加密而来)
  private Integer pageSize; // 每页查询的数量
  private Integer awardTimes;// 点赞数
  private String keyword = "";

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPsnId() {
    if (psnId == null && StringUtils.isNotBlank(des3PsnId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3PsnId);
      if (des3Str == null) {
        return psnId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getOperate() {
    return operate;
  }

  public void setOperate(Integer operate) {
    this.operate = operate;
  }

  public Long getPubId() {
    if (pubId == null && StringUtils.isNotBlank(des3PubId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3PubId);
      if (des3Str == null) {
        return pubId;
      } else {
        return Long.valueOf(des3Str);
      }

    }
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Integer getPlatform() {
    return platform;
  }

  public void setPlatform(Integer platform) {
    this.platform = platform;
  }

  public Long getGrpId() {
    if (grpId == null && StringUtils.isNotBlank(des3GrpId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3GrpId);
      if (des3Str == null) {
        return grpId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Long getReadPsnId() {
    if (readPsnId == null && StringUtils.isNotBlank(des3ReadPsnId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3ReadPsnId);
      if (des3Str == null) {
        return readPsnId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return readPsnId;
  }

  public void setReadPsnId(Long readPsnId) {
    this.readPsnId = readPsnId;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public String getDes3ReadPsnId() {
    return des3ReadPsnId;
  }

  public void setDes3ReadPsnId(String des3ReadPsnId) {
    this.des3ReadPsnId = des3ReadPsnId;
  }

  public String getPubDb() {
    return pubDb;
  }

  public void setPubDb(String pubDb) {
    this.pubDb = pubDb;
  }

  public Integer getCollectOperate() {
    return collectOperate;
  }

  public void setCollectOperate(Integer collectOperate) {
    this.collectOperate = collectOperate;
  }

  public String getOwnerDes3PsnId() {
    return ownerDes3PsnId;
  }

  public void setOwnerDes3PsnId(String ownerDes3PsnId) {
    this.ownerDes3PsnId = ownerDes3PsnId;
  }

  public PubFulltextPsnRcmdVO getPubRcmdft() {
    return pubRcmdft;
  }

  public void setPubRcmdft(PubFulltextPsnRcmdVO pubRcmdft) {
    this.pubRcmdft = pubRcmdft;
  }

  public Integer getIsAll() {
    return isAll;
  }

  public void setIsAll(Integer isAll) {
    this.isAll = isAll;
  }

  public Integer getFulltextCount() {
    return fulltextCount;
  }

  public void setFulltextCount(Integer fulltextCount) {
    this.fulltextCount = fulltextCount;
  }

  public String getDes3FileId() {
    return des3FileId;
  }

  public void setDes3FileId(String des3FileId) {
    this.des3FileId = des3FileId;
  }

  public List<PubFulltextPsnRcmdVO> getPubRcmdftList() {
    return pubRcmdftList;
  }

  public void setPubRcmdftList(List<PubFulltextPsnRcmdVO> pubRcmdftList) {
    this.pubRcmdftList = pubRcmdftList;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public String getSharePsnGroupIds() {
    return sharePsnGroupIds;
  }

  public void setSharePsnGroupIds(String sharePsnGroupIds) {
    this.sharePsnGroupIds = sharePsnGroupIds;
  }

  public Long getSharePsnGroupId() {
    return sharePsnGroupId;
  }

  public void setSharePsnGroupId(Long sharePsnGroupId) {
    this.sharePsnGroupId = sharePsnGroupId;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public String getIds() {
    return ids;
  }

  public void setIds(String ids) {
    this.ids = ids;
  }

  public Integer getDetailPageNo() {
    return detailPageNo;
  }

  public void setDetailPageNo(Integer detailPageNo) {
    this.detailPageNo = detailPageNo;
  }

  public boolean getNeedLikeStatus() {
    return needLikeStatus;
  }

  public void setNeedLikeStatus(boolean needLikeStatus) {
    this.needLikeStatus = needLikeStatus;
  }

  public boolean getNeedCollectStatus() {
    return needCollectStatus;
  }

  public void setNeedCollectStatus(boolean needCollectStatus) {
    this.needCollectStatus = needCollectStatus;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public String getDes3relationid() {
    return des3relationid;
  }

  public void setDes3relationid(String des3relationid) {
    this.des3relationid = des3relationid;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getAwardTimes() {
    return awardTimes;
  }

  public void setAwardTimes(Integer awardTimes) {
    this.awardTimes = awardTimes;
  }

}
