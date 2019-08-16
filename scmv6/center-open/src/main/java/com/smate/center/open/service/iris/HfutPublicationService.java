package com.smate.center.open.service.iris;

import java.util.Map;

import com.smate.center.open.model.sie.publication.PublicationRol;
import com.smate.core.base.utils.model.Page;

/**
 * 合肥工业大学成果Service接口.
 * 
 * @author xys
 *
 */
public interface HfutPublicationService {

  public Page<PublicationRol> getPubs(Map<String, Object> paramsMap, Page<PublicationRol> page);
}
