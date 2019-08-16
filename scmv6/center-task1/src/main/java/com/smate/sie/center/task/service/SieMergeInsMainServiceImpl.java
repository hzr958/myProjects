package com.smate.sie.center.task.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.dao.consts.SieMergeInstitutionBakDao;
import com.smate.core.base.utils.dao.impfilelog.ImportFileLogDao;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.dao.security.role.SieMergeInsPortalBakDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.model.consts.SieMergeInstitutionBak;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;
import com.smate.core.base.utils.model.rol.SieMergeInsPortalBak;
import com.smate.sie.center.task.dao.Sie6InsUnitDao;
import com.smate.sie.center.task.dao.SieArchiveFileDao;
import com.smate.sie.center.task.dao.SieMergeInsBakDao;
import com.smate.sie.center.task.dao.SieMergeInsReflushDao;
import com.smate.sie.center.task.dao.SieMergePdwhInsAddrConstBakDao;
import com.smate.sie.center.task.dao.SiePatDupFieldsDao;
import com.smate.sie.center.task.dao.SiePatKeyWordsDao;
import com.smate.sie.center.task.dao.SiePdwhInsAddrConstDao;
import com.smate.sie.center.task.dao.SiePdwhInsAddrConstRefreshDao;
import com.smate.sie.center.task.dao.SiePubDupFieldsDao;
import com.smate.sie.center.task.dao.SiePubKeyWordsDao;
import com.smate.sie.center.task.model.PatKeyWords;
import com.smate.sie.center.task.model.PubKeyWords;
import com.smate.sie.center.task.model.Sie6InsUnit;
import com.smate.sie.center.task.model.SieMergeInsBak;
import com.smate.sie.center.task.model.SieMergeInsReflush;
import com.smate.sie.center.task.model.SieMergePdwhInsAddrConstBak;
import com.smate.sie.center.task.model.SiePatDupFields;
import com.smate.sie.center.task.model.SiePdwhInsAddrConst;
import com.smate.sie.center.task.model.SiePubDupFields;
import com.smate.sie.center.task.pdwh.service.PublicationXmlManager;
import com.smate.sie.center.task.pdwh.service.SieProjectXmlManager;
import com.smate.sie.core.base.utils.dao.ins.SieInsAliasDao;
import com.smate.sie.core.base.utils.dao.ins.SieInsRegionDao;
import com.smate.sie.core.base.utils.dao.ins.SieMergeInsAliasBakDao;
import com.smate.sie.core.base.utils.dao.insguid.SieInsGuidDao;
import com.smate.sie.core.base.utils.dao.prd.Sie6ProductDao;
import com.smate.sie.core.base.utils.dao.prj.SiePrjDupFieldsDao;
import com.smate.sie.core.base.utils.dao.prj.SiePrjKewordDao;
import com.smate.sie.core.base.utils.dao.prj.SiePrjMemberDao;
import com.smate.sie.core.base.utils.dao.prj.SieProjectDao;
import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;
import com.smate.sie.core.base.utils.dao.psn.SieMergeBackPsnInsDao;
import com.smate.sie.core.base.utils.dao.pub.SieInsRefDbDao;
import com.smate.sie.core.base.utils.dao.pub.SiePatMemberDao;
import com.smate.sie.core.base.utils.dao.pub.SiePatentDao;
import com.smate.sie.core.base.utils.dao.pub.SiePubMemberDao;
import com.smate.sie.core.base.utils.dao.pub.SiePublicationDao;
import com.smate.sie.core.base.utils.model.ins.SieInsAlias;
import com.smate.sie.core.base.utils.model.ins.SieInsRefDbId;
import com.smate.sie.core.base.utils.model.ins.SieMergeInsAliasBak;
import com.smate.sie.core.base.utils.model.prd.Sie6Product;
import com.smate.sie.core.base.utils.model.prj.SiePrjDupFields;
import com.smate.sie.core.base.utils.model.prj.SiePrjKeword;
import com.smate.sie.core.base.utils.model.prj.SiePrjMember;
import com.smate.sie.core.base.utils.model.prj.SieProject;
import com.smate.sie.core.base.utils.model.psn.SieInsPerson;
import com.smate.sie.core.base.utils.model.psn.SieInsPersonPk;
import com.smate.sie.core.base.utils.model.psn.SieMergeBackPsnIns;
import com.smate.sie.core.base.utils.model.pub.SieInsRefDb;
import com.smate.sie.core.base.utils.model.pub.SiePatMember;
import com.smate.sie.core.base.utils.model.pub.SiePatent;
import com.smate.sie.core.base.utils.model.pub.SiePubMember;
import com.smate.sie.core.base.utils.model.pub.SiePublication;

/***
 * 合并单位服务
 * 
 * @author 叶星源
 * @Date 201810
 */
@Service("sieMergeInsMainService")
@Transactional(rollbackOn = Exception.class)
public class SieMergeInsMainServiceImpl implements SieMergeInsMainService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SieMergeInsReflushDao sieMergeInsReflushDao;
  // 成果
  @Autowired
  private SiePublicationDao siePublicationDao;
  @Autowired
  private SiePubMemberDao siePubMemberDao;
  @Autowired
  private SiePubDupFieldsDao siePubDupFieldsDao;
  @Autowired
  private SiePubKeyWordsDao siePubKeyWordsDao;
  // 专利
  @Autowired
  private SiePatentDao siePatentDao;
  @Autowired
  private SiePatMemberDao siePatMemberDao;
  @Autowired
  private SiePatDupFieldsDao siePatDupFieldsDao;
  @Autowired
  private SiePatKeyWordsDao siePatKeyWordsDao;
  // 项目
  @Autowired
  private SieProjectDao sieProjectDao;
  @Autowired
  private SiePrjMemberDao siePrjMemberDao;
  @Autowired
  private SiePrjKewordDao siePrjKewordDao;
  @Autowired
  private SiePrjDupFieldsDao siePrjDupFieldsDao;
  // 部门
  @Autowired
  private Sie6InsUnitDao sie6InsUnitDao;
  // 人员
  @Autowired
  private SieInsPersonDao sieInsPersonDao;
  // 产品
  @Autowired
  private Sie6ProductDao sie6ProductDao;
  @Autowired
  private SieMergeBackPsnInsDao sieMergeBackPsnInsDao;
  // 检索式表
  @Autowired
  private SieInsAliasDao sieInsAliasDao;
  @Autowired
  private SieMergeInsBakDao sieMergeInsBakDao;
  // 别名表
  @Autowired
  private SiePdwhInsAddrConstDao siePdwhInsAddrConstDao;
  @Autowired
  private SiePdwhInsAddrConstRefreshDao siePdwhInsAddrConstRefreshDao;
  // 机构表
  @Autowired
  private Sie6InsPortalDao sieInsPortalDao;
  @Autowired
  private SieInsRegionDao sieInsRegionDao;
  /*
   * @Autowired private SieInsUnitAdminDao sieInsUnitAdminDao;
   */
  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;
  // 文件
  @Autowired
  private SieArchiveFileDao sieArchiveFileDao;
  @Autowired
  private ImportFileLogDao sieImportFileLogDao;
  @Autowired
  private SieInsGuidDao sieInsGuidDao;
  @Autowired
  private SieInsRefDbDao sieInsRefDbDao;
  // 备份表
  @Autowired
  private SieMergeInstitutionBakDao sieMergeInstitutionBakDao;
  @Autowired
  private SieMergeInsPortalBakDao sieMergeInsPortalBakDao;
  @Autowired
  private SieMergeInsAliasBakDao sieMergeInsAliasBakDao;
  @Autowired
  private SieMergePdwhInsAddrConstBakDao sieMergePdwhInsAddrConstBakDao;
  @Autowired
  private PublicationXmlManager siePublicationXmlManager;
  @Autowired
  private SieProjectXmlManager sieProjectXmlManager;

  final private Integer pubType = 1; // 成果
  final private Integer patType = 2; // 专利
  final private Integer prjType = 3; // 项目
  final private Integer unitType = 4; // 部门
  final private Integer personType = 5; // 人员
  final private Integer prdType = 6; // 产品

  /**
   * 合并单位人员、部门、项目、成果、专利、别名、检索式、机构、产品
   */
  @Override
  public void mergeInsMainMethod(SieMergeInsReflush sieMergeInsReflush) {
    Long mainInsId = sieMergeInsReflush.getMainInsId();
    // 合并的机构
    long[] mergeInsIds = new long[] {sieMergeInsReflush.getMergeInsId()};
    // 业务合并，XML中节点的INSID暂不处理
    mergeInsProject(mainInsId, mergeInsIds);
    mergeInsPat(mainInsId, mergeInsIds);
    mergeInsPub(mainInsId, mergeInsIds);
    mergeInsUnit(mainInsId, mergeInsIds);
    mergeInsPerson(mainInsId, mergeInsIds);
    mergeInsAlias(mainInsId, mergeInsIds);
    mergeInsAddConst(mainInsId, mergeInsIds);
    mergeInstitution(mainInsId, mergeInsIds);
    mergeArchiveFiles(mainInsId, mergeInsIds);
    mergeProduct(mainInsId, mergeInsIds);
  }

  /**
   * 合并成果
   * 
   * @param mergeInsIds 被合并的机构单位的数组
   * @param mainInsid 主体insID
   */
  private boolean mergeInsPub(Long mainInsid, long[] mergeInsIds) {
    boolean status = false;
    for (Long mergeid : mergeInsIds) {
      // 转换PUBLICATION
      for (SiePublication siePublication : siePublicationDao.getListByInsId(mergeid)) {
        boolean flag = false;
        try {
          // 查重
          flag = siePublicationXmlManager.getRepeatPubStatus(siePublication.getPubId(), siePublication.getPubType(),
              mainInsid);
        } catch (Exception e) {
          e.printStackTrace();
        }
        // 根据状态判断是否要插入数据，true则插入
        if (flag) {
          long thisPubId = siePublication.getPubId();
          recordMergeInfoLog(thisPubId, mergeid, pubType);
          siePublication.setInsId(mainInsid);
          siePublicationDao.save(siePublication);
          // 只转换不重复的pub数据PUB_MEMBER
          List<SiePubMember> listSiePubMember = siePubMemberDao.getMembersByPubId(thisPubId);
          for (SiePubMember siePatMember : listSiePubMember) {
            siePatMember.setInsId(mainInsid);
            siePubMemberDao.save(siePatMember);
          }
          // 只转换不重复的pub数据PUB_DUP_FIELDS
          SiePubDupFields siePubDupFields = siePubDupFieldsDao.getPubDupFields(thisPubId);
          if (siePubDupFields != null) {
            siePubDupFields.setOwnerId(mainInsid);
            siePubDupFieldsDao.save(siePubDupFields);
            // 只转换不重复的pub数据PUB_KEYWORDS
            for (PubKeyWords siePubKeyWords : siePubKeyWordsDao.getPubKeyWords(thisPubId)) {
              siePubKeyWords.setInsId(mainInsid);
              siePubKeyWordsDao.save(siePubKeyWords);
            }
          }
        } else {
          siePublication.setStatus("1");
          siePublicationDao.save(siePublication);
        }
      }

    }
    return status;
  }

  /**
   * 合并项目
   * 
   * @param mergeInsIds
   * @param mainInsId
   */
  private boolean mergeInsProject(Long mainInsId, long[] mergeInsIds) {
    boolean status = false;
    for (Long mergeid : mergeInsIds) {
      List<SieProject> Projectes = (List<SieProject>) sieProjectDao.getListByInsId(mergeid);
      for (SieProject sieProject : Projectes) {
        boolean flag = false;
        try {
          flag = sieProjectXmlManager.getRepeatPrjStatus(sieProject.getId(), mergeid);
        } catch (SysServiceException | DocumentException e) {
          e.printStackTrace();
          logger.error("单位合并任务，判断项目重复异常，项目prj_id:" + sieProject.getId());
        }
        if (flag) {
          long thisPrjId = sieProject.getId();
          recordMergeInfoLog(thisPrjId, mergeid, prjType);
          sieProject.setInsId(mainInsId);
          sieProjectDao.save(sieProject);
          for (SiePrjMember siePrjMember : siePrjMemberDao.getMembersByPrjId(thisPrjId)) {
            siePrjMember.setInsId(mainInsId);
            siePrjMemberDao.save(siePrjMember);
          }
          SiePrjDupFields siePrjDupFields = siePrjDupFieldsDao.getByPrjId(thisPrjId);
          if (siePrjDupFields != null) {
            siePrjDupFields.setOwnerInsId(mainInsId);
            siePrjDupFieldsDao.save(siePrjDupFields);
            for (SiePrjKeword siePrjKeyWords : siePrjKewordDao.getPrjKeyWords(thisPrjId)) {
              siePrjKeyWords.setInsId(mainInsId);
              siePrjKewordDao.save(siePrjKeyWords);
            }
          }
        } else {
          sieProject.setStatus(1);
          sieProject.setInsId(null);
          sieProjectDao.save(sieProject);
        }
      }
    }
    return status;
  }

  /**
   * 合并单位专利
   * 
   * @param mergeInsIds
   * @param mainInsId
   */
  private boolean mergeInsPat(Long mainInsId, long[] mergeInsIds) {
    boolean status = false;
    for (Long mergeid : mergeInsIds) {
      for (SiePatent siePatent : siePatentDao.getListByInsId(mergeid)) {
        boolean flag = false;
        try {
          flag = siePublicationXmlManager.getRepeatPubStatus(siePatent.getPatId(), siePatent.getPatType(), mainInsId);
        } catch (Exception e) {
          e.printStackTrace();
        }
        if (flag) {
          long patId = siePatent.getPatId();
          recordMergeInfoLog(patId, mergeid, patType);
          siePatent.setInsId(mainInsId);
          siePatentDao.save(siePatent);
          for (SiePatMember siePatMember : siePatMemberDao.getMembersByPatId(patId)) {
            siePatMember.setInsId(mainInsId);
            siePatMemberDao.save(siePatMember);
          }
          SiePatDupFields siePatDupFields = siePatDupFieldsDao.getPatDupFields(patId);
          siePatDupFields.setOwnerId(mainInsId);
          siePatDupFieldsDao.save(siePatDupFields);
          // 这里可能不稳定
          for (PatKeyWords siePatKeyWords : siePatKeyWordsDao.getPatKeyWords(patId)) {
            siePatKeyWords.setInsId(mainInsId);
            siePatKeyWordsDao.save(siePatKeyWords);
          }
        } else {
          siePatent.setStatus("1");
          siePatentDao.save(siePatent);
        }
      }

    }
    return status;
  }

  /**
   * 合并单位部门
   * 
   * @param mergeInsIds
   * @param mainInsId
   */
  private boolean mergeInsUnit(Long mainInsId, long[] mergeInsIds) {
    boolean status = false;
    for (Long mergeid : mergeInsIds) {
      List<Sie6InsUnit> mainUnits = sie6InsUnitDao.getListByInsId(mainInsId);
      for (Sie6InsUnit mergeUnit : sie6InsUnitDao.getListByInsId(mergeid)) {
        // 部门查重逻辑：比较中英文名字
        boolean lock = true;
        for (Sie6InsUnit tempInsUnit : mainUnits) {
          String tempMainZHUnitName = getTrimString(tempInsUnit.getZhName());
          String tempMergeZHUnitName = getTrimString(mergeUnit.getZhName());
          if (tempMainZHUnitName.equals(tempMergeZHUnitName)) {
            lock = false;
            break;
          }
        }
        // 如果没有重复，则锁是打开状态的。
        if (lock) {
          recordMergeInfoLog(mergeUnit.getId(), mergeid, unitType);
          mergeUnit.setInsId(mainInsId);
          sie6InsUnitDao.save(mergeUnit);
        }
      }
    }
    return status;
  }

  /**
   * 获取trim和toLowerCase的字符串
   */
  private String getTrimString(String Name) {
    String tempStr = "";
    if (Name != null) {
      tempStr = Name.toLowerCase().trim();
    }
    return tempStr;
  }

  /**
   * 合并单位人员：在合并了部门之后，才能进行合并人员。合并人员的时候要比较部门是否发生了变化，若是所在的部门不在新的单位里，需要进行处理。
   * 
   * @param mergeInsIds
   * @param mainInsId
   * @throws CloneNotSupportedException
   */
  private boolean mergeInsPerson(Long mainInsId, long[] mergeInsIds) {
    boolean status = false;
    // 获取处理后的数据，并插入
    for (SieInsPerson sieInsPerson : dealInsPsn(mergeInsIds, mainInsId)) {
      backUpPsnIns(sieInsPerson);
      Long insId = sieInsPerson.getPk().getInsId();
      Long psnId = sieInsPerson.getPk().getPsnId();
      SieInsPerson newSieInsPerson = new SieInsPerson(psnId, mainInsId);
      try {
        copyPerson(newSieInsPerson, sieInsPerson);
        if (newSieInsPerson != null) {
          recordMergeInfoLog(psnId, insId, personType);
          sieInsPersonDao.save(newSieInsPerson);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return status;
  }

  /**
   * 获取需要合并的人员信息，会进行人员所在部门的匹配。合并人员根据psnid进行匹配。
   * 
   * @param mergeInsIds 被合并的机构
   * @param mainInsId 主体机构
   */
  private List<SieInsPerson> dealInsPsn(long[] mergeInsIds, Long mainInsId) {
    List<SieInsPerson> firstList = new ArrayList<SieInsPerson>();
    List<SieInsPerson> twoList = new ArrayList<SieInsPerson>();
    // 逻辑是：把多条机构的人员信息一起拿过来，然后进行集中处理。
    for (Long mergeid : mergeInsIds) {
      firstList.addAll(sieInsPersonDao.getListByInsId(mergeid));
    }
    // 清洗现有人员的数据（去重）
    List<SieInsPerson> mainPsnList = sieInsPersonDao.getListByInsId(mainInsId);
    for (SieInsPerson tempPerson : dealInsPsn(firstList)) {
      boolean switchs = true;
      // 判断人员是否已经在主体机构之中
      Long tempMergePsnId = tempPerson.getPk().getPsnId();
      for (SieInsPerson tempMainPerson : mainPsnList) {
        Long tempMainPsnid = tempMainPerson.getPk().getPsnId();
        if (tempMergePsnId.equals(tempMainPsnid)) {
          switchs = false;
          break;
        }
      }
      // 若是人们在新的单位的话，就不会合并，否则走下面的逻辑
      if (switchs) {
        // 判断部门是否存在新单位中，若是存在则不用处理，若是不存在则需要更新部门
        boolean lock = false;
        List<Sie6InsUnit> tempMainInsUnits = sie6InsUnitDao.getListByInsId(mainInsId);
        for (Sie6InsUnit sie6InsUnit : tempMainInsUnits) {
          if (sie6InsUnit.getId().equals(tempPerson.getUnitId())) {
            lock = true;
            break;
          }
        }
        // 如果有锁(unitID没匹配上,人员部门名称不为空)，就需要匹配新的部门名称
        String tempPsnUnitName = tempPerson.getUnitName() == null ? "" : tempPerson.getUnitName().trim();
        if (!lock && StringUtils.isNotEmpty(tempPsnUnitName)) {
          for (Sie6InsUnit sie6InsUnit : tempMainInsUnits) {
            // 如果能找到新的单位中匹配名字的单位
            if (tempPsnUnitName.equals(sie6InsUnit.getZhName().trim())) {
              tempPerson.setUnitId(sie6InsUnit.getId());
              lock = true;
              break;
            }
          }
        }
        twoList.add(tempPerson);
      }
    }

    return twoList;
  }

  /**
   * 处理重复的人员数据(废弃)
   */
  private List<SieInsPerson> dealInsPsn(List<SieInsPerson> listByInsId) {
    List<SieInsPerson> newListSieInsPerson = new ArrayList<SieInsPerson>();
    for (SieInsPerson oldInsPerson : listByInsId) {
      boolean switchs = true;
      for (SieInsPerson tempInsPerson : newListSieInsPerson) {
        Long tempStr1 = tempInsPerson.getPk().getPsnId();
        Long tempStr2 = oldInsPerson.getPk().getPsnId();
        if (tempStr2.equals(tempStr1)) {
          switchs = false;
          break;
        }
      }
      if (switchs) {
        newListSieInsPerson.add(oldInsPerson);
      }
    }
    return newListSieInsPerson;
  }

  /**
   * 合并单位检索式（ins_alias）
   * 
   * @param mergeInsIds
   * @param mainInsId
   */
  private boolean mergeInsAlias(Long mainInsId, long[] mergeInsIds) {
    boolean status = false;
    List<SieInsAlias> sieMainInsAliasList = sieInsAliasDao.getListByInsId(mainInsId);
    // 获取需要合并的ins
    for (Long mergeid : mergeInsIds) {
      // 获取需要合并的单位检索式
      List<SieInsAlias> sieMergeInsAliasList = sieInsAliasDao.getListByInsId(mergeid);
      for (SieInsAlias sieInsAlias : sieMergeInsAliasList) {
        // 备份数据
        copySieInsAlias(sieInsAlias);
        boolean lock = true;
        // 遍历主表比较字段
        for (SieInsAlias tempAlia : sieMainInsAliasList) {
          Long tempName = tempAlia.getInsAliasId().getDbId(); // 主表对象的名字
          Long tempName2 = sieInsAlias.getInsAliasId().getDbId(); // 对比表的名字
          // 如果存在相等的字段，则把锁关闭
          if (tempName.equals(tempName2)) {
            lock = false;
            break;
          }
        }
        // 如果锁能打开，就把数据插入表中
        if (lock) {
          // 记录操作了的数据
          SieInsAlias newTempInsAlias =
              new SieInsAlias(mainInsId, sieInsAlias.getInsAliasId().getDbId(), sieInsAlias.getName());
          newTempInsAlias.setDbName(sieInsAlias.getDbName());
          sieInsAliasDao.save(newTempInsAlias);
        }
      }
    }
    return status;
  }

  /**
   * 合并单位别名（pdwh_ins_addr_const）
   * 
   * @param mergeInsIds
   * @param mainInsId
   */
  private boolean mergeInsAddConst(Long mainInsId, long[] mergeInsIds) {
    boolean status = false;
    for (Long mergeid : mergeInsIds) {
      // 获取被合并的单位的别名
      for (SiePdwhInsAddrConst sieConst : siePdwhInsAddrConstDao.getListByInsId(mergeid)) {
        copySiePdwhInsAddrConst(sieConst);
        boolean lock = true;
        Long tempMergeHash = sieConst.getInsNameHash(); // 对比表的名字
        // 遍历比较是否存在相等的字段，若是存在则把锁关闭
        for (SiePdwhInsAddrConst sieMainConst : siePdwhInsAddrConstDao.getListByInsId(mainInsId)) {
          Long tempMainHash = sieMainConst.getInsNameHash(); // 主表对象的名字
          if (tempMainHash.equals(tempMergeHash)) {
            lock = false;
            break;
          }
        }
        if (lock) {
          sieConst.setInsId(mainInsId);
          siePdwhInsAddrConstDao.save(sieConst);
        }
      }
      // 更新 PDWH_INS_ADDR_CONST_REFRESH
      /*
       * String refreshSql = " update PDWH_INS_ADDR_CONST_REFRESH  set ins_id=? where ins_id=?";
       * siePdwhInsAddrConstRefreshDao.update(refreshSql, new Object[] { mainInsId, mergeid });
       */
    }
    return status;
  }

  /**
   * 合并单位信息（INS_PORTAL表，INS_REGION表）
   */
  private void mergeInstitution(Long mainInsId, long[] mergeInsIds) {
    for (Long mergeid : mergeInsIds) {
      // 域名
      for (Sie6InsPortal sieInsPortal : sieInsPortalDao.getListByInsId(mergeid)) {
        SieMergeInsPortalBak sieMergeInsPortalBak = new SieMergeInsPortalBak();
        sieMergeInsPortalBak.setDefaultLang(sieInsPortal.getDefaultLang());
        sieMergeInsPortalBak.setDomain(sieInsPortal.getDomain());
        sieMergeInsPortalBak.setEnTitle(sieInsPortal.getEnTitle());
        sieMergeInsPortalBak.setInitTitle(sieInsPortal.getInitTitle());
        sieMergeInsPortalBak.setInsId(sieInsPortal.getInsId());
        sieMergeInsPortalBak.setLogo(sieInsPortal.getLogo());
        sieMergeInsPortalBak.setSwitchLang(sieInsPortal.getSwitchLang());
        sieMergeInsPortalBak.setZhTitle(sieInsPortal.getZhTitle());
        sieMergeInsPortalBakDao.save(sieMergeInsPortalBak);
      }
      // 机构表
      for (Sie6Institution sie6Institution : sie6InstitutionDao.getListByInsId(mergeid)) {
        SieMergeInstitutionBak sieMergeInstitutionBak = new SieMergeInstitutionBak();
        sieMergeInstitutionBak.setAbbr(sie6Institution.getAbbr());
        sieMergeInstitutionBak.setCheckEmails(sie6Institution.getCheckEmails());
        sieMergeInstitutionBak.setContactEmail(sie6Institution.getContactEmail());
        sieMergeInstitutionBak.setContactPerson(sie6Institution.getContactPerson());
        sieMergeInstitutionBak.setDataFrom(sie6Institution.getDataFrom());
        sieMergeInstitutionBak.setEnAddress(sie6Institution.getEnAddress());
        sieMergeInstitutionBak.setEnName(sie6Institution.getEnName());
        sieMergeInstitutionBak.setFox(sie6Institution.getFox());
        sieMergeInstitutionBak.setId(sie6Institution.getId());
        sieMergeInstitutionBak.setMobile(sie6Institution.getMobile());
        sieMergeInstitutionBak.setNature(sie6Institution.getNature());
        sieMergeInstitutionBak.setPostcode(sie6Institution.getPostcode());
        sieMergeInstitutionBak.setServerEmail(sie6Institution.getServerEmail());
        sieMergeInstitutionBak.setServerTel(sie6Institution.getServerTel());
        sieMergeInstitutionBak.setStatus(sie6Institution.getStatus());
        sieMergeInstitutionBak.setTel(sie6Institution.getTel());
        sieMergeInstitutionBak.setUniformId1(sie6Institution.getUniformId1());
        sieMergeInstitutionBak.setUniformId2(sie6Institution.getUniformId2());
        sieMergeInstitutionBak.setUrl(sie6Institution.getUrl());
        sieMergeInstitutionBak.setZhAddress(sie6Institution.getZhAddress());
        sieMergeInstitutionBak.setZhName(sie6Institution.getZhName());
        // 增加create_date和update_date的字段数据同步
        sieMergeInstitutionBak.setCreateDate(sie6Institution.getCreateDate());
        sieMergeInstitutionBak.setUpdateDate(sie6Institution.getUpdateDate());
        sieMergeInstitutionBakDao.save(sieMergeInstitutionBak);
      }
      // 机构_数据库关系表
      List<Long> mergeIdList = new ArrayList<Long>();
      mergeIdList.add(mergeid);
      List<Long> mainIdList = new ArrayList<Long>();
      mainIdList.add(mainInsId);
      try {
        for (SieInsRefDb mergeRefDb : sieInsRefDbDao.getDbByIns(mergeIdList)) {
          Long mergeDbId = mergeRefDb.getInsRefDbId().getDbId();
          boolean flag = true;
          for (SieInsRefDb mainRefDb : sieInsRefDbDao.getDbByIns(mainIdList)) {
            if (mergeDbId.equals(mainRefDb.getInsRefDbId().getDbId())) {
              flag = false;
            }
          }
          if (flag) {
            // 插入新的数据
            SieInsRefDb newMainSieInsRefDb = new SieInsRefDb();
            SieInsRefDbId sieInsRefDbId = new SieInsRefDbId(mainInsId, mergeDbId);
            newMainSieInsRefDb.setInsRefDbId(sieInsRefDbId);
            newMainSieInsRefDb.setActionUrl(mergeRefDb.getActionUrl());
            newMainSieInsRefDb.setActionUrlInside(mergeRefDb.getActionUrlInside());
            newMainSieInsRefDb.setEnabled(mergeRefDb.getEnabled());
            newMainSieInsRefDb.setSeqNo(mergeRefDb.getSeqNo());
            newMainSieInsRefDb.setFulltextUrl(mergeRefDb.getFulltextUrl());
            newMainSieInsRefDb.setFulltextUrlInside(mergeRefDb.getFulltextUrlInside());
            newMainSieInsRefDb.setLoginUrl(mergeRefDb.getLoginUrl());
            newMainSieInsRefDb.setLoginUrlInside(mergeRefDb.getLoginUrlInside());
            sieInsRefDbDao.save(newMainSieInsRefDb);
          }
        }
      } catch (SysServiceException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 合并产品，合并产品需要在合并人员之后，因为需要根据psn_id来更新unit_id
   * 
   * @param mainInsId
   * @param mergeInsIds
   */
  private boolean mergeProduct(Long mainInsId, long[] mergeInsIds) {
    boolean status = false;
    // 产品没有查重表，产品直接归并到合并单位
    for (Long mergeInsId : mergeInsIds) {
      for (Sie6Product prd : sie6ProductDao.getListByInsId(mergeInsId)) {
        // 更新机构ID
        prd.setInsId(mainInsId);
        // 根据psn_id所在单位更新unit_id
        SieInsPersonPk pk = new SieInsPersonPk();
        pk.setInsId(mainInsId);
        pk.setPsnId(prd.getPsnId());
        SieInsPerson person = sieInsPersonDao.get(pk);
        prd.setPsnId(person == null ? null : person.getPk().getPsnId());
        prd.setUnitId(person == null ? null : person.getUnitId());
        // 更新产品信息
        sie6ProductDao.save(prd);
        // 合并数据的主键记录
        recordMergeInfoLog(prd.getPdId(), mergeInsId, prdType);
      }
    }
    status = true;
    return status;
  }

  /**
   * 设置表的状态
   */
  @Override
  public void finishDoneMethod(SieMergeInsReflush sieMergeInsReflush, Integer status) {
    sieMergeInsReflush.setMergeTime(getToday());
    sieMergeInsReflush.setStatus(status);
    sieMergeInsReflushDao.save(sieMergeInsReflush);
  }

  @Override
  public List<SieMergeInsReflush> getNeedRefreshData(int maxSize) {
    try {
      return this.sieMergeInsReflushDao.queryNeedRefresh(maxSize);
    } catch (Exception e) {
      logger.error("获取需要合并的单位数据失败", e);
      throw new ServiceException("获取需要合并的单位数据失败", e);
    }
  }

  // 以下是工具类

  /**
   * SIE复制人员信息
   * 
   * @serialData 2018/10/31
   */
  private void copyPerson(SieInsPerson newSieInsPerson, SieInsPerson sieInsPerson) {
    newSieInsPerson.setUnitId(sieInsPerson.getUnitId());
    newSieInsPerson.setSuperUnitId(sieInsPerson.getSuperUnitId());
    newSieInsPerson.setDuty(sieInsPerson.getDuty());
    newSieInsPerson.setTitle(sieInsPerson.getTitle());
    newSieInsPerson.setEmail(sieInsPerson.getEmail());
    newSieInsPerson.setCreateDate(sieInsPerson.getCreateDate());
    newSieInsPerson.setUpdateDate(sieInsPerson.getUpdateDate());
    newSieInsPerson.setStatus(sieInsPerson.getStatus());
    newSieInsPerson.setFirstName(sieInsPerson.getFirstName());
    newSieInsPerson.setLastName(sieInsPerson.getLastName());
    newSieInsPerson.setZhName(sieInsPerson.getZhName());
    newSieInsPerson.setEnName(sieInsPerson.getEnName());
    newSieInsPerson.setPosition(sieInsPerson.getPosition());
    newSieInsPerson.setPosId(sieInsPerson.getPosId());
    newSieInsPerson.setPosGrades(sieInsPerson.getPosGrades());
    newSieInsPerson.setTel(sieInsPerson.getTel());
    newSieInsPerson.setMobile(sieInsPerson.getMobile());
    newSieInsPerson.setAvatars(sieInsPerson.getAvatars());
    newSieInsPerson.setSex(sieInsPerson.getSex());
    newSieInsPerson.setIsPublic(sieInsPerson.getIsPublic());
    newSieInsPerson.setRemark(sieInsPerson.getRemark());
    newSieInsPerson.setUnitName(sieInsPerson.getUnitName());
    newSieInsPerson.setRegionId(sieInsPerson.getRegionId());
    newSieInsPerson.setIdcard(sieInsPerson.getIdcard());
    newSieInsPerson.setHighestEdu(sieInsPerson.getHighestEdu());
  }

  /**
   * 备份数据
   */
  private void copySiePdwhInsAddrConst(SiePdwhInsAddrConst sieConst) {
    SieMergePdwhInsAddrConstBak sieTemp = new SieMergePdwhInsAddrConstBak();
    sieTemp.setAddrStatus(sieConst.getAddrStatus());
    sieTemp.setCity(sieConst.getCity());
    sieTemp.setConstId(sieConst.getConstId());
    sieTemp.setConstStatus(sieConst.getConstStatus());
    sieTemp.setCountry(sieConst.getCountry());
    sieTemp.setEnabled(sieConst.getEnabled());
    sieTemp.setFullAddr(sieConst.getFullAddr());
    sieTemp.setInsId(sieConst.getInsId());
    sieTemp.setInsName(sieConst.getInsName());
    sieTemp.setInsNameHash(sieConst.getInsNameHash());
    sieTemp.setLanguage(sieConst.getLanguage());
    sieTemp.setLastOperator(sieConst.getLastOperator());
    sieTemp.setProvince(sieConst.getProvince());
    sieTemp.setUpdateTime(sieConst.getUpdateTime());
    sieMergePdwhInsAddrConstBakDao.save(sieTemp);
  }

  /**
   * 备份数据
   */
  private void copySieInsAlias(SieInsAlias sieInsAlias) {
    SieMergeInsAliasBak sieMergeInsAliasBak = new SieMergeInsAliasBak(sieInsAlias.getInsAliasId().getInsId(),
        sieInsAlias.getInsAliasId().getDbId(), sieInsAlias.getName());
    sieMergeInsAliasBakDao.save(sieMergeInsAliasBak);
  }

  /**
   * 备份人员数据
   */
  private void backUpPsnIns(SieInsPerson sieInsPerson) {
    SieInsPersonPk sieInsPersonPk = sieInsPerson.getPk();
    if (sieMergeBackPsnInsDao.get(sieInsPersonPk) == null) {
      SieMergeBackPsnIns sieMergeBackPsnIns =
          new SieMergeBackPsnIns(sieInsPersonPk.getPsnId(), sieInsPersonPk.getInsId());
      sieMergeBackPsnIns.setUnitId(sieInsPerson.getUnitId());
      sieMergeBackPsnIns.setSuperUnitId(sieInsPerson.getSuperUnitId());
      sieMergeBackPsnIns.setDuty(sieInsPerson.getDuty());
      sieMergeBackPsnIns.setTitle(sieInsPerson.getTitle());
      sieMergeBackPsnIns.setEmail(sieInsPerson.getEmail());
      sieMergeBackPsnIns.setCreateDate(sieInsPerson.getCreateDate());
      sieMergeBackPsnIns.setUpdateDate(sieInsPerson.getUpdateDate());
      sieMergeBackPsnIns.setStatus(sieInsPerson.getStatus());
      sieMergeBackPsnIns.setFirstName(sieInsPerson.getFirstName());
      sieMergeBackPsnIns.setLastName(sieInsPerson.getLastName());
      sieMergeBackPsnIns.setZhName(sieInsPerson.getZhName());
      sieMergeBackPsnIns.setEnName(sieInsPerson.getEnName());
      sieMergeBackPsnIns.setPosition(sieInsPerson.getPosition());
      sieMergeBackPsnIns.setPosId(sieInsPerson.getPosId());
      sieMergeBackPsnIns.setPosGrades(sieInsPerson.getPosGrades());
      sieMergeBackPsnIns.setTel(sieInsPerson.getTel());
      sieMergeBackPsnIns.setMobile(sieInsPerson.getMobile());
      sieMergeBackPsnIns.setAvatars(sieInsPerson.getAvatars());
      sieMergeBackPsnIns.setSex(sieInsPerson.getSex());
      sieMergeBackPsnIns.setIsPublic(sieInsPerson.getIsPublic());
      sieMergeBackPsnIns.setRemark(sieInsPerson.getRemark());
      sieMergeBackPsnIns.setUnitName(sieInsPerson.getUnitName());
      sieMergeBackPsnIns.setRegionId(sieInsPerson.getRegionId());
      sieMergeBackPsnIns.setIdcard(sieInsPerson.getIdcard());
      sieMergeBackPsnIns.setHighestEdu(sieInsPerson.getHighestEdu());
      sieMergeBackPsnInsDao.save(sieMergeBackPsnIns);
    }
  }

  // 更新archiveFile表的insid字段
  private void mergeArchiveFiles(Long mainInsId, long[] mergeInsIds) {
    for (long mergeIns : mergeInsIds) {
      // archiveFile表
      Object[] insids = new Object[] {mainInsId, mergeIns};
      String sieArchiveFileSql = "update ARCHIVE_FILES set file_ins_id=? where file_ins_id=? ";
      sieArchiveFileDao.update(sieArchiveFileSql, insids);
      // IMPORT_FILE_LOG表
      String sieImportFileLogDaoSql = "update IMPORT_FILE_LOG set ins_id=? where ins_id=? ";
      sieImportFileLogDao.update(sieImportFileLogDaoSql, insids);
    }
  }

  /**
   * 删除各张表中的数据
   */
  @Override
  public void deleteOriginalData(long mergeid) {
    // 删除人员
    sieInsPersonDao.deleteByInsId(mergeid);
    // 删除机构别名
    sieInsAliasDao.deleteByInsId(mergeid);
    // 删除冗余的表数据信息
    siePdwhInsAddrConstRefreshDao.deleteByInsId(mergeid);
    // 删除机构域名信息
    sieInsPortalDao.deleteByInsId(mergeid);
    // 删除机构常量表中相关合并内容
    sieInsRegionDao.deleteByInsId(mergeid);
    // 删除机构管理员
    /* sieInsUnitAdminDao.deleteByInsId(mergeid); */
    // 删除机构本身
    sie6InstitutionDao.deleteByInsId(mergeid);
    // 删除单位的guid对照表
    sieInsGuidDao.deleteByInsId(mergeid);
    // 删除机构/数据库关系表
    sieInsRefDbDao.deleteByInsId(mergeid);
  }

  /**
   * 记录合并日志信息
   * 
   * @param mainInsid 主单位ID
   * @param mergeid 合并单位ID
   * @param typeid 业务主键ID
   * @param type 业务类型
   * @param dbTableName 数据库表名
   */
  private void recordMergeInfoLog(Long businessId, Long insId, Integer typeId) {
    SieMergeInsBak sieMergeInsBak = new SieMergeInsBak(businessId, insId, typeId);
    sieMergeInsBakDao.save(sieMergeInsBak);
  }

  private Date getToday() {
    Calendar cal = Calendar.getInstance();
    Date today = cal.getTime();
    return today;
  }

}
