package com.smate.center.task.v8pub.service;

import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.dao.pdwh.PdwhDataTaskDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

@Service("pdwhAuthorNamesFormatService")
@Transactional(rollbackFor = Exception.class)
public class PdwhAuthorNamesFormatServiceImpl implements PdwhAuthorNamesFormatService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PdwhDataTaskDAO pdwhDataTaskDAO;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;

  @Override
  public List<PdwhDataTaskPO> findPdwhNeedDeal(Long startId, Long endId, Integer size) throws ServiceException {
    return pdwhDataTaskDAO.findPdwhPubId(startId, endId, size);
  }

  @Override
  public void save(PdwhDataTaskPO pubData) throws ServiceException {
    pdwhDataTaskDAO.save(pubData);
  }

  @Override
  public void formatAuthorNames(PdwhDataTaskPO pubData) throws ServiceException {
    try {
      PubPdwhPO pubPdwhPO = pubPdwhDAO.get(pubData.getPubId());
      PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailDAO.findById(pubData.getPubId());
      if (pubPdwhPO != null && pubPdwhDetailDOM != null) {
        String authorNames = pubPdwhPO.getAuthorNames();
        // 1.处理作者中的" and "
        authorNames = cleanAND(authorNames);
        // 2.去除作者中的 [] 以及[]内的内容信息
        authorNames = cleanSquareBrackets(authorNames);
        // 3.去除作者中的() 以及()内的内容信息
        authorNames = cleanCircleBrackets(authorNames);

        // 保存在主表中
        pubPdwhPO.setAuthorNames(authorNames);
        pubPdwhDAO.save(pubPdwhPO);

        // 保存在mongodb中
        pubPdwhDetailDOM.setAuthorNames(authorNames);
        pubPdwhDetailDAO.save(pubPdwhDetailDOM);
      } else {
        pubData.setStatus(-1);
        pubData.setError("主表或者mongodb中无数据");
      }
    } catch (Exception e) {
      logger.error("格式化作者名数据出错pubId={}", pubData.getPubId(), e);
    }
  }

  /**
   * 去除() 以及()内的内容信息
   * 
   * @param authorNames
   * @return
   */
  private String cleanCircleBrackets(String authorNames) {
    if (StringUtils.isBlank(authorNames)) {
      return "";
    }
    // 去除括号内，以及括号
    String regex3 = "\\([^\\(\\)]+\\)|\\(\\)";
    authorNames = authorNames.replaceAll(regex3, "");
    return authorNames;
  }


  /**
   * 去除[]以及[]内的所有内容
   * 
   * @param authorNames
   * @return
   */
  private String cleanSquareBrackets(String authorNames) {
    if (StringUtils.isBlank(authorNames)) {
      return "";
    }
    String regex2 = "\\[[^\\[\\]]+\\]|\\[\\]";
    authorNames = authorNames.replaceAll(regex2, "");
    return authorNames;
  }

  /**
   * 处理作者中的" and ",将" and "替换成"; "
   * 
   * @param authorNames
   * @return
   */
  private String cleanAND(String authorNames) {
    if (StringUtils.isBlank(authorNames)) {
      return "";
    }
    authorNames = authorNames.replace(" and ", "; ");
    return authorNames;
  }

}
