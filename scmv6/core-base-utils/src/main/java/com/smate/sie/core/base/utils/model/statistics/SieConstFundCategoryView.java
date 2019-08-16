package com.smate.sie.core.base.utils.model.statistics;

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
 * 资助机会CONST_FUND_CATEGORY视图-V_KPI_GRANT_SCHEME
 * 
 * @author lijianming
 *
 */
@Entity
@Table(name = "V_KPI_GRANT_SCHEME")
public class SieConstFundCategoryView {

    // 基准库主键
    @Id
    @Column(name = "GRANT_ID")
    @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_KPI_GRANT_SCHEME", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    private Long grantId;

    // 资助类别名称
    @Column(name = "GRANT_NAME")
    private String grantName;

    // 资助类别所属的资助机构
    @Transient
    private String agencyName;

    // 资助机构id关联const_fund_agency表主键
    @Column(name = "AGENCY_ID")
    private Long agencyId;

    // 取开始时间年份
    @Column(name = "STAT_YEAR")
    private Integer statYear;

    // 开始日期
    @Column(name = "START_DATE")
    private Date startDate;

    // 截止日期
    @Column(name = "END_DATE")
    private Date endDate;

    // 指南网址
    @Column(name = "GUIDE_URL")
    private String guideUrl;

    // 申报网址
    @Column(name = "DECLARE_URL")
    private String declareUrl;

    // 项目期限
    @Column(name = "DEADLINE")
    private String deadLine;

    // 是否全年 0否1是
    @Column(name = "ISALLYEAR")
    private Integer isAllYear;

    // 单位要求 1.企业，2，科研机构
    @Column(name = "INS_TYPE")
    private String insType;

    // 类别描述
    @Column(name = "DESCRIPT")
    private String description;

    // 资助强度：多少万元
    @Column(name = "STRENGTH")
    private String schemeMoney;

    // 是否配套 0:不配套；1:配套
    @Column(name = "ISMATCH")
    private Integer isMatch;

    // 创建时间
    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Transient
    private String startEndDate;

    @Transient
    private String des3Id;

    @Transient
    private String logoUrl;

    public SieConstFundCategoryView() {
        super();
    }

    public Long getGrantId() {
        return grantId;
    }

    public void setGrantId(Long grantId) {
        this.grantId = grantId;
    }

    public String getGrantName() {
        return grantName;
    }

    public void setGrantName(String grantName) {
        this.grantName = grantName;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public Integer getStatYear() {
        return statYear;
    }

    public void setStatYear(Integer statYear) {
        this.statYear = statYear;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getGuideUrl() {
        return guideUrl;
    }

    public void setGuideUrl(String guideUrl) {
        this.guideUrl = guideUrl;
    }

    public String getDeclareUrl() {
        return declareUrl;
    }

    public void setDeclareUrl(String declareUrl) {
        this.declareUrl = declareUrl;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public int getIsAllYear() {
        return isAllYear;
    }

    public void setIsAllYear(Integer isAllYear) {
        this.isAllYear = isAllYear;
    }

    public String getInsType() {
        return insType;
    }

    public void setInsType(String insType) {
        this.insType = insType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchemeMoney() {
        return schemeMoney;
    }

    public void setSchemeMoney(String schemeMoney) {
        this.schemeMoney = schemeMoney;
    }

    public Integer getIsMatch() {
        return isMatch;
    }

    public void setIsMatch(Integer isMatch) {
        this.isMatch = isMatch;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStartEndDate() {
        return startEndDate;
    }

    public void setStartEndDate(String startEndDate) {
        this.startEndDate = startEndDate;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getDes3Id() {

        if (this.grantId != null && des3Id == null) {
            des3Id = ServiceUtil.encodeToDes3(this.grantId.toString());
        }
        return des3Id;
    }

    public void setDes3Id(String des3Id) {
        this.des3Id = des3Id;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
