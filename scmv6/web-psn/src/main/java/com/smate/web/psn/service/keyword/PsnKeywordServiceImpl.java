package com.smate.web.psn.service.keyword;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dto.profile.KeywordIdentificationDTO;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.dao.profile.KeywordIdentificationDao;
import com.smate.web.psn.dao.profile.PsnDisciplineKeyDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.service.profile.PersonManager;

/**
 * 个人关键字（研究领域 ）服务接口
 * 
 * @author tsz
 *
 */
@Service("psnKeywordService")
@Transactional(rollbackFor = Exception.class)
public class PsnKeywordServiceImpl implements PsnKeywordService {


  @Autowired
  private PsnDisciplineKeyDao disciplineKeyDao;

  @Autowired
  private KeywordIdentificationDao identificationDao;

  @Autowired
  private PersonManager personManager;

  @Autowired
  private PersonProfileDao personProfileDao;

  /**
   * 获取人员 关键词 相关信息
   */
  @Override
  public List<KeywordIdentificationDTO> getPsnKeyWord(Long psnId, Integer resultSize) throws PsnException {
    List<KeywordIdentificationDTO> list = new ArrayList<KeywordIdentificationDTO>();
    List<Long> kwIdList = disciplineKeyDao.findPsnDiscKeyId(psnId, 1);

    if (CollectionUtils.isEmpty(kwIdList)) {
      return list;
    }


    List<Object[]> countMap = identificationDao.countIdentification(psnId, kwIdList);

    if (CollectionUtils.isNotEmpty(countMap)) {

      for (Object[] map : countMap) {
        kwIdList.remove(map[0]);
        KeywordIdentificationDTO form = new KeywordIdentificationDTO();
        Long kwId = (Long) map[0];
        Long count = (Long) map[1];

        PsnDisciplineKey key = disciplineKeyDao.findUniqueBy("id", kwId);
        String keyword = key == null ? null : key.getKeyWords();

        // 好友ID
        List<Long> friendIdList = identificationDao.findFriendId(psnId, kwId);

        List<Person> friendList = new ArrayList<Person>();

        if (CollectionUtils.isNotEmpty(friendIdList)) {
          for (Long friendId : friendIdList) {
            Person temp = this.personProfileDao.get(friendId);
            if (temp != null) {
              Person person = new Person();
              person.setPersonId(temp.getPersonId());
              person.setName(temp.getName());
              person.setAvatars(temp.getAvatars());
              person.setSex(temp.getSex());
              person.setName(temp.getName());
              person.setFirstName(temp.getFirstName());
              person.setLastName(temp.getLastName());

              // 无头像，设置默认头像
              if (StringUtils.isBlank(person.getAvatars())) {
                person.setAvatars(personManager.refreshRemoteAvatars(person.getPersonId(), person.getSex(), null));
              }

              Locale locale = LocaleContextHolder.getLocale();
              String name = null;
              if (locale.equals(Locale.US)) {
                name = person.getFirstName() == null && person.getLastName() == null ? person.getName()
                    : person.getFirstName() + " " + person.getLastName();
              } else {
                name = person.getName() == null ? person.getLastName() + " " + person.getFirstName() : person.getName();
              }
              person.setName(name);
              friendList.add(person);
            }
          }
        }

        form.setCount(count);
        form.setFriendList(friendList);
        form.setKeyword(keyword);
        form.setKeywordId(kwId);
        form.setPsnId(psnId);

        list.add(form);
      }
    }

    if (CollectionUtils.isNotEmpty(kwIdList)) {
      List<PsnDisciplineKey> onIdentifList = disciplineKeyDao.findPsnDisciplineKey(psnId, kwIdList, 1);
      List<Long> pdkIdList = null;
      if (CollectionUtils.isNotEmpty(onIdentifList)) {
        pdkIdList = new ArrayList<Long>();
        for (PsnDisciplineKey k : onIdentifList) {
          pdkIdList.add(k.getId());
          KeywordIdentificationDTO form = new KeywordIdentificationDTO();
          form.setCount(0l);
          form.setKeyword(k.getKeyWords());
          form.setKeywordId(k.getId());
          form.setPsnId(psnId);

          list.add(form);
        }
      }
    }
    return list;
  }



}
