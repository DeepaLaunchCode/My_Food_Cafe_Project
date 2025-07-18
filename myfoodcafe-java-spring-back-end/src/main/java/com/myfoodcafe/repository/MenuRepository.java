package com.myfoodcafe.repository;
import com.myfoodcafe.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
