package com.smate.web.dyn.model.dynamic;

public class DynanicLink {
  /*
   * 成果的默认，图片地址
   */
  public final static String PUB_DEFAULT_IMAGE = "/resmod/mobile/images/pdf_icon.jpg";

  /*
   * 链接的类型 1：成果 ; 2 ：url解析成功 ; 3：url
   * 
   */
  private Integer linkType;
  /*
   * 成果标题
   */
  private String pubTitle;
  /*
   * 成果作者
   */
  private String pubAhthor;
  /*
   * 成果 图片
   */
  private String pubImage;


  /*
   * 链接，解析后的图片
   */
  private String urlImage;
  /*
   * 链接 ，解析后的标题
   */
  private String urlTitle;



}
