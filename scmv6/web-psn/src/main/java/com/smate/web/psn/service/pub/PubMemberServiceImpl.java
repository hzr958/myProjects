package com.smate.web.psn.service.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.web.psn.dao.pub.PubMemberDao;

@Service("pubMemberService")
@Transactional(rollbackFor = Exception.class)
public class PubMemberServiceImpl implements PubMemberService {

  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubMemberDao pubMemberDao;

  @Override
  public List<PubMemberPO> getPubMemberList(Long pubId) throws Exception {
    try {
      return this.pubMemberDao.getPubMemberList(pubId);
    } catch (Exception e) {
      logger.error(String.format("获取成果pubId:{}作者姓名出现异常：", pubId), e);
      throw new Exception("获取成果pubId:{}作者姓名出现异常", e);
    }
  }

}
