package com.elkabani.mydb.controllers;

import com.elkabani.mydb.dtos.ProductDto;
import com.elkabani.mydb.entities.Product;
import com.elkabani.mydb.mappers.ProductMapper;
import com.elkabani.mydb.repositories.CategoryRepository;
import com.elkabani.mydb.repositories.ProductRepository;
import com.elkabani.mydb.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController

@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public Iterable<ProductDto> getAllProducts(
            @RequestParam(name = "categoryId", required = false) Long categoryId) {

        List<Product> products;

        if (categoryId == null) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findByCategoryId(categoryId);
        }

        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto) {

        var category = categoryRepository.findById(productDto.getCategoryId());

        if (category.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productMapper.toEntity(productDto);
        product.setCategory(category.get());

        Product savedProduct = productRepository.save(product);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productMapper.toDto(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto) {

        var existingProduct = productRepository.findById(id);

        if (existingProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var category = categoryRepository.findById(productDto.getCategoryId());

        if (category.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Product product = existingProduct.get();

        productMapper.update(productDto, product);
        product.setCategory(category.get());

        Product updatedProduct = productRepository.save(product);

        return ResponseEntity.ok(productMapper.toDto(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        var product = productRepository.findById(id);

        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product.get());

        return ResponseEntity.noContent().build();
    }
    }

