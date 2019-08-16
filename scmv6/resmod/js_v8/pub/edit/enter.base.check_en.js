var validate_errorMsg = { 
		 //==========book
	   bookName: {//书名验证
		  check_titleSpecialChar: "专著题目不能为空",
		  maxlength: "专著题目不能超过2000个字符"
	   },
	   error_title_empty: "标题不能为空",
	   error_startAndEndPage: "起始页码格式不正确",
	   error_startAndEndPage1: "开始页码必须小于结束页码",
	   check_publishDate: "日期格式不正确",
	   check_publishDate_max: "日期不能大于当前日期",
	   check_startDateAndEndDate: "开始日期不能大于结束日期",
	   check_volAndIss:"期卷格式错误",
	   check_volAndIss1:"期卷不能为空",
	   check_volAndIss2:"期卷要为整数",
}
//设置默认的错误消息提示
jQuery.extend(jQuery.validator.messages, {
	  required: "必选字段",
	  remote: "请修正该字段",
	  email: "请输入正确格式的电子邮件",
	  url: "请输入正确的链接",
	  date: "请输入合法的日期",
	  dateISO: "请输入合法的日期 (ISO).",
	  number: "请输入合法的数字",
	  digits: "只能输入整数",
	  creditcard: "请输入合法的信用卡号",
	  equalTo: "请再次输入相同的值",
	  accept: "请输入拥有合法后缀名的字符串",
	  maxlength: jQuery.validator.format("请输入一个 长度最多是 {0} 的字符串"),
	  minlength: jQuery.validator.format("请输入一个 长度最少是 {0} 的字符串"),
	  rangelength: jQuery.validator.format("请输入 一个长度介于 {0} 和 {1} 之间的字符串"),
	  range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
	  max: jQuery.validator.format("请输入一个最大为{0} 的值"),
	  min: jQuery.validator.format("请输入一个最小为{0} 的值")
});