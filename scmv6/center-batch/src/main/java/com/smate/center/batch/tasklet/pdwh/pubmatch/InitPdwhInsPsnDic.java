package com.smate.center.batch.tasklet.pdwh.pubmatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.ansj.library.DicLibrary;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.center.batch.service.pub.GenerateAddrPsnConstDicService;

/**
 * @Author LIJUN
 * @Description //TODO 服务器启动加载词典
 * @Date 10:13 2018/7/19
 * @Param
 * @return
 **/
public class InitPdwhInsPsnDic {
  @Value("${extpdwh.custom.dicpath}")
  private String dicRootPath;
  private static String psndic = "psn/";
  private static String addrdic = "addr/";
  // private static String kwsdic = "keywords/";

  @Autowired
  private GenerateAddrPsnConstDicService generateAddrPsnConstDicService;

  @PostConstruct
  public void init() {
    String pathName = getNewestDic(psndic);
    String pathIns = getNewestDic(addrdic);
    // String pathKws = getNewestDic(kwsdic);
    File file = new File(pathName);
    FileInputStream ris = null;
    InputStreamReader isr = null;
    BufferedReader br = null;
    if (file.exists()) {
      Integer k = 0;
      try {
        DicLibrary.clear(DicLibrary.DEFAULT);
        ris = new FileInputStream(file);
        isr = new InputStreamReader(ris, "UTF-8");
        br = new BufferedReader(isr);
        String fileStr = null;
        while (StringUtils.isNotBlank(fileStr = br.readLine())) {
          DicLibrary.insert(DicLibrary.DEFAULT, fileStr.toLowerCase().trim(), "scm_user_name", DicLibrary.DEFAULT_FREQ);
          k++;
        }
      } catch (Exception e) {
        System.out.println("加载个人词典出错");
      } finally {
        try {
          ris.close();
          isr.close();
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      System.out.println("加载个人词典完成====time:" + new Date() + "====加载个人姓名条目数:" + k);
    } else {
      System.out.println("个人词典文件未发现");
    }

    File fileIns = new File(pathIns);
    if (fileIns.exists()) {
      Integer k = 0;
      try {
        ris = new FileInputStream(fileIns);
        isr = new InputStreamReader(ris, "UTF-8");
        br = new BufferedReader(isr);
        String fileStr1 = null;
        while (StringUtils.isNotBlank(fileStr1 = br.readLine())) {
          DicLibrary.insert(DicLibrary.DEFAULT, fileStr1.toLowerCase().trim(), "scm_ins_name", DicLibrary.DEFAULT_FREQ);
          k++;
        }
      } catch (Exception e) {
        System.out.println("加载单位名词典出错: " + e.getMessage());
      } finally {
        try {
          ris.close();
          isr.close();
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      System.out.println("加载单位名词典完成====time:" + new Date() + "====加载单位名条目数:" + k);
    } else {
      System.out.println("单位名词典文件未发现");
    }

    System.out.println("=========pdwhpub成果匹配词典开始加载=======time:" + new Date());
    // 先加载关键词词典
    this.generateAddrPsnConstDicService.generateNsfcKwsDic();

    /*
     * File fileKws = new File(pathKws); if (fileKws.exists()) { Integer k = 0; try { ris = new
     * FileInputStream(fileKws); isr = new InputStreamReader(ris, "UTF-8"); br = new
     * BufferedReader(isr); String fileStr1 = null; while (StringUtils.isNotBlank(fileStr1 =
     * br.readLine())) { DicLibrary.insert(DicLibrary.DEFAULT, fileStr1.toLowerCase().trim(),
     * "nsfc_kw_discipline", DicLibrary.DEFAULT_FREQ); k++; } } catch (Exception e) {
     * System.out.println("加载关键词词典出错: " + e.getMessage()); } finally { try { ris.close(); isr.close();
     * br.close(); } catch (IOException e) { e.printStackTrace(); } }
     * System.out.println("加载关键词词典完成====time:" + new Date() + "====加载关键词条目数:" + k); } else {
     * System.out.println("加载关键词文件未发现"); }
     */
  }

  /**
   * @Author LIJUN
   * @Description //TODO 获取最新的词典
   * @Date 10:17 2018/7/19
   * @Param [dicpath]
   * @return java.lang.String
   **/
  public String getNewestDic(String dicpath) {
    File psnFile = new File(dicRootPath + dicpath);
    List<Long> namelist = new ArrayList<>();
    String[] list = psnFile.list();// 获取词典目录下所有文件名
    if (list == null || list.length <= 0) {
      System.out.println("=========未获取到pdwhpub成果匹配词典=========");
      return "";
    }
    for (String string : list) {
      if (!string.contains("tmp_") && string.endsWith(".dic")) {
        namelist.add(NumberUtils.toLong(string.replace(".dic", "")));
      }
    }
    if (namelist.size() <= 0) {
      return " ";
    }
    String fileName = dicRootPath + dicpath + Collections.max(namelist) + ".dic";
    return fileName;
  }
}
