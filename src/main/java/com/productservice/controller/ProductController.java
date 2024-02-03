package com.productservice.controller;

import com.productservice.dto.GetSingleProductResponseDto;
import com.productservice.dto.ProductDto;
import com.productservice.exception.NotFoundException;
import com.productservice.model.Category;
import com.productservice.model.Product;
import com.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/")
    public List<Product> getAllProduct(){

        return productService.getAllProduct();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetSingleProductResponseDto> getSingleProduct(@PathVariable("productId") Long productId) throws NotFoundException {
        GetSingleProductResponseDto getSingleProductResponseDto = new GetSingleProductResponseDto();
        getSingleProductResponseDto.setProduct(productService.getSingleProduct(productId));

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(
                "auth-token","noAccessForYou"
        );

        Optional<Product> productOptional = Optional.ofNullable(productService.getSingleProduct(productId));
        if(productOptional.isEmpty()){
            throw new NotFoundException("No product with product id:"+productId);
        }

        ResponseEntity<GetSingleProductResponseDto> responseDto = new ResponseEntity<>(
                getSingleProductResponseDto,
                headers,
                HttpStatus.OK

        );
        return responseDto;
    }

    @PostMapping("/")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product newProduct = productService.addProduct(product);
        ResponseEntity<Product> response = new ResponseEntity<>(newProduct, HttpStatus.BAD_GATEWAY);
        return response;
    }

    @PatchMapping("/{productId}")
    public Product updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductDto productDto){
        Product product = new Product();
        product.setId(productDto.getId());
        product.setImageUrl(productDto.getImage());
        product.setTitle(productDto.getTitle());
        product.setCategory(new Category());
        product.getCategory().setName(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(product.getDescription());
        return productService.updateProduct(productId, product);
    }
    @PutMapping("/{productId}")
    public Product replaceProduct(@PathVariable("productId") Long productId, @RequestBody ProductDto productDto){
        Product product = new Product();
        product.setId(productDto.getId());
        product.setImageUrl(productDto.getImage());
        product.setTitle(productDto.getTitle());
        product.setCategory(new Category());
        product.getCategory().setName(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(product.getDescription());
        return productService.replaceProduct(productId, product);
    }
    @DeleteMapping()
    public String deleteProduct(@PathVariable("productId") Long productId){
        return "PRODUCT IS DELETED"+productId;
    }
}
