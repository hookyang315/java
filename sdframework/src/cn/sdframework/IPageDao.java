package cn.sdframework;

import cn.sdframework.support.DataPage;

public interface IPageDao<T> extends ICrudDao<T> {

	DataPage<T> queryForPage(String whereStr, String orderStr, int currentPage, int pageSize) throws Exception;
}
