
window.app = {
	/**
	 * 后端服务发布的url地址
	 */
	serverUrl:'http://192.168.1.102:8080',
	/**
	 * 图片服务器url
	 */
	imgServerUrl:'',
	
	
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
	
	/**
	 * 封装消息提示框 默认mui不支持自定义和居中 用h5+
	 * @param {Object} msg
	 * @param {Object} type
	 */
	showToast: function(msg,type) {
		plus.nativeUI.toast(msg, {
			icon: "image/" + type +".png",
			verticalAlign: "center"
		})
	},
	
	/**
	 * 保存用户的全局对象
	 * @param {Object} user
	 */
	setUserGlobalInfo: function(user) {
		var userInfoStr = JSON.stringify(user);
		console.log("set");
		console.log(userInfoStr);
		plus.storage.setItem("userInfo", userInfoStr);
	},
	/**
	 * 获取用户的全局对象
	 */
	getUserGlobalInfo: function() {
		var userInfoStr = plus.storage.getItem("userInfo");
		console.log("get");
		console.log(userInfoStr);
//		return userInfoStr;
		return JSON.parse(userInfoStr);

	},
	
	/**
	 * 清除所有的缓存
	 */
	clearUserGlobalInfo: function(){
		plus.storage.clear();
	}
}
