package com.smate.web.psn.service.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.ansj.library.DicLibrary;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class PdwhLoadNsfcDic {
  private String psnInsDicPath;

  private String psndic = "psn/";
  private String addrdic = "addr/";

  @PostConstruct
  public void init() {
    System.out.println("=========Nsfc人员与机构关键词词典开始加载=========");
    String pathName = getNewestDic(psndic);
    String pathIns = getNewestDic(addrdic);
    if (StringUtils.isBlank(pathName) || StringUtils.isBlank(pathIns)) {
      return;
    }
    File file = new File(pathName);
    FileInputStream ris = null;
    InputStreamReader isr = null;
    BufferedReader br = null;
    if (file.exists()) {
      try {
        DicLibrary.clear(DicLibrary.DEFAULT);
        ris = new FileInputStream(file);
        isr = new InputStreamReader(ris, "UTF-8");
        br = new BufferedReader(isr);
        String fileStr = null;
        while (StringUtils.isNotBlank(fileStr = br.readLine())) {
          DicLibrary.insert(DicLibrary.DEFAULT, fileStr.toLowerCase().trim(), "scm_user_name", DicLibrary.DEFAULT_FREQ);
        }
      } catch (Exception e) {
        System.out.println("人员检索提取人名，加载个人词典出错");
      } finally {
        try {
          ris.close();
          isr.close();
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      System.out.println("人员检索提取人名，加载个人词典完成");
    } else {
      System.out.println("人员检索提取人名，个人词典文件未发现");
    }

    File fileIns = new File(pathIns);
    if (fileIns.exists()) {
      try {
        ris = new FileInputStream(fileIns);
        isr = new InputStreamReader(ris, "UTF-8");
        br = new BufferedReader(isr);
        String fileStr1 = null;
        while (StringUtils.isNotBlank(fileStr1 = br.readLine())) {
          DicLibrary.insert(DicLibrary.DEFAULT, fileStr1.toLowerCase().trim(), "scm_ins_name", DicLibrary.DEFAULT_FREQ);
        }
      } catch (Exception e) {
        System.out.println("人员检索提取人名，加载单位名词典出错: " + e.getMessage());
      } finally {
        try {
          ris.close();
          isr.close();
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      System.out.println("人员检索提取人名，加载单位名词典完成");
    } else {
      System.out.println("人员检索提取人名，单位名词典文件未发现");
    }
  }

  public String getPsnInsDicPath() {
    return psnInsDicPath;
  }

  public void setPsnInsDicPath(String psnInsDicPath) {
    this.psnInsDicPath = psnInsDicPath;
  }

  public String getNewestDic(String dicpath) {
    File psnFile = new File(psnInsDicPath + dicpath);
    List<Long> namelist = new ArrayList<>();
    String[] list = psnFile.list();// 获取词典目录下所有文件名
    if (list == null || list.length <= 0) {
      System.out.println("=========未获取到Nsfc人员与机构关键词词典=========");
      return "";
    }
    for (String string : list) {
      if (!string.contains("tmp_") && string.endsWith(".dic")) {
        namelist.add(NumberUtils.toLong(string.replace(".dic", "")));
      }
    }
    String fileName = psnInsDicPath + dicpath + Collections.max(namelist) + ".dic";
    return fileName;
  }
}
