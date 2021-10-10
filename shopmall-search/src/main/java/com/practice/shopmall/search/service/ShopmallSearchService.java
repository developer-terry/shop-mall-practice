package com.practice.shopmall.search.service;

import com.practice.shopmall.search.vo.SearchParam;
import com.practice.shopmall.search.vo.SearchResult;

public interface ShopmallSearchService {
    SearchResult search(SearchParam searchParam);
}
