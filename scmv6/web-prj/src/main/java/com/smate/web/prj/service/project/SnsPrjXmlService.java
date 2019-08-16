package com.smate.web.prj.service.project;

import java.util.Map;
import java.util.Optional;

import com.smate.core.base.exception.NoPermissionException;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.utils.model.Page;
import com.smate.web.prj.dto.PrjXmlDTO;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.exception.ProjectNotExistException;
import com.smate.web.prj.form.PrjXmlProcessContext;
import com.smate.web.prj.form.ProjectOptForm;
import com.smate.web.prj.xml.PrjXmlDocument;
import com.smate.web.prj.xml.PrjXmlProcessor;

/**
 * 项目XML相关操作的服务类
 * 
 * @author houchuanjie
 * @date 2018年3月15日 下午4:49:42
 */
public interface SnsPrjXmlService {
  /**
   * 保存项目XML.
   * 
   * @param prjId
   * @param xml
   * @return
   * @throws ServiceException
   */
  public void savePrjXml(Long prjId, String xml);

  /**
   * 获取项目XML.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Optional<PrjXmlDTO> getPrjXmlByPrjId(Long prjId) throws ServiceException;

  /**
   * 加载项目XML字符串到{@code prjOptForm.prjXml}属性，会进行一些格式化操作，刷新权限信息。
   *
   * @author houchuanjie
   * @date 2018年3月19日 下午4:03:48
   * @param prjOptForm
   * @return
   * @throws ServiceException
   * @throws ProjectNotFoundException
   * @throws ProjectNotExistException
   */
  ProjectOptForm loadPrjXml(ProjectOptForm prjOptForm)
      throws ServiceException, NoPermissionException, ProjectNotExistException;

  /**
   * 创建项目XML
   * 
   * @author houchuanjie
   * @date 2018年3月21日 下午5:25:48
   * @param paramsMap 客户端发送的参数集合
   */
  public PrjXmlDocument createPrjXml(Map<String, Object> paramsMap, String prjId);

  /**
   * 更新项目XML，返回同步更新后的project实体
   *
   * @author houchuanjie
   * @date 2018年3月21日 下午6:48:47
   * @param project
   * @param paramsMap
   * @return
   * @throws ProjectNotExistException
   * @throws ServiceException
   * @throws NoPermissionException
   */
  PrjXmlDocument syncUpdatePrjXml(Project project, Map<String, Object> paramsMap)
      throws ProjectNotExistException, ServiceException, NoPermissionException;

  public Long addPrjXml(Long prjId, Map paramsMap)
      throws ProjectNotExistException, ServiceException, NoPermissionException;

  Page getPsnFileListInGroup(Page<StationFile> page) throws Exception;

  /**
   * .构建xml处理上下文
   * 
   * @param psnId
   * @return
   * @throws PrjException
   */
  PrjXmlProcessContext buildXmlProcessContext(Long psnId) throws PrjException;

  /**
   * .处理项目XML及更新相关表
   * 
   * @param context
   * @param xmlDoc
   * @param prjXmlProcessor
   */
  void dealWithXmlByProcessor(PrjXmlProcessContext context, PrjXmlDocument xmlDoc, PrjXmlProcessor prjXmlProcessor)
      throws Exception;

}
