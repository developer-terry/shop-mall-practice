package com.practice.shopmall.search.controller;

import com.practice.shopmall.search.service.ShopmallSearchService;
import com.practice.shopmall.search.vo.SearchParam;
import com.practice.shopmall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {

    @Autowired
    ShopmallSearchService shopmallSearchService;

    /**
     * 將頁面提交過來的所有請求查詢參數封裝成指定的對象
     * @param searchParam
     * @return
     */
    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam, Model model, HttpServletRequest httpServletRequest) {
        String queryString = httpServletRequest.getQueryString();
        searchParam.set_queryString(queryString);
        SearchResult result = shopmallSearchService.search(searchParam);
        model.addAttribute("result", result);
        return "list";
    }

}
