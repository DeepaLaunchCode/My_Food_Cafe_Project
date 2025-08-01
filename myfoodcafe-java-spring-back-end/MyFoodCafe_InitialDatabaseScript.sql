-- =================================================================
--  MyFoodCafe Initial Database Script
-- =================================================================
-- This script creates the necessary tables for the application
-- and populates them with one sample record each.

-- Drop tables in reverse order of creation to avoid foreign key constraints
DROP TABLE IF EXISTS `notification`;
DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `reservation`;
DROP TABLE IF EXISTS `menu_item`;
DROP TABLE IF EXISTS `customer`;
DROP TABLE IF EXISTS `enquiry`;
DROP TABLE IF EXISTS `review`;
-- -----------------------------------------------------
-- Table `customer`
-- Stores customer information.
-- -----------------------------------------------------
CREATE TABLE `customer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL UNIQUE,
  `phone` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table `menu_item`
-- Stores all available menu items for the cafe.
-- (Structure inferred from MenuService)
-- -----------------------------------------------------
CREATE TABLE `menu_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `description` TEXT,
  `price` DECIMAL(10,2) NOT NULL,
  `category` VARCHAR(100),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table `reservation`
-- Stores customer table reservations.
-- -----------------------------------------------------
CREATE TABLE `reservation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `customer_id` BIGINT NOT NULL,
  `reservation_date` DATE NOT NULL,
  `reservation_time` TIME NOT NULL,
  `number_of_guests` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_reservation_customer`
    FOREIGN KEY (`customer_id`)
    REFERENCES `customer` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table `orders`
-- Stores customer food orders.
-- -----------------------------------------------------
CREATE TABLE `orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `customer_id` BIGINT NOT NULL,
  `card_number` VARCHAR(255),
  `expiry_date` VARCHAR(255),
  `cvv` VARCHAR(255),
  `total_amount` DECIMAL(10,2) NOT NULL,
  `order_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_order_customer`
    FOREIGN KEY (`customer_id`)
    REFERENCES `customer` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table `order_item`
-- Stores individual items within a customer`s order.
-- -----------------------------------------------------
CREATE TABLE `order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL,
  `item_name` VARCHAR(255) NOT NULL,
  `quantity` INT NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_order_item_order`
    FOREIGN KEY (`order_id`)
    REFERENCES `orders` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table `notification`
-- Logs sent notifications (e.g., SMS).
-- (Structure inferred from NotificationService/Controller)
-- -----------------------------------------------------
CREATE TABLE `notification` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `recipient` VARCHAR(255) NOT NULL,
  `message` TEXT NOT NULL,
  `status` VARCHAR(50),
  `created_at` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table `enquiry`
-- Logs sent notifications (e.g., SMS).
-- (Structure inferred from NotificationService/Controller)
-- -----------------------------------------------------
CREATE TABLE `enquiry` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `category` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `message` TEXT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `submitted_at` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table `review`
-- Logs sent notifications (e.g., SMS).
-- (Structure inferred from NotificationService/Controller)
-- -----------------------------------------------------
CREATE TABLE `review` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME,
  `image` VARCHAR(255) NOT NULL,
  `message` TEXT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `rating` INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;


-- =================================================================
--  Sample Data Insertion
-- =================================================================

-- Insert a sample customer
INSERT INTO `customer` (`name`, `email`, `phone`)
VALUES ('Deepa Ingole', 'sonaliforamit@gmail.com', '9452755520');

-- Insert a sample menu item
INSERT INTO `menu_item` (`name`, `description`, `price`, `category`)
VALUES ('Classic Burger', 'A juicy beef patty with lettuce, tomato, and our special sauce.', 12.99, 'Burgers');

-- Insert a sample reservation for our customer (customer_id = 1)
-- Note: Using CURDATE() + INTERVAL 7 DAY to set a future date.
INSERT INTO `reservation` (`customer_id`, `reservation_date`, `reservation_time`, `number_of_guests`)
VALUES (1, CURDATE() + INTERVAL 7 DAY, '19:00:00', 4);

-- Insert a sample order for our customer (customer_id = 1)
-- Note: Using NOW() to set the current timestamp.
INSERT INTO `orders` (`customer_id`, `card_number`, `expiry_date`, `cvv`, `total_amount`, `order_date`)
VALUES (1, '1234123412341234', '12/26', '123', 25.98, NOW());

-- Insert a sample order item for the order we just created (order_id = 1)
INSERT INTO `order_item` (`order_id`, `item_name`, `quantity`, `price`)
VALUES (1, 'Classic Burger', 2, 12.99);

-- Insert a sample notification log
INSERT INTO `notification` (`recipient`, `message`, `status`, `created_at`)
VALUES ('9452755520', 'Hi Deepa Ingole, your order #1 for $25.98 has been placed. Thank you!', 'SENT', NOW());

-- Insert a sample enquiry log
INSERT INTO `enquiry` (`category`, `email`, `message`, `name`, `submitted_at`)
VALUES ('Franchise Enquiry', 'deepaingole@gmail.com', 'I have question on franchise, please let me know what are cost and initial investment and related details', 'Deepa Ingole', NOW());

-- Insert a sample review log
INSERT INTO `review` (`created_at`,`image`, `message`, `name`, `rating`)
VALUES (NOW(), 'sam.jpg', 'The pasta was absolutely amazing! Best Ive ever had.','Sam', 5);

-- --- End of Script ---