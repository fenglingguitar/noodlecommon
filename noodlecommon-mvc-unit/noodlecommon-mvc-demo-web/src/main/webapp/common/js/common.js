var Sys = new Object();
Sys.ie 		= navigator.userAgent.indexOf("MSIE") > 0 ? true : false;
Sys.firefox = navigator.userAgent.indexOf("Firefox") > 0 ? true : false;
Sys.chrome 	= navigator.userAgent.indexOf("Chrome") > 0 ? true : false;
Sys.opera 	= navigator.userAgent.indexOf("Opera") > 0 ? true : false;
Sys.safari 	= navigator.userAgent.indexOf("Safari") > 0 ? true : false;

String.prototype.trim = function() { return this.replace(/(^\s*)|(\s*$)/g,""); }
String.prototype.ltrim = function() { return this.replace(/(^\s*)/g, ""); }
String.prototype.rtrim = function() { return this.replace(/(\s*$)/g, ""); }

function StringBuffer() {
  this._strings_ = new Array();
}
StringBuffer.prototype.append = function(str) {
  this._strings_.push(str);
  return this;
}
StringBuffer.prototype.toString = function() {
  return this._strings_.join("");
}

function stringToJson(str) {
    return eval('(' + str + ')');
}
function jsonToString(obj) {
    return JSON.stringify(obj);
}

function getURLParams() {
	
    var args = new Object();
    var query = location.search.substring(1);
    var pairs = query.split("&"); 
    
    for (var i=0; i<pairs.length; i++) {
        var pos = pairs[i].indexOf('=');
        if (pos == -1) { 
        	continue;
        }
        var argname = pairs[i].substring(0, pos); 
        var value = pairs[i].substring(pos + 1);
        value = decodeURIComponent(value);
        args[argname] = value;
    }
    return args;
}

function getURLParam(name) {
    var args = getURLParams();
    return args[name];
}


function appendURLParam(url, name, value) {
	
	var stringBuffer = new StringBuffer();
	
	stringBuffer.append(url);
	
	if (url.indexOf('?') == -1) {
		stringBuffer.append('?')
	} else {
		stringBuffer.append('&')
	}
	
	stringBuffer.append(name)
				.append('=')
				.append(encodeURIComponent(value));
	
	return stringBuffer.toString();
}

function getURLParamObject(urlParam) {
	
	if (urlParam == null) {
		urlParam = 'urlParam';
	}
	
	var urlParam = getURLParam(urlParam);
	if (urlParam != null) {				
		var urlParamObject = stringToJson(urlParam);
		return urlParamObject;
	}
	
	return null;
}

function JsonSet() {
	
	this.entry = new Object();

	this.put = function (key , value) {
		this.entry[key] = value;
	}
 
	this.get = function (key) {
		return key in this.entry ? this.entry[key] : null;
	}
	
	this.remove = function (key) {
		delete this.entry[key];
	}
	
	this.containsKey = function (key) {
		return (key in this.entry);
	}
 
	this.getKeys = function () {
		var keys = new Array();
		for(var key in this.entry) {
			keys.push(key);
		}
		return keys;
	}
	
	this.clean = function () {
		for(var key in this.entry) {
			delete this.entry[key];
		}
	}
	
	this.toString = function () {
		var allArray = new Array();
		for(var key in this.entry) {
			var singleArray = new Array();
			singleArray.push(key);
			singleArray.push('=');
			if (this.entry[key] instanceof Object) {
				singleArray.push(encodeURIComponent(jsonToString(this.entry[key])));
			} else {
				singleArray.push(this.entry[key]);
			}
			allArray.push(singleArray.join(''));
		}
		return allArray.join('&');
	}
}

function Map() {
	
	this.entry = new Object();
	
	this.put = function (key , value) {
		this.entry[key] = value;
	}
 
	this.get = function (key) {
		return key in this.entry ? this.entry[key] : null;
	}
	
	this.remove = function (key) {
		delete this.entry[key];
	}
	
	this.containsKey = function (key) {
		return (key in this.entry);
	}
 
	this.getKeys = function () {
		var keys = new Array();
		for(var key in this.entry) {
			keys.push(key);
		}
		return keys;
	}
	
	this.clean = function () {
		for(var key in this.entry) {
			delete this.entry[key];
		}
	}
	
	this.getEntry = function () {
		return this.entry;
	}
	
	this.setEntry = function (entry) {
		this.entry = entry;
	}
}

function transaction(obj) {
	
	/*
	 * obj.id
	 * obj.url
	 * obj.jsonSet
	 * obj.param
	 * obj.type
	 * obj.cache
	 * obj.async
	 * obj.other
	 * obj.timeout
	*/
	
	if (obj.param != undefined) {
		obj.url = appendURLParam(obj.url, obj.param);
	}
	
	$.ajax({
		
		url: obj.url,
		data: obj.jsonSet == undefined ? '' : obj.jsonSet.toString(),
		type: obj.type == undefined ? 'POST' : obj.type,
		cache: obj.cache == undefined ? false : obj.cache,
		async: obj.async == undefined ? true : obj.async,
		dataType: 'json',
		contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		timeout: obj.timeout == undefined ? 20000 : obj.timeout,
			
		success: function(data, textStatus, jqXHR) {
			if (obj.callback == undefined) {
				callback(obj.id, data, obj.other);
			} else {
				obj.callback(data, obj.other);
			}
		},

		complete: function(jqXHR, textStatus) {
		},
		
		error: function(jqXHR, textStatus, errorThrown) {
			if (textStatus == 'parsererror') {
				alert('您没有访问权限, 请联系管理员');
				return;
			} else {
				alert('ajax error info: ' + textStatus);
			}
			var data = new Object();
			data['result'] = 'false';
			if (obj.callback == undefined) {
				callback(obj.id, data, obj.other);
			} else {
				obj.callback(data, obj.other);
			}
		}
	});
}

function onEnterDown(fun) {
	
	if (event.keyCode == 13) {
		if (fun == null) {
			return;
		}
		fun();
	}
}

function openChildWindowPost(url, paramname, param, height, width, scrollbars) {
	
	if (Sys.chrome) {
		
		height = height != null && height != '' ? height : window.screen.height - 95;
		width = width != null && width != '' ? width : window.screen.width - 20;
		
	} else {
		
		height = height != null && height != '' ? height : window.screen.height;
		width = width != null && width != '' ? width : window.screen.width;
	}

	scrollbars = scrollbars != null && scrollbars != '' && scrollbars == true ? 'yes' : 'no';
	
	var winpos = "left=0,top=0"; 
	var winstyle="width="
		+ width 
		+ ",height=" 
		+ height 
		+ ",status=no,toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=" 
		+ scrollbars 
		+ ",copyhistory=no,titlebar=no," 
		+ winpos;
	
	var name = (new Date()).getTime();
    
	var tempForm = document.createElement("form");    
    tempForm.id="tempForm1";    
    tempForm.method="post";    
    tempForm.action=url;    
    tempForm.target=name;    

    var hideInput = document.createElement("input");    
    hideInput.type="hidden";    
    hideInput.name= paramname;
    hideInput.value= param;  
    tempForm.appendChild(hideInput);     
    
    if (window.ActiveXObject) {
    	
    	tempForm.attachEvent("onsubmit", function(){ 
        	var new_window = window.open('about:blank', name, winstyle);
            new_window.focus();
        }); 
    	
    } else {
    	
    	tempForm.addEventListener("onsubmit", function(){ 
        	var new_window = window.open('about:blank', name, winstyle);
            new_window.focus();
        }, false); 
    }
    
 
    document.body.appendChild(tempForm);    

    if (window.ActiveXObject) {
    	
    	tempForm.fireEvent("onsubmit");  
    	
    } else {
    	
    	var evt = document.createEvent('HTMLEvents');   
    	evt.initEvent('onsubmit', true, true);   
    	tempForm.dispatchEvent(evt);   
    }
     
    tempForm.submit();  
    document.body.removeChild(tempForm);  
}

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "H+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
		    if (new RegExp("(" + k + ")").test(fmt))
		    	fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function repaintGrid(defaultHeight, expander, list, queryDiv, expanderChangeSize ) {
	var listHeight = $("#" + list).height();
	var queryHeight = queryDiv != null ? $("#" + queryDiv).height() : 27;
	var expanderHeight = expanderChangeSize != null ? $("#" + expander).height() + expanderChangeSize : $("#" + expander).height();
	var maxHeight = $(parent.$("iframe")[parent.$("iframe").size()-1]).height() - queryHeight - 98 - expanderHeight;
	
	var rowNum = $("#" + list).jqGrid('getGridParam', 'rowNum');
	var records = $("#" + list).jqGrid('getGridParam', 'records');
	var page = $("#" + list).jqGrid('getGridParam', 'page');
	
	var actual = rowNum * page > records ? records - rowNum * (page - 1) : rowNum
	
	var beyondRowNum = actual > 10 ? actual - 10 : 0;
	
	var expectHeight = defaultHeight + beyondRowNum * 23;
	var actualHeight = expectHeight;
	
	if (expectHeight > maxHeight) {
		actualHeight = defaultHeight + Math.floor((maxHeight - defaultHeight) / 23) * 23;
	}
	
	$("#" + list + "_div .ui-jqgrid-bdiv").height(actualHeight);
}