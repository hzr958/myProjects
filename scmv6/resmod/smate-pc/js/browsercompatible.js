if (!Array.from) {
	Array.from = (function () {
		var toStr = Object.prototype.toString;
		var isCallable = function (fn) {
			return typeof fn === 'function' || toStr.call(fn) === '[object Function]';
		};
		var toInteger = function (value) {
			var number = Number(value);
			if (isNaN(number)) {
				return 0;
			}
			if (number === 0 || !isFinite(number)) {
				return number;
			}
			return (number > 0 ? 1 : -1) * Math.floor(Math.abs(number));
		};
		var maxSafeInteger = Math.pow(2, 53) - 1;
		var toLength = function (value) {
			var len = toInteger(value);
			return Math.min(Math.max(len, 0), maxSafeInteger);
		};
		return function from(arrayLike) {
			var C = this;
			var items = Object(arrayLike);
			if (arrayLike == null) {
				throw new TypeError('Array.from requires an array-like object - not null or undefined');
			}
			var mapFn = arguments.length > 1 ? arguments[1] : void undefined;
			var T;
			if (typeof mapFn !== 'undefined') {
				if (!isCallable(mapFn)) {
					throw new TypeError('Array.from: when provided, the second argument must be a function');
				}
				if (arguments.length > 2) {
					T = arguments[2];
				}
			}
			var len = toLength(items.length);
			var A = isCallable(C) ? Object(new C(len)) : new Array(len);
			var k = 0;
			var kValue;
			while (k < len) {
				kValue = items[k];
				if (mapFn) {
					A[k] = typeof T === 'undefined' ? mapFn(kValue, k) : mapFn.call(T, kValue, k);
				} else {
					A[k] = kValue;
				}
				k += 1;
			}
			A.length = len;
			return A;
		};
	}());
}

if (window.Element && !Element.prototype.closest) {
	Element.prototype.closest =
		function (s) {
			var matches = (this.document || this.ownerDocument).querySelectorAll(s),
				i,
				el = this;
			do {
				i = matches.length;
				while (--i >= 0 && matches.item(i) !== el) {};
			} while ((i < 0) && (el = el.parentElement));
			return el;
		};
}

if (typeof Object.assign != 'function') {
	Object.assign = function (target, varArgs) {
		'use strict';
		if (target == null) {
			throw new TypeError('Cannot convert undefined or null to object');
		}

		var to = Object(target);

		for (var index = 1; index < arguments.length; index++) {
			var nextSource = arguments[index];

			if (nextSource != null) {
				for (var nextKey in nextSource) {
					if (Object.prototype.hasOwnProperty.call(nextSource, nextKey)) {
						to[nextKey] = nextSource[nextKey];
					}
				}
			}
		}
		return to;
	};
}

(function (arr) {
  arr.forEach(function (item) {
    if (item.hasOwnProperty('remove')) {
      return;
    }
    Object.defineProperty(item, 'remove', {
      configurable: true,
      enumerable: true,
      writable: true,
      value: function remove() {
        this.parentNode.removeChild(this);
      }
    });
  });
})([Element.prototype, CharacterData.prototype, DocumentType.prototype]);

function encodeFormData(data) {
	if (!data) {
		return "";
	}
	var pairs = [];
	for (var name in data) {
		if (!data.hasOwnProperty(name)) {
			continue;
		}
		if (typeof data[name] === "function") {
			continue;
		}
		var value = data[name].toString();
		pairs.push(name + "=" + value);
	}
	return pairs.join('&');
}

/**
 * 把请求需要提交的参数转化成一个表单对象
 * @param	{Object}		data			所需提交参数对象
 * 
 * @return	{FormData}
 */
function convertToFormData(data) {
	var oStr = '';
	const $formData = new FormData();
	for (var key in data) {
		if (data.hasOwnProperty(key) && typeof data[key] !== "function") {
			$formData.append(key, data[key]);
			　oStr += key+"="+encodeURIComponent(data[key])+"&";
		}
	}
	return oStr;
}

function parentElements(el, parentSelector) {
    if (parentSelector === undefined) {
        parentSelector = document;
    }
    var parents = [];
    var p = el.parentNode;
    while (p !== parentSelector) {
        var o = p;
        parents.push(o);
        p = o.parentNode;
    }
    parents.push(parentSelector);
    return parents;
}
