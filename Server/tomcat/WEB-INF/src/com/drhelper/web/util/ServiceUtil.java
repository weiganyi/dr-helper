package com.drhelper.web.util;

import com.drhelper.web.bean.PageInfo;

public class ServiceUtil {
	public static PageInfo makePageInfo(int currPage, int itemNum, int itemPerPage) {
		int pageNum = 1;
		int startPage = 1;
		int endPage = 1;

		if (itemNum != 0 && itemPerPage != 0) {
			pageNum = itemNum/itemPerPage;
			if (itemNum%itemPerPage != 0) {
				pageNum = pageNum + 1;
			}
		}
		
		//the page number can be displayed
		int pageDisplayNum = 9;
		if (currPage > pageDisplayNum/2) {
			startPage = currPage - pageDisplayNum/2;
		}
		
		endPage = startPage + (pageDisplayNum-1);
		if (endPage > pageNum) {
			endPage = pageNum;
		}

		PageInfo pgInfo = new PageInfo();
		
		//init the page variable
		pgInfo.setPageNum(pageNum);
		pgInfo.setStartPage(startPage);
		pgInfo.setEndPage(endPage);
		
		return pgInfo;
	}
}
