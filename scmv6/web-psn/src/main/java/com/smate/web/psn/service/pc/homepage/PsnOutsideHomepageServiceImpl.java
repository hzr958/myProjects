package com.smate.web.psn.service.pc.homepage;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.psn.dao.merge.AccountsMergeDao;
import com.smate.web.psn.model.homepage.PersonProfileForm;

@Service
@Transactional
public class PsnOutsideHomepageServiceImpl implements PsnOutsideHomepageService {

  @Autowired
  private AccountsMergeDao accountsMergeDao;
  @Autowired
  private PersonDao personDao;

  @Override
  public boolean checkHasMerge(PersonProfileForm form) {
    if (Objects.isNull(form.getPsnInfo())) {
      if (NumberUtils.isNotNullOrZero(NumberUtils.toLong(form.getDes3PsnId()))
          && NumberUtils.isNullOrZero(form.getPsnId())) {
        form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
      }
      Long saveId = accountsMergeDao.findPsnIdByMergePsnId(form.getPsnId());
      if (saveId != null && saveId > 0L) {
        while (personDao.get(saveId) == null) {
          saveId = accountsMergeDao.findPsnIdByMergePsnId(saveId);
        }
        form.setPsnId(saveId);
        return true;
      }
    }
    return false;
  }

}
