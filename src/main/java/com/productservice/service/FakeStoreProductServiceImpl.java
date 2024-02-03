package com.productservice.service;

import com.productservice.dto.FakeStoreProductDto;
import com.productservice.model.Category;
import com.productservice.model.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FakeStoreProductServiceImpl implements ProductService{

    private RestTemplateBuilder restTemplateBuilder;

    FakeStoreProductServiceImpl(RestTemplateBuilder restTemplateBuilder){
        this.restTemplateBuilder = restTemplateBuilder;
    }

    private Product convertFakeStoreProductDtoIntoProduct(FakeStoreProductDto fakeStoreProductDto){
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setTitle(fakeStoreProductDto.getTitle());
        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setImageUrl(fakeStoreProductDto.getImage());
        return product;
    }


    private <T> ResponseEntity<T> requestForEntity(HttpMethod httpMethod, String url,
                                                   @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.requestFactory(
                HttpComponentsClientHttpRequestFactory.class
        ).build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }
    @Override
    public List<Product> getAllProduct() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto[]> response = restTemplate.getForEntity(
                "https://fakestoreapi.com/products",
                //List.class
                /*Resttemplate is giving back list but list of what ? we are not defining the
                object type of list elements. by default we have linkedHashMap as object
                We can either convert that hashmap into productdto object but that is not compile
                time safe, soo thats why we are using array*/
                FakeStoreProductDto[].class
        );
        List<Product> allProductList = new ArrayList<>();

        for(FakeStoreProductDto productDto : response.getBody()){
            Product product = new Product();
            product = convertFakeStoreProductDtoIntoProduct(productDto);
            allProductList.add(product);
        }
        return allProductList;
    }

    @Override
    public Product getSingleProduct(Long productId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> response = restTemplate.getForEntity(
                "https://fakestoreapi.com/products/{id}", FakeStoreProductDto.class, productId);
        //syntax above = (url, return type class, params in url)
        FakeStoreProductDto fakeStoreProductDto = response.getBody();
        Product product = (convertFakeStoreProductDtoIntoProduct(fakeStoreProductDto));
        return Optional.of(product);
    }

    @Override
    public Product addProduct(Product product) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> response = restTemplate.postForEntity(
                "https://fakestoreapi.com/products",
                product,
                FakeStoreProductDto.class
        );
        FakeStoreProductDto productDto = response.getBody();
        return convertFakeStoreProductDtoIntoProduct(productDto);
    } 

    @Override
    public Product updateProduct(Long productId, Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setImage(product.getImageUrl());
        fakeStoreProductDto.setTitle(product.getTitle());
        fakeStoreProductDto.setCategory(product.getCategory().getName());

        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = requestForEntity(
                HttpMethod.PATCH,
                "https://fakestoreapi.com/products/{id}",
                fakeStoreProductDto,
                FakeStoreProductDto.class,
                productId
        );

        return convertFakeStoreProductDtoIntoProduct(fakeStoreProductDtoResponseEntity.getBody());
    }

    @Override
    public Product replaceProduct(Long productId, Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setImage(product.getImageUrl());
        fakeStoreProductDto.setTitle(product.getTitle());
        fakeStoreProductDto.setCategory(product.getCategory().getName());

        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = requestForEntity(
                HttpMethod.PUT,
                "https://fakestoreapi.com/products/{id}",
                fakeStoreProductDto,
                FakeStoreProductDto.class,
                productId
        );

        return convertFakeStoreProductDtoIntoProduct(fakeStoreProductDtoResponseEntity.getBody());
    }

    @Override
    public boolean deleteProduct(Long productId) {
        return false;
    }
}