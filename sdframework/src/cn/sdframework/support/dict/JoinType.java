package cn.sdframework.support.dict;

public enum JoinType {
	

	INNER("INNER JOIN"),
	LEFT("LEFT JOIN"),
	RIGHT("RIGHT JOIN"),
	NULL("");
	private JoinType(String type){
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
