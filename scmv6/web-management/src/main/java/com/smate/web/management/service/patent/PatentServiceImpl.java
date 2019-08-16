package com.smate.web.management.service.patent;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.management.model.patent.PatentForm;

/**
 * 专利相关数据获取业务实现类
 * 
 * @author LJ
 *
 */
@Service("patentService")
@Transactional(rollbackFor = Exception.class)
public class PatentServiceImpl implements PatentService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public String getPatentCompAnalysisdata(PatentForm form) throws Exception {
    String data = null;
    int mid = form.getmId();
    if (mid == 0) {
      data = "[" + "{value:335,name:\"监测系统 \"}," + "{value:163,name:\"大气\"}," + "{value:179,name:\"二氧化碳\"},"
          + "{value:109,name:\"污染土壤\"}," + "{value:150,name:\"地震\"}," + "]";
    }

    if (mid == 1) {

      data = "[" + "{value:1735,name:\"电机\"}," + "{value:1850,name:\"合金\"}," + "{value:1923,name:\"车\"},"
          + "{value:1694,name:\"纤维\"}," + "{value:910,name:\"烯\"}," + "]";
    }

    if (mid == 2) {
      data = "[" + "{value:254,name:\"管理系统\"}," + "{value:196,name:\"物联网\"}," + "{value:55,name:\"信息管理\"},"
          + "{value:231,name:\"管理方法\"}," + "{value:71,name:\"决策\"}," + "]";
    }

    if (mid == 3) {
      data = "[" + "{value:2314,name:\"电池\"}," + "{value:1856,name:\"催化剂\"}," + "{value:907,name:\"石墨\"},"
          + "{value:705,name:\"燃料\"}," + "{value:582,name:\"乙烯\"}," + "]";
    }

    if (mid == 4) {
      data = "[" + "{value:2028,name:\"基因\"}," + "{value:1363,name:\"生物薄膜\"}," + "{value:895,name:\"植物\"},"
          + "{value:742,name:\"病毒\"}," + "{value:553,name:\"发酵\"}," + "]";
    }

    if (mid == 5) {

      data = "[" + "{value:529,name:\"预测\"}," + "{value:563,name:\"识别方法\"}," + "{value:382,name:\"检测系统\"},"
          + "{value:382,name:\"评价方法\"}," + "{value:378,name:\"定位方法\"}," + "]";
    }

    if (mid == 6) {
      data = "[" + "{value:1871,name:\"数据\"}," + "{value:1651,name:\"图像\"}," + "{value:1007,name:\"通信\"},"
          + "{value:1051,name:\"三维图像\"}," + "{value:310,name:\"导航\"}," + "]";

    }

    if (mid == 7) {
      data = "[" + "{value:498,name:\"免疫\"  }," + "{value:514,name:\"肿瘤\"  }," + "{value:362,name:\"药物组合\"  },"
          + "{value:203,name:\"多肽\" }," + "{value:686,name:\"中药 \" }," + "]";
    }

    return data;
  }

  @Override
  public Map<String, String> getMatrixData(PatentForm form) throws Exception {
    int mid = form.getmId();
    String mdata = null;
    String planning = null;
    String measures = null;
    String max = null;
    Map<String, String> map = new HashMap<String, String>();
    if (mid == 0) {
      mdata = "[[0,0,102],[0,1,7],[0,2,104],[0,3,95],[0,4,8],[0,5,0],"
          + "[1,0,24],[1,1,15],[1,2,6],[1,3,37],[1,4,1],[1,5,39],"
          + "[2,0,34],[2,1,21],[2,2,9],[2,3,31],[2,4,24],[2,5,1],"
          + "[3,0,39],[3,1,38],[3,2,7],[3,3,6],[3,4,1],[3,5,11],"
          + "[4,0,40],[4,1,2],[4,2,33],[4,3,2],[4,4,34],[4,5,0],"
          + "[5,0,3],[5,1,68],[5,2,2],[5,3,60],[5,4,2],[5,5,84],"
          + "[6,0,10],[6,1,10],[6,2,21],[6,3,3],[6,4,51],[6,5,3]]";

      planning = "['监测系统','大气','二氧化碳','污染土壤','地震','海洋','地质']";

      measures = "['检测', '处理', '分析', '测量', '模拟', '保护']";

      max = "110";
    }

    else if (mid == 1) {
      mdata = "[[0,0,8],[0,1,79],[0,2,704],[0,3,0],[0,4,488],[0,5,398],"
          + "[1,0,624],[1,1,115],[1,2,26],[1,3,337],[1,4,708],[1,5,0],"
          + "[2,0,34],[2,1,321],[2,2,609],[2,3,31],[2,4,324],[2,5,291],"
          + "[3,0,39],[3,1,38],[3,2,7],[3,3,6],[3,4,1],[3,5,11],"
          + "[4,0,40],[4,1,2],[4,2,33],[4,3,2],[4,4,34],[4,5,0],"
          + "[5,0,3],[5,1,68],[5,2,2],[5,3,60],[5,4,2],[5,5,84],"
          + "[6,0,10],[6,1,10],[6,2,21],[6,3,3],[6,4,51],[6,5,3]]";
      planning = "['电机','合金','车','纤维','太阳能','陶瓷','石墨烯']";

      measures = "['方法','应用','装置','制备','材料','控制']";
      max = "710";
    } else if (mid == 2) {
      mdata = "[[0,0,158],[0,1,9],[0,2,34],[0,3,10],[0,4,0],[0,5,4],"
          + "[1,0,124],[1,1,5],[1,2,16],[1,3,3],[1,4,78],[1,5,25],"
          + "[2,0,164],[2,1,21],[2,2,69],[2,3,21],[2,4,124],[2,5,181],"
          + "[3,0,49],[3,1,8],[3,2,7],[3,3,0],[3,4,38],[3,5,21],"
          + "[4,0,24],[4,1,35],[4,2,10],[4,3,2],[4,4,34],[4,5,16],"
          + "[5,0,45],[5,1,36],[5,2,8],[5,3,0],[5,4,48],[5,5,58],"
          + "[6,0,10],[6,1,94],[6,2,81],[6,3,33],[6,4,61],[6,5,78]]";
      planning = " ['管理系统','物联网','管理方法','信息管理','资源管理','决策','调度方法']";

      measures = "['系统','方法','模型','模拟','智能','数据']";
      max = "190";

    } else if (mid == 3) {
      mdata = "[[0,0,1158],[0,1,201],[0,2,3],[0,3,1156],[0,4,2],[0,5,986],"
          + "[1,0,684],[1,1,780],[1,2,651],[1,3,310],[1,4,78],[1,5,1109],"
          + "[2,0,0],[2,1,841],[2,2,69],[2,3,721],[2,4,124],[2,5,161],"
          + "[3,0,419],[3,1,8],[3,2,7],[3,3,502],[3,4,0],[3,5,521],"
          + "[4,0,14],[4,1,365],[4,2,10],[4,3,421],[4,4,0],[4,5,16],"
          + "[5,0,45],[5,1,36],[5,2,18],[5,3,312],[5,4,0],[5,5,158],"
          + "[6,0,0],[6,1,94],[6,2,1],[6,3,333],[6,4,0],[6,5,8]]";

      planning = " ['电池','催化剂','石墨','燃料','乙烯','磷酸','氟']";

      measures = "['氧化', '材料', '活性', '应用', '纳米', '反应']";
      max = "1160";
    } else if (mid == 4) {

      mdata = "[[0,0,0],[0,1,1423],[0,2,334],[0,3,603],[0,4,0],[0,5,4],"
          + "[1,0,640],[1,1,542],[1,2,634],[1,3,302],[1,4,78],[1,5,25],"
          + "[2,0,16],[2,1,21],[2,2,691],[2,3,211],[2,4,12],[2,5,181],"
          + "[3,0,49],[3,1,325],[3,2,245],[3,3,68],[3,4,88],[3,5,21],"
          + "[4,0,324],[4,1,35],[4,2,210],[4,3,312],[4,4,4],[4,5,16],"
          + "[5,0,145],[5,1,136],[5,2,238],[5,3,98],[5,4,4],[5,5,18],"
          + "[6,0,126],[6,1,84],[6,2,181],[6,3,93],[6,4,51],[6,5,48]]";
      planning = "['基因','生物薄膜','植物','病毒','发酵','杆菌','微生物']";

      measures = "['制备', '检测', '应用', '方法', '结构', '蛋白']";
      max = "700";
    } else if (mid == 5) {

      mdata = "	[[0,0,258],[0,1,197],[0,2,0],[0,3,34],[0,4,25],[0,5,15],"
          + "[1,0,197],[1,1,106],[1,2,59],[1,3,98],[1,4,78],[1,5,25],"
          + "[2,0,26],[2,1,68],[2,2,176],[2,3,21],[2,4,36],[2,5,55],"
          + "[3,0,128],[3,1,63],[3,2,0],[3,3,0],[3,4,98],[3,5,93],"
          + "[4,0,163],[4,1,35],[4,2,10],[4,3,83],[4,4,34],[4,5,53],"
          + "[5,0,92],[5,1,56],[5,2,8],[5,3,0],[5,4,48],[5,5,42],"
          + "[6,0,36],[6,1,94],[6,2,0],[6,3,33],[6,4,61],[6,5,46]]";
      planning = "['预测','识别方法','检测系统','评价方法','定位方法','非线性','计算方法']";

      measures = "['系统', '分析', '检测', '实验', '性能', '技术']";
      max = "200";
    } else if (mid == 6) {

      mdata = "[[0,0,678],[0,1,394],[0,2,462],[0,3,337],[0,4,0],[0,5,0],"
          + "[1,0,522],[1,1,298],[1,2,188],[1,3,433],[1,4,0],[1,5,210],"
          + "[2,0,164],[2,1,21],[2,2,69],[2,3,21],[2,4,124],[2,5,181],"
          + "[3,0,399],[3,1,269],[3,2,217],[3,3,67],[3,4,0],[3,5,99],"
          + "[4,0,69],[4,1,35],[4,2,54],[4,3,87],[4,4,34],[4,5,31],"
          + "[5,0,58],[5,1,37],[5,2,98],[5,3,10],[5,4,23],[5,5,68],"
          + "[6,0,47],[6,1,56],[6,2,38],[6,3,33],[6,4,16],[6,5,78]]";
      planning = "['数据','图像','通信','三维图像','导航','移动终端','无线传感器']";

      measures = "['方法', '系统', '设备', '传输', '优化', '信号']";
      max = "680";
    } else if (mid == 7) {

      mdata = "[[0,0,8],[0,1,218],[0,2,134],[0,3,10],[0,4,65],[0,5,48],"
          + "[1,0,24],[1,1,269],[1,2,116],[1,3,65],[1,4,178],[1,5,225],"
          + "[2,0,174],[2,1,31],[2,2,19],[2,3,71],[2,4,4],[2,5,181],"
          + "[3,0,59],[3,1,38],[3,2,7],[3,3,0],[3,4,28],[3,5,61],"
          + "[4,0,424],[4,1,135],[4,2,0],[4,3,22],[4,4,4],[4,5,316],"
          + "[5,0,215],[5,1,36],[5,2,78],[5,3,26],[5,4,68],[5,5,18],"
          + "[6,0,0],[6,1,84],[6,2,0],[6,3,143],[6,4,0],[6,5,18]]";
      planning = "['免疫','肿瘤','药物组合','抗肿瘤药','中药','多肽','核磁共振']";

      measures = "['制备', '实验', '基因', '分析', '细胞', '辅助']";

      max = "430";
    }

    map.put("mdata", mdata);
    map.put("planning", planning);
    map.put("measures", measures);
    map.put("max", max);


    return map;
  }

}