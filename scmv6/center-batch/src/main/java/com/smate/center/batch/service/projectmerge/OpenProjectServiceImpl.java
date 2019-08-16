package com.smate.center.batch.service.projectmerge;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.context.OpenProjectContext;
import com.smate.center.batch.dao.rol.ProjectSchemeDao;
import com.smate.center.batch.dao.sns.prj.OpenPrjMemberDao;
import com.smate.center.batch.dao.sns.prj.OpenProjectDao;
import com.smate.center.batch.dao.sns.prj.PrjRelatedPubRefreshDao;
import com.smate.center.batch.dao.sns.wechat.OpenUserUnionDao;
import com.smate.center.batch.model.rol.prj.ProjectScheme;
import com.smate.center.batch.model.sns.prj.OpenPrjMember;
import com.smate.center.batch.model.sns.prj.OpenProject;
import com.smate.center.batch.model.sns.prj.PrjRelatedPubRefresh;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.project.model.PrjMember;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfEasy;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

/**
 * 第三方项目合并任务Service
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @throws Exception
 */
@Service("openProjectService")
// @Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor =
// Exception.class)
@Transactional(rollbackFor = Exception.class)
public class OpenProjectServiceImpl implements OpenProjectService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OpenProjectDao openProjectDao;
  @Autowired
  private ProjectSchemeDao projectSchemeDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private PrjRelatedPubRefreshDao prjRelatedPubRefreshDao;
  @Autowired
  private OpenPrjMemberDao openPrjMemberDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Resource(name = "psnCnfPrjService")
  private PsnCnfEasy prjEasy;
  @Resource(name = "cnfService")
  private PsnCnfEasy psnEasy;

  /**
   * 初始化上下文
   */
  @Override
  public OpenProjectContext init() {
    OpenProjectContext context = new OpenProjectContext();
    Locale locale = LocaleContextHolder.getLocale();
    context.setLocale(locale);
    context.setCurrentLanguage(locale.getLanguage());
    return context;
  }

  /**
   * 查询待合并项目
   */
  @Override
  public List<OpenProject> queryOpenProject(Integer size) {
    List<OpenProject> result = openProjectDao.queryBySize(size);
    return result;
  }

  /**
   * 设置项目合并状态
   */
  @Override
  public void changeStatus(OpenProject project, Integer status) {
    project.setTaskStatus(status);
    openProjectDao.save(project);

  }

  /**
   * 保存项目
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param prj
   */
  public void snsProjectDaoSave(Project prj) {
    projectDao.save(prj);
  }

  @Override
  public void projectSchemeDaoSave(ProjectScheme pojo) {
    projectSchemeDao.save(pojo);

  }

  @Override
  public void prjRelatedPubRefreshDaoSave(PrjRelatedPubRefresh prjRelatedPubRefresh) {
    prjRelatedPubRefreshDao.save(prjRelatedPubRefresh);

  }

  @Override
  public PrjMember snsProjectDaoGetMemberById(Long id) {
    return projectDao.getPrjMemberById(id);

  }

  @Override
  public void snsProjectDaoSavePrjMember(PrjMember pm) {
    projectDao.savePrjMember(pm);

  }

  @Override
  public List<OpenPrjMember> openPrjMemberDaoGetMembersByPrjId(Long id) {
    return openPrjMemberDao.getMembersByPrjId(id);

  }

  @Override
  public OpenUserUnion openUserUnionDaoGetOpenUserUnion(Long openId, String token) throws Exception {
    return openUserUnionDao.getOpenUserUnion(openId, token);
  }

  @Override
  public PsnCnfBase prjEasyGet(PsnCnfBase cnfBase) throws Exception {
    return psnEasy.get(cnfBase);
  }

  @Override
  public void PrjEasySave(PsnCnfBase cnfBase) throws Exception {
    prjEasy.save(cnfBase);

  }

  @Override
  public boolean repeatProject(OpenProject project) throws Exception {
    OpenUserUnion user = this.openUserUnionDaoGetOpenUserUnion(project.getOpenId(), project.getToken());
    return projectDao.queryRepeat(project.getInternalNo(), project.getExternalNo(), project.getAgencyName(),
        project.getEnAgencyName(), project.getZhTitle(), project.getEnTitle(), user.getPsnId());
  }

  @Override
  // @Transactional(propagation=Propagation.NOT_SUPPORTED,rollbackFor =
  // Exception.class)
  public OpenProject queryOpenProject(Long id) {

    return openProjectDao.queryById(id);
  }

}
