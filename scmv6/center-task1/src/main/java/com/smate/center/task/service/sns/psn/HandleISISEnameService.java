package com.smate.center.task.service.sns.psn;

import java.util.List;

import com.smate.core.base.utils.model.security.Person;


/**
 * 处理isis英文名字数据接口
 * 
 * @author zzx
 *
 */
public interface HandleISISEnameService {

  List<Long> findList(int batchSize) throws Exception;

  void handle(Person person) throws Exception;

  List<Person> findPsnList(List<Long> list) throws Exception;

  void update(Long personId, int i, String msg) throws Exception;

}
