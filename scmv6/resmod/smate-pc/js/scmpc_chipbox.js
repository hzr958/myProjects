/**
 * 关键词模块 输出的方法
 * 
 * @method updateTotalCount 更新关键词统计个数
 * @method chipBoxInitialize 初始化关键词框
 * @method resetChipBoxData 重置关键词框 ChipBox对象所需的参数
 * @param {String}
 *          name 唯一标识符，用于指定关键词框
 * @param {Number}
 *          [maxItem] 能输入的最多词条数量，不填默认为无限制
 * @param {Object}
 *          [stats] 是否需要统计数显示配置 display {Boolean} 是否显示
 * @param {Number}
 *          checknum 判断执行的哪一个构造词条
 * @param {Object}
 *          [callbacks] 回调方法 compose {Function} 生成字条时的回调 remove {Function} 删除词条时的回调
 * @OriginalAuthor shenxingjia
 * @LatestVersion 2017 Jul 05 (shenxingjia)
 */
(function (window, document) {
	function ChipBox(options) {
		const $self = this;
		$self.name = options.name;
		$self.checknum = options.checknum;
		$self.editable = options.editable || false;
		$self.maxItem = options.maxItem;
		$self.stats = options.stats || {};
		if ($self.stats.display === undefined) {
			$self.stats.display = true;
		}
		$self.callbacks = options.callbacks || {};
		if ($self.callbacks.compose === undefined) {
			$self.callbacks.compose = function () {};
		}
		if ($self.callbacks.remove === undefined) {
			$self.callbacks.remove = function () {};
		}
		try {
			if (typeof $self.callbacks.compose !== "function") {
				throw "The property values of Object callbacks must be Function";
			}
			if (typeof $self.callbacks.remove !== "function") {
				throw "The property values of Object callbacks must be Function";
			}
		} catch (e) {
			console.error(e);
		}
		// 定义DOM变量
		$self.mainbox = Array.from(document.getElementsByClassName("chip-panel__box")).filter(function (x) {
			return x.getAttribute("chipbox-id") === $self.name;
		})[0]; // 静态HTMLElement
		$self.inputfield = $self.mainbox.getElementsByClassName("chip-panel__manual-input")[0]; // 静态HTMLElement
		$self.statsfield = $self.mainbox.getElementsByClassName("chip-panel__stats"); // 动态NodeList
		$self.allchips = $self.mainbox.getElementsByClassName("chip__box"); // 动态NodeList
		$self.selected = $self.mainbox.getElementsByClassName("chip_selected"); // 动态NodeList
		this.chipBoxInitialize();
		this.bindChipInputEvents();
	}
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
		o.addEventListener(evt, f);
		$EventArray.push($object); // 每添加一个监听事件就在数组中添加这个事件的一些属性，方便之后移除
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
		$childElement.textContent = textContent;
		if (f !== undefined) {
			f($childElement);
		}
		parentElement.appendChild($childElement);
	}
	/**
   * 绑定单个词条的监听事件
   * 
   * @param {Constructor}
   *          constructor 构造对象
   * @param {Object}
   *          o 词条对象
   */
	function bindSingleChipEvents(constructor, o) {
		const deleteEvent = function (e) {
			e.preventDefault();
			e.stopPropagation();
			var $inputContent;
			if (constructor.inputfield.tagName === "INPUT") {
				$inputContent = constructor.inputfield.value.trim();
			}
			if (constructor.inputfield.tagName === "DIV") {
				$inputContent = constructor.inputfield.textContent.trim();
			}
			// 正在输入的时候删除一个词条，焦点定位回输入框
			if ($inputContent) {
				focusOnLastCharacter(constructor.inputfield);
			}
			o.parentElement.removeChild(o);
			constructor.callbacks.remove(o); // 先执行回调
			constructor.updateTotalCount(); // 每删除一个重新统计数量
			constructor.mainbox.classList.remove("count_exceed"); // 超过上限的时候删除取消样式
		};
		addSpecificEventListener(o.getElementsByClassName("chip__icon icon_delete")[0], "click", "deleteEvent", deleteEvent);
	}
	/**
   * 焦点定位到最后一个字符
   * 
   * @param {HTMLELement}
   *          o DOM对象
   */
	function focusOnLastCharacter(o) {
	  try {
	    const $range = document.createRange();
	    const $selection = window.getSelection();
	    $range.selectNodeContents(o);
	    $range.collapse(false);
	    $selection.removeAllRanges();
	    $selection.addRange($range);
	  } catch (err) {
	  }
	}
	/**
   * 更新个数统计
   */
	ChipBox.prototype.updateTotalCount = function () {
		if (this.statsfield[0] && this.maxItem) {
			this.statsfield[0].textContent = this.allchips.length + '/' + this.maxItem;
		}
	};
	/**
   * 初始化关键词框
   */
	ChipBox.prototype.chipBoxInitialize = function () {
		const $self = this;
		// 构建统计框
		if ($self.maxItem && !$self.statsfield[0]) {
			const $chipStats = document.createElement("div");
			// 当需要显示的时候才添加到页面上
			if ($self.stats.display) {
				$chipStats.classList.add("chip-panel__stats");
				$self.mainbox.appendChild($chipStats);
			}
		}
		$self.updateTotalCount();
		// 移除已被选中的样式
		Array.from($self.selected).forEach(function (x) {
			x.classList.remove("chip_selected");
		});
		// 给已经存在的词条绑定删除事件
		Array.from($self.allchips).forEach(function (x) {
			bindSingleChipEvents($self, x);
		});
	};
	/**
   * 重置关键词框
   */
	ChipBox.prototype.resetChipBoxData = function () {
		this.inputfield.removeAttribute("code"); // 当关键词输入框绑定自动填词时，会添加code属性，重置时需移除
		if (this.inputfield.tagName === "INPUT") {
			this.inputfield.value = "";
		}
		if (this.inputfield.tagName === "DIV") {
			this.inputfield.textContent = "";
		}
		// 删除所有已存在的词条
		Array.from(this.allchips).forEach(function (x) {
			x.parentNode.removeChild(x);
		});
		this.updateTotalCount();
	};
	/**
   * 绑定输入框事件
   */
	ChipBox.prototype.bindChipInputEvents = function () {
		const $self = this;
		// 点击关键词框时，会焦点到输入框最后
		const clickFocus = function (e) {
			if (e.target === $self.mainbox) {
				focusOnLastCharacter($self.inputfield);
			}
		};
		addSpecificEventListener($self.mainbox, "click", "clickFocus", clickFocus);
		/**
     * 检查输入框是否存在内容
     */
		function checkInputContent() {
			var $inputContent;
			if ($self.inputfield.tagName === "INPUT") {
				$inputContent = $self.inputfield.value.trim();
			}
			if ($self.inputfield.tagName === "DIV") {
				$inputContent = $self.inputfield.textContent.trim();
			}
			if ($inputContent) {
				return $inputContent;
			} else {
				return false;
			}
		}

		/**
     * 构建词条
     * 
     * @param {Boolean}
     *          bool true: 使用分号进行分割
     */
		
		function composeTip(){
			// 创建新的词条
			const $chipItem = document.createElement("div");
			$chipItem.classList.add("chip__box");
			var setinner = '<div class="chip_container-content">'
	               +'<div class="chip_container-content_author">DDDDD</div>'
	               +'<div class="chip_container-content_prj">lllll</div>'
	               +'</div>'
		           +'<div class="chip_container-style">XXXX</div>';
			createHTMLElement("div", $chipItem, "chip_container",setinner);
			$self.inputfield.parentElement.insertBefore($chipItem, $self.inputfield); // 把构建的词条插入在输入框的前方
		}
	
		
		function composeChip(bool) {

			function composeExtractedChip(str) {
				if (str.length > 200) {
					str = str.substring(0, 200);
				}
				if (str.length > 0) {
					const $chipItem = document.createElement("div");
					$chipItem.classList.add("chip__box");
					createHTMLElement("div", $chipItem, "chip__avatar");
					createHTMLElement("div", $chipItem, "chip__text", str);
					createHTMLElement("div", $chipItem, "chip__icon icon_delete", null, function (el) {
						createHTMLElement("i", el, "material-icons", "close");
					});
					$self.inputfield.parentElement.insertBefore($chipItem, $self.inputfield); // 把构建的词条插入在输入框的前方
					// 当绑定自动填词时会有code属性，给词条赋予code属性
					if ($self.inputfield.getAttribute("code")) {
						$chipItem.setAttribute("code", $self.inputfield.getAttribute("code"));
					} else {
						$chipItem.setAttribute("code", "");
					}
					bindSingleChipEvents($self, $chipItem);
					// 先执行回调，再更新统计数
					$self.callbacks.compose();
					$self.updateTotalCount();
				}
			}

			const $maxChip = $self.maxItem || 65535;
			setTimeout(function () {
				var $inputContent = checkInputContent();
				if ($inputContent !== false) {
					if (bool === false) {
						composeExtractedChip($inputContent);
					} else {
						var $regExpTestArray = $inputContent.split(/[;\uFF1B]/);
						for(var i=0; i<$regExpTestArray.length; i++){
							if ($self.allchips.length < $maxChip) {
								composeExtractedChip($regExpTestArray[i].trim());
							} else {
								// 超过了最大条数加提醒样式，不添加内容
								$self.mainbox.classList.add("count_exceed");
							}
						}
						/*
             * $inputContent = $inputContent + ";"; const $semicolonRegExp =
             * /[^(;|\uFF1B)]*(;|\uFF1B)/g; var $regExpTestArray; while (($regExpTestArray =
             * $semicolonRegExp.exec($inputContent)) !== null) { if ($self.allchips.length <
             * $maxChip) { composeExtractedChip($regExpTestArray[0].substring(0,
             * $regExpTestArray[0].length - 1).trim()); } else { //超过了最大条数加提醒样式，不添加内容
             * $self.mainbox.classList.add("count_exceed"); break; } }
             */
					}
					// 清空输入框
					if ($self.inputfield.tagName === "INPUT") {
						$self.inputfield.value = "";
					}
					if ($self.inputfield.tagName === "DIV") {
						$self.inputfield.textContent = "";
					}
					$self.inputfield.setAttribute("code", "");
				}
				$inputLenth = checkInputContent() ? checkInputContent().length : 0;
			}, 0);
		}
		/**
     * 从自动填词选取添加事件
     */
		const addChipFromAC = function (e) {
			// 自动填词有返回值时会有一个透明遮蔽层，点击事件会焦点到输入框
			if (document.getElementsByClassName("background-cover cover_transparent")[0]) {
				// SCM-15880 首页完善个人经历，选择开始时间或结束时间时提示脚本错误，需要判断当前是否为关键词模块
				if(document.getElementsByClassName("background-cover cover_colored")[0] &&document.getElementsByClassName("background-cover cover_colored")[0].getAttribute("dialog-target") == "psnKeyWordsBox"){
					focusOnLastCharacter($self.inputfield);
				}
				// SCM-22765 论文推荐》关键词弹框》使用鼠标选中下拉匹配项的关键词，没有生成一个独立的关键词，详见描述。
				if(document.getElementsByClassName("background-cover cover_colored")[0] &&document.getElementsByClassName("background-cover cover_colored")[0].getAttribute("dialog-target") == "keyWordsBox"){
          focusOnLastCharacter($self.inputfield);
        }
				// 创建群组时，点击事件会焦点到输入框create_grp_ui
				if(document.getElementsByClassName("background-cover cover_colored")[0] &&document.getElementsByClassName("background-cover cover_colored")[0].getAttribute("dialog-target") == "create_grp_ui"){
					focusOnLastCharacter($self.inputfield);
				}
				// 不是首页时，点击事件会焦点到输入框SCM-17064
				if(!document.getElementsByClassName("background-cover cover_colored")[0]){
					focusOnLastCharacter($self.inputfield);
				}

			}
			// 如果鼠标的目标事件是自动填词的选项，那么会构建一个词条
			if (document.activeElement === $self.inputfield && e.target.classList.contains("ac__item")) {
				setTimeout(function () {
					if(	$self.checknum=== 1){
						composeTip();
					}else{
						composeChip();
					}
					
				}, 0);
			}
		};
		addSpecificEventListener(document, "click", "addChipFromAC", addChipFromAC);
		/**
     * 输入框失去焦点事件
     */
		const blurEvent = function () {
			// 失去焦点的时候所有词条取消选中状态
			Array.from($self.allchips).forEach(function (x) {
				x.classList.remove("chip_selected");
			});
			$self.inputfield.classList.remove("input_hidden"); // 取消输入框的隐藏状态
			$self.mainbox.classList.remove("count_exceed"); // 取消关键词框的其他样式
		};
		addSpecificEventListener($self.inputfield, "blur", "blurEvent", blurEvent);

		var $inputLenth; // 定义输入框词条长度值
		var $checkSelected; // 定义判断词条是否选中变量，返回boolean值
		/**
     * keydown和keypress事件，用来阻止默认事件和返回输入词条长度
     */
		const keydownEvent = function (e) {
			// 检测是否有被选中的词条，如果存在，输入框不触发任何事件
			$checkSelected = Array.from($self.allchips).some(function (x) {
				return x.classList.contains("chip_selected");
			});
			// 防止浏览器默认回车时间添加换行符
			if (e.keyCode === 13) {
				e.preventDefault();
			}
			if ($checkSelected === true) {
				e.preventDefault();
			}
		};
		addSpecificEventListener(this.inputfield, "keydown", "keydownEvent", keydownEvent);
		addSpecificEventListener(this.inputfield, "keypress", "keydownEvent", keydownEvent); // 兼容不同浏览器会在不同时间点触发事件

		/**
     * keyup事件，用来生成词条或者键盘选中词条
     */
		const keyupEvent = function (e) {
			const $allchips = Array.from($self.allchips);
			const $selected = $self.selected[0];
			var $selectedIndex;
			if ($checkSelected === true) {
				$selectedIndex = $allchips.indexOf($selected); // 获取选中元素的位置
			}
			const $inputContent = checkInputContent();
			if (e.keyCode === 13 || e.keyCode === 59 || e.keyCode === 186 || e.keyCode === 37 || e.keyCode === 39 || e.keyCode === 8 || e.keyCode === 46) {
				switch (e.keyCode) {
					// 回车事件
					case 13:
						e.preventDefault(); // 阻止默认添加换行符
						if (e.ctrlKey) {
							composeChip(false);
						} else {
							composeChip();
						}
						setTimeout(function () {
							$self.inputfield.focus(); // 重新焦点到输入框
						}, 0);
						break;
						// 分号时间，firefox为59，其余为186
					case 59:
					case 186:
						if (!e.shiftKey) {
							if (!e.ctrlKey) {
								if($self.checknum === 1){
									composeTip();
								}else{
									composeChip();
								}
								
								
								
								setTimeout(function () {
									$self.inputfield.focus(); // 重新焦点到输入框
								}, 0);
							} else {
								if ($self.inputfield.tagName === "INPUT") {
									$self.inputfield.value += ';';
								}
								if ($self.inputfield.tagName === "DIV") {
									$self.inputfield.textContent += ';';
								}
								focusOnLastCharacter($self.inputfield);
							}
						} else {
							if (e.ctrlKey) {
								if ($self.inputfield.tagName === "INPUT") {
									$self.inputfield.value += '\uFF1B';
								}
								if ($self.inputfield.tagName === "DIV") {
									$self.inputfield.textContent += '\uFF1B';
								}
								focusOnLastCharacter($self.inputfield);
							}
						}
						break;
						// 左方向事件，仅在无输入内容且已经存在词条时触发
					case 37:
						if ($inputContent === false && $allchips.length > 0) {
							e.preventDefault();
							// 如没有选中的词条，则选中最后一个词条
							if ($checkSelected === false) {
								$self.inputfield.classList.add("input_hidden"); // 隐藏输入框，但还是处于激活状态
								$allchips[$allchips.length - 1].classList.add("chip_selected");
							} else {
								// 如果不是第一个则选取前面一个
								if ($selectedIndex > 0) {
									$selected.classList.remove("chip_selected");
									$allchips[$selectedIndex - 1].classList.add("chip_selected");
								}
							}
						}
						break;
						// 右方向事件，一定在无输入内容且存在选中词条时触发
					case 39:
						if ($inputContent === false) {
							e.preventDefault();
							if ($checkSelected === true) {
								$selected.classList.remove("chip_selected");
								// 如果选中词条不是最后一个，则选取下一个，否则显示输入框
								if ($selectedIndex < $allchips.length - 1) {
									$allchips[$selectedIndex + 1].classList.add("chip_selected");
								} else {
									$self.inputfield.classList.remove("input_hidden");
								}
							}
						}
						break;
						// backspace事件，在无输入且存在词条情况下触发
					case 8:
						if ($inputContent === false && $allchips.length > 0) {
							// 360 IE
                            // 中文输入过程中，用backspace删除拼音时，会将词条选中删除，暂时放弃backspace删除词条功能
							// var active = document.activeElement.id;
							return;
							$self.mainbox.classList.remove("count_exceed"); // 移除超出限制样式
							e.preventDefault();
							// 如没有选中的则选中最后一个，否则删除选中的
							if ($checkSelected === false) {
								if ($inputLenth === 0) {
									$self.inputfield.classList.add("input_hidden");
									$allchips[$allchips.length - 1].classList.add("chip_selected");
								}
							} else {
								$selected.parentElement.removeChild($selected);
								$self.updateTotalCount();
								if ($selectedIndex < $allchips.length - 1) {
									$allchips[$selectedIndex + 1].classList.add("chip_selected");
								} else {
									$self.inputfield.classList.remove("input_hidden");
								}
							}
						}
						break;
						// delete事件，没有内容且有选中的时候触发删除
					case 46:
						if ($inputContent === false) {
							$self.mainbox.classList.remove("count_exceed"); // 当光标位于输入框起始部按delete删除，删除全部的时候会移除超出限制样式
							e.preventDefault();
							if ($checkSelected === true) {
								$selected.parentElement.removeChild($selected);
								$self.updateTotalCount();
								if ($selectedIndex < $allchips.length - 1) {
									$allchips[$selectedIndex + 1].classList.add("chip_selected");
								} else {
									$self.inputfield.classList.remove("input_hidden");
								}
							}
						}
						break;
				}
			} else {
				if (e.keyCode !== 16 && e.keyCode !== 17) {
					// 如果有选中样式时，不触发任何事件
					if ($checkSelected === true) {
						e.preventDefault();
					}
				}
			}
			$inputLenth = checkInputContent() ? checkInputContent().length : 0;
		};
		addSpecificEventListener($self.inputfield, "keyup", "keyupEvent", keyupEvent);
	};

	window.ChipBox = function (options) {
		return new ChipBox(options);
	};

})(window, document);
