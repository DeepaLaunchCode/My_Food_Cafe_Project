package com.myfoodcafe.service;

import com.myfoodcafe.entity.Customer;
import com.myfoodcafe.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer findOrCreateCustomer(String name, String email, String phone) {
        Optional<Customer> existingCustomer = customerRepository.findByEmail(email);
        if (existingCustomer.isPresent()) {
            // Update existing customer's name and phone if they differ
            Customer customer = existingCustomer.get();
            if (!customer.getName().equals(name)) {
                customer.setName(name);
            }
            if (!customer.getPhone().equals(phone)) {
                customer.setPhone(phone);
            }
            return customerRepository.save(customer);
        } else {
            Customer newCustomer = new Customer();
            newCustomer.setName(name);
            newCustomer.setEmail(email);
            newCustomer.setPhone(phone);
            return customerRepository.save(newCustomer);
        }
    }

    // New: Method to save a customer
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}