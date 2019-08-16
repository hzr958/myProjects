package com.smate.web.mobile.v8pub.vo.sns;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

public class PubOperateVO implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 2305728452660743073L;
  private Long psnId;
  private String des3PsnId;
  private String des3PrjId;
  private Long pubId;
  private String des3PubId;
  private String des3FundId;
  private String des3SearchPsnId;
  private Integer operate; // 赞操作 0:取消赞 1:点赞
  private String content; // 评论内容
  private String comment;// 分享成果时的评论
  private Integer platform;// 分享平台 微信、微博等 参照SharePlatformEnum
  private Long grpId; // 群组id
  private Long readPsnId;// 被阅读人id
  private String des3ReadPsnId;
  private String pubDb;// 成果所属的库，PDWH-基准库，SNS-个人库
  private Integer pubSum;// 个人成果列表总数，用于控制滑动论文详情尾页的判断
  private Integer collectOperate; // 收藏操作： 0：收藏， 1：取消收藏
  private String fromPage = "psn";
  private Boolean useoldform = false;
  private Integer articleType = 1; // 查询类型 1 成果 , 2 文献
  private String nextId;// 下一个查询id,用于更多
  private String des3NextId;// 下一个编码查询id,用于更多，
  private String orderType = "add"; // add 创建时间 publish 发表时间 citid 引用次数
  private String listType = "";// 所选择的类型 EI,ISTP,SCIE,SSCI
  private String pubType = "3,4,5,0";// ,或者是成果类别：会议论文，期刊论文，专利，其他,
  // 分享成果界面论文类别过滤条件默认全部选中
  private Integer pubIndex;// 成果在当前面的行数，从0开始
  private Integer detailPageSum;// 成果详情--当前过滤条件总共有多少条成果
  private Integer detailPageNo;// 成果详情--当前成果是第几条（第几页，每页中显示1条）
  private Integer detailPageSize = 1;// 成果详情--每页1条
  private Integer isNextPub = 1;// 成果详情--标识是下一篇，还是上一篇 ，-1:上一篇，0:当前篇,1:下一篇
  private Integer detailCurrSize;// 详情页当前大小，用于控制详情页面头部
                                 // .....的显示个数，大于等于5，则显示5个，小于5，则显示1-4个
  private String pubLocale = "zh_cn,en_us"; // zh_cn en_us,
  // 分享成果页面语言过滤条件默认选中中文和英文
  private String title;
  private String toBack;
  private String ids; // PUB_FULLTEXT_PSN_RCMD表id，多个逗号隔开
  private String fullTextImagePath; // 全文图片链接
  private String srcFullTextImagePath; // 源图片（高清图片）
  private boolean hasLogin; // 已登录
  private boolean needLikeStatus = true; // 是否要查出赞状态（已赞过还是未赞过）
  private boolean needCollectStatus = true; // 是否要查出收藏状态（已收藏过还是未收藏过）
  private String viewStatus; // 查看成果时的状态，没权限、成果已删除、成果不存在...

  private String searchPubType;// 查询的成果类型
  private String searchKey;

  private String des3relationid;// 消息关系表V_MSG_RELATION id
  private String referrer;
  private String operateType = "detail";
  private String whoFirst;// 成果认领与全文认领谁先显示
  /**
   * 收录列表，多个逗号隔离 例如：ei,sci
   */
  public String includeType = "";
  /**
   * 发表年份 多个逗号隔离 例如： 2015,2014
   */
  public String publishYear = "";
  /**
   * 排序的字段 citations:引用次数 title：标题 publishDate：发表日期 gmtModified ： 更新时间 下面是群组排序说明： createDate ： 创建时间
   * 默认排序 publishDate ： 最新发表 citations ： 最多引用排序
   */
  public String orderBy = "";
  public String representDes3PubIds;// 代表成果id多个用逗号分隔
  private String domain; // 域名

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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

  public String getNextId() {
    return nextId;
  }

  public void setNextId(String nextId) {
    this.nextId = nextId;
  }

  public String getDes3NextId() {
    return des3NextId;
  }

  public void setDes3NextId(String des3NextId) {
    this.des3NextId = des3NextId;
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

  public String getDes3SearchPsnId() {
    return des3SearchPsnId;
  }

  public void setDes3SearchPsnId(String des3SearchPsnId) {
    this.des3SearchPsnId = des3SearchPsnId;
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

  public String getFromPage() {
    return fromPage;
  }

  public void setFromPage(String fromPage) {
    this.fromPage = fromPage;
  }

  public Boolean getUseoldform() {
    return useoldform;
  }

  public void setUseoldform(Boolean useoldform) {
    this.useoldform = useoldform;
  }

  public Integer getArticleType() {
    return articleType;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public String getListType() {
    return listType;
  }

  public void setListType(String listType) {
    this.listType = listType;
  }

  public String getPubType() {
    return pubType;
  }

  public void setPubType(String pubType) {
    this.pubType = pubType;
  }

  public Integer getPubIndex() {
    return pubIndex;
  }

  public void setPubIndex(Integer pubIndex) {
    this.pubIndex = pubIndex;
  }

  public Integer getDetailPageSum() {
    return detailPageSum;
  }

  public void setDetailPageSum(Integer detailPageSum) {
    this.detailPageSum = detailPageSum;
  }

  public Integer getDetailPageNo() {
    return detailPageNo;
  }

  public void setDetailPageNo(Integer detailPageNo) {
    this.detailPageNo = detailPageNo;
  }

  public Integer getDetailPageSize() {
    return detailPageSize;
  }

  public void setDetailPageSize(Integer detailPageSize) {
    this.detailPageSize = detailPageSize;
  }

  public Integer getIsNextPub() {
    return isNextPub;
  }

  public void setIsNextPub(Integer isNextPub) {
    this.isNextPub = isNextPub;
  }

  public Integer getDetailCurrSize() {
    return detailCurrSize;
  }

  public void setDetailCurrSize(Integer detailCurrSize) {
    this.detailCurrSize = detailCurrSize;
  }

  public String getPubLocale() {
    return pubLocale;
  }

  public void setPubLocale(String pubLocale) {
    this.pubLocale = pubLocale;
  }

  public String getToBack() {
    return toBack;
  }

  public void setToBack(String toBack) {
    this.toBack = toBack;
  }

  public String getIds() {
    return ids;
  }

  public void setIds(String ids) {
    this.ids = ids;
  }

  public String getFullTextImagePath() {
    return fullTextImagePath;
  }

  public void setFullTextImagePath(String fullTextImagePath) {
    this.fullTextImagePath = fullTextImagePath;
  }

  public boolean getHasLogin() {
    return hasLogin;
  }

  public void setHasLogin(boolean hasLogin) {
    this.hasLogin = hasLogin;
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

  public String getViewStatus() {
    return viewStatus;
  }

  public void setViewStatus(String viewStatus) {
    this.viewStatus = viewStatus;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getSearchPubType() {
    return searchPubType;
  }

  public void setSearchPubType(String searchPubType) {
    this.searchPubType = searchPubType;
  }

  public String getIncludeType() {
    return includeType;
  }

  public void setIncludeType(String includeType) {
    this.includeType = includeType;
  }

  public String getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public String getDes3relationid() {
    return des3relationid;
  }

  public void setDes3relationid(String des3relationid) {
    this.des3relationid = des3relationid;
  }

  public String getDes3PrjId() {
    return des3PrjId;
  }

  public void setDes3PrjId(String des3PrjId) {
    this.des3PrjId = des3PrjId;
  }

  public String getDes3FundId() {
    return des3FundId;
  }

  public void setDes3FundId(String des3FundId) {
    this.des3FundId = des3FundId;
  }

  public String getSrcFullTextImagePath() {
    return srcFullTextImagePath;
  }

  public void setSrcFullTextImagePath(String srcFullTextImagePath) {
    this.srcFullTextImagePath = srcFullTextImagePath;
  }

  public String getReferrer() {
    return referrer;
  }

  public void setReferrer(String referrer) {
    this.referrer = referrer;
  }

  public String getOperateType() {
    return operateType;
  }

  public void setOperateType(String operateType) {
    this.operateType = operateType;
  }


  public String getRepresentDes3PubIds() {
    return representDes3PubIds;
  }

  public void setRepresentDes3PubIds(String representDes3PubIds) {
    this.representDes3PubIds = representDes3PubIds;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getWhoFirst() {
    return whoFirst;
  }

  public void setWhoFirst(String whoFirst) {
    this.whoFirst = whoFirst;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }



}
