package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.dao.sns.representpub.RepresentPubDao;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PubListResult;

/**
 * 根据des3PsnId获取个人的代表成果
 * 
 * @author Administrator
 *
 */
@Transactional(rollbackOn = Exception.class)
public class PsnRepresentQueryServiceImpl extends AbstractPubQueryService {
  @Autowired
  private PsnConfigDao psnConfigDao;
  @Autowired
  private RepresentPubDao representPubDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (StringUtils.isBlank(pubQueryDTO.getDes3SearchPsnId())) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询个人代表成果的des3SearchPsnId不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    Long psnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(pubQueryDTO.getDes3SearchPsnId()), 0L);
    if (NumberUtils.isZero(psnId)) {
      psnId = pubQueryDTO.getPsnId();
    }
    if (!NumberUtils.isZero(psnId)) {
      pubQueryDTO.setSearchPsnId(psnId);
      Long confId = psnConfigDao.getPsnConfId(psnId);
      // 查询代表性成果ID
      List<Long> pubIds =
          representPubDao.findPsnRepresentPubIds(psnId, 0, confId, PsnCnfConst.ALLOWS_SELF + PsnCnfConst.ALLOWS_FRIEND);
      if (pubIds.size() > 0) {
        pubQueryDTO.setSearchPubIdList(pubIds);
        // pubSnsDAO.queryByPubIds(pubQueryDTO);
      }
    }

  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult result = new PubListResult();
    List<PubInfo> pubInfoList = new ArrayList<PubInfo>();
    // List<PubPO> pubList = pubQueryDTO.getPubList();
    if (CollectionUtils.isNotEmpty(pubQueryDTO.getSearchPubIdList())) {
      for (Long pubId : pubQueryDTO.getSearchPubIdList()) {
        PubSnsPO pubSnsPO = pubSnsDAO.get(pubId);
        PubInfo pubInfo = new PubInfo();
        pubInfo.setIsRepresentPub(1);
        pubInfo.setPubId(pubSnsPO.getPubId());
        pubInfo.setDes3PubId(ServiceUtil.encodeToDes3(pubSnsPO.getPubId().toString()));
        // 初始化成果的赞，评论一些操作
        builSnsPubStatistics(pubInfo, pubQueryDTO.getPsnId());
        pubInfo.setTitle(pubSnsPO.getTitle());
        pubInfo.setBriefDesc(pubSnsPO.getBriefDesc());
        pubInfo.setOwnerPsnId(pubQueryDTO.getSearchPsnId());
        pubInfo.setDes3OwnerPsnId(Des3Utils.encodeToDes3(pubQueryDTO.getSearchPsnId().toString()));
        pubInfo.setAuthorNames(pubSnsPO.getAuthorNames());
        pubInfo.setPublishYear(pubSnsPO.getPublishYear());

        // 成果短地址
        buildSnsPubIndexUrl(pubInfo, pubQueryDTO.getSearchPsnId());
        buildSnsPubFulltext(pubInfo, pubQueryDTO.getPsnId());// 构建全文下载短地址和图片链接
        pubInfoList.add(pubInfo);
      }
      result.setTotalCount(pubInfoList.size());
      result.setResultList(pubInfoList);
    }

    return result;
  }
}
