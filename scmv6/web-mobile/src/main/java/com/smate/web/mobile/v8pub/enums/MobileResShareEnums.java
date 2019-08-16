package com.smate.web.mobile.v8pub.enums;

/**
 * @description 移动端项目/个人库成果/基准库成果/基金/资助机构分享枚举类
 * @author xiexing
 * @date 2019年4月2日
 */
public enum MobileResShareEnums {
  PRJ("1", "prj", "项目"), SNS("2", "sns", "个人库成果"), PDWH("3", "pdwh", "基准库成果"), FUND("4", "fund", "基金"), AIDINS("5",
      "aidIns", "资助机构"), SNSDETAIL("6", "snsDetail", "个人库成果详情"), PRJDETAIL("7", "prjDetail", "项目详情"), PDWHDETAIL("8",
          "pdwhDetail", "基准库成果详情"), FUNDDETAIL("9", "fundDetail", "基金详情"), AIDINSDETAIL("10", "aidInsDetail",
              "资助机构内基金分享"), GRPFILE("11", "grpFile",
                  "群组文件分享"), GRPWORKFILE("11", "grpWorkFile", "群组作业分享"), GRPCOURSEFILE("11", "grpCourseFile", "群组课件分享"),
  NEWS("26", "news", "新闻");

  private String code;// 编码
  private String value;// 值
  private String msg;// 描述



  public String getCode() {
    return code;
  }



  public void setCode(String code) {
    this.code = code;
  }



  public String getValue() {
    return value;
  }



  public void setValue(String value) {
    this.value = value;
  }



  public String getMsg() {
    return msg;
  }



  public void setMsg(String msg) {
    this.msg = msg;
  }



  private MobileResShareEnums(String code, String value, String msg) {
    this.code = code;
    this.value = value;
    this.msg = msg;
  };


}
