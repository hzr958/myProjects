package com.smate.center.batch.service.rol.pub;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.ConstOATypeDao;
import com.smate.center.batch.dao.rol.pub.JnlOATypeDao;
import com.smate.center.batch.dao.rol.pub.JnlOATypeRefreshDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.ConstOAType;
import com.smate.center.batch.model.rol.pub.JnlOAType;
import com.smate.center.batch.model.rol.pub.JnlOATypeRefresh;
import com.smate.center.batch.service.pdwh.pub.BaseJournalService;

/**
 * 期刊-开放存储类型刷新SERVICE实现.
 * 
 * @author xys
 * 
 */
@Service("jnlOATypeRefreshService")
@Transactional(rollbackFor = Exception.class)
public class JnlOATypeRefreshServiceImpl implements JnlOATypeRefreshService {

  /**
   * 
   */
  private static final long serialVersionUID = 9096990544151512916L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private JnlOATypeRefreshDao jnlOATypeRefreshDao;
  @Autowired
  private JnlOATypeDao jnlOATypeDao;
  @Autowired
  private ConstOATypeDao constOATypeDao;
  @Autowired
  private BaseJournalService baseJournalService;

  @Override
  public List<JnlOATypeRefresh> getJnlOATypeNeedRefreshBatch(int maxSize) throws ServiceException {
    return this.jnlOATypeRefreshDao.getJnlOATypeNeedRefreshBatch(maxSize);
  }

  @Override
  public void updateJnlOAType(JnlOATypeRefresh jnlOATypeRefresh) throws ServiceException {
    Long jid = jnlOATypeRefresh.getJid();
    try {
      String romeoColour = this.baseJournalService.getRomeoColourByJid(jid);
      JnlOAType jnlOAType = this.jnlOATypeDao.get(jid);
      if (StringUtils.isBlank(romeoColour)) {
        if (jnlOAType != null) {// 删除期刊-开放存储类型
          this.jnlOATypeDao.delete(jnlOAType);
        }
        return;
      }
      // 保存期刊-开放存储类型
      if (jnlOAType == null) {
        jnlOAType = new JnlOAType(jid);
      }
      ConstOAType constOAType = this.constOATypeDao.getConstOATypeByRomeoColour(romeoColour);
      if (constOAType != null) {
        jnlOAType.setOaTypeId(constOAType.getId());
        jnlOAType.setRomeoColourZh(constOAType.getRomeoColourZh());
        jnlOAType.setRomeoColourEn(constOAType.getRomeoColourEn());
      } else {
        jnlOAType.setRomeoColourZh(romeoColour);
        jnlOAType.setRomeoColourEn(romeoColour);
      }
      this.jnlOATypeDao.save(jnlOAType);
    } catch (Exception e) {
      logger.error("更新期刊-开放存储类型出错,jid: " + jid, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveJnlOATypeRefresh(JnlOATypeRefresh jnlOATypeRefresh) throws ServiceException {
    try {
      jnlOATypeRefreshDao.save(jnlOATypeRefresh);
    } catch (Exception e) {
      logger.error("保存期刊-开放存储类型刷新记录出错，jid: " + jnlOATypeRefresh.getJid() + ",status: " + jnlOATypeRefresh.getStatus(),
          e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdateJnlOATypeRefresh(Long jid) {
    try {
      JnlOATypeRefresh jnlOATypeRefresh = this.jnlOATypeRefreshDao.get(jid);
      if (jnlOATypeRefresh == null) {
        jnlOATypeRefresh = new JnlOATypeRefresh(jid);
      }
      jnlOATypeRefresh.setStatus(0);
      this.jnlOATypeRefreshDao.save(jnlOATypeRefresh);
    } catch (Exception e) {
      logger.error("保存或更新期刊-开放存储类型刷新记录出错，jid: " + jid, e);
    }
  }

  @Override
  public void saveJnlOATypeRefresh(Long jid) {
    try {
      JnlOATypeRefresh jnlOATypeRefresh = this.jnlOATypeRefreshDao.get(jid);
      if (jnlOATypeRefresh == null) {
        jnlOATypeRefresh = new JnlOATypeRefresh(jid);
        this.jnlOATypeRefreshDao.save(jnlOATypeRefresh);
      }
    } catch (Exception e) {
      logger.error("保存期刊-开放存储类型刷新记录出错，jid: " + jid, e);
    }
  }

}
