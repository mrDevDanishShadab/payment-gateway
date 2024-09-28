package com.payment.gateway.controller;

import com.payment.gateway.entity.Product;
import com.payment.gateway.repository.ProductRepository;
import com.payment.gateway.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentService paymentService;

    //http://localhost:8080/payments/initiate?productId=1
    @PostMapping("/initiate")
    public ResponseEntity<String> initiatePayment(@RequestParam Long productId){
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product not found with id:" + productId)
        );

        String paymentIntentId = paymentService.createPaymentIntent(product.getPrice());

        return ResponseEntity.ok(paymentIntentId);
    }

}
