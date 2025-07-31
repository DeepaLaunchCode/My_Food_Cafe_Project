package com.myfoodcafe.service;

import com.myfoodcafe.dto.OrderRequest;
import com.myfoodcafe.entity.Customer;
import com.myfoodcafe.entity.Order;
import com.myfoodcafe.entity.OrderItem;
import com.myfoodcafe.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService; // Inject CustomerService

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        // 1. Find or create the customer
        Customer customer = customerService.findOrCreateCustomer(
                orderRequest.getCustomerName(),
                orderRequest.getCustomerEmail(),
                orderRequest.getCustomerPhone()
        );

        // 2. Create the order and link it to the customer
        Order order = new Order();
        order.setCustomer(customer); // Link the customer object

        // Set remaining order details
        order.setCardNumber(orderRequest.getCardNumber());
        order.setExpiryDate(orderRequest.getExpiryDate());
        order.setCvv(orderRequest.getCvv());
        order.setTotalAmount(orderRequest.getTotalAmount());
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = orderRequest.getItems().stream().map(itemDto -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemName(itemDto.getItemName());
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(itemDto.getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Send SMS Notification
        try{
            String smsMessage = String.format("Hi %s, your order #%d for $%.2f has been placed. Thank you!",
                    customer.getName(), savedOrder.getId(), savedOrder.getTotalAmount());

        notificationService.sendSms(customer.getPhone(), smsMessage);
        }
        catch (Exception e) {
            System.err.println("Error sending SMS: " + e.getMessage());
        }

        // --- IMPROVED EMAIL NOTIFICATION ---
        // Send Email Notification to the actual customer with order details
       try{
           String emailSubject = String.format("Your MyFoodCafe Order Confirmation (#%d)", savedOrder.getId());

        String emailBody = String.format(
                "Hello %s,\n\nThank you for your order! We've received it and are getting it ready for you.\n\n" +
                        "Order ID: %d\n" +
                        "Total Amount: $%.2f\n\n" +
                        "We appreciate your business!\n\n" +
                        "Sincerely,\nThe MyFoodCafe Team",
                customer.getName(),
                savedOrder.getId(),
                savedOrder.getTotalAmount()
        );

        // Use the customer's email from the customer object
        emailService.sendSimpleEmail(customer.getEmail(), emailSubject, emailBody);
       }
       catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
        return savedOrder;
    }
}