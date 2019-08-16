package com.smate.center.task.service.rol.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.bpo.ConstDictionaryBpoDao;
import com.smate.center.task.dao.bpo.InsDataFromBpoDao;
import com.smate.center.task.dao.bpo.InsRegionBpoDao;
import com.smate.center.task.dao.bpo.InstitutionBpoDao;
import com.smate.center.task.dao.bpo.InstitutionEditHistoryBpoDao;
import com.smate.center.task.dao.bpo.TmpInsInfoDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.bpo.InsInfo;
import com.smate.center.task.model.bpo.InstitutionBpo;
import com.smate.center.task.model.bpo.InstitutionEditHistoryBpo;
import com.smate.center.task.model.bpo.TmpInsInfo;
import com.smate.center.task.model.common.InsDataFrom;
import com.smate.center.task.model.common.InsDataFromId;
import com.smate.center.task.model.common.InsReg;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.dao.security.bpo.BpoInsPortalDao;
import com.smate.core.base.utils.model.bpo.BpoInsPortal;

@Service("bpoInsSyncService")
@Transactional(rollbackFor = Exception.class)
public class BpoInsSyncServiceImpl implements BpoInsSyncService {

  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TmpInsInfoDao tmpInsInfoDao;
  @Autowired
  private InstitutionBpoDao institutionBpoDao;
  @Autowired
  private InstitutionEditHistoryBpoDao institutionEditHistoryBpoDao;
  @Autowired
  private BpoInsPortalDao bpoInsPortalDao;
  @Autowired
  private InsDataFromBpoDao insDataFromBpoDao;
  @Autowired
  private ConstDictionaryBpoDao constDictionaryDao;
  @Autowired
  private InsRegionBpoDao insRegionBpoDao;

  /**
   * 查询需要同步信息的单位.
   * 
   * @return
   * @throws ServiceException
   */
  @Override
  public List<TmpInsInfo> findSyncInsList(int maxsize) throws ServiceException {
    List<TmpInsInfo> insList = null;
    try {
      insList = tmpInsInfoDao.findSyncInsList(maxsize, 0L);
    } catch (Exception e) {
      logger.error("同步单位信息不正确，查询单位信息失败", e);
      throw new ServiceException("tmp_ins_info查询单位信息失败", e);
    }
    return insList;
  }

  @Override
  public Long addIns(InsInfo info) throws ServiceException {
    if (!NumberUtils.isNumber(info.getOrgCode().toString()) || info.getToken() == null) {
      throw new ServiceException("同步单位信息不正确，orgcode=" + info.getOrgCode() + " token=" + info.getToken());
    }
    Long orgCode = info.getOrgCode();
    try {
      // 通过orgcode和token，查询现有单位id
      InsDataFrom insByCode = insDataFromBpoDao.get(new InsDataFromId(orgCode, info.getToken()));
      // 通过单位名称，查询现有单位
      InstitutionBpo insByName = institutionBpoDao.findInsByName(info.getZhName());
      if (insByCode == null && insByName == null) {// 不存在单位需要新增单位
        // 查询新单位的ins_id
        Long insId = institutionBpoDao.findNewInsId();
        if (insId == null || insId == 0) {
          logger.error("同步单位信息不正确，新增或获取ins_id失败");
          throw new ServiceException("同步单位信息不正确，新增或获取ins_id失败");
        }
        // INS_REGION
        this.addInsReg(insId, info.getPrvId(), info.getCyId(), info.getDisId());
        info.setInsId(insId);

        InstitutionBpo ins = new InstitutionBpo(info);
        // 判断是否来自isis系统的数据
        if (info.getToken().equalsIgnoreCase("2bcca485")) {
          ins.setIsisOrgCode(Integer.valueOf(info.getOrgCode().toString()));
        }
        // 添加
        institutionBpoDao.save(ins);
        // 向数据来源表插入数据
        insDataFromBpoDao.save(new InsDataFrom(info.getInsId(), info.getOrgCode(), info.getToken(), new Date()));
        return insId;
      } else {// 已存在单位，更新单位信息
        if (insByName == null) {
          logger.error("仅匹配到orgcode，且无名称相同的记录，不考虑单位名称修改的情况，insId=" + insByCode.getInsId() + ",orgCode+token="
              + info.getOrgCode() + info.getToken());
          throw new ServiceException("仅匹配到orgcode，且无名称相同的记录，不考虑单位名称修改的情况");
        }
        // 需要考虑更新的字段：联系电话、单位网址、单位地址（这些字段均为原先为空时更新，否则不更新）
        String contactTel = StringUtils.isBlank(insByName.getTel()) ? info.getContactTel() : insByName.getTel();
        String url = StringUtils.isBlank(insByName.getUrl()) ? info.getUrl() : insByName.getUrl();
        String address = StringUtils.isBlank(insByName.getZhAddress()) ? info.getAddress() : insByName.getZhAddress();
        Long regionId = 0L;
        // INS_REGION
        InsReg r = this.addInsReg(insByName.getId(), info.getPrvId(), info.getCyId(), info.getDisId());
        // 从ins_region取值更新，取值顺序：市->省，即优先取“市”，若“市”无值（为空或等于0）则取“省”更新）
        if (r.getCyId() != null && r.getCyId() > 0) {
          regionId = r.getCyId();
        } else {
          regionId = r.getPrvId();
        }
        if (insByCode != null && insByName != null) {
          // 比较ins_id是否相同，相同更新，不同报异常，名字被占用
          if (insByCode.getInsId().equals(insByName.getId())) {// 同一条记录
            // 备份修改前单位的数据
            InstitutionBpo ins = institutionBpoDao.get(insByCode.getInsId());
            institutionEditHistoryBpoDao.save(new InstitutionEditHistoryBpo(ins));
            // 直接更新
            institutionBpoDao.updateIns(new Object[] {contactTel, regionId, address, url, insByCode.getInsId()});
            return insByCode.getInsId();
          } else {
            logger.error("存在orgcode和单位名称两条记录，发生冲突，请更改被占用的单位名称。");
            throw new ServiceException("存在orgcode和单位名称两条记录，发生冲突，请更改被占用的单位名称。");
          }
        } else {
          if (insByName != null) {
            InsDataFrom df = insDataFromBpoDao.findByInsIdAndToken(insByName.getId(), info.getToken());
            // 单位名称被另一条记录占用，orgcode非空，且不一样
            if (df != null && df.getId().getOrgCode() != null && !df.getId().getOrgCode().equals(info.getOrgCode())) {// 存在单位名称，但单位orgcode不同，返回错误状态
              logger.error("单位名称被另一条记录（orgcode非空，且不一样）占用。");
              throw new ServiceException("单位名称被另一条记录（orgcode非空，且不一样）占用。");
            } else {// 名字相同，且orgcode为空
              // 备份修改前单位的数据
              InstitutionBpo ins = institutionBpoDao.get(insByName.getId());
              institutionEditHistoryBpoDao.save(new InstitutionEditHistoryBpo(ins));
              // 更新信息
              institutionBpoDao.updateIns(new Object[] {contactTel, regionId, address, url, insByName.getId()});
              // 向数据来源表插入数据
              insDataFromBpoDao
                  .save(new InsDataFrom(insByName.getId(), info.getOrgCode(), info.getToken(), new Date()));
              return insByName.getId();
            }
          }

          logger.error("仅匹配到orgcode，且无名称相同的记录，不考虑单位名称修改的情况，insId=" + insByCode.getInsId() + ",orgCode+token="
              + info.getOrgCode() + info.getToken());
          throw new ServiceException("仅匹配到orgcode，且无名称相同的记录，不考虑单位名称修改的情况");
          // 仅有orgcode
          // 备份修改前单位的数据
          /*
           * InstitutionBpo ins=institutionBpoDao.get(insByCode.getInsId());
           * institutionEditHistoryBpoDao.save(new InstitutionEditHistoryBpo(ins)); //
           * 仅匹配到orgcode，且无名称相同的记录，更新信息 institutionBpoDao.updateIns(new Object[] { info.getZhName(),
           * info.getEnName(), info.getContactName(), info.getContactTel(), info.getRegionId(),
           * Integer.valueOf(info.getOrgCode().toString()),info.getNature(), info.getAddress(),info.getUrl(),
           * info.getContactEmail(),insByCode.getInsId()});
           */

        }
      }
    } catch (DaoException e) {
      logger.error(e.getMessage() + "同步单位信息不正确，新增或更新单位信息失败", e);
      throw new ServiceException(e.getMessage() + "  institution新增或更新单位信息失败", e);
    }
  }

  @Override
  public void addInsPortal(InsInfo info) throws ServiceException {
    if (info.getInsId() == null) {
      logger.error("同步单位信息不正确，insId不能为空，单位：" + info.getZhName());
      throw new ServiceException("同步单位信息不正确，insId不能为空，单位：" + info.getZhName());
    }

    try {
      // 查询现有单位
      BpoInsPortal insPortal = bpoInsPortalDao.get(info.getInsId());
      if (insPortal == null) {// 不存在单位需要增加域名配置

        // 查询新单位的ins_id
        String domain = info.getInsId() + ".cn.scholarmate.com";
        insPortal = new BpoInsPortal();
        insPortal.setInsId(info.getInsId());
        insPortal.setDomain(domain);
        insPortal.setRolNodeId(10000);
        insPortal.setSnsNodeId(1);
        insPortal.setDefaultLang(info.getDefaultLang());
        insPortal.setSwitchLang(info.getSwitchLang());
        insPortal.setVersion(info.getVersion());
        insPortal.setZhTitle(info.getZhName());
        insPortal.setEnTitle(info.getEnName());
      }
      bpoInsPortalDao.saveEntity(insPortal);
    } catch (Exception e) {
      logger.error("同步单位信息不正确，新增或更新单位域名失败", e);
      throw new ServiceException("ins_portal新增或更新单位域名失败", e);
    }

  }


  @Override
  public void updateTmpInsInfo(TmpInsInfo info) throws ServiceException {
    try {
      tmpInsInfoDao.save(info);
    } catch (Exception e) {
      logger.error("同步单位信息不正确，保存单位更新状态失败,id=" + info.getId() + ",synFlag=" + info.getSynFlag(), e);
      throw new ServiceException("tmp_ins_info保存单位更新状态失败", e);
    }
  }

  @Override
  public Long getNatureByName(String name) throws ServiceException {
    try {
      if (name == null) {
        return 99L;
      }
      ConstDictionary c = constDictionaryDao.findConstByCategoryAndName("ins_type", name.trim());
      if (c != null) {
        return Long.valueOf(c.getKey().getCode());
      }
    } catch (Exception e) {
      logger.error("同步单位信息不正确，高查询单位类型失败", e);
      throw new ServiceException("查询CONST_DICTIONARY表单位类型失败,name=" + name, e);
    }
    return 99L;

  }

  public InsReg addInsReg(Long insId, Long prvId, Long cyId, Long disId) throws ServiceException {
    if (insId == null) {

      throw new ServiceException("同步单位信息不正确，insId不能为空");
    }

    try {
      InsReg ins = insRegionBpoDao.get(insId);
      if (ins == null) {
        ins = new InsReg();
        ins.setInsId(insId);
        ins.setPrvId(prvId);
        ins.setCyId(cyId);
        ins.setDisId(disId);
      } else {
        if (ins.getCyId() == null || ins.getCyId() == 0) {
          ins.setCyId(cyId);
        }
        if (ins.getPrvId() == null || ins.getPrvId() == 0) {
          ins.setPrvId(prvId);
        }
        ins.setDisId(disId);
      }

      insRegionBpoDao.save(ins);
      return ins;
    } catch (Exception e) {
      logger.error("同步单位信息不正确，高校所在地表记录失败", e);
      throw new ServiceException("INS_REGION增加失败,insId=" + insId, e);
    }


  }



}
