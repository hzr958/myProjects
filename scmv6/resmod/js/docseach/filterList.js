/**
 * 列表检索------zzx-----
 * 大叔，你的卡丁车好大只哦~
 * @param window
 * @param document
 */
(function (window, document) {
	//Main 方法
	var FilterList = function(options){
		var self_obj = this;
		var defaults = {
				"list":$("*[scm-id='span_id']"),//li父级
				"list-item":".main-list__item ",
				"search":""//检索节点
		};
		if (typeof options != "undefined") {
			defaults=$.extend({},defaults, options);
	    }
		self_obj.doSearch(defaults);
	};
	FilterList.prototype.doSearch = function(defaults){
		defaults.search.keyup( function () {
	        var filter = $(this).val().trim();
	        if(filter) {
	        	$matches = defaults.list.find(".dev_filter_class[filter_name*='"+filter+"']").closest(""+defaults['list-item']);
	        	$(""+defaults['list-item'], defaults.list).not($matches).slideUp();
	        	$matches.slideDown();
	        } else {
	        	defaults.list.find(""+defaults['list-item']).slideDown();
	        }
	        return false;
	      });
	}
	//主入口
	window.FilterList = function (options) {
		return new FilterList(options);
	};
})(window, document);