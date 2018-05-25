package com.e3mall.search.service;

import com.e3mall.common.pojo.SearchResult;

public interface SearchService {
	SearchResult query(String key, Integer page, Integer rows) throws Exception;
}
