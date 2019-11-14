package com.example.demo.reponse;

/**
 * 统一数据响应接口标准 统一所有开发人员响应前端请求的返回结果格式。
 */
public class AjaxResponse {
    private boolean isok;//请求是否处理成功
    private int code;//请求响应状态码
    private String  message;//请求结果描述信息
    private Object data;//请求结果数据

    private AjaxResponse(){

    }

    public static AjaxResponse success(){
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(true);
        resultBean.setCode(200);
        resultBean.setMessage("success");
        return resultBean;
    }

    public static AjaxResponse success(Object data){
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(true);
        resultBean.setCode(200);
        resultBean.setMessage("success");
        resultBean.setData(data);
        return resultBean;
    }

    public boolean isIsok() {
        return isok;
    }

    public void setIsok(boolean isok) {
        this.isok = isok;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
