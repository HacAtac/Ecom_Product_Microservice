package com.hacatac.ProductService.service;

import com.hacatac.ProductService.model.request.ProductRequest;
import com.hacatac.ProductService.model.response.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);
}
