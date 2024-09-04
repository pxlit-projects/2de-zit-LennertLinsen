package be.pxl.services.controller;

import be.pxl.services.domain.Wishlist;
import be.pxl.services.services.IWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final IWishlistService _wishlistService;

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createWishlist(@PathVariable Long userId){
        Wishlist newWishlist = new Wishlist();

        newWishlist.setUserId(userId);

        _wishlistService.addWishlist(newWishlist);
    }

    @PutMapping("/addItemToWishlist/{userId}/{productId}")
    public ResponseEntity<Wishlist> addItemToCart(@PathVariable Long userId, @PathVariable Long productId){
        Wishlist wishlist = _wishlistService.addItemToWishlist(userId, productId);

        if(wishlist != null){
            return ResponseEntity.ok(wishlist);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/removeItemFromWishlist/{userId}/{productId}")
    public ResponseEntity<Wishlist> removeItemFromWishList(@PathVariable Long userId, @PathVariable Long productId){
        Wishlist wishlist = _wishlistService.removeItemFromWishlist(userId, productId);

        if(wishlist != null){
            return ResponseEntity.ok(wishlist);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Wishlist> getWishlistByUserId(@PathVariable Long userId){
        Wishlist wishlist = _wishlistService.getWishlistByUserId(userId);
        if (wishlist != null){
            return ResponseEntity.ok(wishlist);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
