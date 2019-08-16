package com.smate.core.base.project.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.project.dao.PrjMemberDao;
import com.smate.core.base.project.model.PrjMember;

/**
 * 项目组成员服务实现类
 * 
 * @author houchuanjie
 * @date 2018年3月22日 下午5:59:30
 */
@Service("prjMemberService")
@Transactional(rollbackOn = Exception.class)
public class PrjMemberServiceImpl implements PrjMemberService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PrjMemberDao prjMemberDao;

  @Override
  public Optional<PrjMember> removeById(Long pmId) {
    return Optional.ofNullable(prjMemberDao.removeById(pmId));
  }

  @Override
  public Optional<PrjMember> getPrjMember(Long pmId) {
    return Optional.ofNullable(prjMemberDao.get(pmId));
  }

  @Override
  public void savePrjMember(PrjMember pm) {
    prjMemberDao.save(pm);
  }

}
