package com.smate.center.task.service.tmp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.dao.tmp.FundAgencyAddrDao;
import com.smate.center.task.model.fund.rcmd.ConstFundAgency;
import com.smate.center.task.model.sns.pub.ConstRegion;

@Service("fundAgencyAddressService")
@Transactional(rollbackFor = Exception.class)
public class FundAgencyAddressServiceImpl implements FundAgencyAddressService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private FundAgencyAddrDao fundAgencyAddrDao;

  @Override
  public List<Long> getFundAgency(Integer size) {
    return null;
  }

  @Override
  public void startProcessing() throws Exception {
    List<ConstFundAgency> constFundAgencys = fundAgencyAddrDao.batchGetData();
    List<Long> regionIds = new ArrayList<Long>();
    for (ConstFundAgency constFundAgency : constFundAgencys) {
      if (constFundAgency.getRegionId() != null) {
        regionIds = constRegionDao.getSuperRegionList(constFundAgency.getRegionId(), true);
        List<ConstRegion> regList = constRegionDao.findBitchRegionName(regionIds);
        String address = "";
        String enAddress = "";
        for (ConstRegion constRegion : regList) {
          address = address + constRegion.getZhName() + ", ";
          enAddress = enAddress + constRegion.getEnName() + ", ";
        }
        int indx = address.lastIndexOf(",");
        if (indx != -1) {
          address = address.substring(0, indx) + address.substring(indx + 1, address.length());
        }
        int enindx = enAddress.lastIndexOf(",");
        if (enindx != -1) {
          enAddress = enAddress.substring(0, enindx) + enAddress.substring(enindx + 1, enAddress.length());
        }

        fundAgencyAddrDao.updateAddress(address, enAddress, constFundAgency.getId());
      }
    }

  }

}
