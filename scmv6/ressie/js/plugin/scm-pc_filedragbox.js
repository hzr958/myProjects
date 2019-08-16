/**
 * 文件上传模块（手动上传版） 输出的方法
 * 
 * @method initialization 初始化上传框
 * @method uploadFile 上传文件
 * @method resetFileUploadBox 重置上传框 方法initialization(el, options)中options初始化所需参数
 * @param {String}
 *            fileurl complusory 上传的地址
 * @param {Object}
 *            filedata optional 上传添加的其他参数
 * @param {Function}
 *            filecallback optional 上传成功后的回调方法
 * @param {Object}
 *            filecallbackparam optional 上传成功后回调方法时传的额外参数
 * @param {Array}
 *            filetype optional 上传文件的类型，数组元素需要是String
 * @param {String}
 *            method optional 上传的方式，直接上传或二次确认 扩展-不需要上传状态
 *            "dropupload"(default)||"dropadd"||"nostatus"
 * @OriginalAuthor shenxingjia
 * @LatestVersion 2018 Aug 28 (叶星源)
 */
const sieFileUploadModule = (function () {
	const multilingual = {};
	const multilingualSettings = {
		"addFile": ["Click or drag to add file.", "点击或拖动添加文件"],
		"uploadFile": ["Click or drag to add file.", "点击或拖动文件上传"],
		"noFile": ["No file chosen.", "文件不存在"],
		"overSize": ["File size can not exceed 30MB.", "文件大小不能超过30MB"],
		"overSizeSmall": ["File size can not null.", "不能上传空文件！"],
		"wrongType": ["File type is incorrect.", "该文件类型不允许上传"]
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
	 * 移除一个监听事件
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
	// 上传文件集合,最多保存一个文件
	var $uploadFileList = [];
	/**
	 * 添加上传文件的到列表
	 */
	function addUploadFileToList(file) {
		// 先移除之前的文件;
		removeUploadFileFromList();
		$uploadFileList.push(file);
	}
	/**
	 * 移除所有的文件
	 */
	function removeUploadFileFromList() {
		$uploadFileList.forEach(function (o, idx) {
			$uploadFileList.splice(idx, 1);
		})
	}
	/**
	 * 构造一个HTMLElement元素的方法，仅能设置对象的className，仅支持appendChild添加方法
	 * 
	 * @param {String}
	 *            nodetype 所需创建对象的TagName
	 * @param {String}
	 *            classList 创建的对象className
	 * @param {HTMLElement}
	 *            parentElement 所需创建对象的父元素，创建的对象会append到父元素上
	 */
	function createHTMLElement(nodetype, classList, parentElement) {
		classList = classList || "";
		const $childElement = document.createElement(nodetype);
		$childElement.className = classList;
		parentElement.appendChild($childElement);
	}
	/**
	 * 遍历每个上传文件的框，构造其内部所有子元素，构造出的HTML详见文件尾部Appendix 1
	 * 
	 * @param {HTMLElement}
	 *            o 一个class中包含‘fileupload__box’的对象
	 */
	function createFileUploadBox(o, type) {
		createHTMLElement("div", "fileupload__core initial_shown", o);

		const $coreBox = o.getElementsByClassName("fileupload__core")[0];
		createHTMLElement("div", "fileupload__initial", $coreBox);
		createHTMLElement("div", "fileupload__progress", $coreBox);
		createHTMLElement("div", "fileupload__saving", $coreBox);
		createHTMLElement("div", "fileupload__finish", $coreBox);
		createHTMLElement("div", "fileupload__hint-text", $coreBox);

		const $initialBox = o.getElementsByClassName("fileupload__initial")[0];
		if("NotList" == type){
			createHTMLElement("div", "fileupload__upload-logo", $initialBox);
		}else{
			createHTMLElement("img", "request-add__tip1_avator", $initialBox);
			$initialBox.getElementsByClassName("request-add__tip1_avator")[0].src="/ressie/images/file_ upload1.png";
			createHTMLElement("img", "request-add__tip2_avator", $initialBox);
			$initialBox.getElementsByClassName("request-add__tip2_avator")[0].src="/ressie/images/file_ upload.png";
		}
		createHTMLElement("input", "fileupload__input", $initialBox);
		$initialBox.getElementsByClassName("fileupload__input")[0].type = "file";
		$initialBox.getElementsByClassName("fileupload__input")[0].style.display="none";

		const $progressBox = o.getElementsByClassName("fileupload__progress")[0];
		createHTMLElement("canvas", "", $progressBox);
		createHTMLElement("div", "fileupload__progress_text", $progressBox);
		$progressBox.getElementsByTagName("CANVAS")[0].width = 56;
		$progressBox.getElementsByTagName("CANVAS")[0].height = 56;

		const $savingBox = o.getElementsByClassName("fileupload__saving")[0];
		createHTMLElement("div", "preloader", $savingBox);
		createHTMLElement("div", "fileupload__saving-text", $savingBox);

		// 根据上传的方法（添加或直接上传）设置不同的提示文字
		const $hintBox = o.getElementsByClassName("fileupload__hint-text")[0];
		if (o.method === "dropupload") {
			$hintBox.textContent = multilingual.uploadFile;
		} else {
			if("NotList" == type){
				$hintBox.textContent = multilingual.addFile;
			}
		}
	}
	
	/**
	 * 初始化鼠标相关的监听事件
	 */
	const initActive = function($self){
		// 增加监听事件，当鼠标进入或者拖动文件进入框内的时候，文件框会变蓝
		const uploadLogoActive = function (e) {
			$self.classList.add("upload_ready");
			e.stopPropagation();
			e.preventDefault();
		};
		addSpecificEventListener($self, "dragenter", "uploadLogoActive", uploadLogoActive);
		addSpecificEventListener($self, "dragover", "uploadLogoActive", uploadLogoActive);
		addSpecificEventListener($self, "mouseenter", "uploadLogoActive", uploadLogoActive);

		// 增加监听事件，当鼠标离开或者拖动文件离开框的时候，文件框变回原来的样子
		const uploadLogoDeactive = function (e) {
			if (!$uploadFileList[0]) {
				$self.classList.remove("upload_ready");
			}
			e.stopPropagation();
			e.preventDefault();
		};
		addSpecificEventListener($self, "dragleave", "uploadLogoDeactive", uploadLogoDeactive);
		addSpecificEventListener($self, "mouseleave", "uploadLogoDeactive", uploadLogoDeactive);
		
		// 添加点击事件
		const fileuploadBoxOpenInputClick = function (ev) {
		    var $this = $(ev.currentTarget);
		    $this.find('input.fileupload__input').click();
		};
		addSpecificEventListener($self, "click", "fileuploadBoxOpenInputClick", fileuploadBoxOpenInputClick);
	}
	/**
	 * 初始化一个document中所有的文件上传框，绑定相关属性和事件
	 * 
	 * @param {Document}
	 *            el 包含需要初始化上传框的document或HTMLElement（继承document）
	 * @param {Object}
	 *            options 初始化的参数，所有@param el包含的上传框会被给予相同的参数
	 */
	const fileUploadMainModule = function (el, options) {
		// 参数校验，el必须存在且为DOMElement；options必须为对象形式
		try {
			if (typeof el === "undefined") {
				throw "The first arguement of function 'fileUploadInitialization' can not be omitted.";
			} else if (el.nodeType !== 1) {
				throw "The first arguement of function 'fileUploadInitialization' must be a DOMElement.";
			}
			if (typeof options !== "object") {
				throw "The second argument of function 'fileUploadInitialization' must be an object in order to define the extrinsic properties of upload event.";
			} else {
				if (!options.fileurl) {
					throw "An url must be defined for uploading.";
				}
			}
		} catch (e) {
			console.error(e);
		}

		// 遍历所有的文件上传框并初始化他们
		Array.from(el.getElementsByClassName("fileupload__box")).forEach(function (x) {
			const $self = x;
			$self.fileurl = options.fileurl;
			$self.filedata = options.filedata || {};
			$self.filecallback = options.filecallback || function () {};
			// 文件上传的类型
			$self.type = options.type||"none";
			// 回调函数额外参数
			$self.filecallbackparam  = options.filecallbackparam || {};
// $self.filetype = options.filetype ||
// [".jpg",".jpeg",".pdf",".doc",".docx",".png",".rar",".zip"];
			$self.filetype = options.filetype || [".pdf"];
			$self.method = options.method || "dropupload";

			// 如果上传框没有子元素，重新构造这个上传框
			if (!$self.firstChild) {
				createFileUploadBox($self,$self.type);
			}

			const $inputField = $self.querySelector('input[type="file"]'); // 文件上传INPUT
			const $textField = $self.getElementsByClassName("fileupload__hint-text")[0]; // 文字提示DIV
			
			// 添加对鼠标进入或者拖动文件进入框内的监听事件
			initActive($self);

			/**
			 * 文件校验
			 * 
			 * @param {FileObject}
			 *            file 所需校验的文件
			 * @return {Boolean} 是否同时符合大小和类型
			 */
			function fileValidation(file) {
				function fileUploadReset(str) {
					$inputField.value = null;
					removeUploadFileFromList();
// scmpublictoast(str, 1000);
					$("#validateTip").find("label").html(str);
					$("#validateTip").css("display","flex");
					const $hintBox_1 = $self.getElementsByClassName("fileupload__hint-text")[0];
					if($hintBox_1!=null){
						if ($self.method === "dropupload") {
							$hintBox_1.textContent = multilingual.uploadFile;
						} else {
							if("NotList" == $self.type){
								$hintBox_1.textContent = multilingual.addFile;
							}
						}
					}
					return false;
				}
				if (!file) {
					return fileUploadReset(multilingual.noFile);
				}
				// 文件大小不能超过30MB
				if (file.size >= 1024 * 1024 * 30) {
					return fileUploadReset(multilingual.overSize);
				}
				if(file.size<=0){
					return fileUploadReset(multilingual.overSizeSmall);
				}
				// 文件类型验证
				var $typeCheck = false;
				const $fileName = file.name;
				if (!$self.filetype) {
					$typeCheck = true; // 如不需要验证，则始终返回true
				} else {
					for (var i = 0; i < $self.filetype.length; i++) {
						if ($fileName.substr($fileName.lastIndexOf(".")).toLowerCase() === $self.filetype[i].toLowerCase()) {
							$typeCheck = true;
							break;
						}
					}
				}
				if (!$typeCheck) {
					return fileUploadReset(multilingual.wrongType);
				}
				return true;
			}


			// 添加拖动文件放下时候的监听事件
			const dropFileEvent = function (e) {
				e.preventDefault();
				e.stopPropagation();
				$("#validateTip").find("label").html("");
        $("#validateTip").css("display", "none");
				const $selectedFile = e.dataTransfer.files[0];
				const $validation = fileValidation($selectedFile);
				// 仅在还未进入上传状态时才能在拖动放下的时候添加文件
				if ($self.getElementsByClassName("fileupload__core")[0].classList.contains("initial_shown")) {
					// 文件校验成功后才会执行后续方法，如果method设定为“dropupload”，直接上传，否则仅添加文件至input
					if ($validation) {
						// $inputField.files[0] = $selectedFile;
						addUploadFileToList($selectedFile);
						if($self.method === "nostatus"){
							uploadFileToServer($self,options);
							return;
						}
						$textField.textContent = $selectedFile.name;
						$textField.classList.add("file_selected");
						if ($self.method === "dropupload") {
							uploadFileToServer($self,options);
						} else {
							$self.classList.add("upload_ready");
						}
					}
				}
			};
			addSpecificEventListener($self, "drop", "dropFileEvent", dropFileEvent);

			// 添加input值改变时候事件，会在添加进文件的时候触发，如果method设置为“dropupload”，也会直接在添加后上传
			const inputChangeEvent = function () {
				// 仅在值不为空的时候做校验
				if ($inputField.value === null || $inputField.value === "") {
					return;
				}
				$("#validateTip").find("label").html("");
		    $("#validateTip").css("display", "none");
				const $validation = fileValidation($inputField.files[0]);
				if ($validation) {
					addUploadFileToList($inputField.files[0]);
					if($self.method === "nostatus"){
						uploadFileToServer($self,options);
						return;
					}
					$textField.textContent = $inputField.files[0].name;
					$textField.classList.add("file_selected");
					if ($self.method === "dropupload") {
						uploadFileToServer($self,options);
					} else {
						$self.classList.add("upload_ready");
					}
				}
			};
			if($inputField != null){
				addSpecificEventListener($inputField, "change", "inputChangeEvent", inputChangeEvent);
			}
		});
	};

	/**
	 * 上传文件至伺服器
	 * 
	 * @param {HTMLElement}
	 *            o 所需上传文件的对象，其class中包含‘fileupload__box’
	 * @param {Object}
	 *            data 用于单独调用的时候赋予的额外参数，多用于非直接上传场景
	 */
	const uploadFileToServer = function (o, options) {
		try {
			if (typeof o === "undefined") {
				throw "A fileupload box must be assigned in uploadFileToServer method";
			}
		} catch (e) {
			console.error(e);
		}

		const $uploadBoxCore = o.getElementsByClassName("fileupload__core")[0]; // 文件上传状态显示DIV
		const $inputField = o.querySelector('input[type="file"]'); // 文件上传INPUT
		const $uploadFile = $uploadFileList[0]; // 文件上传的具体文件
		const $canvasField = o.getElementsByTagName("CANVAS")[0]; // 上传进度显示CANVAS

		// 构建一个FormData对象，把文件、初始化参数、额外参数添加到对象中
		const $formData = new FormData();
		$formData.append("filedata", $uploadFile);
		if (options.filedata) {
			for (var key1 in o.filedata) {
				if (o.filedata.hasOwnProperty(key1)) {
					$formData.append(key1, o.filedata[key1]);
				}
			}
		}
		// 构建一个新XMLHttpRequest对象上传文件
		const xhr = new XMLHttpRequest();
		xhr.open("post", options.fileurl,true);
		xhr.setRequestHeader("x-requested-with", "XMLHttpRequest");
		xhr.withCredentials = true;
		xhr.upload.onprogress = displayProgress;
		/**
		 * 上传进度显示
		 */
		function displayProgress(e) {
			if (e.lengthComputable) { // 如e.lengthComputable为false，则e.total会返回0
				const $progress = Math.round(e.loaded / e.total * 100);
				const $progressText = o.getElementsByClassName("fileupload__progress_text")[0];
				$progressText.textContent = $progress + '%';
				const ctx = $canvasField.getContext("2d");
				const $radius = ctx.canvas.width / 2;
				const $lineWidth = 4;
				ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
				ctx.beginPath();
				ctx.arc($radius, $radius, ($radius - $lineWidth / 2), -0.5 * Math.PI, (-0.5 + 2 * ($progress / 100)) * Math.PI);
				ctx.strokeStyle = "#2196f3";
				ctx.lineWidth = $lineWidth;
				ctx.stroke();

				// 当上传结束后，会进入后台保存中的状态，文件会先保存至总文件表
				if ($progress === 100) {
					createIndeterminateCirclePreloader(o.getElementsByClassName("preloader")[0], 30, "green-style");
					o.getElementsByClassName("preloader")[0].classList.add("active");
					/*
					 * o.getElementsByClassName("fileupload__saving-text")[0].textContent =
					 * "Saving...";
					 */
					o.classList.remove("upload_ready");
					o.classList.add("upload_finished");
					$uploadBoxCore.classList.remove("progress_shown");
					$uploadBoxCore.classList.add("saving_shown");
					removeSpecificEventListener(o, "drop", "dropFileEvent");
					removeSpecificEventListener($inputField, "change", "inputChangeEvent");
					removeUploadFileFromList();
				}

				window.requestAnimationFrame(displayProgress);
			}
		}

		// 保存至总文件表之后的回调函数，保存后即为完成状态
		xhr.onreadystatechange = function () {
			if (xhr.readyState === 4) {
				if (xhr.responseText === '{"ajaxSessionTimeOut":"yes"}') {
					ScmLoginBox.showLoginToast();
	            	while(o.hasChildNodes()){o.removeChild(o.firstChild);} 
					fileUploadMainModule(o.parentElement, options);
				}else if (xhr.responseText === '') {
					$.ajax({
				        url:options.checktimeouturl,
				        type:"post",
				        dataType:"json",
				        data:"",
				        success:function(data){
				            if (data.ajaxSessionTimeOut == "yes") {
				            	ScmLoginBox.showLoginToast();
				            	while(o.hasChildNodes()){o.removeChild(o.firstChild);} 
								fileUploadMainModule(o.parentElement, options);
				            }
				        },
						error:function(data){
			            }
					});
				}else{
					$uploadBoxCore.classList.remove("saving_shown");
					$uploadBoxCore.classList.add("finish_shown");
					if( typeof options.filecallback === 'function'){
						options.filecallback(xhr, options.filecallbackparam);
					}
				}
			}
		};
		xhr.send($formData);

		// 发送请求后，即进入上传中的状态
		$uploadBoxCore.classList.remove("initial_shown");
		$uploadBoxCore.classList.add("progress_shown");
	};

	/**
	 * 重置文件上传框
	 * 
	 * @param {HTMLElement}
	 *            o 所需上传文件的对象，其class中包含‘fileupload__box’
	 */
	const resetFileUploadBox = function (o) {
		// 移除所有文件框子节点，重新构造
		while (o.firstChild) {
			o.removeChild(o.firstChild);
		}
		createFileUploadBox(o);
		o.className = "fileupload__box"; // 重置文件框的样式
	};

	return {
		initialization: fileUploadMainModule,
		uploadFile: uploadFileToServer,
		resetFileUploadBox: resetFileUploadBox
	};

})();

/**
 * Appendix 1: 构造的上传框子元素 <div class="fileupload__core initial_shown"> <div
 * class="fileupload__initial"> <div class="fileupload__upload-logo"></div>
 * <input type="file" class="fileupload__input" title="&nbsp;"> </div> <div
 * class="fileupload__progress"> <canvas width="64" height="64"></canvas> <div
 * class="fileupload__progress_text"></div> </div> <div
 * class="fileupload__saving"> <div class="preloader"></div> <div
 * class="fileupload__saving-text">Saving...</div> </div> <div
 * class="fileupload__finish"></div> <div
 * class="fileupload__hint-text">点击或拖动选取文件</div> </div>
 */
