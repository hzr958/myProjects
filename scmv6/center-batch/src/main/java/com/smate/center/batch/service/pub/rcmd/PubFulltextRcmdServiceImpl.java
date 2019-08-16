package com.smate.center.batch.service.pub.rcmd;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rcmd.pub.RcmdIsiPublicationDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.pub.RcmdIsiPublication;
import com.smate.center.batch.util.pub.LogUtils;

/**
 * 成果全文推荐服务.
 * 
 * @author pwl
 * 
 */
@Service("pubFulltextRcmdService")
@Transactional(rollbackFor = Exception.class)
public class PubFulltextRcmdServiceImpl implements PubFulltextRcmdService {

  /**
   * 
   */
  private static final long serialVersionUID = -3147114933697328969L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private RcmdIsiPublicationDao rcmdIsiPublicationDao;

  @Override
  public void saveIsiPublication(List<Map<String, Object>> list) throws ServiceException {

    for (Map<String, Object> map : list) {
      Long pubId = NumberUtils.createLong(ObjectUtils.toString(map.get("pubId")));
      try {
        RcmdIsiPublication isiPub = this.rcmdIsiPublicationDao.get(pubId);
        if (isiPub == null) {
          isiPub = new RcmdIsiPublication(pubId);
        }
        Integer pubType = NumberUtils.toInt(ObjectUtils.toString(map.get("pubType")));
        isiPub.setPubType(pubType);
        String title = map.get("title") != null ? map.get("title").toString() : null;
        isiPub.setTitle(title);
        Long titleHash = map.get("titleHash") != null ? Long.valueOf(map.get("titleHash").toString()) : null;
        isiPub.setTitleHash(titleHash);
        Long issnHash = map.get("issnHash") != null ? (Long) map.get("issnHash") : null;
        isiPub.setIssnHash(issnHash);
        Long confnHash = map.get("confnHash") != null ? (Long) map.get("confnHash") : null;
        isiPub.setConfnHash(confnHash);
        String sourceId = map.get("sourceId") != null ? map.get("sourceId").toString() : null;
        isiPub.setSourceId(sourceId);
        this.rcmdIsiPublicationDao.save(isiPub);
      } catch (Exception e) {
        LogUtils.error(logger, e, "保存基准库成果库ISI成果pubId={}冗余信息", pubId);
      }
    }
  }

}
