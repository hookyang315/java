package cn.sdframework;

import cn.sdframework.support.DataPage;

public interface IPageService<T> extends ICrudService<T> {
	public DataPage<T> queryForPage(String whereStr,String orderStr,int pageIndex,int pageSize) throws Exception;
}
