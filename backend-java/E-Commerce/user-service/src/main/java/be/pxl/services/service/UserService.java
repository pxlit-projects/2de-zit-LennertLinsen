package be.pxl.services.service;

import be.pxl.services.clients.CartClient;
import be.pxl.services.clients.WishlistClient;
import be.pxl.services.domain.User;
import be.pxl.services.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository _userRepository;
    private final CartClient _cartClient;
    private final WishlistClient _wishlistClient;

    public User getUserByEmailAndPassword(String email, String password){
        return _userRepository.findByEmailAndPassword(email, password);
    }

    public void createNewUser(User user){
        _userRepository.save(user);
        _cartClient.createCart(user.getId());
        _wishlistClient.createWishlist(user.getId());
    }
}
