package com.myfoodcafe.util;

import com.myfoodcafe.entity.MenuItem;
import com.myfoodcafe.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public void run(String... args) throws Exception {
        if (menuItemRepository.count() == 0) {
            loadMenuItems();
        }
    }

    private void loadMenuItems() {
        List<MenuItem> menuItems = Arrays.asList(
                createMenuItem("Burger", "Delicious beef burger with cheese.", 9.99, "Burgers", "burger.jpg"),
                createMenuItem("Pizza", "Classic Margherita pizza.", 12.99, "Pizza", "pizza.jpg"),
                createMenuItem("Pasta", "Spaghetti with tomato sauce.", 10.99, "Pasta", "pasta.jpg"),
                createMenuItem("Salad", "Fresh garden salad.", 7.99, "Salads", "salad.jpg"),
                createMenuItem("Fries", "Crispy french fries.", 3.99, "Sides", "fries.jpg"),
                createMenuItem("Soda", "Refreshing soda.", 1.99, "Drinks", "soda.jpg"),
                createMenuItem("Chicken Wings", "Spicy chicken wings.", 11.99, "Starters", "wings.jpg"),
                createMenuItem("Coffee", "Energetic espresso.", 1.99, "Drinks", "coffee.jpeg")
        );
        menuItemRepository.saveAll(menuItems);
        System.out.println("Loaded " + menuItems.size() + " menu items into the database.");
    }

    private MenuItem createMenuItem(String name, String description, double price, String category, String imageUrl) {
        MenuItem item = new MenuItem();
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setCategory(category);
        item.setImageUrl(imageUrl);
        return item;
    }
}