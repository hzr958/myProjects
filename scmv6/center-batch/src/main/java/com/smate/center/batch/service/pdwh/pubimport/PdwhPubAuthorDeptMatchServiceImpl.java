package com.smate.center.batch.service.pdwh.pubimport;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.smate.center.batch.dao.pdwh.pub.PdwhInsAddrConstDao;
import com.smate.center.batch.dao.pdwh.pub.PdwhMemberInsNameDAO;
import com.smate.center.batch.dao.pdwh.pub.PdwhPubMemberDAO;
import com.smate.center.batch.dao.pdwh.pub.PubPdwhDetailDAO;
import com.smate.center.batch.model.pdwh.pub.PdwhInsAddrConst;
import com.smate.center.batch.util.pub.AuthorNameUtils;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.web.v8pub.dom.MemberInsBean;
import com.smate.web.v8pub.dom.PubMemberBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

@Service("PdwhPubAuthorDeptMatchService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubAuthorDeptMatchServiceImpl implements PdwhPubAuthorDeptMatchService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhAddrMacthInsService pdwhAddrMacthInsService;
  @Autowired
  private PdwhInsAddrConstDao pdwhInsAddrConstDao;
  @Autowired
  private PdwhMemberInsNameDAO pdwhMemberInsNameDAO;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PdwhPubMemberDAO pdwhPubMemberDAO;


  @Override
  public void saveMemberInsData(Map<String, Object> memberMap, PubPdwhDetailDOM pdwhPub) {
    String dept = HtmlUtils.Html2Text(memberMap.get("dept").toString().trim());
    Long id = Long.valueOf(memberMap.get("id").toString());
    Long pdwhPubId = Long.valueOf(memberMap.get("pdwhPubId").toString());
    try {
      Map<String, Set<String>> extractInsName =
          pdwhAddrMacthInsService.getExtractInsName(AuthorNameUtils.replaceChars(dept));
      Set<String> addrlist = extractInsName.get("scm_ins_name");
      /*
       * logger.error( "pubId:" + memberMap.get("pdwhPubId") + "的memberId" + memberMap.get("memberId") +
       * "匹配到的单位地址" + addrlist);
       */
      if (!CollectionUtils.isEmpty(addrlist)) {
        for (String addr : addrlist) {
          Long addrHash = PubHashUtils.cleanPubAddrHash(addr);
          List<PdwhInsAddrConst> insInfo = pdwhInsAddrConstDao.getInsInfoByNameHash(addrHash);
          if (!CollectionUtils.isEmpty(insInfo)) {
            // 先保存至人名单位拆分表
            pdwhMemberInsNameDAO.saveMemberInsData(id, addr, insInfo.get(0).getInsId());
            // 保存至mongodb
            this.SavePubPdwhDetail(pdwhPub, memberMap, addr, insInfo.get(0).getInsId());
            break;
          }
        }
      }
    } catch (Exception e) {
      logger.error("基准库成果作者单位匹配出错，pubId:" + memberMap.get("pdwhPubId"));
    }
  }


  @SuppressWarnings("unchecked")
  private void SavePubPdwhDetail(PubPdwhDetailDOM pdwhPub, Map<String, Object> memberMap, String addr, Long insId) {
    List<PubMemberBean> memberBean = pdwhPub.getMembers();
    for (PubMemberBean pubMemberBean : memberBean) {
      if (pubMemberBean.getName().equals(memberMap.get("name"))) {
        List<MemberInsBean> memberInsList = pubMemberBean.getInsNames();
        Boolean flag = true;
        for (MemberInsBean memberInsBean : memberInsList) {
          if (addr.equals(memberInsBean.getInsName())) {
            flag = false;
          }
        }
        if (flag) {
          MemberInsBean insBean = new MemberInsBean();
          insBean.setInsName(addr);
          insBean.setInsId(insId);
          memberInsList.add(insBean);
        }
        pubMemberBean.setInsNames(memberInsList);

      }
    }
    pubPdwhDetailDAO.update(pdwhPub);
  }


  @Override
  public void updateInsCount(Long pubId) {
    List<Long> memberIdList = pdwhPubMemberDAO.getIdByPubId(pubId);
    for (Long memberId : memberIdList) {
      List<Long> insIds = pdwhMemberInsNameDAO.getMatchIns(memberId);
      if (insIds != null && insIds.size() > 0) {
        pdwhPubMemberDAO.updateInsCount(insIds.size(), memberId);
      }
    }
  }

}
