package com.st.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.st.domain.Emp;

public interface EmpRepo extends JpaRepository<Emp, Integer> {
	

}
