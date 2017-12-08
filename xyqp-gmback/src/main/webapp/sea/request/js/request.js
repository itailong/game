define(function(require, exports, module){
	var dialog = require("../../dialog/js/dialog");
	
	exports.base = seajs.data.base.substring(0, seajs.data.base.length - 5);
	
	exports.ajax = function(options) {
		var settings = {
			waitMessage: null,
			showError: true
		};
		$.extend(settings, options);
		if (settings.waitMessage) {
			dialog.showWait(settings.waitMessage);
		}
		settings.success = function(data) {
			dialog.hideWait();
			if (!data) {
				return;
			}
			if (data.code == 200) {
				if (options.success) {
					options.success(data.data);
				}
				return;
			}
			if (data.code == 100) {
				dialog.showConfirm("登录超时，是否进入登录页面！", function(){
					top.location.href = exports.base + "/system/login"
				});
				return;
			}
			if (options.error) {
				options.error(data);
			}
			if (settings.showError) {
				var msg = data.msg;
				if (!msg) {
					msg = "未知错误！";
				}
				dialog.showMessage(msg);
			}
		};
		settings.error = function(e) {
			if (options.error) {
				options.error(e);
			}
			if (settings.showError) {
				dialog.showMessage("未知错误！");
			}
		}
		$.ajax(settings);
	};
	
	exports.submit = function(options) {
		var settings = {
			url: "",
			method: "post",
			data: {}
		};
		$.extend(settings, options);
		var form = $("<form></from>");
		form.attr("action", settings.url);
		form.attr("method", settings.method);
		for(var name in settings.data) {
			var value = settings.data[name];
			var input = $("<input type='hidden' />");
			input.attr("name", name);
			input.val(value);
			form.append(input);
		}
		$("body").append(form);
		form.submit();
	};
	
});