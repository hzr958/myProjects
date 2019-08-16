package com.smate.core.base.utils.constant;

/**
 * 数据源枚举类
 * 
 * @author tsz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public enum DBSessionEnum {

    SNS("sns"), RCMD("rcmd"), PDWH("pdwh"), ROL("rol"), EMAILSRV("emailsrv"), CAS("cas"), BPO("bpo"), GXROL(
            "gxrol"), ZSROL("zsrol"), STDROLHN("stdrolhn"), EGTEXPERT("egtexpert"), MAILDISPACH(
                    "maildispatch"), INNOCITY("innocity"), JXONLINE("jxonline"), SCNF(
                            "scnf"), SIE("sie"), SNSBAK("sns_bak"), MYSQL("mysql"), TMPPDWH("tmppdwh");
    // 定义私有变量
    private String value;

    // 构造函数，枚举类型只能为私有
    private DBSessionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
