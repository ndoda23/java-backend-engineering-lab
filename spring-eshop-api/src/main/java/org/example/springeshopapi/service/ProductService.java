package org.example.springeshopapi.service;

import org.example.springeshopapi.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDTO updateProduct(Long id,ProductDTO productDTO);
    void deleteProduct(Long id);
}
