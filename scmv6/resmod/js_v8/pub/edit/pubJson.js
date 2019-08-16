function Publication() {
  this.pubHandlerName = "saveSnsPubHandler";// 保存来源saveSnsPubHandler updateSnsPubHandler
  this.pubGenre = 1; // 成果的类别 1：个人成果 2：群组成果 3：基准库导入成果
  this.isEdit = 0; // 是否编辑过成果
  this.modifyDate = ""// 修改日期
  this.des3PubId = "";
  this.des3PsnId = "";
  this.title = "";
  this.publishDate = ""; // 出版日期，发表日期
  this.countryId = ""; // 国家或地区
  this.fundInfo = ""; // 基金标注（资助标注）
  this.citations = ""; // 引用次数
  this.srcDbId = ""; // 当前收录文献库的DbId
  this.doi = ""; // doi
  this.summary = ""; // 摘要
  this.keywords = ""; // 成果关键词(多个拼接)
  this.srcFulltextUrl = ""; // 全文链接
  this.pubType = ""; // 成果类型 1:奖励；2:书/著作；3:会议论文；4:期刊论文；5:专利；7:其他；8:学位论文；10:书籍章节
  this.oldPubType = "";
  this.recordFrom = "0"; // 记录来源 0, "手工录入" 1, "数据库导入" 2, "文件导入" 3, "基准库导入"
  this.updateMark = ""; // 1在线导入，基准库导入未编辑，2在线导入，基准库导入已编辑，3手工录入或者文件导入，pdf导入
  this.organization = "";
  this.fullText = ""; // 全文json对象
  this.pubTypeInfo = ""; // 各种类型的字段json对象
  this.members = ""; // 成员
  this.situations = ""; // 收录情况
  this.accessorys = ""; // 成果附件
  this.scienceAreas = ""; // 科技领域
  this.industrys = ""; // 行业
  this.sourceId = ""; // 成果来源id
  this.citedUrl = "";
  this.sourceUrl = "";
  this.permission = 7; // 7公开，4私有
  this.HCP = "0"; // 高被引用文章 0未否，1为是
  this.HP = "0"; // 热门文章 0未否，1为是
  this.OA = ""; // Open Access
  this.isProjectPub = ""; // 群组：成果1 文献0
  this.des3GrpId = "";
  // 获取json
  Publication.prototype.getJson = function(isChange) { // isChange 1、切换类型
    for ( var key in this) {
      var $node = $(".json_" + key);
      switch (key) {
        case 'fullText' :
          this.fullText = this.getFullText();
        break;
        case 'pubTypeInfo' :
          if (!isChange || isChange != 1) {
            this.pubTypeInfo = this.getPubTypeInfo();
          } else {
            this.pubTypeInfo = null;
          }
        break;
        case 'members' :
          this.members = this.getMembers();
        break;
        case 'situations' :
          this.situations = this.getSituations();
        break;
        case 'accessorys' :
          this.accessorys = this.getAccessorys();
        break;
        case 'scienceAreas' :
          this.scienceAreas = this.getScienceAreas();
        break;
        case 'industrys' :
          this.industrys = this.getIndustrys();
        break;
        case 'keywords' :
          this.keywords = this.getKeywords();
        break;
        case 'srcFulltextUrl' :
          this.srcFulltextUrl = /^http:\/\/$/.test($.trim($node.val())) ? "" : $.trim($node.val());
        break;
        default :
          if ($node.length > 0 && $node.val() != "") {
            this[key] = $.trim($node.val());
          } else if ($node.length == 0 && isChange == 1) {// 改变类型时候避免丢失一些公共字段
            var $change = $(".change_type_" + key);
            if ($change.length > 0 && $change.val() != "") {
              this[key] = $.trim($change.val());
            }
          }
        break;
      }

    }
    this.dealWithParam(this);// 处理一些参数
    return JSON.stringify(this);
  };
  // 处理一些参数
  Publication.prototype.dealWithParam = function(pub) {
    if (pub.des3PubId && pub.des3PubId != "") {
      pub.pubHandlerName = "updateSnsPubHandler";
      pub.isEdit = 1;
    } else {
      pub.recordFrom == null ? 0 : pub.recordFrom;
    }
    if (pub.des3GrpId != "") {
      pub.pubGenre = 2;
    }
    var info = pub.pubTypeInfo;
    for ( var key in info) {
      if (key == "acceptDate") {
        info[key] = this.dealDate(info[key]);
      }
      if (key == "defenseDate") {
        info[key] = this.dealDate(info[key]);
      }
      if (key == "applicationDate") {
        info[key] = this.dealDate(info[key]);
      }
      if (key == "startDate") {
        info[key] = this.dealDate(info[key]);
      }
      if (key == "endDate") {
        info[key] = this.dealDate(info[key]);
      }
      if (key == "awardDate") {
        info[key] = this.dealDate(info[key]);
      }

    }
    pub.pubTypeInfo = info;
  }
  // 日期转化
  Publication.prototype.dealDate = function(date) {
    if (/^\d{4}(\/\d{2}){0,2}$/.test(date)) {
      date = date.replace(/\//g, "-");
    }
    return date;
  }
  // 获取info
  Publication.prototype.getPubTypeInfo = function() {
    var pubType = Number($("#pubType").val());
    var pubTypeInfo;
    switch (pubType) {
      case 1 : // 奖励
        pubTypeInfo = new AwardsInfoBean();
        this.setInfo(pubTypeInfo, "award");
      break;
      case 2 :// 书籍
        pubTypeInfo = new BookInfoBean();
        this.setInfo(pubTypeInfo, "book");
      break;
      case 3 :// 会议论文
        pubTypeInfo = new ConferencePaperInfoBean();
        this.setInfo(pubTypeInfo, "conferencePaper");
      break;
      case 4 :// 期刊论文
        pubTypeInfo = new JournalInfoBean();
        this.setInfo(pubTypeInfo, "journal");
      break;
      case 5 :// 专利
        pubTypeInfo = new PatentInfoBean();
        this.setInfo(pubTypeInfo, "patent");
        if (pubTypeInfo.status == "1") {// 授权状态下
          if (pubTypeInfo.transitionStatus == "NONE") {
            pubTypeInfo.price = "";
          }
        } else {// 申请状态下清空这些
          pubTypeInfo.startDate = "";
          pubTypeInfo.endDate = "";
          pubTypeInfo.publicationOpenNo = "";
          pubTypeInfo.transitionStatus = "NONE";
          pubTypeInfo.price = "";
        }
      break;
      case 7 :// 其他
        pubTypeInfo = new OtherInfoBean();
        this.setInfo(pubTypeInfo, "other");
      break;
      case 8 :// 学位论文
        pubTypeInfo = new ThesisInfoBean();
        pubTypeInfo = this.setInfo(pubTypeInfo, "thesis");
      break;
      case 10 :// 书籍章节
        pubTypeInfo = new BookChapterInfoBean();
        this.setInfo(pubTypeInfo, "bookChapter");
      break;
      case 12 : // 标准
        pubTypeInfo = new StandardInfoBean();
        this.setInfo(pubTypeInfo, "standard");
      break;
      case 13 : // 软件著作权
        pubTypeInfo = new SoftwareCopyrightBean();
        this.setInfo(pubTypeInfo, "softwarecopyright");
      break;
    }
    return pubTypeInfo;
  };
  // 设置info
  Publication.prototype.setInfo = function(info, name) {
    for ( var key in info) {
      var $node = $(".json_" + name + "_" + key);
      if ($node.length > 0 && $node.val() != "") {
        info[key] = $node.val();
      }
    }
    return info;
  };
  // 获取全文
  Publication.prototype.getFullText = function() {
    var fulltext = new FullText();
    for ( var key in fulltext) {
      var $node = $(".json_fulltext_" + key);
      if ($node.length > 0 && $node.val() != "") {
        fulltext[key] = $.trim($node.val());
      }
    }
    return fulltext;
  };
  // 获取成员
  Publication.prototype.getMembers = function() {
    var members = [];
    var seqNo = 1;
    $(".json_member").each(function(i) {
      if ($(this).find(".json_member_name").val() != "") {
        var member = new Member();
        var $this = $(this);
        for ( var key in member) {
          var $node = $(this).find(".json_member_" + key);
          if ($node.length > 0 && $node.val() != "") {
            switch (key) {
              case 'insNames' :
                var ins = {};
                ins.insId = $node.attr("code");
                ins.insName = $.trim($node.val());
                member[key].push(ins);
              break;
              default :
                member[key] = $.trim($node.val());

            }

          }
        }
        if (seqNo == 1) {
          member['firstAuthor'] = 'true';
        } else {
          member['firstAuthor'] = 'false';
        }
        member['seqNo'] = seqNo++;
        members.push(member);
      }
    });
    return members;
  };
  // 获取收录情况
  Publication.prototype.getSituations = function() {
    var situations = [];
    $(".json_situation").each(function() {
      var $this = $(this);
      if ($(this).find(".json_situation_sitStatus").val() == "true") {// 选择了的
        var situation = new Situation();
        for ( var key in situation) {
          var $node = $(this).find(".json_situation_" + key);
          if ($node.length > 0 && $node.val() != "") {
            situation[key] = $.trim($node.val());
          }
        }
        situations.push(situation);
      }
    });
    return situations;
  };
  // 获取附件
  Publication.prototype.getAccessorys = function() {
    var accessorys = [];
    $(".json_accessory").each(function() {
      var accessory = new Accessory();
      var $this = $(this);
      for ( var key in accessory) {
        var $node = $(this).find(".json_accessory_" + key);
        if ($node.length > 0 && $node.val() != "") {
          accessory[key] = $.trim($node.val());
        }
      }
      accessorys.push(accessory);
    });
    return accessorys;
  };
  // 获取科技领域
  Publication.prototype.getScienceAreas = function() {
    var scienceAreas = [];
    $(".json_scienceArea").each(function() {
      var scienceArea2 = new ScienceArea();
      var scienceArea1 = new ScienceArea();
      var $this = $(this);
      var $node1 = $this.find(".json_scienceArea_scienceAreaId:eq(0)");
      var $node2 = $this.find(".json_scienceArea_scienceAreaId:eq(1)");

      if ($node1.length > 0 && $node1.val() != "") {
        scienceArea1.scienceAreaId = $node1.val();
        scienceAreas.push(scienceArea1);
      }
      if ($node2.length > 0 && $node2.val() != "") {
        scienceArea2.scienceAreaId = $node2.val();
        scienceAreas.push(scienceArea2);
      }
    });
    return scienceAreas;
  };
  // 获取行业
  Publication.prototype.getIndustrys = function() {
    var industrys = [];
    $(".json_industry").each(function() {
      var industry1 = new Industry();
      var industry2 = new Industry();
      var $this = $(this);
      var $node1 = $this.find(".json_industry_industryCode:eq(0)");
      var $node2 = $this.find(".json_industry_industryCode:eq(1)");

      if ($node1.length > 0 && $node1.val() != "") {
        industry1.industryCode = $node1.val();
        industrys.push(industry1);
      }
      if ($node2.length > 0 && $node2.val() != "") {
        industry2.industryCode = $node2.val();
        industrys.push(industry2);
      }
    });
    return industrys;
  };
  // 获取关键词
  Publication.prototype.getKeywords = function() {
    var keywords = "";
    $(".json_keyword").each(function() {
      var text = $.trim($(this).text());
      if (text != "") {
        keywords = keywords + text + ";";
      }
    });
    return keywords.replace(/;$/, "");
  };
};

// ////////////////////////////////////////对象
// 全文
function FullText() {
  this.des3fileId = ""; // 全文附件id，对应archive_files中的file_id
  this.fileName = ""; // 文件名
  this.permission = ""; // 全文下载权限，0所有人可下载，1好友可下载，2自己可下载
};
// 成员
function Member() {
  this.seqNo = ""; // 默认 成果所有人将拥有seq_no=0的记录 ，排序使用
  this.des3PsnId = ""; // 科研之友对应的人员id 默认为空
  this.name = ""; // 成员名字
  this.insNames = []; // 单位名称
  this.email = ""; // 邮箱
  this.owner = "false"; // 1: 为本人，0: 不是本人'
  this.communicable = "false"; // '0:作者,1:通讯作者'
  this.firstAuthor = "false"; // 0或者空：不是第一作者 1:第一作者'
};
// 收录情况
function Situation() {
  this.libraryName = "";// 收录机构名
  this.sitStatus = "false";// 收录状态 0:未收录 ，1:收录
  this.sitOriginStatus = "false";// 原始收录状态 0:未收录 ，1:收录
  this.srcUrl = "";// 来源URL
  this.srcDbId = "";// 来源dbid
  this.srcId = "";// 来源唯一标识
};
// 成果附件
function Accessory() {
  this.des3fileId = "";
  this.fileName = "";
  this.permission = "";// 权限
};
// 科技领域
function ScienceArea() {
  this.scienceAreaId = "";
};
// 行业
function Industry() {
  this.industryCode = "";// 行业编码
};
// //////////////////////////////////////////////pubTypeInfo
// //期刊
function JournalInfoBean() {
  this.jid = ""; // 期刊id
  this.name = ""; // 期刊名称
  this.volumeNo = ""; // 卷号
  this.issue = ""; // 期号
  this.issn = ""; // 期刊的issn号
  this.pageNumber = ""; // 起始页码文章号
  this.publishStatus = ""; // 发表状态(P已发表/A已接收)
};

// //书籍章节 章节名title
function BookChapterInfoBean() {
  this.name = "";
  this.seriesName = "";// 丛书名称
  this.editors = "";// 编辑
  this.isbn = "";// 国际标准图书编号
  this.publisher = "";// 出版社
  this.totalWords = "";// 总字数
  this.pageNumber = ""; // 起始页码
  this.type = "NULL";// 书籍/著作类型
  this.totalPages = "";// 总页数
  this.chapterNo = "";// 章节号
};

// //书籍著作 书名title
function BookInfoBean() {
  this.seriesName = "";// 丛书名称
  this.isbn = "";// 国际标准图书编号
  this.editors = "";// 编辑
  this.language = "";// 语言
  this.publisher = "";// 出版社
  this.totalWords = "";// 总字数
  this.type = "";// 书籍/著作类型
  this.totalPages = "";// 总页数
  this.pageNumber = ""; // 起始页码
};

// //学位论文
function ThesisInfoBean() {
  this.degree = "OTHER"; // 枚举 0硕士-1博士-2其他
  this.defenseDate = ""; // 答辩日期
  this.issuingAuthority = ""; // 颁发单位
  this.department = ""; // 部门
  this.isbn = ""; // 国际标准图书编号
};

// //专利
function PatentInfoBean() {
  this.type = "";// 专利类别，发明专利51/实用新型52/外观设计53
  this.area = "";// 专利国家 中国专利/美国专利/欧洲专利/WIPO专
  this.status = "";// 专利状态，0:申请/1:授权
  this.applier = "";// 申请人 (专利为申请状态)
  this.applicationDate = "";// 申请日期
  this.patentee = "";// 专利权人 (专利为授权状态)
  this.startDate = "";// 专利生效起始日期 (专利为授权状态)
  this.endDate = "";// 专利失效结束日期 (专利为授权状态)
  this.applicationNo = "";// 申请号
  this.publicationOpenNo = "";// 专利公开（公告）号
  this.ipc = "";// IPC号
  this.cpc = "";// cpc号
  this.transitionStatus = "";// 专利转换状态 许可/转让/作价投资/未转化
  this.price = "";// 交易金额
  this.issuingAuthority = "";// 专利授权组织，颁发机构
}

// //会议
function ConferencePaperInfoBean() {
  this.name = ""; // 会议名称
  this.paperType = ""; // 论文类别
  this.papers = "";// 论文集名
  this.organizer = "";// 会议组织者
  this.startDate = "";// 开始日期
  this.endDate = "";// 结束日期
  this.pageNumber = ""; // 起始页码
};

// //其他
function OtherInfoBean() {

};

// //奖励
function AwardsInfoBean() {
  this.category = ""; // 奖项种类
  this.issuingAuthority = "";// 授奖机构
  this.issueInsId = "";// 授奖机构id
  this.certificateNo = "";// 证书编号
  this.awardDate = "";// 授奖日期
  this.grade = "";// 奖项等级
};
// 软件著作权
function SoftwareCopyrightBean() {
  this.registerNo = ""; // 登记号
  this.acquisitionType = "NULL"; // 获得方式
  this.registerDate = ""; // 登记日期
  this.firstPublishDate = ""; // 首次发布日期
  this.publicityDate = ""; // 公示日期
  this.categoryNo = ""; // 分类号
  this.scopeType = "NULL"; // 权利范围
}
// 标准
function StandardInfoBean() {
  this.type = "NULL"; // 标准类型
  this.standardNo = ""; // 标准号
  this.publishAgency = ""; // 公布机构
  this.obsoleteDate = ""; // 作废日期
  this.implementDate = ""; // 实施日期
  this.icsNo = ""; // ICS分类
  this.domainNo = ""; // 中标分类
  this.technicalCommittees = ""; // 归口单位
}
