package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPubDao;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;

/**
 * 个人成果列表查询服务
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
@Transactional(rollbackFor = Exception.class)
public class PubListQueryServiceImpl extends AbstractPubQueryService {
  @Resource
  private PubSnsService pubSnsService;
  @Resource
  private PsnPubService psnPubService;
  @Autowired
  private PubSnsDetailService pubSnsDetailService;
  @Autowired
  private PsnConfigDao psnConfigDao;
  @Autowired
  private PsnConfigPubDao psnConfigPubDao;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (pubQueryDTO.getSearchPsnId() == null || pubQueryDTO.getSearchPsnId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的人员psnId不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    pubSnsService.queryPubList(pubQueryDTO);
  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult listResult = new PubListResult();
    List<Long> pubIdList = new ArrayList<>();
    List<PubPO> pubList = pubQueryDTO.getPubList();
    if (pubList != null && pubList.size() > 0) {
      List<PubInfo> list = new ArrayList<>(pubList.size() + 1);
      for (PubPO pubPO : pubList) {
        PubSnsPO pubSnsPO = (PubSnsPO) pubPO;
        PubInfo pubInfo = new PubInfo();

        Long cnfId = psnConfigDao.getPsnConfId(pubQueryDTO.getSearchPsnId());
        Integer anyUser = psnConfigPubDao.getAnyUser(cnfId, pubSnsPO.getPubId());
        pubInfo.setIsAnyUser(anyUser);// 获取成果的隐私

        pubInfo.setPubId(pubSnsPO.getPubId());
        pubInfo.setDes3PubId(Des3Utils.encodeToDes3(pubSnsPO.getPubId().toString()));
        pubInfo.setTitle(pubPO.getTitle());
        pubInfo.setBriefDesc(pubPO.getBriefDesc());
        // pubInfo.setAuthorNames(pubSnsDetailService.buildPubAuthorNames(pubSnsPO.getPubId()));
        // AuthorNames 为空时 在判断pubPO的AuthorNames
        // --SCM-20775 直接取值
        pubInfo.setAuthorNames(pubPO.getAuthorNames());
        pubInfo.setNoneHtmlLableAuthorNames(HtmlUtils.Html2Text(pubPO.getAuthorNames()));
        pubInfo.setCitations(pubPO.getCitations());
        pubInfo.setOwnerPsnId(psnPubService.getPubOwnerId(pubSnsPO.getPubId()));
        pubInfo.setUpdateMark(pubPO.getUpdateMark());
        pubInfo.setPublishYear(pubPO.getPublishYear());
        if (pubQueryDTO.isSearchDoi()) {
          PubSnsDetailDOM detailDOM = pubSnsDetailService.getByPubId(pubSnsPO.getPubId());
          if (detailDOM != null) {
            pubInfo.setDOI(detailDOM.getDoi());
            pubInfo.setSrcDbId(detailDOM.getSrcDbId());
          }
        }
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == 0) {
          /* currentUserId = pubQueryDTO.getSearchPsnId(); */
          currentUserId = pubQueryDTO.getPsnId();
        }
        builSnsPubStatistics(pubInfo, currentUserId);// 构建初始化赞/分享信息
        pubIdList.add(pubSnsPO.getPubId());
        list.add(pubInfo);
      }
      if (!pubQueryDTO.isNotFulltextAndSortUrl()) {// 需要全文和短地址
        // 成果短地址
        buildSnsPubIndexUrl(list, pubIdList);
        // 全文
        Long psnId = pubQueryDTO.getPsnId();
        if (!NumberUtils.isNullOrZero(psnId)) {
          buildSnsPubFulltext(list, pubIdList, psnId);
        } else {
          buildSnsPubFulltext(list, pubIdList);
        }
      }
      listResult.setResultList(list);
      listResult.setTotalCount(NumberUtils.toInt(pubQueryDTO.getTotalCount().toString()));
    }
    return listResult;
  }
}
