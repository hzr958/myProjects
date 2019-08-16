package com.smate.web.v8pub.controller;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.consts.PublicationArticleType;
import com.smate.web.v8pub.service.fileimport.FileImportService;
import com.smate.web.v8pub.service.fileimport.extract.PubSaveDataForm;
import com.smate.web.v8pub.service.pub.fileimport.PubFileImport;
import com.smate.web.v8pub.vo.PendingImportPubVO;
import com.smate.web.v8pub.vo.importfile.ImportSaveVo;
import com.smate.web.v8pub.vo.importfile.ImportfileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PubFileImportController {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubFileImport pubFileImport;
  @Autowired
  private FileImportService fileImportService;

  /**
   * 导入成果页面
   * 
   * @return
   */
  @RequestMapping("/pub/file/importenter")
  public ModelAndView pubByFile(Integer totalResult, String dbType) {
    ModelAndView model = new ModelAndView();
    ImportfileVo importfileVo = new ImportfileVo();
    importfileVo.setPublicationArticleType(PublicationArticleType.OUTPUT);
    model.addObject("importfileVo", importfileVo);
    if (dbType != null) {
      importfileVo.setDbType(dbType);
    }
    if (totalResult != null) {
      model.addObject("totalResult", totalResult);
    }
    model.setViewName("pub/fileimport/file_collect_import");
    return model;
  }

  /**
   * 读取文件
   * 
   * @param sourceFile
   * @param importfileVo
   * @param model
   * @return
   * @throws Exception
   */
  @RequestMapping("/pub/file/import")
  public ModelAndView initReferenceFileList(@RequestParam(required = false) MultipartFile sourceFile,
      ImportfileVo importfileVo, ModelAndView model) throws Exception {
    Integer result = 1;
    try {
      if (sourceFile == null) {
        model.setView(new RedirectView("/pub/file/importenter", false, false));
        return model;
      }
      PubSaveDataForm form = fileImportService.initFile(sourceFile, importfileVo.getDbType());// 读取文件获得成果
      List<PendingImportPubVO> pubList = pubFileImport.getInitImportPubVOList(form.getList(), importfileVo);// 初始化和校验

      if (pubList != null && pubList.size() > 0) {
        importfileVo.setPubList(pubList);
        importfileVo.setPubTypeList(pubFileImport.getConstPubTypeAll());
        model.addObject("importfileVo", importfileVo);
      } else {
        result = -1;
      }
    } catch (Exception e) {
      result = -1;
      logger.error("导入成果文件出错", e);
    }
    if (result == -1) {
      model.addObject("totalResult", -1);
      model.addObject("dbType", importfileVo.getDbType());
      model.setView(new RedirectView("/pub/file/importenter", false, false, true));
    } else {
      model.setViewName("pub/fileimport/collect_imp_fileList");
    }
    return model;
  }

  /**
   * 导入成果弹出页面
   * 
   * @param totalResult
   * @param model
   * @return
   * @throws Exception
   */
  @RequestMapping("/pub/file/ajaxfileResult")
  public ModelAndView ajaxfileResult(Integer totalResult, ModelAndView model) throws Exception {
    model.addObject("totalResult", totalResult != null ? totalResult : 0);
    model.setViewName("pub/fileimport/collect_imp_fileResult");
    return model;
  }

  /**
   * 保存成果
   * 
   * @param saveJson
   * @param model
   * @return
   * @throws Exception
   */
  @RequestMapping("/pub/file/save")
  public ModelAndView saveReferenceFileList(String saveJson, ModelAndView model) throws Exception {
    Map<String, Integer> result = new HashMap<String, Integer>();
    try {
      ImportSaveVo importSaveVo = JacksonUtils.jsonObject(saveJson, ImportSaveVo.class);
      result = pubFileImport.savePublist(importSaveVo);
    } catch (Exception e) {
      logger.error("导入成果文件出错", e);
      result.put("totalResult", -1);
    }
    model.addObject("totalResult", result.get("totalResult") == null ? 0 : result.get("totalResult"));
    model.setView(new RedirectView("/pub/file/ajaxfileResult", false, false, true));
    return model;
  }

}
