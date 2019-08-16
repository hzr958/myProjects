package com.smate.center.oauth.action.pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

@Namespace("/")
@Results({@Result(name = "main", location = "/WEB-INF/jsp/pdf_reader_main.jsp")})
public class PDFReaderAction extends ActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = -8033875375408675363L;

  private File filedata;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;

  @Actions({@Action("/oauth/pdfreader/main")})
  public String index() {

    return "main";
  }

  @Actions({@Action("/oauth/pdfreader/reader")})
  public String reader() {
    Map<String, String> map = new HashMap<String, String>();
    PDDocument doc;
    try {
      doc = PDDocument.load(filedata);

      // 获取页码
      int pages = doc.getNumberOfPages();

      // 读文本内容

      PDFTextStripper stripper = new PDFTextStripper();
      // 设置按顺序输出
      stripper.setSortByPosition(true);
      stripper.setStartPage(1);
      stripper.setEndPage(2);
      String content = stripper.getText(doc);
      map.put("content", content);
      map.put("status", "success");
      HttpServletResponse response = Struts2Utils.getResponse();
      response.setHeader("Charset", "UTF-8");
      response.setContentType("text/html;charset=UTF-8");
      response.getWriter().print(JacksonUtils.jsonObjectSerializer(map));
    } catch (InvalidPasswordException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;
  }

  @Actions({@Action("/oauth/api/test")})
  public String testApi() {

    try {
      String s = FileUtils.openFile("http://dev.scholarmate.com/resmod/js/baseutils/fund.js");
      System.out.println(s);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Map<String, Object> map = new HashMap<String, Object>();
    List<Map<String, Object>> dataList = new ArrayList<>();
    map.put("status", "success");
    Map<String, Object> data = new HashMap<>();
    Map<String, Object> data2 = new HashMap<>(data);
    dataList.add(data);
    dataList.add(data2);
    List titles = new ArrayList();
    titles.add("title一级");
    titles.add("title二级");
    titles.add("title三级");
    data.put("fund_title_cn", titles);
    data.put("fund_title_en", titles);
    data.put("fund_title_abbr", "fund_title_abbr");
    data.put("fund_number", "fund_number");
    data.put("fund_desc", "fund_desc");
    data.put("discipline_classification_type", "discipline_classification_type");
    data.put("discipline_limit", "discipline_limit");
    data.put("fund_keywords", "fund_keywords");
    data.put("fund_year", 2014);
    data.put("apply_date_start", "2018-08-15");
    data.put("apply_date_end", "2018-08-15");
    data.put("funding_agency", "10000000");
    data.put("estimated_grant_amount", "125.5");
    data.put("declare_guide_url", "www.baidu.com");
    data.put("declare_url", "www.baidu.com");
    // data.put("region_limit","12312312");
    // data.put("title_limit","title_limit");
    // data.put("ins_limit","ins_limit");
    // data.put("degree_limit",125);
    // data.put("age_limit","1970-01-01 - 1980-01-01");
    // data.put("status",1);
    // data.put("update_time","2018-08-15");
    List accUrls = new ArrayList();
    accUrls.add("www.baidu.com");
    accUrls.add("www.tenxun.com");
    accUrls.add("www.hao123.com");
    data.put("accessory_url", accUrls);
    map.put("result", JacksonUtils.listToJsonStr(dataList));
    Struts2Utils.renderJson(dataList, "encoding:utf-8");

    return null;
  }


  public File getFiledata() {
    return filedata;
  }

  public void setFiledata(File filedata) {
    this.filedata = filedata;
  }

  public static void main(String[] args) {

  }

}
