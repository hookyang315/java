package cn.sdframework.support.dict;

public enum OperateType {
	
	TOGETHER("TOGETHER"),
	
	DELETE("DELETE"),
	
	QUERY("QUERY"),
	
	NONE("NONE");
	
	private OperateType(String type){
		this.type = type;
	}
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
