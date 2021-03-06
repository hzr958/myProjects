package com.smate.web.v8pub.service.pubquery;

import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 中山系统，获取成果列表查询
 *
 * @author aijiangbin
 * @date 2018-10-22
 */

@Transactional(rollbackFor = Exception.class)
public class ZSPubListQueryServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubSnsService pubSnsService;
  @Resource
  private PubFullTextService pubFullTextService;
  @Resource
  private PsnPubService psnPubService;

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
    pubSnsService.queryPubListForZS(pubQueryDTO);

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
        pubInfo.setPubId(pubSnsPO.getPubId());
        pubInfo.setDes3PubId(Des3Utils.encodeToDes3(pubSnsPO.getPubId().toString()));
        pubInfo.setTitle(pubPO.getTitle());
        pubInfo.setOwnerPsnId(psnPubService.getPubOwnerId(pubSnsPO.getPubId()));
        pubInfo.setBriefDesc(pubPO.getBriefDesc());
        pubInfo.setAuthorNames(pubPO.getAuthorNames());
        pubInfo.setPubType(pubPO.getPubType());
        PubFullTextPO fullTextPO = pubFullTextService.get(pubSnsPO.getPubId());
        if (fullTextPO != null) {
          pubInfo.setFullTextFieId(fullTextPO.getFileId());
          if (StringUtils.isNotBlank(fullTextPO.getFileName())) {
            try {
              FileUtils.FileNameExtension fileNameExtension = FileUtils.getFileNameExtension(fullTextPO.getFileName());
              pubInfo.setFulltextExt(fileNameExtension != null ? fileNameExtension.getValue() : "");
            } catch (Exception e) {
            }
          }
        }
        pubIdList.add(pubSnsPO.getPubId());
        list.add(pubInfo);
      }
      buildSnsPubFulltext(list, pubIdList, pubQueryDTO.getSearchPsnId());
      listResult.setResultList(list);
    }
    listResult.setTotalCount(
        pubQueryDTO.getTotalCount() == null ? 0 : NumberUtils.toInt(pubQueryDTO.getTotalCount().toString()));
    return listResult;
  }

}
