package com.smate.web.group.service.grp.rcmd;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.group.action.grp.form.GrpBaseForm;
import com.smate.web.group.action.grp.form.GrpShowInfo;
import com.smate.web.group.dao.group.OpenGroupUnionDao;
import com.smate.web.group.dao.grp.file.GrpFileDao;
import com.smate.web.group.dao.grp.grpbase.GrpBaseInfoDao;
import com.smate.web.group.dao.grp.grpbase.GrpKwDiscDao;
import com.smate.web.group.dao.grp.grpbase.GrpStatisticsDao;
import com.smate.web.group.dao.grp.member.CategoryMapBaseDao;
import com.smate.web.group.dao.grp.pub.GrpPubsDao;
import com.smate.web.group.dao.grp.rmcd.GrpRcmdDao;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.model.grp.member.CategoryMapBase;
import com.smate.web.group.model.grp.rcmd.GrpRcmd;

/**
 * 群组推荐服务service
 * 
 * @author AiJiangBin
 *
 */
@Service("grpRcmdService")
@Transactional(rollbackFor = Exception.class)
public class GrpRcmdServiceImpl implements GrpRcmdService {

  @Autowired
  private GrpRcmdDao grpRcmdDao;
  @Autowired
  private OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  private GrpStatisticsDao grpStatisticsDao;
  @Autowired
  private GrpKwDiscDao grpKwDiscDao;
  @Autowired
  private GrpFileDao grpFileDao;
  @Autowired
  private GrpPubsDao grpPubsDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;

  @Override
  public void optionRcmdGrp(Long psnId, Long grpId, Integer status) throws Exception {
    GrpRcmd grpRcmd = grpRcmdDao.getGrpRcmd(psnId, grpId);
    if (grpRcmd == null) {
      grpRcmd = new GrpRcmd(psnId, grpId, new Date());
      grpRcmd.setStatus(status.toString());
      grpRcmdDao.save(grpRcmd);
    } else {
      grpRcmdDao.updateOptStatus(psnId, grpId, status);
    }


  }

  @Override
  public void getRcmdGrpList(GrpBaseForm form) throws Exception {
    Integer count = grpRcmdDao.getGrpRcmdList(form, true);// 一定要在v_grp_rcmd表中
    if (count == 0) {// 如果没有群组推荐数据,就在v_grp_baseinfo表里取数据
      grpRcmdDao.getGrpRcmdBaseList(form);
    } else {
      grpRcmdDao.getGrpRcmdList(form, false);
    }
    bildGrpInfo(form);
  }

  /**
   * 构建显示信息
   * 
   * @param form
   */
  private void bildGrpInfo(GrpBaseForm form) {

    if (form.getPage().getResult() != null && form.getPage().getResult().size() > 0) {
      List<GrpShowInfo> grpShowInfoList = new ArrayList<GrpShowInfo>();
      GrpShowInfo grpShowInfo = null;
      for (Object groupId : form.getPage().getResult()) {
        Long grpId = Long.valueOf(groupId.toString());
        GrpBaseinfo g = grpBaseInfoDao.get(grpId);
        grpShowInfo = new GrpShowInfo();
        grpShowInfo.setIsGrpUnion(isUnionGrp(g.getGrpId())); // 0是
        grpShowInfo.setGrpBaseInfo(g);
        grpShowInfo.setDes3GrpId(Des3Utils.encodeToDes3(g.getGrpId().toString()));
        grpShowInfo.setGrpKwDisc(grpKwDiscDao.get(g.getGrpId()));
        boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
        if (grpShowInfo.getGrpKwDisc() != null) {
          if (grpShowInfo.getGrpKwDisc().getFirstCategoryId() != null) {
            CategoryMapBase c1 = categoryMapBaseDao.get(grpShowInfo.getGrpKwDisc().getFirstCategoryId());
            if (c1 != null) {
              grpShowInfo.setFirstDisciplinetName(iszhCN ? c1.getCategoryZh() : c1.getCategoryEn());
            }
          }
          if (grpShowInfo.getGrpKwDisc().getSecondCategoryId() != null) {
            CategoryMapBase c2 = categoryMapBaseDao.get(grpShowInfo.getGrpKwDisc().getSecondCategoryId());
            if (c2 != null) {
              grpShowInfo.setSecondDisciplinetName(iszhCN ? c2.getCategoryZh() : c2.getCategoryEn());
            }
          }
        }
        /*
         * if (grpShowInfo.getGrpKwDisc() != null && StringUtils.isNotBlank
         * (grpShowInfo.getGrpKwDisc().getKeywords())) { String[] str =
         * grpShowInfo.getGrpKwDisc().getKeywords().split(";"); if (str != null && str.length > 0) {
         * List<String> grpKwList = new ArrayList<String>(); for (int i = 0; i < str.length; i++) { if
         * (StringUtils.isNotBlank(str[i])) { grpKwList.add(str[i]); } }
         * grpShowInfo.setGrpKeywordList(grpKwList); } }
         */
        grpShowInfo.setGrpStatistic(grpStatisticsDao.get(g.getGrpId()));
        grpShowInfo.setGrpId(g.getGrpId());
        buildGrpStatistic(grpShowInfo);
        grpShowInfoList.add(grpShowInfo);
      }
      form.setGrpShowInfoList(grpShowInfoList);

    }
  }

  /**
   * 是否关联群组
   * 
   * @return
   */
  private Integer isUnionGrp(Long grpId) {
    return openGroupUnionDao.IsExist(grpId);
  }

  @Override
  public void getRcmdGrpListStatistics(GrpBaseForm form) throws Exception {
    List<Object[]> list = new ArrayList<Object[]>();
    List<Object[]> listDisc = new ArrayList<Object[]>();
    Integer count = grpRcmdDao.getGrpRcmdList(form, true);// 一定要在v_grp_rcmd表中
    if (count == 0) {// 如果没有群组推荐数据，就不加一定要在v_grp_rcmd表中这个条件
      list = grpRcmdDao.getGrpRcmdBaseCategoryStatistic(form);
      listDisc = grpRcmdDao.getGrpRcmdBaseDisciplineStatistic(form);
    } else {
      list = grpRcmdDao.getGrpRcmdCategoryStatistic(form);
      listDisc = grpRcmdDao.getGrpRcmdDisciplineStatistic(form);
    }


    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, String> disciplineCategoryMap = new HashMap<String, String>();
    Map<String, String> grpCategoryMap = new HashMap<String, String>();
    disciplineCategoryMap.put("1", "0");
    disciplineCategoryMap.put("2", "0");
    disciplineCategoryMap.put("3", "0");
    disciplineCategoryMap.put("4", "0");
    disciplineCategoryMap.put("5", "0");
    disciplineCategoryMap.put("6", "0");
    disciplineCategoryMap.put("7", "0");

    grpCategoryMap.put("10", "0");
    grpCategoryMap.put("11", "0");
    grpCategoryMap.put("12", "0");
    if (list != null && list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {
        Object[] obj = list.get(i);
        if (obj[0] == null)
          continue;
        grpCategoryMap.put(obj[0].toString(), obj[1].toString());
      }
    }
    if (listDisc != null && listDisc.size() > 0) {
      for (int i = 0; i < listDisc.size(); i++) {
        Object[] obj = listDisc.get(i);
        if (obj[0] == null)
          continue;
        disciplineCategoryMap.put(obj[0].toString(), obj[1].toString());
      }
    }
    map.put("disciplineCategory", disciplineCategoryMap);
    map.put("grpCategory", grpCategoryMap);
    form.setResult(map);
  }

  /**
   * 构建群组统计数
   * 
   * @param grpShowInfo
   */
  public void buildGrpStatistic(GrpShowInfo grpShowInfo) {
    if (grpShowInfo.getGrpBaseInfo().getGrpCategory() == 10) {
      // 课程群组 <!-- 成员，文献，课件，作业 -->
      Long courseFileSum = grpFileDao.countGrpFile(grpShowInfo.getGrpId(), 2);
      Long workFileSum = grpFileDao.countGrpFile(grpShowInfo.getGrpId(), 1);
      grpShowInfo.setGrpCourseFileSum(courseFileSum);
      grpShowInfo.setGrpWorkFileSum(workFileSum);

    } else if (grpShowInfo.getGrpBaseInfo().getGrpCategory() == 11) {
      // 项目群组<!-- 成员，成果，文献，文件 -->
      Long projectRefSum = grpPubsDao.countGrpPubsSum(grpShowInfo.getGrpId(), 0);
      Long projectPubSum = grpPubsDao.countGrpPubsSum(grpShowInfo.getGrpId(), 1);
      grpShowInfo.setGrpProjectPubSum(projectPubSum);
      grpShowInfo.setGrpProjectRefSum(projectRefSum);
    }

  }

}
