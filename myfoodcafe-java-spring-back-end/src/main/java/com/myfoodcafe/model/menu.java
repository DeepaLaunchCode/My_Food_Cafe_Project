package com.myfoodcafe.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import lombok.Data;


@Entity
@Data
public class Menu {

        @Id
        @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
        private int id;
        private String name;
        private String description;
        private double price;
        private  String image;
        private  String category;


}
