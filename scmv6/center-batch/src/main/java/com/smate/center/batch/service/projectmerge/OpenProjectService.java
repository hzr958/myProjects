package com.smate.center.batch.service.projectmerge;

import java.util.List;

import com.smate.center.batch.context.OpenProjectContext;
import com.smate.center.batch.model.rol.prj.ProjectScheme;
import com.smate.center.batch.model.sns.prj.OpenPrjMember;
import com.smate.center.batch.model.sns.prj.OpenProject;
import com.smate.center.batch.model.sns.prj.PrjRelatedPubRefresh;
import com.smate.core.base.project.model.PrjMember;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

public interface OpenProjectService {

  public OpenProjectContext init();

  public List<OpenProject> queryOpenProject(Integer size);

  public OpenProject queryOpenProject(Long id);

  public void changeStatus(OpenProject project, Integer status);

  public void snsProjectDaoSave(Project prj);

  public void projectSchemeDaoSave(ProjectScheme pojo);

  public void prjRelatedPubRefreshDaoSave(PrjRelatedPubRefresh prjRelatedPubRefresh);

  public PrjMember snsProjectDaoGetMemberById(Long id);

  public void snsProjectDaoSavePrjMember(PrjMember pm);

  public List<OpenPrjMember> openPrjMemberDaoGetMembersByPrjId(Long id);

  public OpenUserUnion openUserUnionDaoGetOpenUserUnion(Long openId, String token) throws Exception;

  public PsnCnfBase prjEasyGet(PsnCnfBase cnfBase) throws Exception;

  public void PrjEasySave(PsnCnfBase cnfBase) throws Exception;

  public boolean repeatProject(OpenProject project) throws Exception;

}
