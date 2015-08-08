package org.fl.noodle.common.mvc.vo;

public class ResultVo {
	
	private String result;
	private String errorMessage;
	private String param;
	
	public ResultVo() {
	}
	
	public ResultVo(String result) {
		this.result = result;
	}
	
	public ResultVo(String result, String errorMessage) {
		this.result = result;
		this.errorMessage = errorMessage;
	}
	
	public ResultVo(String result, String errorMessage, String param) {
		this.result = result;
		this.errorMessage = errorMessage;
		this.param = param;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
}
