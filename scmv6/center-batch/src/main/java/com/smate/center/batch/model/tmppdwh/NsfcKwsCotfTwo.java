package com.smate.center.batch.model.tmppdwh;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "NSFC_KW_COTF_TWO")
public class NsfcKwsCotfTwo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -247889363113187533L;

    @Id
    @Column(name = "COTF_ID")
    @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_NSFC_KW_YTF", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    private Long cotfId;

    @Column(name = "DISCODE")
    private String discode;

    @Column(name = "KWSTR")
    private String kwsStr;

    @Column(name = "KW_1ST")
    private String kwFirst;

    @Column(name = "KW_2ND")
    private String kwSecond;

    @Column(name = "COTF")
    private Integer cotf;

    @Column(name = "KWSTR_LANG")
    private Integer language;


    public NsfcKwsCotfTwo() {
        super();
    }

    public NsfcKwsCotfTwo(Long cotfId, String discode, String kwsStr, String kwFirst, String kwSecond, Integer cotf,
            Integer language) {
        super();
        this.cotfId = cotfId;
        this.discode = discode;
        this.kwsStr = kwsStr;
        this.kwFirst = kwFirst;
        this.kwSecond = kwSecond;
        this.cotf = cotf;
        this.language = language;
    }

    public Long getCotfId() {
        return cotfId;
    }

    public void setCotfId(Long cotfId) {
        this.cotfId = cotfId;
    }

    public String getDiscode() {
        return discode;
    }

    public void setDiscode(String discode) {
        this.discode = discode;
    }

    public String getKwsStr() {
        return kwsStr;
    }

    public void setKwsStr(String kwsStr) {
        this.kwsStr = kwsStr;
    }

    public String getKwFirst() {
        return kwFirst;
    }

    public void setKwFirst(String kwFirst) {
        this.kwFirst = kwFirst;
    }

    public String getKwSecond() {
        return kwSecond;
    }

    public void setKwSecond(String kwSecond) {
        this.kwSecond = kwSecond;
    }

    public Integer getCotf() {
        return cotf;
    }

    public void setCotf(Integer cotf) {
        this.cotf = cotf;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }


}
