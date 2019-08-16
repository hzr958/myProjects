package com.smate.center.data.service.pub;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.data.dao.pub.PubSimpleDao;
import com.smate.center.data.dao.pub.PubSimpleHashDao;
import com.smate.center.data.model.pub.PubSimple;
import com.smate.center.data.model.pub.PubSimpleHash;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.common.HtmlUtils;

/**
 * 成果hash服务实现类
 * 
 * @author lhd
 *
 */
@Service("pubSimpleHashService")
@Transactional(rollbackFor = Exception.class)
public class PubSimpleHashServiceImpl implements PubSimpleHashService {
  @Autowired
  private PubSimpleHashDao pubSimpleHashDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;

  @Override
  public Long getPubSimpleHash(Integer size, Long startPubId, Long endPubId) throws Exception {
    List<Long> pubIds = pubSimpleHashDao.getPubSimpleHashList(size, startPubId, endPubId);
    if (CollectionUtils.isNotEmpty(pubIds)) {
      List<PubSimple> pubs = pubSimpleDao.findPubSimpleListByPubIds(pubIds);
      if (CollectionUtils.isNotEmpty(pubs)) {
        for (PubSimple pub : pubs) {
          PubSimpleHash ph = pubSimpleHashDao.get(pub.getPubId());
          if (ph != null) {
            String zhTitle = pub.getZhTitle();
            String enTitle = pub.getEnTitle();
            String pubType = pub.getPubType() == null ? "" : pub.getPubType() + "";
            String publishYear = pub.getPublishYear() == null ? "" : pub.getPublishYear() + "";
            String zhHashCode = generateHash(zhTitle, pubType, publishYear);
            String enHashCode = generateHash(enTitle, pubType, publishYear);
            String tpHashZh = generateHash(zhTitle, pubType, null);
            String tpHashEn = generateHash(enTitle, pubType, null);
            ph.setZhHashCode(zhHashCode);
            ph.setEnHashCode(enHashCode);
            ph.setTpHashZh(tpHashZh);
            ph.setTpHashEn(tpHashEn);
            pubSimpleHashDao.save(ph);
          }

        }
      }
      return pubIds.get(pubIds.size() - 1);
    }
    return null;
  }

  private String generateHash(String title, String type, String year) {
    if (title == null) {
      title = "";
    }
    if (type == null) {
      type = "";
    }
    title = title.toLowerCase();
    title = title.replaceAll(" and ", "");
    title = title.replaceAll("&amp;", "");
    title = HtmlUtils.Html2Text(title);
    title = title.replaceAll("[\\pP\\p{Punct}\\pZ]", "");
    type = type.trim();
    if (year == null) {
      return HashUtils.getStrHashCode(title + type) + "";
    } else {
      return HashUtils.getStrHashCode(title + type + year) + "";
    }
  }

}
