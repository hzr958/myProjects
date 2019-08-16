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
 * 资助机构CONST_FUND_AGENCY视图-V_KPI_GRANT_FROM
 * 
 * @author lijianming
 *
 */
@Entity
@Table(name = "V_KPI_GRANT_FROM")
public class SieConstFundAgencyView {

    // 基准库主键
    @Id
    @Column(name = "AGENCY_ID")
    @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_KPI_GRANT_FROM", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    private Long agencyId;

    @Column(name = "TYPE")
    private Long type;// 级别

    @Column(name = "ZH_NAME")
    private String zhName;// 资助机构

    @Column(name = "ABBR")
    private String abbr;// 机构缩写

    @Column(name = "URL")
    private String url;// 机构网址

    @Column(name = "CREATE_DATE")
    private Date createDate;// 创建时间

    @Column(name = "UPDATE_DATE")
    private Date updateDate;// 最近使用时间

    @Column(name = "COUNTRY_ID")
    private Long countryId;// 地址-国家

    @Column(name = "PRV_ID")
    private Long prvId;// 地址-省

    @Column(name = "CITY_ID")
    private Long cityId;// 地址-市

    @Column(name = "ADDRESS")
    private String address;// 机构地址

    @Column(name = "LOGO_URL")
    private String logoUrl; // 机构logo地址

    @Transient
    private String schemeCount; // 资助机会统计数

    @Transient
    private String des3Id; // 加密的gancyId

    public SieConstFundAgencyView() {
        super();
    }

    public SieConstFundAgencyView(Long agencyId, Long type, String zhName, String abbr, String url, Date createDate,
            Date updateDate, Long countryId, Long prvId, Long cityId, String address) {
        super();
        this.agencyId = agencyId;
        this.type = type;
        this.zhName = zhName;
        this.abbr = abbr;
        this.url = url;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.countryId = countryId;
        this.prvId = prvId;
        this.cityId = cityId;
        this.address = address;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getPrvId() {
        return prvId;
    }

    public void setPrvId(Long prvId) {
        this.prvId = prvId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getSchemeCount() {
        return schemeCount;
    }

    public void setSchemeCount(String schemeCount) {
        this.schemeCount = schemeCount;
    }

    public String getDes3Id() {

        if (this.agencyId != null && des3Id == null) {
            des3Id = ServiceUtil.encodeToDes3(this.agencyId.toString());
        }
        return des3Id;
    }

    public void setDes3Id(String des3Id) {
        this.des3Id = des3Id;
    }

}
