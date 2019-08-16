package com.smate.center.task.service.rol.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.dao.rol.quartz.PsnPmCnkiNameDao;
import com.smate.center.task.dao.rol.quartz.PsnPmIsiNameDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.model.rol.quartz.PsnPmCnkiName;
import com.smate.center.task.single.dao.rol.psn.RolPsnInsDao;
import com.smate.center.task.single.model.rol.psn.RolPsnIns;
import com.smate.center.task.single.model.rol.psn.RolPsnInsPk;
import com.smate.center.task.single.util.pub.PsnPmIsiNameUtils;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;

@Service("syncPmNameFromPersonService")
@Transactional(rollbackFor = Exception.class)
public class SyncPmNameFromPersonServiceImpl implements SyncPmNameFromPersonService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnPmIsiNameDao psnPmIsiNameDao;
  @Autowired
  private PsnPmCnkiNameDao psnPmCnkiNameDao;
  @Autowired
  private RolPsnInsDao psnInsDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  private static int[] jobType = TaskJobTypeConstants.SyncPmNameFromPersonTask;

  @Override
  public List<TmpTaskInfoRecord> batchGetNeedSyncData(Integer size) throws Exception {
    ArrayList<Integer> list = new ArrayList<Integer>();
    for (Integer type : jobType) {
      list.add(type);
    }
    return tmpTaskInfoRecordDao.getTaskInfoRecordList(size, list);

  }

  @Override
  public void startFillData(TmpTaskInfoRecord job) throws Exception {
    int jobType = job.getJobType();
    Long psnId = job.getHandleId();
    Long insId = job.getRelativeId();
    try {
      if (jobType == 101) {
        this.generalPsnPmIsiName(psnId, insId);
      } else if (jobType == 102) {
        this.saveAddtPsnPmCnkiName(psnId, insId);
      } else if (jobType == 103) {
        // this.fillDataToIns(psnId);
      }
    } catch (Exception e) {
      logger.error("保存数据出错，jobId:" + job.getJobId(), e);
      this.updateTaskStatus(job.getJobId(), 2, e.getMessage());
    }
    this.updateTaskStatus(job.getJobId(), 1, "");
  }

  /**
   * 保存isiname
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void generalPsnPmIsiName(Long psnId, Long insId) throws ServiceException {
    try {
      // Person personName = personDao.getPersonName(psnId);
      RolPsnInsPk pk = new RolPsnInsPk();
      pk.setPsnId(psnId);
      pk.setInsId(insId);
      RolPsnIns personName = psnInsDao.get(pk);

      if (personName == null) {
        return;
      }
      String firstName = StringUtils.trimToNull(personName.getFirstName());
      String lastName = StringUtils.trimToNull(personName.getLastName());
      String otherName = StringUtils.trimToNull(personName.getOtherName());
      if (firstName == null || lastName == null) {
        return;
      }
      firstName = firstName.trim().toLowerCase();
      lastName = lastName.trim().toLowerCase();
      // 很多人的first name是连接在一起的，有必要进行拆分
      firstName = PsnPmIsiNameUtils.splitJoinFirstName(firstName, personName.getZhName());
      firstName = firstName.trim().toLowerCase();

      // 用户名前缀
      Set<String> prefixNames = PsnPmIsiNameUtils.buildPrefixName(firstName, lastName, otherName);
      if (CollectionUtils.isEmpty(prefixNames)) {
        return;
      } else {
        psnPmIsiNameDao.savePrefixName(prefixNames, psnId);
      }

      // initNames
      Set<String> initNames = PsnPmIsiNameUtils.buildInitName(firstName, lastName, otherName);
      if (CollectionUtils.isNotEmpty(initNames)) {
        psnPmIsiNameDao.saveInitName(initNames, psnId);
      }

      // fullNames
      Set<String> fullNames = PsnPmIsiNameUtils.buildFullName(firstName, lastName, otherName);
      if (CollectionUtils.isNotEmpty(fullNames)) {
        psnPmIsiNameDao.saveFullName(fullNames, psnId);
      }

    } catch (Exception e) {
      logger.error("生成用户isi名称", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 保存cnkiname
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void saveAddtPsnPmCnkiName(Long psnId, Long insId) throws ServiceException {
    // Person personName = personDao.getPersonName(psnId);

    RolPsnInsPk pk = new RolPsnInsPk();
    pk.setPsnId(psnId);
    pk.setInsId(insId);
    RolPsnIns personName = psnInsDao.get(pk);
    if (personName == null) {
      return;
    }
    String name = null;
    String zhname = personName.getZhName();
    String enname = personName.getEnName();
    if (StringUtils.isNotBlank(zhname)) {
      name = zhname;
    } else {
      name = enname;
    }
    try {
      if (StringUtils.isBlank(name)) {
        return;
      }
      name = name.trim().toLowerCase();
      if (!this.psnPmCnkiNameDao.isAddtNameExists(name, psnId)) {
        PsnPmCnkiName pn = new PsnPmCnkiName(name, psnId, 2);
        this.psnPmCnkiNameDao.save(pn);
      }
    } catch (Exception e) {
      logger.error("保存用户确认成果CNKI名称", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 保存数据到ROlpsnIns
   * 
   * @param psnId
   */
  public void fillDataToIns(Long psnId) {
    Person person = personDao.get(psnId);
    RolPsnIns ins = new RolPsnIns();
    RolPsnInsPk pk = new RolPsnInsPk();
    pk.setPsnId(psnId);
    pk.setInsId(person.getInsId());
    ins.setPk(pk);
    ins.setPsnEmail(person.getEmail());
    ins.setAvatars(person.getAvatars());
    ins.setEnName(person.getEname());
    ins.setFirstName(person.getFirstName());
    ins.setLastName(person.getLastName());
    ins.setOtherName(person.getOtherName());
    ins.setTel(person.getTel());
    ins.setIsLogin(person.getIsLogin());
    ins.setPosition(person.getPosition());
    ins.setPosId(person.getPosId());
    ins.setPosGrades(person.getPosGrades());
    ins.setSex(person.getSex());
    ins.setCreateAt(new Date());
    /*
     * ins.setDuty(duty); ins.setStatus(status); ins.setAllowSubmitPub(allowSubmitPub);
     * ins.setIsIns(isIns);
     */
    psnInsDao.save(ins);

  }

  @Override
  public void updateTaskStatus(Long jobId, int status, String err) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatusById(jobId, status, err);
    } catch (Exception e) {
      logger.error("更新任务状态记录出错！jobId " + jobId, e);
    }
  }

}
