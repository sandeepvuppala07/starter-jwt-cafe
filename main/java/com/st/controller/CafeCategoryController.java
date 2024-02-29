package com.st.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.st.constants.UserConstants;
import com.st.domain.CafeCategory;
import com.st.service.CategoryService;
import com.st.utils.CafeUtils;

@RestController
@RequestMapping("/cate")
public class CafeCategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/getcates")
	public ResponseEntity<List<CafeCategory>> getCates(@RequestBody(required = false) String filter){
		try {
			return categoryService.getCates(filter);
		}
		catch(Exception exp) {
			exp.printStackTrace();
		}
		return new ResponseEntity<List<CafeCategory>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/add")
	public ResponseEntity<String> addCategory(@RequestBody Map<String, String> addMap){
		try {
			return categoryService.addCategory(addMap);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return CafeUtils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
