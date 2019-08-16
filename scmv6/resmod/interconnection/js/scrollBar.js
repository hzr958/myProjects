(function(win){ 
    var doc = win.document,db = doc.body; 
    var mousewheel = 'onmousewheel' in document ? 'mousewheel' : 'DOMMouseScroll'; 
    var skyScroll = function(opts){ return new skyScroll.prototype.init(opts);}; 
    skyScroll.prototype = { 
        constructor:skyScroll, 
        //初始化 
        init:function(opts){ 
            var set = _extend({ 
                target:'contentbox', 
                dir:'top',
                height:376,/*
                this.height = document.getElementById("scrollTest").clientHeight,*/
                callback:function(){} 
            },opts||{}); 
            var _this = this,mousemoveHandle,mousedownHandle; 
            this.target = _$(set.target); 
            this.parent = this.target.parentNode; 
            this.width = set.width; 
            this.height = set.height;
            this.dir = set.dir; 
            this.callback = set.callback; 
            this.addWarpper(set.dir); 
            switch(set.dir){ 
                case 'top': 
                    this.addVscroll(); 
                    break; 
                case 'left': 
                    this.addLscroll(); 
                    break; 
                default : 
                    this.addVscroll(); 
                    this.addLscroll(); 
            }; 
            _addEvent(doc,'mousedown',function(e){ 
                var e = e || window.event,target = e.target || e.srcElement,pos= _getMousePos(e); 
                if(target == _this.vScroll || target == _this.lScroll){ 
                    pos.tTop = parseInt(_this.target.style.top); 
                    pos.tLeft = parseInt(_this.target.style.left); 
                    pos.sTop = parseInt(target.style.top); 
                    pos.sLeft = parseInt(target.style.left); 
                    mousemoveHandle = _mousemoveHandle.call(_this,pos,target); 
                    _addEvent(doc,'mousemove',mousemoveHandle); 
                    _addEvent(doc,'mouseup',function(){_removeEvent(doc,'mousemove',mousemoveHandle)}); 
                }; 
                if(target == _this.vScrollOuter || target == _this.lScrollOuter){ 
                    _mounsedownHandle.call(_this,pos,target); 
                }; 
            }); 
        },   
        //对外提供重新计算滚动条高度或宽度以及滚动范围的方法，用于动态改变内容时，作出的相对应的调整 
        recalculated:function(){ 
            var H = this.target.offsetHeight,W = this.target.offsetWidth,T = parseInt(this.target.style.top),L = parseInt(this.target.style.left),h,w; 
            this.ratio = {l:this.width / W,v:this.height / H}; 
            this.range = {l:W-this.width, t: H - this.height}; 
            if(this.vScroll){ 
                h = Math.round(Math.pow(this.height,2) / H); 
                this.vScroll.style.height = h+'px'; 
                this.vScroll.style.top = Math.round(this.height * (-T/H)) + 'px'; 
                this.range.st = this.height - h; 
                this.wrapper.style.height = this.height + 'px'; 
            }; 
            if(this.lScroll){ 
                w = Math.round(Math.pow(this.width,2) / W) 
                this.lScroll.style.width = w + 'px'; 
                this.lScroll.style.left = Math.round(this.width * (-L/W)) + 'px'; 
                this.range.sl = this.width - w; 
                this.wrapper.style.width = this.width + 'px'; 
            }; 
        }, 
        //对外提供设置滚动条的位置的方法 
        set:function(pos){ 
            if(!_isObject(pos)) throw new Error('参数类型错误，参数必须是object!'); 
            if(pos.top && !isNaN(parseInt(pos.top)) && this.vScroll){ 
                var top = Math.min(pos.top,this.range.t); 
                this.target.style.top = -top + 'px'; 
                this.vScroll.style.top = Math.round(this.height * (top / this.target.offsetHeight)) + 'px'; 
            }; 
            if(pos.left && !isNaN(parseInt(pos.left)) && this.lScroll){ 
                var left = Math.min(pos.left,this.range.l); 
                this.target.style.left = -left + 'px'; 
                this.lScroll.style.left = Math.round(this.width * (left / this.target.offsetWidth)) + 'px'; 
            }; 
        }, 
        addWarpper:function(dir){ 
            if(this.wrapper) return; 
            var _this = this,W = this.target.offsetWidth,H = this.target.offsetHeight,mousewheelHandle; 
            this.wrapper = _createEl('<div class="sky_warpper" style="position:relative;overflow:hidden;"></div>',this.parent); 
            this.wrapper.appendChild(this.target); 
            this.target.style.cssText = 'position:absolute;top:0;left:0'; 
            switch(dir){ 
                case 'top': 
                    this.wrapper.style.height = this.height + 'px'; 
                    this.wrapper.style.width = W + 'px'; 
                    break; 
                case 'left': 
                    this.wrapper.style.height = H + 'px'; 
                    this.wrapper.style.width = this.width + 'px'; 
                    break; 
                default : 
                    this.wrapper.style.width = this.width + 'px'; 
                    this.wrapper.style.height = this.height + 'px'; 
            }; 
            _addEvent(this.wrapper,'mouseenter',function(e){ 
                var pos = {}; 
                pos.tTop = parseInt(_this.target.style.top); 
                pos.tLeft = parseInt(_this.target.style.left); 
                if(_this.vScroll) pos.sTop = parseInt(_this.vScroll.style.top); 
                if(_this.lScroll) pos.sLeft = parseInt(_this.lScroll.style.left); 
                mousewheelHandle = _mousewheelHandle.call(_this,pos); 
                _addEvent(_this.wrapper,'mousewheel',mousewheelHandle); 
                _addEvent(_this.wrapper,'mouseleave',function(){_removeEvent(_this.wrapper,'mousewheel',mousewheelHandle)}); 
            }); 
        }, 
        //对外提供添加竖向滚动条的方法 
        addVscroll:function(){ 
            if(this.vScroll) return; 
            !this.wrapper && this.addWarpper('top'); 
            this.vScrollOuter = _createEl('<div class="sky_scrollTopOuter" style="position:absolute;top:0;right:0;height:'+this.height+'px;overflow:hidden"></div>',this.wrapper) 
            this.vScroll = _createEl('<div class="sky_scrollTop" style="position:absolute;top:0;right:0;"></div>',this.wrapper); 
            this.recalculated(); 
        }, 
        //对外提供添加横向滚动条的方法 
        addLscroll:function(){ 
            if(this.lScroll) return; 
            !this.wrapper && this.addWarpper('left'); 
            this.lScrollOuter = _createEl('<div class="sky_scrollLeftOuter" style="position:absolute;bottom:0;left:0;width:'+this.width+'px;overflow:hidden"></div>',this.wrapper) 
            this.lScroll = _createEl('<div class="sky_scrollLeft" style="position:absolute;left:0;bottom:0;"></div>',this.wrapper); 
            this.recalculated(); 
        }, 
        //删除竖向滚动条 
        delVscroll:function(){ 
            _deleteScroll.call(this,1,this.vScroll,this.vScrollOuter,this.lScroll,this.lScrollOuter); 
        }, 
        //删除横向滚动条    
        delLscroll:function(){ 
            _deleteScroll.call(this,0,this.lScroll,this.lScrollOuter,this.vScroll,this.vScrollOuter); 
        } 
    }; 
    skyScroll.prototype.init.prototype = skyScroll.prototype; 
    window.skyScroll = skyScroll; 
    /*************************私有函数*************************/
    function _mousemoveHandle(pos,target){ 
        var _this = this; 
        return target == this.vScroll ? function(e){ 
            e = e || window.event; 
            var newPos = _getMousePos(e); 
            _this.target.style.top = Math.min(0,Math.max(pos.tTop + (pos.y - newPos.y)/_this.ratio.v,-_this.range.t)) + 'px'; 
            target.style.top = Math.max(0,Math.min(pos.sTop - pos.y + newPos.y,_this.range.st))+ 'px'; 
            _this.callback.call(_this); 
            _cancelSelect() 
        }:function(e){ 
            e = e || window.event; 
            var newPos = _getMousePos(e); 
            _this.target.style.left = Math.min(0,Math.max(pos.tLeft + (pos.x - newPos.x)/_this.ratio.l,-_this.range.l)) + 'px'; 
            target.style.left = Math.max(0,Math.min(pos.sLeft - pos.x + newPos.x,_this.range.sl)) + 'px'; 
            _this.callback.call(_this); 
            _cancelSelect(); 
        } 
    }; 

    function _mousewheelHandle(pos){ 
        var _this = this; 
        return this.vScroll ? function(e){ 
            e = e || window.event; 
            _stopEvent(e); 
            var data = e.wheelDelta ? e.wheelDelta /120 : -e.detail/3; 
            var top = parseInt(_this.target.style.top); 
            var sTop = parseInt(_this.vScroll.style.top); 
            var dist = data * 5; 
            _this.target.style.top = Math.min(0,Math.max(top + dist / _this.ratio.v, -_this.range.t)) + 'px'; 
            _this.vScroll.style.top = Math.max(0,Math.min(sTop-dist,_this.range.st)) + 'px'; 
            _this.callback.call(_this); 
        }:function(e){ 
            e = e || window.event; 
            _stopEvent(e); 
            var data = e.wheelDelta ? e.wheelDelta /120 : -e.detail/3; 
            var left = parseInt(_this.target.style.left); 
            var sLeft = parseInt(_this.lScroll.style.left); 
            var dist = data * 5; 
            _this.target.style.left = Math.min(0,Math.max(left + dist / _this.ratio.l, -_this.range.l)) + 'px'; 
            _this.lScroll.style.left = Math.max(0,Math.min(sLeft-dist,_this.range.sl)) + 'px'; 
            _this.callback.call(_this); 
        } 
    }; 
    function _mounsedownHandle(pos,target){ 
        var _this = this; 
        var elPos = _getElementPosition(target); 
        if(target == this.vScrollOuter){ 
            console.log(pos.y - elPos.y); 
            _this.set({ 
                top:pos.y - elPos.y 
            }); 
        }else{ 
            _this.set({ 
                left:pos.x - elPos.x 
            }); 
        }; 
    }; 
    function _deleteScroll(n,s1,s11,s2,s22){ 
        var o = n ? 'Height' : 'Width' ,s = n ? 'top' : 'left'; 
        if(!s1) return; 
        this.wrapper.removeChild(s1); 
        this.wrapper.removeChild(s11); 
        n ?  (this.vScroll = null) : (this.lScroll = null); 
        if(!s2){ 
            this.wrapper.parentNode.appendChild(this.target); 
            this.wrapper.parentNode.removeChild(this.wrapper); 
            this.target.style.cssText = ''; 
            this.wrapper = null; 
        }else{ 
            this.wrapper.style[o.toLowerCase()] = this.target['offset'+o] + 'px'; 
            this.recalculated(); 
        }; 
        this.target.style[s] = '0px'; 
        //this.target.style[o.toLowerCase()]= 'auto'; 
    }; 
    /*************************工具函数*************************/
    function _$(id){ 
        return typeof id === 'string' ? doc.getElementById(id) : id; 
    }; 
    function _extend(target,source){ 
        for(var key in source) target[key] = source[key]; 
        return target; 
    }; 
    function _createEl(html,parent){ 
        var div = doc.createElement('div'); 
        div.innerHTML = html; 
        el = div.firstChild; 
        parent && parent.appendChild(el); 
        return el; 
    }; 
    function _getMousePos(e){ 
        if(e.pageX || e.pageY) return {x:e.pageX,y:e.pageY}; 
        return { 
            x:e.clientX + document.documentElement.scrollLeft - document.body.clientLeft, 
            y:e.clientY + document.documentElement.scrollTop - document.body.clientTop 
        }; 
    }; 
    function _isObject(o){ 
        return o === Object(o); 
    }; 
    function _getElByClass(node,oClass,parent){ 
        var re = [],els,parent = parent || doc; 
        els = parent.getElementsByTagName(node); 
        for(var i=0,len=els.length;i<len;i++){ 
            if((' ' + els[i].className+' ').indexOf(' '+oClass+' ') > -1) re.push(els[i]); 
        }; 
        return re; 
    }; 
    function _stopEvent(e){ 
        e.stopPropagation ? e.stopPropagation() : (e.cancelBubble = true); 
        e.preventDefault ? e.preventDefault() :(e.returnValue = false); 
    }; 
    function _addEvent(el,type,fn){ 
        if(typeof el.addEventListener != 'undefined'){ 
            if(type == 'mouseenter'){ 
                el.addEventListener('mouseover',_findElement(fn),false); 
            }else if(type === 'mouseleave'){ 
                el.addEventListener('mouseout',_findElement(fn),false); 
            }else{ 
                el.addEventListener(type,fn,false); 
            } 
        }else if(typeof el.attachEvent != 'undefined'){ 
            el.attachEvent('on'+type,fn); 
        }else{ 
            el['on'+type] = fn; 
        } 
    }; 
    function _removeEvent(el,type,fn){ 
        if(typeof el.removeEventListener != 'undefined'){ 
            el.removeEventListener(type,fn,false); 
        }else if(typeof el.detachEvent != 'undefined'){ 
            el.detachEvent('on'+type,fn); 
        }else{ 
            el['on'+type] = null; 
        } 
    }; 
    function _findElement(fn){ 
        return function(e){ 
            var parent = e.relatedTarget; 
            while(parent && parent != this) parent = parent.parentNode; 
            if(parent != this) fn.call(this,e); 
        } 
    }; 
    function _cancelSelect(){ 
        if (window.getSelection) { 
            if (window.getSelection().empty) {  // Chrome 
                window.getSelection().empty(); 
            } else if (window.getSelection().removeAllRanges) {  // Firefox 
                window.getSelection().removeAllRanges(); 
            } 
        }else if (document.selection) {  // IE? 
          document.selection.empty(); 
        } 
    }; 
    function _getElementPosition(el){ 
        var x = 0,y=0; 
        if(el.getBoundingClientRect){ 
            var pos = el.getBoundingClientRect(); 
            var d_root = document.documentElement,db = document.body; 
            x = pos.left + Math.max(d_root.scrollLeft,db.scrollLeft) - d_root.clientLeft; 
            y = pos.top + Math.max(d_root.scrollTop,db.scrollTop) - d_root.clientTop; 
        }else{ 
            while(el != db){ 
                x += el.offsetLeft; 
                y += el.offsetTop; 
                el = el.offsetParent; 
            }; 
        }; 
        return { 
            x:x, 
            y:y 
        }; 
    }; 
})(window); 