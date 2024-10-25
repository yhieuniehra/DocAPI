package com.example.DocAPI.controller;

import com.example.DocAPI.model.Product;
import com.example.DocAPI.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // Lấy danh sách tất cả sản phẩm
    @Operation(summary = "Nhận tất cả sản phẩm", description = "Lấy danh sách tất cả sản phẩm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy thành công danh sách sản phẩm")
    })
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Lấy thông tin sản phẩm theo ID
    @Operation(summary = "Nhận sản phẩm theo ID", description = "Lấy thông tin sản phẩm theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy thành công sản phẩm"),
            @ApiResponse(responseCode = "404", description = "Sản phẩm không tồn tại")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok().body(product))
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo mới sản phẩm
    @Operation(summary = "Tạo mới một sản phẩm", description = "Tạo mới một sản phẩm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sản phẩm đã được tạo thành công")
    })
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // Cập nhật sản phẩm
    @Operation(summary = "Cập nhật sản phẩm", description = "Cập nhật thông tin sản phẩm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sản phẩm đã được cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Sản phẩm không tồn tại")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setPrice(productDetails.getPrice());
                    Product updatedProduct = productRepository.save(product);
                    return ResponseEntity.ok().body(updatedProduct);
                }).orElse(ResponseEntity.notFound().build());
    }

    // Xóa sản phẩm
    @Operation(summary = "Xóa sản phẩm", description = "Xóa sản phẩm theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sản phẩm đã được xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Sản phẩm không tồn tại")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
