package com.st.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.st.domain.CafeCategory;
import com.st.repository.CategoryRepo;
@Component
public class CategoryDaoImpl {
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	public List<CafeCategory> getAllCategories(){
		List<CafeCategory> catList=categoryRepo.findAll();
		System.out.println(catList);
		 return catList;
	}
	
	public ResponseEntity<String> addCategory(CafeCategory CafeCategory){
		
		categoryRepo.save(CafeCategory);
		return new ResponseEntity("Category Added",HttpStatus.OK);
	}

}
