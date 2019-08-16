package com.smate.web.prj.service.project.fileimport;

import com.smate.web.prj.form.fileimport.PrjInfoDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提取 mdb 格式的文件 文件服务
 *
 * @author aijiangbin
 * @create 2019-06-13 14:44
 **/

@Service("extractMdbFileService")
@Transactional (rollbackOn = Exception.class)
public class ExtractMdbFileServiceImpl extends  BaseExtractFileService{
  @Override
  public Map<String, Object> checkFile(File file ,String sourceFileFileName) {
    Map result = new HashMap();
    String suffix =  sourceFileFileName.substring(sourceFileFileName.lastIndexOf(".")) ;
    if(!".mdb".equalsIgnoreCase(suffix) && !".xlsx".equalsIgnoreCase(suffix)){
      result.put("warnmsg","文件格式错误") ;
      return result;
    }
    return null;
  }

  @Override
  public List<PrjInfoDTO> parseFile(File file) {
      List<PrjInfoDTO>  list = new ArrayList<>();
      try {
        List<Map<String, Object>> mapList = AccessMdbExtract.getData(file);
        if (CollectionUtils.isNotEmpty(mapList)) {
          for (Map<String, Object> map : mapList) {
            String prjNO = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("XMPZH")));// 项目批准号
            String titleZh = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("PZZWMC")));// 批准后的中文名
            String titleEn = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("PZYWMC")));// 批准后的英文名
            if ("".equals(titleZh)) {
              titleZh = titleEn;
            }
            if (StringUtils.isBlank(titleZh) && StringUtils.isBlank(titleEn)) {
              continue;
            }
            PrjInfoDTO info = new PrjInfoDTO();
            list.add(info);
            info.setSourceDbCode("SCMIRIS") ;
            info.setZhTitle(titleZh);
            info.setEnTitle(titleEn);
            String prjType = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("grant_type_id")));// 项目类别
            info.setPrjType(prjType);
            info.setProjectNo(prjNO);
            String authorNames = "";
            String psnName = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("XM")));// 负责人名
            String institutions = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("DWMC")));// 机构
            String email = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("EMAIL")));// 邮箱
            if (StringUtils.isNotBlank(psnName)) {
              info.setLeader(psnName);
              authorNames += psnName;
              Map<String , String>  memberMap = new HashMap<>();
              memberMap.put("seq_no","0");
              memberMap.put("member_psn_name",psnName);
              memberMap.put("ins_name1",institutions);
              memberMap.put("email",email);
              memberMap.put("notify_author","1");
              info.getMembersList().add(memberMap);
            }
            authorNames = psnName;
            for (int i = 1; i < 10; i++) {
              String tempName = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("XM" + i)));// 项目参与人
              if (StringUtils.isNotBlank(tempName) && !psnName.equals(tempName)) {
                Map<String , String>  memberMap = new HashMap<>();
                 institutions = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("DWMC" + i)));// 机构
                memberMap.put("seq_no",(i)+"");
                memberMap.put("member_psn_name",tempName);
                memberMap.put("ins_name1",institutions);
                memberMap.put("email","");
                memberMap.put("notify_author","0");
                info.getMembersList().add(memberMap);
              }
            }
            info.setPrjMembers(authorNames);
            String insName = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("YTDWMC")));// 依托单位
            info.setInsName(insName);
            String amount = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("PZJE")));// 资助资金  单位万元
            if(StringUtils.isNotBlank(amount)){
              BigDecimal bg=new BigDecimal(Double.parseDouble(amount)*10000);
              if(bg.toString().indexOf(".")>0){
                info.setPrjAmount(bg.toString().split("\\.")[0]);
              }else{
                info.setPrjAmount(bg.toString());
              }
            }
            String ekeywords = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("YWZTC")));// 项目英文主题词
            info.setEnKeywords(ekeywords);
            String ckeywords = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("ZWZTC")));// 项目中文主题词
            info.setZhkeywords(ckeywords);
            String startDate = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("PZQSNY")));// 批准项目起始日期
            info.setStartDate(startDate);
            String endDate = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("PZZZNY")));// 批准项目结束日期
            info.setEndDate(endDate);
            info.setAgency("国家自然科学基金");
            info.setScheme(ObjectUtils.toString(map.get("grant_type_id")));
            //xmlWriter.writeAttribute("prj_from_id", "5");
            String cabstract = StringUtils.trimToEmpty(ObjectUtils.toString(map.get("ZY")));// 项目摘要
            info.setZhAbstract(cabstract);
            info.setEnAbstract(cabstract);
            // 申请学科代码 根据视图v_category_map_scm_nsfc 进行申请代码到科研之友代码的转换
            info.setSubject_code1(ObjectUtils.toString(map.get("SQXKDM1")));
            info.setSubject_code2(ObjectUtils.toString(map.get("SQXKDM2")));
          }
        }
      } catch (Exception e) {
        logger.error("将mdb文件拆分成 prjInfo 对象异常", e);
      }
      return list;
  }
}
