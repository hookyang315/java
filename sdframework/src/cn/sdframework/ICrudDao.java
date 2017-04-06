package cn.sdframework;

import java.util.List;

public interface ICrudDao<T> {
	 void add(T t) throws Exception;
	 void modify(T t) throws Exception;
	 void delete(Object id) throws Exception;
	 T findOne(Object id) throws Exception;
	 T findOneWith(String whereString)throws Exception;
	 List<T> findList(String whereString) throws Exception;
}
