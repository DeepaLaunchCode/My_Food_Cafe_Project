package com.myfoodcafe.controller;

import com.myfoodcafe.dto.EnquiryRequest;
import com.myfoodcafe.dto.GenericResponse;
import com.myfoodcafe.entity.Enquiry;
import com.myfoodcafe.service.EnquiryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enquiries")
@Tag(name = "Enquiries", description = "Endpoints for handling contact form submissions")
public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;

    @PostMapping
    public ResponseEntity<GenericResponse> handleEnquiry(@Valid @RequestBody EnquiryRequest request) {
        Enquiry newEnquiry = enquiryService.submitEnquiry(request);
        return ResponseEntity.ok(
                new GenericResponse(true, "Enquiry submitted successfully!", newEnquiry.getId())
        );
    }
}