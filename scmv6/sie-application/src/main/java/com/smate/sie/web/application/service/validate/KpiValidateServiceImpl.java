package com.smate.sie.web.application.service.validate;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.github.inspektr.audit.annotation.Audit;
import com.smate.core.base.file.model.Sie6ArchiveFile;
import com.smate.core.base.file.service.Sie6ArchiveFileService;
import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.exception.SysDataException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.model.impfilelog.ImportFileLog;
import com.smate.core.base.utils.model.impfilelog.UploadTypeContant;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.pdfdata.HiddenDataUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.impfilelog.ImportFileLogService;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.core.base.utils.consts.validate.SieKpiVdConstant;
import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;
import com.smate.sie.core.base.utils.dao.validate.KpiPayValidateDao;
import com.smate.sie.core.base.utils.dao.validate.KpiPayValidateUserDao;
import com.smate.sie.core.base.utils.dao.validate.KpiValidateDetailDao;
import com.smate.sie.core.base.utils.dao.validate.KpiValidateMainDao;
import com.smate.sie.core.base.utils.model.psn.SieInsPerson;
import com.smate.sie.core.base.utils.model.validate.KpiPayValidate;
import com.smate.sie.core.base.utils.model.validate.KpiPayValidateUser;
import com.smate.sie.core.base.utils.model.validate.KpiValidateDetail;
import com.smate.sie.core.base.utils.model.validate.KpiValidateMain;
import com.smate.sie.core.base.utils.model.validate.KpiValidateMainUpload;
import com.smate.sie.web.application.dao.validate.KpiValidateMainUploadDao;
import com.smate.sie.web.application.form.validate.KpiValidateForm;

@Service("kpiValidateService")
@Transactional(rollbackFor = Exception.class)
public class KpiValidateServiceImpl implements KpiValidateService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonDao personDao; // sns人员信息dao
  @Autowired
  private KpiValidateMainDao kpiValidateMainDao;
  @Autowired
  private KpiValidateMainUploadDao kpiValidateMainUploadDao;
  @Autowired
  private KpiValidateDetailDao kpiValidateDetailDao;
  @Autowired
  private ImportFileLogService importFileLogService;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private Sie6ArchiveFileService sie6ArchiveFileService;
  @Autowired
  private SieInsPersonDao sieInsPersonDao;
  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;
  @Autowired
  private KpiPayValidateDao kpiPayValidateDao;
  @Autowired
  private KpiCodecService kpiCodecService;

  @Autowired
  private KpiPayValidateUserDao kpiPayValidateUserDao;

  @Override
  public void initLeftMenu(KpiValidateForm form) throws SysServiceException {
    List<Map<String, String>> typeResult = new ArrayList<Map<String, String>>();
    List<Map<String, Object>> yearResult = new ArrayList<Map<String, Object>>();
    Map<String, Object> otherMap = new HashMap<String, Object>();
    otherMap.put("itemName", "未分类");
    otherMap.put("itemId", 99L);
    try {
      yearResult = kpiValidateMainDao.getYearList(form.getUserId());
      yearResult.add(otherMap);
      form.setYearList(yearResult);
      typeResult = kpiValidateMainDao.getKeyTypeList();
      form.setTypeList(typeResult);
      judgementPayment(form, form.getClientIP());
    } catch (Exception e) {
      logger.error("科研验证列表左侧查询条件初始化出错 ", e);
      throw new SysServiceException(e);
    }

  }

  @Override
  public void queryKpiValidateList(KpiValidateForm form, Page<KpiValidateMainUpload> page) throws SysServiceException {
    kpiValidateMainDao.queryKpiValidateList(form.getUserId(), form.getSearchKey(), form.getTypeId(), form.getPrpYear(),
        page);
    enhanceList(page.getResult());
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void getPsnCountByCon(KpiValidateForm form) throws SysServiceException {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, String> typeMap = new HashMap<String, String>();
    Map<String, String> prpYearMap = new HashMap<String, String>();
    // 类型 kpi_validate_main.key_type
    List<Map<String, Object>> keyTypeList = kpiValidateMainDao.getValidateCount(form.getUserId(), form.getSearchKey(),
        form.getTypeId(), form.getPrpYear(), 1);
    if (keyTypeList != null && keyTypeList.size() > 0) {
      for (Map type : keyTypeList) {
        typeMap.put(type.get("typeId").toString(), type.get("count").toString());
      }
    }
    // 申请年份
    List<Map<String, Object>> prpYearList = kpiValidateMainDao.getValidateCount(form.getUserId(), form.getSearchKey(),
        form.getTypeId(), form.getPrpYear(), 2);
    for (Map year : prpYearList) {
      prpYearMap.put(year.get("prpYear").toString(), year.get("count").toString());
    }
    map.put("typeId", typeMap);
    map.put("prpYear", prpYearMap);
    form.setResultMap(map);
  }

  @Override
  public Map<String, Object> extractPDFFileContent(KpiValidateForm form, String savePathRoot)
      throws SysServiceException, IOException {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    File filedata = form.getFiledata();
    try {
      if (filedata != null) {
        filedata.toString();
        ImportFileLog importFileLog = null;
        File newFile = this.saveImportFile(filedata, savePathRoot, form.getFiledataFileName());
        importFileLog = this.saveImportFileLog(newFile.getName(), SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentInsId(), newFile.getAbsolutePath(), UploadTypeContant.KPI_VALIDATE);
        String extractData = HiddenDataUtils.parseHiddenData(filedata.toString());
        // 保存文件到文件库
        Sie6ArchiveFile archiveFile = null;
        try {
          archiveFile = sie6ArchiveFileService.saveSie6ArchiveFile(importFileLog, filedata, "",
              form.getFiledataFileName(), savePathRoot);
        } catch (Exception e) {
          logger.error("保存上传文件出错");
          throw new IOException("保存上传文件出错", e);
        }
        if (StringUtils.isNotBlank(extractData) && JacksonUtils.isJsonString(extractData)) {
          if (importFileLog != null) {
            importFileLog.setStatus(1);
            importFileLog.setMsg("已经提交后台检测");
            saveImportFileLog(importFileLog);
          }
          String encryptedData = kpiCodecService.encode("11111111", extractData);
          Map<String, Object> encodeData = new HashMap<String, Object>();
          encodeData.put(SieKpiVdConstant.ENCODE_CONTENT, encryptedData);
          Object obj = acceptValidation(JacksonUtils.mapToJsonStr(encodeData)); // 调接口
          if (obj != null) {
            Map<String, Object> resultMapFormJson = JacksonUtils.jsonToMap(obj.toString());
            Object resultObject = resultMapFormJson.get("result");
            if (resultObject != null) {
              Map<String, Object> content = JacksonUtils.jsonToMap(resultObject.toString());
              String contentJson = decodeResult(content.get("content").toString());
              @SuppressWarnings("unchecked")
              List<Map<String, Object>> resultList = (List<Map<String, Object>>) JacksonUtils.jsonToList(contentJson);
              if (resultList != null) {
                for (Map<String, Object> map2 : resultList) {
                  Set<String> keySet = map2.keySet();
                  Iterator<String> iterator = keySet.iterator();
                  while (iterator.hasNext()) {
                    String key = iterator.next();
                    if ("uuid".equals(key)) {
                      String uuid = map2.get(key).toString();
                      if (StringUtils.isNotBlank(uuid)) {
                        KpiValidateMainUpload upload = new KpiValidateMainUpload();
                        upload.setFileId(archiveFile.getFileId());
                        upload.setIp(form.getClientIP());
                        upload.setUuId(uuid);
                        upload.setIsDel(0);
                        Long psnId = SecurityUtils.getCurrentUserId();
                        upload.setPsnId(psnId);
                        List<SieInsPerson> insIds = sieInsPersonDao.findPsnInsListByPsnId(form.getUserId());
                        Person snsPsn = personDao.get(form.getUserId());
                        upload.setPsnName(snsPsn.getName());
                        // 存在多个单位，默认取insId最小那个
                        if (insIds.size() > 0) {
                          upload.setInsId(insIds.get(0).getPk().getInsId());
                          Sie6Institution ins = sie6InstitutionDao.get(insIds.get(0).getPk().getInsId());
                          upload.setInsName(ins.getInsName());
                        } else {
                          // 不存在单位，则个人库首要单位填充
                          upload.setInsId(snsPsn.getInsId());
                          upload.setInsName(snsPsn.getInsName());
                        }
                        upload.setSubmitTime(new Date());
                        kpiValidateMainUploadDao.save(upload);
                      } else {
                        returnMap.put("msg", "error");
                        return returnMap;
                      }
                    }
                  }
                }
              } else {
                returnMap.put("msg", "error");
                return returnMap;
              }
            } else {
              returnMap.put("msg", "error");
              return returnMap;
            }
            returnMap.put("msg", "success");
            return returnMap;
          } else {
            returnMap.put("msg", "error");
            return returnMap;
          }
        } else {
          if (importFileLog != null) {
            importFileLog.setStatus(0);
            importFileLog.setMsg("该申请书不支持，请联系在线客服");
            saveImportFileLog(importFileLog);
          }
          returnMap.put("msg", "error");
          return returnMap;
        }
      } else {
        returnMap.put("msg", "error");
        return returnMap;
      }
    } catch (IOException e) {
      logger.error("抽取PDF内容报错");
      throw new IOException("抽取PDF内容报错", e);
    }
  }

  /**
   * 解密调接口返回的值
   * 
   * @param result
   * @return
   */
  public String decodeResult(String result) {
    return kpiCodecService.decode("11111111", result);
  }

  private List<KpiValidateMainUpload> enhanceList(List<KpiValidateMainUpload> lists) {
    if (CollectionUtils.isEmpty(lists)) {
      return lists;
    }
    for (KpiValidateMainUpload upload : lists) {
      String uuid = upload.getUuId();
      KpiValidateMain main = kpiValidateMainDao.get(uuid);
      String title = main.getTitle();
      Integer status = main.getStatus();
      KpiValidateMain mainShow = new KpiValidateMain(title, status, uuid);
      upload.setMain(mainShow);
    }
    return lists;
  }

  /**
   * 
   * @param file
   * @param savePathRoot
   * @param sourceFileName
   * @return
   */
  private File saveImportFile(File file, String savePathRoot, String sourceFileName) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String nowDate = simpleDateFormat.format(new Date());
    String folderName = "/" + SecurityUtils.getCurrentInsId() + "_" + nowDate;

    String suffix = sourceFileName.substring(sourceFileName.lastIndexOf("."));
    // 重命名
    String uuid = UUID.randomUUID().toString().replace("-", "");
    File newFile = new File(savePathRoot + folderName + "/" + uuid + suffix);
    // 移动文件
    try {
      FileUtils.copyFile(file, newFile, false);
    } catch (IOException e) {
      logger.error("把文件从" + file.getAbsolutePath() + ",移动到" + newFile.getAbsolutePath() + "出现错误！");
    }
    return newFile;
  }

  private ImportFileLog saveImportFileLog(String fileName, Long psnId, Long insId, String savePath, String uploadType) {
    try {
      ImportFileLog importFileLog = new ImportFileLog();
      importFileLog.setFileName(fileName);
      importFileLog.setInsId(insId);
      importFileLog.setPsnId(psnId);
      importFileLog.setSavePath(savePath);
      importFileLog.setUploadDate(new Date());
      importFileLog.setUploadType(uploadType);
      importFileLog = importFileLogService.saveImportFileLog(importFileLog);
      return importFileLog;
    } catch (Exception e) {
      logger.error("保存文件日志出现问题，保存文件路径为" + savePath);
      return null;
    }
  }

  private Object acceptValidation(String encryptedData) throws SysServiceException {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    try {
      mapDate.put("openid", 99999999L);// 系统默认openId
      mapDate.put("token", "11111111siescirv");// 系统默认token
      mapDate.put("data", encryptedData);
    } catch (Exception e) {
      logger.error("根据上传的PDF文件抽取出的json字符串拆分内容报错", e);
      throw new SysServiceException(e);
    }
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  private void saveImportFileLog(ImportFileLog importFileLog) {
    try {
      importFileLogService.saveImportFileLog(importFileLog);
    } catch (Exception e) {
      logger.error("保存文件导入日志出现问题!");
    }
  }

  @Override
  public Map<String, Object> viewValidateDetail(String uuId, KpiValidateForm form) throws SysServiceException {
    List<Map<String, Object>> psnList = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> insList = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> prjPubList = new ArrayList<Map<String, Object>>();
    int psnPass = 0;
    int psnCount = 0;
    int insPass = 0;
    int insCount = 0;
    int prjPubPass = 0;
    int prjPubCount = 0;
    try {
      KpiValidateMain main = kpiValidateMainDao.get(uuId);
      form.setTitle(main.getTitle());
      List<KpiValidateDetail> detailList = null;
      if ("2".equals(main.getKeyType()) || "3".equals(main.getKeyType())) {
        detailList = kpiValidateDetailDao.findDetailByUuidAndType(uuId);
        form.setFlag(false);
      } else {
        detailList = kpiValidateDetailDao.findDetailByUuid(uuId);
        form.setFlag(true);
      }
      for (KpiValidateDetail kpiValidateDetail : detailList) {
        if (kpiValidateDetail.getType() == 1 && kpiValidateDetail.getInterfaceStatus() == 1) {
          String psn = kpiValidateDetail.getParamsOut();
          Map<String, Object> psnMap = new HashMap<String, Object>();
          psnMap = JacksonUtils.jsonToMap(psn);
          // 拼接页面提示。
          viewPsnTips(psnMap);
          psnList.add(psnMap);
          psnCount++;
          if (kpiValidateDetail.getStatus() == 1) {
            psnPass++;
          }
        } else if (kpiValidateDetail.getType() == 2 && kpiValidateDetail.getInterfaceStatus() == 1) {
          String ins = kpiValidateDetail.getParamsOut();
          Map<String, Object> insMap = new HashMap<String, Object>();
          insMap = JacksonUtils.jsonToMap(ins);
          // 拼接页面提示。
          viewInsTips(insMap);
          insList.add(insMap);
          insCount++;
          if (kpiValidateDetail.getStatus() == 1) {
            insPass++;
          }
        } else if (kpiValidateDetail.getType() == 3 && kpiValidateDetail.getInterfaceStatus() == 1) {
          String insPub = kpiValidateDetail.getParamsOut();
          Map<String, Object> insPubMap = new HashMap<String, Object>();
          insPubMap = JacksonUtils.jsonToMap(insPub);
          // 拼接页面提示。
          viewInsPubTips(insPubMap);
          prjPubList.add(insPubMap);
          prjPubCount++;
          if (kpiValidateDetail.getStatus() == 1) {
            prjPubPass++;
          }
        } else if (kpiValidateDetail.getType() == 4 && kpiValidateDetail.getInterfaceStatus() == 1) {
        }
      }
      form.setPsnList(psnList);
      form.setInsList(insList);
      form.setPrjPubList(prjPubList);
      form.setPsnPass(psnPass);
      form.setPsnCount(psnCount);
      form.setInsPass(insPass);
      form.setInsCount(insCount);
      form.setPrjPubPass(prjPubPass);
      form.setPrjPubCount(prjPubCount);
      form.setTotalDetailNum(psnCount + insCount + prjPubCount);
    } catch (Exception e) {
      logger.error("查看科研验证详情报错", e);
      throw new SysServiceException(e);
    }
    return null;
  }

  public void judgementPayment(KpiValidateForm form, String userRealIP) throws SysServiceException {
    try {
      List<Long> insIds = sieInsPersonDao.findPsnInsIds(form.getUserId());
      if (CollectionUtils.isNotEmpty(insIds)) {
        Collections.sort(insIds);
        Sie6Institution ins = sie6InstitutionDao.get(insIds.get(0));
        boolean flag = kpiPayValidateDao.judgementPayment(insIds);
        boolean endTimeFlag = kpiPayValidateDao.judgementPaymentByEndTime(insIds);
        if (flag) {
          form.setIsPaymentIns(1);
        } else {
          Map<String, String> dataPermitIp = matchPermitIp(userRealIP);
          if ("true".equals(dataPermitIp.get("flag"))) {
            form.setIsPaymentIns(1);
          } else {
            form.setAdminPsnName(ins.getContactPerson());
            form.setAdminEmail(ins.getContactEmail());
            if (endTimeFlag) { // 过期
              form.setIsPaymentIns(2);
            } else {
              form.setIsPaymentIns(0);
            }
          }
        }
      } else {
        Map<String, String> dataPermitIp = matchPermitIp(userRealIP);
        if ("true".equals(dataPermitIp.get("flag"))) {
          form.setIsPaymentIns(1);
        } else if ("false".equals(dataPermitIp.get("flag"))) {
          form.setIsPaymentIns(0);
        } else if ("overdue".equals(dataPermitIp.get("flag"))) {
          form.setIsPaymentIns(2);
        }
      }
    } catch (Exception e) {
      form.setIsPaymentIns(1);
      logger.error("判断当前用户是否为付费单位人员报错", e);
      throw new SysServiceException(e);
    }
  }

  private Map<String, String> matchPermitIp(String userIp) {
    Boolean flag = false;
    Map<String, String> data = new HashMap<String, String>();
    List<KpiPayValidate> listByIp = kpiPayValidateDao.judgementPaymentList();
    if (CollectionUtils.isNotEmpty(listByIp)) {
      for (KpiPayValidate kpiPayValidate : listByIp) {
        String[] ipList = kpiPayValidate.getPermitIP().split(";");
        for (String ip : ipList) {
          int num = countStr(ip, ".");
          if (num == 3) {
            if (userIp.contains(ip)) {
              flag = true;
              data.put("flag", "true");
              break;
            }
          } else if (num == 2) {
            if (userIp.contains(ip + ".")) {
              flag = true;
              data.put("flag", "true");
              break;
            }
          }
        }
        if (flag) {
          break;
        }
        data.put("flag", "false");
      }
    } else {
      List<KpiPayValidate> listByEndTime = kpiPayValidateDao.judgementPaymentListByEndTime();
      if (CollectionUtils.isNotEmpty(listByEndTime)) {
        for (KpiPayValidate kpiPayValidate : listByEndTime) {
          String[] ipList = kpiPayValidate.getPermitIP().split(";");
          for (String ip : ipList) {
            int num = countStr(ip, ".");
            if (num == 3) {
              if (userIp.contains(ip)) {
                flag = true;
                data.put("flag", "overdue");
                break;
              }
            } else if (num == 2) {
              if (userIp.contains(ip + ".")) {
                flag = true;
                data.put("flag", "overdue");
                break;
              }
            }
          }
          if (flag) {
            break;
          }
          data.put("flag", "false");
        }
      } else {
        data.put("flag", "false");
      }
    }
    return data;
  }

  private int countStr(String str, String sToFind) {
    int num = 0;
    while (str.contains(sToFind)) {
      str = str.substring(str.indexOf(sToFind) + sToFind.length());
      num++;
    }
    return num;
  }

  @Override
  public Map<String, Object> beforeSubMit(KpiValidateForm form) throws SysServiceException {
    List<Integer> limit = new ArrayList<Integer>();
    Boolean flag = false;
    String userIP = form.getClientIP();
    Map<String, Object> resultMap = new HashMap<String, Object>();

    // 加入判断个人是否购买付费验证的处理
    KpiPayValidateUser payUser = kpiPayValidateUserDao.judgementPaymentListByPsnId(form.getUserId());
    if (payUser != null) {
      resultMap.put("flag", "true"); // 无限制
      return resultMap;
    }
    List<Long> insIds = sieInsPersonDao.findPsnInsIds(form.getUserId());
    List<KpiPayValidate> mainList = kpiPayValidateDao.getLimitCount(insIds);
    if (CollectionUtils.isNotEmpty(mainList)) {
      for (KpiPayValidate kpiPayValidate : mainList) {
        if (kpiPayValidate.getLimitPDf() == null) {
          resultMap.put("flag", "true"); // 无限制
          return resultMap;
        } else {
          limit.add(kpiPayValidate.getLimitPDf());
        }
      }
    }
    List<KpiPayValidate> listByIp = kpiPayValidateDao.judgementPaymentList();
    if (CollectionUtils.isNotEmpty(listByIp)) {
      for (KpiPayValidate kpiPayValidate : listByIp) {
        if (StringUtils.isNotBlank(kpiPayValidate.getPermitIP())) {
          String[] ipList = kpiPayValidate.getPermitIP().split(";");
          for (String ip : ipList) {
            int num = countStr(ip, ".");
            if (num == 3) {
              if (userIP.contains(ip)) {
                if (kpiPayValidate.getLimitPDf() != null) {
                  limit.add(kpiPayValidate.getLimitPDf());
                } else {
                  resultMap.put("flag", "true");// 无限制
                  return resultMap;
                }
                flag = true;
                break;
              }
            } else if (num == 2) {
              if (userIP.contains(ip + ".")) {
                if (kpiPayValidate.getLimitPDf() != null) {
                  limit.add(kpiPayValidate.getLimitPDf());
                } else {
                  resultMap.put("flag", "true");
                  return resultMap;
                }
                flag = true;
                break;
              }
            }
          }
          if (flag) {
            break;
          }
        }
      }
    }
    int count = kpiValidateMainDao.getValidateCountByPsnId(form.getUserId());
    if (CollectionUtils.isNotEmpty(limit)) {
      Collections.sort(limit);
    } else {
      limit.add(10);
    }
    if (count >= limit.get(limit.size() - 1)) {
      resultMap.put("flag", "false");
    } else {
      resultMap.put("flag", "true");
    }
    return resultMap;
  }

  @Override
  public void refreshStatusCurrent(KpiValidateForm form) throws SysServiceException {
    try {
      KpiValidateMain main = kpiValidateMainDao.get(Des3Utils.decodeFromDes3(form.getDes3Uuid()));
      Integer mainStatus = main.getStatus();
      if (mainStatus == 0) {
        form.setStatus(0);
      } else {
        // List<KpiValidateDetail> detailList = kpiValidateDetailDao.findDetailByUuid(main.getUuId());
        // form.setStatus(1);
        // for (KpiValidateDetail kpiValidateDetail : detailList) {
        // Integer detailStatus = kpiValidateDetail.getStatus();
        // if (detailStatus == 2 || detailStatus == 3 || detailStatus == 4) {
        // form.setStatus(2);
        // break;
        // }
        // }
        form.setStatus(1);
      }
    } catch (Exception e) {
      logger.error("刷新当前数据检验结果报错", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public void delValidateMainUpload(Long id) throws SysServiceException {
    try {
      KpiValidateMainUpload upload = kpiValidateMainUploadDao.get(id);
      if (upload != null) {
        upload.setIsDel(1);
      }
    } catch (Exception e) {
      logger.error("删除KPI_VALIDATE_MAIN_UPLOAD记录异常，id" + id, e);
      throw new SysServiceException(e);
    }
  }

  @SuppressWarnings("rawtypes")
  public void viewPsnTips(Map<String, Object> psnMap) {
    String validateEmaile = ObjectUtils.toString(psnMap.get("validate_email"));
    String validateMobile = ObjectUtils.toString(psnMap.get("validate_mobile"));
    if ("2".equals(validateEmaile)) {
      List correlationEmailList = (List) psnMap.get("correlation_email");
      String wemail = ObjectUtils.toString(psnMap.get("w_email"));
      if (CollectionUtils.isNotEmpty(correlationEmailList) && correlationEmailList.size() > 1) {
        @SuppressWarnings("unchecked")
        // 多个则用中文逗号隔开
        String correlationEmailStr = String.join("，", correlationEmailList);
        psnMap.put("mistake_email_tip", wemail);
        psnMap.put("right_email_tip", correlationEmailStr + "曾使用此邮箱");
      } else if (correlationEmailList.size() == 1) {
        psnMap.put("mistake_email_tip", wemail);
        psnMap.put("right_email_tip", correlationEmailList.get(0) + "曾使用此邮箱");
      } else {
        psnMap.put("mistake_email_tip", wemail);
        psnMap.put("right_email_tip", "");
      }
    }
    if ("2".equals(validateMobile)) {
      List correlationMobileList = (List) psnMap.get("correlation_mobile");
      String wmobile = ObjectUtils.toString(psnMap.get("w_mobile"));
      if (CollectionUtils.isNotEmpty(correlationMobileList) && correlationMobileList.size() > 1) {
        @SuppressWarnings("unchecked")
        String correlationMobileStr = String.join("，", correlationMobileList);
        psnMap.put("mistake_mobile_tip", wmobile);
        psnMap.put("right_mobile_tip", correlationMobileStr + "曾使用此手机号");
      } else if (correlationMobileList.size() == 1) {
        psnMap.put("mistake_mobile_tip", wmobile);
        psnMap.put("right_mobile_tip", correlationMobileList.get(0) + "曾使用此手机号");
      } else {
        psnMap.put("mistake_mobile_tip", wmobile);
        psnMap.put("right_mobile_tip", "");
      }
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public void viewInsTips(Map<String, Object> insMap) {
    String validateUniform = ObjectUtils.toString(insMap.get("validate_uniform"));
    String wuniform = ObjectUtils.toString(insMap.get("w_uniform")); // 编号
    String orgName = ObjectUtils.toString(insMap.get("org_name")); // 名称
    StringBuffer correlationArray = new StringBuffer();
    if ("2".equals(validateUniform)) {
      List correlationUniformList = (List) insMap.get("correlation_uniform");
      if (CollectionUtils.isNotEmpty(correlationUniformList) && correlationUniformList.size() > 0) {
        for (int i = 0; i < correlationUniformList.size(); i++) {
          Map<String, Object> dataMap = (Map<String, Object>) correlationUniformList.get(i);
          if (i == 0) {
            correlationArray.append(dataMap.get("name") + "，" + dataMap.get("orgNo"));
          } else {
            correlationArray.append("；");
            correlationArray.append(dataMap.get("name") + "，" + dataMap.get("orgNo"));
          }
        }
      }
      if (correlationArray.length() > 0) {
        insMap.put("mistake_uniform_tip", orgName + "，" + wuniform);
        insMap.put("right_uniform_tip", correlationArray.toString());
      } else {
        insMap.put("mistake_uniform_tip", orgName + "，" + wuniform);
        insMap.put("right_uniform_tip", "");
      }
    }
  }

  @SuppressWarnings("rawtypes")
  public void viewInsPubTips(Map<String, Object> insPubMap) {
    Object obj = insPubMap.get("correlation_pub");
    if (obj != null) {
      Map correlationPubMap = (Map) obj;
      Iterator itor = correlationPubMap.keySet().iterator();
      while (itor.hasNext()) {
        // 获取验证状态
        String validateTitle = ObjectUtils.toString(insPubMap.get("validate_title")); // 标题
        String validateDoi = ObjectUtils.toString(insPubMap.get("validate_doi"));// doi
        String validateJname = ObjectUtils.toString(insPubMap.get("validate_jname"));// 期刊
        String validateAuthor = ObjectUtils.toString(insPubMap.get("validate_author"));// 作者
        String validateFundinfo = ObjectUtils.toString(insPubMap.get("validate_fundinfo"));// 标注
        String validatePubyear = ObjectUtils.toString(insPubMap.get("validate_pubyear"));// 年份
        // 获取填写的值
        String wtitle = ObjectUtils.toString(insPubMap.get("zh_title")); // 标题
        String wdoi = ObjectUtils.toString(insPubMap.get("w_doi"));// doi
        String wjname = ObjectUtils.toString(insPubMap.get("w_jname"));// 期刊
        String wauthor = ObjectUtils.toString(insPubMap.get("w_author"));// 作者
        String wfundinfo = ObjectUtils.toString(insPubMap.get("w_fundinfo"));// 标注
        String wpubyear = ObjectUtils.toString(insPubMap.get("w_pubyear"));// 年份
        String key = ObjectUtils.toString(itor.next());
        String value = ObjectUtils.toString(correlationPubMap.get(key));
        if ("title".equals(key)) {
          if ("2".equals(validateTitle)) {
            insPubMap.put("mistake_title_tip", wtitle);
            if (StringUtils.isNotBlank(value)) {
              insPubMap.put("right_title_tip", value);
            } else {
              insPubMap.put("right_title_tip", "");
            }
          }
        }
        if ("doi".equals(key)) {
          if ("2".equals(validateDoi)) {
            insPubMap.put("mistake_doi_tip", wdoi);
            if (StringUtils.isNotBlank(value)) {
              insPubMap.put("right_doi_tip", value);
            } else {
              insPubMap.put("right_doi_tip", "");
            }
          }
        }
        if ("journalName".equals(key)) {
          if ("2".equals(validateJname)) {
            insPubMap.put("mistake_jname_tip", wjname);
            if (StringUtils.isNotBlank(value)) {
              insPubMap.put("right_jname_tip", value);
            } else {
              insPubMap.put("right_jname_tip", "");
            }
          }
        }
        if ("authorNames".equals(key)) {
          if ("2".equals(validateAuthor)) {
            insPubMap.put("mistake_author_tip", wauthor);
            if (StringUtils.isNotBlank(value)) {
              insPubMap.put("right_author_tip", value);
            } else {
              insPubMap.put("right_author_tip", "");
            }
          }
        }
        if ("fundingInfo".equals(key)) {
          if ("2".equals(validateFundinfo)) {
            insPubMap.put("mistake_fund_tip", wfundinfo);
            if (StringUtils.isNotBlank(value)) {
              insPubMap.put("right_fund_tip", value);
            } else {
              insPubMap.put("right_fund_tip", "");
            }
          }
        }
        if ("publishYear".equals(key)) {
          if ("2".equals(validatePubyear)) {
            insPubMap.put("mistake_year_tip", wpubyear);
            if (StringUtils.isNotBlank(value)) {
              insPubMap.put("right_year_tip", value);
            } else {
              insPubMap.put("right_year_tip", "");
            }
          }
        }
      }
    }
  }

  @Override
  public Integer isAlreadyPaid(Long psnId, String ip) throws SysServiceException {
    Integer status = 0;
    Object resultData = buildParam(psnId, ip);
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(resultData.toString());
    if (resultMap != null && "success".equals(resultMap.get("status"))) {
      List<Map<String, Object>> mapList = (List<Map<String, Object>>) resultMap.get("result");
      Object obj = mapList.get(0).get("status");
      status = Integer.parseInt(obj == null ? "0" : String.valueOf(obj));
    } else {
      logger.error("科研验证受理接口调用失败！原因：{}", resultMap.get("msg"));
    }
    return status;
  }

  private Object buildParam(Long psnId, String ip) throws SysServiceException {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("psnId", psnId);
    dataMap.put("appType", "2");// appType=1，开题分析；appType=2科研验证
    dataMap.put("ip", ip);// 用户端ip
    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("openid", 99999999L);// 系统默认openId
    mapDate.put("token", "11111111kpipayv1");// 系统默认token
    mapDate.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  @Override
  @Audit(action = "ValidateClickAdd", actionResolverName = "AUDIT_ACTION_RESOLVER", resourceResolverName = "PARAM_JSON")
  public int clickAdd(Integer isPay, Long userId, String zhName, String userRealIP) {
    return isPay;
  }

  @Override
  public String findPsnNameByInsIdAndPsnId(Long psnId, Long insId) {
    String zhName = "";
    try {
      SieInsPerson person = sieInsPersonDao.findPsnIns(psnId, insId);
      if (person != null) {
        zhName = person.getZhName();
      }
    } catch (SysDataException e) {
      e.printStackTrace();
    }
    return zhName;
  }

  @Override
  public String findPsnNameByPsnId(Long psnId) {
    List<SieInsPerson> psnList = sieInsPersonDao.findPsnInsListByPsnId(psnId);
    String zhName = "";
    if (CollectionUtils.isNotEmpty(psnList)) {
      SieInsPerson person = psnList.get(0);
      zhName = person.getZhName();
    }
    return zhName;
  }

}
