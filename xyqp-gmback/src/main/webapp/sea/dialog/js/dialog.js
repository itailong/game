/**
 * 对话框模块
 */
define(function(require, exports, module){
	require("../css/dialog.css");
	
	var waitShade = null;
	
	MessageBox = function(options) {
		var defaults = {
			title: "提示",
			message: "",
			ok: function(){},
			cancle: null
		};
		this.settings = $.extend({}, defaults, options);
		this.container = null;
		this.shade = null;
		this.init();
	};

	MessageBox.prototype.init = function(){
		this.createContainer();
		this.update();
		this.addEvent();
	};
	
	MessageBox.prototype.createContainer = function() {
		var html = '<div class="dialog-message-box-container">'
			+ '<div class="dialog-message-box-head">'
			+ '<div class="dialog-message-box-title"></div>'
			+ '<button class="dialog-close"></button>'
			+ '</div>'
			+ '<div class="dialog-message-box-line"></div>'
			+ '<div class="dialog-message-box-content">'
			+ '<div class="dialog-message-box-information"></div>'
			+ '</div>'
			+ '<div class="dialog-message-box-btn-bar">'
			+ '<button class="dialog-message-box-btn">取消</button>'
			+ '<button class="dialog-message-box-btn">确定</button>'
			+ '</div>'
			+ '</div>';
		this.container = $(html);
		this.container.hide();
		this.container.appendTo("body");
		
		this.shade = $("<div class='dialog-shade' ></div>");
		this.shade.hide();
		this.shade.appendTo("body");
	};
	
	MessageBox.prototype.update = function() {
		this.container.find(".dialog-message-box-title").html(this.settings.title);
		this.container.find(".dialog-message-box-information").html(this.settings.message);
	};
	
	MessageBox.prototype.addEvent = function() {
		var thi = this;
		var closeBtn = this.container.find(".dialog-close");
		var btns = this.container.find(".dialog-message-box-btn");
		var okBtn = btns.eq(1);
		var cancleBtn = btns.eq(0);
		closeBtn.click(function(){
			if (thi.settings.cancle) {
				thi.settings.cancle();
			}
			thi.close();
		});
		if (this.settings.ok) {
			okBtn.click(function(){
				var flag = thi.settings.ok();
				if (flag != false) {
					thi.close();
				}
			});
		} else {
			okBtn.hide();
		}
		if (this.settings.cancle) {
			cancleBtn.click(function(){
				var flag = thi.settings.cancle();
				if (flag != false) {
					thi.close();
				}
			});
		} else {
			cancleBtn.hide();
		}
	};
	
	MessageBox.prototype.show = function() {
		this.shade.show();
		this.container.show();
	};
	
	MessageBox.prototype.hide = function() {
		this.container.hide();
	}
	
	MessageBox.prototype.close = function() {
		this.container.detach();
		this.shade.detach();
	}
	
	function WaitShade() {
		this.shade = null;
		this.content = null;
		this.init();
	}
	
	WaitShade.prototype.init = function() {
		this.createShade();
		this.createContent();
	};
	
	WaitShade.prototype.createShade = function() {
		this.shade = $("<div class='dialog-shade' ></div>");
		this.shade.hide();
		this.shade.appendTo("body");
	};
	
	WaitShade.prototype.createContent = function() {
		var html = '<div class="dialog-wait-content">'
			+ '<img class="dialog-wait-img" src="'
			+ seajs.data.base
			+ 'dialog/img/loading.gif"/>'
			+ ' <div class="dialog-wait-message">'
			+ '</div>'
			+ ' </div>';
		this.content = $(html);
		this.content.hide();
		this.content.appendTo("body");
	};
	
	WaitShade.prototype.show = function(message) {
		this.content.find(".dialog-wait-message").html(message);
		this.shade.show();
		this.content.show();
	}
	
	WaitShade.prototype.hide = function() {
		this.shade.hide();
		this.content.hide();
	}
	
	function Dialog(options) {
		var defaults = {
			title: "提示",
			content: "",
			width: 200,
			height: 200,
			onClose: function(){
			}
		};
		this.settings = $.extend({}, defaults, options);
		this.shade = null;
		this.container = null;
		this.mask = null;
		this.init();
	}
	
	Dialog.prototype.init = function() {
		this.createShade();
		this.createContainer();
		this.createMask();
		this.addEvent();
	};
	
	Dialog.prototype.createShade = function() {
		var html = '<div class="dialog-shade"></div>';
		this.shade = $(html);
		this.shade.hide();
		this.shade.appendTo("body");
	};
	
	Dialog.prototype.createContainer = function() {
		var html = '<div class="dialog-container">'
			+ '<div class="dialog-head">'
			+ '<div class="dialog-title"></div>'
			+ '<button class="dialog-close"></button>'
			+ '</div>'
			+ '<div class="dialog-content">'
			+ '</div>'
			+ '</div>';
		this.container = $(html);
		this.container.find(".dialog-title").html(this.settings.title);
		var content = $(this.settings.content);
		this.container.find(".dialog-content").append(content);
		this.container.css("width", this.settings.width);
		this.container.css("height", this.settings.height);
		var left = ($(window).width() - this.settings.width) / 2;
		var top = ($(window).height() - this.settings.height) / 2;
		this.container.css("left", left);
		this.container.css("top", top);
		this.container.hide();
		this.container.appendTo("body");
	};
	
	Dialog.prototype.createMask = function() {
		var html = '<div class="dialog-mask"></div>';
		this.mask = $(html);
		this.mask.hide();
		this.mask.appendTo("body");
	};
	
	Dialog.prototype.addEvent = function() {
		var thi = this;
		this.container.find(".dialog-close").mousedown(function(){
			thi.hide();
			thi.settings.onClose();
			return false;
		});
		this.container.find(".dialog-head").mousedown(function(e){
			thi.mask.show();
			var matrix = {};
			matrix.clickX = e.clientX;
			matrix.clickY = e.clientY;
			var offset = thi.container.offset();
			matrix.originalX = offset.left;
			matrix.originalY = offset.top;
			thi.mask.data("matrix", matrix);
		});
		this.mask.mousemove(function(e){
			var matrix = thi.mask.data("matrix");
			var offsetX = e.clientX - matrix.clickX;
			var offsetY = e.clientY - matrix.clickY;
			var left = matrix.originalX + offsetX;
			var top = matrix.originalY + offsetY;
			thi.container.css("left", left);
			thi.container.css("top", top);
		});
		this.mask.mouseup(function(e){
			thi.mask.hide();
		});
	};
	
	Dialog.prototype.resetLocation = function() {
		var left = ($(window).width() - this.settings.width) / 2;
		var top = ($(window).height() - this.settings.height) / 2;
		this.container.css("left", left);
		this.container.css("top", top);
	};
	
	Dialog.prototype.show = function() {
		this.container.show();
		this.shade.show();
	};
	
	Dialog.prototype.hide = function() {
		this.container.hide();
		this.shade.hide();
		this.mask.hide();
	};
	
	
	exports.showMessage = function(message, ok) {
		var dlg = new MessageBox({
			message: message,
			ok: ok
		});
		dlg.show();
		return dlg;
	};
	
	exports.showConfirm = function(message, ok) {
		var dlg = new MessageBox({
			message: message,
			ok: ok,
			cancle: function(){}
		});
		dlg.show();
		return dlg;
	};
	
	exports.createMessageBox = function(options) {
		return new MessageBox(options);
	};
	
	exports.showWait = function() {
		if (!waitShade) {
			waitShade = new WaitShade();
		}
		waitShade.show();
	};
	
	exports.hideWait = function() {
		if (!waitShade) {
			return;
		}
		waitShade.hide();
	};
	
	exports.createDialog = function(options) {
		return new Dialog(options);
	};
});
