package com.smate.center.task.quartz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;

public class InterfaceTestTask extends TaskAbstract {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String requestUrl;// 测试接口地址
  private String soapXml;// 参数XML地址
  private String soapAction;
  private String desFile;// 日志文件存放路径

  public String getRequestUrl() {
    return requestUrl;
  }

  public void setRequestUrl(String requestUrl) {
    this.requestUrl = requestUrl;
  }

  public String getSoapXml() {
    return soapXml;
  }

  public void setSoapXml(String soapXml) {
    this.soapXml = soapXml;
  }

  public String getSoapAction() {
    return soapAction;
  }

  public void setSoapAction(String soapAction) {
    this.soapAction = soapAction;
  }

  public String getDesFile() {
    return desFile;
  }

  public void setDesFile(String desFile) {
    this.desFile = desFile;
  }

  public InterfaceTestTask() {
    super();
  }

  public InterfaceTestTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    logger.info("=========InterfaceTestTask开始运行==========");
    try {
      this.doSoap();
    } catch (Exception e) {
      logger.error("InterfaceTestTask,运行异常", e);
    }

  }

  /**
   * @throws IOException
   * @throws FileNotFoundException
   * 
   */
  @SuppressWarnings("resource")
  public void doSoap() throws FileNotFoundException, IOException {
    // String requestUrl = "http://testwebservice.scholarmate.com:39593/psnInfoService";
    // String soapXml = "D:/search.xml";// 要发送的soap格式xml文件
    // String soapAction = "";// soapAction
    OutputStream out = null;
    BufferedReader in = null;
    try {
      URL url = new URL(requestUrl);
      HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
      // 读取soap需要发送的XML
      File fileToSend = new File(soapXml);
      byte[] buf = new byte[(int) fileToSend.length()];// 用于存放文件数据的数组
      new FileInputStream(soapXml).read(buf);
      // 设置请求头信息
      httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
      httpConn.setRequestProperty("soapActionString", soapAction);// Soap
      httpConn.setRequestMethod("POST");
      httpConn.setDoOutput(true);// 发送POST请求必须设置如下两行
      httpConn.setDoInput(true);
      out = httpConn.getOutputStream();// 获取URLConnection对象对应的输出流
      out.write(buf);
      out.flush();
      out.close();
      // 定义BufferedReader输入流来读取URL的响应内容
      in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
      String inputLine;
      Date date = new Date();
      SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
      String dateStr = "Requesttime:" + formatter.format(date);
      String respMsg = httpConn.getResponseCode() + "";
      this.saveTolocal(dateStr);
      this.saveTolocal("URL:" + requestUrl);
      this.saveTolocal("ResponseCode:" + respMsg);
      // 响应内容
      /*
       * while ((inputLine = in.readLine()) != null) { this.saveTolocal(inputLine); }
       */
      httpConn.disconnect();

      /*
       * Map<String, List<String>> map = httpConn.getHeaderFields(); System.out.println("显示响应Header信息\n");
       * 
       * for (Map.Entry<String, List<String>> entry : map.entrySet()) { System.out.println("Key : " +
       * entry.getKey() + " ,Value : " + entry.getValue()); }
       */

    } catch (Exception e) {
      logger.error("post soap error,please check the soapxml file!!", e);
    }
    // 使用finally块来关闭输出流、输入流
    finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

  }

  /**
   * 写入文件到本地
   * 
   * @param info
   */
  public void saveTolocal(String info) {
    BufferedWriter writer = null;
    File file;
    try {
      file = new File(desFile);
      if (!file.exists()) {
        file.createNewFile();
      }
      writer = new BufferedWriter(new FileWriter(file, true));

      writer.write(info);
      writer.newLine();
      writer.flush();
      writer.close();

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
