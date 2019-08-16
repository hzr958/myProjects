package com.smate.sie.center.task.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;

/**
 * ip地址解析
 * 
 * @author hd
 *
 */
public final class ParseIpUtils {
  public static final String URL_TAOBAO = "http://ip.taobao.com/service/getIpInfo.php";

  public static final String STR_COUNTRY = "country";
  public static final String STR_PRVO = "region";
  public static final String STR_CITY = "city";
  private static final String ENCODEING_DEFALUT = "utf-8";

  /**
   * <p>
   * 解析ip所属的国家
   * </p>
   * <b>例子：</b>
   * 
   * <pre>
   * ParseIpUtils.getIPCountry("ip=119.137.54.194", "utf-8") = "中国"
   * </pre>
   * 
   * @param content ip地址串
   * @param encodingString 编码格式，如：utf-8
   * @return 解析结果
   */
  public static String getIPCountry(String content, String encodingString) {
    Map<String, String> data = getAddresses(content, encodingString);
    if (data != null) {
      return data.get(STR_COUNTRY);
    } else {
      return null;
    }

  }

  /**
   * <p>
   * 解析ip所属的省份
   * </p>
   * <b>例子：</b>
   * 
   * <pre>
   * ParseIpUtils.getIPProv("ip=119.137.54.194", "utf-8") = "广东"
   * </pre>
   * 
   * @param content ip地址串
   * @param encodingString 编码格式，如：utf-8
   * @return 解析结果
   */
  public static String getIPProv(String content, String encodingString) {
    Map<String, String> data = getAddresses(content, encodingString);
    if (data != null) {
      return data.get(STR_PRVO);
    } else {
      return null;
    }

  }

  /**
   * <p>
   * 解析ip所属的城市
   * </p>
   * <b>例子：</b>
   * 
   * <pre>
   * ParseIpUtils.getIPCity("ip=119.137.54.194", "utf-8") = "深圳"
   * </pre>
   * 
   * @param content ip地址串
   * @param encodingString 编码格式，如：utf-8
   * @return 解析结果
   */
  public static String getIPCity(String content, String encodingString) {
    Map<String, String> data = getAddresses(content, encodingString);
    if (data != null) {
      return data.get(STR_CITY);
    } else {
      return null;
    }

  }

  /**
   * <p>
   * 解析ip所属的国家、省份、城市 ：香港、澳门、台湾都视为中国的省份
   * </p>
   * <b>例子：</b>
   * 
   * <pre>
   * ParseIpUtils.getAddresses("ip=119.137.54.194", "utf-8");
   * return
   *           {ip=119.137.54.194, country=中国, area=, region=广东, city=深圳, county=XX, isp=电信, 
   *              country_id=CN, area_id=, region_id=440000, city_id=440300, county_id=xx, isp_id=100017}
   * </pre>
   * 
   * @param content ip地址串
   * @param encodingString 编码格式，如：utf-8
   * @return 解析结果
   */
  @SuppressWarnings("unchecked")
  public static Map<String, String> getAddresses(String content, String encodingString) {
    Map<String, String> data = null;
    try {
      if (content == null) {
        return null;
      } else {
        content = "ip=" + content;
      }
      // 取得IP所在的省市区信息
      String returnStr = null;
      int i = 0;
      while (i < 3) {// 尝试3次
        try {
          Thread.sleep(100);
          returnStr = getResult(URL_TAOBAO, content, encodingString);
        } catch (Exception e) {
          i = i + 1;
          continue;
        }
        if (returnStr != null) {
          break;
        } else {
          i = i + 1;
        }
      }
      if (returnStr != null) {
        returnStr = decodeUnicode(returnStr);
        boolean b = JacksonUtils.isJsonString(returnStr);
        if (b) {
          Map<String, Object> returnMap = JacksonUtils.jsonToMap(returnStr);
          Object o1 = returnMap.get("code");
          Object o2 = returnMap.get("data");
          if (o1 != null && NumberUtils.isDigits(o1.toString())) {
            Integer code = NumberUtils.parseInt(o1.toString());
            if (code == 0 && o2 != null) {
              data = (Map<String, String>) o2;
              if (data != null) {
                if (data.get(ParseIpUtils.STR_COUNTRY).equals("XX")) {
                  data.put(ParseIpUtils.STR_COUNTRY, "");
                }
                if (data.get(ParseIpUtils.STR_PRVO).equals("XX")) {
                  data.put(ParseIpUtils.STR_PRVO, "");
                }
                if (data.get(ParseIpUtils.STR_CITY).equals("XX") || data.get(ParseIpUtils.STR_CITY).equals("内网IP")) {
                  data.put(ParseIpUtils.STR_CITY, "");
                }
                if (data.get(ParseIpUtils.STR_COUNTRY).equals("香港") || data.get(ParseIpUtils.STR_COUNTRY).equals("台湾")
                    || data.get(ParseIpUtils.STR_COUNTRY).equals("澳门")) {
                  data.put(ParseIpUtils.STR_COUNTRY, "中国");

                }
                // 与const_region常量表匹配
                if (StringUtils.isNotBlank(data.get(ParseIpUtils.STR_PRVO))) {
                  data.put(ParseIpUtils.STR_PRVO, getFormatProv(data.get(ParseIpUtils.STR_PRVO)));
                }
                if (StringUtils.isNotBlank(data.get(ParseIpUtils.STR_CITY))) {
                  data.put(ParseIpUtils.STR_CITY, data.get(ParseIpUtils.STR_CITY) + "市");
                }

              }
            }

          }
        }

      }

    } catch (Exception e) {
      return null;
    }
    return data;

  }

  private static String getFormatProv(String prvo) {
    switch (prvo.trim()) {
      case "新疆":
        return "新疆维吾尔自治区";
      case "广西":
        return "广西壮族自治区";
      case "西藏":
        return "西藏自治区";
      case "宁夏":
        return "宁夏回族自治区";
      case "内蒙古":
        return "内蒙古自治区";
      case "上海":
        return "上海市";
      case "北京":
        return "北京市";
      case "天津":
        return "天津市";
      case "重庆":
        return "重庆市";
      case "香港":
        return "香港";
      case "澳门":
        return "澳门";
      case "台湾":
        return "台湾";
      default:
        return prvo.trim() + "省";
    }
  }

  private static String getResult(String urlStr, String content, String encoding) throws Exception {
    URL url = null;
    HttpURLConnection connection = null;
    try {
      if (encoding == null) {
        encoding = ENCODEING_DEFALUT;
      }
      url = new URL(urlStr);
      connection = (HttpURLConnection) url.openConnection();// 新建连接实例
      connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
      connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
      connection.setDoOutput(true);// 是否打开输出流 true|false
      connection.setDoInput(true);// 是否打开输入流true|false
      connection.setRequestMethod("POST");// 提交方法POST|GET
      connection.setUseCaches(false);// 是否缓存true|false
      connection.connect();// 打开连接端口
      DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
      out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
      out.flush();// 刷新
      out.close();// 关闭输出流
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
      // ,以BufferedReader流来读取
      StringBuffer buffer = new StringBuffer();
      String line = "";
      while ((line = reader.readLine()) != null) {
        buffer.append(line);
      }
      reader.close();
      return buffer.toString();
    } catch (Exception e) {
      throw new Exception("获取失败");
    } finally {
      if (connection != null) {
        connection.disconnect();// 关闭连接
      }
    }
  }

  /**
   * unicode 转换成 中文
   * 
   * @param theString
   * @return
   */
  public static String decodeUnicode(String theString) {
    char aChar;
    int len = theString.length();
    StringBuffer outBuffer = new StringBuffer(len);
    for (int x = 0; x < len;) {
      aChar = theString.charAt(x++);
      if (aChar == '\\') {
        aChar = theString.charAt(x++);
        if (aChar == 'u') {
          int value = 0;
          for (int i = 0; i < 4; i++) {
            aChar = theString.charAt(x++);
            switch (aChar) {
              case '0':
              case '1':
              case '2':
              case '3':
              case '4':
              case '5':
              case '6':
              case '7':
              case '8':
              case '9':
                value = (value << 4) + aChar - '0';
                break;
              case 'a':
              case 'b':
              case 'c':
              case 'd':
              case 'e':
              case 'f':
                value = (value << 4) + 10 + aChar - 'a';
                break;
              case 'A':
              case 'B':
              case 'C':
              case 'D':
              case 'E':
              case 'F':
                value = (value << 4) + 10 + aChar - 'A';
                break;
              default:
                throw new IllegalArgumentException("Malformed      encoding.");
            }
          }
          outBuffer.append((char) value);
        } else {
          if (aChar == 't') {
            aChar = '\t';
          } else if (aChar == 'r') {
            aChar = '\r';
          } else if (aChar == 'n') {
            aChar = '\n';
          } else if (aChar == 'f') {
            aChar = '\f';
          }
          outBuffer.append(aChar);
        }
      } else {
        outBuffer.append(aChar);
      }
    }
    return outBuffer.toString();
  }

}
