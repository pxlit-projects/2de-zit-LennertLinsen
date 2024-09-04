package be.pxl.services.controller;

import be.pxl.services.controller.CategoryResponse;
import be.pxl.services.domain.Category;
import be.pxl.services.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() throws Exception {
        // Arrange
        Category category1 = new Category(1L, "Electronics", null);
        Category category2 = new Category(2L, "Books", null);

        List<CategoryResponse> categoryResponses = Arrays.asList(
                new CategoryResponse(1L, "Electronics", List.of()),
                new CategoryResponse(2L, "Books", List.of())
        );

        when(categoryService.getAllCategories()).thenReturn(categoryResponses);

        // Act & Assert
        mockMvc.perform(get("/api/categories/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].categoryId").value(1))
                .andExpect(jsonPath("$[0].categoryName").value("Electronics"))
                .andExpect(jsonPath("$[1].categoryId").value(2))
                .andExpect(jsonPath("$[1].categoryName").value("Books"));
    }

    @Test
    void testCreateCategory() throws Exception {
        // Arrange
        Category newCategory = new Category(1L, "Electronics", null);

        // Act & Assert
        mockMvc.perform(post("/api/categories/createCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));

        // Verify that the service's createCategory method was called exactly once with the correct argument
        verify(categoryService, times(1)).createCategory(newCategory);
    }
}

