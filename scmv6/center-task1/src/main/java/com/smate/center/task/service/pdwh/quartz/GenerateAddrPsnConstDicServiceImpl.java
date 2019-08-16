package com.smate.center.task.service.pdwh.quartz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhInsAddrConstDao;
import com.smate.center.task.dao.sns.psn.PersonPmNameDao;

/**
 * 人员姓名 ，地址常量词典更新任务业务实现
 * 
 * @author LIJUN
 * @date 2018年4月2日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GenerateAddrPsnConstDicServiceImpl implements GenerateAddrPsnConstDicService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhInsAddrConstDao pdwhInsAddrConstDao;
  @Autowired
  private PersonPmNameDao personPmNameDao;
  @Value("${extpdwh.custom.dicpath}")
  private String dicRootPath;
  private static String psndic = "psn/";
  private static String addrdic = "addr/";

  @Override
  public void generatePdwhPubMatchDic() {
    // 1.人名字典更新
    File psnDir = new File(dicRootPath + psndic);
    if (!psnDir.exists()) {
      psnDir.mkdirs();
    }
    File addrDir = new File(dicRootPath + addrdic);
    if (!addrDir.exists()) {
      addrDir.mkdirs();
    }
    int pIndex = 0;
    int aIndex = 0;
    int psnCount = 0;
    int addrCount = 0;
    Long timeStamp = System.currentTimeMillis();
    /**
     * 更新词典时防止生成中的词典被调用加上tmp前缀，生成完自动去除
     */
    String psnnamedic = psndic + "tmp_" + timeStamp + ".dic";
    String addrnamedic = addrdic + "tmp_" + timeStamp + ".dic";

    // 删除旧词典和日志
    removeOldDic(dicRootPath + psndic);
    removeOldDic(dicRootPath + addrdic);
    while (true) {
      pIndex++;
      List<String> nameList = personPmNameDao.batchGetName(2000, pIndex);
      if (CollectionUtils.isEmpty(nameList)) {
        try {
          writeLog(psndic + timeStamp, psnCount);
        } catch (Exception e) {
          logger.error("人员姓名词典写入log出错", e);
        }
        changeTmpDicName(psnnamedic);
        break;
      }
      psnCount += nameList.size();
      for (String string : nameList) {
        try {
          this.writeToDicFile(psnnamedic, cleanAuthorChars(string));
        } catch (IOException e) {
          logger.error("人员姓名词典写入出错，name:" + string, e);
        }
      }
    }

    // 1.地址字典更新
    while (true) {
      aIndex++;
      List<String> addrlist = pdwhInsAddrConstDao.batchGetAddr(2000, aIndex);
      if (CollectionUtils.isEmpty(addrlist)) {
        try {
          writeLog(addrdic + timeStamp, addrCount);
        } catch (IOException e) {
          logger.error("地址词典写入log出错", e);
        }
        changeTmpDicName(addrnamedic);
        break;
      }
      addrCount += addrlist.size();
      for (String string : addrlist) {
        try {
          this.writeToDicFile(addrnamedic, replaceChars(string));
        } catch (IOException e) {
          logger.error("地址词典写入出错，name:" + string, e);

        }
      }
    }
  }

  /**
   * 重命名词典
   * 
   * @param string
   * @author LIJUN
   * @date 2018年5月9日
   */
  private void changeTmpDicName(String name) {
    try {
      File file = new File(dicRootPath + name);
      String newName = name.replace("tmp_", "");
      file.renameTo(new File(dicRootPath + newName));
    } catch (Exception e) {
      logger.error("去除字典临时标记信息出错,filename:" + name, e);
    }
  }

  /**
   * 先删除上次生成的文件
   */
  public void removeOldDic(String filePath) {
    File psnFile = new File(filePath);
    List<Long> namelist = new ArrayList<>();
    String[] list = psnFile.list();// 获取词典目录下所有文件名
    if (ArrayUtils.isEmpty(list)) {
      return;
    }

    for (String string : list) {
      if (!string.contains("tmp_") && (string.endsWith(".dic") || string.endsWith(".txt"))) {
        namelist.add(NumberUtils.toLong(string.replace(".dic", "").replace(".txt", "")));
      }
    }
    String maxfileName = Collections.max(namelist).toString();
    File dicfile = new File(filePath);
    File[] listFiles = dicfile.listFiles();
    for (File file : listFiles) {
      if (file.exists() && !file.getName().contains(maxfileName)) {
        file.delete();
      }
    }

  }

  /**
   * 写入到文件
   * 
   * @param path
   * @param content
   * @throws IOException
   */
  public void writeToDicFile(String fileName, String content) throws IOException {
    ByteArrayInputStream InputStringStream = new ByteArrayInputStream(content.getBytes());
    // 可检测多种类型，并剔除bom
    BOMInputStream bomIn = new BOMInputStream(InputStringStream, false, ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE,
        ByteOrderMark.UTF_16BE);
    String charset = "utf-8";
    // 若检测到bom，则使用bom对应的编码
    if (bomIn.hasBOM()) {
      charset = bomIn.getBOMCharsetName();
    }
    InputStreamReader reader = new InputStreamReader(bomIn, charset);
    BufferedReader bufferedReader = null;
    BufferedWriter writer = null;
    try {

      bufferedReader = new BufferedReader(reader);
      String str = null;
      File dic = new File(dicRootPath + fileName);
      writer = new BufferedWriter(new FileWriter(dic, true));
      /*
       * window/dos 下文件换行符为 0x0D ,0x0A unix/linux 下文件换行符为 0x0A 0x0D == \r 0x0A == \n
       */
      while ((str = bufferedReader.readLine()) != null) {
        writer.write(str + "\n");
      }
      writer.flush();
    } catch (Exception e) {
      logger.error("写入字典文件错误！", e);

    } finally {
      bufferedReader.close();
      writer.close();
    }

  }

  public void writeLog(String filename, int count) throws IOException {
    BufferedWriter bufferedWriter = null;
    try {
      String dic = dicRootPath + filename + ".txt";
      File txt = new File(dic);
      bufferedWriter = new BufferedWriter(new FileWriter(txt, true));
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
      String diclog = "写入总记录数（行计数）为：" + count + ",生成时间：" + sdf.format(new Date());
      bufferedWriter.write(diclog + "\r\n");
      bufferedWriter.flush();

    } catch (IOException e) {
      logger.error("字典文件记录信息写入错误");
    } finally {
      bufferedWriter.close();
    }

  }

  /**
   * 替换常见字符
   * 
   * @param string
   * @return
   * @author LIJUN
   * @date 2018年3月26日
   */
  public String replaceChars(String string) {

    string = string.replace("，", ",").replace("（", "(").replace("）", ")").replace("。", ".");
    string = string.replace(" ", "空格").replace(",", "逗号").replace(".", "点符号").replace("(", "左括号").replace(")", "右括号")
        .replace("&", "和符号").replace("'", "撇号").replace("-", "杠符号").replace("《", "前书名号").replace("》", "后书名号");
    return string;

  }

  /**
   * 将姓名常见字符替换为空格 如果有连续空格则只保留一个
   * 
   * @param string
   * @return
   * @author LIJUN
   * @date 2018年3月26日
   */
  public static String cleanAuthorChars(String string) {

    string = string.replace("，", ",").replace("（", "(").replace("）", ")").replace("。", ".");
    string = string.replace(" ", "空格").replace(",", "空格").replace(".", "空格").replace("(", "空格").replace(")", "空格")
        .replace("&", "空格").replace("'", "空格").replace("-", "空格").replace("《", "空格").replace("》", "空格");
    while (string.contains("空格空格")) {
      string = string.replace("空格空格", "空格");
    }
    return string;

  }

  /**
   * 还原字符
   * 
   * @param string
   * @return
   * @author LIJUN
   * @date 2018年3月26日
   */
  public String resetChars(String string) {
    string = string.replace("空格", " ").replace("逗号", ",").replace("点符号", ".").replace("左括号", "(").replace("右括号", ")")
        .replace("和符号", "&").replace("撇号", "'").replace("杠符号", "-").replace("前书名号", "《").replace("后书名号", "》");
    return string;

  }
}
