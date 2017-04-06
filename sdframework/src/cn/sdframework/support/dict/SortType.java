package cn.sdframework.support.dict;

public enum SortType {
	
	NULL(""),
	ASC("ASC"),
	DESC("DESC");
	
	private SortType(String type){
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
