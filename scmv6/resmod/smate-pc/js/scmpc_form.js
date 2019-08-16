/**
 * 列表模块 输出的方法
 * 
 * @method addFormElementsEvents 给指定DOM内的所有输入框，文本框，下拉框，自动填词等绑定相应事件
 * @method createIndeterminateCirclePreloader 添加加载圈
 * @method createIndeterminateLinearPreloader 添加直线型加载
 * @OriginalAuthor shenxingjia
 * @LatestVersion 2017 May 23 (shenxingjia)
 */

(function (window, document) {
	const multilingual = {};
	const multilingualSettings = {
		"January": ["Jan", "\u4e00\u6708"],
		"February": ["Feb", "\u4e8c\u6708"],
		"March": ["Mar", "\u4e09\u6708"],
		"April": ["Apr", "\u56db\u6708"],
		"May": ["May", "\u4e94\u6708"],
		"June": ["Jun", "\u516d\u6708"],
		"July": ["Jul", "\u4e03\u6708"],
		"August": ["Aug", "\u516b\u6708"],
		"September": ["Sep", "\u4e5d\u6708"],
		"October": ["Oct", "\u5341\u6708"],
		"November": ["Nov", "\u5341\u4e00\u6708"],
		"December": ["Dec", "\u5341\u4e8c\u6708"],
		"confirm": ["OK", "\u786e\u8ba4"],
		"cancel": ["Cancel", "\u53d6\u6d88"],
		"clear": ["Clear", "\u6e05\u7a7a"]
	};
	Object.keys(multilingualSettings).forEach(function (x) {
		if (window.locale === "en_US") {
			multilingual[x] = multilingualSettings[x][0];
		} else {
			multilingual[x] = multilingualSettings[x][1];
		}
	});
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
   *          o 所需要绑定事件的HTMLElement对象
   * @param {String}
   *          evt 事件类型名称
   * @param {String}
   *          fname 方法名称
   * @param {Function}
   *          f 定义的具体方法
   */
	function addSpecificEventListener(o, evt, fname, f) {
		removeSpecificEventListener(o, evt, fname); // 添加事件之前也移除，避免重复绑定
		const $object = {};
		$object.node = o;
		$object.eventType = evt;
		$object.functionName = fname;
		$object.function = f;
		if(o != null){
		  o.addEventListener(evt, f);
	    $EventArray.push($object); // 每添加一个监听事件就在数组中添加这个事件的一些属性，方便之后移除
		}
	}
	/**
   * 添加一个监听事件
   * 
   * @param {HTMLElemnt}
   *          o 所需要绑定事件的HTMLElement对象
   * @param {String}
   *          evt 事件类型名称
   * @param {String}
   *          fname 方法名称
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
   *          nodetype 所需创建对象的TagName
   * @param {HTMLElement}
   *          parentElement 所需创建对象的父元素，创建的对象会append到父元素上
   * @param {String}
   *          classList 创建的对象className
   * @param {String}
   *          textContent 创建的对象包含的文字
   * @param {Function}
   *          f 创建的对象所执行的方法
   */
	function createHTMLElement(nodetype, parentElement, classList, textContent, f) {
		textContent = textContent || "";
		classList = classList || "";
		const $childElement = document.createElement(nodetype);
		$childElement.className = classList;
		$childElement.innerHTML = textContent;
		if (f !== undefined) {
			f($childElement);
		}
		parentElement.appendChild($childElement);
	}

	window.addFormElementsEvents = function (o) {
		o = o || document;

		/**
     * 数据校验
     * 
     * @param {HTMLElemnt}
     *          o 所需校验的者输入框或文本框
     */
		function inputValidationCheck(o) {
			// 是否必填判断
			if (o.required === true) {
				if (!o.value.trim()) {
					o.closest(".input__box").classList.add("input_invalid");
				} else {
					o.closest(".input__box").classList.remove("input_invalid");
				}
			}
		}

		const inputFocusEvent = function () {
			this.closest(".input__box").classList.add("input_focused");
			if(this.getAttribute("date-format")=="yyyy-mm"){
				const datepickerEvent = createDatepicker.bind(null,this);
				addSpecificEventListener(this, "click", "datepickerEvent", datepickerEvent);
			}
		};
		const inputBlurEvent = function () {
			if (this.value.trim()) {
				this.closest(".input__box").classList.add("input_not-null");
			} else {
				this.closest(".input__box").classList.remove("input_not-null");
			}
			this.closest(".input__box").classList.remove("input_focused");
			inputValidationCheck(this);
		};

		// 遍历所有的文本框或者输入框，添加相应事件
		Array.from(o.querySelectorAll(".input__box input,.input__box textarea")).forEach(function (x) {
			// 判断是否已经存在数据
			if (x.value.trim()) {
				x.closest(".input__box").classList.add("input_not-null");
			} else {
				x.closest(".input__box").classList.remove("input_not-null");
			}
			addSpecificEventListener(x, "focus", "inputFocusEvent", inputFocusEvent);
			addSpecificEventListener(x, "blur", "inputBlurEvent", inputBlurEvent);
		});

		function textareaAutoresize(o) {
			const $div = o.closest(".input__area").getElementsByClassName("textarea-autoresize-div")[0]; // 自动填词辅助框，用来计算高度
			const $styleArray = ["font", "letter-spacing", "word-spacing"]; // 定义哪些样式需要统一
			// 给辅助框添加与输入框匹配的样式
			$styleArray.forEach(function (x) {
				if (!$div.style[x]) {
					$div.style[x] = window.getComputedStyle(o).getPropertyValue(x);
				}
			});
			if(BaseUtils.replaceHtml){
                o.value = BaseUtils.replaceHtml(o.value);
            }
			$div.innerHTML = o.value + "<br/>";
			o.closest(".input__area").style.height = window.getComputedStyle($div).getPropertyValue("height");
		}

		// 遍历所有文本框，添加自动填词事件
		Array.from(o.querySelectorAll(".input__box textarea")).forEach(function (x) {
			textareaAutoresize(x);
			const autoresizeEvent = textareaAutoresize.bind(null, x);
			addSpecificEventListener(x, "input", "autoresizeEvent", autoresizeEvent);
			addSpecificEventListener(x, "change", "autoresizeEvent", autoresizeEvent);
		});

		const radioInputEvent = function () {
			const $input = this.getElementsByTagName("input")[0];
			if ($input.disabled === false) {
				if ($input.checked === true) {
					if ($input.getAttribute("type") === "checkbox") {
						$input.checked = false;
					}
				} else {
					$input.checked = true;
				}
			}
			var $evt = document.createEvent("HTMLEvents");
			$evt.initEvent("change", true, false);
			$input.dispatchEvent($evt);
		};
		// 遍历所有的单选框和复选框，绑定点击切换事件
		Array.from(o.getElementsByClassName("input-radio__sxn")).forEach(function (x) {
			addSpecificEventListener(x, "click", "radioInputEvent", radioInputEvent);
		});

		const submitCheck = function () {
			const $matchForm = document.querySelector('form[form-id="' + this.getAttribute("submitform-id") + '"]');
			Array.from($matchForm.querySelectorAll(".input__box input,.input__box textarea")).forEach(function (input) {
				inputValidationCheck(input);
			});
		};
		// 遍历所有的提交按钮，校验表单元素
		Array.from(o.querySelectorAll("button[submitform-id]")).forEach(function (x) {
			addSpecificEventListener(x, "mousedown", "submitCheck", submitCheck);
		});

		// 日期选择器添加事件
		Array.from(o.querySelectorAll(".input__box input[datepicker]")).forEach(function (x) {
			const datepickerEvent = createDatepicker.bind(null, x);
			addSpecificEventListener(x, "click", "datepickerEvent", datepickerEvent);
		});

		// 当获取焦点的时候也会弹出时间选择器
		Array.from(o.querySelectorAll(".input__box input[focusdata]")).forEach(function (x) {
            const datepickerEvent = createDatepicker.bind(null, x);
            addSpecificEventListener(x, "focus", "datepickerEvent", datepickerEvent);
        });
	/*
   * Array.from(o.querySelectorAll(".input__box input[focusdata]")).forEach(function (x) { x.onblur =
   * function(){ document.getElementsByClassName("datepicker__box")[0].style.display = "none"; } });
   */
		// 下拉选择框
		Array.from(o.getElementsByClassName("sel__box")).forEach(function (x) {
			selectorDisplayEvent(x);
		});

		// 自动填词框
		Array.from(o.getElementsByClassName("js_autocompletebox")).forEach(function (x) {
			autocompleteDisplayEvent(x);
		});

		function navClickAnimationEvent(o) {
			const $underline = o.getElementsByClassName("nav__underline")[0]; // 选项卡下划线
			const itemEvent = function () {
				const $itemLeft = this.offsetLeft; // 当前元素左侧距离父元素的距离
				const $itemWidth = this.getBoundingClientRect().width; // 当前元素的宽度
				if($underline == undefined){
				  return;
				}
				const $lineCurrentLeft = $underline.offsetLeft; // 下划线左侧距离父元素的距离
				const $selfWidth = this.offsetWidth;
				const $lineCurrentWidth = $underline.getBoundingClientRect().width; // 下划线当前宽度
				const $transitionTiming = 200; // 定义动画时间
				const $transitionStart = "all " + $transitionTiming / 2 + "ms ease-out"; // 拉伸动画
				const $transitionEnd = "all " + $transitionTiming / 2 + "ms ease-in"; // 收缩动画
				// 判断点击的元素是在当前选中左侧还是右侧，不同的拉伸动画参数
				if ($lineCurrentLeft < $itemLeft) {
					$underline.style.cssText = "transition: " + $transitionStart + "; width: " +  ($itemLeft - $lineCurrentLeft + $itemWidth) + "px; left: " + $lineCurrentLeft   + "px";
				} else {
					$underline.style.cssText = "transition: " + $transitionStart + "; width: " +  ($lineCurrentLeft - $itemLeft + $lineCurrentWidth) + "px; left: " + $itemLeft   + "px";
				}
				// 一半时间后收缩动画
				setTimeout(function () {
					$underline.style.cssText = "transition: " + $transitionEnd + "; width: " + $itemWidth + "px; left: " + $itemLeft  + "px";
				}, $transitionTiming / 2);
				// 移除其他元素的选择样式，并给当前添加
				Array.from(o.getElementsByClassName("nav__item")).forEach(function (x) {
					x.classList.remove("item_selected");
				});
				this.classList.add("item_selected");
			};
			Array.from(o.getElementsByClassName("nav__item")).forEach(function (x) {
				addSpecificEventListener(x, "click", "itemEvent", itemEvent);
			});
		}

		// 遍历选项卡选项点击事件动画，不触发绑定的其他事件
		Array.from(o.getElementsByClassName("nav_horiz")).forEach(function (x) {
			const $underline = x.getElementsByClassName("nav__underline")[0];
			Array.from(x.getElementsByClassName("nav__item")).forEach(function (x) {
				if (x.classList.contains("item_selected")) {
					/* const $itemLeft = x.offsetLeft + 10; */
				    const $itemLeft = x.offsetLeft;
					/* const $itemWidth = x.offsetWidth; */
					const $itemWidth = x.getBoundingClientRect().width;
					if($underline != undefined){
					  $underline.style.cssText = "width: " + $itemWidth + "px; left: " + $itemLeft + "px";
					}
				}
			});
			navClickAnimationEvent(x);
		});

		// 流列表点击展开和缩回事件
		Array.from(o.getElementsByClassName("js_togglelist")).forEach(function (x) {
			const $matchSection = x.closest(".nav-cascade__item");
			const $matchList = $matchSection.nextElementSibling;
			$matchList.style.height = $matchList.getElementsByClassName("nav-cascade__item").length * 40 + 'px';
			const toggleEvent = function () {
				if (this.classList.contains("list_toggle-off")) {
					this.classList.remove("list_toggle-off");
				} else {
					this.classList.add("list_toggle-off");
				}
			};
			addSpecificEventListener($matchSection, "click", "toggleEvent", toggleEvent);
		});

		// 多行显示事件
		Array.from(document.getElementsByClassName("multipleline-ellipsis")).forEach(function (x) {
			const $maxHeight = parseInt(window.getComputedStyle(x).getPropertyValue("max-height"));
			const $realHeight = x.getElementsByClassName("multipleline-ellipsis__content-box")[0].clientHeight;
			
			x.style.height= Math.min($realHeight, $maxHeight) + "px";
			const displayAllContent = function () {
				const $realHeight = x.getElementsByClassName("multipleline-ellipsis__content-box")[0].clientHeight;
				if (window.getSelection().toString() === "") {
					x.style.maxHeight = "none";
					x.style.height = (parseInt(x.style.height) < $realHeight) ? $realHeight + "px" : Math.min($realHeight, $maxHeight) + "px";
				}
			};
			addSpecificEventListener(x, "click", "displayAllContent", displayAllContent);
		});

		// 按钮波纹效果事件
		Array.from(document.getElementsByClassName("ripple-effect")).forEach(function (x) {
			const $itemWidth = x.clientWidth;
			const $itemHeight = x.clientHeight;
			const $itemDiagonal = Math.ceil(Math.sqrt(Math.pow($itemWidth, 2) + Math.pow($itemHeight, 2)));
			const $circleDiameter = Math.ceil($itemDiagonal * 2);
			if (!x.getElementsByClassName("ripple-effect__circle")[0]) {
				const $circle = document.createElement("div");
				$circle.className = "ripple-effect__circle";
				x.appendChild($circle);
			}
			const $circleDiv = x.getElementsByClassName("ripple-effect__circle")[0];
			$circleDiv.style.cssText = 'width: ' + $circleDiameter + 'px; height: ' + $circleDiameter + 'px;';

			const mousedownEvent = function (e) {
				const $relativeX = e.clientX - this.getBoundingClientRect().left;
				const $relativeY = e.clientY - this.getBoundingClientRect().top;
				$circleDiv.style.top = -(Math.ceil($circleDiameter / 2) - $relativeY) + 'px';
				$circleDiv.style.left = -(Math.ceil($circleDiameter / 2) - $relativeX) + 'px';
				if (x.getAttribute("ripple-animating") !== "initial") {
					x.setAttribute("ripple-animating", "initial");
					setTimeout(function () {
						x.setAttribute("ripple-animating", "intermediate");
					}, 196);
					/*
           * this.classList.remove("circle-release-state");
           * this.classList.add("circle-active-state");
           */
				}
			};
			addSpecificEventListener(x, "mousedown", "mousedownEvent", mousedownEvent);
		});

		const rippleMouseupEvent = function () {
			Array.from(document.getElementsByClassName("circle-active-state")).forEach(function (x) {
				if (x.getAttribute("ripple-animating") === "initial") {
					const $observer = new MutationObserver(function (mutations) {
						mutations.forEach(function (x) {
							// console.log($(x).offset().right);
							// console.log($(x).parent().width());
							$observer.disconnect();
							x.target.classList.remove("circle-active-state");
							x.target.classList.add("circle-release-state");
							setTimeout(function () {
								x.target.classList.remove("circle-release-state");
							}, 360);
						});
					});
					const $config = {
						attributes: true,
						attributeFilter: ["ripple-animating"]
					};
					$observer.observe(x, $config);
				} else {
					x.classList.remove("circle-active-state");
					x.classList.add("circle-release-state");
					setTimeout(function () {
						x.classList.remove("circle-release-state");
					}, 196);
				}
			});
		};
		addSpecificEventListener(document, "mouseup", "rippleMouseupEvent", rippleMouseupEvent);
	};

	/**
   * 显示下拉数据框
   * 
   * @param {HTMLElemnt}
   *          el 需显示下拉数据框的元素
   */
	function selectorDisplayEvent(o) {
		const $dataBox = document.querySelector('*[selector-data="' + o.getAttribute("selector-id") + '"]');
		const $selValue = o.getElementsByClassName("sel__value_selected")[0];

		// 如果在加载的时候已经有选择，列表数据回显
		function selectorDataShowBack() {
			// 页面初始返回时如果已经存在选择值的话才回显
			if (o.getAttribute("sel-value")) {
				const $value = o.getAttribute("sel-value"); // 已选择的下拉框的已选择值
				const $dataBox = document.querySelector('*[selector-data="' + o.getAttribute("selector-id") + '"]'); // 匹配的下拉列表
				Array.from($dataBox.querySelectorAll(".sel-dropdown__item")).forEach(function (x) {
					// 找到与所选相匹配的值，添加样式和设置属性
					if (x.getAttribute("sel-itemvalue") === $value) {
						x.classList.add("item_selected");
						x.setAttribute("selected", "selected");
					}
				});
				$selValue.classList.remove("sel__value_placeholder"); // 移除类placeholder样式
			}
		}
		selectorDataShowBack();

		// 绑定列表数据的点击事件
		function selectorChooseEvent(myfunction) {
			const itemClickEvent = function () {
				// 重置其他兄弟元素的样式和属性，给点击的元素加样式和属性
				Array.from(this.parentElement.children).forEach(function (x) {
					x.classList.remove("item_selected");
					x.setAttribute("selected", "");
				});
				this.classList.add("item_selected");
				this.setAttribute("selected", "selected");
				// 点击后下拉框数据发生变化
				o.setAttribute("sel-value", this.getAttribute("sel-itemvalue"));
				o.setAttribute("sel-nextlevel", this.getAttribute("sel-nextlevel"));
				if($selValue.tagName == "INPUT"){
					$selValue.value = this.textContent; 
				}else{
					$selValue.textContent = this.textContent;
					$selValue.classList.remove("sel__value_placeholder");					
				}
				selectorEnableScroll($dataBox);
				if (typeof myfunction == "function") {
					myfunction();
				}
			};
			Array.from($dataBox.getElementsByClassName("sel-dropdown__item")).forEach(function (el) {
				addSpecificEventListener(el, "click", "itemClickEvent", itemClickEvent);
			});
		}

		// 数据框定位
		function displayListData() {
			// 创建遮蔽层，禁止滚动事件
			createTransparentCover($dataBox);
			selectorDisableScroll($dataBox);
			// 定位列表内部滚动位置
			$dataBox.style.display = "block";
			const $selPos = o.getBoundingClientRect(); // 下拉框的位置属性
			const $dataPos = $dataBox.getBoundingClientRect(); // 列表框的位置属性
			const $selected = $dataBox.querySelector('*[selected="selected"]') ? $dataBox.querySelector('*[selected="selected"]') : ($dataBox.getElementsByClassName("sel-dropdown__list")[0].firstElementChild || $dataBox.firstElementChild); // 已选择的数据或者列表第一个
			$dataBox.scrollTop = $selected.offsetTop + $selected.clientHeight / 2 - $dataPos.height / 2; // 将选择的元素居于列表框的中间位置
			// 设置列表位置
			const $dataTop = Math.max(Math.min($selPos.height / 2 + $selPos.top - $selected.clientHeight / 2 - ($selected.offsetTop - $dataBox.scrollTop), window.innerHeight - 8 - $dataPos.height), 8); // 选择元素与下拉框对齐所需要的列表框顶部位置,
                                                                                                                                                                                                            // 最小距离为8，且底部距离不能小于8
			$dataBox.style.cssText = "display: block; top: " + $dataTop + "px; left: " + ($selPos.left - 16) + "px; min-width: " + (o.clientWidth + 32) + "px";
		}

		// 下拉框的点击事件
		const selectorEvent = function (e) {
			if ($dataBox.getAttribute("data-src") === "request") {
				const $requestUrl = $dataBox.getAttribute("request-url"); // 请求的地址
				const $requestData = eval($dataBox.getAttribute("request-data")); // 请求所需的参数
				// 重新创建数据框子元素
				while ($dataBox.firstChild) {
					$dataBox.removeChild($dataBox.firstChild);
				}
				createHTMLElement("div", $dataBox, "preloader", null, function (el) {
					el.style.cssText = "width: 100%; height: 144px";
				});
				createHTMLElement("div", $dataBox, "sel-dropdown__list");
				const $dataList = $dataBox.getElementsByClassName("sel-dropdown__list")[0]; // 数据列表框
				const $preloader = $dataBox.getElementsByClassName("preloader")[0]; // 加载圈外框
				createIndeterminateCirclePreloader($preloader); // 发送请求的时候加载提示
				displayListData(o);
				// 发送请求
				var xhr = new XMLHttpRequest();
				xhr.open("POST", $requestUrl);
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
							// 成功返回数据后移除加载圈
							while ($preloader.firstChild) {
								$preloader.removeChild($preloader.firstChild);
							}
							$preloader.style.cssText = "";

							if ("json" === $dataBox.getAttribute("data-type")) {
								const $returnData = Array.from(JSON.parse(xhr.responseText));
								if ($returnData.length > 0) {
									const itemClickEvent = function () {
										if ($dataBox.getAttribute("item-event") != null) {
											eval($dataBox.getAttribute("item-event"));
										}
									}
									$returnData.forEach(function (x) {

										createHTMLElement("div", $dataList, "sel-dropdown__item", x.name, function (el) {
											el.setAttribute("sel-itemvalue", x.code);
											el.setAttribute("sel-nextlevel", x.nextlevel);
											// addSpecificEventListener(el,
                                            // "click", "itemClickEvent",
                                            // itemClickEvent);
										})
									});
									selectorDataShowBack();
									selectorChooseEvent(itemClickEvent);
									displayListData();
								} else {
									selectorEnableScroll($dataBox);
								}
							} else {
								var $parser = new DOMParser();
								var $xhrDOM = $parser.parseFromString(xhr.responseText, "text/html");
								// 如果有数据，把数据放进列表框，且添加相应事件，否则隐藏数据框
								if ($xhrDOM.querySelector(".sel-dropdown__item")) {
									$dataList.innerHTML = xhr.responseText;
									selectorDataShowBack();
									selectorChooseEvent();
									displayListData();
								} else {
									selectorEnableScroll($dataBox);
								}
							}
						}
					}
				};
				xhr.send(convertToFormData($requestData));
			} else {
				selectorDataShowBack();
				selectorChooseEvent();
				displayListData();
			}
			e.stopPropagation();
		};
		addSpecificEventListener(o, "click", "selectorEvent", selectorEvent);
	}

	const $acXHRArray = []; // 自动填词异步请求数组
	/**
   * 显示自动填词数据
   * 
   * @param {HTMLElemnt}
   *          o 所需显示自动填词的定位框
   */
	function autocompleteDisplayEvent(o) {
		if (!document.getElementsByClassName("ac__box")[0]) {
			createHTMLElement("div", document.body, "ac__box", null, function (el) {
				createHTMLElement("div", el, "preloader_ind-linear", el, function (el) {
					el.style.width = "100%";
				});
				createHTMLElement("div", el, "ac__list");
			}); // 创建自动填词容器
		}

		const $acBox = document.getElementsByClassName("ac__box")[0]; // 定义自动填词列表容
		const $acList = $acBox.getElementsByClassName("ac__list")[0]; // 定义自动填词列表
		const $preloader = $acBox.getElementsByClassName("preloader_ind-linear")[0]; // 自动填词加载条
		const $manualInput = o.getAttribute("manual-input") || "yes";
		const $input = o.parentElement.querySelector("input") || o.parentElement.querySelector('div[contenteditable="true"]'); // 自动填词的输入框
		var $extraData = o.getAttribute("request-data") ? eval(o.getAttribute("request-data")) : {}; // 发送自动填词请求的额外条件

		function acInputFocusEvent() {
			$extraData = o.getAttribute("request-data") ? eval(o.getAttribute("request-data")) : {}; // 发送自动填词请求的额外条件
		}
		// 输入框失去焦点事件
		function inputBlurEvent() {
			$input.blur();
			if($input.classList.contains("forminput-word")){
			   var numtext = $input.getAttribute("code");
			   if(numtext==""){
				   $input.value="";
			   }
			}
			setTimeout(function () {
				selectorEnableScroll($acBox);
			}, 0);
		}

		// 请求数据
		function acSendRequestKey() {
			// 移除列表中数据
			while ($acList.firstChild) {
				$acList.removeChild($acList.firstChild);
			}
			const $inputContent = ($input.tagName === "INPUT") ? $input.value.trim() : $input.textContent.trim(); // 获取输入框字段
			const $maxRecord = parseInt(o.getAttribute("max-record")) || 5; // 定义列表最多可显示个数
			const $noneRequest = eval(o.getAttribute("none-request")) || false; // 为空时候是否请求
			const $data = Object.assign($extraData, {
				searchKey: $inputContent
			}); // 拼接成请求条件
			// 在连续输入的时候，中止未完成的请求
			$acXHRArray.forEach(function (x) {
				x.abort();
			});
			// 如果存在输入怎发送请求，否则关闭浮层激活面板(或者设置了空的也请求)
			if ($inputContent || $noneRequest) {
				// 不允许手动输入的值时，如果有选择的词条，点击背景隐藏的时候会把选择的词条填充
				const coverClickEvent = function () {
					const $selectedItem = $acBox.getElementsByClassName("item_selected")[0];
					if ($manualInput === "no") {
						if ($selectedItem) {
							if ($input.tagName === "INPUT") {
								$input.value = this.textContent;
							}
							if ($input.tagName === "DIV") {
								$input.innerHTML = this.textContent;
							}
							$input.setAttribute("code", this.getAttribute("code"));
							if ($input.closest(".input__box")) {
								$input.closest(".input__box").classList.remove("input_invalid");
							}
						} else {
							// $input.setAttribute("code", "");
							if ($input.closest(".input__box")) {
								$input.closest(".input__box").classList.add("input_invalid");
							}
						}
					}
					inputBlurEvent();
				};
				createTransparentCover($acBox, coverClickEvent);
				selectorDisableScroll($acBox);
				const $acPos = o.getBoundingClientRect(); // 自动填词定位的位置属性
				const $acBoxHeight = parseInt(window.getComputedStyle($acBox).getPropertyValue("max-height")); // 自动填词容器的最大高度
				// 默认定位在下方，否则定位在上方
				if ((($acPos.top + $acPos.height + $acBoxHeight + 8) <= window.innerHeight) || is_mobile()) {
					$acBox.style.cssText = "display: block; width: " + $acPos.width + "px; left: " + $acPos.left + "px; top: " + ($acPos.top + $acPos.height) + "px; bottom: auto";
				} else {
					$acBox.style.cssText = "display: block; width: " + $acPos.width + "px; left: " + $acPos.left + "px; bottom: " + (window.innerHeight - $acPos.top) + "px; top: auto";
				}
				// 发送请求
				var xhr = new XMLHttpRequest();
				xhr.open("POST", o.getAttribute("request-url"));
				xhr.setRequestHeader("x-requested-with", "XMLHttpRequest");
				xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
				xhr.onreadystatechange = function () {
					if (xhr.readyState === 4) {
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
							while ($preloader.firstChild) {
								$preloader.removeChild($preloader.firstChild);
							}
							if (xhr.responseText && xhr.responseText!="null")  {
								const $returnData = Array.from(JSON.parse(xhr.responseText));
								if ($returnData.length > 0) {
									const $iterateNo = Math.min($returnData.length, $maxRecord); // 定义最大显示数目
									// 遍历返回的数据，添加到自动填词列表中，并绑定点击事件
									$returnData.forEach(function (x, idx) {
										if (idx < $iterateNo) {
										  // 区别期刊名称提示和标题提示
                                            if(x.from != undefined &&x.from == "journal" ){
                                                createHTMLElement("li", $acList, "ac__item ac__item-titledetail", "<div class='ac__item-pubtitle'>"+x.name+"</div><div class='ac__item-pubcontent'>"+x.issn+"</div>", function (el) {
                                                    el.setAttribute("code", x.code);
                                                    el.setAttribute("from", "journal");
                                                    el.setAttribute("name", x.name);
                                                });
                                            } else if(!x.pubTitle){
										     createHTMLElement("li", $acList, "ac__item", x.name, function (el) {
	                                           el.setAttribute("code", x.code);});
										    }else{
										        createHTMLElement("li", $acList, "ac__item ac__item-titledetail", "<div class='ac__item-pubtitle'>"+x.pubTitle+"</div><div class='ac__item-pubcontent'>"+x.name+"</div>", function (el) {
                                                    el.setAttribute("code", x.code);
                                                    // 如果有基准库id的话那么就添加一个标题 补全其他时间
                                                    if(x.des3pdwhPubId){
                                                      el.setAttribute("pdwhpubid",x.des3pdwhPubId);
                                                      el.setAttribute("onmousedown","PubEdit.autoFillPubInfo('"+x.des3pdwhPubId+"')");
                                                    }
                                                  });
										  }
										  var otherData = "";
											if (x.other) {// 可以放其他数据
											    otherData = x.other;
											}
											createHTMLElement("input", $acList, "other_input", otherData, function (other) {
											    other.type = 'hidden';
											    other.setAttribute("code", x.code);
											    other.setAttribute("value", x.other);
											});
										}
									});
									// 定义点击事件
									const acItemClickEvent = function () {
										if(this.getAttribute("code") === "AddNewJournal"){
											// 添加期刊事件
											PubEdit.showAddNewJournal();
											setTimeout(function(){
												selectorEnableScroll($acBox);
											},0);
											return ;
										}
										
										if ($input.tagName === "INPUT") {
										  var pattern=/\w+Title/i;
										  if(!pattern.test(this.getAttribute("code"))){
										      if(this.getAttribute("from") != undefined && this.getAttribute("from") == "journal" ){
										          // 添加期刊的框，特殊
                                                  $input.value = this.getAttribute("name");
                                              }else{
                                                  $input.value = this.textContent;
                                              }
										  }
										}
										if ($input.tagName === "DIV") {
										    if($input.getAttribute("autotext")){
										        var setdiv = document.createElement("div"); 
										        setdiv.className = "chip__box";
										        setdiv.setAttribute("code", this.getAttribute("code"));
										        var content = '<div class="chip__avatar">'
										                     +'</div>'
										                     +'<div class="chip__text">'+ this.textContent +'</div>'
										                     +'<div class="chip__icon icon_delete">'
										                     +'<i class="material-icons">close</i>'
										                     +'</div>';
										        setdiv.innerHTML = content;
										        setdiv.querySelector(".chip__icon").onclick = function(){
										            this.closest(".chip-panel__box").removeChild(this.closest(".chip__box"));
										        }
										        $input.closest(".chip-panel__box").insertBefore(setdiv,$input);
										        $input.innerHTML = "";
										    }else{
										        $input.innerHTML = this.textContent;   
										    }
										}
										$input.setAttribute("code", this.getAttribute("code")); // 设置输入框的code
										// inputBlurEvent();
										// wsn_添加自定义点击选中处理事件
										if ($input.getAttribute("item-event") != null) {
											eval($input.getAttribute("item-event"));
										}
										// 有其他数据处理的函数 ltl
										var otherF = $input.getAttribute("other-event");
										if (otherF != null) {
											(new Function("obj","value","return "+otherF+"(obj,value);"))($input,$(this).next('input').val());
										}
										if($($input).attr("itemEvent")){
											 new Function("obj",'return '+$($input).attr("itemEvent")+'(obj)')($input);
										}
										setTimeout(function(){
											selectorEnableScroll($acBox);
										},0);
									};
									const acItemClickEvent_box = function () {
										var length = $($input).parent().find(".chip__box[code='"+this.getAttribute("code")+"']").length;
										if ($input.tagName === "DIV"&&length==0) {
											const str1 = "<div class='chip__box' code='";
											const str2 = "'>" +
													"<div class='chip__avatar'>" +
													"<img src='https://test.scholarmate.com/avatars/41/5f/61/1000000037165.jpg'>" +
													"</div>" +
													"<div class='chip__text'>";
											const str3 = "</div>" +
													"<div class='chip__icon icon_delete' onclick='SmateShare.cancelSelectFriend(this)'>" +
													"<i class='material-icons'>close</i>" +
													"</div>" +
													"</div>";
											const str = str1+this.getAttribute("code")+str2+this.textContent+str3;
											$($input).before(str);
											if($("#shareFriendResults").length>0){
												$("#shareFriendResults").append(str);
											}
										}
										$input.innerHTML = "";
										// wsn_添加自定义点击选中处理事件
										if ($input.getAttribute("item-event") != null) {
											eval($input.getAttribute("item-event"));
										}
										if($($input).attr("itemEvent")){
											 new Function('return '+$($input).attr("itemEvent"))();
										}
										setTimeout(function(){
											selectorEnableScroll($acBox);
										},0);
									};
									var auto_box = o.getAttribute("auto_box");
									Array.from($acList.getElementsByClassName("ac__item")).forEach(function (x) {
										if(auto_box!=null&&auto_box=="true"){
											addSpecificEventListener(x, "click", "acItemClickEvent_box", acItemClickEvent_box);
											
										}else{
											addSpecificEventListener(x, "click", "acItemClickEvent", acItemClickEvent);
										}
									});
									// 定义鼠标进入样式事件
									const acItemMouseEnterEvent = function () {
										Array.from($acList.getElementsByClassName("ac__item")).forEach(function (x) {
											x.classList.remove("item_hovered");
										});
										this.classList.add("item_hovered");
									};
									Array.from($acList.getElementsByClassName("ac__item")).forEach(function (x) {
										addSpecificEventListener(x, "mouseenter", "acItemMouseEnterEvent", acItemMouseEnterEvent);
									});
									// 如禁止使用手动输入的则默认标注第一个选项
									if ($manualInput === "no") {
										$acList.firstElementChild.classList.add("item_hovered");
									}
								}
							} else {
								selectorEnableScroll($acBox);
							}
						}
					}
				};
				xhr.send(convertToFormData($data));
				$acXHRArray.push(xhr);
			} else {
				selectorEnableScroll($acBox);
			}
		}

		const afterInputBlur = function () {
			const $acListItemHovered = $acList.querySelector(".item_hovered");
			if ($acListItemHovered) {
				var inputtext = "";
				if ($input.tagName === "INPUT") {
					inputtext = $input.value;
				}
				if ($input.tagName === "DIV") {
					inputtext = $input.innerHTML;
				}
				// 仅限用于文字输入的时候
				if ($acListItemHovered.textContent === inputtext) {
					$input.setAttribute("code", $acListItemHovered.getAttribute("code"));
				} else {
					$input.setAttribute("code", "");
				}
			} else {
				$input.setAttribute("code", "");
			}
			// 去掉加载进度条
			while ($preloader.firstChild) {
                $preloader.removeChild($preloader.firstChild);
            }
		};
		// 输入框获取焦点的时候，发送自动填词请求
		addSpecificEventListener($input, "focus", "acSendRequestKey", acSendRequestKey);
		addSpecificEventListener($input, "focus", "acInputFocusEvent", acInputFocusEvent);
		addSpecificEventListener($input, "keyup", "acInputFocusEvent", acInputFocusEvent);
		addSpecificEventListener($input, "blur", "afterInputBlur", afterInputBlur);


		// 输入中文时，避免按键触发事件，等合成了中文字之后才触发事件
		var $inputLock = false;
		const startEvent = function () {
			$inputLock = true;
		};
		const endEvent = function () {
			$inputLock = false;
			fixEdgeBrowser();// edge浏览器在触发此事件endEvent后不会自动触发keyup事件。
		};
		// 阻止keydown和keypress的默认事件，如添加<br>
		var keyValue,charValue;
		const keyDownPressEvent = function (e) {
			if("keydown" == e.type){
				keyValue = e.keyCode;
			}
			if("keypress" == e.type){
				charValue = e.keyCode;
				judgeChorme(keyValue,charValue,e);
			}
		};
		
		/**
     * 解决edge浏览器在触发compositionend事件后不会自动触发keyUp事件 导致在输入中文时，输入完成后无法自动发送请求的问题，SCM-23211
     * 修改为手动发送请求（由于在edge浏览器中keyUp事件和compositionend冲突了，无法在此手动触发keyup事件，所有只能直接发送请求）
     */
    function fixEdgeBrowser(){
       var isEdge= window.navigator.userAgent.indexOf("Edge") !== -1;
       if(isEdge){
         createIndeterminateLinearPreloader($preloader);
         acSendRequestKey();
       }
    }
		// 谷歌浏览器↑↓的e.keyCode影响了&(的输入 用keydown和keypress事件的e.keycode区分
		function judgeChorme(keyValue,charValue,e){
			var isChrome = window.navigator.userAgent.indexOf("Chrome") !== -1;
			if(isChrome){
				if(keyValue == 13||(keyValue != 55&& charValue == 38)||(keyValue != 57&& charValue == 40)){
					e.preventDefault();
				}
			}else if (e.keyCode === 13 || e.keyCode === 38 || e.keyCode === 40) {
				e.preventDefault();
			}
		};
		const $allItem = $acBox.getElementsByClassName("ac__item"); // 自动填词列表包含的所有条目
		flagnum  = 10;
		const keyUpEvent = function (e) {
			if (!$inputLock) {
				if ($allItem && (e.keyCode === 13 || e.keyCode === 38 || e.keyCode === 40)) {
					e.preventDefault();
					const $selectedItem = Array.from($allItem).filter(function (x) {
						return x.classList.contains("item_hovered");
					})[0];
					
					const acItemEnterEvent_box = function (el) {
					    if(typeof(el) != "undefined"){
					        var length = $($input).parent().find(".chip__box[code='"+el.getAttribute("code")+"']").length;
					        if ($input.tagName === "DIV"&&length==0) {
					            const str1 = "<div class='chip__box' code='";
					            const str2 = "'>" +
					            "<div class='chip__avatar'>" +
					            "<img src='https://test.scholarmate.com/avatars/41/5f/61/1000000037165.jpg'>" +
					            "</div>" +
					            "<div class='chip__text'>";
					            const str3 = "</div>" +
					            "<div class='chip__icon icon_delete' onclick='SmateShare.cancelSelectFriend(this)'>" +
					            "<i class='material-icons'>close</i>" +
					            "</div>" +
					            "</div>";
					            const str = str1+el.getAttribute("code")+str2+el.textContent+str3;
					            $($input).before(str);
					            if($("#shareFriendResults").length>0){
					                $("#shareFriendResults").append(str);
					            }
					        }
					        $input.innerHTML = "";
					        if($($input).attr("itemEvent")){
					            new Function('return '+$($input).attr("itemEvent"))();
					        }
					        setTimeout(function(){
					            selectorEnableScroll($acBox);
					        },0);
					    }else{
					      selectorEnableScroll($acBox);
					    }
		            };
					
					// 向上的事件，如果没有选中则选择最后一个， 否则选择上一个 向上
		            if(e.keyCode === 38){
		                if($selectedItem){
		                    if(($selectedItem.previousElementSibling)&&($selectedItem.previousElementSibling)){
		                        $selectedItem.classList.remove("item_hovered");
		                        $selectedItem.previousElementSibling.previousSibling.classList.add("item_hovered");
		                    }else{
		                        $acBox.getElementsByClassName("item_hovered")[0].classList.remove("item_hovered");
		                        /* $acList.lastChild.classList.add("item_hovered"); */
		                        $acBox.getElementsByClassName("ac__item")[$allItem.length-1].classList.add("item_hovered");
		                    }
		                }else{
		                    $acBox.getElementsByClassName("ac__item")[$allItem.length-1].classList.add("item_hovered");
		                } 
		            }
					/*
           * if (e.keyCode === 38) { if (!$selectedItem) { if($acList.firstChild != undefined){
           * $acList.lastChild.classList.add("item_hovered"); } } else {
           * $selectedItem.classList.remove("item_hovered"); if ($selectedItem.previousSibling) {
           * $selectedItem.previousSibling.classList.add("item_hovered"); } else {
           * $acList.lastChild.classList.add("item_hovered"); } } }
           */
					// 向下的事件，如果没有选中则选中第一个，否则选择下一个 向下
		            if(e.keyCode === 40){
		                if($selectedItem){
		                    if(($selectedItem.nextElementSibling)&&($selectedItem.nextElementSibling.nextElementSibling)){
		                        $selectedItem.classList.remove("item_hovered");
		                        $selectedItem.nextElementSibling.nextElementSibling.classList.add("item_hovered");
		                    }else{
		                        $acBox.getElementsByClassName("item_hovered")[0].classList.remove("item_hovered");
		                        $acBox.getElementsByClassName("ac__item")[0].classList.add("item_hovered");
		                    }
		                }else{
		                    $acBox.getElementsByClassName("ac__item")[0].classList.add("item_hovered");
		                }
		            }
		            /*
                 * if (e.keyCode === 40) { if (!$selectedItem) { if($acList.firstChild !=
                 * undefined){ $acList.firstChild.classList.add("item_hovered"); } } else {
                 * $selectedItem.classList.remove("item_hovered"); if ($selectedItem.nextSibling) {
                 * $selectedItem.nextSibling.classList.add("item_hovered"); } else {
                 * $acList.firstChild.classList.add("item_hovered"); } } }
                 */
					// 定义回车事件，会触发blur事件，如有选中元素则添加至input中
					if (e.keyCode === 13) {
						if ($selectedItem) {
							if($selectedItem.getAttribute("code") === "AddNewJournal"){
								// 添加期刊事件
								PubEdit.showAddNewJournal();
								setTimeout(function(){
									selectorEnableScroll($acBox);
								},0);
								return ;
							}
							var pattern=/\w+Title/i;
							// 标题回车事件
							if(pattern.test($selectedItem.getAttribute("code"))){
                // 自动补全成果事件
							  PubEdit.autoFillPubInfo($selectedItem.getAttribute("pdwhpubid")); 
                setTimeout(function(){
                  selectorEnableScroll($acBox);
                },0);
                return ;
              }
							if ($input.tagName === "INPUT") {
                                if($selectedItem.getAttribute("from") != undefined && $selectedItem.getAttribute("from") == "journal" ){
                                    // 添加期刊的框，特殊
                                    $input.value = $selectedItem.getAttribute("name");
                                }else{
                                    $input.value = $selectedItem.textContent;
                                }
							}
							if ($input.tagName === "DIV") {
								$input.innerHTML = $selectedItem.textContent;
							}
							if($input.getAttribute("data-code")){
								$input.setAttribute("data-code","");
							}
							$input.setAttribute("code", $selectedItem.getAttribute("code"));
							if ($input.closest(".input__box")) {
								$input.closest(".input__box").classList.remove("input_invalid");
							}
							// 有其他数据处理的函数 ltl
							var otherF = $input.getAttribute("other-event");
							if (otherF != null) {
								(new Function("obj","value","return "+otherF+"(obj,value);"))($input,$($selectedItem).next('input').val());
							}
						} else {
							if ($manualInput === "no") {
								// $input.setAttribute("code", "");
								if ($input.closest(".input__box")) {
									$input.closest(".input__box").classList.add("input_invalid");
								}
							} else {
								$input.setAttribute("code", "");
							}
						}
						// inputBlurEvent();
						// wsn_添加自定义回车选中处理事件
						if ($input.getAttribute("item-event") != null) {
							eval($input.getAttribute("item-event"));
						}else{
						    var auto_box = o.getAttribute("auto_box");
			                if(auto_box!=null&&auto_box=="true"){
			                    acItemEnterEvent_box($selectedItem);
			                }
						}
                        // 回车关闭$acBox 2019-01-18 ajb
                        setTimeout(function(){
                            selectorEnableScroll($acBox);
                        },0);
					}
				} else {
					// 不是左右键的时候发送请求
					if (e.keyCode !== 37 && e.keyCode !== 39) {
						createIndeterminateLinearPreloader($preloader);
						acSendRequestKey();
					}
				}
			}
		};
		addSpecificEventListener($input, "compositionstart", "startEvent", startEvent);
		addSpecificEventListener($input, "compositionend", "endEvent", endEvent);
		addSpecificEventListener($input, "keydown", "keyDownPressEvent", keyDownPressEvent);
		addSpecificEventListener($input, "keypress", "keyDownPressEvent", keyDownPressEvent);
		addSpecificEventListener($input, "keyup", "keyUpEvent", keyUpEvent);
	}

	/**
   * 创建透明遮蔽层
   * 
   * @param {HTMLElemnt}
   *          el 激活的自动填词或者下拉框
   * @param {Function}
   *          f 除关闭激活窗口额外的方法
   */
	function createTransparentCover(el, f) {
		if (!document.getElementsByClassName("background-cover cover_transparent")[0]) {
			createHTMLElement("div", document.body, "background-cover cover_transparent");
		}
		const $cover = document.getElementsByClassName("background-cover cover_transparent")[0];
		const coverEvent = function () {
			selectorEnableScroll(el);
			if (f) {
				f();
			}
		};
		addSpecificEventListener($cover, "click", "coverEvent", coverEvent);
	}

	/**
   * 自动填词框和下拉框激活，禁止非当前元素的滚动事件
   * 
   * @param {HTMLElemnt}
   *          el 激活的自动填词或者下拉框
   */
	function selectorDisableScroll(el) {
		document.body.classList.add("js_selectornoscroll");
		const selectorScrollHandler = function (e) {
			if (!document.body.classList.contains("js_selectornoscroll")) {
				return;
			} else {
				if (e.target.classList.contains("background-cover")) {
					e.preventDefault();
				} else {
					var p = e.target;
					while ((p !== el && (p.clientWidth >= p.offsetWidth - 8)) || p.clientWidth === 0) {
						p = p.parentNode;
					}
					var $wheelY = e.wheelDelta || -e.detail;
					p.scrollTop += ($wheelY < 0 ? 1 : -1) * 30;
					e.preventDefault();
				}
			}
		};
		addSpecificEventListener(document, "mousewheel", "selectorScrollHandler", selectorScrollHandler);
		addSpecificEventListener(document, "DOMMouseScroll", "selectorScrollHandler", selectorScrollHandler);
	}

	/**
   * 隐藏激活框，激活滚动
   * 
   * @param {HTMLElemnt}
   *          el 激活的自动填词或者下拉框
   */
	function selectorEnableScroll(el) {
		el.style.display = "none";
		document.body.classList.remove("js_selectornoscroll");
		const $cover = document.getElementsByClassName("background-cover cover_transparent")[0];
		if ($cover) {
			$cover.parentNode.removeChild($cover);
		}
		removeSpecificEventListener(document, "mousewheel", "selectorScrollHandler");
		removeSpecificEventListener(document, "mousewheel", "selectorScrollHandler");
	}

	/**
   * 创建日期选择器
   * 
   * @param {HTMLElemnt}
   *          el 所需构建日期选择器的输入框
   */
	function createDatepicker(el) {
		if (document.getElementsByClassName("datepicker__box")[0]) {
			document.getElementsByClassName("datepicker__box")[0].parentNode.removeChild(document.getElementsByClassName("datepicker__box")[0]);
		}
		// 构建日期选择器
		(function createDatePickerBox() {
			createHTMLElement("div", document.body, "datepicker__box", null, function (el) {
				createHTMLElement("div", el, "datepicker__header");
				createHTMLElement("div", el, "datepicker__content", null, function (el) {
					createHTMLElement("div", el, "datepicker__year_box", null, function (el) {
						createHTMLElement("div", el, "datepicker__year_content");
					});
					createHTMLElement("div", el, "datepicker__month_box", null, function (el) {
						createHTMLElement("div", el, "datepicker__month_header", null, function (el) {
							createHTMLElement("div", el, "datepicker__month_icon prev-year material-icons", "chevron_left");
							createHTMLElement("div", el, "datepicker__month_year", null, function (el) {
								createHTMLElement("a", el, "js_monthYear");
							});
							createHTMLElement("div", el, "datepicker__month_icon next-year material-icons", "chevron_right");
						});
						createHTMLElement("div", el, "datepicker__month_content");
					});
					createHTMLElement("div", el, "datepicker__date_box", null, function (el) {
						createHTMLElement("div", el, "datepicker__date_header", null, function (el) {
							createHTMLElement("div", el, "datepicker__date_icon prev-month material-icons", "chevron_left");
							createHTMLElement("div", el, "datepicker__date_year", null, function (el) {
								createHTMLElement("a", el, "js_dateYear");
							});
							createHTMLElement("div", el, "datepicker__date_month", null, function (el) {
								createHTMLElement("a", el, "js_dateMonth");
							});
							createHTMLElement("div", el, "datepicker__date_icon next-month material-icons", "chevron_right");
						});
						createHTMLElement("div", el, "datepicker__date_day", null, function (el) {
							const $dayArray = ["S", "M", "T", "W", "T", "F", "S"];
							$dayArray.forEach(function (x) {
								createHTMLElement("div", el, "datepicker__date_number", x);
							});
						});
						createHTMLElement("div", el, "datepicker__date_content");
					});
				});
				createHTMLElement("div", el, "datepicker__footer", null, function (el) {
					createHTMLElement("button", el, "button_main button_dense js_clear", multilingual.clear);
					createHTMLElement("button", el, "button_main button_dense js_cancel", multilingual.cancel);
					createHTMLElement("button", el, "button_main button_dense button_primary js_confirm", multilingual.confirm);
				});
			});
		})();

		const $box = document.getElementsByClassName("datepicker__box")[0]; // 整个选择器
		const $head = $box.getElementsByClassName("datepicker__header")[0]; // 选择器的头部，用来显示当前的选择数据
		const $content = $box.getElementsByClassName("datepicker__content")[0]; // 面板容器，包含年份面板容器，月份面板容器，和日期面板容器
		const $contentYear = $box.getElementsByClassName("datepicker__year_content")[0]; // 年份面板
		const $contentMonth = $box.getElementsByClassName("datepicker__month_content")[0]; // 月份面板
		const $contentDate = $box.getElementsByClassName("datepicker__date_content")[0]; // 日期面板
		const $monthYear = $box.getElementsByClassName("js_monthYear")[0]; // 月份面板显示的年，点击回到年份面板
		const $dateYear = $box.getElementsByClassName("js_dateYear")[0]; // 日期面板显示的年，点击回到年份面板
		const $dateMonth = $box.getElementsByClassName("js_dateMonth")[0]; // 日期面板显示的月，点击回到月份面板
		const $prevYear = $box.getElementsByClassName("prev-year")[0]; // 月份面板上一年
		const $nextYear = $box.getElementsByClassName("next-year")[0]; // 月份面板下一年
		const $prevMonth = $box.getElementsByClassName("prev-month")[0]; // 日期面板上一月
		const $nextMonth = $box.getElementsByClassName("next-month")[0]; // 日期面板下一月
		const $minYear = 1901; // 最小年份
		const $maxYear = 2040; // 最大年份
		const $format = el.getAttribute("date-format") || "yyyy-mm-dd"; // 选择格式："yyyy"||"yyyy-mm"||"yyyy-mm-dd"
		// 点击输入框的时候显示日期选择器
		$box.style.display = "block";
		$content.classList.add("show_year-panel");
		const $inputRect = el.getBoundingClientRect();
		const $pickerRect = $box.getBoundingClientRect();
		// 选择器默认顶端与输入框对齐，如超出视窗底部则选择底部与输入框顶部对齐
		if ($inputRect.height + $inputRect.top + $pickerRect.height + 8 < window.innerHeight) {
			$box.style.top = $inputRect.top + 'px';
		} else {
			$box.style.top = ($inputRect.top - $pickerRect.height) + 'px';
		}
		// 选择器左侧默认与输入框左侧对齐，单右侧至少与视窗留8px的距离
		if ($inputRect.left + $pickerRect.width + 8 < window.innerWidth) {
			$box.style.left = $inputRect.left - 9 + 'px';
		} else {
			$box.style.left = (window.innerWidth - $pickerRect.width - 8) + 'px';
		}
		// createTransparentCover($box);
		createTransparentCover($box,function(){
			// 需要触发blur 进行校验
			const $event = document.createEvent("HTMLEvents");
			$event.initEvent("blur", false, true);
			el.dispatchEvent($event);
		});
		selectorDisableScroll($box.getElementsByClassName("datepicker__year_box")[0]);

		// 检查是否选中的最小年份或者最大年份，满足则禁用向前或者向后按钮
		function checkDateLimit() {
			// 如果是最小年份，月份面板的前一年按钮不可用
			if (parseInt($monthYear.textContent) === $minYear) {
				$prevYear.classList.add("icon_disabled");
			} else {
				$prevYear.classList.remove("icon_disabled");
			}
			// 如果是最大年份，月份面板的前一年按钮不可用
			if (parseInt($monthYear.textContent) === $minYear) {
				$nextYear.classList.add("icon_disabled");
			} else {
				$nextYear.classList.remove("icon_disabled");
			}
			// 如果是最小年份的一月，日期面板的前一月按钮不可用
			if (parseInt($monthYear.textContent) === $minYear && $dateMonth.textContent === multilingual.January) {
				$prevMonth.classList.add("icon_disabled");
			} else {
				$prevMonth.classList.remove("icon_disabled");
			}
			// 如果是最大年份的十二月，日期面板的后一月按钮不可用
			if (parseInt($monthYear.textContent) === $maxYear && $dateMonth.textContent === multilingual.December) {
				$nextMonth.classList.add("icon_disabled");
			} else {
				$nextMonth.classList.remove("icon_disabled");
			}
		}

		// 设置头部的属性和文字，包括回显至月份面板和日期面板的数据
		function setHeaderAttribute() {
			// 获取是否有选中的年月日
			const $selectedYear = $box.getElementsByClassName("year_selected")[0];
			const $selectedMonth = $box.getElementsByClassName("month_selected")[0];
			const $selectedDate = $box.getElementsByClassName("date_selected")[0];
			// 拼接头部的字符串
			var $yearString = "",
				$monthString = "",
				$dateString = "";
			// SCM-16168 用于判断是否是重新生成的日期选择框
			var isHasYear = $head.getAttribute("date-year")?true:false;
			// 重置属性并重新赋值
			$head.setAttribute("date-year", "");
			$head.setAttribute("date-month", "");
			$head.setAttribute("date-date", "");
			if ($selectedYear) {
				$yearString = $selectedYear.getAttribute("year-value");
				$head.setAttribute("date-year", $selectedYear.getAttribute("year-value"));
			}
			if ($selectedMonth) {
				$monthString = '-' + $selectedMonth.getAttribute("month-value");
				$head.setAttribute("date-month", $selectedMonth.getAttribute("month-value"));
			}
			if ($selectedDate) {
				$dateString = '-' + $selectedDate.getAttribute("date-value");
				$head.setAttribute("date-date", $selectedDate.getAttribute("date-value"));
			}
			$head.textContent = $yearString + $monthString + $dateString;
			// 如果只需要选择月份，回显月份面板的年
			if ($format === "yyyy-mm") {
				$monthYear.textContent = $head.getAttribute("date-year");
			}
			// 如果需要选择日期，回显日期面板的年月
			if ($format === "yyyy-mm-dd") {
				$monthYear.textContent = $head.getAttribute("date-year");
				$dateYear.textContent = $head.getAttribute("date-year");
				$dateMonth.textContent = $head.getAttribute("date-month");
                if($head.textContent != ""){
                    $head.textContent = $head.textContent.replace(/\//g,"-");
                }
			}
			// SCM-16168 从日期选择框的头部读取年月日数据填充到输入框，同时把属性值也传过去 begin
			/*el.value = $head.textContent;*/ //remove By GZL
			el.setAttribute("date-year", $head.getAttribute("date-year"));
			el.setAttribute("date-month", $head.getAttribute("date-month"));
			el.setAttribute("date-date", $head.getAttribute("date-date"));
			// 定义个日期回调函数
			if($(el).attr("itemEvent")){
				 new Function("obj",'return '+$(el).attr("itemEvent")+"(obj);")(el);
			}
			// 触发input的blur事件
			const $event = document.createEvent("HTMLEvents");
			$event.initEvent("blur", false, true);
			el.dispatchEvent($event);
			// 选择完成后自动关闭窗口
			if(isHasYear && $format === "yyyy-mm" && $selectedMonth){
				// 回复鼠标滚动事件
				selectorEnableScroll($box);
				// 触发input的blur事件
				const $event2 = document.createEvent("HTMLEvents");
                $event2.initEvent("blur", false, true);
				el.dispatchEvent($event2);
				$box.parentNode.removeChild($box);
			}
			// end
		}
		
		// 设置头部的属性和文字，包括回显至月份面板和日期面板的数据
		function setHeaderAttributeType(type) {
			const monthType = type;
			// 获取是否有选中的年月日
			const $selectedYear = $box.getElementsByClassName("year_selected")[0];
			const $selectedMonth = $box.getElementsByClassName("month_selected")[0];
			const $selectedDate = $box.getElementsByClassName("date_selected")[0];
			// 拼接头部的字符串
			var $yearString = "",
				$monthString = "",
				$dateString = "";
			// SCM-16168 用于判断是否是重新生成的日期选择框
			var isHasYear = $head.getAttribute("date-year")?true:false;
			// 重置属性并重新赋值
			$head.setAttribute("date-year", "");
			$head.setAttribute("date-month", "");
			$head.setAttribute("date-date", "");
			if ($selectedYear) {
				$yearString = $selectedYear.getAttribute("year-value");
				$head.setAttribute("date-year", $selectedYear.getAttribute("year-value"));
			}
			if ($selectedMonth) {
				$monthString = '-' + $selectedMonth.getAttribute("month-value");
				$head.setAttribute("date-month", $selectedMonth.getAttribute("month-value"));
			}
			if ($selectedDate) {
				$dateString = '-' + $selectedDate.getAttribute("date-value");
				$head.setAttribute("date-date", $selectedDate.getAttribute("date-value"));
			}
			$head.textContent = $yearString + $monthString + $dateString;
			// 如果只需要选择月份，回显月份面板的年
			if ($format === "yyyy-mm") {
				$monthYear.textContent = $head.getAttribute("date-year");
			}
			// 如果需要选择日期，回显日期面板的年月
			if ($format === "yyyy-mm-dd") {
				$monthYear.textContent = $head.getAttribute("date-year");
				$dateYear.textContent = $head.getAttribute("date-year");
				$dateMonth.textContent = $head.getAttribute("date-month");
			}
			// SCM-16168 从日期选择框的头部读取年月日数据填充到输入框，同时把属性值也传过去 begin
			el.value = $head.textContent;
			el.setAttribute("date-year", $head.getAttribute("date-year"));
			el.setAttribute("date-month", $head.getAttribute("date-month"));
			el.setAttribute("date-date", $head.getAttribute("date-date"));
			// 触发input的blur事件
			const $event = document.createEvent("HTMLEvents");
			$event.initEvent("blur", false, true);
			el.dispatchEvent($event);
			// 选择完成后自动关闭窗口
			if(isHasYear && $format === "yyyy-mm" && $selectedMonth && monthType !== "next" && monthType !== "previous"){
				// 回复鼠标滚动事件
				selectorEnableScroll($box);
				// 触发input的blur事件
				const $event = document.createEvent("HTMLEvents");
				$event.initEvent("blur", false, true);
				el.dispatchEvent($event);
				$box.parentNode.removeChild($box);
			}
			// end
		}

		// 创建年份面板
		(function createYearPanel() {
			var $yearArray = [];
			for (var i = $minYear; i < $maxYear + 1; i++) {
				$yearArray.push(i); // 输出年份数组
			}
			// 定义点击年份的事件
			const yearEvent = function () {
				// 给当前的添加选中样式并移除其他年份的选中样式
				Array.from($contentYear.children).forEach(function (x) {
					x.classList.remove("year_selected");
				});
				this.classList.add("year_selected");
				// 点击年份的时候会重置月份和日期的选择
				if ($format !== "yyyy") {
					Array.from($contentMonth.getElementsByClassName("month_selected")).forEach(function (x) {
						x.classList.remove("month_selected");
					});
					$content.className = "datepicker__content show_month-panel";
					if ($format !== "yyyy-mm") {
						Array.from($contentDate.getElementsByClassName("date_selected")).forEach(function (x) {
							x.classList.remove("date_selected");
						});
					}
				}
				setHeaderAttribute();
				checkDateLimit();
			};
			$yearArray.forEach(function (x) {
				createHTMLElement("div", $contentYear, "datepicker__year_number", x, function (el) {
					el.setAttribute("year-value", x); // 给每个年份设置属性值等于年份值
					addSpecificEventListener(el, "click", "yearEvent", yearEvent);
				});
			});
		})();

		// 构建月份面板
		(function createMonthPanel() {
			const $monthArray = [multilingual.January, multilingual.February, multilingual.March, multilingual.April, multilingual.May, multilingual.June, multilingual.July, multilingual.August, multilingual.September, multilingual.October, multilingual.November, multilingual.December]; // 定义月份数组
			// 点击月份事件
			const monthEvent = function () {
				Array.from($contentMonth.getElementsByClassName("month_selected")).forEach(function (x) {
					x.classList.remove("month_selected");
				});
				this.classList.add("month_selected");
				if ($format !== "yyyy-mm") {
					Array.from($contentDate.children).forEach(function (x) {
						x.classList.remove("date_selected");
					});
					$content.className = "datepicker__content show_date-panel";
					createDatePanel(parseInt($head.getAttribute("date-year")), parseInt(this.getAttribute("month-value")));
				}
				setHeaderAttribute();
				checkDateLimit();
			};
			$monthArray.forEach(function (x, i) {
				createHTMLElement("div", $contentMonth, "datepicker__month_number", x, function (el) {
					el.setAttribute("month-value", (i + 1 < 10) ? '0' + (i + 1).toString() : (i + 1).toString()); // 给每个月份设置属性值等于月份值（2位数字）
					addSpecificEventListener(el, "click", "monthEvent", monthEvent);
				});
			});
		})();

		// 根据选择的年份和月份创建日期面板
		function createDatePanel(year, month) {
			const $year = year || parseInt($head.getAttribute("date-year"));
			const $month = month || parseInt($head.getAttribute("date-month"));
			const $firstDay = new Date($year, $month - 1, 1).getDay(); // 获取该月1号是星期几
			const $totalNo = new Date($year, $month, 0).getDate(); // 获取该月总天数
			// 构建日期数组
			var $dateArray = [];
			for (var i = 0; i < $totalNo; i++) {
				$dateArray[i] = (i + 1 < 10) ? '0' + (i + 1).toString() : (i + 1).toString();
			}
			for (var j = 0; j < $firstDay; j++) {
				$dateArray.unshift(""); // 默认第一列是星期天，如1号不是星期天，前面用空元素填充
			}
			// 构建日期面板前先移除所有子元素
			while ($contentDate.firstChild) {
				$contentDate.removeChild($contentDate.firstChild);
			}
			// 点击日期事件
			const dateEvent = function () {
				Array.from($contentDate.children).forEach(function (x) {
					x.classList.remove("date_selected");
				});
				this.classList.add("date_selected");
				setHeaderAttribute();
			};
			$dateArray.forEach(function (x) {
				createHTMLElement("div", $contentDate, "datepicker__date_number", x, function (el) {
					if (x !== "") {
						el.setAttribute("date-value", x);
						addSpecificEventListener(el, "click", "dateEvent", dateEvent);
					}
				});
			});
		}

		// 添加翻页和返回等按钮事件
		(function datepickerAddBackEvents() {
			// 定义年份显示区域的事件，点击后会回退到年份面板，且清空月份和日期的选择
			const yearEvent = function () {
				$content.className = "datepicker__content show_year-panel"; // 显示年份面板
				// 移除所有选择的月份样式
				Array.from($contentMonth.getElementsByClassName("month_selected")).forEach(function (x) {
					x.classList.remove("month_selected");
				});
				// 移除格式允许选择日期，移除所有选择的日期样式
				if ($format === "yyyy-mm-dd") {
					Array.from($contentDate.getElementsByClassName("date_selected")).forEach(function (x) {
						x.classList.remove("date_selected");
					});
				}
				setHeaderAttribute();
				// 年份面板定位到年份选择的附近
				const $selectedYearTop = $contentYear.getElementsByClassName("year_selected")[0].offsetTop;
				const $contentHeight = $content.clientHeight;
				$contentYear.scrollTop = $selectedYearTop - $contentHeight / 2;
			};
			addSpecificEventListener($monthYear, "click", "yearEvent", yearEvent); // 绑定月份面板的年份显示的事件
			addSpecificEventListener($dateYear, "click", "yearEvent", yearEvent); // 绑定日期面板的年份显示的事件

			// 定义年份显示区域的事件，点击后会回退到月份面板，且清空日期的选择
			const monthEvent = function () {
				$content.className = "datepicker__content show_month-panel"; // 显示月份面板
				// 移除所有选择的日期样式
				Array.from($contentDate.getElementsByClassName("date_selected")).forEach(function (x) {
					x.classList.remove("date_selected");
				});
				setHeaderAttribute();
			};
			addSpecificEventListener($dateMonth, "click", "monthEvent", monthEvent); // 绑定日期面板的月份显示的事件

			// 定义月份面板的前一年事件
			const prevYearEvent = function () {
				const $selected = $contentYear.getElementsByClassName("year_selected")[0];
				$selected.classList.remove("year_selected");
				$selected.previousSibling.classList.add("year_selected");
				const previous = "previous";
				setHeaderAttributeType(previous);
				checkDateLimit();
			};
			addSpecificEventListener($prevYear, "click", "prevYearEvent", prevYearEvent); // 绑定前一年事件

			// 定义月份面板的后一年事件
			const nextYearEvent = function () {
				const $selected = $contentYear.getElementsByClassName("year_selected")[0];
				$selected.classList.remove("year_selected");
				$selected.nextSibling.classList.add("year_selected");
				const next = "next";
				setHeaderAttributeType(next);
				checkDateLimit();
			};
			addSpecificEventListener($nextYear, "click", "nextYearEvent", nextYearEvent); // 绑定后一年事件

			// 定义日期面板的前一月事件
			const prevMonthEvent = function () {
				const $selectedYear = $contentYear.getElementsByClassName("year_selected")[0];
				const $selectedMonth = $contentMonth.getElementsByClassName("month_selected")[0];
				$selectedMonth.classList.remove("month_selected");
				// 如果是在1月的时候往前一个月，会把年份提前一年且选中12月
				if ($selectedMonth.previousSibling) {
					$selectedMonth.previousSibling.classList.add("month_selected");
				} else {
					$selectedYear.classList.remove("year_selected");
					$selectedYear.previousSibling.classList.add("year_selected");
					$contentMonth.lastChild.classList.add("month_selected");
				}
				setHeaderAttribute();
				checkDateLimit();
				createDatePanel();
			};
			addSpecificEventListener($prevMonth, "click", "prevMonthEvent", prevMonthEvent); // 绑定前一月事件

			// 定义日期面板的后一月事件
			const nextMonthEvent = function () {
				const $selectedYear = $contentYear.getElementsByClassName("year_selected")[0];
				const $selectedMonth = $contentMonth.getElementsByClassName("month_selected")[0];
				// 如果是在12月的时候往后一个月，会把年份后推一年且选中1月
				$selectedMonth.classList.remove("month_selected");
				if ($selectedMonth.nextSibling) {
					$selectedMonth.nextSibling.classList.add("month_selected");
				} else {
					$selectedYear.classList.remove("year_selected");
					$selectedYear.nextSibling.classList.add("year_selected");
					$contentMonth.firstChild.classList.add("month_selected");
				}
				setHeaderAttribute();
				checkDateLimit();
				createDatePanel();
			};
			addSpecificEventListener($nextMonth, "click", "nextMonthEvent", nextMonthEvent); // 绑定后一月事件

			// 确认按钮事件
			const confirmEvent = function () {
				// 从日期选择框的头部读取年月日数据填充到输入框，同时把属性值也传过去
				el.value = $head.textContent;
				el.setAttribute("date-year", $head.getAttribute("date-year"));
				el.setAttribute("date-month", $head.getAttribute("date-month"));
				el.setAttribute("date-date", $head.getAttribute("date-date"));
				
				// 新增属性mustdate 必须精确值，yyyy:代表必须精确至年,mm:代表必须精确至月,dd:代表必须精确至日
				var mustdate = el.getAttribute("mustdate");
				if(mustdate != null && mustdate != ""){
				  if(mustdate == "yyyy" && $head.getAttribute("date-year") == ""){
				    scmpublictoast("日期必须精确至年份！", 1000);
				    return;
				  }
				  if(mustdate == "mm" && $head.getAttribute("date-month") == ""){
            scmpublictoast("日期必须精确至月份！", 1000);
            return;
          }
				  if(mustdate == "dd" && $head.getAttribute("date-date") == ""){
            scmpublictoast("日期必须精确至日！", 1000);
            return;
          }
				}
				
				// 回复鼠标滚动事件
				selectorEnableScroll($box);
                // 定义个日期确认时回调函数
                if($(el).attr("confirmEvent")){
                    new Function("obj",'return '+$(el).attr("confirmEvent")+"(obj);")(el);
                }
				// 触发input的blur事件
				const $event = document.createEvent("HTMLEvents");
				$event.initEvent("blur", false, true);
				el.dispatchEvent($event);
				$box.parentNode.removeChild($box);
			};
			addSpecificEventListener($box.getElementsByClassName("js_confirm")[0], "click", "confirmEvent", confirmEvent);


			// 取消按钮事件
			const cancelEvent = function () {
			    // 点击取消按钮，恢复上一次选中的值
		        var $yearString = "",
                $monthString = "",
                $dateString = "";
		        if ($inputYear) {
	                $yearString = $inputYear;
	                el.setAttribute("date-year", $inputYear);
	            }else{
	                el.setAttribute("date-year", "");
	            }
	            if ($inputMonth) {
	                $monthString = '-' + $inputMonth;
	                el.setAttribute("date-month", $inputMonth);
	            }else{
	                el.setAttribute("date-month", "");
	            }
	            if ($inputDate) {
	                $dateString = '-' + $inputDate;
	                el.setAttribute("date-date", $inputDate);
	            }else{
	                el.setAttribute("date-month", "");
	            }
	            el.value = $yearString + $monthString + $dateString;
				// 回复鼠标滚动事件
				selectorEnableScroll($box);
				// 触发input的blur事件
				const $event = document.createEvent("HTMLEvents");
				$event.initEvent("blur", false, true);
				el.dispatchEvent($event);
				$box.parentNode.removeChild($box);
			};
			addSpecificEventListener($box.getElementsByClassName("js_cancel")[0], "click", "cancelEvent", cancelEvent);

			// 清空按钮事件
			const clearEvent = function () {
				$content.className = "datepicker__content show_year-panel"; // 显示年份面板
				// 滚动到当前年份
				var $selectedYearDomsPosition = Array.from($contentYear.children).filter(function (x) {
					return x.classList.contains("year_selected");
				})[0];
				if(typeof($selectedYearDomsPosition) != "undefined" && $selectedYearDomsPosition != null){
				    const $selectedYearPosition = $selectedYearDomsPosition.offsetTop;
				    $box.getElementsByClassName("datepicker__year_box")[0].scrollTop = $selectedYearPosition - $content.clientHeight / 2;
				}
				// 清空选择年月日的样式，重置头部属性
				Array.from($contentDate.children).forEach(function (x) {
					x.classList.remove("date_selected");
				});
				Array.from($contentMonth.children).forEach(function (x) {
					x.classList.remove("month_selected");
				});
				Array.from($contentYear.children).forEach(function (x) {
					x.classList.remove("year_selected");
				});
				setHeaderAttribute();
			};
			addSpecificEventListener($box.getElementsByClassName("js_clear")[0], "click", "clearEvent", clearEvent);
		})();

		// 从输入框的值回显数据，先获取年月日
		const $inputYear = parseInt(el.value.split('-')[0]);
		const $inputMonth = parseInt(el.value.split('-')[1]);
		const $inputDate = parseInt(el.value.split('-')[2]);
		// 如果已读取数据可能没有设置属性值，重新赋值一次
		el.setAttribute("date-year", $inputYear);
		el.setAttribute("date-month", $inputMonth);
		el.setAttribute("date-date", $inputDate);
		// 如果不存在值，定位到当前年份
		if (!$inputYear && !$inputMonth && !$inputDate) {
			const $currentLocalYear = new Date().getFullYear(); // 获取当前年份
			const $targetPosition = Array.from($contentYear.children).filter(function (x) {
				return parseInt(x.getAttribute("year-value")) === $currentLocalYear;
			})[0].offsetTop; // 获取当前年份距离父元素的距离
			$box.getElementsByClassName("datepicker__year_box")[0].scrollTop = $targetPosition - $content.clientHeight / 2; // 滚动至定位当前年份至中间
		} else {
			// 显示年份面板
			if ($inputYear) {
				$content.className = "datepicker__content show_year-panel";
				const $selectedYear = Array.from($contentYear.children).filter(function (x) {
					return parseInt(x.getAttribute("year-value")) === $inputYear;
				})[0];
				$selectedYear.classList.add("year_selected");
				$box.getElementsByClassName("datepicker__year_box")[0].scrollTop = $selectedYear.offsetTop - $content.clientHeight / 2;
			}
			// 显示月份面板
			if ($inputMonth) {
				$content.className = "datepicker__content show_month-panel";
				Array.from($contentMonth.children).filter(function (x) {
					return parseInt(x.getAttribute("month-value")) === parseInt($inputMonth);
				})[0].classList.add("month_selected");
			}
			// 显示日期面板
			if ($inputDate) {
				createDatePanel($inputYear, $inputMonth);
				$content.className = "datepicker__content show_date-panel";
				Array.from($contentDate.children).filter(function (x) {
					return parseInt(x.getAttribute("date-value")) === parseInt($inputDate);
				})[0].classList.add("date_selected");
			}
			setHeaderAttribute(); // 头部赋值操作
		}
	}

	/**
   * 创建加载圈
   * 
   * @param {HTMLElemnt}
   *          o 需创建的加载圈的父元素，classList必须含有preloader
   * @param {Number}
   *          size 圈的大小，直径像素
   * @param {String}
   *          style 定义颜色，具体名称参考css表
   */
	window.createIndeterminateCirclePreloader = function (o, size, style) {
		try {
			if (!o.classList.contains("preloader")) {
				throw "Preloader must be inserted into a HTMLElement contains a class name 'preloader'";
			}
		} catch (e) {
			console.error(e);
		}

		// 判断传过来的参数数量，若没有传数字，第二个参数为style
		const $size = (typeof size === "number") ? size : 24;
		var $style;
		if (arguments.length < 3 && typeof size === "string") {
			$style = size;
		} else {
			$style = style || "";
		}
		// 定义加载圈样式
		if ($style !== "") {
			o.classList.add($style);
		}
		o.classList.add("active");
		// 移除加载圈容器的所有子元素
		while (o.firstChild) {
			o.removeChild(o.firstChild);
		}
		createHTMLElement("div", o, "preloader-ind-cir__box", null, function (el) {
			el.style.cssText = "width: " + $size + "px; height: " + $size + "px";
			createHTMLElement("div", el, "preloader-ind-cir__fill", null, function (el) {
				createHTMLElement("div", el, "preloader-ind-cir__arc-box left-half", null, function (el) {
					createHTMLElement("div", el, "preloader-ind-cir__arc");
				});
				createHTMLElement("div", el, "preloader-ind-cir__gap", null, function (el) {
					createHTMLElement("div", el, "preloader-ind-cir__arc");
				});
				createHTMLElement("div", el, "preloader-ind-cir__arc-box right-half", null, function (el) {
					createHTMLElement("div", el, "preloader-ind-cir__arc");
				});
			});
		});
	};

	/**
   * 创建加载直线条
   * 
   * @param {HTMLElemnt}
   *          o 需创建的加载圈的父元素，classList必须含有preloader
   */
	window.createIndeterminateLinearPreloader = function (o) {
		try {
			if (!o.classList.contains("preloader_ind-linear")) {
				throw "Preloader must be inserted into a HTMLElement contains a class name 'preloader_ind-linear'";
			}
		} catch (e) {
			console.error(e);
		}

		// 移除加载条的所有子元素
		while (o.firstChild) {
			o.removeChild(o.firstChild);
		}

		createHTMLElement("div", o, "preloader-ind-linear__box", null, function (el) {
			createHTMLElement("div", el, "preloader-ind-linear__bar1");
			createHTMLElement("div", el, "preloader-ind-linear__bar2");
		});
	};
})(window, document);

// 判断是否为移动端
function is_mobile() {
  var regex_match = /(nokia|iphone|android|motorola|^mot-|softbank|foma|docomo|kddi|up.browser|up.link|htc|dopod|blazer|netfront|helio|hosin|huawei|novarra|CoolPad|webos|techfaith|palmsource|blackberry|alcatel|amoi|ktouch|nexian|samsung|^sam-|s[cg]h|^lge|ericsson|philips|sagem|wellcom|bunjalloo|maui|symbian|smartphone|midp|wap|phone|windows ce|iemobile|^spice|^bird|^zte-|longcos|pantech|gionee|^sie-|portalmmm|jigs browser|hiptop|^benq|haier|^lct|operas*mobi|opera*mini|320x320|240x320|176x220)/i;
  var u = navigator.userAgent;
  if (null == u) {
    return true;
  }
  var result = regex_match.exec(u);
  if (null == result) {
    return false
  } else {
    return true
  }
}
