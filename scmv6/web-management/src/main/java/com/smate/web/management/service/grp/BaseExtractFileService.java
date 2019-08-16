package com.smate.web.management.service.grp;

import com.smate.core.base.consts.service.InstitutionService;
import com.smate.web.management.model.grp.GrpItemInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aijiangbin
 * @create 2019-06-13 14:20
 **/
 abstract class BaseExtractFileService  implements  ExtractFileService{


  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected static  final  String  splitNameReg = "，|；|;| and ";
  protected static  final  String  splitKeywordReg = "；|;";
  @Autowired
  private InstitutionService institutionService;
  /**
   *
   *  resutlMap.put("xmlData", "");   把文件提取成 xml
   *  resutlMap.put("warnmsg", "");   错误信息
   *  resutlMap.put("count", 0);      条数
   * @return
   */
  @Override
  public Map<String, Object> extractFile(File file , String sourceFileFileName) {
    Map<String, Object> checkFile = checkFile(file , sourceFileFileName);
    Map<String,Object> resutlMap=  new HashMap<>();
    if(checkFile == null){
       resutlMap = extractData(file);
    }else{
      resutlMap.put("count",0);
      resutlMap.put("warnmsg",resutlMap.get("warnmsg"));
    }
    return resutlMap;
  }

  /**
   * 检查文件     正确直接返回 null
   *  resutlMap.put("warnmsg", "");   错误信息
   * @param file
   * @return
   */
  public abstract  Map<String , Object>  checkFile(File file ,String sourceFileFileName) ;

  public abstract List<GrpItemInfo> parseFile(File file) ;


  public Map<String , Object>   extractData(File file){
    List<GrpItemInfo> list = parseFile(file);
    Map<String,Object> resutlMap=  new HashMap<>();
    if(CollectionUtils.isNotEmpty(list)){
      resutlMap.put("count",list.size());
      resutlMap.put("list",list);
    }else{
      resutlMap.put("count",0);
    }
    return resutlMap;
  }



 /* *//**
   * 反转义数据
   * @param prjInfo
   *//*
  private void escapeObj(PrjInfo prjInfo){
    try{
      Field[] fields = PrjInfo.class.getDeclaredFields();
      for(Field field : fields){
        field.setAccessible(true);
        Object o = field.get(prjInfo);
        Class<?> type = field.getType();
        if("java.lang.String".equals(type.getName()) && StringUtils.isNotBlank(o.toString())){
          String val = StringEscapeUtils.escapeHtml4(o.toString());
          val = XssUtils.filterByXssStr(val);
          field.set(prjInfo ,val );
        }
      }
    }catch (Exception e){
      logger.error("反转义数据异常",e);
    }

  }*/




}
