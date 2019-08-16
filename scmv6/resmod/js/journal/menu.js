
function searchCtrl_KeyPress(e) {
	var e = e || window.event;
	var keyCode = e.which || e.keyCode;
	if (keyCode == 13) {
		var x = document.getElementById(searchCtrlButtonId);
		if (x) {
			x.click();
			return false;
		}
	}
	return true;
}
var shm_ShowTimer;
var shm_HideTimer;
var shm_ShowTimeout = 150;
var shm_HideTimeout = 500;
var shm_RootTagId = "shm_hmenu#";
var shm_hideClassName = "shm_hide";
var shm_selectedNavClassName = "shm_Selected";
var shm_LiHasChild_Selected = "shm_selectedLi";
var shm_selectedNav;
var shm_activeLink;
var shm_dropdownArray = null;
var shm_defaultSelectedNav;
var shm_WidthofSubElem;
var shm_WidthofParent;
var shm_LeftPosofParent;
var shm_LeftPosofSubElemWRTSHM;
var shm_RightPosofSubElem;
var shm_LeftPosofSubElem;
var shm_LeftofRootNode;
var shm_Width = 942;
var shm_lids = new Array();
var shm_comp_index = -1;
var shm_curr_comp_index = -1;
var shm_impT = null;

shm_comp_index = 'ID0EAAAAAA'; 
shm_lids[shm_comp_index] = new Array(); 

function shm_swtI(comp_indx, n) {
	if (shm_impT != null && n >= 0 && shm_lids[comp_indx][n] != false) {
		try {
			dcsMultiTrack("DCS.dcsuri", "/shm_" + comp_indx + "_Level2 MenuItem/" + (n + 1), "WT.ad", "", "WT.mc_id", "", "DCSext.wt_linkid", shm_lids[shm_comp_index][n], "WT.dl", "5", "WT.ti", "Level2 menuItem " + (n + 1));
			shm_lids[shm_comp_index][n] = false;
			shm_impT = null;
		}
		catch (e) {
		}
	}
}
function shm_addCId(cid) {
	if (cid == "") {
		return;
	} else {
		cid = ";" + cid;
	}
	var metaTag = document.getElementsByTagName("meta");
	var wtLinkIdObj = null;
	var WTLinkIDMetaName = "dcsext.wt_linkid";
	if (metaTag) {
		for (var m = metaTag.length - 1; m >= 0; m--) {
			wtLinkIdObj = metaTag[m];
			if (wtLinkIdObj != null && wtLinkIdObj["name"].toLowerCase() == WTLinkIDMetaName) {
				var wtMetaContent = wtLinkIdObj["content"];
				wtMetaContent += cid;
				if (wtMetaContent.substring(wtMetaContent.length - 1, wtMetaContent.length) == ";") {
					wtMetaContent = wtMetaContent.substring(0, wtMetaContent.length - 1);
				}
				wtLinkIdObj.content = wtMetaContent;
			}
		}
	}
}
shm_BindEvent(window, "load", shm_AddEvents);
function shm_BindEvent(element, eventType, func) {
	if (window.addEventListener) {
		element.addEventListener(eventType, func, false);
	} else {
		element.attachEvent("on" + eventType, func);
	}
}
function shm_AddEvents() {
	var navigationRoot = document.getElementById(shm_RootTagId);
	if (navigationRoot != null) {
		for (var i = 0; i < navigationRoot.childNodes.length; i++) {
			var cNode = navigationRoot.childNodes.item(i);
			if (cNode.nodeType == 1 && cNode.tagName == "LI" && cNode.className.indexOf(shm_selectedNavClassName) != -1) {
				shm_AlignCenter(cNode);
				shm_selectedNav = cNode;
				shm_defaultSelectedNav = cNode;
				break;
			}
		}
		var anchorElements = navigationRoot.getElementsByTagName("a");
		if (anchorElements != null) {
			for (var i = 0; i < anchorElements.length; i++) {
				shm_BindEvent(anchorElements[i], "mouseover", shm_Delegate(anchorElements[i], shm_mouseover));
				shm_BindEvent(anchorElements[i], "mouseout", shm_Delegate(anchorElements[i], shm_mouseout));
				shm_BindEvent(anchorElements[i], "focus", shm_Delegate(anchorElements[i], shm_mouseover));
				shm_BindEvent(anchorElements[i], "blur", shm_Delegate(anchorElements[i], shm_mouseout));
			}
		}
		shm_Width = navigationRoot.offsetWidth;
		/*shm_DisplayItemTree()*/
	}
}
function shm_Delegate(instance, method) {
	return function () {
		return method.apply(instance, arguments);
	};
}
function shm_mouseover(e) {
	if (shm_HideTimer) {
		window.clearTimeout(shm_HideTimer);
	}
	if (shm_ShowTimer) {
		window.clearTimeout(shm_ShowTimer);
	}
	if (!e) {
		e = (e) ? e : (window.event ? window.event : "");
	}
	shm_activeLink = (e && e.srcElement) ? e.srcElement : e.target;
	shm_ShowTimer = window.setTimeout("shm_show(shm_activeLink);", shm_ShowTimeout);
}
function shm_mouseout(e) {
	if (shm_HideTimer) {
		window.clearTimeout(shm_HideTimer);
	}
	if (shm_ShowTimer) {
		window.clearTimeout(shm_ShowTimer);
	}
	if (!e) {
		e = (e) ? e : (window.event ? window.event : "");
	}
	var obj = (e && e.srcElement) ? e.srcElement : e.target;
	shm_HideTimer = window.setTimeout("shm_hide(true);", shm_HideTimeout);
}
function shm_show(object) {
	if (shm_ShowTimer) {
		window.clearTimeout(shm_ShowTimer);
	}
	var currentLI = object.parentElement ? object.parentElement : object.parentNode;
	var parentUL = currentLI.parentElement ? currentLI.parentElement : currentLI.parentNode;
	var parentULId = parentUL.id ? parentUL.id : null;
	if (currentLI != shm_selectedNav) {
		if (parentULId != shm_RootTagId) {
			for (var j = 0; j < parentUL.childNodes.length; j++) {
				var sNode = parentUL.childNodes.item(j);
				if (sNode.nodeType == 1 && sNode.tagName == "LI") {
					for (var i = 0; i < sNode.childNodes.length; i++) {
						var cNode = sNode.childNodes.item(i);
						if (cNode.nodeType == 1 && cNode.tagName == "UL") {
							for (var k = 0; k < cNode.childNodes.length; k++) {
								var s1Node = cNode.childNodes.item(k);
								if (s1Node.nodeType == 1 && s1Node.tagName == "LI") {
									for (var m = 0; m < s1Node.childNodes.length; m++) {
										var c1Node = s1Node.childNodes.item(m);
										if (c1Node.nodeType == 1 && c1Node.tagName == "UL") {
											if (c1Node.className.indexOf(shm_hideClassName) == -1) {
												c1Node.className = c1Node.className + " " + shm_hideClassName;
											}
											if (s1Node.className.indexOf(shm_LiHasChild_Selected) != -1) {
												s1Node.className = s1Node.className.replace(" " + shm_LiHasChild_Selected, "");
											}
										}
									}
								}
							}
							if (sNode == currentLI) {
								if (cNode.className.indexOf(shm_hideClassName) != -1) {
									cNode.className = cNode.className.replace(" " + shm_hideClassName, "");
									if (sNode.className.indexOf(shm_LiHasChild_Selected) == -1) {
										sNode.className += " " + shm_LiHasChild_Selected;
									}
									shm_curr_comp_index = j;
									if (shm_impT) {
										clearTimeout(shm_impT);
									}
									shm_impT = setTimeout("shm_swtI(shm_comp_index,shm_curr_comp_index)", 100);
								}
							} else {
								if (cNode.className.indexOf(shm_hideClassName) == -1) {
									cNode.className = cNode.className + " " + shm_hideClassName;
									if (sNode.className.indexOf(shm_LiHasChild_Selected) != -1) {
										sNode.className = sNode.className.replace(" " + shm_LiHasChild_Selected, "");
									}
								}
							}
						}
					}
				}
			}
		} else {
			var childNode;
			if (parentUL != null) {
				for (var k = 0; k < parentUL.childNodes.length; k++) {
					childNode = parentUL.childNodes[k];
					if (childNode.nodeType == 1 && childNode.tagName == "LI") {
						childNode.className = childNode.className.replace(" " + shm_selectedNavClassName, "");
						childNode.className = childNode.className.replace(" " + shm_LiHasChild_Selected, "");
					}
					for (var j = 0; j < childNode.childNodes.length; j++) {
						var cNode = childNode.childNodes[j];
						if (cNode.nodeType == 1 && cNode.tagName == "UL") {
							cNode.style.display = "none";
						}
					}
				}
			}
			if (currentLI != null) {
				for (var j = 0; j < currentLI.childNodes.length; j++) {
					var cNode = currentLI.childNodes.item(j);
					if (cNode.nodeType == 1 && cNode.tagName == "UL") {
						shm_AlignCenter(currentLI);
						currentLI.className = currentLI.className.replace(" " + shm_hideClassName, "");
						currentLI.className += " " + shm_selectedNavClassName;
						shm_selectedNav = currentLI;
					}
				}
			}
			shm_hide(false);
		}
	} else {
		shm_HideTimer = window.setTimeout("shm_hide(false);", shm_HideTimeout);
	}
}
function shm_hide(needHide) {
	if (shm_HideTimer) {
		window.clearTimeout(shm_HideTimer);
	}
	shm_HideDwnLvlMenu(shm_selectedNav);
	shm_HideDwnLvlMenu(shm_defaultSelectedNav);
	if (needHide) {
		if (shm_selectedNav != shm_defaultSelectedNav) {
			for (var i = 0; i < shm_selectedNav.childNodes.length; i++) {
				var cNode = shm_selectedNav.childNodes[i];
				if (cNode.nodeType == 1 && cNode.tagName == "UL") {
					cNode.style.display = "none";
				}
			}
			shm_selectedNav.className = shm_selectedNav.className.replace(" " + shm_LiHasChild_Selected, "");
			shm_selectedNav.className = shm_selectedNav.className.replace(" " + shm_selectedNavClassName, "");
			shm_defaultSelectedNav.className = shm_defaultSelectedNav.className.replace(" " + shm_selectedNavClassName, "");
			shm_defaultSelectedNav.className += " " + shm_selectedNavClassName;
			shm_selectedNav = shm_defaultSelectedNav;
			for (var i = 0; i < shm_defaultSelectedNav.childNodes.length; i++) {
				var cNode = shm_defaultSelectedNav.childNodes[i];
				if (cNode.nodeType == 1 && cNode.tagName == "UL") {
					cNode.style.display = "block";
				}
			}
		}
	}
}
function shm_AlignCenter(sNode) {
	var childUl;
	for (var i = 0; i < sNode.childNodes.length; i++) {
		var cNode = sNode.childNodes.item(i);
		if (cNode.nodeType == 1 && cNode.tagName == "UL") {
			childUl = cNode;
			break;
		}
	}
	if (childUl) {
		childUl.style.display = "inline";
		shm_WidthofSubElem = childUl.offsetWidth;
		shm_WidthofParent = sNode.offsetWidth;
		shm_LeftPosofParent = shm_FindPos(sNode);
		shm_LeftPosofSubElemWRTSHM = eval(shm_LeftPosofParent + eval(shm_WidthofParent / 2) - eval(shm_WidthofSubElem / 2));
		shm_RightPosofSubElem = eval(shm_LeftPosofSubElemWRTSHM + shm_WidthofSubElem);
		shm_LeftofRootNode = shm_FindPos(document.getElementById(shm_RootTagId));
		shm_LeftPosofSubElem = shm_LeftPosofSubElemWRTSHM - shm_LeftofRootNode;
		if (shm_LeftPosofSubElem > 0 && shm_RightPosofSubElem < eval(shm_Width + shm_LeftofRootNode)) {
			childUl.style.left = shm_LeftPosofSubElem + "px";
		} else {
			if (shm_RightPosofSubElem > eval(shm_Width + shm_LeftofRootNode)) {
				childUl.style.left = eval((shm_Width - shm_WidthofSubElem) - 5) + "px";
			} else {
				childUl.style.left = "0px";
			}
		}
	}
}
function shm_FindPos(obj) {
	var leftPos = 0;
	while (obj) {
		leftPos += obj.offsetLeft;
		obj = obj.offsetParent;
	}
	return leftPos;
}
function shm_HideDwnLvlMenu(liElement) {
	if (liElement != null) {
		var ulelements = liElement.getElementsByTagName("ul");
		for (var i = 0; i < ulelements.length; i++) {
			var cNode = ulelements[i];
			var pNode = cNode.parentElement ? cNode.parentElement : cNode.parentNode;
			if (pNode.tagName == "LI" && pNode.className.indexOf(shm_LiHasChild_Selected) != -1) {
				pNode.className = pNode.className.replace(" " + shm_LiHasChild_Selected, "");
			}
			cNode.className = cNode.className.replace(" " + shm_hideClassName, "");
			cNode.className = cNode.className.replace(" " + shm_LiHasChild_Selected, "");
			cNode.className = cNode.className + " " + shm_hideClassName;
		}
	}
}
function shm_DisplayItemTree() {
	var parentTag = document.getElementById(shm_RootTagId);
	if (parentTag) {
		var aObjs = parentTag.getElementsByTagName("a");
		var Url = window.location.href;
		Url = Url.substr(Url.lastIndexOf("/") + 1, Url.length - Url.lastIndexOf("/") + 1).toLowerCase();
		for (var i = 0; i < aObjs.length; i++) {
			var href = aObjs[i].href;
			var page = href.substr(href.lastIndexOf("/") + 1, href.length - href.lastIndexOf("/") + 1).toLowerCase();
			if (page == Url) {
				shm_Exec(aObjs[i], shm_DisplayTreeOnLoad);
			}
		}
	}
}
function shm_DisplayTreeOnLoad(obj) {
	if (obj) {
		var className = obj.className;
		var parentLI = obj.parentElement ? obj.parentElement : obj.parentNode;
		if (parentLI.tagName == "LI") {
			if (parentLI.className.indexOf("shm_LiHasChild") > -1) {
				if (obj.className.indexOf("1") < 0) {
					if (obj.className.indexOf("2") > -1) {
						obj.className += " shm_highLight_hasClild_2Tier";
					} else {
						obj.className += " shm_highLight_hasClild_3Tier";
					}
				}
			} else {
				if (obj.className.indexOf("1") < 0) {
					if (obj.className.indexOf("2") > -1) {
						obj.className += " shm_highLight_2Tier";
					} else {
						obj.className += " shm_highLight_3Tier";
					}
				}
			}
		}
	}
}
function position_bottom(){
	if(!document.getElementById("footer")){
			return ;
		}else{
			if(document.getElementById("footer").offsetTop<document.body.scrollHeight)
			{document.getElementById("footer").style.top = document.body.scrollHeight+"px";	}					
			}
	}
/*function shm_Exec(object,method)
{while(object!=null)
{method.call(object,object);var parentLI=object.parentElement?object.parentElement:object.parentNode;var parentUL=parentLI.parentElement?parentLI.parentElement:parentLI.parentNode;var grandParentLi=parentUL.parentElement?parentUL.parentElement:parentUL.parentNode;var parentATag=grandParentLi.childNodes.item(0);if(parentATag.tagName=="A")
object=parentATag;else
object=null;}}*/

