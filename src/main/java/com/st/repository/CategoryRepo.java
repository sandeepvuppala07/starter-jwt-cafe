package com.st.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.st.domain.CafeCategory;

public interface CategoryRepo extends JpaRepository<CafeCategory, Integer> {
	

}
