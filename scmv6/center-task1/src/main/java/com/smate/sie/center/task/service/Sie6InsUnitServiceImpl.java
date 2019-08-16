package com.smate.sie.center.task.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;
import com.smate.sie.center.task.dao.ImportThirdUnitsHistoryDao;
import com.smate.sie.center.task.dao.Sie6InsUnitDao;
import com.smate.sie.center.task.model.ImportThirdUnits;
import com.smate.sie.center.task.model.ImportThirdUnitsHistory;
import com.smate.sie.center.task.model.Sie6InsUnit;
import com.smate.sie.core.base.utils.unit.avatars.UnitAvatarsUtils;

/**
 * 单位部门服务实现.
 * 
 * @author xys
 *
 */
@Service("sie6InsUnitService")
@Transactional(rollbackFor = Exception.class)
public class Sie6InsUnitServiceImpl implements Sie6InsUnitService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private Sie6InsUnitDao sie6InsUnitDao;
  @Autowired
  private ImportThirdUnitsHistoryDao importThirdUnitsHistoryDao;

  @Autowired
  private Sie6InsPortalDao sie6InsPortalDao;


  @Autowired
  private SieInsUnitMergeService sieInsUnitMergeService;

  @Value("${sie.file.root}")
  private String rootPath;

  @Override
  public void refreshInsUnit(ImportThirdUnits importThirdUnits) throws ServiceException {
    String unitId = importThirdUnits.getPk().getUnitId();
    Long insId = importThirdUnits.getPk().getInsId();
    String zhName = importThirdUnits.getZhName();
    String enName = importThirdUnits.getEnName();
    Sie6InsUnit insUnit = null;
    Long sieUnitId = null;
    try {
      ImportThirdUnitsHistory importThirdUnitsHistory =
          importThirdUnitsHistoryDao.getImportThirdUnitsHistory(insId, unitId);
      if (importThirdUnitsHistory != null) {
        sieUnitId = importThirdUnitsHistory.getSieUnitId();
        if (sieUnitId != null && sieUnitId > 0) {
          importThirdUnits.setSieUnitId(sieUnitId);
          insUnit = this.sie6InsUnitDao.get(sieUnitId);

          // sieUnitId !=null ,考虑merge_unit表， 找到合并至的最终部门finalUnitId
          if (insUnit == null) {// ins_unit表不存在记录时， 则去merge_unit 表中查询是否被合并了;
            Long finalUnitId = null;
            finalUnitId = sieInsUnitMergeService.getFinalUnitId(insId, sieUnitId);

            // 当finalUnitId 与 sieUnitId 不相等时
            if (finalUnitId != null && !sieUnitId.equals(finalUnitId)) {
              sieUnitId = finalUnitId;
            }

            // 根据最新的sieUnitId 再查下部门
            insUnit = sie6InsUnitDao.get(sieUnitId);
          }

          if (insUnit != null) {// 更新部门信息
            insUnit.setZhName(zhName);
            insUnit.setEnName(enName);
            insUnit.setUpdDate(new Date()); // 添加更新日期
            this.sie6InsUnitDao.save(insUnit);
            // ROL-5910 实现部门头像生成
            this.saveUnitImg(insUnit);
            importThirdUnits.setSieUnitId(insUnit.getId());
          }
        }
      }
      if (insUnit == null) {// 新增部门信息
        insUnit = new Sie6InsUnit();
        insUnit.setInsId(insId);
        insUnit.setZhName(zhName);
        insUnit.setEnName(enName);
        insUnit.setCreDate(new Date());// 添加新增部门日期
        insUnit.setUpdDate(new Date());// 添加更新部门日期
        this.sie6InsUnitDao.save(insUnit);
        // ROL-5910 实现部门头像生成
        this.saveUnitImg(insUnit);
        importThirdUnits.setSieUnitId(insUnit.getId());
      }
    } catch (Exception e) {
      logger.error("刷新单位部门信息出现异常了喔,insId:{},unitId:{},unitName:{},pid:{}",
          new Object[] {importThirdUnits.getPk().getInsId(), importThirdUnits.getPk().getUnitId(),
              importThirdUnits.getZhName(), importThirdUnits.getSuperUnitId(), e});
      throw new ServiceException(e);
    }
  }

  /**
   * 产生用户头像， 并将访问地址放入sieInsUnit表中
   * 
   * @param sieInsUnit
   */
  private void saveUnitImg(Sie6InsUnit sieInsUnit) {
    String zhName = sieInsUnit.getZhName();
    String enName = sieInsUnit.getEnName();
    Sie6InsPortal insPortal = sie6InsPortalDao.get(sieInsUnit.getInsId()); // 单位域名设置.
    String domainscm = "http://" + insPortal.getDomain();
    // 默认图标路径
    String avatars = domainscm + "/ressie/images/fixation.png";

    try {
      // 生成图标使用的字符串
      String imgStr = UnitAvatarsUtils.getIconStr(zhName, enName);
      if (imgStr.length() > 0) {
        String iconPath = UnitAvatarsUtils.unitAvatars(imgStr, sieInsUnit.getId(), rootPath + "/unitavatars");

        // 新生成图标路径
        avatars = domainscm + "/unitavatars" + iconPath;
      }
    } catch (Exception e) {
      logger.error("根据部门名称随机产生默认头像失败!");
    }

    sieInsUnit.setUnitAvatars(avatars);
    this.sie6InsUnitDao.save(sieInsUnit);
  }
}
