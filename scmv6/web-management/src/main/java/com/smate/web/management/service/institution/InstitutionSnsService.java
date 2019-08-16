package com.smate.web.management.service.institution;

import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.security.Person;

public interface InstitutionSnsService {

  void save(InsPortal insPortal, Institution institution, Person person) throws Exception;

}
