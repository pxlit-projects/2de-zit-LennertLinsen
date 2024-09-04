package be.pxl.services.controller;

import be.pxl.services.domain.Wishlist;
import be.pxl.services.services.IWishlistService;
import be.pxl.services.controller.WishlistController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.http.MediaType;
import org.mockito.Mockito;

@WebMvcTest(WishlistController.class)
public class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IWishlistService wishlistService;

    @Test
    void createWishlist() throws Exception {
        Long userId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/wishlist/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(wishlistService).addWishlist(Mockito.any(Wishlist.class));
    }

    @Test
    void addItemToCart() throws Exception {
        Long userId = 1L;
        Long productId = 1L;
        Wishlist wishlist = new Wishlist();
        wishlist.addProduct(productId);

        Mockito.when(wishlistService.addItemToWishlist(userId, productId)).thenReturn(wishlist);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/wishlist/addItemToWishlist/{userId}/{productId}", userId, productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productIds[0]").value(productId));
    }

    @Test
    void removeItemFromWishlist() throws Exception {
        Long userId = 1L;
        Long productId = 1L;
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.addProduct(productId);
        wishlist.removeItem(productId);  // Ensure item is removed

        Mockito.when(wishlistService.removeItemFromWishlist(userId, productId)).thenReturn(wishlist);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/wishlist/removeItemFromWishlist/{userId}/{productId}", userId, productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productIds").isEmpty()); // Check if the list is empty
    }


    @Test
    void getWishlistByUserId() throws Exception {
        Long userId = 1L;
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);

        Mockito.when(wishlistService.getWishlistByUserId(userId)).thenReturn(wishlist);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/wishlist/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId));
    }
}
