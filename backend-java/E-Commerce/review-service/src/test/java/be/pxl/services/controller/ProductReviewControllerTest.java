package be.pxl.services.controller;

import be.pxl.services.domain.ProductReview;
import be.pxl.services.service.IProductReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductReviewController.class)
public class ProductReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductReviewService productReviewService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProductReview() throws Exception {
        ProductReviewDTO dto = new ProductReviewDTO("Amazing", "Loved it", 5, "SupplierY");
        ProductReview review = new ProductReview("Amazing", "Loved it", 5, "SupplierY", 1L);

        when(productReviewService.addProductReview(dto, 1L)).thenReturn(review);

        mockMvc.perform(post("/api/productreviews/add/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Amazing"))
                .andExpect(jsonPath("$.description").value("Loved it"))
                .andExpect(jsonPath("$.stars").value(5))
                .andExpect(jsonPath("$.supplier").value("SupplierY"))
                .andExpect(jsonPath("$.productId").value(1));
    }

    @Test
    public void testGetProductReviewsByProductId() throws Exception {
        ProductReview review = new ProductReview("Great", "Nice product", 5, "SupplierX", 1L);

        when(productReviewService.getProductReviewsByProductId(1L)).thenReturn(Collections.singletonList(review));

        mockMvc.perform(get("/api/productreviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Great"))
                .andExpect(jsonPath("$[0].description").value("Nice product"))
                .andExpect(jsonPath("$[0].stars").value(5))
                .andExpect(jsonPath("$[0].supplier").value("SupplierX"))
                .andExpect(jsonPath("$[0].productId").value(1));
    }

    @Test
    public void testGetProductReviewsBySupplier() throws Exception {
        ProductReview review = new ProductReview("Good", "Very good product", 4, "SupplierX", 2L);

        when(productReviewService.getProductReviewsBySupplier("SupplierX")).thenReturn(Collections.singletonList(review));

        mockMvc.perform(get("/api/productreviews/bySupplier")
                        .param("supplier", "SupplierX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Good"))
                .andExpect(jsonPath("$[0].description").value("Very good product"))
                .andExpect(jsonPath("$[0].stars").value(4))
                .andExpect(jsonPath("$[0].supplier").value("SupplierX"))
                .andExpect(jsonPath("$[0].productId").value(2));
    }
}
