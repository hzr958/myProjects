(function (window, document) {


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

    const $observerArray = [];

    window.showDialog = function (str) {
        const $dialog = Array.from(document.getElementsByClassName("dialogs__box")).filter(function (x) {
            return x.getAttribute("dialog-id") === str;
        })[0];
        try {
            if (!$dialog) {
                throw 'No dialog named "' + str + '".';
            }
        } catch (e) {
            console.error(e);
        }
        const $dialogDir = $dialog.getAttribute("flyin-direction") || "bottom";

        dialogDisableScroll($dialog);
        const $cover = document.createElement("div");
        $cover.className = "background-cover cover_colored";
        $cover.setAttribute("dialog-target", str);
        document.body.appendChild($cover);

        var $dialogHeight = 0;
        Array.from($dialog.children).forEach(function (x) {
            const $height = x.style.height ? parseInt(x.style.height) : x.scrollHeight;
            $dialogHeight = $dialogHeight + $height;
        });
        // 补丁，二次确认框变小问题SCM-16225
        if(str === "dev_jconfirm_ui"  && $dialog.style.height &&  parseInt ( $dialog.style.height ) > $dialogHeight ){
            $dialogHeight  =parseInt ( $dialog.style.height ) ;
        }
        
        /*
         * 缩小窗口高度，存在窗口超出头部菜单栏的问题 源代码：$dialogHeight = Math.min($dialogHeight,
         * $cover.clientHeight - 16); 更改代码：$dialogHeight =
         * Math.min($dialogHeight, $cover.clientHeight - 110);
         */  
        $dialogHeight = Math.min($dialogHeight, $cover.clientHeight - 110);
        const $dialogWidth = $dialog.getBoundingClientRect().width;
        const $windowHeight = $cover.clientHeight;
        const $windowWidth = $cover.clientWidth;
        const $dialogLeft = ($windowWidth - $dialogWidth) / 2;
        const $dialogTop = ($windowHeight - $dialogHeight) / 2;


        const $dialogTransition = "all 200ms ease-out";

        function animationProcess() {
            setTimeout(function () {
                $dialog.style.cssText = 'width: ' + $dialogWidth + 'px; height: ' + $dialogHeight + 'px; top: ' + $dialogTop + 'px; left: ' + $dialogLeft + 'px; visibility: visible; opacity: 1; transition: ' + $dialogTransition + ';';
            }, 32);
            setTimeout(function () {
                $dialog.style.transition = "";
                var indexxx = 0 ;
                const $observer = new MutationObserver(function (mutations) {
                    var $dialogHeight = 0;
                    Array.from($dialog.children).forEach(function (x) {
                        const $height = x.style.height ? parseInt(x.style.height) : x.clientHeight;
                        $dialogHeight = $dialogHeight + $height;
                    });
                    $dialogHeight = ($dialogHeight >= $cover.clientHeight - 110) ? $cover.clientHeight - 110 : $dialogHeight;
                    if ($dialogHeight !== $dialog.clientHeight) {
                        // 补丁，二次确认框变小问题SCM-16225
                        if($dialog.getAttribute("dialog-id") === "dev_jconfirm_ui" ){
                            // 确认框不要重新设置高度，否则会向下移动
                        }else{
                            $dialog.style.top = ($cover.clientHeight - $dialogHeight) / 2 + 'px';
                            $dialog.style.height = $dialogHeight + 'px';
                        }
                        
                    }
                    mutations.forEach(function(mutation){
                        if (mutation.target === $cover){
                            $dialog.style.left = ($cover.clientWidth - $dialog.clientWidth) / 2 + 'px';
                        }
                    });
                });
                const $configDialog = {
                    attributes: true,
                    subtree: true,
                    characterData: true,
                    childList: true,
                    attributeFilter: ["style"]
                };
                const $configWindow = {
                    attributes: true,
                    attributeOldValue: true,
                    subtree: true,
                    characterData: true,
                    childList: true,
                    attributeFilter: ["width"]
                };
                Array.from($dialog.children).forEach(function(x){
                    $observer.observe(x, $configDialog);
                })
                $observer.observe($cover, $configWindow);
                $observerArray.push({
                    name: str,
                    observer: $observer
                });
            }, 248);
        }
        switch ($dialogDir) {
            case "bottom":
                $dialog.style.cssText = 'width: ' + $dialogWidth + 'px; height: ' + $dialogHeight + 'px; top: ' + $windowHeight + 'px; left: ' + $dialogLeft + 'px; visibility: visible;';
                animationProcess();
                break;
            case "top":
                $dialog.style.cssText = 'width: ' + $dialogWidth + 'px; height: ' + $dialogHeight + 'px; top: -' + $dialogHeight + 'px; left: ' + $dialogLeft + 'px; visibility: visible;';
                animationProcess();
                break;
            case "left":
                $dialog.style.cssText = 'width: ' + $dialogWidth + 'px; height: ' + $dialogHeight + 'px; top: ' + $dialogTop + 'px; left: -' + $dialogWidth + 'px; visibility: visible;';
                animationProcess();
                break;
            case "right":
                $dialog.style.cssText = 'width: ' + $dialogWidth + 'px; height: ' + $dialogHeight + 'px; top: ' + $dialogTop + 'px; left: ' + $windowWidth + 'px; visibility: visible;';
                animationProcess();
                break;
        }

        if ($dialog.getAttribute("cover-event") === "hide") {
            setTimeout(function () {
                $cover.addEventListener("click", function (e) {
                    e.stopPropagation();
                    hideDialog(str);
                    if(document.getElementsByClassName("datepicker__box").length > 0){
                        document.body.removeChild(document.getElementsByClassName("datepicker__box")[0])
                    }
                    if(document.getElementsByClassName("ac__box").length > 0){
                        document.body.removeChild(document.getElementsByClassName("ac__box")[0]);
                    }
                });
                document.addEventListener("keydown", function (e) {
                    if(e.keyCode == 27){
                        e.stopPropagation();
                        e.preventDefault();
                        hideDialog(str);
                        if(document.getElementsByClassName("datepicker__box").length > 0){
                            document.body.removeChild(document.getElementsByClassName("datepicker__box")[0])
                        }
                        if(document.getElementsByClassName("ac__box").length > 0){
                            document.body.removeChild(document.getElementsByClassName("ac__box")[0]);
                        }
                    }
                });
            }, 248);
        }
        
        const windowResizeListener = function () {
            if (document.getElementsByClassName("background-cover cover_colored").length > 0) {
                Array.from(document.getElementsByClassName("background-cover cover_colored")).forEach(function (x) {
                    x.setAttribute("width", window.innerWidth);
                });
            }
        };
        addSpecificEventListener(window, "resize", "windowResizeListener", windowResizeListener);
    };

    window.hideDialog = function (str) {
        const $dialog = Array.from(document.getElementsByClassName("dialogs__box")).filter(function (x) {
            return x.getAttribute("dialog-id") === str;
        })[0];
        const $cover = Array.from(document.getElementsByClassName("background-cover cover_colored")).filter(function (x) {
            return x.getAttribute("dialog-target") === str;
        })[0];
        const $dialogDir = $dialog.getAttribute("flyin-direction") || "bottom";
        const $dialogHeight = $dialog.getBoundingClientRect().height;
        const $dialogWidth = $dialog.getBoundingClientRect().width;
        if($cover != undefined ){
          const $windowHeight = $cover.clientHeight;
          const $windowWidth = $cover.clientWidth;
          const $dialogTransition = "all 200ms ease-out";
          $observerArray.forEach(function (x, idx) {
              if (x.name === str) {
                  x.observer.disconnect();
                  $observerArray.splice(idx, 1);
              }
          });

          function hideProcess() {
              // SCM-13720 去掉延时执行，不然两个弹出框切换时会一闪一闪的
  // setTimeout(function () {
                  $dialog.style.transition = "";
                  $dialog.style.opacity = 0;
                  // 修改引起 edge页面卡死问题 $dialog.style.visibility = "hidden";;
                  // 用样式操作会导致页面卡死
                  $dialog.setAttribute("visibility","hidden");
                  $cover.parentNode.removeChild($cover);
                  if (document.getElementsByClassName("background_cover cover_colored").length <= 1) {
                      document.body.classList.remove("js_dialognoscroll");
                      removeSpecificEventListener(document, "mousewheel", "dialogScrollHandler");
                      removeSpecificEventListener(document, "DOMMouseScroll", "dialogScrollHandler");
                  }
  // }, 248);
          }
          $cover.style.pointerEvents = "none"; // 防止重复点击
          $dialog.style.transition = $dialogTransition; // 设置动画效果
          switch ($dialogDir) {
              case "bottom":
                  $dialog.style.top = $windowHeight + 'px';
                  hideProcess();
                  break;
              case "top":
                  $dialog.style.top = '-' + $dialogHeight + 'px';
                  hideProcess();
                  break;
              case "left":
                  $dialog.style.left = '-' + $dialogWidth + 'px';
                  hideProcess();
                  break;
              case "right":
                  $dialog.style.left = $windowWidth + 'px';
                  hideProcess();
                  break;
          }
      };
   }
        
    function dialogDisableScroll(el) {
    	el.classList.add("js_dialognoscroll");
        const dialogScrollHandler = function (e) {
            if (!document.body.classList.contains("js_dialognoscroll")) {
                return;
            } else {
                if (e.target.classList.contains("background-cover")) {
                    e.preventDefault();
                } else {
                    // 找到最近的滚动元素
                    var p = e.target;
                    while ((p !== el && (p.clientWidth >= p.offsetWidth - 8)) || p.clientWidth === 0) {
                        p = p.parentNode;
                    }
                    const $wheelY = e.wheelDelta || -e.detail;
                    p.scrollTop += ($wheelY < 0 ? 1 : -1) * 30;
                    e.preventDefault();
                }
            }
        };
        addSpecificEventListener(document, "mousewheel", "dialogScrollHandler", dialogScrollHandler);
        addSpecificEventListener(document, "DOMMouseScroll", "dialogScrollHandler", dialogScrollHandler);
    }

})(window, document);
