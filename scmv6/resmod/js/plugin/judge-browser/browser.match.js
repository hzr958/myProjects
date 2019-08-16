/**
 * @author ChuanjieHou
 * @date 2017-8-29
 */
;(function(window, document) {
	var OP_CONSTS = {EXACT: "=", LE: "-", GE: "+", INTERVAL: "~"};
	var UA_MAX_INT = 1000000000000;
	var UA_MIN_INT = 0;
	var UA_MIN_VERSION = 0;
	var UA_MAX_VERSION = 99999999999999999;
	var TRIE_REVERSE = 1;
	var TRIE_CONTINUE = 2;
	var UA_MAX_OFFSET = 8;
	/**
	 * Trie树节点
	 */
	var TrieNode = function(){
		/**
		 * 子节点数组，存储当前节点的所有直接子节点，最多能容纳256个子节点，每个节点代表一个ASCII码，范围0-255
		 */
		var next = null;
		var intervals; 	//存放版本区间的数组
		/**
		 * 查询线索节点，关联上一级节点对应的ASCII码的节点
		 */
		var search_clue = null;
		var key;	//当前节点能够匹配到的字符串的长度
		var value;	//当前节点能够匹配到的字符串
		/**
		 * 标志是否是贪婪的，用于匹配到之后继续进行匹配
		 */
		var greedy = false;
		var val;	//当前节点的值
	};
	var Trie = function(){
		this.root = new TrieNode();
	};
	Trie.prototype = {
		insert: function(str, mode){
			if(typeof str != "string"){
				console.log("trie.insert param str must be string");
				return null;
			}
			var pos, step, index;
			if(mode & TRIE_REVERSE){
				pos = str.length;
				step = -1;
			}else{
				pos = -1;
				step = 1;
			}
			var p = this.root;
			var i = 0;
			//搜索trie树，为字符串str建立相关节点
			while(i < str.length){
				pos = pos + step;
				index = str.charCodeAt(pos);
				if(index < 0 || index >= 256){
					continue;
				}
				//每一个节点下都有最多256个子节点
				if(!p.next){
					p.next = new Array(256);
				}
				//如果p.next[index]没有节点，则创建该节点
				if(!p.next[index]){
					p.next[index] = new TrieNode();
				}
				p = p.next[index];
				i++;
			}
			//直到str字符串最后一个字符的节点创建后，将该节点的key设置为str的长度，
			//用于搜索时判断该字符串，如果搜索的字符串找到p，并且长度一致，则说明搜索的字符串存在于Trie树
			p.key = str.length;
			p.value = str;
			p.val = str[pos];
			//如果mode为TRIE_CONTINUE,则设置当前字符串的greedy属性为1，标志当前字符串是贪婪的，用于继续搜索
			if( mode & TRIE_CONTINUE){
				p.greedy = true;
			}
			return p;
		},
		query: function(str, mode){
			var intervals, i, step, pos, index, p, root, version_pos, name;
			intervals = null;
			var trie_node = null;
			root = this.root;
			p = root;
			i = 0;
			if(mode & TRIE_REVERSE){
				pos = str.length;
				step = -1;
			}else{
				pos = -1;
				step = 1;
			}
			//空树，直接返回null
			if(!p.next){
				return null;
			}
			//循环遍历字符串，搜索其在Trie树中的位置
			while(i < str.length){
				pos += step;
				index = str.charCodeAt(pos);
				if(index < 0 || index >= 256){
					index = 0;
				}
				//如果p.next[index]为null，则搜索p的查询线索节点，直到搜索到p为根节点
				while( !p.next || !p.next[index]){
					if(p == root){
						break;
					}
					p = p.search_clue;
				}
				//找到该字符的节点，如果为null，p指向根节点
				p = p.next[index];
				p = !p ? root : p;
				//如果p.key存在
				if(p.key){
					//console.log(str.substring(pos, pos + p.key));
					name = p.value;
					intervals = p.intervals;
					version_pos = pos + p.key;
					if(!p.greedy){
						return {'name': name, 'intervals': intervals, 'pos': version_pos};
					}
					p = root;
				}
				i++;
			}
			return {'name': name, 'intervals': intervals, 'pos': version_pos};
		},
		/*
		 * 建立查询线索
		 */
		build_clue: function(){
			var i, head, tail;
			var q = new Array(300), p, t, root;
			root = this.root;
			head = tail = 0;
			/* q 为 创建的一个队列，用于存储所有节点，尾指针指向末端尚未建立查询线索的节点（通常为父节点），头指针指向下一个节点*/
			q[head++] = root;
			root.search_clue = null;
			while(head != tail){
				t = q[tail++];
				tail %= 300;
				if(!t.next){
					continue;
				}
				p = null;
				for(i=0; i < 256; i++){
					if(!t.next[i]){
						continue;
					}
					/* 如果当前t节点为根节点，那么将t节点下的所有子节点的查询线索节点都设为根节点,
					 * head指针增加，将头节点的下一级子节点的第i个放入q队列，头指针指向这个节点
					 */
					if(t == root){
						t.next[i].search_clue = root;
						q[head++] = t.next[i];
						head %= 300;
						continue;
					}
					/**
					 * t不是根节点，将此节点的查询线索节点（父节点）取出为p，
					 * 遍历其下所有子节点，将t节点的next[i]节点的查询线索节点设为t节点查询线索节点的next[i]节点
					 */
					p = t.search_clue;
					while(p){
						/**
						 * t节点的查询线索节点p的子节点中存在ASCII码为i的节点p.next[i],则设置t.next[i]节点为p.next[i]
						 */
						if(p.next && p.next[i]){
							t.next[i].search_clue = p.next[i];
							break;
						}
						/**
						 * t的查询线索节点p的子节点中不存在对应ASCII码的节点，则去p节点的查询线索节点的子节点中去查找线索节点
						 * 直到找到根节点，还是未找到
						 */
						p = p.search_clue;
					}
					/**
					 * 最终没有找到对应ASCII码的查询线索节点，则将t.next[i]的查询线索节点设为根节点
					 */
					if(!p){
						t.next[i].search_clue = root;
					}
					//将当前t.next[i]节点放入队列
					q[head++] = t.next[i];
					head %= 300;
				}
			}
			return true;
		}
	};
	//版本区间对象
	var Interval = function(){
		var left;	//起始版本
		var right;	//截止版本
		var val;	//匹配后的标识
		var modern = false;	//当前版本区间匹配到的是否是现代的，true或false
	};
	var UserAgent = function (settings){
		this.cnf = {};
		this.ua = navigator.userAgent;
		this.trie = new Trie();
		this.default_val = null;	//用于存储UA的默认识别值
		this.initialize(settings);
		return this.match();
	};
	UserAgent.prototype = {
		initCnf: function(){
			var trie = this.trie;
			var cnf = this.cnf;
			if(cnf['default']){
				this.default_val = cnf['default'];
			}
			if(cnf['greedy']){
				for(var i=0; i< cnf['greedy'].length; i++){
					var mode = TRIE_REVERSE | TRIE_CONTINUE;
					trie.insert(cnf['greedy'][i], mode);
					//console.log(cnf['greedy'][i]);
				}
			}
			if(cnf['names']){
				for(var i=0; i < cnf['names'].length; i++){
					var mode = TRIE_REVERSE;
					trie.insert(cnf['names'][i], mode);
				}
			}
			if(cnf['matches']){
				for(var i=0; i < cnf['matches'].length; i++){
					var m = cnf['matches'][i];
					var interval;
					if(m['version']){
						interval = this.getInterval(m['version']);
					}else{
						interval = new Interval();
						interval.left = UA_MIN_VERSION;
						interval.right = UA_MAX_VERSION;
					}
					if(interval){
						interval.val = m['value'];
						if(m['modern']){
							interval.modern = m['modern'];
						}
						var mode = TRIE_REVERSE;
						var trie_node = trie.insert(m['name'], mode);
						if(trie_node){
							var intervals = trie_node.intervals;
							if(!intervals){
								intervals = new Array();
							}
							for(var intval in intervals){
								if ((intval.left >= interval.left && intval.left <= interval.right)
									|| (intval.right >= interval.left && intval.right <= interval.right)
									|| (interval.left >= intval.left && interval.left <= intval.right)
									|| (interval.right >= intval.left && interval.right <= intval.right)){
									console.log("版本区间覆盖！");
									return null;
								}
							}
							intervals.push(interval);
							trie_node.intervals = intervals;
						}else{
							console.log("mathes识别出错！参数错误！");
							return null;
						}						
					}else{
						console.log("mathes识别出错！参数错误！");
						return null;
					}
				}
			}
			
			return this;
		},
		getInterval: function(value){
			var op = OP_CONSTS["EXACT"];
			var scale = UA_MAX_INT;
			var version = 0, ver = 0, n = 0;
			var interval = new Interval();
			interval.left = UA_MIN_VERSION;
			interval.right = UA_MAX_VERSION;
			for(i=0; i < value.length; i++){
				if(value[i] >= '0'&& value[i] <= '9'){
					ver = ver * 10 + parseInt(value[i]);
					continue;
				}
				if(value[i] == '.'){
					version += ver * scale;
		            ver = 0;
		            scale /= 10000;
				} else if (value[i] == OP_CONSTS["LE"]) {
		            if (i != value.length - 1) {
		                console.log("-后面不能有值");
		                return null;
		            }
		
		            op = OP_CONSTS["LE"];
		        } else if (value[i] == OP_CONSTS["EXACT"]) {
		            if (i != value.length - 1) {
		                console.log("=后面不能有值");
		                return null;
		            }
		        } else if (value[i] == OP_CONSTS.GE) {
		            if (i != value.length - 1) {
		                console.log("+后面不能有值");
		                return null;
		            }
		
		            op = OP_CONSTS["GE"];
		        } else if (value[i] == OP_CONSTS["INTERVAL"]) {
		            op = OP_CONSTS["INTERVAL"];
		            if (n >= 2) {
		                console.log("too many versions");
		                return null;
		            }
		
		            version += ver * scale;
		            interval.left = version;
		            n++;
		
		            ver = 0;
		            scale = UA_MAX_INT;
		            version = 0;
		
		            if (i + 1 >= value.length) {
		                console.log("~后面必须有值");
		                return null;
		            }
		
		            if (!(value[i + 1] >= '0' && value[i + 1] <= '9')) {
		                console.log("~后面必须为数值");
		                return null;
		            }
		        } else {
		            console.log("未能识别的版本范围！version: " + value);
		            return null;
		        }
			}
			version += ver * scale;
		    if (op == OP_CONSTS["LE"] || op == OP_CONSTS["INTERVAL"]) {
		        interval.right = version;
		
		    } else if (op == OP_CONSTS["GE"]) {
		        interval.left = version;
				
		    } else if (op == OP_CONSTS["EXACT"]) {
		        interval.left = version;
		        interval.right = version;
		    }
		
		    return interval;
		},
		initialize: function(settings){
			var trie = this.trie;
			if(settings && (typeof settings == 'object')){
				for(var name in settings){
					this.cnf[name] = settings[name];
				}
				if(!(this.initCnf() && trie.build_clue())){
					console.log("初始化参数信息及Trie树创建失败！")
				}
			}else{
				console.log("必须提供参数！");
			}
			
		},
		match: function(){
			var version, scale, ver, offset, intervals, ua = this.ua, name, real_ver, match, modern;
			if(ua){
				//console.log(ua)
				var mode = TRIE_REVERSE;
				var value = this.trie.query(ua, mode);
				if(value){
					//console.log(value)
					version = 0;
				    scale = UA_MAX_INT;
				    ver = 0;
				    offset = 0;
				    name = value.name;
				    for(pos = value.pos; pos < ua.length; pos++, offset++){
				        if(ua[pos] >= '0' && ua[pos] <= '9'){
				            break;
				        }else if (ua[pos] == ';' || ua[pos] == '(' || ua[pos] == ')'){
				            break;
				        }
				        if(offset >= UA_MAX_OFFSET) {
				            break;
				        }
				    }
				    real_ver = "";
				    for (; pos < ua.length; pos++) {
				    	//console.log(ua[pos])
				        if (ua[pos] == '.') {
				            version += ver * scale;
				            ver = 0;
				            scale /= 10000;
				            real_ver += '.';
				            continue;
				        } else if(ua[pos] >= '0' && ua[pos] <= '9') {
				            ver = ver * 10 + parseInt(ua[pos]);
				            real_ver += ua[pos];
				            continue;
				        }
				        break;
				    }
				    //console.log(real_ver);
				    version += ver * scale;
				    intervals = value.intervals;
				    for (i = 0; i < intervals.length; i++) {
				        if (version >= intervals[i].left && version <= intervals[i].right) {
				        	match = intervals[i].val;
				        	modern = intervals[i].modern;
				        	break;
				            //return {'name': name, 'version': real_ver, 'match': intervals[i].val};
				        }
				    }
				    match = match || this.default_val;
				    //return {'name': name, 'version': real_ver, 'match': this.default_val};
				}else{
					name = match = this.default_val;
					//return {'name': name, 'version': real_ver, 'match': this.default_val};
				}
			}else{
				name = match = this.default_val;
				//return {'name': name, 'version': real_ver, 'match': this.default_val};
			}
			if(match && match == this.default_val){
				modern = true;
			}
			return {'name': name, 'version': real_ver, 'match': match, 'modern': modern};
		}
	};
	var Browser = function(){
		var name;	//识别到的浏览器名称
		var match;	//匹配显示的浏览器名称
		var version;	//识别到的浏览器原始版本号字符串
		var closeVersion;	//浏览器原始版本号的近似值（整数）
		var platform;	//浏览器平台位数，Win64/Win32
	};
	Browser.prototype = {
		/*
		 * 360浏览器得识别一直是个难题。这里采用了一种偷鸡得判断方式，利用了mimeTypes来进行识别，
		 * 360极速内核模式下，会有一个类型application/vnd.chromium.remoting-viewer，
		 * 目前也仅在极速模式下才有，利用这个来判断360极速浏览器，测试了其他常见得浏览器，尚未发现这个
		 * 类型得标识。暂且认为试360浏览器独有吧。这个方法判断并不是100%准确。请谨慎使用。还有，尚
		 * 无法区分360极速浏览器和360安全浏览器。
		 */
		is360: function(){
			var flag = false;
			if(this.name.indexOf("Chrome") == -1){
				return flag;
			}
	        var mimeTypes = navigator.mimeTypes;
            for (var mt in mimeTypes) {
                if (mimeTypes[mt]["type"] == "application/vnd.chromium.remoting-viewer") {
                    flag = true;
                    break;
                }
            }
	        return flag;
		}
	};
	var System = function(){
		var name;	//识别到的操作系统名称
		var match;	//匹配显示的操作系统名称
		var version;	//识别到的操作系统原始版本号字符串
		var closeVersion;	//浏览器原始版本号的近似值（整数）
		var bit;
	};
	var Client = function(settings){
		var browser;
		var system;
		var ancientBrowser;
		var modernBrowser;
		var ancientSystem;
		var modernSystem;
		var language;
		
		this.initialize(settings);
	};
	Client.prototype = {
		initialize: function(settings){
			if(settings){
				if(settings.browser){
					for(var name in settings.browser){
						browser_cnf[name] = settings.browser[name];
					}
				}
				if(settings.os){
					for(var name in settings.browser){
						os_cnf[name] = settings.os[name];
					}
				}
			}
			var browser = new UserAgent(browser_cnf);
			var os = new UserAgent(os_cnf);
			//console.log(browser);
			//console.log(os);
			if(!!browser){
				this.browser = new Browser();
				this.browser.name = browser.name || browser.match;
				if(browser.version && browser.version.length > 0){
					this.browser.version = browser.version;
					var ver = browser.version;
					var dotindex1 = ver.indexOf(".") + 1;
					if(dotindex1 > 0){
						this.browser.closeVersion = ver.substring(0, dotindex1+1);
					}else{
						this.browser.closeVersion = ver;
					}
				}
				this.browser.match = browser.match;
				this.ancientBrowser = !(this.modernBrowser = (browser.modern || false));
				this.browser.platform = navigator.platform;
			}
			if(!!os){
				this.system = new System();
				this.system.name = os.name || os.match;
				if(os.version && os.version.length > 0){
					this.system.version = os.version;
					this.system.closeVersion = parseFloat(os.version);
					var ver = os.version;
					var dotindex1 = ver.indexOf(".") + 1;
					if(dotindex1 > 0){
						this.system.closeVersion = ver.substring(0, dotindex1+1);
					}else{
						this.system.closeVersion = ver;
					}
				}
				this.system.match = os.match;
				this.ancientSystem = !(this.modernSystem = (os.modern || false));
			}
			var lang = navigator.language || navigator.userLanguage || navigator.browserLanguage || navigator.systemLanguage;
			this.language = lang.replace(/-/g, '_');
		}
	};
	var browser_cnf = {
		/* 未识别的默认值 */
		'default': 'unkonwn',
		/* 从右向左扫描ua字符串，匹配到标记为greedy数组中名称的浏览器，
		 * 继续向左匹配，直到找不到greedy/names/matches数组中给定的浏
		 * 览器字符串标识，返回最后一次匹配到的标识。 */
		'greedy': [
			'Chrome', 'Safari', 'Firefox', 'Trident', 'OPR', 'QQBrowser', 'UBrowser', 'UCBrowser', 'BIDUBrowser', 'LBBROWSER'
		],
		/*
		 * 浏览器名称的字符串标识
		 */
		'names': [
			'Edge', 'Trident', 'MSIE'
		],
		/*
		 * 对浏览器名称的字符串标识按照配置进行识别替换，如Chrome/38.0以上版本识别为chrome_high
		 */
		'matches': [
			{'name': 'Chrome', 'version': '38.0+', 'value': 'chrome_high', 'modern': true},
			{'name': 'Chrome', 'version': '0~37.9999', 'value': 'chrome_low'},
			{'name': 'Firefox', 'version': '40.0+', 'value': 'firefox_high', 'modern': true},
			{'name': 'Firefox', 'version': '0~39.9999', 'value': 'firefox_low'},
			{'name': 'Edge', 'value': 'edge', 'modern': true},
			{'name': 'Trident', 'version': '7.0', 'value': 'msie11', 'modern': true},
			{'name': 'Trident', 'version': '6.0', 'value': 'msie10'},
			{'name': 'Trident', 'version': '5.0', 'value': 'msie9'},
			{'name': 'Trident', 'version': '4.0', 'value': 'msie8'},
			{'name': 'MSIE', 'version': '10.0', 'value': 'msie10'},
			{'name': 'MSIE', 'version': '9.0', 'value': 'msie9'},
			{'name': 'MSIE', 'version': '8.0', 'value': 'msie8'},
			{'name': 'MSIE', 'version': '7.0', 'value': 'msie7'},
			{'name': 'MSIE', 'version': '6.0', 'value': 'msie6'},
			{'name': 'MSIE', 'version': '5.9999-', 'value': 'msie_low'},
		]
	};
	var os_cnf = {
		'default': 'unkonwn',
		'names': [
			'Windows NT', 'Linux', 'unix', 'Mac', 'Android', 'iPad', 'iPhone', 'Windows Phone'
		],
		'matches': [
			{'name': 'iPad', 'value': 'ipad', 'modern': true},
			{'name': 'iPhone', 'value': 'iphone', 'modern': true},
			{'name': 'Symbian', 'value': 'symbian'},
			{'name': 'MeeGo', 'value': 'meego'},
			{'name': 'Windows Phone', 'value': 'windowsphone'},
			{'name': 'Android', 'value': 'android', 'modern': true},
			{'name': 'BlackBerry', 'value': 'blackberry'},
			{'name': 'MIUI', 'value': 'miui'},
			{'name': 'Windows NT', 'version': '5.0', 'value': 'windows_server_2000'},
			{'name': 'Windows NT', 'version': '5.1', 'value': 'windows_xp'},
			{'name': 'Windows NT', 'version': '5.2', 'value': 'windows_server_2003'},
			{'name': 'Windows NT', 'version': '6.0', 'value': 'windows_vista'},
			{'name': 'Windows NT', 'version': '6.1', 'value': 'windows_7', 'modern': true},
			{'name': 'Windows NT', 'version': '6.2', 'value': 'windows_8', 'modern': true},
			{'name': 'Windows NT', 'version': '6.3', 'value': 'windows_8_1', 'modern': true},
			{'name': 'Windows NT', 'version': '6.4', 'value': 'windows_10_preview', 'modern': true},
			{'name': 'Windows NT', 'version': '10.0', 'value': 'windows_10', 'modern': true},
			{'name': 'Linux', 'value': 'linux', 'modern': true},
			{'name': 'unix', 'value': 'unix', 'modern': true},
			{'name': 'FreeBSD', 'value': 'freebsd', 'modern': true},
			{'name': 'Mac', 'value': 'macos', 'modern': true},
			{'name': 'Solaris', 'value': 'solaris', 'modern': true},
			{'name': 'SunOS', 'value': 'solaris', 'modern': true}
		]
	};
	window.Client = Client;
	window.client = new Client();
}(window, document));