var scmLeftMenu;
ScmLeftMenu = function(leftMenuId) {
	this.leftMenuId=leftMenuId;
	this.showMenu = ScmLeftMenu.showMenu;
	this.addMenu = ScmLeftMenu.addMenu;
	this.editMenu = ScmLeftMenu.editMenu;
	this.handlerEditMenu = ScmLeftMenu.handlerEditMenu;
	this.deleteMenu = ScmLeftMenu.deleteMenu;
	this.switchContent = ScmLeftMenu.switchContent;
	this.init=ScmLeftMenu.init;
};

/**
 * 初始.
 */
ScmLeftMenu.init=function(){
	$("#" + this.leftMenuId).find("a").attr("class", "leftnav-hover");// 初始选中项
	if (this.leftMenuId != "menu-mine") {
		$("#" + this.leftMenuId).parent().attr("class", "menu-expansion");
		$("#" + this.leftMenuId).parent().parent().find(".Shear-head").attr(
				"class", "Shear-headbottom");
	}else{
		$("#tag-dl").attr("class","menu-expansion");
		$("#tag-dl").parent().find(".Shear-head").attr(
				"class", "Shear-headbottom");
	}
};

/**
 * 菜单收缩展开.
 * 
 * @param obj
 * @return
 */
ScmLeftMenu.showMenu = function(obj) {
	var currentDl = $(obj).parent().find("dl");
	if ($(currentDl).hasClass("menu-expansion")) {
		currentDl.attr("class", "menu-shrink");
		$(obj).find(".Shear-headbottom").attr("class", "Shear-head");
	} else {
		// 关闭其他
		$(".menu-expansion").each(
				function() {
					$(this).attr("class", "menu-shrink");
					$(this).parent().find(".Shear-headbottom").attr("class",
					"Shear-head");
				});
		// 当前
		currentDl.attr("class", "menu-expansion");
		$(obj).find(".Shear-head").attr("class", "Shear-headbottom");
	}

};

/**
 * 菜单添加.
 * 
 * @param obj
 * @param leftMenuParent左侧菜单父元素ID
 * @param url
 *            添加请求url
 * @param fn回调函数
 * @return
 */
ScmLeftMenu.addMenu = function(obj, leftMenuParent, url, fn) {
	var tagInput = $.trim($("#tag-input").val());
	if (tagInput == "") {
		$.scmtips.show("warn", "请输入标签名");
	} else {
		$
		.ajax( {
			method : "post",
			url : url,
			data : {
			"menuText" : tagInput
		},
		dataType : "json",
		success : function(data) {
			if (data.result == "success") {
				var newMenuId = data.menuId;
				// 左侧菜单添加
				$("#" + leftMenuParent)
				.append(
						"<dd id=\"menu-dd"
						+ newMenuId
						+ "\"><a href=\"javascript:void(0)\" onclick=\"scmLeftMenu.switchContent('menu-dd"
						+ newMenuId + "','" + ctxpath
						+ "/demo/leftMenu')\">" + tagInput
						+ "</a></dd>");
				// 当前弹出窗口
				var newTagHtml = "<li><a href=\"#\" class=\"pop-delete\"></a>"
					+ "<span class=\"lable-nr\">"
					+ tagInput
					+ "</span>"
					+ "<input type=\"text\" style=\"display:none;\" class=\"p_lab\" value=\""
					+ tagInput + "\"/></li>";
				$(obj).parent().parent().find(".label-list").append(
						newTagHtml);
				// 清空当前输入框
				$("#tag-input").val("");
				if (fn != null && typeof (fn) != "undefined")
					fn(newMenuId);
				$.scmtips.show("success", "添加成功");
			} else if (data.result == "exist") {
				$.scmtips.show("warn", "已经存在");
			}
		},
		error : function(e) {
		}
		});
	}
};

/**
 * 切换至菜单编辑.
 * 
 * @param menuId
 *            菜单的ID
 * @return
 */
ScmLeftMenu.editMenu = function(menuId) {
	// 其他变为非编辑状态
	$("span[id*=menu-label]").each(function() {
		$(this).show();
	});
	$("input[id*=menu-input]").each(function() {
		$(this).hide();
	});
	$("#menu-label" + menuId).hide();
	var menuInputObj = $("#menu-input" + menuId);
	menuInputObj.show();
	menuInputObj.focus();
};

/**
 * 菜单编辑处理
 * 
 * @param menuId
 *            菜单ID
 * @param url
 *            编辑请求url
 * @param fn
 *            回调函数.
 * @return
 */
ScmLeftMenu.handlerEditMenu = function(menuId, url, fn) {
	var spanText = $.trim($("#menu-label" + menuId).text());
	var inputText = $.trim($("#menu-input" + menuId).val());
	if (spanText != inputText) {
		jConfirm('您确定要更新此文件夹名称吗?', 'Confirmation Dialog', function(r) {
			if (r) {
				$.ajax( {
					method : "post",
					url : url,
					data : {},
					dataType : "json",
					success : function(data) {
						if (data.result == "exist") {
							$.scmtips.show("warn", "已经存在");
						} else if (data.result == "success") {
							// 更新左侧菜单
							$("#menu-dd" + menuId).find("a").text(inputText);
							// 更新当前弹出窗口
							$("#menu-labelr" + menuId).text(inputText);
							// 切换编辑
							$("#menu-label" + menuId).show();
							$("#menu-input" + menuId).hide();
							if (fn != null && typeof (fn) != "undefined")
								fn(menuId);
							$.scmtips.show("success", "更新成功");
						}
					},
					error : function(e) {
					}
				});
			} else {
				$("#p_lab" + menuId).val(spanText);
				$("#lable-nr" + menuId).show();
				$("#p_lab" + menuId).hide();
			}
		});
	} else {
		$("#lable-nr" + menuId).show();
		$("#p_lab" + menuId).hide();
	}
};

/**
 * 菜单删除.
 * 
 * @param obj
 * @param menuId
 *            菜单ID
 * @param url
 *            菜单删除请求url
 * @param fn
 *            回调函数.
 * @return
 */
ScmLeftMenu.deleteMenu = function(obj, menuId, url, fn) {
	jConfirm('您确定要删除吗?', 'Confirmation Dialog', function(r) {
		if (r) {
			$.ajax( {
				method : "post",
				url : url,
				data : {
				"tagId" : menuId
			},
			dataType : "json",
			success : function(data) {
				if (data.result == "success") {
					// 左侧菜单移除
					$("#menu-dd" + menuId).remove();
					// 当前弹出窗口移除
					$(obj).parent().remove();
					$.scmtips.show('success', '删除成功');
				}
			},
			error : function(e) {
			}
			});
		}
	});
};

/**
 * 菜单选中更新.
 * 
 * @param menuId
 *            选中菜单元素的ID
 * @param url
 * @return
 */
ScmLeftMenu.switchContent = function(menuId, url) {
	if (menuId != this.leftMenuId) {
		$("#switchForm").remove();
		$(
				"<form id='switchForm'  name='switchForm'  method='post' >"
				+ "<input name='leftMenuId' type='hidden' value='"
				+ menuId + "'/>" + "</form>").insertAfter("body");
		$('#switchForm').attr("action", url);
		$('#switchForm').submit();
	}
};