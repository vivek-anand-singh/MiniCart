package com.example.minicart.services;

import com.example.minicart.dto.*;
import com.example.minicart.exception.CartNotFoundException;
import com.example.minicart.exception.CartQuantityException;
import com.example.minicart.exception.ProductNotFoundException;
import com.example.minicart.models.CartItem;
import com.example.minicart.models.Product;
import com.example.minicart.repositories.CartRepository;
import com.example.minicart.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService
{
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public CartItem addProduct(CartAddRequestDto cartRequestDto) throws ProductNotFoundException
    {
        if (cartRequestDto.getQuantity() <= 0)
        {
            throw new CartQuantityException("Quantity must be greater than 0");
        }

        Optional<CartItem> optionalCartItem = cartRepository.findByProductId(cartRequestDto.getProductId());

        if(optionalCartItem.isEmpty())
        {
            Optional<Product> optionalProduct = productRepository.findById(cartRequestDto.getProductId());
            if(optionalProduct.isEmpty())
            {
                throw new ProductNotFoundException("Product with id "+ cartRequestDto.getProductId()+ " not found");
            }
            CartItem cartItem = new CartItem();
            cartItem.setProduct(optionalProduct.get());
            cartItem.setQuantity(cartRequestDto.getQuantity());
            return cartRepository.save(cartItem);
        }

        CartItem cartItem = optionalCartItem.get();
        cartItem.setQuantity(cartItem.getQuantity() + cartRequestDto.getQuantity());
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

        long newQuantity = cartItem.getQuantity() + cartUpdateRequestDto.getQuantity();
        if(newQuantity < 0)
        {
            throw new CartQuantityException("Total quantity cannot be negative");
        }
        if(newQuantity == 0)
        {
            cartRepository.delete(cartItem);
            return;
        }
        cartItem.setQuantity(newQuantity);
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
        billDto.setItemTotalPaise(itemTotalPaise);
        billDto.setDeliveryFeePaise(deliveryFeePaise);
        billDto.setGrandTotalPaise(itemTotalPaise+deliveryFeePaise);

        return  billDto;
    }
}
