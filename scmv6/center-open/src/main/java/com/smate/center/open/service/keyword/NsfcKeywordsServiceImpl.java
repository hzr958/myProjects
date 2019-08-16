package com.smate.center.open.service.keyword;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.nsfc.NsfcKwForSortingDao;
import com.smate.center.open.dao.nsfc.NsfcKwsTfCotfDao;
import com.smate.center.open.dao.nsfc.project.ProjectDataFiveYearDao;
import com.smate.center.open.model.nsfc.NsfcKwForRcmd;
import com.smate.center.open.model.nsfc.NsfcKwScoreForSorting;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.data.XmlUtil;

@Service("nsfcKeywordsService")
@Transactional(rollbackFor = Exception.class)
public class NsfcKeywordsServiceImpl implements NsfcKeywordsService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String[] filterKws = {"单元", "校准", "协同", "感知", "主观", "人为", "分类", "特色", "表达", "训练", "优化", "影响", "方法", "提高",
            "提升", "提供", "技术", "显示", "相关", "面向", "传输", "部件", "信号", "异常", "识别", "自动", "目标", "方案", "能量", "共享", "探索", "信息",
            "模糊", "获取", "探索", "环境", "优化", "空间", "下一代", "过程", "基础", "综合", "打印", "快速", "分析", "表情", "表征", "特性", "传输", "提升",
            "变化", "室内", "位置", "提取", "工程", "抑制", "数字", "构建", "筛选", "相关", "准确", "变换", "识别", "模拟", "改进", "技术", "影响", "综述",
            "品质", "模式", "响应", "地方", "强度", "检测", "互作", "定位", "设计", "增强", "社区", "中国", "研究", "协调", "原理", "北京", "界面", "验证",
            "原因", "发展", "合作", "转移", "机理", "机制", "分支", "制备", "控制", "合成", "功能", "模型", "调控", "模型", "功能", "评价", "学习", "预防",
            "性能", "修订", "效率", "诊断", "预测方法", "评估方法", "统一建模", "质量提升", "新理论", "算法研究", "问题研究", "实际应用", "应用研究", "新方法", "正确性",
            "系统性", "科学问题", "技术研究", "理论基础", "设计理论", "理论模型", "理论模拟", "基础理论", "学习理论", "理论分析", "理论与方法", "理论计算", "篇章理论",
            "系统研究", "研究热点", "方法研究", "研究方法", "研究成果", "其他研究", "实验研究", "机制研究", "功能研究", "机理研究", "跨学科交叉研究", "试验研究", "队列研究",
            "理论研究", "基础研究", "实证研究", "原位研究", "性能研究", "关联研究", "纵向研究", "比较研究", "定量研究", "对比研究", "战略研究", "前瞻性队列研究", "临床研究",
            "质性研究", "案例研究"};

    private String[] stopKws = {"able", "about", "above", "according", "accordingly", "across", "actually", "after",
            "afterwards", "again", "against", "ain't", "all", "allow", "allows", "almost", "alone", "along", "already",
            "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow",
            "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate",
            "are", "aren't", "around", "as", "a's", "aside", "ask", "asking", "associated", "at", "available", "away",
            "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand",
            "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both",
            "brief", "but", "by", "came", "can", "cannot", "cant", "can't", "cause", "causes", "certain", "certainly",
            "changes", "clearly", "c'mon", "co", "com", "come", "comes", "concerning", "consequently", "consider",
            "considering", "contain", "containing", "contains", "corresponding", "could", "couldn't", "course", "c's",
            "currently", "definitely", "described", "despite", "did", "didn't", "different", "do", "does", "doesn't",
            "doing", "done", "don't", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else",
            "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody",
            "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "fifth",
            "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from",
            "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got",
            "gotten", "greetings", "had", "hadn't", "happens", "hardly", "has", "hasn't", "have", "haven't", "having",
            "he", "hello", "help", "hence", "her", "here", "hereafter", "hereby", "herein", "here's", "hereupon",
            "hers", "herself", "he's", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit",
            "however", "i'd", "ie", "if", "ignored", "i'll", "i'm", "immediate", "in", "inasmuch", "inc", "indeed",
            "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isn't", "it",
            "it'd", "it'll", "its", "it's", "itself", "i've", "just", "keep", "keeps", "kept", "know", "known", "knows",
            "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "let's", "like", "liked",
            "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean",
            "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself",
            "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless",
            "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel",
            "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones",
            "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside",
            "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus",
            "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really",
            "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same",
            "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems",
            "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she",
            "should", "shouldn't", "since", "six", "so", "some", "somebody", "somehow", "someone", "something",
            "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying",
            "still", "sub", "such", "sup", "sure", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks",
            "thanx", "that", "thats", "that's", "the", "their", "theirs", "them", "themselves", "then", "thence",
            "there", "thereafter", "thereby", "therefore", "therein", "theres", "there's", "thereupon", "these", "they",
            "they'd", "they'll", "they're", "they've", "think", "third", "this", "thorough", "thoroughly", "those",
            "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward",
            "towards", "tried", "tries", "truly", "try", "trying", "t's", "twice", "two", "un", "under",
            "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses",
            "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasn't", "way",
            "we", "we'd", "welcome", "well", "we'll", "went", "were", "we're", "weren't", "we've", "what", "whatever",
            "what's", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "where's",
            "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "who's",
            "whose", "why", "will", "willing", "wish", "with", "within", "without", "wonder", "won't", "would",
            "wouldn't", "yes", "yet", "you", "you'd", "you'll", "your", "you're", "yours", "yourself", "yourselves",
            "you've", "zero", "zt", "ZT", "zz", "ZZ", "一", "一下", "一些", "一切", "一则", "一天", "一定", "一方面", "一旦", "一时", "一来",
            "一样", "一次", "一片", "一直", "一致", "一般", "一起", "一边", "一面", "万一", "上下", "上升", "上去", "上来", "上述", "上面", "下列", "下去",
            "下来", "下面", "不一", "不久", "不仅", "不会", "不但", "不光", "不单", "不变", "不只", "不可", "不同", "不够", "不如", "不得", "不怕", "不惟",
            "不成", "不拘", "不敢", "不断", "不是", "不比", "不然", "不特", "不独", "不管", "不能", "不要", "不论", "不足", "不过", "不问", "与", "与其",
            "与否", "与此同时", "专门", "且", "两者", "严格", "严重", "个", "个人", "个别", "中小", "中间", "丰富", "临", "为", "为主", "为了", "为什么",
            "为什麽", "为何", "为着", "主张", "主要", "举行", "乃", "乃至", "么", "之", "之一", "之前", "之后", "之後", "之所以", "之类", "乌乎", "乎",
            "乘", "也", "也好", "也是", "也罢", "了", "了解", "争取", "于", "于是", "于是乎", "云云", "互相", "产生", "人们", "人家", "什么", "什么样",
            "什麽", "今后", "今天", "今年", "今後", "仍然", "从", "从事", "从而", "他", "他人", "他们", "他的", "代替", "以", "以上", "以下", "以为",
            "以便", "以免", "以前", "以及", "以后", "以外", "以後", "以来", "以至", "以至于", "以致", "们", "任", "任何", "任凭", "任务", "企图", "伟大",
            "似乎", "似的", "但", "但是", "何", "何况", "何处", "何时", "作为", "你", "你们", "你的", "使得", "使用", "例如", "依", "依照", "依靠",
            "促进", "保持", "俺", "俺们", "倘", "倘使", "倘或", "倘然", "倘若", "假使", "假如", "假若", "做到", "像", "允许", "充分", "先后", "先後",
            "先生", "全部", "全面", "兮", "共同", "关于", "其", "其一", "其中", "其二", "其他", "其余", "其它", "其实", "其次", "具体", "具体地说",
            "具体说来", "具有", "再者", "再说", "冒", "冲", "决定", "况且", "准备", "几", "几乎", "几时", "凭", "凭借", "出去", "出来", "出现", "分别",
            "则", "别", "别的", "别说", "到", "前后", "前者", "前进", "前面", "加之", "加以", "加入", "加强", "十分", "即", "即令", "即使", "即便",
            "即或", "即若", "却不", "原来", "又", "及", "及其", "及时", "及至", "双方", "反之", "反应", "反映", "反过来", "反过来说", "取得", "受到", "变成",
            "另", "另一方面", "另外", "只是", "只有", "只要", "只限", "叫", "叫做", "召开", "叮咚", "可", "可以", "可是", "可能", "可见", "各", "各个",
            "各人", "各位", "各地", "各种", "各级", "各自", "合理", "同", "同一", "同时", "同样", "后来", "后面", "向", "向着", "吓", "吗", "否则", "吧",
            "吧哒", "吱", "呀", "呃", "呕", "呗", "呜", "呜呼", "呢", "周围", "呵", "呸", "呼哧", "咋", "和", "咚", "咦", "咱", "咱们", "咳",
            "哇", "哈", "哈哈", "哉", "哎", "哎呀", "哎哟", "哗", "哟", "哦", "哩", "哪", "哪个", "哪些", "哪儿", "哪天", "哪年", "哪怕", "哪样",
            "哪边", "哪里", "哼", "哼唷", "唉", "啊", "啐", "啥", "啦", "啪达", "喂", "喏", "喔唷", "嗡嗡", "嗬", "嗯", "嗳", "嘎", "嘎登", "嘘",
            "嘛", "嘻", "嘿", "因", "因为", "因此", "因而", "固然", "在", "在下", "地", "坚决", "坚持", "基本", "处理", "复杂", "多", "多少", "多数",
            "多次", "大力", "大多数", "大大", "大家", "大批", "大约", "大量", "失去", "她", "她们", "她的", "好的", "好象", "如", "如上所述", "如下", "如何",
            "如其", "如果", "如此", "如若", "存在", "宁", "宁可", "宁愿", "宁肯", "它", "它们", "它们的", "它的", "安全", "完全", "完成", "实现", "实际",
            "宣布", "容易", "密切", "对", "对于", "对应", "将", "少数", "尔后", "尚且", "尤其", "就", "就是", "就是说", "尽", "尽管", "属于", "岂但",
            "左右", "巨大", "巩固", "己", "已经", "帮助", "常常", "并", "并不", "并不是", "并且", "并没有", "广大", "广泛", "应当", "应用", "应该", "开外",
            "开始", "开展", "引起", "强烈", "强调", "归", "当", "当前", "当时", "当然", "当着", "形成", "彻底", "彼", "彼此", "往", "往往", "待", "後来",
            "後面", "得", "得出", "得到", "心里", "必然", "必要", "必须", "怎", "怎么", "怎么办", "怎么样", "怎样", "怎麽", "总之", "总是", "总的来看",
            "总的来说", "总的说来", "总结", "总而言之", "恰恰相反", "您", "意思", "愿意", "慢说", "成为", "我", "我们", "我的", "或", "或是", "或者", "战斗",
            "所", "所以", "所有", "所谓", "打", "扩大", "把", "抑或", "拿", "按", "按照", "换句话说", "换言之", "据", "掌握", "接着", "接著", "故",
            "故此", "整个", "方便", "方面", "旁人", "无宁", "无法", "无论", "既", "既是", "既然", "时候", "明显", "明确", "是", "是否", "是的", "显然",
            "显著", "普通", "普遍", "更加", "曾经", "替", "最后", "最大", "最好", "最後", "最近", "最高", "有", "有些", "有关", "有利", "有力", "有所",
            "有效", "有时", "有点", "有的", "有着", "有著", "望", "朝", "朝着", "本", "本着", "来", "来着", "极了", "构成", "果然", "果真", "某", "某个",
            "某些", "根据", "根本", "欢迎", "正在", "正如", "正常", "此", "此外", "此时", "此间", "毋宁", "每", "每个", "每天", "每年", "每当", "比",
            "比如", "比方", "比较", "毫不", "没有", "沿", "沿着", "注意", "深入", "清楚", "满足", "漫说", "焉", "然则", "然后", "然後", "然而", "照",
            "照着", "特别是", "特殊", "特点", "现代", "现在", "甚么", "甚而", "甚至", "用", "由", "由于", "由此可见", "的", "的话", "目前", "直到", "直接",
            "相似", "相信", "相反", "相同", "相对", "相对而言", "相应", "相当", "相等", "省得", "看出", "看到", "看来", "看看", "看见", "真是", "真正", "着",
            "着呢", "矣", "知道", "确定", "离", "积极", "移动", "突出", "突然", "立即", "第", "等", "等等", "管", "紧接着", "纵", "纵令", "纵使", "纵然",
            "练习", "组成", "经", "经常", "经过", "结合", "结果", "给", "绝对", "继续", "继而", "维持", "综上所述", "罢了", "考虑", "者", "而", "而且",
            "而况", "而外", "而已", "而是", "而言", "联系", "能", "能否", "能够", "腾", "自", "自个儿", "自从", "自各儿", "自家", "自己", "自身", "至",
            "至于", "良好", "若", "若是", "若非", "范围", "莫若", "获得", "虽", "虽则", "虽然", "虽说", "行为", "行动", "表明", "表示", "被", "要",
            "要不", "要不是", "要不然", "要么", "要是", "要求", "规定", "觉得", "认为", "认真", "认识", "让", "许多", "论", "设使", "设若", "该", "说明",
            "诸位", "谁", "谁知", "赶", "起", "起来", "起见", "趁", "趁着", "越是", "跟", "转动", "转变", "转贴", "较", "较之", "边", "达到", "迅速",
            "过", "过去", "过来", "运用", "还是", "还有", "这", "这个", "这么", "这么些", "这么样", "这么点儿", "这些", "这会儿", "这儿", "这就是说", "这时",
            "这样", "这点", "这种", "这边", "这里", "这麽", "进入", "进步", "进而", "进行", "连", "连同", "适应", "适当", "适用", "逐步", "逐渐", "通常",
            "通过", "造成", "遇到", "遭到", "避免", "那", "那个", "那么", "那么些", "那么样", "那些", "那会儿", "那儿", "那时", "那样", "那边", "那里",
            "那麽", "部分", "鄙人", "采取", "里面", "重大", "重新", "重要", "鉴于", "问题", "防止", "阿", "附近", "限制", "除", "除了", "除此之外", "除非",
            "随", "随着", "随著", "集中", "需要", "非但", "非常", "非徒", "靠", "顺", "顺着", "首先", "高兴", "是不是", "说说", "基于"};

    @Autowired
    private SolrIndexService solrIndexService;

    @Autowired
    private ProjectDataFiveYearDao projectDataFiveYearDao;
    @Autowired
    private NsfcKwForSortingDao nsfcKwForSortingDao;
    @Autowired
    private NsfcKwsTfCotfDao nsfcKwsTfCotfDao;

    @Override
    public Long getNsfcKwTf(String kw, String discode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getRelatedKw(String kw, String discode, String queryType) throws Exception {
        ArrayList<String> rsList = new ArrayList<String>();
        Map<String, Object> kwsMap = this.solrIndexService.queryRelatedKws(kw, discode, queryType);
        if (kwsMap == null || kwsMap.size() == 0) {
            return rsList;
        }
        Set<String> standardKwsStr = (Set<String>) kwsMap.get(SolrIndexSerivceImpl.RESULT_ITEMS);
        if (standardKwsStr == null || standardKwsStr.size() == 0) {
            return null;
        }
        Map<String, Long> facetMap = (Map<String, Long>) kwsMap.get(SolrIndexSerivceImpl.RESULT_FACET);
        HashSet<NsfcKwForRcmd> kwSet = new HashSet<NsfcKwForRcmd>();
        for (String str : standardKwsStr) {
            if (StringUtils.isEmpty(str) || str.length() < 3) {
                continue;
            }
            if (str.equalsIgnoreCase(kw)) {
                continue;
            }
            int flag = 0;
            for (String filter : filterKws) {
                if (str.indexOf(filter) >= 0) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 1) {
                continue;
            }
            Long cotf = facetMap.get(str);
            if (cotf == null || cotf <= 0) {
                continue;
            }
            Long tf = 0L;
            tf = this.solrIndexService.queryKwTf(str, discode, queryType);
            NsfcKwForRcmd nkfr = new NsfcKwForRcmd(discode, str, tf, cotf);
            kwSet.add(nkfr);
        }
        NsfcKwForRcmd[] newKws = kwSet.toArray(new NsfcKwForRcmd[kwSet.size()]);
        Arrays.sort(newKws);
        ArrayList<NsfcKwForRcmd> rsList1 = new ArrayList<NsfcKwForRcmd>();
        int size = 0;
        for (NsfcKwForRcmd it : newKws) {
            if (size > 50) {
                break;
            }
            rsList.add(it.getKw());
            rsList1.add(it);
            size++;
        }
        System.out.println(rsList1);
        return rsList;
    }

    @Override
    public List<String> getRelatedKwByHash(String kw, String discode, String queryType) throws Exception {
        ArrayList<String> rsList = new ArrayList<String>();
        Map<String, Object> kwsMap = this.solrIndexService.queryRelatedKwsByKwHash(kw, discode, queryType);
        if (kwsMap == null || kwsMap.size() == 0) {
            return rsList;
        }
        Set<String> standardKwsStr = (Set<String>) kwsMap.get(SolrIndexSerivceImpl.RESULT_ITEMS);
        if (standardKwsStr == null || standardKwsStr.size() == 0) {
            return null;
        }
        Map<String, Long> facetMap = (Map<String, Long>) kwsMap.get(SolrIndexSerivceImpl.RESULT_FACET);
        HashSet<NsfcKwForRcmd> kwSet = new HashSet<NsfcKwForRcmd>();
        for (String str : standardKwsStr) {
            if (StringUtils.isEmpty(str) || str.length() < 3) {
                continue;
            }
            if (str.equalsIgnoreCase(kw)) {
                continue;
            }
            int flag = 0;
            for (String filter : filterKws) {
                if (str.indexOf(filter) >= 0) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 1) {
                continue;
            }
            Long cotf = facetMap.get(str);
            if (cotf == null || cotf <= 0) {
                continue;
            }
            Long tf = 0L;
            tf = this.solrIndexService.queryKwTf(str, discode, queryType);
            NsfcKwForRcmd nkfr = new NsfcKwForRcmd(discode, str, tf, cotf);
            kwSet.add(nkfr);
        }
        NsfcKwForRcmd[] newKws = kwSet.toArray(new NsfcKwForRcmd[kwSet.size()]);
        Arrays.sort(newKws);
        ArrayList<NsfcKwForRcmd> rsList1 = new ArrayList<NsfcKwForRcmd>();
        int size = 0;
        for (NsfcKwForRcmd it : newKws) {
            if (size > 50) {
                break;
            }
            rsList.add(it.getKw());
            rsList1.add(it);
            size++;
        }
        System.out.println(rsList1);
        return rsList;
    }

    /**
     * 从项目中获取*中文关键词*，以标题提取关键词与自填关键词为基础，通过计算与摘要提取关键词cotf与tf排序，最终返回排序结果
     */
    @Override
    public Integer extractPrjKws(String title, String abstractStr, String kws, String category,
            List<HashMap<String, Object>> dataMap) throws Exception {
        Integer rs = 1;
        List<NsfcKwScoreForSorting> kwsSet = this.extractKwsByCotfRestrictToContentZh(title, kws, abstractStr);
        // List<NsfcKwScoreForSorting> kwsSet = this.extractKwsFromPubOrPrjForNsfc(title, kws, abstractStr,
        // category);
        if (kwsSet == null || kwsSet.size() <= 0) {
            rs = 2;
        } else {
            for (NsfcKwScoreForSorting kw : kwsSet) {
                if (StringUtils.isEmpty(kw.getKeywords())) {
                    continue;
                }
                HashMap<String, Object> mp = new HashMap<String, Object>();
                mp.put("keyword", kw.getKeywords());
                mp.put("score", kw.getPrjkwScore());
                // mp.put("wtf", kw.getRkwScore());
                dataMap.add(mp);
            }
        }
        return rs;
    }

    // 从标题，自填关键词中得到关键词，然后利用co-tf计算abstract中对应
    /*
     * private List<String> getPdwhPubKwsFromTitleAndAbsByCotf(String title, String pubAbstract, String
     * pubKw, String category) throws Exception { if (StringUtils.isBlank(title)) { } if
     * (StringUtils.isBlank(pubAbstract)) { } if (StringUtils.isBlank(pubKw)) { } List<String> rsList =
     * new ArrayList<String>(); String content = title + " " + pubAbstract; if
     * (StringUtils.isEmpty(content)) { return null; } Set<String> extractTitleKwStrings = new
     * TreeSet<String>(); Set<String> extractAbsKwStrings = new TreeSet<String>(); extractTitleKwStrings
     * = this.getRsSetNew(title); extractAbsKwStrings = this.getRsSetNew(pubAbstract); List<String>
     * kwList_1 = new ArrayList<String>(); for (String keyTitle : extractTitleKwStrings) {
     * kwList_1.add(keyTitle); }
     * 
     * if (StringUtils.isNotBlank(pubKw)) { pubKw = pubKw.replace("；", ";"); String[] kws =
     * pubKw.split(";"); if (kws != null || kws.length > 0) { for (String str : kws) { if
     * (StringUtils.isNotEmpty(str)) { str = StringUtils.trimToEmpty(str); str =
     * str.toLowerCase().replaceAll("\\s+", " "); extractTitleKwStrings.add(str); } } } } //
     * List<String> kwList = this.getExtractKwStringNew(extractTitleKwStrings, extractAbsKwStrings);
     * List<String> kwList = new ArrayList<String>(); List<String> titleKwList = new
     * ArrayList<String>(); // List<String> titleKwList = this.getExtractKwStringNew(null,
     * extractTitleKwStrings); for (String keyTitle : extractTitleKwStrings) {
     * titleKwList.add(keyTitle); } List<String> absKwList; Map<String, Double> absKwMap; //
     * nsfc项目需要使用学科代码限制 if (StringUtils.isNotEmpty(category)) { if (category.length() > 5) { //
     * 只限制到2级比如G0102 category = category.substring(0, 5); } absKwList = this.getExtractKwStringNew(null,
     * extractAbsKwStrings, category); absKwMap = this.getExtractKwStringNew1(null, extractAbsKwStrings,
     * category); } else { absKwList = this.getExtractKwStringNew(null, extractAbsKwStrings); absKwMap =
     * this.getExtractKwStringNew1(null, extractAbsKwStrings); } if
     * (CollectionUtils.isNotEmpty(titleKwList)) { kwList.addAll(titleKwList); }
     * 
     * if (CollectionUtils.isNotEmpty(titleKwList) && CollectionUtils.isNotEmpty(absKwList)) {
     * 
     * List<String> extractKws = this.projectDataFiveYearDao.getKwByTf(titleKwList, absKwList,
     * category); List<Map<String, Object>> rsMap =
     * this.projectDataFiveYearDao.getKwByCoTfCout(titleKwList, absKwList, category); int i = 0; for
     * (String kw : extractKws) { Map<String, Object> map = rsMap.get(i); Integer cotf = 0; if (map !=
     * null && map.size() > 0) { cotf = ((BigDecimal) map.get("COUNTS")).intValue(); } } if
     * (CollectionUtils.isNotEmpty(extractKws)) { kwList.addAll(extractKws); } }
     * 
     * if (kwList != null && kwList.size() > 0) { for (String str : kwList) { if
     * (StringUtils.isEmpty(str)) { continue; }
     * 
     * if (rsList.size() >= 25) { break; }
     * 
     * int flag = 1; for (String rskwStr : rsList) { if (rskwStr.length() > str.length()) { if
     * (rskwStr.indexOf(str) > -1) { flag = 0; break; } } else {
     * 
     * if (str.indexOf(rskwStr) > -1) { flag = 0; break; } } } if (flag == 1) { rsList.add(str); } } }
     * return rsList; }
     */


    // 计算项目不加权重，需要加入category限制
    /*
     * private List<String> getExtractKwStringNew(Set<String> titleSet, Set<String> absSet, String
     * category) { Set<String> rsSet = new TreeSet<String>(); List<NsfcKwTfCotfForSorting> rsList = new
     * ArrayList<NsfcKwTfCotfForSorting>(); List<String> rsStringList = new ArrayList<String>(); if
     * (titleSet != null && titleSet.size() > 0) { List<NsfcKwTfCotfForSorting> listTitle =
     * sortExtractKwStringNewNoWeight(titleSet, 1.5, category); if (listTitle != null &&
     * listTitle.size() > 0) { rsList.addAll(listTitle); // 如果在标题中出现过了，就不在abs中重复计算 if (absSet != null &&
     * absSet.size() > 0) { for (String str : absSet) { if (!titleSet.contains(str)) { rsSet.add(str); }
     * } } } } if (rsSet.size() > 0) { List<NsfcKwTfCotfForSorting> listAbstract =
     * sortExtractKwStringNewNoWeight(rsSet, 1.0, category); if (listAbstract != null &&
     * listAbstract.size() > 0) { rsList.addAll(listAbstract); } } else if (absSet != null &&
     * absSet.size() > 0) { List<NsfcKwTfCotfForSorting> listAbstract =
     * sortExtractKwStringNewNoWeight(absSet, 1.0, category); if (listAbstract != null &&
     * listAbstract.size() > 0) { rsList.addAll(listAbstract); } } if (rsList.size() > 0) {
     * Collections.sort(rsList); for (NsfcKwTfCotfForSorting nsfcKw : rsList) { if
     * (StringUtils.isEmpty(nsfcKw.getKeywords())) { continue; } rsStringList.add(nsfcKw.getKeywords());
     * } } return rsStringList; }
     * 
     * // 计算项目不加权重，需要加入category限制 private Map<String, Double> getExtractKwStringNew1(Set<String>
     * titleSet, Set<String> absSet, String category) { Set<String> rsSet = new TreeSet<String>();
     * List<NsfcKwTfCotfForSorting> rsList = new ArrayList<NsfcKwTfCotfForSorting>(); List<String>
     * rsStringList = new ArrayList<String>(); if (titleSet != null && titleSet.size() > 0) {
     * List<NsfcKwTfCotfForSorting> listTitle = sortExtractKwStringNewNoWeight(titleSet, 1.5, category);
     * if (listTitle != null && listTitle.size() > 0) { rsList.addAll(listTitle); //
     * 如果在标题中出现过了，就不在abs中重复计算 if (absSet != null && absSet.size() > 0) { for (String str : absSet) { if
     * (!titleSet.contains(str)) { rsSet.add(str); } } } } } if (rsSet.size() > 0) {
     * List<NsfcKwTfCotfForSorting> listAbstract = sortExtractKwStringNewNoWeight(rsSet, 1.0, category);
     * if (listAbstract != null && listAbstract.size() > 0) { rsList.addAll(listAbstract); } } else if
     * (absSet != null && absSet.size() > 0) { List<NsfcKwTfCotfForSorting> listAbstract =
     * sortExtractKwStringNewNoWeight(absSet, 1.0, category); if (listAbstract != null &&
     * listAbstract.size() > 0) { rsList.addAll(listAbstract); } } Map<String, Double> rsMap = new
     * HashMap<String, Double>(); if (rsList.size() > 0) { Collections.sort(rsList); for
     * (NsfcKwTfCotfForSorting nsfcKw : rsList) { if (StringUtils.isEmpty(nsfcKw.getKeywords())) {
     * continue; } rsMap.put(nsfcKw.getKeywords(), nsfcKw.getIsiTf()); } } return rsMap; }
     * 
     * private List<NsfcKwTfCotfForSorting> sortExtractKwStringNewNoWeight(Set<String> extractKwStrings,
     * Double type, String category) { List<NsfcKwTfCotfForSorting> rsSet = new
     * ArrayList<NsfcKwTfCotfForSorting>(); // List<Map<String, Object>> rsTitle =
     * nsfcKwForSortingDao.getKwsInfo(extractKwStrings); List<Map<String, Object>> rsTitle =
     * projectDataFiveYearDao.getKwsInfo(extractKwStrings, category); if (rsTitle != null &&
     * rsTitle.size() > 0) { for (Map<String, Object> map : rsTitle) { if (map.get("KW_STR") == null) {
     * continue; } String kwStr = (String) map.get("KW_STR"); Long tf = map.get("TF") == null ? 0L :
     * ((BigDecimal) map.get("TF")).longValue(); if (tf <= 1L) { continue; } Integer length =
     * map.get("KW_LENGTH") == null ? 0 : ((BigDecimal) map.get("KW_LENGTH")).intValue(); Integer kwType
     * = map.get("KW_TYPE") == null ? 0 : ((BigDecimal) map.get("KW_TYPE")).intValue();
     * NsfcKwTfCotfForSorting kw = new NsfcKwTfCotfForSorting(kwStr, length, tf * type);
     * 
     * if (kwType == 1) { kw = new NsfcKwTfCotfForSorting(kwStr, length, tf * type); } else { //
     * 非项目关键词，得分减少一半 kw = new NsfcKwTfCotfForSorting(kwStr, length, tf * type * 0.5); }
     * 
     * if (length == 1) { kw.setIsiTf(kw.getIsiTf() / 0.6); // continue; } rsSet.add(kw); } }
     * 
     * return rsSet; }
     * 
     * private Set<String> getRsSetNew(String content) { Set<String> extractKwStrings = new
     * TreeSet<String>(); if (StringUtils.isEmpty(content)) { return extractKwStrings; } if
     * (!XmlUtil.isChinese(content)) { content = content.toLowerCase().replaceAll("\\s+", "空格"); }
     * Result kwRs = DicAnalysis.parse(content); for (Term t : kwRs.getTerms()) { if (t == null) {
     * continue; } if ("nsfc_kw_discipline".equals(t.getNatureStr())) { if
     * (StringUtils.isNotEmpty(t.getName())) { String kw = t.getName().replaceAll("空格",
     * " ").replaceAll("\\s+", " ").trim(); if (StringUtils.isNotEmpty(kw) && kw.length() > 2) { if
     * (!XmlUtil.isChinese(kw) && kw.length() < 4) { continue; } // 只有不包含在字符串里边，才往里边加入
     * extractKwStrings.add(kw); } } } } return extractKwStrings; }
     * 
     * private List<String> getExtractKwStringNew(Set<String> titleSet, Set<String> absSet) {
     * Set<String> rsSet = new TreeSet<String>(); List<NsfcKwTfCotfForSorting> rsList = new
     * ArrayList<NsfcKwTfCotfForSorting>(); List<String> rsStringList = new ArrayList<String>(); if
     * (titleSet != null && titleSet.size() > 0) { List<NsfcKwTfCotfForSorting> listTitle =
     * sortExtractKwStringNew(titleSet, 1.5); if (listTitle != null && listTitle.size() > 0) {
     * rsList.addAll(listTitle); // 如果在标题中出现过了，就不在abs中重复计算 if (absSet != null && absSet.size() > 0) {
     * for (String str : absSet) { if (!titleSet.contains(str)) { rsSet.add(str); } } } } } if
     * (rsSet.size() > 0) { List<NsfcKwTfCotfForSorting> listAbstract = sortExtractKwStringNew(rsSet,
     * 1.0); if (listAbstract != null && listAbstract.size() > 0) { rsList.addAll(listAbstract); } }
     * else if (absSet != null && absSet.size() > 0) { List<NsfcKwTfCotfForSorting> listAbstract =
     * sortExtractKwStringNew(absSet, 1.0); if (listAbstract != null && listAbstract.size() > 0) {
     * rsList.addAll(listAbstract); } } if (rsList.size() > 0) { Collections.sort(rsList); for
     * (NsfcKwTfCotfForSorting nsfcKw : rsList) { if (StringUtils.isEmpty(nsfcKw.getKeywords())) {
     * continue; } rsStringList.add(nsfcKw.getKeywords()); } } return rsStringList; }
     * 
     * private List<NsfcKwTfCotfForSorting> sortExtractKwStringNew(Set<String> extractKwStrings, Double
     * type) { List<NsfcKwTfCotfForSorting> rsSet = new ArrayList<NsfcKwTfCotfForSorting>(); //
     * List<Map<String, Object>> rsTitle = nsfcKwForSortingDao.getKwsInfo(extractKwStrings);
     * List<Map<String, Object>> rsTitle = projectDataFiveYearDao.getKwsInfo(extractKwStrings); if
     * (rsTitle != null && rsTitle.size() > 0) { for (Map<String, Object> map : rsTitle) { if
     * (map.get("KW_STR") == null) { continue; } String kwStr = (String) map.get("KW_STR"); Long tf =
     * map.get("TF") == null ? 0L : ((BigDecimal) map.get("TF")).longValue(); Integer length =
     * map.get("KW_LENGTH") == null ? 0 : ((BigDecimal) map.get("KW_LENGTH")).intValue(); Integer kwType
     * = map.get("KW_TYPE") == null ? 0 : ((BigDecimal) map.get("KW_TYPE")).intValue();
     * NsfcKwTfCotfForSorting kw; if (kwType == 1) { kw = new NsfcKwTfCotfForSorting(kwStr, length, tf *
     * type); } else { // 非项目关键词，得分减少一半 kw = new NsfcKwTfCotfForSorting(kwStr, length, tf * type * 0.5);
     * } if (length == 1) { kw.setIsiTf(kw.getIsiTf() / 0.6); // continue; } rsSet.add(kw); } }
     * 
     * return rsSet; }
     * 
     * private Map<String, Double> getExtractKwStringNew1(Set<String> titleSet, Set<String> absSet) {
     * Set<String> rsSet = new TreeSet<String>(); List<NsfcKwTfCotfForSorting> rsList = new
     * ArrayList<NsfcKwTfCotfForSorting>(); List<String> rsStringList = new ArrayList<String>(); if
     * (titleSet != null && titleSet.size() > 0) { List<NsfcKwTfCotfForSorting> listTitle =
     * sortExtractKwStringNew(titleSet, 1.5); if (listTitle != null && listTitle.size() > 0) {
     * rsList.addAll(listTitle); // 如果在标题中出现过了，就不在abs中重复计算 if (absSet != null && absSet.size() > 0) {
     * for (String str : absSet) { if (!titleSet.contains(str)) { rsSet.add(str); } } } } } if
     * (rsSet.size() > 0) { List<NsfcKwTfCotfForSorting> listAbstract = sortExtractKwStringNew(rsSet,
     * 1.0); if (listAbstract != null && listAbstract.size() > 0) { rsList.addAll(listAbstract); } }
     * else if (absSet != null && absSet.size() > 0) { List<NsfcKwTfCotfForSorting> listAbstract =
     * sortExtractKwStringNew(absSet, 1.0); if (listAbstract != null && listAbstract.size() > 0) {
     * rsList.addAll(listAbstract); } } Map<String, Double> rsMap = new HashMap<String, Double>(); if
     * (rsList.size() > 0) { Collections.sort(rsList); for (NsfcKwTfCotfForSorting nsfcKw : rsList) { if
     * (StringUtils.isEmpty(nsfcKw.getKeywords())) { continue; } rsMap.put(nsfcKw.getKeywords(),
     * nsfcKw.getIsiTf()); } } return rsMap; }
     */

    // 从标题，自填关键词中得到英文关键词全部保留并计算TF，然后从ABSTRACT中获取关键词与TF
    private List<String> extractKwsByCotfRestrictToContentEn(String title, String pubKw, String pubAbstract)
            throws Exception {
        List<String> rsList = new ArrayList<String>();
        String content = title + " " + pubAbstract;
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        Map<String, Integer> rsKwMap = new HashMap<String, Integer>();
        Map<String, Object> extractTitleKwMap = new HashMap<String, Object>();
        Map<String, Object> extractAbsKwMap = new HashMap<String, Object>();
        Set<String> extractTitleKwStrings = new TreeSet<String>();
        Set<String> extractAbsKwStrings = new TreeSet<String>();
        extractTitleKwMap = this.extractKwAndTfInContent(title);
        extractAbsKwMap = this.extractKwAndTfInContent(pubAbstract);
        extractTitleKwStrings = (Set<String>) extractTitleKwMap.get("EXTRACT_KWS_STRSETS");
        extractAbsKwStrings = (Set<String>) extractAbsKwMap.get("EXTRACT_KWS_STRSETS");
        if (StringUtils.isNotBlank(pubKw)) {
            pubKw = pubKw.replace("；", ";");
            String[] kws = pubKw.split(";");
            if (kws != null || kws.length > 0) {
                for (String str : kws) {
                    if (StringUtils.isNotEmpty(str)) {
                        str = StringUtils.trimToEmpty(str);
                        str = str.toLowerCase().replaceAll("\\s+", " ");
                        extractTitleKwStrings.add(str);
                        // 合并标题与自填关键词tf
                        Integer tf = extractTitleKwMap.get(str) == null ? 0 : (Integer) extractTitleKwMap.get(str);
                        if (0 == tf) {
                            extractTitleKwMap.put(str, 1);
                        } else {
                            tf = tf + 1;
                            extractTitleKwMap.put(str, tf);
                        }
                    }
                }
            }
        }
        // 存储所有提取到的关键词，并保留相应排序
        List<String> kwList = new ArrayList<String>();
        // 存储标题与自填关键词
        List<String> titleKwList = new ArrayList<String>();
        for (String keyTitle : extractTitleKwStrings) {
            titleKwList.add(keyTitle);
        }
        if (CollectionUtils.isNotEmpty(titleKwList)) {
            for (String kw : titleKwList) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                Integer tfInt = extractTitleKwMap.get(kw) == null ? 0 : (Integer) extractTitleKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长从4到9，加强TF为6倍;
                if (language == 0 && kw.length() >= 4 && kw.length() <= 9) {
                    tfInt = tfInt * 5;
                } else if (language == 0 && kw.length() == 3) {
                    tfInt = tfInt * 3;
                }
                rsKwMap.put(kw, tfInt * 100000000);
            }
        }

        if (CollectionUtils.isNotEmpty(extractTitleKwStrings) && CollectionUtils.isNotEmpty(extractAbsKwStrings)) {
            List<String> absKwList = new ArrayList<String>();
            for (String kw : extractAbsKwStrings) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                // 在此处0为自填关键词
                Integer tfInt = extractAbsKwMap.get(kw) == null ? 0 : (Integer) extractAbsKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长从4到9，加强TF为6倍;
                if (language == 0 && kw.length() >= 4 && kw.length() <= 9) {
                    tfInt = tfInt * 5;
                } else if (language == 0 && kw.length() == 3) {
                    tfInt = tfInt * 3;
                }
                // 当标题与自填关键词中有对应关键词，则提取出来
                if (!extractTitleKwStrings.contains(kw)) {
                    absKwList.add(kw);
                }
                // 合并标题自填关键词与摘要关键词tf
                Integer tf = rsKwMap.get(kw) == null ? 0 : (Integer) rsKwMap.get(kw);
                if (0 == tf) {
                    rsKwMap.put(kw, tfInt);
                } else {
                    tf = tf + tfInt;
                    rsKwMap.put(kw, tf);
                }
            }

            if (absKwList.size() > 0) {
                List<String> extractKws = this.nsfcKwForSortingDao.getKwByTfExcludePubKw(titleKwList, absKwList, 2);
                if (extractKws != null && extractKws.size() > 0) {
                    List<Map<String, Object>> rsMap =
                            this.nsfcKwForSortingDao.getKwByCoTfCoutExcludePubKw(titleKwList, titleKwList, 2);
                    int i = 0;
                    for (String kw : extractKws) {
                        Map<String, Object> map = rsMap.get(i);
                        Integer cotf = 0;
                        if (map != null && map.size() > 0) {
                            cotf = ((BigDecimal) map.get("COUNTS")).intValue();
                        }
                        // 合并标题自填关键词与摘要关键词cotf得分
                        Integer tf = rsKwMap.get(kw) == null ? 0 : (Integer) rsKwMap.get(kw);
                        if (0 == tf) {
                            rsKwMap.put(kw, cotf * 1000);
                        } else {
                            tf = tf + cotf * 1000;
                            rsKwMap.put(kw, tf);
                        }
                        i++;
                    }
                }
            }
        }
        if (rsKwMap.size() > 0) {
            List<NsfcKwScoreForSorting> sortList = new ArrayList<NsfcKwScoreForSorting>();
            for (Entry<String, Integer> et : rsKwMap.entrySet()) {
                if (StringUtils.isEmpty(et.getKey())) {
                    continue;
                }
                NsfcKwScoreForSorting nkfs =
                        new NsfcKwScoreForSorting(et.getKey(), et.getKey().length(), et.getValue().longValue());
                sortList.add(nkfs);
            }
            Collections.sort(sortList);
        }
        return kwList;
    }

    private List<NsfcKwScoreForSorting> extractKwsByCotfRestrictToContentZh(String title, String pubKw,
            String pubAbstract) throws Exception {
        List<NsfcKwScoreForSorting> rsList = new ArrayList<NsfcKwScoreForSorting>();
        String content = title + " " + pubKw + " " + pubAbstract;
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        Map<String, Long> rsKwMap = new HashMap<String, Long>();
        Map<String, Object> extractTitleKwMap = new HashMap<String, Object>();
        Map<String, Object> extractAbsKwMap = new HashMap<String, Object>();
        Set<String> extractTitleKwStrings = new TreeSet<String>();
        Set<String> extractAbsKwStrings = new TreeSet<String>();
        extractTitleKwMap = this.extractKwAndTfInContent(title);
        extractAbsKwMap = this.extractKwAndTfInContent(pubAbstract);
        if (extractTitleKwMap.get("EXTRACT_KWS_STRSETS") != null) {
            extractTitleKwStrings = (Set<String>) extractTitleKwMap.get("EXTRACT_KWS_STRSETS");
        }
        if (extractAbsKwMap.get("EXTRACT_KWS_STRSETS") != null) {
            extractAbsKwStrings = (Set<String>) extractAbsKwMap.get("EXTRACT_KWS_STRSETS");
        }
        if (StringUtils.isNotBlank(pubKw)) {
            pubKw = pubKw.replace("；", ";");
            String[] kws = pubKw.split(";");
            if (kws != null || kws.length > 0) {
                for (String str : kws) {
                    if (StringUtils.isNotEmpty(str)) {
                        str = StringUtils.trimToEmpty(str);
                        str = str.toLowerCase().replaceAll("\\s+", " ");
                        extractTitleKwStrings.add(str);
                        // 合并标题与自填关键词tf
                        Integer tf = extractTitleKwMap.get(str) == null ? 0 : (Integer) extractTitleKwMap.get(str);
                        if (0 == tf) {
                            extractTitleKwMap.put(str, 1);
                        } else {
                            tf = tf + 1;
                            extractTitleKwMap.put(str, tf);
                        }
                    }
                }
            }
        }
        // 存储标题与自填关键词
        List<String> titleKwList = new ArrayList<String>();
        for (String keyTitle : extractTitleKwStrings) {
            titleKwList.add(keyTitle);
        }
        if (CollectionUtils.isNotEmpty(titleKwList)) {
            for (String kw : titleKwList) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                Integer tfInt = extractTitleKwMap.get(kw) == null ? 0 : (Integer) extractTitleKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长从4到9，加强TF为6倍;
                if (language == 0 && kw.length() >= 4 && kw.length() <= 9) {
                    tfInt = tfInt * 5;
                } else if (language == 0 && kw.length() == 3) {
                    tfInt = tfInt * 3;
                } else if (language == 1) {
                    Integer lengthEn = kw.split(" ").length;
                    if (lengthEn > 1 && lengthEn <= 6) {
                        tfInt = tfInt * 3;
                    }
                }
                rsKwMap.put(kw, tfInt * 100000000L);
            }
        }

        if (CollectionUtils.isNotEmpty(extractAbsKwStrings)) {
            List<String> absKwList = new ArrayList<String>();
            for (String kw : extractAbsKwStrings) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                // 在此处0为自填关键词
                Long tfInt = extractAbsKwMap.get(kw) == null ? 0L : ((Integer) extractAbsKwMap.get(kw)).longValue();
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长从4到9，加强TF为6倍;
                if (language == 0 && kw.length() >= 4 && kw.length() <= 9) {
                    tfInt = tfInt * 5;
                } else if (language == 0 && kw.length() == 3) {
                    tfInt = tfInt * 3;
                } else if (language == 1) {
                    Integer lengthEn = kw.split(" ").length;
                    if (lengthEn > 1 && lengthEn <= 6) {
                        tfInt = tfInt * 3;
                    }
                }
                // 当标题与自填关键词中有对应关键词，则提取出来
                if (!extractTitleKwStrings.contains(kw)) {
                    absKwList.add(kw);
                }
                // 合并标题自填关键词与摘要关键词tf
                Long tf = rsKwMap.get(kw) == null ? 0L : (Long) rsKwMap.get(kw);
                if (0L == tf) {
                    rsKwMap.put(kw, tfInt);
                } else {
                    tf = tf + tfInt;
                    rsKwMap.put(kw, tf);
                }
            }
            /*
             * //这个地方有些疑惑，暂时先注释 if (absKwList.size() > 0 && titleKwList.size() > 0) { List<String> extractKws =
             * this.nsfcKwForSortingDao.getKwByTfExcludePubKw(titleKwList, absKwList, 2); if (extractKws != null
             * && extractKws.size() > 0) { List<Map<String, Object>> rsMap =
             * this.nsfcKwForSortingDao.getKwByCoTfCoutExcludePubKw(titleKwList, absKwList, 2); int i = 0; for
             * (String kw : extractKws) { Map<String, Object> map = rsMap.get(i); Integer cotf = 0; if (map !=
             * null && map.size() > 0) { cotf = ((BigDecimal) map.get("COUNTS")).intValue(); } //
             * 合并标题自填关键词与摘要关键词cotf得分 Long tf = rsKwMap.get(kw) == null ? 0L : (Long) rsKwMap.get(kw); if (0 ==
             * tf) { rsKwMap.put(kw, cotf * 10000L); } else { tf = tf + cotf * 10000L; rsKwMap.put(kw, tf); }
             * i++; } } }
             */
            calculateCoTf(titleKwList, absKwList, rsKwMap, 1);// 计算项目标题和摘要中关键词（英文-英文）关系的cotf
            calculateCoTf(titleKwList, absKwList, rsKwMap, 2);// 计算项目标题和摘要中关键词（中文-中文）关系的cotf
        }
        if (rsKwMap.size() > 0) {
            for (Entry<String, Long> et : rsKwMap.entrySet()) {
                if (StringUtils.isEmpty(et.getKey())) {
                    continue;
                }
                NsfcKwScoreForSorting nkfs =
                        new NsfcKwScoreForSorting(et.getKey(), et.getKey().length(), et.getValue());
                rsList.add(nkfs);
            }
            Collections.sort(rsList);
        }
        return rsList;
    }

    // pubKw需要用分号分割，权重公式wTF=TF自填 +TF标题 +TF摘要 [TF自填=TF标题 =1, 读取摘要中与自填/标题关键词相关的关键词, TF摘要=0.33*关键词个数，最大=1]
    @Override
    public List<NsfcKwScoreForSorting> extractKwsFromPubOrPrjForNsfc(String title, String pubKw, String pubAbstract,
            String nsfcCategory) {
        List<NsfcKwScoreForSorting> rsList = new ArrayList<NsfcKwScoreForSorting>();
        Map<String, Object> titleKwsTf = extractKwAndTfInContent(title);
        Map<String, Object> absKwsTf = extractKwAndTfInContent(pubAbstract);
        Set<String> extractTitleKwStrings = new TreeSet<String>();
        Set<String> extractAbsKwStrings = new TreeSet<String>();
        Set<String> selfKwStrings = new TreeSet<String>();
        if (titleKwsTf.get("EXTRACT_KWS_STRSETS") != null) {
            extractTitleKwStrings = (Set<String>) titleKwsTf.get("EXTRACT_KWS_STRSETS");
        }
        if (absKwsTf.get("EXTRACT_KWS_STRSETS") != null) {
            extractAbsKwStrings = (Set<String>) absKwsTf.get("EXTRACT_KWS_STRSETS");
        }
        this.mergeTitleKwsAndSelfKws(pubKw, extractTitleKwStrings, titleKwsTf, selfKwStrings);
        // 通过知识库查找摘要中与标题和自填关键词相关关键词，并标记
        List<String> firstKws = new ArrayList<String>(extractTitleKwStrings);
        List<String> secondKws = new ArrayList<String>();
        // 区分与标题一样的关键词
        for (String kw : extractAbsKwStrings) {
            if (!(extractTitleKwStrings.contains(kw))) {
                secondKws.add(kw);
            }
        }
        List<String> absRelatedKws = this.nsfcKwsTfCotfDao.getRelatedKwsBasedOnCotf(firstKws, secondKws, nsfcCategory);
        // 加入标题与自填关键词
        if (extractTitleKwStrings != null && extractTitleKwStrings.size() > 0) {
            for (String titleKw : extractTitleKwStrings) {
                Integer ttf = titleKwsTf.get(titleKw) == null ? 0 : (Integer) titleKwsTf.get(titleKw);
                Integer atf = absKwsTf.get(titleKw) == null ? 0 : (Integer) absKwsTf.get(titleKw);
                if (ttf == 0 && atf == 0) {
                    continue;
                }
                Double aScore = atf * 0.33d;
                if (aScore.compareTo(1.00d) > 0) {
                    aScore = 1.00d;
                }
                // 计算得分
                aScore = aScore + ttf;
                Long rtf = new BigDecimal(aScore * 100000).longValue();
                // 标记类别
                Integer type = 1;
                if (selfKwStrings.contains(titleKw)) {
                    type = 2;
                }
                NsfcKwScoreForSorting nksf = new NsfcKwScoreForSorting(titleKw, titleKw.length(), type, rtf, aScore);
                rsList.add(nksf);
            }
        }

        if (extractAbsKwStrings != null && extractAbsKwStrings.size() > 0) {
            for (String absKw : extractAbsKwStrings) {
                Integer atf = absKwsTf.get(absKw) == null ? 0 : (Integer) absKwsTf.get(absKw);
                if (atf == 0 || extractTitleKwStrings.contains(absKw)) {
                    continue;
                }
                Double aScore = atf * 0.33d;
                if (aScore.compareTo(1.00d) > 0) {
                    aScore = 1.00d;
                }
                Long rtf = new BigDecimal(aScore * 100).longValue();
                // 标记类别
                Integer type = 4;
                if (absRelatedKws.contains(absKw)) {
                    type = 3;
                    rtf = new BigDecimal(aScore * 1000).longValue();
                }
                NsfcKwScoreForSorting nksf = new NsfcKwScoreForSorting(absKw, absKw.length(), type, rtf, aScore);
                rsList.add(nksf);
            }
        }

        Collections.sort(rsList);
        return rsList;
    }


    /**
     * 计算关键词的cotf
     * 
     * @param titleKwList titleKwList 标题关键词列表
     * @param absKwList 摘要关键词列表
     * @param rsKwMap
     * @param language 语言（1为外文，2中文）
     */
    private void calculateCoTf(List<String> titleKwList, List<String> absKwList, Map<String, Long> rsKwMap,
            int language) {
        if (absKwList.size() > 0 && titleKwList.size() > 0) {
            List<Map<String, Object>> rsMap =
                    this.nsfcKwForSortingDao.getKwByCoTfCoutExcludePubKw(titleKwList, absKwList, language);
            if (CollectionUtils.isNotEmpty(rsMap)) {
                for (Map<String, Object> map : rsMap) {
                    if (map != null && map.size() > 0) {
                        String kw = map.get("KW") == null ? null : map.get("KW").toString();// 获取关键词字符
                        if (StringUtils.isNotBlank(kw)) {
                            Integer cotf = ((BigDecimal) map.get("COUNTS")).intValue();
                            // 合并标题自填关键词与摘要关键词cotf得分
                            Long tf = rsKwMap.get(kw) == null ? 0L : (Long) rsKwMap.get(kw);
                            if (0 == tf) {
                                rsKwMap.put(kw, cotf * 10000L);
                            } else {
                                tf = tf + cotf * 10000L;
                                rsKwMap.put(kw, tf);
                            }
                        }
                    }
                }
            }
        }
    }

    private void mergeTitleKwsAndSelfKws(String pubKw, Set<String> extractTitleKwStrings, Map<String, Object> titleKws,
            Set<String> selfKwStrings) {
        if (StringUtils.isBlank(pubKw)) {
            return;
        }
        // 合并标题与自填关键词
        pubKw = pubKw.replace("；", ";");
        String[] kws = pubKw.split(";");
        if (kws != null && kws.length > 1) {
            for (String str : kws) {
                if (StringUtils.isNotEmpty(str)) {
                    str = StringUtils.trimToEmpty(str);
                    str = str.toLowerCase().replaceAll("\\s+", " ");
                    extractTitleKwStrings.add(str);
                    // 合并标题与自填关键词tf
                    Integer tf = titleKws.get(str) == null ? 0 : (Integer) titleKws.get(str);
                    if (0 == tf) {
                        titleKws.put(str, 1);
                        // 在标题里边已经出现的自填关键词，就算入标题关键词。只标记未出现的关键词
                        selfKwStrings.add(str);
                    } else {
                        // 公式tf最多不超过1
                        if (tf > 1) {
                            tf = 1;
                        }
                        tf = tf + 1;
                        titleKws.put(str, tf);
                    }
                }
            }
        }
    }

    private List<String> markAbsRelatedKws(Set<String> extractTitleKwStrings, Set<String> extractAbsKwStrings,
            String nsfcCategory) {
        // 去重
        List<String> firstKws = new ArrayList<String>(extractTitleKwStrings);
        List<String> secondKws = new ArrayList<String>();
        for (String kw : extractAbsKwStrings) {
            if (!(firstKws.contains(kw))) {
                secondKws.add(kw);
            }
        }
        List<String> relatedKws = this.nsfcKwsTfCotfDao.getRelatedKwsBasedOnCotf(firstKws, secondKws, nsfcCategory);
        return relatedKws;
    }

    private Map<String, Object> extractKwAndTfInContentEn(String content) {
        Set<String> extractKwStrings = new TreeSet<String>();
        Map<String, Object> tfMp = new HashMap<String, Object>();
        if (StringUtils.isEmpty(content)) {
            return tfMp;
        }
        content = content.toLowerCase().replaceAll("\\s+", "空格");
        Result kwRs = DicAnalysis.parse(content);
        for (Term t : kwRs.getTerms()) {
            if (t == null) {
                continue;
            }
            if ("nsfc_kw_discipline".equals(t.getNatureStr())) {
                if (StringUtils.isNotEmpty(t.getName())) {
                    String kw = t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim();
                    if (StringUtils.isNotEmpty(kw) && kw.length() > 1) {
                        if (!XmlUtil.isChinese(kw) && kw.length() < 3) {
                            continue;
                        }
                        // 只有不包含在字符串里边，才往里边加入
                        extractKwStrings.add(kw);
                        Integer tf = t.natrue().allFrequency;
                        Integer ltf = (Integer) tfMp.get(kw);
                        if (ltf == null || 0 == ltf) {
                            tfMp.put(kw, tf);
                        } else {
                            tf = tf + ltf;
                            tfMp.put(kw, tf);
                        }
                    }
                }
            }
            tfMp.put("EXTRACT_KWS_STRSETS", extractKwStrings);
        }
        return tfMp;
    }

    private Map<String, Object> extractKwAndTfInContent(String content) {
        Set<String> extractKwStrings = new TreeSet<String>();
        Map<String, Object> tfMp = new HashMap<String, Object>();
        if (StringUtils.isEmpty(content)) {
            return tfMp;
        }
        if (!XmlUtil.isChinese(content)) {
            content = content.toLowerCase().replaceAll("\\s+", "空格");
        }
        Result kwRs = DicAnalysis.parse(content);
        for (Term t : kwRs.getTerms()) {
            if (t == null) {
                continue;
            }
            if ("nsfc_kw_discipline".equals(t.getNatureStr())) {
                if (StringUtils.isNotEmpty(t.getName())) {
                    String kw = t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim();
                    if (StringUtils.isNotEmpty(kw) && kw.length() > 1) {
                        if (!XmlUtil.isChinese(kw) && kw.length() < 3) {
                            continue;
                        }
                        // 只有不包含在字符串里边，才往里边加入
                        extractKwStrings.add(kw);
                        Integer tf = t.natrue().allFrequency;
                        Integer ltf = (Integer) tfMp.get(kw);
                        if (ltf == null || 0 == ltf) {
                            tfMp.put(kw, tf);
                        } else {
                            tf = tf + ltf;
                            tfMp.put(kw, tf);
                        }
                    }
                }
            }
            tfMp.put("EXTRACT_KWS_STRSETS", extractKwStrings);
        }
        return tfMp;
    }

    public Map<String, Object> getKwsCotfInfo(List<String> psnKws, List<String> prjKws) {
        if (psnKws == null || prjKws == null || psnKws.size() <= 0 || prjKws.size() <= 0) {
            return null;
        }
        Collections.sort(psnKws);
        Collections.sort(prjKws);
        ArrayList<String> commonKws = this.getLongestCommonKwSequenceList(psnKws, prjKws);

        return null;
    }

    public List<Map<String, Object>> getListKwsCotfBySize(Integer size, List<String> kwList, String discode,
            Integer language) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        Collections.sort(kwList);
        List<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>();
        switch (size) {
            case 2:
                rsList = this.getSubsetsStrLengthTwo(kwList, discode, language);
                return rsList;
            case 3:
                rsList = this.getSubsetsStrLengthThree(kwList, discode, language);
                return rsList;
            case 4:
                rsList = this.getSubsetsStrLengthFour(kwList, discode, language);
                return rsList;
            case 5:
                rsList = this.getSubsetsStrLengthFive(kwList, discode, language);
                return rsList;
            default:
                return null;
        }
    }

    /*
     * 计算长度为5的子集,需要提前去重排序
     * 
     */
    private List<Map<String, Object>> getSubsetsStrLengthFive(List<String> kwList, String discode, Integer language) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        Integer length = kwList.size();
        List<String> sbList = new ArrayList<String>();
        for (int k = 0; k < length - 4; k++) {
            for (int k1 = k + 1; k1 < length - 3; k1++) {
                for (int k2 = k1 + 1; k2 < length - 2; k2++) {
                    for (int k3 = k2 + 1; k3 < length - 1; k3++) {
                        for (int k4 = k3 + 1; k4 < length; k4++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(kwList.get(k)).append(";").append(kwList.get(k1)).append(";")
                                    .append(kwList.get(k2)).append(";").append(kwList.get(k3)).append(";")
                                    .append(kwList.get(k4));
                            sbList.add(sb.toString());
                        }
                    }
                }
            }
        }
        List<Map<String, Object>> rsMap = new ArrayList<Map<String, Object>>();
        if (StringUtils.isNotBlank(discode)) {
            rsMap = this.nsfcKwsTfCotfDao.getKwsCotfSizeFive(sbList, discode, language);
        } else {
            rsMap = this.nsfcKwsTfCotfDao.getKwsCotfSizeFiveWithoutDiscode(sbList, language);
        }
        return rsMap;
    }

    /*
     * 计算长度为4的子集,需要提前去重排序
     * 
     */
    private List<Map<String, Object>> getSubsetsStrLengthFour(List<String> kwList, String discode, Integer language) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        Integer length = kwList.size();
        List<String> sbList = new ArrayList<String>();
        for (int k = 0; k < length - 3; k++) {
            for (int k1 = k + 1; k1 < length - 2; k1++) {
                for (int k2 = k1 + 1; k2 < length - 1; k2++) {
                    for (int k3 = k2 + 1; k3 < length; k3++) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(kwList.get(k)).append(";").append(kwList.get(k1)).append(";").append(kwList.get(k2))
                                .append(";").append(kwList.get(k3));
                        sbList.add(sb.toString());
                    }
                }
            }
        }
        List<Map<String, Object>> rsMap = new ArrayList<Map<String, Object>>();
        if (StringUtils.isNotBlank(discode)) {
            rsMap = this.nsfcKwsTfCotfDao.getKwsCotfSizeFour(sbList, discode, language);
        } else {
            rsMap = this.nsfcKwsTfCotfDao.getKwsCotfSizeFourWithoutDiscode(sbList, language);
        }
        return rsMap;
    }

    /*
     * 计算长度为3的子集,需要提前去重排序
     * 
     */
    private List<Map<String, Object>> getSubsetsStrLengthThree(List<String> kwList, String discode, Integer language) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        Integer length = kwList.size();
        List<String> sbList = new ArrayList<String>();
        for (int k = 0; k < length - 2; k++) {
            for (int k1 = k + 1; k1 < length - 1; k1++) {
                for (int k2 = k1 + 1; k2 < length; k2++) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(kwList.get(k)).append(";").append(kwList.get(k1)).append(";").append(kwList.get(k2));
                    sbList.add(sb.toString());
                }
            }
        }
        List<Map<String, Object>> rsMap = new ArrayList<Map<String, Object>>();
        if (StringUtils.isNotBlank(discode)) {
            rsMap = this.nsfcKwsTfCotfDao.getKwsCotfSizeThree(sbList, discode, language);
        } else {
            rsMap = this.nsfcKwsTfCotfDao.getKwsCotfSizeFourWithoutDiscode(sbList, language);
        }
        return rsMap;
    }

    /*
     * 计算长度为2的子集,需要提前去重排序
     * 
     */
    private List<Map<String, Object>> getSubsetsStrLengthTwo(List<String> kwList, String discode, Integer language) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        Integer length = kwList.size();
        List<String> sbList = new ArrayList<String>();
        for (int k = 0; k < length - 1; k++) {
            for (int k1 = k + 1; k1 < length; k1++) {
                StringBuilder sb = new StringBuilder();
                sb.append(kwList.get(k)).append(";").append(kwList.get(k1)).append("\r\n");
                sbList.add(sb.toString());
            }
        }
        List<Map<String, Object>> rsMap = new ArrayList<Map<String, Object>>();
        if (StringUtils.isNotBlank(discode)) {
            rsMap = this.nsfcKwsTfCotfDao.getKwsCotfSizeTwo(sbList, discode, language);
        } else {
            rsMap = this.nsfcKwsTfCotfDao.getKwsCotfSizeFiveWithoutDiscode(sbList, language);
        }
        return rsMap;
    }

    private ArrayList<String> getLongestCommonKwSequenceList(List<String> applicationKwsOld,
            List<String> userPubKwsOld) {
        if (applicationKwsOld == null || userPubKwsOld == null || applicationKwsOld.size() <= 0
                || userPubKwsOld.size() <= 0) {
            return null;
        }
        List<String> applicationKws = new ArrayList<String>();
        List<String> userPubKws = new ArrayList<String>();
        for (String aStr : applicationKwsOld) {
            if (StringUtils.isEmpty(aStr)) {
                continue;
            }
            applicationKws.add(aStr.toLowerCase().trim());
        }
        for (String uStr : userPubKwsOld) {
            if (StringUtils.isEmpty(uStr)) {
                continue;
            }
            userPubKws.add(uStr.toLowerCase().trim());
        }
        int[][] c = new int[applicationKws.size() + 1][userPubKws.size() + 1];
        // 从0开始，初始化矩阵
        for (int row = 0; row <= applicationKws.size(); row++) {
            c[row][0] = 0;
        }
        for (int column = 0; column <= userPubKws.size(); column++) {
            c[0][column] = 0;
        }
        for (int i = 1; i <= applicationKws.size(); i++)
            for (int j = 1; j <= userPubKws.size(); j++) {
                if ((applicationKws.get(i - 1)).equalsIgnoreCase(userPubKws.get(j - 1))) { // c[i][j] = c[i - 1][j - 1]
                                                                                           // + 1
                    c[i][j] = c[i - 1][j - 1] + 1;
                } else if (c[i][j - 1] > c[i - 1][j]) { // 如果c[i][j - 1] ！=c[i - 1][j]， c[i][j] = Max(c[i][j - 1], c[i -
                                                        // 1][j])
                    c[i][j] = c[i][j - 1];
                } else {
                    c[i][j] = c[i - 1][j];
                }
            }

        ArrayList<String> longestCommonSeq = new ArrayList<String>();
        // 回溯输出子序列
        for (int i = applicationKws.size(); i > 0; i--) {
            for (int j = userPubKws.size(); j > 0; j--) {
                if (c[i][j] > c[i - 1][j] && c[i][j] > c[i][j - 1]) {
                    if (c[i][j] == 0) {
                        break;
                    }
                    longestCommonSeq.add(applicationKws.get(i - 1));
                }
            }
        }
        return longestCommonSeq;
    }

    @Override
    public Integer extractKwsForIsis(String title, String abstractStr, String kws, String category,
            List<HashMap<String, Object>> dataMap) throws Exception {
        Integer rs = 1;
        List<NsfcKwScoreForSorting> kwsSet = this.extractKwsForNsfc(title, kws, abstractStr, null);
        if (kwsSet == null || kwsSet.size() <= 0) {
            rs = 2;
        } else {
            for (NsfcKwScoreForSorting kw : kwsSet) {
                if (StringUtils.isEmpty(kw.getKeywords())) {
                    continue;
                }
                if (kw.getType() != 2 && kw.getKeywords().length() < 3) {
                    continue;
                }
                HashMap<String, Object> mp = new HashMap<String, Object>();
                mp.put("keyword", kw.getKeywords());
                mp.put("score", kw.getPrjkwScore());
                // mp.put("wtf", kw.getRkwScore());
                mp.put("type", kw.getType());
                dataMap.add(mp);
            }
        }
        return rs;
    }

    private List<NsfcKwScoreForSorting> extractKwsForNsfc(String title, String pubKw, String pubAbstract,
            String nsfcCategory) {
        List<NsfcKwScoreForSorting> rsList = new ArrayList<NsfcKwScoreForSorting>();
        Map<String, Object> titleKwsTf = extractKwWithoutLengthConstrain(title);
        Map<String, Object> absKwsTf = extractKwWithoutLengthConstrain(pubAbstract);
        Set<String> extractTitleKwStrings = new TreeSet<String>();
        Set<String> extractAbsKwStrings = new TreeSet<String>();
        Set<String> selfKwStrings = new TreeSet<String>();
        if (titleKwsTf.get("EXTRACT_KWS_STRSETS") != null) {
            extractTitleKwStrings = (Set<String>) titleKwsTf.get("EXTRACT_KWS_STRSETS");
        }
        if (absKwsTf.get("EXTRACT_KWS_STRSETS") != null) {
            extractAbsKwStrings = (Set<String>) absKwsTf.get("EXTRACT_KWS_STRSETS");
        }
        this.mergeTitleKwsAndSelfKws(pubKw, extractTitleKwStrings, titleKwsTf, selfKwStrings);
        // 通过知识库查找摘要中与标题和自填关键词相关关键词，并标记
        List<String> firstKws = new ArrayList<String>(extractTitleKwStrings);
        List<String> secondKws = new ArrayList<String>();
        // 区分与标题一样的关键词
        for (String kw : extractAbsKwStrings) {
            if (!(extractTitleKwStrings.contains(kw))) {
                secondKws.add(kw);
            }
        }
        HashSet<String> subsetTwoList = this.getSubsetTwoFromList(firstKws, secondKws);
        HashSet<String> absRelatedKws =
                this.nsfcKwsTfCotfDao.getRelatedKwsBasedOnCotf(subsetTwoList, secondKws, nsfcCategory);
        // 加入标题与自填关键词
        if (extractTitleKwStrings != null && extractTitleKwStrings.size() > 0) {
            for (String titleKw : extractTitleKwStrings) {
                Integer ttf = titleKwsTf.get(titleKw) == null ? 0 : (Integer) titleKwsTf.get(titleKw);
                Integer atf = absKwsTf.get(titleKw) == null ? 0 : (Integer) absKwsTf.get(titleKw);
                if (ttf == 0 && atf == 0) {
                    continue;
                }
                Integer aScore = atf;
                /*
                 * Double aScore = atf * 0.33d; if (aScore.compareTo(1.00d) > 0) { aScore = 1.00d; }
                 */
                // 计算得分
                aScore = aScore + ttf;
                Long rtf = new BigDecimal(aScore * 100000000).longValue();
                // 标记类别
                Integer type = 1;
                if (selfKwStrings.contains(titleKw)) {
                    type = 2;
                }
                NsfcKwScoreForSorting nksf = new NsfcKwScoreForSorting(titleKw, titleKw.length(), type, rtf);
                rsList.add(nksf);
            }
        }

        if (extractAbsKwStrings != null && extractAbsKwStrings.size() > 0) {
            for (String absKw : extractAbsKwStrings) {
                Integer atf = absKwsTf.get(absKw) == null ? 0 : (Integer) absKwsTf.get(absKw);
                if (atf == 0 || extractTitleKwStrings.contains(absKw)) {
                    continue;
                }
                Integer aScore = atf;
                /*
                 * Double aScore = atf * 0.33d; if (aScore.compareTo(1.00d) > 0) { aScore = 1.00d; }
                 */
                Integer weightWords = 1000;
                if ((absKw.indexOf(" ") > 0 || absKw.indexOf("-") > 0) && !(XmlUtil.isChinese(absKw))) {
                    weightWords = 100000;
                }

                Long rtf = new BigDecimal(aScore * weightWords).longValue();
                // 标记类别
                Integer type = 4;
                if (absRelatedKws.contains(absKw)) {
                    type = 3;
                    // rtf = rtf + new BigDecimal(aScore * 10000).longValue();
                    rtf = rtf + 10000;
                }
                NsfcKwScoreForSorting nksf = new NsfcKwScoreForSorting(absKw, absKw.length(), type, rtf);
                rsList.add(nksf);
            }
        }

        Collections.sort(rsList);
        return rsList;
    }

    private HashSet<String> getSubsetTwoFromList(List<String> firstKws, List<String> secondKws) {
        if (CollectionUtils.isEmpty(firstKws) || CollectionUtils.isEmpty(secondKws)) {
            return null;
        }
        HashSet<String> subsets = new HashSet<String>();

        for (int k = 0; k < firstKws.size() - 1; k++) {
            for (int k1 = 0; k1 < secondKws.size() - 1; k1++) {
                String[] strs = {firstKws.get(k), secondKws.get(k1)};
                Arrays.sort(strs);
                subsets.add(strs[0] + ";" + strs[1]);
            }
        }
        return subsets;
    }

    private Map<String, Object> extractKwWithoutLengthConstrain(String content) {
        Set<String> extractKwStrings = new TreeSet<String>();
        Map<String, Object> tfMp = new HashMap<String, Object>();
        if (StringUtils.isEmpty(content)) {
            return tfMp;
        }
        if (!XmlUtil.isChinese(content)) {
            content = content.toLowerCase().replaceAll("\\s+", "空格");
        }
        Result kwRs = DicAnalysis.parse(content);
        for (Term t : kwRs.getTerms()) {
            if (t == null) {
                continue;
            }
            if ("nsfc_kw_discipline".equals(t.getNatureStr())) {
                if (StringUtils.isNotEmpty(t.getName())) {
                    String kw = t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim();
                    if (StringUtils.isNotEmpty(kw)) {
                        // 只有不包含在字符串里边，才往里边加入
                        extractKwStrings.add(kw);
                        Integer tf = t.natrue().allFrequency;
                        Integer ltf = (Integer) tfMp.get(kw);
                        if (ltf == null || 0 == ltf) {
                            tfMp.put(kw, tf);
                        } else {
                            tf = tf + ltf;
                            tfMp.put(kw, tf);
                        }
                    }
                }
            }
            tfMp.put("EXTRACT_KWS_STRSETS", extractKwStrings);
        }
        return tfMp;
    }

    public static void main(String[] args) {
        Double a = 0.66d;
        Integer b = 2;
        System.out.println(a + b);
        System.out.println((a + b) * 10000);
        System.out.println(new BigDecimal((a + b) * 10000).longValue());

        NsfcKeywordsServiceImpl nl = new NsfcKeywordsServiceImpl();
        SolrIndexService solrIndexService = new SolrIndexSerivceImpl();
        String kw = "抗菌肽";
        try {
            Map<String, Object> kwsMap = solrIndexService.queryRelatedKws(kw, null, null);
            Set<String> standardKwsStr = (Set<String>) kwsMap.get(SolrIndexSerivceImpl.RESULT_ITEMS);
            Map<String, Long> facetMap = (Map<String, Long>) kwsMap.get(SolrIndexSerivceImpl.RESULT_FACET);
            HashSet<NsfcKwForRcmd> kwSet = new HashSet<NsfcKwForRcmd>();
            for (String str : standardKwsStr) {
                Long cotf = facetMap.get(str);
                if (cotf == null || cotf <= 0) {
                    continue;
                }
                Long tf = 0L;
                tf = solrIndexService.queryKwTf(str, null, null);
                NsfcKwForRcmd nkfr = new NsfcKwForRcmd(null, str, tf, cotf);
                kwSet.add(nkfr);
            }
            NsfcKwForRcmd[] newKws = kwSet.toArray(new NsfcKwForRcmd[kwSet.size()]);
            Arrays.sort(newKws);
            ArrayList<String> rsList = new ArrayList<String>();
            ArrayList<NsfcKwForRcmd> rsList1 = new ArrayList<NsfcKwForRcmd>();
            for (NsfcKwForRcmd it : newKws) {
                rsList.add(it.getKw());
                rsList1.add(it);
            }
            System.out.println(rsList);
            System.out.println(rsList1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
