package com.newsbhaskar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsbhaskar.model.Editor;

import java.util.List;

public interface EditorRepository extends JpaRepository<Editor, Integer> {
    Editor getBiodataById(Integer id);

    List<Editor> findAllByStatus(String args);
}
