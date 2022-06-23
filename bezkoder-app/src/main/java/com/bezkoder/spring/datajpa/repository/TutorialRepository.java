package com.bezkoder.spring.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.datajpa.model.Newspaper;

public interface NewpaperRepository extends JpaRepository<Newspaper, Long> {
	List<Newspaper> findByPublished(boolean published);
	List<Newspaper> findByTitleContaining(String title);
}
