package com.CSO2.product_catalogue_service.controller;

import com.CSO2.product_catalogue_service.dto.request.ProductCreateRequest;
import com.CSO2.product_catalogue_service.dto.response.ProductDetailDTO;
import com.CSO2.product_catalogue_service.dto.response.ProductListDTO;
import com.CSO2.product_catalogue_service.model.Product;
import com.CSO2.product_catalogue_service.service.ProductService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new Jackson3HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void getProducts_ShouldReturnPageOfProducts() throws Exception {
        ProductListDTO productDTO = new ProductListDTO();
        productDTO.setId("1");
        productDTO.setName("Test Product");

        List<ProductListDTO> content = List.of(productDTO);
        Page<ProductListDTO> page = new PageImpl<>(content, PageRequest.of(0, 10), content.size());

        when(productService.getProducts(any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].name").value("Test Product"));
    }

    @Test
    void searchProducts_ShouldReturnListOfProducts() throws Exception {
        ProductListDTO productDTO = new ProductListDTO();
        productDTO.setId("1");
        productDTO.setName("Test Product");

        when(productService.searchProducts(anyString())).thenReturn(List.of(productDTO));

        mockMvc.perform(get("/api/products/search").param("q", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {
        ProductDetailDTO productDTO = new ProductDetailDTO();
        productDTO.setId("1");
        productDTO.setName("Test Product");

        when(productService.getProductById("1")).thenReturn(productDTO);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getFeaturedProducts_ShouldReturnListOfProducts() throws Exception {
        Product product = new Product();
        product.setId("1");
        product.setName("Featured Product");

        when(productService.getFeaturedProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products/featured"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Featured Product"));
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("New Product");
        request.setPrice(BigDecimal.valueOf(100.0));

        Product product = new Product();
        product.setId("1");
        product.setName("New Product");
        product.setPrice(BigDecimal.valueOf(100.0));

        when(productService.createProduct(any(ProductCreateRequest.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    static class Jackson3HttpMessageConverter extends AbstractHttpMessageConverter<Object> {
        private final ObjectMapper objectMapper;

        public Jackson3HttpMessageConverter(ObjectMapper objectMapper) {
            super(MediaType.APPLICATION_JSON);
            this.objectMapper = objectMapper;
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return true;
        }

        @Override
        protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
                throws IOException, HttpMessageNotReadableException {
            return objectMapper.readValue(inputMessage.getBody(), clazz);
        }

        @Override
        protected void writeInternal(Object o, HttpOutputMessage outputMessage)
                throws IOException, HttpMessageNotWritableException {
            objectMapper.writeValue(outputMessage.getBody(), o);
        }
    }
}
