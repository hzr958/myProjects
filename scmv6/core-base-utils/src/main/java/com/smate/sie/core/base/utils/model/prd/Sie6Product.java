package com.smate.sie.core.base.utils.model.prd;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 产品实体类
 * 
 * @author lijianming
 *
 */
@Entity
@Table(name = "PRODUCT")
public class Sie6Product implements Serializable {

    private static final long serialVersionUID = 4234995917652273622L;

    @Id
    @Column(name = "PD_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PRODUCT", allocationSize = 1)
    private Long pdId;

    // 产品名称
    @Column(name = "TITLE")
    private String title;

    // 依托单位id
    @Column(name = "INS_ID")
    private Long insId;

    // 发布日期
    @Column(name = "PUBLISH_DATE")
    private Date publishDate;

    // psnid
    @Column(name = "PSN_ID")
    private Long psnId;

    // 负责人
    @Column(name = "PSN_NAME")
    private String psnName;

    // 描述
    @Column(name = "SUMMARY")
    private String summary;

    // 创建日期
    @Column(name = "CREATE_DATE")
    private Date createDate;

    // 最后更新日期
    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    // 发表年份
    @Column(name = "PUBLISH_YEAR")
    private Integer publishYear;

    // 数据来源：0: 手工输入，1:数据库导入，2:文件导入
    @Column(name = "DATA_FROM")
    private Integer dataFrom;

    // 0/1数据是否完整;0：数据不完整，1：数据完整
    @Column(name = "IS_VALID")
    private Integer isValid;

    // 是否公开：0不公开1公开，默认1
    @Column(name = "IS_PUBLIC")
    private int isPublic = 1;

    // 产品图片ID
    @Column(name = "PD_LOGO")
    private String pdLogo;

    // 产品说明书全文ID
    @Column(name = "FULLTEXT_FILEID")
    private String fulltextFileid;

    // 部门Id
    @Column(name = "UNIT_ID")
    private Long unitId;

    // 加密Id.
    @Transient
    private String des3Id;

    @Transient
    private Long viewCount; // 阅读数

    @Transient
    private Long awardCount; // 点赞数

    @Transient
    private Long shareCount; // 分享数

    @Transient
    private Integer isAward = 0; // 是否赞过 1 是, 0否

    @Transient
    private String picFileName; // 产品图片全文名称

    @Transient
    private Integer picFileType; // 全文类型，3为基准库，6为人员在SIE中上传的

    @Transient
    private String picDownloadUrl; // 全文图片的下载地址

    public Long getPdId() {
        return pdId;
    }

    public void setPdId(Long pdId) {
        this.pdId = pdId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getInsId() {
        return insId;
    }

    public void setInsId(Long insId) {
        this.insId = insId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getPsnId() {
        return psnId;
    }

    public void setPsnId(Long psnId) {
        this.psnId = psnId;
    }

    public String getPsnName() {
        return psnName;
    }

    public void setPsnName(String psnName) {
        this.psnName = psnName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(Integer dataFrom) {
        this.dataFrom = dataFrom;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public String getPdLogo() {
        return pdLogo;
    }

    public void setPdLogo(String pdLogo) {
        this.pdLogo = pdLogo;
    }

    public String getFulltextFileid() {
        return fulltextFileid;
    }

    public void setFulltextFileid(String fulltextFileid) {
        this.fulltextFileid = fulltextFileid;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getAwardCount() {
        return awardCount;
    }

    public void setAwardCount(Long awardCount) {
        this.awardCount = awardCount;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public String getPicFileName() {
        return picFileName;
    }

    public void setPicFileName(String picFileName) {
        this.picFileName = picFileName;
    }

    public String getDes3Id() {
        if (this.pdId != null && des3Id == null) {
            des3Id = ServiceUtil.encodeToDes3(this.pdId.toString());
        }
        return des3Id;
    }

    public void setDes3Id(String des3Id) {
        this.des3Id = des3Id;
    }

    public Integer getIsAward() {
        return isAward;
    }

    public void setIsAward(Integer isAward) {
        this.isAward = isAward;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Integer getPicFileType() {
        return picFileType;
    }

    public void setPicFileType(Integer picFileType) {
        this.picFileType = picFileType;
    }

    public String getPicDownloadUrl() {
        return picDownloadUrl;
    }

    public void setPicDownloadUrl(String picDownloadUrl) {
        this.picDownloadUrl = picDownloadUrl;
    }
}
