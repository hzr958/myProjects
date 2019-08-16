package com.smate.core.base.psn.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dto.config.ConfigElement;
import com.smate.core.base.psn.dto.config.ConfigItem;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.psn.model.psncnf.PsnConfigBrief;
import com.smate.core.base.psn.model.psncnf.PsnConfigContact;
import com.smate.core.base.psn.model.psncnf.PsnConfigEdu;
import com.smate.core.base.psn.model.psncnf.PsnConfigExpertise;
import com.smate.core.base.psn.model.psncnf.PsnConfigMoudle;
import com.smate.core.base.psn.model.psncnf.PsnConfigPosition;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.model.psncnf.PsnConfigTaught;
import com.smate.core.base.psn.model.psncnf.PsnConfigWork;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.resume.ResumeInitProcess;
import com.smate.core.base.utils.number.NumberUtils;

public class PsnCnfUtils {

  public static boolean canRun(Long runs, PsnCnfEnum... psnCnfEnums) {
    boolean can = false;
    if (runs == null) {
      return can;
    }
    for (PsnCnfEnum psnCnfEnum : psnCnfEnums) {

      int run = Integer.valueOf(psnCnfEnum.toString());
      can = (run & runs) == run;
      if (!can) {
        break;
      }
    }
    return can;
  }

  public static boolean canView(Integer anyView, Integer anyUserLimit) {
    boolean can = false;
    if (anyView == null) {
      return can;
    }

    can = (anyUserLimit & anyView) == anyUserLimit;

    return can;
  }

  // 旧数据权限转换
  public static Integer convertAnyUser(Integer user2, Integer user3) {
    if (user2 == null || ResumeInitProcess.SHOW.equals(user2)) {
      return PsnCnfConst.ALLOWS;
    }

    if (user3 == null || ResumeInitProcess.SHOW.equals(user3)) {
      return PsnCnfConst.ALLOWS_FRIEND | PsnCnfConst.ALLOWS_SELF;
    }

    return PsnCnfConst.ALLOWS_SELF;
  }

  // 旧数据空/非空转换
  public static Integer convertAnyView(boolean view) {
    return convertAnyUser(view ? 1 : 0, view ? 1 : 0);
  }

  // 旧数据模块显示/隐藏转换
  public static Long convertAnyMod(Integer user5, PsnCnfEnum psnCnfEnum) {
    if (user5 == null || ResumeInitProcess.SHOW.equals(user5)) {
      return Long.valueOf(psnCnfEnum.toString());
    }
    return 0L;
  }

  // 旧数据列表转换
  public static Map<String, Integer> convert2Map(List<ConfigElement> cList) {
    if (cList == null) {
      return new HashMap<String, Integer>();
    }

    Map<String, Integer> cMap = new HashMap<String, Integer>();
    for (ConfigElement ce : cList) {
      if (NumberUtils.isNumber(ce.getElemCode())) {
        cMap.put(ce.getElemCode(), ce.getIsShow());
      }
    }
    return cMap;
  }

  // 旧数据模块顺序
  public static void convert2Seqs(ConfigItem ci, Map<Integer, PsnCnfEnum> seqMap, PsnCnfEnum psnCnfEnum) {
    if (ci == null || ci.getSeqNum() == null || ci.getSeqNum() < 0) {
      seqMap.put(20 + Integer.valueOf(psnCnfEnum.toString()), psnCnfEnum);
    } else {
      seqMap.put(ci.getSeqNum(), psnCnfEnum);
    }
  }

  // 旧数据模块显示/隐藏
  public static Long convert2Mod(ConfigItem ci, PsnCnfEnum psnCnfEnum) {
    if (ci == null || ci.getIsShow() == null || ResumeInitProcess.SHOW.equals(ci.getIsShow())) {
      return Long.valueOf(psnCnfEnum.toString());
    } else {
      return Long.valueOf(ci.getIsShow());
    }
  }

  /*
   * // 多数据类型主键转换(如：成果配置主键、工作经历配置主键)
   * 
   * @SuppressWarnings("unchecked") public static List<CnfBasePk> convertCnfBasePk(String cnfBases) {
   * List<CnfBasePk> cnfPkList = (List<CnfBasePk>) JacksonUtils.jsonObject(cnfBases, new
   * TypeReference<List<CnfBasePk>>() { }); return cnfPkList; }
   */
  // 设置cnfId
  public static void convertCnfId(PsnCnfBase cnfBase, PsnConfig cnfExist) {

    if (cnfBase instanceof PsnConfigBrief) {
      ((PsnConfigBrief) cnfBase).setCnfId(cnfExist.getCnfId());
    } else if (cnfBase instanceof PsnConfigContact) {
      ((PsnConfigContact) cnfBase).setCnfId(cnfExist.getCnfId());
    } else if (cnfBase instanceof PsnConfigEdu) {
      ((PsnConfigEdu) cnfBase).getId().setCnfId(cnfExist.getCnfId());
    } else if (cnfBase instanceof PsnConfigWork) {
      ((PsnConfigWork) cnfBase).getId().setCnfId(cnfExist.getCnfId());
    } else if (cnfBase instanceof PsnConfigExpertise) {
      ((PsnConfigExpertise) cnfBase).setCnfId(cnfExist.getCnfId());
    } else if (cnfBase instanceof PsnConfigMoudle) {
      ((PsnConfigMoudle) cnfBase).setCnfId(cnfExist.getCnfId());
    } else if (cnfBase instanceof PsnConfigPrj) {
      ((PsnConfigPrj) cnfBase).getId().setCnfId(cnfExist.getCnfId());
    } else if (cnfBase instanceof PsnConfigPub) {
      ((PsnConfigPub) cnfBase).getId().setCnfId(cnfExist.getCnfId());
    } else if (cnfBase instanceof PsnConfigTaught) {
      ((PsnConfigTaught) cnfBase).setCnfId(cnfExist.getCnfId());
    } else if (cnfBase instanceof PsnConfigPosition) {
      ((PsnConfigPosition) cnfBase).setCnfId(cnfExist.getCnfId());
    }
  }
}
