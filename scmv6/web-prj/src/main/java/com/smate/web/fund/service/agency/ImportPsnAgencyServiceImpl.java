package com.smate.web.fund.service.agency;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.fund.agency.dao.FundAgencyInterestDao;
import com.smate.web.fund.agency.model.FundAgencyInterest;
import com.smate.web.fund.model.common.ConstFundAgency;
import com.smate.web.fund.recommend.dao.ConstFundAgencyDao;
import com.smate.web.prj.dao.sns.PsnInsDao;


/**
 * 同步人员关注的资助机构
 * 
 * @author Administrator
 *
 */
@Service("importPsnAgencyService")
@Transactional(rollbackFor = Exception.class)
public class ImportPsnAgencyServiceImpl implements ImportPsnAgencyService {
  @Autowired
  private PsnInsDao psnInsDao;
  @Autowired
  private FundAgencyInterestDao fundAgencyInterestDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;

  @Override
  public void importPsnAgency(Long insId, String angencyIdsStr) throws Exception {
    if (insId != null && StringUtils.isNotBlank(angencyIdsStr)) {
      List<Long> psnIdList = psnInsDao.getAllPsnIdByInsId(insId);
      for (Long psnId : psnIdList) {
        updatePsnAgency(psnId, angencyIdsStr);
      }
    }

  }

  public void updatePsnAgency(Long psnId, String agencyIdsStr) {
    List<Long> agencyIdList = Stream.of(agencyIdsStr.split(",")).map(ids -> NumberUtils.toLong(ids, 0L))
        .filter(id -> id > 0).collect(Collectors.toList());
    Integer increase = 1;
    Integer order = fundAgencyInterestDao.getPsnFundAgencyInteresNum(psnId);
    for (Long agencyId : agencyIdList) {
      ConstFundAgency agency = constFundAgencyDao.findFundAgencyInfo(agencyId);
      if (agency != null) {
        FundAgencyInterest psnAgency = fundAgencyInterestDao.getFundAgencyInteresByPsnIdAndAgencyId(psnId, agencyId);
        if (psnAgency == null) {
          psnAgency = new FundAgencyInterest();
          psnAgency.setAgencyId(agencyId);
          psnAgency.setPsnId(psnId);
          psnAgency.setStatus(1);
          psnAgency.setAgencyOrder(order + increase);
          increase++;
          psnAgency.setUpdateDate(new Date());
          psnAgency.setCreateDate(new Date());
          fundAgencyInterestDao.save(psnAgency);
        }
      }
    }
  }

}
