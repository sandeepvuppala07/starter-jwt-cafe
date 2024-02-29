package com.st.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.st.constants.UserConstants;
import com.st.dao.CategoryDaoImpl;
import com.st.domain.CafeCategory;
import com.st.security.Jwtfilter;
import com.st.utils.CafeUtils;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryDaoImpl categoryDaoImpl;
	
	@Autowired
	private Jwtfilter jwtfilter;

	@Override
	public ResponseEntity<List<CafeCategory>> getCates(String fiter) {
		if(!Strings.isNullOrEmpty(fiter) && fiter.equalsIgnoreCase("true")) {
			
			return new ResponseEntity(categoryDaoImpl.getAllCategories(),HttpStatus.OK);
		}
		return new ResponseEntity<List<CafeCategory>> (new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> addCategory(Map<String, String> addMap) {
		try {
			if(jwtfilter.isAdmin()) {
				if(validateCategoryMap(addMap, false)) {
				return categoryDaoImpl.addCategory(getCatMap(addMap, false));
				}
			}
			else {
				return CafeUtils.getResponseEntity(UserConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
			
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return CafeUtils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private boolean validateCategoryMap(Map<String, String> catMap,boolean validateId) {
		if(catMap.containsKey("name")) {
			if(catMap.containsKey("id") && validateId) {
				return true;
			}
			else if (!validateId) {
				return true;
			}
		}
	    return false;
	}
	
	private CafeCategory getCatMap(Map<String, String> catMap,boolean isIdExists) {
		CafeCategory cafeCategory = new CafeCategory();
		if(isIdExists) {
			
			cafeCategory.setCatid(Integer.parseInt(catMap.get("id")));
		}
		cafeCategory.setCatname(catMap.get("name"));
		return cafeCategory;
	}

}


