package com.smate.center.open.service.publication;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.publication.PubMemberDao;
import com.smate.center.open.model.publication.PubMember;

@Service("pubMemberService")
@Transactional(rollbackFor = Exception.class)
public class PubMemberServiceImpl implements PubMemberService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubMemberDao pubMemberDao;

  @Override
  public List<PubMember> getPubMemberList(Long pubId) throws Exception {
    try {
      return this.pubMemberDao.getPubMemberList(pubId);
    } catch (Exception e) {
      logger.error(String.format("获取成果pubId:{}作者姓名出现异常：", pubId), e);
      throw new Exception("获取成果pubId:{}作者姓名出现异常", e);
    }
  }

}
