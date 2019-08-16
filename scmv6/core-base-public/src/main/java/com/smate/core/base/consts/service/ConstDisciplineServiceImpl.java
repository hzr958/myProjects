package com.smate.core.base.consts.service;

import com.smate.core.base.consts.dao.ConstDisciplineDao;
import com.smate.core.base.consts.model.ConstDiscipline;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学科领域常量服务实现类
 * 
 * @author houchuanjie
 * @date 2018年3月19日 下午5:34:54
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ConstDisciplineServiceImpl implements ConstDisciplineService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstDisciplineDao constDisciplineDao;

  @Override
  public Optional<String> getDisciplineName(Long id, Locale locale) throws ServiceException {
    try {
      // 当且仅当存在学科领域
      return Optional.ofNullable(constDisciplineDao.getConstDisciplineById(id)).map(dis -> {
        String disName = dis.getDiscCode() + "-";
        disName += Locale.US.equals(locale) ? dis.getEnName() : dis.getZhName();
        //只要查询二级学
        if(dis.getDiscCode().length()>3){
          dis = constDisciplineDao.getConstDisciplineByDisCode(dis.getDiscCode().substring(0, 3));
          disName =  dis.getDiscCode() + "-";
          disName += Locale.US.equals(locale) ? dis.getEnName() : dis.getZhName();
        }
        return disName;
      });
    } catch (Exception e) {
      logger.error("获取学科领域名称错误.", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String findDiscJsonData(String discCode) throws ServiceException {
    try {

      List<String> searchKey = new ArrayList<String>();
      // 根据学科代码长度进行拆分
      switch (StringUtils.length(discCode)) {
        case 1:// 当前为1级学科代码，需要查询1级和2级的学科代码
          searchKey.add("");
          searchKey.add(discCode.substring(0, 1));
          break;
        case 3:// 当前为2级学科代码，需要查询1级、2级和3级的学科代码
          searchKey.add("");
          searchKey.add(discCode.substring(0, 1));
          searchKey.add(discCode.substring(0, 3));
          break;
        case 5:// 当前为3级学科代码，需要查询1级、2级、3级和4级的学科代码
          searchKey.add("");
          searchKey.add(discCode.substring(0, 1));
          searchKey.add(discCode.substring(0, 3));
          searchKey.add(discCode.substring(0, 5));
          break;
        case 7:// 当前为4级学科代码，无需查询
          break;
        default:// 查询1级学科代码
          searchKey.add("");
          break;
      }

      Map<String, List<Map<String, String>>> allData = new HashMap<String, List<Map<String, String>>>();

      for (String key : searchKey) {
        // 查找学科代码
        Optional<List<ConstDiscipline>> optDiscList = Optional.ofNullable(constDisciplineDao.findByDiscCode(key));
        // 将查询得到的学科代码列表List<ConstDiscipline>转换为List<Map<String,String>>
        List<Map<String, String>> mapList = optDiscList.map(list -> {
          return list.stream().map(cd -> {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", Objects.toString(cd.getId()));
            map.put("zh_CN_name", cd.getZhName());
            map.put("en_US_name", cd.getEnName());
            map.put("disc_code", cd.getDiscCode());
            return map;
          }).collect(Collectors.toList());
        }).orElseGet(Collections::emptyList);

        allData.put("discipline_code_" + (key == null ? 1 : (key.length() + 3) / 2), mapList);
      }

      // 返回json数据，格式
      return JacksonUtils.mapToJsonStr(allData);
    } catch (Exception e) {
      logger.error("通过discCode获取ID错误.", e);
      throw new ServiceException(e);
    }

  }


  @Override
  public Integer dealDisciplineId(Integer disciplineId)throws ServiceException {
    //处理历史数据
    if(disciplineId !=null){
      ConstDiscipline discipline = constDisciplineDao.get(Long.parseLong(disciplineId.toString()));
      if(discipline !=null && discipline.getDiscCode().length() == 5){
        disciplineId = Integer.parseInt(discipline.getSuperId().toString());
      }
    }
    return disciplineId;
  }
  @Override
  public Map<String,List<Map<String, String>>> findDiscData(Integer disciplineId) throws ServiceException {
    try {
      List<String> searchKey = new ArrayList<String>();
      //直接查询 一级，二级学科
      searchKey.add("");
      Locale locale = LocaleContextHolder.getLocale();
      Map<String, List<Map<String, String>>> allData = new HashMap<String, List<Map<String, String>>>();
      // 查找学科代码
      Optional<List<ConstDiscipline>> optDiscList = Optional.ofNullable(constDisciplineDao.findByDiscCode(""));
      List<Map<String, String>> disciplineList =  new ArrayList<>();
      // 将查询得到的学科代码列表List<ConstDiscipline>转换为List<Map<String,String>>
      List<Map<String, String>> mapList = optDiscList.map(list -> {
        return list.stream().map(cd -> {
          Map<String, String> map = new HashMap<String, String>();
          map.put("id", Objects.toString(cd.getId()));
          if(locale.equals(Locale.CHINA) ){
            map.put("zh_CN_name", cd.getZhName());
          }else{
            map.put("zh_CN_name", cd.getEnName());
          }
          map.put("disc_code", cd.getDiscCode());
          List<ConstDiscipline> byDiscCode = constDisciplineDao.findByDiscCode(cd.getDiscCode());
          if(CollectionUtils.isNotEmpty(byDiscCode)){
            List<Map<String, String>> mapList2 = new ArrayList<>();
            allData.put(cd.getDiscCode(),mapList2);
            for(ConstDiscipline  discipline : byDiscCode){
              Map<String, String> map2 = new HashMap<String, String>();
              if(disciplineId !=null && disciplineId.intValue() == discipline.getId().intValue()){
                map2.put("added","true");
                disciplineList.add(map2);
              }
              map2.put("id", Objects.toString(discipline.getId()));
              if(locale.equals(Locale.CHINA)){
                map2.put("zh_CN_name", discipline.getZhName());
              }else{
                map2.put("zh_CN_name", discipline.getEnName());
              }
              map2.put("disc_code",  discipline.getDiscCode());
              mapList2.add(map2);
            }
          }
          return map;
        }).collect(Collectors.toList());
      }).orElseGet(Collections::emptyList);
      allData.put("first",mapList);  //一级学科
      allData.put("disciplineList",disciplineList); //选中的学科
      // 返回json数据，格式
      return allData;
    } catch (Exception e) {
      logger.error("通过discCode获取ID错误.", e);
      throw new ServiceException(e);
    }

  }
}
