package com.elkabani.mydb.services;

import com.elkabani.mydb.entities.Category;
import com.elkabani.mydb.entities.Product;
import com.elkabani.mydb.entities.User;
import com.elkabani.mydb.repositories.CategoryRepository;
import com.elkabani.mydb.repositories.ProductRepository;
import com.elkabani.mydb.repositories.ProfileRepository;
import com.elkabani.mydb.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ProductService {
    private final EntityManager entityManager;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void makeNewProductAndCategory()
    {
        var category = Category.builder()
                .name("Electronics")
                .build();

        category = categoryRepository.save(category);

        var product = Product.builder()
                .name("Camera")
                .price(new BigDecimal(129.99))
                .category(category)
                .build();

        productRepository.save(product);
    }

    @Transactional
    public void makeNewProduct()
    {
        var category = categoryRepository.findById(1L)
                .orElseThrow();

        var product = Product.builder()
                .name("Radio")
                .price(new BigDecimal(209.99))
                .category(category)
                .build();

        productRepository.save(product);

    }

    public void removeProduct() {

        Product product;
        product = productRepository.findById(5L)
                .orElseThrow();

        productRepository.delete(product);
    }
}

