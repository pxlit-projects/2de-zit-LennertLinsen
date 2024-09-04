package be.pxl.services.controller;

import be.pxl.services.domain.Order;
import be.pxl.services.service.IOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IOrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService)).build();
    }

    @Test
    void testGetAllOrders_Success() throws Exception {
        Long userId = 1L;
        Order order = new Order(userId);
        order.setOrderId(1L);
        order.setTotalPrice(100.0);

        when(orderService.getAll(userId)).thenReturn(Collections.singletonList(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalPrice").value(100.0));
    }

    @Test
    void testGetAllOrders_NotFound() throws Exception {
        Long userId = 1L;

        when(orderService.getAll(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect 200 OK, not 404
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty()); // Ensure the response body is empty
    }


    @Test
    void testCreateOrder_Success() throws Exception {
        Long userId = 1L;
        double totalPrice = 200.0;
        Order order = new Order(userId);
        order.setOrderId(1L);
        order.setTotalPrice(totalPrice);

        when(orderService.createOrder(userId, totalPrice)).thenReturn(order);

        mockMvc.perform(post("/api/orders/createOrder/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totalPrice)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(totalPrice));
    }

    @Test
    void testCreateOrder_BadRequest() throws Exception {
        Long userId = 1L;
        double totalPrice = 200.0;

        when(orderService.createOrder(userId, totalPrice)).thenReturn(null);

        mockMvc.perform(post("/api/orders/createOrder/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totalPrice)))
                .andExpect(status().isNotFound());
    }
}
