package com.st.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.st.domain.CafeCategory;

public interface CategoryService {

	public ResponseEntity<List<CafeCategory>> getCates(String filter);

	public ResponseEntity<String> addCategory(Map<String, String> addMap);
}
