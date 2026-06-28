package org.example.springeshopapi.service;

import lombok.RequiredArgsConstructor;
import org.example.springeshopapi.repository.ProductRepository;
import org.example.springeshopapi.dto.ProductDTO;
import org.example.springeshopapi.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;


    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());

        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
    }

    private ProductDTO mapToDto(Product savedProduct) {
        ProductDTO dto = new ProductDTO();
        dto.setId(savedProduct.getId());
        dto.setDescription(savedProduct.getDescription());
        dto.setTitle(savedProduct.getTitle());
        dto.setPrice(savedProduct.getPrice());
        dto.setStock(savedProduct.getStock());
        dto.setImageUrl(savedProduct.getImageUrl());

        return dto;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product prod = productRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Product not found with this id: "+id));

        return mapToDto(prod);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());

        Product updatedProduct = productRepository.save(product);
        return mapToDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);

    }
}
