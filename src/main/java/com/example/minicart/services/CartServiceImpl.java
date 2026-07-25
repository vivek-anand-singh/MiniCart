package com.example.minicart.services;

import com.example.minicart.dto.CartAddRequestDto;
import com.example.minicart.dto.CartTotalResponseDto;
import com.example.minicart.dto.CartUpdateRequestDto;
import com.example.minicart.exception.CartNotFoundException;
import com.example.minicart.exception.CartQuantityException;
import com.example.minicart.exception.ProductNotFoundException;
import com.example.minicart.models.CartItem;
import com.example.minicart.models.Product;
import com.example.minicart.repositories.CartRepository;
import com.example.minicart.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService
{
    CartRepository cartRepository;
    ProductRepository productRepository;
    @Override
    public CartItem addProduct(CartAddRequestDto cartRequestDto) throws ProductNotFoundException
    {
        Optional<CartItem> optionalCartItem = cartRepository.findByProductId(cartRequestDto.getProductId());

        if(optionalCartItem.isEmpty())
        {
            Optional<Product> optionalProduct = productRepository.findById(cartRequestDto.getProductId());
            if(optionalProduct.isEmpty())
            {
                throw new ProductNotFoundException("Product with "+ cartRequestDto.getProductId()+ " not Found");
            }
            CartItem cartItem = new CartItem();
            cartItem.setProduct(optionalProduct.get());
            cartItem.setQuantity(cartRequestDto.getQuantity());
            return cartRepository.save(cartItem);
        }

        CartItem cartItem = optionalCartItem.get();
        // updating the cart quantity
        cartItem.setQuantity(cartItem.getQuantity()+cartRequestDto.getQuantity());
        return cartRepository.save(cartItem);
    }

    @Override
    public void updateProduct(long productId, CartUpdateRequestDto cartUpdateRequestDto) throws CartNotFoundException, CartQuantityException
    {
        Optional<CartItem> optionalCartItem = cartRepository.findByProductId(productId);
        if(optionalCartItem.isEmpty())
        {
            throw new CartNotFoundException("Cart with Product id "+ productId+" not Found");
        }

        CartItem cartItem = optionalCartItem.get();

        long quantity = cartUpdateRequestDto.getQuantity();
        if(quantity == 0)
        {
            cartRepository.delete(cartItem);
            return;
        }
        if(quantity < 0)
        {
            throw new CartQuantityException("Cart Quantity is invalid "+ quantity);
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cartItem);
    }

    @Override
    public CartTotalResponseDto cartTotalResponse()
    {
        return null;
    }
}
