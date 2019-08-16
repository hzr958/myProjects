package com.smate.web.v8pub.service.sns.group;

import com.smate.web.v8pub.dao.sns.group.GrpKwDiscDAO;
import com.smate.web.v8pub.exception.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 群组关键词服务实现类
 * 
 * @author YJ
 *
 *         2018年8月3日
 */

@Service(value = "grpKwDiscService")
@Transactional(rollbackFor = Exception.class)
public class GrpKwDiscServiceImpl implements GrpKwDiscService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GrpKwDiscDAO grpKwDiscDao;

  @Override
  public List<String> listGrpKeyword(Long grpId) throws ServiceException {
    try {
      String grpkwords = grpKwDiscDao.getGrpKwDisc(grpId);
      if (StringUtils.isNotEmpty(grpkwords)) {
        int startIndex = grpkwords.indexOf(";");
        if (startIndex == 0) {
          // 去掉第一个；分隔符，否则split后会出现一个空的关键字
          grpkwords = StringUtils.substring(grpkwords, startIndex + 1, grpkwords.length() - 1);
        }
        if (grpkwords.split(";") != null) {
          return Arrays.asList(grpkwords.split(";"));
        }
      }
      return null;
    } catch (Exception e) {
      logger.error("群组关键词服务：获取群组关键词失败！ grpId={}", grpId);
      throw new ServiceException(e);
    }

  }

}
