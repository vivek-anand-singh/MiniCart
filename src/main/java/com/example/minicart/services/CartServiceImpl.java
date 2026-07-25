package com.example.minicart.services;

import com.example.minicart.dto.*;
import com.example.minicart.exception.CartNotFoundException;
import com.example.minicart.exception.CartQuantityException;
import com.example.minicart.exception.ProductNotFoundException;
import com.example.minicart.models.CartItem;
import com.example.minicart.models.Product;
import com.example.minicart.repositories.CartRepository;
import com.example.minicart.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
        List<CartItem> cartItems = cartRepository.findAll();
        List<CartItemResponse> cartItemResponses = CartItemResponse.from(cartItems);
        BillDto billDto = computeBill(cartItems);

        CartTotalResponseDto cartTotalResponseDto = new CartTotalResponseDto();
        cartTotalResponseDto.setItems(cartItemResponses);
        cartTotalResponseDto.setBill(billDto);

        return cartTotalResponseDto;
    }

    private BillDto computeBill(List<CartItem> cartItems)
    {
        long itemTotalPaise = 0, deliveryFeePaise = 3000;
        for(CartItem cartItem: cartItems)
        {
            Product product = cartItem.getProduct();
            itemTotalPaise = itemTotalPaise + cartItem.getQuantity()*product.getPaise();
        }

        if(itemTotalPaise >50000) deliveryFeePaise = 0;

        BillDto billDto = new BillDto();
        billDto.setTotalPaise(itemTotalPaise);
        billDto.setDeliveryFeePaise(deliveryFeePaise);
        billDto.setGrandTotalPaise(itemTotalPaise+deliveryFeePaise);

        return  billDto;
    }
}
