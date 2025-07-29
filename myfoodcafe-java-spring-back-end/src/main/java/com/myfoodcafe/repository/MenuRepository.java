package com.myfoodcafe.repository;

import com.myfoodcafe.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findByCategory(String category);
}