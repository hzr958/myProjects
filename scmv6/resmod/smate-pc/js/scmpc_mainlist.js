﻿﻿﻿/**
 * 列表模块
 * 
 * 输出的方法
 * 
 * @method sendRequestForList 请求列表
 * @method sendRequestForStats 请求统计数
 * @method reloadCurrentPage 重新加载当前页
 * @method resetAllFilterOptions 重置所有过滤条件加载
 * @method getDrawerSelectedId 获取批量选择的所有ID
 * @method drawerRemoveSelected 移除批量选择中的数据
 * @method initializeDrawer 初始化批量选择
 * @method setCookieValues 将过滤条件存放到cookie中
 * @method resetCookieValues 读取并请求cookie中过滤条件
 * 
 * Mainlist对象所需的参数
 * @param {String}
 *            name 唯一标识符，用于绑定属于同一列表的不同DOM区域
 * @param {String}
 *            listurl 请求列表的AJAX地址
 * @param {Object}
 *            [listdata] 请求列表需要的额外参数
 * @param {Function}
 *            [beforeSend] 发送请求列表前执行的回调方法 ---扩展参数
 * @param {Function}
 *            [listcallback] 请求成功的额外回掉方法（已默认把内容j贴进HTML）
 * @param {String}
 *            [noresultHTML] 请求返回的列表总数为0时的提示，必须为HTMLString
 * @param {String}
 *            [statsurl] 请求统计数的AJAX地址
 * @param {String}
 *            [method] 翻页的方法 "scroll"||"pagination"
 * @param {Object}
 *            [drawermethods] 批量操作的方法，对象名为操作按钮名称，值为方法
 * @param {Object}
 *            [exportmethods] 导出方法           
 * @param {function}
 *            [drawermethodsmaskback] 批量展开回调 more
 * @param {String}
 *            more 是否构建多个批量处理按钮
 * @param {Map}
 *            [filtermap] key为过滤条件组的HTMLElement，value为动态参数对象 {url： 查询列表请求的地址，
 *            acURL： 添加额外字段的请求地址， name： 过滤条件的名字， code： 过滤条件的ID，
 *            acType:额外请求字段的显示方式，默认是button ---扩展参数
 *            addPosition:添加额外请求字段在列表中的位置：pre||suf 默认suf ---扩展参数（目前actype=input
 *            时使用） isSelected:添加元素是否是选中状态 true||false ---扩展参数 （目前actype=input
 *            时使用） [其他映射]}
 * 
 * @OriginalAuthor shenxingjia
 * @LatestVersion 2017 Jul 06 (shenxingjia)
 */
(function (window, document) {
	const multilingual = {};
	const multilingualSettings = {
		"add": ["Add More", "\u6dfb\u52a0"],
		"goTo": ["Go to: ", "\u8df3\u8f6c\u003a\u0020"],
		"clearList": ["Clear All", "\u6e05\u7a7a\u5217\u8868"],
		"deleteList": ["Batch deleting", "\u6279\u91cf\u5220\u9664"],
		"batchOperation": ["Batch Operation", "\u6279\u91cf\u64cd\u4f5c"],
		"noresultHint": ["No matched record has been found.", "\u672a\u627e\u5230\u7b26\u5408\u6761\u4ef6\u7684\u8bb0\u5f55"],
		"grpOutsideNoRecord": ["You should join the group first before publishing and viewing the discuss.", "\u9700\u8981\u52a0\u5165\u7fa4\u7ec4\u540e\u624d\u80fd\u53d1\u8868\u3001\u67e5\u770b\u8ba8\u8bba"],
		"dataError": ["Please input results and retrieve results in the search box.",  "\u8bf7\u5728\u68c0\u7d22\u6846\u8f93\u5165\u6210\u679c\u4fe1\u606f\u68c0\u7d22\u6210\u679c\u5e76\u5bfc\u5165"],
	    "paperDataError":["Please input results and retrieve results in the search box.",  "\u8bf7\u5728\u68c0\u7d22\u6846\u8f93\u5165\u6210\u679c\u4fe1\u606f\u68c0\u7d22\u6210\u679c"],
	    "paperDataNoResult":["<h2>Sorry, no results matching all your search terms were found.</h2><div class='no_effort_tip'><span>Suggestions：</span><p>Make sure that all words are spelled correctly.</p><p>Try synonyms or more general keywords.</p><p>Search in other categories or clear filters.</p></div>","<h2>\u5f88\u62b1\u6b49\uff0c\u672a\u627e\u5230\u4e0e\u68c0\u7d22\u6761\u4ef6\u76f8\u5173\u7ed3\u679c</h2><div class='no_effort_tip'><span>\u6e29\u99a8\u63d0\u793a\uff1a</span><p>检查输入条件是否正确.</p><p>\u5c1d\u8bd5\u540c\u4e49\u8bcd\u6216\u66f4\u901a\u7528\u5173\u952e\u8bcd\u002e</p><p>\u66f4\u6362\u68c0\u7d22\u7c7b\u522b\u6216\u8fc7\u6ee4\u6761\u4ef6\u002e</p></div>"],
	     "exportcontentdetail":["Selection of export product format","\u5bfc\u51fa\u6210\u679c\u683c\u5f0f\u9009\u62e9"],
	     "exportconfirm":["Confirm","\u786e\u8ba4"],
	     "exportcancle":["Cancle","\u53d6\u6d88"]
	     
	
	};
	Object.keys(multilingualSettings).forEach(function (x) {
		if (window.locale === "en_US") {
			multilingual[x] = multilingualSettings[x][0];
		} else {
			multilingual[x] = multilingualSettings[x][1];
		}
	});

	function Mainlist(options) {
		const $self = this;
		$self.name = options.name;
		$self.listurl = options.listurl;
		$self.listdata = options.listdata;
		$self.beforeSend = options.beforeSend||function(){};
		$self.listcallback = options.listcallback;
		$self.noresultHTML = options.noresultHTML;
		$self.statsurl = options.statsurl;
		$self.method = options.method || "pagination";
		$self.drawermethods = options.drawermethods;
		$self.exportmethods = options.exportmethods;
		$self.drawermethodsmaskback = options.drawermethodsmaskback||function(){};
		$self.filtermap = options.filtermap;
        $self.more = options.more||false;
        $self.grpmore = options.grpmore||false;
		// 定义动态变量
		$self.primarylistLive = document.getElementsByClassName("main-list__list");
		$self.filterlistsLive = document.getElementsByClassName("filter-list");
		$self.searchfieldLive = document.getElementsByClassName("main-list__searchbox");
		$self.paginationfieldLive = document.getElementsByClassName("pagination__box");
		$self.setStaticNodeList();
		if($self.primarylist==undefined){
		  return false;
		}
		if($self.primarylist.getAttribute("memlist") == 'display'){
			// 读取cookie缓存的过滤条件
			var page = $self.resetCookieValues();
			$self.sendRequestForList(page);
			$self.sendRequestForStats();
			$self.initializeDrawer();
			$self.bindFilterEvents();
			$self.bindSearchEvents();
			return true;
		}else{
			// 初始化过滤状态
			$self.filtersections.forEach(function (x) {
				if (x.getAttribute("filter-method") === "compulsory") {
					if(x.getAttribute("init") != "ignore"){// 如果已手动初始化，跳过此处的初始化。通常用于满足第一次特殊条件请求
						x.getElementsByTagName("ul")[0].firstElementChild.classList.add("option_selected");
					}else{
						x.removeAttribute("init");
					}
				}
			});
			$self.filtervalues.forEach(function (x) {
				if (x.getElementsByTagName("input")[0].checked === true) {
					x.classList.add("option_selected");
				}
			});
		}
		// 移除列表滚动监听
		// removeSpecificEventListener(window, "scroll", "loadOnScroll");
		/*
         * if ($self.method === "scroll") {
         *  }
         */

		if ($self.filtermap) {
			$self.modifyDynamicFilterValues();
		} else {
			// $self.listdata.pageValue 定位页数
			$self.sendRequestForList($self.listdata.pageValue);
			$self.sendRequestForStats();
			$self.initializeDrawer();
			$self.bindFilterEvents();
			$self.bindSearchEvents();
		}
	}


	Mainlist.prototype.setStaticNodeList = function () {
		const $self = this;
		// 按照初始化的名字对主列表、检索框及翻页框进行过滤
		const $primaryList = Array.from($self.primarylistLive).filter(function (x) {
			return x.getAttribute("list-main") === $self.name;
		});
		const $searchField = Array.from($self.searchfieldLive).filter(function (x) {
			return x.getAttribute("list-search") === $self.name;
		});
		const $paginationField = Array.from($self.paginationfieldLive).filter(function (x) {
			return x.getAttribute("list-pagination") === $self.name;
		});
		try {
			if ($primaryList.length === 0) {
				throw "Can not find the list named '" + $self.name + "'.";
			}
			if ($primaryList.length > 1) {
				throw "There are two lists named '" + $self.name + "'.";
			}
			if ($searchField.length > 1) {
				throw "One list can only have one search field.";
			}
			if ($paginationField.length > 1) {
				throw "One list can only have one pagination field.";
			}
		} catch (e) {
			//console.error(e);
		}

		// 遍历过滤条件，形成条件组数组和选项数组
		var $sectionArray = [];
		var $valueArray = [];
		const $filterLists = Array.from($self.filterlistsLive).filter(function (x) {
			return x.getAttribute("list-filter") === $self.name;
		});
		$filterLists.forEach(function (x) {
			$sectionArray = $sectionArray.concat(Array.from(x.getElementsByClassName("filter-list__section")));
			$valueArray = $valueArray.concat(Array.from(x.getElementsByClassName("filter-value__item")));
		});

		// 对象属性赋值
		this.primarylist = $primaryList[0];
		this.filtersections = $sectionArray;
		this.filtervalues = $valueArray;
		this.searchfield = $searchField[0];
		this.paginationfield = $paginationField[0];
	};



	/**
     * 定义事件移除方法和时间添加方法，避免在初始化时重复绑定
     * 
     * @method addSpecificEventListener 添加一个监听事件
     * @method removeSpecificEventListener 移除一个监听事件，在addSpecificEventListener前使用
     */
	const $EventArray = []; // 定义一个事件集合数组
	/**
     * 添加一个监听事件
     * 
     * @param {HTMLElemnt}
     *            o 所需要绑定事件的HTMLElement对象
     * @param {String}
     *            evt 事件类型名称
     * @param {String}
     *            fname 方法名称
     * @param {Function}
     *            f 定义的具体方法
     */
	function addSpecificEventListener(o, evt, fname, f) {
		removeSpecificEventListener(o, evt, fname); // 添加事件之前也移除，避免重复绑定
		const $object = {};
		$object.node = o;
		$object.eventType = evt;
		$object.functionName = fname;
		$object.function = f;
		o.addEventListener(evt, f);
		$EventArray.push($object); // 每添加一个监听事件就在数组中添加这个事件的一些属性，方便之后移除
	}
	/**
     * 添加一个监听事件
     * 
     * @param {HTMLElemnt}
     *            o 所需要绑定事件的HTMLElement对象
     * @param {String}
     *            evt 事件类型名称
     * @param {String}
     *            fname 方法名称
     */
	function removeSpecificEventListener(o, evt, fname) {
		// 遍历数组，找到事件属性相同的数组元素，并移除相关方法
		$EventArray.forEach(function (x, idx) {
			if (x.node === o && x.eventType === evt && x.functionName === fname) {
				o.removeEventListener(evt, x.function);
				$EventArray.splice(idx, 1);
			}
		});
	}

	/**
     * 构造一个HTMLElement元素的方法，仅能设置对象的className，仅支持appendChild添加方法
     * 
     * @param {String}
     *            nodetype 所需创建对象的TagName
     * @param {HTMLElement}
     *            parentElement 所需创建对象的父元素，创建的对象会append到父元素上
     * @param {String}
     *            classList 创建的对象className
     * @param {String}
     *            textContent 创建的对象包含的文字
     * @param {Function}
     *            f 创建的对象所执行的方法
     */
	function createHTMLElement(nodetype, parentElement, classList, textContent, f) {
		textContent = textContent || "";
		classList = classList || "";
		const $childElement = document.createElement(nodetype);
		$childElement.className = classList;
		$childElement.textContent = textContent;
		if (f !== undefined) {
			f($childElement);
		}
		parentElement.appendChild($childElement);
	}
	/**
     * 构造一个HTMLElement元素的方法，仅能设置对象的className，仅支持insertBefore添加方法
     * 
     * @param {String}
     *            nodetype 所需创建对象的TagName
     * @param {HTMLElement}
     *            parentElement 所需创建对象的父元素，创建的对象会append到父元素上
     * @param {String}
     *            classList 创建的对象className
     * @param {String}
     *            textContent 创建的对象包含的文字
     * @param {Function}
     *            f 创建的对象所执行的方法
     */
	function createHTMLElementBefore(nodetype, parentElement, classList, textContent, f) {
		textContent = textContent || "";
		classList = classList || "";
		const $childElement = document.createElement(nodetype);
		$childElement.className = classList;
		$childElement.textContent = textContent;
		if (f !== undefined) {
			f($childElement);
		}
		if(parentElement.children[0]){
			parentElement.insertBefore($childElement,parentElement.children[0]);
		}else{
			parentElement.appendChild($childElement);
		}
	}

	Mainlist.prototype.modifyDynamicFilterValues = function () {
		const $self = this;

		function createAddingItemModule(o, value) {
			const url = value.acURL;
			const type = value.acType;
			const addPosition = value.addPosition;
			const isSelected = value.isSelected;
			const $list = o.getElementsByClassName("filter-value__list")[0];
			// 直接显示input start
			if(type === "input"){
				createHTMLElementBefore("div", $list, "filter-section__create input_shown", null, function (el) {
					createHTMLElement("div", el, "filter-create__input", null, function (el) {
						createHTMLElement("input", el, "js_autocompletebox", null, function (el) {
							el.setAttribute("request-url", url);
							el.setAttribute("contenteditable", "true");
						});
					});
				});
				const $createBox = o.getElementsByClassName("filter-section__create")[0];
				const $input = o.getElementsByClassName("js_autocompletebox")[0];
				const $observer = new MutationObserver(function (mutations) {
					mutations.forEach(function (x) {
						if (x.attributeName === "code") {
							if (x.target.getAttribute("code") !== "") {
								$observer.disconnect();
								// 判断 当前过滤列表中不存在选中词条时 则创建词条
								if (getSingleFilterSectionValues(o).indexOf(x.target.value) === -1) {
									const $item = document.createElement("li");
									$item.className = "filter-value__item item_hidden";
									$item.setAttribute("filter-value", x.target.value);
									createHTMLElement("div", $item, "input-custom-style", null, function (el) {
										createHTMLElement("input", el, "", null, function (el) {
											el.type = "checkbox";
										});
										createHTMLElement("div", el, "material-icons custom-style");
									});
									createHTMLElement("div", $item, "filter-value__option filter-value__option-style", x.target.value);
									createHTMLElement("div", $item, "filter-value__stats");
									createHTMLElement("i", $item, "material-icons filter-value__cancel", "");
									if(addPosition === "pre"){
										if($item.children[1]){// 0号位置是输入框
											$list.insertBefore($item, $list.children[1]);
										}
									}else{
										$list.appendChild($item, $createBox);
									}
									$self.bindFilterEvents();
									if(isSelected){
										$item.classList.add("option_selected");
										$item.getElementsByTagName("input")[0].checked = true;
										$self.sendRequestForList();
									}
									// $self.sendRequestForStats();
								}
								setTimeout(function () {
									$input.setAttribute("code", "");
									$input.value = null;
								}, 0);
							}
						}
					});
				});
				
				const $config = {
						attributes: true,
						attributeFilter: ["code"]
					};
				const beginobserve = function () {
						$observer.observe($input, $config);
					};
				const manualComplete = function(e){
					const $value = $input.value.trim();
					if($value==null||$value==""){
						return false;
					}
					var event = e.which || e.keyCode;
					if(event == 13){
						if (getSingleFilterSectionValues(o).indexOf($value) === -1) {
							const $item = document.createElement("li");
							$item.className = "filter-value__item item_hidden";
							$item.setAttribute("filter-value", $value);
							createHTMLElement("div", $item, "input-custom-style", null, function (el) {
								createHTMLElement("input", el, "", null, function (el) {
									el.type = "checkbox";
								});
								createHTMLElement("div", el, "material-icons custom-style");
							});
							createHTMLElement("div", $item, "filter-value__option  filter-value__option-style", $value);
							createHTMLElement("div", $item, "filter-value__stats");
							createHTMLElement("i", $item, "material-icons filter-value__cancel", "");
							if(addPosition === "pre"){
								if($item.children[1]){// 0号位置是输入框
									$list.insertBefore($item, $list.children[1]);
								}
							}else{
								$list.appendChild($item, $createBox);
							}
							$self.bindFilterEvents();
							if(isSelected){
								$item.classList.add("option_selected");
								$item.getElementsByTagName("input")[0].checked = true;
								$self.sendRequestForList();
							}
							$input.value = null;
						}
					}
				}
				addSpecificEventListener($input, "focus", "beginobserve", beginobserve);
				addSpecificEventListener($input, "keydown", "manualComplete",manualComplete);
				return true;
			}
			// 直接显示input end
			createHTMLElement("div", $list, "filter-section__create", null, function (el) {
				createHTMLElement("button", el, "filter-create__add button_main button_primary", null, function (el) {
					el.textContent = multilingual.add;
					const $icon = document.createElement("i");
					$icon.className = "material-icons";
					$icon.textContent = "add";
					el.insertBefore($icon, el.firstChild);
				});
				createHTMLElement("div", el, "filter-create__input", null, function (el) {
					createHTMLElement("input", el, "js_autocompletebox", null, function (el) {
						el.setAttribute("request-url", url);
						el.setAttribute("manual-input", "no");
					});
				});
			});

			const $createBox = o.getElementsByClassName("filter-section__create")[0];
			const $addButton = o.getElementsByClassName("filter-create__add")[0];
			const $input = o.getElementsByClassName("js_autocompletebox")[0];
			const $observer = new MutationObserver(function (mutations) {
				mutations.forEach(function (x) {
					if (x.attributeName === "code") {
						if (x.target.getAttribute("code") !== "") {
							$observer.disconnect();

							// 没有被选择的时候才执行
							if (getSingleFilterSectionValues(o).indexOf(x.target.getAttribute("code")) === -1) {
								// 创建词条
								const $item = document.createElement("li");
								$item.className = "filter-value__item item_hidden";
								$item.setAttribute("filter-value", x.target.getAttribute("code"));
								createHTMLElement("div", $item, "input-custom-style", null, function (el) {
									createHTMLElement("input", el, "", null, function (el) {
										el.type = "checkbox";
									});
									createHTMLElement("div", el, "material-icons custom-style");
								});
								createHTMLElement("div", $item, "filter-value__option", x.target.value);
								createHTMLElement("div", $item, "filter-value__stats");
								createHTMLElement("i", $item, "material-icons filter-value__cancel", "close");
								$list.insertBefore($item, $createBox);

								$self.bindFilterEvents();
								$self.sendRequestForStats();
							}
							setTimeout(function () {
								$createBox.classList.remove("input_shown");
								$input.setAttribute("code", "");
								$input.value = null;
							}, 0);
						}
					}
				});
			});
			const $config = {
				attributes: true,
				attributeFilter: ["code"]
			};

			const addItemEvent = function () {
				$createBox.classList.add("input_shown");
				$observer.observe($input, $config);
				$input.focus();
			};
			/*
             * const inputBlurEvent = function () { setTimeout(function () {
             * $createBox.classList.remove("input_shown");
             * $input.setAttribute("code", ""); $input.value = null; }, 0); };
             */
			addSpecificEventListener($addButton, "click", "addItemEvent", addItemEvent);
			// addSpecificEventListener($input, "blur", "inputBlurEvent",
            // inputBlurEvent);

			const documentClickBlurEvent = function (e) {
				const $activeBox = document.getElementsByClassName("filter-section__create input_shown")[0];
				if ($activeBox) {
					const $input = $activeBox.getElementsByTagName("input")[0];
					if ($activeBox && !document.getElementsByClassName("background-cover cover_transparent")[0] && e.target.closest(".filter-section__create") !== $activeBox) {
						$activeBox.classList.remove("input_shown");
						$input.setAttribute("code", "");
						$input.value = null;
					}
				}
			};
			addSpecificEventListener(document, "click", "documentClickBlurEvent", documentClickBlurEvent);
		}

		if ($self.filtermap) {
			var $callbackCount = 0;
			const $totalDynamicSection = $self.filtermap.size;

			$self.filtermap.forEach(function (value, key) {
				// 每个过滤条件url, name和code都是固定且必要属性，定义成不可枚举
				Object.defineProperty(value, "url", {
					enumerable: false
				});
				Object.defineProperty(value, "acURL", {
					enumerable: false
				});
				Object.defineProperty(value, "name", {
					enumerable: false
				});
				Object.defineProperty(value, "code", {
					enumerable: false
				});
				Object.defineProperty(value, "acType", {
					enumerable: false
				});
				const $list = key.getElementsByClassName("filter-value__list")[0]; // 找到对应的过滤条件列表
				const $matchedUrl = value.url;
				const $matchedName = value.name; // 返回String
				const $matchedCode = value.code; // 返回String
				const $data = $self.listdata;

				var xhr = new XMLHttpRequest();
				xhr.open("POST", $matchedUrl);
				xhr.setRequestHeader("x-requested-with", "XMLHttpRequest");
				xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
				xhr.onreadystatechange = function () {
					if (xhr.readyState === 4) {
						var $timeOutCheck = false;
						if (xhr.responseText === '{"ajaxSessionTimeOut":"yes"}') {
							$timeOutCheck = true;
						}
						if (!$timeOutCheck && xhr.responseText !== null) {
							$timeOutCheck = xhr.responseText.ajaxSessionTimeOut;
						}
						if ($timeOutCheck) {
							jConfirm("系统超时或未登录，你要登录吗？", "提示", function (r) {
								if (r) {
									document.location.href = window.location.href;
									return 0;
								}
							});
						} else {
							while ($list.firstChild) {
								$list.removeChild($list.firstChild);
							}
							const $returnDataArray = JSON.parse(xhr.responseText); // 把返回的JSON转成数组

							// 遍历数组，如果有与过滤条件相匹配的，则创建一个过滤条件值的HTMLElement
							$returnDataArray.forEach(function (x) {
								createHTMLElement("li", $list, "filter-value__item", null, function (el) {
									el.setAttribute("filter-value", x[$matchedCode]);
									createHTMLElement("div", el, "input-custom-style", null, function (el) {
										createHTMLElement("input", el, "", null, function (el) {
											el.type = "checkbox";
										});
										createHTMLElement("div", el, "material-icons custom-style");
									});
									createHTMLElement("div", el, "filter-value__option filter-value__option-style", x[$matchedName]);
									createHTMLElement("div", el, "filter-value__stats");
									createHTMLElement("i", el, "material-icons filter-value__cancel", value.acType =="input"?"":"close");
								});
							});
							$callbackCount++;
							if (value.acURL) {
								createAddingItemModule(key, value);
							}
							if ($callbackCount === $totalDynamicSection) {
								$self.filtersections.forEach(function (x) {
									addFormElementsEvents(x);
								});
								$self.sendRequestForList();
								$self.sendRequestForStats();
								$self.initializeDrawer();
								$self.bindFilterEvents();
								$self.bindSearchEvents();
							}
						}
					}
				};
				xhr.send(convertToFormData($data));
			});
		}
	};

	/**
     * 获取单个filter section的所有过滤选项
     * 
     * @param {Object}
     *            o 指向filter section的HTMLElement
     * 
     * @return {String}
     */
	function getSingleFilterSectionValues(o) {
		var $sectionAllValues = [];
		Array.from(o.getElementsByClassName("filter-value__item")).forEach(function (x) {
			$sectionAllValues = $sectionAllValues.concat(x.getAttribute("filter-value").split(","));
		});
		return $sectionAllValues.join(",");
	}

	/**
     * 生成参数请求对象
     * 
     * @param {Constructor}
     *            constructor 构造对象
     * @param {Number}
     *            page 所需请求的页码
     * 
     * @return {Object}
     */
	function composeRequestValues(constructor, page) {
		constructor.setStaticNodeList();
		const $postValues = {};

		/**
         * 获取单个过滤条件所需请求的参数值
         * 
         * @param {Object}
         *            o 过滤条件所在的DOM对象
         * 
         * @return {String}
         */
		function getFilterSelectedValues(o) {
			const $selectedArray = [];
			Array.from(o.getElementsByClassName("option_selected")).forEach(function (x) {
				if (x.getAttribute("filter-value") !== "") {
					$selectedArray.push(x.getAttribute("filter-value"));
				}
			});
			var value = $selectedArray.join(",");
			if($.trim(value) != ""){
                value = $.trim(value);
                //去除开头位置的逗号
                if(value.indexOf(",") == 0){
                    value = value.substring(1,value.length);
                }
            }
			return value;
		}

		function getAllFilterValues() {
			const $allValue = {};
			constructor.filtersections.forEach(function (x) {
				if (x.getAttribute("filter-method") === ("multiple" || "single")) {
					$allValue[x.getAttribute("filter-section")] = getSingleFilterSectionValues(x);
				}
			});
			return JSON.stringify($allValue);
		}

		// 遍历所有的过滤条件，把每个条件和所选择的值存入输出对象
		constructor.filtersections.forEach(function (x) {
			$postValues[x.getAttribute("filter-section")] = getFilterSelectedValues(x);
		});
		// 定义所有的可过滤条件
		$postValues["allFilterValues"] = getAllFilterValues();
		// 获取检索条件
		if (constructor.searchfield) {
			var searchtext = constructor.searchfield.getElementsByTagName("input")[0].value.trim();
			// 最多只能传到后台50个字符检索
			if (searchtext.length > 50) {
				searchtext = searchtext.substr(0, 50);
			}
			// 检索条件为空时不要触发
			if(!!searchtext)
				$postValues["searchKey"] = searchtext;
		}
		// 获取列表总条数和目标页码
		const $listInfo = constructor.primarylist.getElementsByClassName("js_listinfo")[0];
		const $total = $listInfo ? parseInt($listInfo.getAttribute("smate_count")) : -1;
		$postValues["page.totalCount"] = $total;
		$postValues["page.pageNo"] = page || 1;
		$postValues["currentLoad"] = constructor.primarylist.getElementsByClassName("main-list__item").length;
		return $postValues;
	}

	// 存储异步请求对象，用来在有新请求时中止之前的请求
	const $xhrArray = [];
	const $xhrStatsArray = [];
	var $xhrSingleRequest = false; // 滚动加载时单个请求
	// 所有需要统一回掉的异步请求的数组
	var $allAsynXhrRequestArray = [];
	/**
     * 添加异步请求
     * 
     * @param {XMLHttpRequest}
     *            xhr 异步请求对象
     */
	function addAsynXhrRequest(xhr) {
		const $object = {};
		$object.status = 0; // 发送请求的时候默认初始化为0
		$object.xhr = xhr;
		$allAsynXhrRequestArray.push($object); // 每发送一个异步请求就在该数组中添加这个异步请求
	}

	/**
     * 移除异步请求， 在中止请求的时候移除
     * 
     * @param {XMLHttpRequest}
     *            xhr 异步请求对象
     */
	function removeAsynXhrRequest(xhr) {
		$allAsynXhrRequestArray.forEach(function (o, idx) {
			if (o.xhr === xhr) {
				$allAsynXhrRequestArray.splice(idx, 1);
			}
		});
	}
	/**
     * 请求完成后调用请求。状态为4
     * 
     * @param {XMLHttpRequest}
     *            xhr 异步请求对象
     * @param {Function}
     *            callback 该异步请求的回掉方法
     */
	function callBackAsynXhrRequest(xhr, callback) {
		var $allRequestFinished = true;
		$allAsynXhrRequestArray.forEach(function (o) {
			// 成功返回数据的时候，修改状态为4，且添加回掉的方法
			if (o.xhr === xhr) {
				o.status = 4;
				o.callback = callback;
			}
			// 如果有任一个异步对象还没有返回则修改判断为false
			if (o.status !== 4) {
				$allRequestFinished = false;
			}
		});
		// 当所有请求全部返回成功时，执行每一个请求的回掉函数，并重置数组
		if ($allRequestFinished) {
			const $processXHR = []; // 因执行回调时可能会改变数组，先把需要同步回调的数组预先存储
			$allAsynXhrRequestArray.forEach(function (o) {
				$processXHR.push(o.xhr);
			});
			$allAsynXhrRequestArray.forEach(function (o) {
				if(o.callback != undefined){
                    o.callback();
                }else{
					console.log("undefined= o.callback");
				}
			});
			// 全部回调完成后只移除之前的XHR，在回调中新增的XHR不会从数组移除，确保之后回调会正确执行
			$processXHR.forEach(function (x) {
				removeAsynXhrRequest(x);
			});
		}
	}

	/**
     * 请求列表并显示
     * 
     * @param {Number}
     *            page 所需请求的页码
     * @param {Object}
     *            data 除listdata属性以外的需请求的参数
     */
	Mainlist.prototype.sendRequestForList = function (page, data) {
		page = page || 1;
		const $self = this;
		$self.setStaticNodeList();
		const $data = Object.assign(composeRequestValues($self, page), $self.listdata, data); // 拼接所有的请求数据成一个对象
		const $list = $self.primarylist; // 主列表的静态HTMLELement
		const $listParent = $list.parentElement;
		const $preloader = $list.getElementsByClassName("preloader active"); // 加载圈的动态NodeList
		// 定义滚动加载的事件
		const loadOnScroll = function () {
			const $currentItemNo = $list.getElementsByClassName("main-list__item").length; // 获取列表当前数目
			const $maxScrollEvent = function () { // 定义滚动到底部的加载事件
				if (!$xhrSingleRequest && parseInt($list.getAttribute("total-count")) > $currentItemNo) {
					$self.sendRequestForList(Math.ceil($currentItemNo / 10) + 1);
				}
			};
			// 判断元素是否隐藏，如隐藏则移除滚动事件
			if ($list.offsetParent) {
				// 判断列表是否滚动到底部
				if ($listParent.scrollHeight <= $listParent.clientHeight) { // 列表没有限制最大高度，此时滚动监听列表底部是否在窗口之上
					if (window.innerHeight + 10 >= $list.getBoundingClientRect().bottom) {
						$maxScrollEvent();
					}
				} else { // 列表有限制最大高度，通常处于浮层中的列表，监听列表是否滚动到底部
					if ($listParent.scrollHeight - $listParent.scrollTop - $listParent.clientHeight <= 10) {
						$maxScrollEvent();
					}
				}
			} else {
				if ($list.getAttribute("scroll-attached") === "window") {
					removeSpecificEventListener(window, "scroll", "loadOnScroll");
					$list.setAttribute("scroll-listener", "deactive");
				} else {
					removeSpecificEventListener($list.parentElement, "scroll", "loadOnScroll");
					$list.setAttribute("scroll-listener", "deactive");
				}
			}
		};

		// 中止之前存在的相同列表异步请求
		$xhrArray.forEach(function (x) {
			x.abort();
			removeAsynXhrRequest(x);
		});
		$xhrSingleRequest = true;
		$self.beforeSend();
		var xhr = new XMLHttpRequest();
		xhr.open("POST", $self.listurl);
		xhr.setRequestHeader("x-requested-with", "XMLHttpRequest");
		xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		xhr.onreadystatechange = function () {
			if (xhr.readyState === 4) {
				const requestForListCallBack = function () {
					// 如果没有返回任何数据，则不会触发回调方法
					if (xhr.responseText === "") {
						return;
					}
					// 超时判断
					var $timeOutCheck = false;
					if (xhr.responseText === '{"ajaxSessionTimeOut":"yes"}') {
						$timeOutCheck = true;
					}
					if (!$timeOutCheck && xhr.responseText !== null) {
						$timeOutCheck = xhr.responseText.ajaxSessionTimeOut;
					}
					if ($timeOutCheck) {
						jConfirm("系统超时或未登录，你要登录吗？", "提示", function (r) {
							if (r) {
								document.location.href = window.location.href;
								return 0;
							}
						});
					} else {
						$xhrSingleRequest = false; // 发送一次之后重置单次请求变量
						const $parser = new DOMParser();
						const $xhrDOM = $parser.parseFromString(xhr.responseText, "text/html");
						var listWithMenu=false;
						var contentList=null;
						var leftMenu=null;
						if($xhrDOM.getElementById("listWithMenu")){//是否是列表数据和菜单栏一起返回
						  listWithMenu=true;
						  contentList=$xhrDOM.getElementById("content_list");
						  leftMenu=$xhrDOM.getElementById("left_menu");
						}
						try {
							if ($xhrDOM.getElementsByClassName("js_listinfo").length === 0) {
								throw "The response of list query must contain total item count.";
							}
						} catch (e) {
							console.error(e);
						}
						if ($list) {
							// 根据返回的数据获取列表查询出的总数
							const $responseAttr = $xhrDOM.getElementsByClassName("js_listinfo"); 
							const $total = $responseAttr.length !== 0 ? parseInt($responseAttr[0].getAttribute("smate_count")) : -1;
							var responseTextStr=(listWithMenu==true?contentList.innerHTML:xhr.responseText);
							// 没有总数返回的时候可能是数据出错
							const $responseText = $total === -1 ? '<div class="response_no-result">' + multilingual.paperDataError + '</div>' : responseTextStr;  //
							if ($total >= 0) {
								$list.setAttribute("total-count", $total); // 把总数设置成列表的一个属性
							}
							// 当总数为0的时候显示记录不存在的提示
							if ($total === 0) {
								while ($list.firstChild) {
									$list.removeChild($list.firstChild);
								}
								createHTMLElement("div", $list, "response_no-result", null, function (el) {
									if ($self.noresultHTML) {
										el.innerHTML = $self.noresultHTML;
									  /*
                                         * el.innerHTML = ' <div
                                         * class="no_effort">' +'<h2 class="tc">' +
                                         * $self.noresultHTML +'</h2>' +'<div
                                         * class="no_effort_tip pl27">' +'</div>' +'</div>';
                                         */
									} else {
										
									    var tipcontent = multilingual.paperDataNoResult;
									    var subcontent = document.createElement("div");
									    subcontent.className = "no_effort";
									    subcontent.innerHTML = tipcontent;
									   
									    if( $list.getAttribute("list-main") == "addPubList"){
									        if(document.getElementById("pubSearchInput").value == ""){
									            el.innerHTML = multilingual.dataError;
									        }else{
									            el.appendChild(subcontent);
									        }
									       
									    }else{
									        if ($self.listurl == "/dynweb/grpdyn/outside/ajaxshow") {
									          el.innerHTML = multilingual.grpOutsideNoRecord;
									        } else {
									          el.innerHTML = multilingual.noresultHint;
									        }
									    }
									    
									    /*
                                         * el.innerHTML =
                                         * multilingual.dataError;
                                         */
									  /* el.innerHTML = tipcontent; */
									}
								});
								$self.listcallback(xhr);
							} else {
								if ($self.method === "scroll") {
									// 如果请求的是第一页，覆盖整个HTML，否则添加到后面
									if($preloader[0]){
										$preloader[0].parentNode.removeChild($preloader[0]);										
									}
									$list.insertAdjacentHTML('beforeend', $responseText);
									$self.listcallback(xhr);
									// 因每次返回的时候都会返回统计数节点，移除多于的节点
									Array.from($list.getElementsByClassName("js_listinfo")).forEach(function (x, idx) {
										if (idx > 0) {
											x.parentNode.removeChild(x);
										}
									});
									// 仅当总数大于10的时候，才给window绑定滚动加载事件
									if (parseInt($list.getAttribute("total-count")) > 10) {
										// 判断列表是处于window还是浮层中，如父元素scrollHeight>clientHeight则绑定再父元素上，否则绑定window
										if ($listParent.scrollHeight <= $listParent.clientHeight) {
											// 即使scrollHeight<=clientHeight，也有可能是因为一次请求没有塞满列表
											// 再次判断列表是否还可以继续显示，即列表底部高于浏览器底部时再次请求，直至列表没有在页面上显示完全
											if ($list.getBoundingClientRect().bottom <= window.innerHeight) {
												$self.sendRequestForList(Math.ceil(page + 1));
											} else {
												if ($list.getAttribute("scroll-listener") !== "active") {
													addSpecificEventListener(window, "scroll", "loadOnScroll", loadOnScroll);
													$list.setAttribute("scroll-listener", "active");
													$list.setAttribute("scroll-attached", "window");
												}
											}
										} else {
											if ($list.getAttribute("scroll-listener") !== "active") {
												addSpecificEventListener($listParent, "scroll", "loadOnScroll", loadOnScroll);
												$list.setAttribute("scroll-listener", "active");
												$list.setAttribute("scroll-attached", "parent");
											}
										}
									}
									// 如果加载完了就移除滚动事件
									if (parseInt($list.getAttribute("total-count")) <= $list.getElementsByClassName("main-list__item").length) {
										if ($list.getAttribute("scroll-listener") === "parent") {
											removeSpecificEventListener($list.parentElement, "scroll", "loadOnScroll");
										} else {
											removeSpecificEventListener(window, "scroll", "loadOnScroll");
										}
									}
								}
								if ($self.method === "pagination") {
									// 判断请求是否超过了最大页数，如果超过最大页数则请求最后一页
									const $maxPage = Math.ceil(parseInt($list.getAttribute("total-count") || 10) / 10);
									if (page > $maxPage) {
										$self.sendRequestForList($maxPage);
									} else if($preloader[0] !=  undefined){
										$preloader[0].parentNode.removeChild($preloader[0]);
										$list.innerHTML = $responseText;
										$self.listcallback(xhr);
										// 如果有批量选择框则绑定复选框事件
										try{
										  if($self.drawermethods){
										    bindCheckBoxEvent($self);
										  }
										  createPagination($self, page);
										}catch(e){
										}
									}
								}
							}
						}
  					if(listWithMenu&&$(".cont_l")){//如果存在左侧分类栏且有数据，就将数据填充
  					    $(".cont_l").html(leftMenu.innerHTML);
  					}
					}
				};
				callBackAsynXhrRequest(xhr, requestForListCallBack);
			}
		};

		if (!($self.method === "scroll" && page > 1 && (parseInt($list.getAttribute("total-count")) <= $list.getElementsByClassName("main-list__item").length))) {
			xhr.send(convertToFormData($data));

			// 发送请求之后出现加载请求圈
			// 当是滚动加载且请求第一页，或是翻页加载的时候清空列表除加载圈之外的所有子节点
			if (($self.method === "scroll" && page === 1) || $self.method === "pagination") {
				if ($preloader.length === 0) { // 此判断是为了连续触发请求时不会重构加载圈
					while ($list.firstChild) {
						$list.removeChild($list.firstChild);
					}
				}
				if ($self.method === "scroll") {
					$list.setAttribute("scroll-listener", "deactive");
				}
			}
			// 不存在加载圈外框的时候会新构建一个
			if ($preloader.length === 0) {
				createHTMLElement("div", $list, "preloader active", null);
				createIndeterminateCirclePreloader($preloader[0]);
			}
			// 如果请求的是第一页先移除翻页框内容
			if (page === 1 && $self.method === "pagination") {
				while ($self.paginationfield.firstChild) {
					$self.paginationfield.removeChild($self.paginationfield.firstChild);
				}
			}
			$xhrArray.push(xhr);
			addAsynXhrRequest(xhr);
		}
	};



	/**
     * 请求列表过滤条件的统计数
     */
	Mainlist.prototype.sendRequestForStats = function () {
		const $self = this;
		// 仅在需要统计数的时候才执行
		if ($self.statsurl) {
			const $data = Object.assign(composeRequestValues($self), $self.listdata);
			$xhrStatsArray.forEach(function (x) {
				x.abort();
				removeAsynXhrRequest(x);
			});
			var xhr = new XMLHttpRequest();
			xhr.open("POST", $self.statsurl);
			xhr.setRequestHeader("x-requested-with", "XMLHttpRequest");
			xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			xhr.onreadystatechange = function () {
				if (xhr.readyState === 4) {
					const requestForStatsCallBack = function () {
						var $timeOutCheck = false;
						if (xhr.responseText === '{"ajaxSessionTimeOut":"yes"}') {
							$timeOutCheck = true;
						}
						if (!$timeOutCheck && xhr.responseText !== null) {
							$timeOutCheck = xhr.responseText.ajaxSessionTimeOut;
						}
						if ($timeOutCheck) {
							jConfirm("系统超时或未登录，你要登录吗？", "提示", function (r) {
								if (r) {
									document.location.href = window.location.href;
									return 0;
								}
							});
						} else {
							displayFilterStats($self, xhr.responseText);
						}
					};
					callBackAsynXhrRequest(xhr, requestForStatsCallBack);
				}
			};
			xhr.send(convertToFormData($data));
			$xhrStatsArray.push(xhr);
			addAsynXhrRequest(xhr);
		}
	};

	/**
     * 显示统计数
     * 
     * @param {Constructor}
     *            constructor 构造对象
     * @param {JSON}
     *            data 返回的统计数数据
     */
	function displayFilterStats(constructor, data) {
		constructor.setStaticNodeList();
		if (data) {
			const $statsObj = JSON.parse(data);
			// 遍历筛选条件组，匹配相对应的返回数据
			constructor.filtersections.forEach(function (x) {
				const $sectionId = x.getAttribute("filter-section");
				const $matchedData = $statsObj[$sectionId];
				const $childrenNodesArray = Array.from(x.getElementsByClassName("filter-value__item"));
				// 每个条件组的选项循环遍历
				$childrenNodesArray.forEach(function (x) {
					const $optionStats = x.getElementsByClassName("filter-value__stats")[0];
					if ($optionStats && $matchedData) {
					// SCM-14560
						if(!x.classList.contains("not_limit")){
							const $valueArray = x.getAttribute("filter-value").split(","); // 获取每个过滤条件值可能含有多个条件值
							var $valueSum = 0;
							// 返回的数据和条件值匹配，统计数为所有包含值的和
							$valueArray.forEach(function (x) {
								$valueSum = $valueSum + parseInt(($matchedData[x] || 0));
							});
							// 如果统计数为0，改变样式为不可点击
							if ($valueSum === 0) {
								x.classList.add("option_no-data");
							} else {
								x.classList.remove("option_no-data");
							}
							$optionStats.textContent = '(' + $valueSum + ')';
						}else{
							// 处理不限的选项统计数
							var $valueSum = parseInt(($matchedData["noLimit"] || 0));
							// 如果统计数为0，改变样式为不可点击
							if ($valueSum === 0) {
								x.classList.add("option_no-data");
							} else {
								x.classList.remove("option_no-data");
							}
							$optionStats.textContent = '(' + $valueSum + ')';
						}
					}
				});
			});
		}
	}

	/**
     * 绑定过滤条件事件
     */
	Mainlist.prototype.bindFilterEvents = function () {
		const $self = this;
		$self.setStaticNodeList();
		// 遍历每一个过滤选项，绑定选中和取消的事件
		$self.filtervalues.forEach(function (x) {
			const optionEvent = function () {
				const $matchedSection = x.closest(".filter-list__section");
				const $allValueItems = $matchedSection.getElementsByClassName("filter-value__item");
				switch ($matchedSection.getAttribute("filter-method")) {
					case "single":
						const option_selected = x.classList.contains("option_selected");
						Array.from($allValueItems).forEach(function (el) {
							el.classList.remove("option_selected");
							el.getElementsByTagName("input")[0].checked = false;
						});
						if(x.getElementsByClassName("filter-value__cancel")[0].innerHTML.trim() == ""
							||x.getElementsByClassName("filter_need_cancel").length>0){
							// 没有取消按钮时 再次点击取消
							if (!option_selected) {
								x.classList.add("option_selected");
								x.getElementsByTagName("input")[0].checked = true;
							} else {
								x.classList.remove("option_selected");
								x.getElementsByTagName("input")[0].checked = false;
							}
						}else{
							x.classList.add("option_selected");
							x.getElementsByTagName("input")[0].checked = true;
						}
						$self.sendRequestForList(); // 更换过滤条件的时候默认从第一页开始显示
						$self.sendRequestForStats();
						break;
					case "compulsory":
						Array.from($allValueItems).forEach(function (el) {
							el.classList.remove("option_selected");
							el.getElementsByTagName("input")[0].checked = false;
						});
						x.classList.add("option_selected");
						x.getElementsByTagName("input")[0].checked = true;
						$self.sendRequestForList(); // compulsory通常用于排序，排序不会变更统计数，不需要额外请求
						break;
					case "multiple":
					case "master":
						if (!x.classList.contains("option_selected")) {
							x.classList.add("option_selected");
							x.getElementsByTagName("input")[0].checked = true;
						} else {
							x.classList.remove("option_selected");
							x.getElementsByTagName("input")[0].checked = false;
						}
						$self.sendRequestForList();
						$self.sendRequestForStats();
						break;
				}
			};
			const cancelEvent = function () {
				x.classList.remove("option_selected");
				x.getElementsByTagName("input")[0].checked = false;
				$self.sendRequestForList();
				$self.sendRequestForStats();
			};
			// 有些custom
            // style样式中，可能filter-value__item和filter-value__option会在一个节点上，所以绑定的时候做判断
			const $optionElement = x.classList.contains("filter-value__option") ? x : x.getElementsByClassName("filter-value__option")[0];
			addSpecificEventListener($optionElement, "click", "optionEvent", optionEvent);
			// 如需要，则绑定取消事件
			if (x.getElementsByClassName("filter-value__cancel")[0]) {
				addSpecificEventListener(x.getElementsByClassName("filter-value__cancel")[0], "click", "cancelEvent", cancelEvent);
			}
		});
	};

	/**
     * 根据总条数和当前页生成页码数组
     * 
     * @param {Number}
     *            total 列表总条数
     * @param {Number}
     *            current 当前页码
     * 
     * @return {Array}
     */
	function createPaginationPageArray(total, current) {
		const a = [];
		const $current = current || 1;
		const $totalPage = Math.ceil(total / 10);
		for (var i = 0; i < $totalPage; i++) {
			a.push(i + 1);
		}
		// 总页数不小于10页的时候，中间会用省略号表示。省略号至少表示有两个页码的省略，前后最小保持页码为2，中间为3
		if ($totalPage < 10) {
			return a;
		} else {
			switch (true) {
				case $current <= 5:
					a.splice(6);
					a.push('...', $totalPage - 1, $totalPage);
					break;
				case $current > ($totalPage - 5):
					a.splice(0, $totalPage - 6);
					a.unshift(1, 2, '...');
					break;
				case $current > 5 && $current <= ($totalPage - 5):
					a.splice(0, $current - 2);
					a.splice(3);
					a.unshift(1, 2, '...');
					a.push('...', $totalPage - 1, $totalPage);
					break;
			}
			return a;
		}
	}

	/**
     * 构造翻页
     * 
     * @param {Constructor}
     *            constructor 构造对象
     * @param {Number}
     *            page 当前页码
     */
	function createPagination(constructor, page) {
		constructor.setStaticNodeList();
		const $paginationField = constructor.paginationfield;
		var $total;
		try {
			$total = parseInt(constructor.primarylist.getElementsByClassName("js_listinfo")[0].getAttribute("smate_count"));
		} catch (e) {
			$total = 0;
		}
		
		const $currentPage = page || 1;
		// 清空页码区域预备重新构造
		while ($paginationField.firstChild) {
			$paginationField.removeChild($paginationField.firstChild);
		}
		// 当页码数超过一页的时候才会构造翻页区域
		if (Math.ceil($total / 10) > 1) {
			modifyPagination($total, $currentPage);
		}

		/**
         * 构造翻页区域并绑定事件
         * 
         * @param {Number}
         *            total 列表总条数
         * @param {Number}
         *            page 当前页码
         */
		function modifyPagination(total, page) {
			createHTMLElement("div", $paginationField, "pagination__per-page");
			createHTMLElement("div", $paginationField, "pagination__main");
			// 创建页码框和跳转框
			const $paginationMain = $paginationField.getElementsByClassName("pagination__main")[0];
			createHTMLElement("div", $paginationMain, "pagination__pages_box");
			// 创建页码框的子元素，包括翻页和页码列表
			createHTMLElement("div", $paginationMain, "pagination__redirect",multilingual.goTo);
			const $pageBox = $paginationMain.getElementsByClassName("pagination__pages_box")[0];
			createHTMLElement("div", $pageBox, "pagination__pages_prev");
			createHTMLElement("ul", $pageBox, "pagination__pages_list");
			createHTMLElement("div", $pageBox, "pagination__pages_next");
			// 绑定向前和向后翻页的事件
			const $prevPage = $pageBox.getElementsByClassName("pagination__pages_prev")[0];
			const $nextPage = $pageBox.getElementsByClassName("pagination__pages_next")[0];
			const pageEvent = function (page) {
				constructor.sendRequestForList(page);
			};
			createHTMLElement("i", $prevPage, "material-icons", "chevron_left", function (el) {
				const prevSendRequest = pageEvent.bind(null, parseInt(page) - 1);
				addSpecificEventListener(el, "click", "prevSendRequest", prevSendRequest);
				if (parseInt(page) === 1) {
					$prevPage.classList.add("item_disabled"); // 当前页为第一页的时候，禁用向前翻页
				}
			});
			createHTMLElement("i", $nextPage, "material-icons", "chevron_right", function (el) {
				const nextSendRequest = pageEvent.bind(null, parseInt(page) + 1);
				addSpecificEventListener(el, "click", "nextSendRequest", nextSendRequest);
				if (parseInt(page) === Math.ceil(total / 10)) {
					$nextPage.classList.add("item_disabled"); // 当前页为最后页的时候，禁用向后翻页
				}
			});
			// 创建页面跳转输入框并绑定事件
			const $redirectInput = document.createElement("input");
			const $redirect = $paginationMain.getElementsByClassName("pagination__redirect")[0];
			const redirectSendRequest = function (e) {
				if (e.keyCode === 13) {
					const $targetPage = $redirectInput.value;
					if (!isNaN($targetPage)) {
						// 修复IE翻页的无响应问题SCM-15759 ajb- 2018-01-06
						$(".pagination__redirect").find("input").focus();
						$(".pagination__redirect").find("input").blur();
						switch (true) {
							case Math.floor($targetPage) < 1: // 输入页码小于1的时候重新定向第1页
								constructor.sendRequestForList();
								break;
							case Math.floor($targetPage) > Math.ceil($total / 10): // 输入大于最大最大页数的时候重新定向最大页
								constructor.sendRequestForList(Math.ceil($total / 10));
								break;
							case Math.floor($targetPage) >= 1 && Math.floor($targetPage) <= Math.ceil($total / 10): // 属于范围内的时候四舍五入
								constructor.sendRequestForList(Math.round($targetPage));
								break;
						}
					} else {
						return false;
					}
				}
			};
			$redirect.appendChild($redirectInput);
			addSpecificEventListener($redirect, "keydown", "redirectSendRequest", redirectSendRequest);
			// 返回页码数组，创建每个页码元素并绑定跳转事件
			const $pageArray = createPaginationPageArray(total, page);
			const $pageList = $pageBox.getElementsByTagName("ul")[0];
			$pageArray.forEach(function (x) {
				if (!isNaN(x)) {
					const pageSendRequest = pageEvent.bind(null, parseInt(x));
					createHTMLElement("li", $pageList, "pagination__pages_item", x, function (el) {
						el.setAttribute("page-value", x);
						addSpecificEventListener(el, "click", "pageSendRequest", pageSendRequest);
						// 如果是当前页会加样式
						if (parseInt(x) === parseInt(page)) {
							el.classList.add("item_selected");
						}
					});
				} else {
					createHTMLElement("li", $pageList, "pagination__pages_item item_ellipsis", x, function (el) {
						el.setAttribute("page-value", x);
					});
				}
			});
		}

	}

	/**
     * 绑定检索请求事件，每输入一个字符都会触发检索
     */
	Mainlist.prototype.bindSearchEvents = function () {
		const $self = this;
		$self.setStaticNodeList();
		if ($self.searchfield) {
			const $searchInput = $self.searchfield.getElementsByTagName("input")[0];
			// 为输入中文定义一个开关变量
			var $inputLock = false;
			const startEvent = function () {
				$inputLock = true;
			};
			const endEvent = function () {
				$inputLock = false;
				// 如果字符串已经没有检索到信息，那么包含这个字符串的其他字符串也不会收到信息，减少请求，下同
				if (!($searchInput.value.trim().indexOf($self.primarylist.getAttribute("search-string")) >= 0 && parseInt($self.primarylist.getAttribute("total-count")) === 0)) {
					$self.sendRequestForList(); // input事件在compositionend之后触发，所以在end的时候也要请求一次
					$self.sendRequestForStats();
				}
				$self.primarylist.setAttribute("search-string", $searchInput.value.trim());
			};
			const inputEvent = function () {
				// 为空就不要在查询了 解决IE 浏览器问题 SCM-13951
				if($searchInput.value.trim() ==="" &&  (  $self.primarylist.getAttribute("search-string") ===""  || $self.primarylist.getAttribute("search-string")===null )  ){
					return ;
				}
				if (!$inputLock) {
					if (!($searchInput.value.trim().indexOf($self.primarylist.getAttribute("search-string")) >= 0 && parseInt($self.primarylist.getAttribute("total-count")) === 0)) {
						$self.sendRequestForList();
						$self.sendRequestForStats();
					}
					$self.primarylist.setAttribute("search-string", $searchInput.value.trim());
				}
			};
			addSpecificEventListener($searchInput, "compositionstart", "startEvent", startEvent);
			addSpecificEventListener($searchInput, "compositionend", "endEvent", endEvent);
			addSpecificEventListener($searchInput, "input", "inputEvent", inputEvent);
		}
	};

	/**
     * 重新刷新当前页，不会重置其他的过滤条件
     */
	Mainlist.prototype.reloadCurrentPage = function () {
		const $self = this;
		$self.setStaticNodeList();
		// 如果有翻页请求当前页，否则请求第一页
		if ($self.paginationfield && $self.paginationfield.firstChild) {
			const $currentPage = $self.paginationfield.getElementsByClassName("item_selected")[0].getAttribute("page-value");
			$self.sendRequestForList(parseInt($currentPage));
		} else {
			$self.sendRequestForList();
		}
	};

	/**
     * 重置所有过滤选项并回到第一页， 并不重置filter-section为master中的选项
     */
	Mainlist.prototype.resetAllFilterOptions = function () {
		const $self = this;
		$self.setStaticNodeList();
		$self.filtervalues.forEach(function (x) {
			if (x.closest(".filter-list__section").getAttribute("filter-method") !== "master") {
				x.classList.remove("option_selected");
			}
		});
		$self.filtersections.forEach(function (x) {
			if (x.getAttribute("filter-method") === "compulsory") {
				x.getElementsByTagName("ul")[0].firstElementChild.classList.add("option_selected");
			}
		});
		if ($self.paginationfield && $self.paginationfield.getElementsByTagName("input")[0]) {
			$self.paginationfield.getElementsByTagName("input")[0].value = "";
		}
		if ($self.searchfield) {
			$self.searchfield.getElementsByTagName("input")[0].value = "";
		}
		$self.sendRequestForList();
		$self.sendRequestForStats();
		$self.initializeDrawer();
	};

	/**
     * 获取批量选择框所选项的全部ID
     * 
     * @return {Array}
     */
	Mainlist.prototype.getDrawerSelectedId = function () {
		const $array = [];
		const $drawerList = document.getElementsByClassName("drawer-batch__box")[0].getElementsByClassName("main-list__item");
		Array.from($drawerList).forEach(function (x) {
			$array.push(x.getAttribute("drawer-id"));
		});
		return $array;
	};

	/**
     * 清空复选框选择列表
     * 
     * @param {Array}
     *            data 所需要清空数据的数组，不传参默认清除全部
     */
	Mainlist.prototype.drawerRemoveSelected = function (data) {
		const $self = this;
		const $allSelected = data || $self.getDrawerSelectedId(); // 所选择的所有ID数组
		const $drawer = document.getElementsByClassName("drawer-batch__box")[0];
		const $drawerItems = Array.from($drawer.getElementsByClassName("main-list__item"));
		const $allCheckBox = Array.from($self.primarylist.getElementsByTagName("input")).filter(function (x) {
			return x.type === "checkbox";
		});
		// 移除列表的选择
		$drawerItems.forEach(function (x) {
			if ($allSelected.indexOf(x.getAttribute("drawer-id")) !== -1) {
				x.parentNode.removeChild(x);
			}
		});
		// 遍历列表的复选框，如果复选框隶属ID选择数组中，则移除复选框的选择
		$allCheckBox.forEach(function (x) {
			const $matchId = x.closest(".main-list__item").getAttribute("drawer-id");
			if ($allSelected.indexOf($matchId) !== -1) {
				x.checked = false;
			}
		});
		drawerShowStats($drawer);
	};

	/**
     * 绑定列表复选框事件
     * 
     * @param {Constructor}
     *            constructor 构造对象
     */
	function bindCheckBoxEvent(constructor) {
		constructor.setStaticNodeList();
		const $allCheckBox = Array.from(constructor.primarylist.getElementsByTagName("input")).filter(function (x) {
			return x.type === "checkbox";
		});
		const $drawer = document.getElementsByClassName("drawer-batch__box")[0];
		const $drawerList = $drawer.getElementsByClassName("main-list__list")[0];
		const $drawerArray = constructor.getDrawerSelectedId();
		$allCheckBox.forEach(function (x) {
			// 先验证是否处于批量选择中，如存在，复选框为选中状态
			const $matchItem = x.closest(".main-list__item");
			const $drawerId = $matchItem.getAttribute("drawer-id");
			if ($drawerArray.indexOf($drawerId) !== -1) {
				x.checked = true;
			}
			// 监听复选框值改变时的事件
			const checkboxEvent = function () {
				if (this.checked === true) {
					
					const $content = $matchItem.getElementsByClassName("main-list__item_content")[0].cloneNode(true); // 复制列表内容、
					// 删除社交化DOM
					if(	document.getElementsByClassName("drawer-batch__tip-container").length!=0){
						document.getElementsByClassName("drawer-batch__tip-container")[0].style.display="block";
					}
					
					const $socialActions = $content.getElementsByClassName("idx-social__list")[0];
					if ($socialActions) {
						$socialActions.parentNode.removeChild($socialActions);
					}
					const $socialTime= $content.getElementsByClassName("file-idx__main_src")[0];
					if ($socialTime) {
						$socialTime.parentNode.removeChild($socialTime);
					}
					// 在框批量构造一个新的列表对象及删除按钮
					const $drawerItem = document.createElement("div");
					$drawerItem.classList.add("main-list__item");
					$drawerItem.setAttribute("drawer-id", $drawerId);

					/*$drawerItem.appendChild($content);*/

					if($content.querySelector(".main-list__item_checkbox")){
					    $content.querySelector(".pub-idx__full-text_box").removeChild($content.querySelector(".main-list__item_checkbox"));
					    $drawerItem.appendChild($content);
					}else if($content.querySelector(".main-list__item_actions")){
					    $content.removeChild($content.querySelector(".main-list__item_actions"));
                        $drawerItem.appendChild($content);
					}else{
					    $drawerItem.appendChild($content);
					}
					createHTMLElement("div", $drawerItem, "main-list__item_actions", null, function (el) {
						createHTMLElement("button", el, "button_main button_icon", null, function (el) {
							createHTMLElement("i", el, "material-icons", "close");
						});
					});

					$drawerList.appendChild($drawerItem);
					drawerShowStats($drawer);
					// 绑定删除事件
					const $cancelButton = $drawerItem.getElementsByTagName("button")[0];
					const deleteEvent = function (e) {
						e.stopPropagation();
						const $listItem = Array.from(constructor.primarylist.getElementsByClassName("main-list__item"));
						$listItem.forEach(function (x) {
							if (x.getAttribute("drawer-id") === $drawerItem.getAttribute("drawer-id")) {
								x.querySelector('input[type="checkbox"]').checked = false;
							}
						});
						$drawerItem.parentNode.removeChild($drawerItem);
						drawerShowStats($drawer);
					};
					addSpecificEventListener($cancelButton, "click", "deleteEvent", deleteEvent);
				} else {
					constructor.drawerRemoveSelected([$drawerId]);
				}
			};
			addSpecificEventListener(x, "change", "checkboxEvent", checkboxEvent);
		});
	}

	/**
     * 更新批量选择统计数
     * 
     * @param {Object}
     *            o 批量选择DOM对象
     */
	function drawerShowStats(o) {
		const $headerStats = o.querySelector(".drawer-batch__header_stats");
		const $maskStats = o.querySelector(".drawer-batch__mask_stats");
		const $total = o.querySelectorAll(".main-list__item").length;
		var checklist = document.getElementsByTagName("input");
		var checked_counts = 0;
		$headerStats.textContent = '(' + $total + ')';
		$maskStats.textContent = $total;
		if ($total === 0) {
			o.classList.add("drawer_folded");
			for(var i = 0; i < checklist.length; i++){
				if(checklist[i].checked){
					checked_counts++
				}
				if(checked_counts=="0"){
					if(document.getElementsByClassName("drawer-batch__tip-container").length!=0){ 
						document.getElementsByClassName("drawer-batch__tip-container")[0].style.display="none";
				    }
				}
			}
 
			setTimeout(function () {
				o.classList.add("drawer_hidden");
			}, 400);
		} else {
			o.classList.remove("drawer_hidden");
		}
	}

	/**
     * 初始化批量选择
     */
	Mainlist.prototype.initializeDrawer = function () {
		const $self = this;
		$self.setStaticNodeList();
		// 移除已经存在的批量选择框
		Array.from(document.getElementsByClassName("drawer-batch__box")).forEach(function (x) {
			x.parentNode.removeChild(x);
		});

		// 仅当需要批量选择框时，才会构造批量选择框
		if ($self.drawermethods) {
			const createDrawer = function () {
				createHTMLElement("div", document.body, "drawer-batch__box drawer_folded drawer_hidden", null, function (el) {
					el.setAttribute("list-drawer", $self.name);
				});
				// 构建批量选择框的子元素
				const $drawer = document.getElementsByClassName("drawer-batch__box")[0];
				createHTMLElement("div", $drawer, "drawer-batch__header", null, function (el) {
					createHTMLElement("div", el, "drawer-batch__header_title",multilingual.batchOperation, function (el) {
						createHTMLElement("span", el, "drawer-batch__header_stats");
					});
					createHTMLElement("button", el, "button_main button_icon", null, function (el) {
						createHTMLElement("i", el, "material-icons", "remove");
					});
				});
				
				createHTMLElement("div", $drawer, "drawer-batch__actions", null, function (el) {
					createHTMLElement("button", el, "button_main button_grey", multilingual.clearList, function (el) {
						el.id = "drawer_clearall";
					});
					if($self.more){
					    createHTMLElement("button", el, "button_main button_grey",multilingual.deleteList,function (el) {
							el.id = "drawer_delall";
						});
					}
					if($self.grpmore){
					    createHTMLElement("button", el, "button_main button_grey",multilingual.deleteList,function (el) {
              el.id = "drawer_grpdelall";
					  });
					}
					createHTMLElement("div", el, "drawer_batch__actions_custom");
				});
				
				createHTMLElement("div", $drawer, "drawer-batch__content", null, function (el) {
					createHTMLElement("div", el, "main-list__list");
				});
				createHTMLElement("div", $drawer, "drawer-batch__mask", null, function (el) {
					createHTMLElement("div", el, "drawer-batch__mask_stats", "0");
				});
				
				/*new add start*/
				createHTMLElement("div", $drawer, "drawer-batch__layer", null, function (el) {
				    var detailtext = '<div class="drawer-batch__layer-header">'+ multilingual.exportcontentdetail +'</div>'
                    +'<div class="drawer-batch__layer-body">'
                    +'<div class="drawer-batch__layer-body_item" value="endNote">EndNote</div>'
                    +'<div class="drawer-batch__layer-body_item" value="excel">Excel</div>'
                    +'<div class="drawer-batch__layer-body_item" value="refWorks">Refworks</div>'
                    +'</div>'
                    +'<div class="drawer-batch__layer-footer">'
                    +'<div class="drawer-batch__layer-footer_cacle">'+ multilingual.exportcancle + '</div>'
                    +'<div class="drawer-batch__layer-footer_sure">'+ multilingual.exportconfirm +'</div>'
                    +'</div>';
				    var setele = document.createElement("div");
				    setele.className = "drawer-batch__layer-container";
				    setele.innerHTML = detailtext;
				    el.appendChild(setele);
                });
	      //绑定选择导出成果类型点击事件
	      $('.drawer-batch__layer-body_item').on("click",function(){
	        $(".drawer-batch__layer-body_item").not(this).removeClass("drawer-batch__layer-body_item-selected");
	        $(this).toggleClass("drawer-batch__layer-body_item-selected");
	       })
	      document.getElementsByClassName("drawer-batch__layer-footer_cacle")[0].addEventListener("click",function(){
             document.getElementsByClassName("drawer-batch__layer")[0].style.display = "none";
        });
        document.getElementsByClassName("drawer-batch__layer-footer_sure")[0].addEventListener("click",function(){
          const $selectedItem = $self.getDrawerSelectedId();
          var exportType = $(".drawer-batch__layer-body_item.drawer-batch__layer-body_item-selected").attr("value");                  
          $self.exportmethods.call(null, $selectedItem,exportType);
        })
				/*new add end*/
			};
			createDrawer();
			// 添加清空列表事件
			const $clearAll = document.getElementById("drawer_clearall");
			const clearAllEvent = function () {
				$self.drawerRemoveSelected();
			};
			addSpecificEventListener($clearAll, "click", "clearAllEvent", clearAllEvent);
			
			
			// 添加批量删除事件
			const $delAll = document.getElementById("drawer_delall");
			const deleteAllEvent = function () {
				const $allSelected =  $self.getDrawerSelectedId(); // 所选择的所有ID数组
				Pub.pubDel($allSelected+"","batch");
				// $self.drawerRemoveSelected();
			};
			if($delAll != null && $delAll != undefined){
				addSpecificEventListener($delAll, "click", "deleteAllEvent", deleteAllEvent);
			}
			
		// 群组成果/文献添加批量删除事件
      const $grpdelAll = document.getElementById("drawer_grpdelall");
      const deleteGrpPubAllEvent = function () {
        const $allGrpSelected =  $self.getDrawerSelectedId(); // 所选择的所有ID数组
        GrpPub.pubDel($allGrpSelected+"");
      };
      if($grpdelAll != null && $grpdelAll != undefined){
        addSpecificEventListener($grpdelAll, "click", "deleteGrpPubAllEvent", deleteGrpPubAllEvent);
      }
			
			// 构建自定义操作
			const $activeDrawer = document.getElementsByClassName("drawer-batch__box")[0];
			const $actionBox = $activeDrawer.getElementsByClassName("drawer_batch__actions_custom")[0];
			const $actionNameArray = Object.keys($self.drawermethods);
			$actionNameArray.forEach(function (x) {
				const actionEvent = function () {
					const $selectedItem = $self.getDrawerSelectedId();
					$self.drawermethods[x].call(null, $selectedItem);
				};
				
				createHTMLElement("button", $actionBox, "button_main button_primary", x, function (el) {
					addSpecificEventListener(el, "click", "actionEvent", actionEvent);
				});
				
				
			});
			// 添加批量选择框展开和收起事件
			const $drawerMask = $activeDrawer.getElementsByClassName("drawer-batch__mask")[0];
			const $drawerHeader = $activeDrawer.getElementsByClassName("drawer-batch__header")[0];
			const $drawerHeaderButton = $drawerHeader.getElementsByClassName("button_main button_icon")[0];
			const maskEvent = function (e) {
				$activeDrawer.classList.remove("drawer_folded");
				$drawerHeader.style.opacity= "1";
				/*$self.drawermethodsmaskback();*/  /*删除弹框内X符号*/
				e.stopPropagation();
				if(document.getElementsByClassName("drawer-batch__tip-container").length!=0){ 
                    document.getElementsByClassName("drawer-batch__tip-container")[0].style.display="none";
                }
			};
			const headerEvent = function (e) {
				$activeDrawer.classList.add("drawer_folded");
				$drawerHeader.style.opacity= "0";
				e.stopPropagation();
			};
			addSpecificEventListener($drawerMask, "click", "maskEvent", maskEvent);
			//addSpecificEventListener($drawerHeader, "click", "headerEvent", headerEvent);
			addSpecificEventListener($drawerHeaderButton, "click", "headerEvent", headerEvent);
		}
	};
	
	/**
     * 将过滤条件保存到cookie中 注意：各浏览器对cookie使用限制不同，常规浏览器 FireFox 50个, IE7以上50个 Chrome
     * 53个 大小不超过4K 4096Byte
     */
	Mainlist.prototype.setCookieValues = function(){
		// 获取所有过滤条件
		const $self = this;
		// 遍历所有 filter-list__section 获取已选条件
		const cookieArray = [];
		const mainlistArray=[];
		$self.filtersections.forEach(function (x) {
			const $allValueItems = x.getElementsByClassName("filter-value__item");
			const $selectedArray = [];
			Array.from($allValueItems).forEach(function (el) {
				if(el.classList.contains("option_selected")){
					$selectedArray.push(el.getAttribute("filter-value"));
				}
			});
			if($selectedArray.length>0){
				mainlistArray.push(x.getAttribute("filter-section"));
				cookieArray.push(x.getAttribute("filter-section") + "="+escape($selectedArray.join(",")));
			}
		});
		// 获取搜索和翻页条件
		if ($self.searchfield) {
			var searchtext = $self.searchfield.getElementsByTagName("input")[0].value.trim();
			if (searchtext.length > 50) {
				searchtext = searchtext.substr(0, 50);
			}
			if(!!searchtext){
				cookieArray.push("searchKey="+escape(searchtext));
				mainlistArray.push("searchKey");
			}
		}
		if ($self.paginationfield && $self.paginationfield.firstChild) {
			const $currentPage = $self.paginationfield.getElementsByClassName("item_selected")[0].getAttribute("page-value");
			cookieArray.push("page="+$currentPage);
			mainlistArray.push("page");
		}
		if(getCookie("mainlist")==null){
			setCookie("mainlist="+escape(mainlistArray.join(",")));
			for(var i = 0,len=cookieArray.length; i < len; i++) {
				setCookie(cookieArray[i]);
			}
		}else{
			// 先删除已有，再存放
			var listArray = unescape(getCookie("mainlist")).split(",");
			for(var i = 0,len=listArray.length; i < len; i++) {
				delCookie(mainlistArray[i]);
			}
			delCookie("mainlist");
			setCookie("mainlist="+escape(mainlistArray.join(",")));
			for(var i = 0,len=cookieArray.length; i < len; i++) {
				setCookie(cookieArray[i]);
			}
		}
	};
	/**
     * 读取并请求cookie中的过滤条件
     */
	Mainlist.prototype.resetCookieValues = function(){
		const $self = this;
		if(getCookie("mainlist")==null)
			return false;
		const listArray = getCookie("mainlist").split(",");
		var page = 1;
        var editPubFlag =$self.listdata.editPubFlag;
		for(var i = 0,len=listArray.length; i < len; i++) {
			const filterKey = listArray[i];
			var filterValueStr = getCookie(listArray[i]);
            if(editPubFlag !=undefined && editPubFlag == true && filterKey=="orderBy"){
            	// 编辑成果，默认排序 2018-11-27
                filterValueStr = "updateDate";
                $self.listdata.editPubFlag = false ;
            }
			// 过滤条件包含搜索条件，页数，和过滤条件
			switch(filterKey){
			case "searchKey" :
				$self.searchfield.getElementsByTagName("input")[0].value = filterValueStr;
				break;
			case "page" :
				page = filterValueStr;
				/*
                 * if ($self.paginationfield &&
                 * $self.paginationfield.firstChild) { var pageitems =
                 * $self.paginationfield.getElementsByClassName("pagination__pages_item");
                 * for(var i=0,len=pageitems.length;i<len;i++){
                 * if(el.getAttribute("page-value") === filterValue){
                 * el.classList.add("item_selected"); } } }
                 */
				break;
			 default:
				 if(filterValueStr!=null){
					 // 虽然使用了3层循环，但是每个循环体一般不超过5条数据，基本不影响速度
						$self.filtersections.forEach(function (x) {// 1层
							const $allValueItems = x.getElementsByClassName("filter-value__item");
							const $selectedArray = [];
							// 2种情况，1.有些是多选的,用','分割, 2.有些是单选，但是value是用','分割的
                            // 比如发表年份（1年，3年，5年）针对多选的进行分割。
							var filterValue = new Array();
							if(x.getAttribute("filter-method") === "multiple" || x.getAttribute("filter-method") === "master")
								filterValue = filterValueStr.split(",");
							else
								filterValue[0] = filterValueStr;
							if(x.getAttribute("filter-section") === filterKey){// 过滤类型匹配才会初始化选中
								Array.from($allValueItems).forEach(function (el) {// 2层
									for(var i=0,len=filterValue.length;i<len;i++){// 3层
										if(el.getAttribute("filter-value") == filterValue[i]){
											el.classList.add("option_selected");
										}
									}
								});
							}
						});
					}
				 break;
			}
			delCookie(listArray[i]);
		}
		delCookie("mainlist");
		$self.setStaticNodeList();
		// 如果有翻页请求当前页，否则请求第一页
		if ($self.paginationfield && $self.paginationfield.firstChild) {
			return page;
		} else {
			return null;
		}
	}
	
	/**
     * 向cookie写过滤条件
     */
	function setCookie(cookieStr) {
	    var Days = 1; // 过期时间 单位/天
	    var exp = new Date(); 
	    exp.setTime(exp.getTime() + Days*24*60*60*1000); 
	    document.cookie = cookieStr + ";expires=" + exp.toGMTString(); 
	};
	/**
     * 获取cookie中的过滤条件
     */
	function getCookie(name){
		if (document.cookie.length>0){
			var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
			if(arr=document.cookie.match(reg))
			    return unescape(arr[2]); 
		}
		return null;
	};
	/**
     * 删除cookie中的过滤条件
     */
	function delCookie(name){
		var exp = new Date();
		exp.setTime(exp.getTime() - 1);
		var cval=getCookie(name);
		if(cval!=null)
			document.cookie= name + "="+cval+";expires="+exp.toGMTString();
	};
	
	window.Mainlist = function (options) {
		return new Mainlist(options);
	};

})(window, document);
