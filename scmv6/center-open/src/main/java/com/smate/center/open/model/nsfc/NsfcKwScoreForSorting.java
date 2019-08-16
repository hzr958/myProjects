package com.smate.center.open.model.nsfc;

import java.io.Serializable;


public class NsfcKwScoreForSorting implements Serializable, Comparable<NsfcKwScoreForSorting> {

    /**
     * 
     */
    private static final long serialVersionUID = -6850935487905679842L;
    private String nsfcApplicationCode;
    private String keywords;
    private Long tf;
    private Long cotf;
    private Integer length = 0;
    private Double isiTf;
    private Long prjkwScore;
    private Double rkwScore;
    private Integer type; // 1.来自标题; 2.自填关键词; 3.与标题自填关键词相关的摘要关键词; 4其他摘要关键词

    public NsfcKwScoreForSorting() {
        super();
    }


    public NsfcKwScoreForSorting(String keywords, Integer length, Long prjkwScore) {
        super();
        this.keywords = keywords;
        this.length = length;
        this.prjkwScore = prjkwScore;
    }


    public NsfcKwScoreForSorting(String keywords, Integer length, Integer type, Long prjkwScore, Double rkwScore) {
        super();
        this.keywords = keywords;
        this.length = length;
        this.type = type;
        this.prjkwScore = prjkwScore;
        this.rkwScore = rkwScore;
    }

    public NsfcKwScoreForSorting(String keywords, Integer length, Integer type, Long prjkwScore) {
        super();
        this.keywords = keywords;
        this.length = length;
        this.type = type;
        this.prjkwScore = prjkwScore;
    }


    public String getNsfcApplicationCode() {
        return nsfcApplicationCode;
    }

    public void setNsfcApplicationCode(String nsfcApplicationCode) {
        this.nsfcApplicationCode = nsfcApplicationCode;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Long getTf() {
        return tf;
    }

    public void setTf(Long tf) {
        this.tf = tf;
    }

    public Long getCotf() {
        return cotf;
    }

    public void setCotf(Long cotf) {
        this.cotf = cotf;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Double getIsiTf() {
        return isiTf;
    }

    public void setIsiTf(Double isiTf) {
        this.isiTf = isiTf;
    }

    public Long getPrjkwScore() {
        return prjkwScore;
    }

    public void setPrjkwScore(Long prjkwScore) {
        this.prjkwScore = prjkwScore;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public Double getRkwScore() {
        return rkwScore;
    }


    public void setRkwScore(Double rkwScore) {
        this.rkwScore = rkwScore;
    }


    @Override
    public String toString() {
        return this.nsfcApplicationCode + " " + this.keywords + " " + this.tf + " " + this.cotf;
    }

    @Override
    public int compareTo(NsfcKwScoreForSorting o) {
        if (o == null) {
            return -1;
        }
        if (o.prjkwScore - this.prjkwScore > 0) {
            return 1;
        } else if (o.prjkwScore - this.prjkwScore == 0) {
            if (o.length - this.length > 0) {
                return 1;
            } else if (o.length - this.length == 0) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

}
