package com.smate.web.v8pub.service.handler.assembly.construct;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.match.PubAuthorMatchService;

/**
 * 成果权限字段的构造 <br/>
 * 原则： <br/>
 * 1.基准库导入至个人库的成果，根据当前人的用户名跟AuthorName进行匹配，匹配上则为公开，匹配不上则为私人 <br/>
 * 2.传入permission <br/>
 * 
 * @author YJ
 *
 *         2018年9月17日
 */
@Transactional(rollbackFor = Exception.class)
public class ASConstructPubPermissionImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAuthorMatchService pubAuthorMatchService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      pub.permission = (pub.permission == null) ? 7 : pub.permission;
      if (pub.pubGenre == PubGenreConstants.GROUP_PUB) {
        return null;
      }
      if (pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FORM_FILE
          || pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FROM_PDWH
          || pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FROM_DATABASE) {
        // 基准库成果导入，文件导入，联邦检索才做这个事情
        // 进行匹配AuthorNames
        Integer match = pubAuthorMatchService.isMatch(pub.authorNames, pub.psnId);
        if (match != null && match == 1) {
          // 公开
          pub.permission = 7;
        } else {
          // 私人
          pub.permission = 4;
        }
      }
    } catch (Exception e) {
      logger.error("构造成果permission字段出错！", e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

}
