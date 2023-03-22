package com.hacatac.ProductService.service.impl;

import com.hacatac.ProductService.entity.ProductEntity;
import com.hacatac.ProductService.exception.ProductServiceCustomException;
import com.hacatac.ProductService.model.request.ProductRequest;
import com.hacatac.ProductService.model.response.ProductResponse;
import com.hacatac.ProductService.repository.ProductRepository;
import com.hacatac.ProductService.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.*;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding product: " + productRequest.getProductName());
        ProductEntity productEntity
                = ProductEntity.builder()
                .productName(productRequest.getProductName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();
        productRepository.save(productEntity);

        log.info("Product added: " + productEntity.getProductId());
        return productEntity.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Getting product by id: " + productId);
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("PRODUCT_NOT_FOUND", "Product not found"));

        ProductResponse productResponse
                = new ProductResponse();

        copyProperties(productEntity, productResponse);

        log.info("Product found: " + productResponse.getProductName());
        return productResponse;

    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reducing quantity {} for product id: {}", quantity, productId);

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("PRODUCT_NOT_FOUND", "Product not found"));

        if(productEntity.getQuantity() < quantity){
            throw new ProductServiceCustomException("PRODUCT_QUANTITY_NOT_AVAILABLE", "Product does not have sufficient quantity");
        }

        productEntity.setQuantity(productEntity.getQuantity() - quantity);
        productRepository.save(productEntity);
        log.info("Quantity reduced successfully");
    }
}
