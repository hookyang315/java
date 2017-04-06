package cn.sdframework;

public interface ICrudService<T> {
	
	public void addObject(T t) throws Exception;
	
	public void modifyObject(T t) throws Exception;
	
	public void deleteObject(Object key) throws Exception;
	
	public T queryByKey(Object key) throws Exception;
}
