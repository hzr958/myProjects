package com.smate.sie.core.base.utils.pay.ali;

import java.io.FileWriter;
import java.io.IOException;

/*
 * * 类名：AlipayConfig 功能：基础配置类/home/innocity/servers
 * 
 */
public class AlipayConfig {
  /*
   * public static String FILE_PATH; // 运行时用到的配置文件名
   * 
   * static { String env = System.getenv("RUN_ENV"); if (env.equals("development")) { FILE_PATH =
   * "C:\\"; } else if ("test".equals(env)) { FILE_PATH =
   * "/home/innocity/servers/innocity2-web/webapps/onlineweb/WEB-INF/classes/unionpay/"; } else {
   * FILE_PATH = "/home/scmv3/servers/innocity2-web/webapps/onlineweb/WEB-INF/classes/unionpay/"; } }
   * 
   * 
   * // 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串2088821236786841 // public static String partner =
   * "2088521141285429"; public static String partner = "2088821236786841"; //
   * 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号 public static String seller_id = partner;
   * 
   * // MD5密钥，安全检验码，由数字和字母组成的32位字符串30s2i1gnitn768m6g05mixz7cb8yw5sj // public static String key =
   * "37msc6a3emvh7vpaq5wp55ianu177u5o"; public static String key =
   * "30s2i1gnitn768m6g05mixz7cb8yw5sj"; // 签名方式 public static String sign_type = "MD5";
   * 
   * // 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。 public static String log_path
   * = FILE_PATH;
   * 
   * // 字符编码格式 目前支持 gbk 或 utf-8 public static String input_charset = "utf-8";
   * 
   * // 支付类型 ，无需修改 public static String payment_type = "1";
   * 
   * // 调用的接口名，无需修改 public static String service = "create_direct_pay_by_user";
   * 
   * // 调用的接口名，无需修改 public static String refund_service = "refund_fastpay_by_platform_pwd"; // 退款日期
   * 时间格式 yyyy-MM-dd HH:mm:ss public static String refund_date = UtilDate.getDateFormatter();
   * 
   * // 防钓鱼时间戳 若要使用请调用类文件submit中的query_timestamp函数 public static String anti_phishing_key = "";
   * 
   * // 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1 public static String exter_invoke_ip = "";
   * 
   * public static String it_b_pay = "3m";
   */



  // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
  public static String app_id = "2017101109242475";

  // 商户私钥，您的PKCS8格式RSA2私钥
  public static String merchant_private_key =
      "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCtDXA6PYMjBuG4n7gP7dtgLrznTNmk19GK9jhnrxX1GU3nLgl+UoU+VDGXBlhPCPu/PPS8kXJqjaMB6xoxUnzDgAP3KkWlKuQ1p1JTbZ/dgbOj2l/9mq4EIUgwdfEDmSOcyrOzsejML1ymn9XfmWU9AMfwWd3b6ZwHq2QO1T850Q5owmQqpvYw7jLLP7ErSilYl6NGDf1ZV7/3AXoeC7Pb8ZOtG7/V9uvE1UUMS+qiWwJADou16gVLVzKnMhD4NzykTDcMcxOlhpbb7QDwXlDOzff58BZ4KHr8arrxPXBnzvrEewmuWaxsHyC+cgrUqV81kE17CNlTDaBdegp7ArnRAgMBAAECggEAUtgSF++GregHeykLJqUJ1nvJdzl1H9Mpv0qsKgbJjjJ09u01IkJDA1mu+8vPztZQHncB+pvumFAB71MrkN0aWUUZoOOdv7pO0diuMr5SP+pto01RksjP8a41Lu3nWX+gatknOik7dzRdOG9gJdD7hD6xeMCs0zB8v7cvCWLRB83gGDGeOeFt3C4Lm00HS3w1jTfUJ81hC5fqVUb4l3xJW0+C8LpjgljvIegcfA7QWnZ6aTMCOGXDyeY2/xGKI7WFn6sxbFg7Qy9kV1jSzKSA2YP4yb8XuQ7tRGQGmhT5pqTPKBAN99wzj7+s96lJIf1NQJD9c7dVJuDXDMv/hkxQ+QKBgQDs1cn07WfGy7FW4BPRugJ+iA9AVRVXK2JbxlbpZ7qgI+A7k7Y0gwN/bijNIziuBt2m+Agc2LEh6z77/tCC/WMNic3+uWW9sdY3oYZmYkgb+fvnvWSldYC8+cOPoFCHA+jrOdjvB7AeN9cSZm6GhN27V9s7Atx6qFG2kgTHqasBDwKBgQC7DlimPTu6QlTmJ/jBjsoIgg7+I+x8AK5e4Gn1XZTxICDk8tsntJGoqfkqaLKGihpXMBbrlKaRfPaa2PFZlbeyL69hiMciMSiAduMBD4EJSpst6D2N1DSTHNW4TBUCn3XNYE66lJAaKJIKqTuIygG6b8dmBKjH174Zwo414/bXHwKBgF8CaPFj5/TdNDhoziqdl3CRPqUNPPFqSxwMFkOuRDUID2V3HWo1gMq3M5EP5yYRRQw9hmukFnNH/01ybM2PvxPoS4Y7c/BEjm5k0xjxpoHqqD7QQLcLy2iEPgq5QnTXgbIT084YZHPK7PHJu98lIBqO5TjoLfDa42T9cQN0MlDLAoGAP6GzumleJhBmfRzf3EhMpfRIUV+7FLvOlWvV36gEALqjNf7/WlRssGmR30FaabYVG8+6HE2nFBrkcOyXvZfWs4DRMmarBxCd7IWE11mE1bpRa55zQgoMEHdgpJ8NDqVcF49JeOMM2jku5P2IyRGe4OMhe6zeFY1ol669Xcu8rpUCgYBGJM97jEZmaFNA1dCeaGmVVTrkTcfK2D3eWhaltTTmvQ6jDppUXHrYuXb7f6rRTTW0ZwL/xk1jl6BKGKVWbfW4Au/zqU2BmElZmFP63T3BnbTlLZR0/t5DcENCuKoPhPUSoqxDBOt42JGAiiq9r6/WZyJa66BrBD200lenRw3Kwg==";

  // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
  public static String alipay_public_key =
      "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjwDO02mQwGJBwVuOpggp9yWWaRBeXxLhEB0p4kIguo3/KKFztOWjNd5+HEp3RBfl8XBWJ6J2Qj7iQz8lT7Ok2wp5rpqYN3YU1aMa7Hp0znAMzFSxxg1vRvOdr9guOpDTTUDyI1JENZ9F7Os29vrSdYoLhQ/dSAvR+3fLgk4yHudQB2Wz3ilIXS+Gavt4BuYcgZ2E5+cCZTa589jZrNCAi4zrBI9SdHNSkVVWOo6Dykcj1FK3IzLBpOoiMxZdZfoQofcZMpYkh1di+esC4TxHtpTT+2yYKkBFyVmd1c/SpZ98x5sloetCkVmcrEbIyIxMxNo4iETs/afzUD9khI5moQIDAQAB";

  // 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
  // "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp"
  // public static String notify_url = "http://dev.scholarmate.com";

  // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
  // http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp
  // public static String return_url = "http://dev.scholarmate.com";

  // 签名方式
  public static String sign_type = "RSA2";

  // 字符编码格式
  public static String charset = "utf-8";

  // 支付宝网关
  public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";

  // 支付宝网关
  public static String log_path = "C:\\";

  // 支付方式
  public static String TRADE_TYPE = "Alipay";


  // ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

  /**
   * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
   * 
   * @param sWord 要写入日志里的文本内容
   */
  public static void logResult(String sWord) {
    FileWriter writer = null;
    try {
      writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
      writer.write(sWord);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }


}

