package com.smate.web.v8pub.service.fileimport.extract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.smate.web.v8pub.utils.FileCodeUtils;


/**
 * 
 * @author aijiangbin
 * @date 2018年8月1日
 */
public class CSAFileExtractService extends FileExtractBaseService {
  private Logger logger = LoggerFactory.getLogger(getClass());



  @Override
  public List<Map<String, String>> fileExtractToMap(MultipartFile sourceFile) {
    List<Map<String, String>> list = new ArrayList<>();
    InputStream in = null;
    Map<String, String> pubMap = null;
    String contentTag =
        "TI:|AU:|SO:|IS:|DE:|AB:|AV:|LA:|PY:|PT:|AN:|PB:|DO:|SL:|UD:|SF:|RA:|CD:|NT:|CL:|JV:|JI:|JP:|CP:|RP:|AN:";
    String prjstartTag = "DN:";
    String prjendTag = "RECORD";
    int tagLen = 2;

    String[] contentTags = contentTag.split("\\|");
    Boolean isElement = false;
    BufferedReader br = null;
    try {
      Charset fileCode = FileCodeUtils.guessCharset(sourceFile.getInputStream());
      in = sourceFile.getInputStream();
      br = new BufferedReader(new InputStreamReader(in, fileCode));
      String line = br.readLine();
      Boolean isTag = false;
      Boolean isfirst = true;
      Boolean isAuthor = false;
      Boolean isSO = false;
      String[] SOsixStr = null;
      String currentKey = "";
      String currentValue = "";
      while (line != null) {
        if (!"".equals(line)) {
          if (isElement && line.toUpperCase().startsWith(prjendTag)) {
            isElement = false;
          } else if (line.startsWith(prjstartTag) || line.startsWith(prjstartTag, 1)) {
            pubMap = new HashMap<>();
            list.add(pubMap);
            currentKey = prjstartTag.substring(0, tagLen);
            isElement = true;
          } else {
            isTag = false;
            for (int i = 0; i < contentTags.length; i++) {
              if (line.startsWith(contentTags[i])) {
                isfirst = true;
                String title = contentTags[i].substring(0, tagLen);
                currentKey = title;
                if ("SO".equals(title)) {
                  isSO = true;
                } else {
                  isSO = false;
                }
                isElement = true;
                isTag = true;
                if ("AU".equals(title)) {
                  isAuthor = true;
                } else {
                  isAuthor = false;
                }
                break;
              }
            }
            if (isElement && !isTag) {// 这个判断，表示跳过标签的第一行
              if (isAuthor) { // 如果是导入作者，则将作者内“，”替换掉“”
                line = line.trim().replace(",", "");
              }
              if (isfirst) {
                String context = line.trim();
                if (isSO) {
                  String[] castext = context.split("\\s*,\\s*");
                  if (castext.length == 6) {
                    SOsixStr = castext;
                  }
                  currentValue = castext[0];
                  pubMap.put(currentKey, currentValue);
                } else {
                  currentValue = context;
                  pubMap.put(currentKey, StringUtils.trim(currentValue));
                }
                isfirst = false;
              } else {
                if (StringUtils.isNotBlank(pubMap.get(currentKey))) {
                  String lineSeparator = "; ";
                  switch (currentKey) {
                    case "TI":
                    case "AB":
                    case "SO":
                      lineSeparator = " ";
                      break;
                    case "DE":
                      lineSeparator = "; ";
                      break;
                    default:
                      break;
                  }
                  String oldValue = pubMap.get(currentKey);
                  if (StringUtils.isNotBlank(oldValue)) {
                    pubMap.put(currentKey, oldValue + lineSeparator + line);
                  } else {
                    pubMap.put(currentKey, line);
                  }
                } else {
                  pubMap.put(currentKey, line);
                }

              }
            }
          }
        }
        line = br.readLine();
      }
      String[] soStr = Optional.of(pubMap.get("SO")).map(str -> str.split("\\s*,\\s*")).orElse(new String[0]);

      if (soStr.length == 6 && ArrayUtils.isNotEmpty(SOsixStr)) {
        pubMap.put("PY", SOsixStr[1]);

        pubMap.put("vo", SOsixStr[2]);

        pubMap.put("is", SOsixStr[3]);

        pubMap.put("pn", SOsixStr[5]);

      } else if (soStr.length == 8) {
        pubMap.put("PY", SOsixStr[3]);

        pubMap.put("vo", SOsixStr[4]);

        pubMap.put("is", SOsixStr[5]);

        pubMap.put("pn", SOsixStr[7]);
      }

    } catch (Exception e) {
      logger.error("解析cas文件，出现异常", e);
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          logger.error("解析文件，关闭输入流异常", e);
        }
      }
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          logger.error("解析文件，关联输入流异常", e);
        }
      }
    }
    return list;
  }

  @Override
  public List<PubFileInfo> processData(List<Map<String, String>> list) {

    List<PubFileInfo> pubFileInfoList = new ArrayList<>();
    PubFileInfo pubInfo = null;
    if (list != null && list.size() > 0) {
      int seqNo = 1;
      for (Map<String, String> map : list) {
        pubInfo = new PubFileInfo();
        pubFileInfoList.add(pubInfo);
        pubInfo.setSeqNo(seqNo++);
        pubInfo.setTitle(map.get("TI"));
        pubInfo.setAuthorNames(map.get("AU"));
        pubInfo.setSourceDbCode("fileCSA");
        pubInfo.setOriginal(map.get("SO"));
        pubInfo.setCabstract(map.get("AB"));
        pubInfo.setKeywords(map.get("DE"));
        pubInfo.setISBN(map.get("IB"));
        pubInfo.setPubyear(map.get("PY"));
        // TODO
        pubInfo.setVolumeNo(map.get("vo"));
        pubInfo.setIssue(map.get("is"));
        pubInfo.setPageNumber(map.get("pn"));
        pubInfo.setPubType(4);

      }
    }
    return pubFileInfoList;

  }

  public static void main(String[] args) {
    String str = "aa" + System.lineSeparator() + "bbb";
    System.out.println(str);
  }
}
