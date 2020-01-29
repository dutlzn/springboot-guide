
window.app = {
	/**
	 * 后端服务发布的url地址
	 */
	serverUrl:'http://192.168.1.102:8080',
	/**
	 * 判断字符串是否为空
	 * @param {Object} str
	 * true: 不为空
	 * false: 为空
	 */
	isNotNull: function(str) {
		if(str!=null && str!="" && str!=undefined){
			return true;
		}
		return false;
	},
	
	// 输入对话框
	showToast: function(msg,type) {
		plus.nativeUI.toast(msg, {
			icon: "image/" + type +".png",
			verticalAlign: "center"
		})
	}
}
