package com.smate.web.v8pub.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.spring.SpringUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.service.export.ExportPubService;
import com.smate.web.v8pub.service.export.TxtService;
import com.smate.web.v8pub.service.poi.PoiService;
import com.smate.web.v8pub.service.poi.RefWorksTxtService;
import com.smate.web.v8pub.vo.PubExportVO;
import com.smate.web.v8pub.vo.exportfile.ExportPubTemp;

/**
 * 导出成果
 * 
 * @author yhx
 *
 */
@Controller
public class PubExportController {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TxtService txtService;
  @Autowired
  private ExportPubService exportPubService;
  @Autowired
  protected PoiService<ExportPubTemp> poiService;
  @Autowired
  protected RefWorksTxtService refWorksTxtService;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 导出成果
   * 
   * @param pubExportVO
   * @return
   */
  @RequestMapping("/pub/opt/ajaxpubexport")
  @ResponseBody
  public String pubExport(@ModelAttribute PubExportVO pubExportVO) {
    try {
      HttpServletResponse response = SpringUtils.getResponse();
      if ("txt".equals(pubExportVO.getExportType())) {
        exportTxt(response, pubExportVO);
      } else if ("excel".equals(pubExportVO.getExportType())) {
        this.exportExcel(response, pubExportVO);
      } else if ("endNote".equals(pubExportVO.getExportType())) {
        this.exportEndNoteTXT(response, pubExportVO);
      } else if ("refWorks".equals(pubExportVO.getExportType())) {
        this.exportRefworksTXT(response, pubExportVO);
      }
    } catch (Exception e) {
      logger.error("导出成果出错,pubIds= " + pubExportVO.getPubIds() + ", exportType(导出类型)= " + pubExportVO.getExportType()
          + ", exportScope(导出范围)= " + pubExportVO.getExportScope(), e);
    }
    return null;
  }

  /**
   * 导出TXT
   * 
   * @param response
   * @param pubExportVO
   */
  public void exportTxt(HttpServletResponse response, PubExportVO pubExportVO) {
    try {
      response.setContentType("application/octet-stream");
      if ("common".equals(pubExportVO.getExportScope())) {// 导出常用字段
        if (pubExportVO.getArticleType() == 1) {
          response.setHeader("Content-disposition", "attachment; filename=\"smate_pub_common.txt\"");// 统一文件命名
        }
      }
      if (StringUtils.isNotBlank(pubExportVO.getPubIds())) {
        List<Long> pubIds = new ArrayList<Long>();
        String[] tmpPubIds = pubExportVO.getPubIds().split(",");
        for (String pubId : tmpPubIds) {
          pubIds.add(NumberUtils.toLong(ServiceUtil.decodeFromDes3(pubId)));
        }
        pubExportVO.setPubInfoList(txtService.getInfoList(pubIds));
      }
      OutputStream out = response.getOutputStream();
      out.write(txtService.exportPubTxt(pubExportVO.getPubInfoList()).getBytes());
      out.flush();
      out.close();
    } catch (Exception e) {
      logger.error(
          "导出TXT出错,,pubIds= " + pubExportVO.getPubIds() + ", exportScope(导出范围)= " + pubExportVO.getExportScope(), e);
    }

  }

  /**
   * 导出excel.
   * 
   * @param response
   * @param articleType
   * @throws Exception
   */
  public void exportExcel(HttpServletResponse response, PubExportVO pubExportVO) throws Exception {
    response.setCharacterEncoding("utf-8");
    response.setHeader("Content-Type", "application/msexcel");
    response.setHeader("Content-Disposition", "attachment;filename=publication_list.xls");
    List<Long> pubIdList = new ArrayList<Long>();
    if (StringUtils.isNotBlank(pubExportVO.getPubIds())) {
      pubIdList = Stream.of(pubExportVO.getPubIds().split(",")).map(Des3Utils::decodeFromDes3)
          .map(NumberUtils::parseLong).collect(Collectors.toList());
    }
    List<ExportPubTemp> publications = this.exportPubService.queryExportPubTemp(pubIdList);
    HSSFWorkbook workbook = null;
    String language = LocaleContextHolder.getLocale().getLanguage();
    // String fileTempPath = "http://dev.scholarmate.com/ressns/help/SMate_Output_zh.xls";
    String fileTempPath = domainscm + "/ressns/help/SMate_Output_" + language + ".xls";
    workbook = this.poiService.exportExcelByTemp(fileTempPath, publications, null);


    workbook.write(response.getOutputStream());
    response.getOutputStream().flush();

  }

  /**
   * 导出EndNote txt.
   * 
   * @param response
   * @param pubExportVO
   * @throws Exception
   */
  public void exportEndNoteTXT(HttpServletResponse response, PubExportVO pubExportVO) throws Exception {
    response.setContentType("application/octet-stream");
    response.setHeader("Content-disposition", "attachment; filename=\"SMate_Output_EndNote_Export.txt\"");// 成果文件统一文件命名

    List<Long> pubIdList = new ArrayList<Long>();
    if (StringUtils.isNotBlank(pubExportVO.getPubIds())) {
      pubIdList = Stream.of(pubExportVO.getPubIds().split(",")).map(Des3Utils::decodeFromDes3)
          .map(NumberUtils::parseLong).collect(Collectors.toList());
    }
    List<ExportPubTemp> publications = this.exportPubService.queryExportPubTemp(pubIdList);

    OutputStream out = response.getOutputStream();
    out.write(this.txtService.exportPubEndnoteTxt(publications).getBytes());
    out.flush();
    out.close();

  }

  /**
   * 导出refwork txt.
   * 
   * @param response
   * @param articleType
   * @throws Exception
   */
  public void exportRefworksTXT(HttpServletResponse response, PubExportVO pubExportVO) throws Exception {
    response.setContentType("application/octet-stream");
    response.setHeader("Content-disposition", "attachment; filename=\"SMate_Output_Refwork_Export.txt\"");// 统一文件命名
    List<Long> pubIdList = new ArrayList<Long>();
    if (StringUtils.isNotBlank(pubExportVO.getPubIds())) {
      pubIdList = Stream.of(pubExportVO.getPubIds().split(",")).map(Des3Utils::decodeFromDes3)
          .map(NumberUtils::parseLong).collect(Collectors.toList());
    }
    List<ExportPubTemp> publications = this.exportPubService.queryExportPubTemp(pubIdList);

    OutputStream out = response.getOutputStream();
    out.write(this.refWorksTxtService.exportPubRefworksTxt(publications).getBytes());
    out.flush();
    out.close();
  }
}
