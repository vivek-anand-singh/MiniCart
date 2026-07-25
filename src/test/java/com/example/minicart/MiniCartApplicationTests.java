package com.example.minicart;

import com.example.minicart.repositories.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MiniCartApplicationTests {

    @Autowired MockMvc mockMvc;
    @Autowired CartRepository cartRepository;

    @BeforeEach
    void clearCart() {
        cartRepository.deleteAll();
    }

    @Test
    void getProducts_returns10Products() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].name").value("Indian Tomato"))
                .andExpect(jsonPath("$[0].paise").value(2500))
                .andExpect(jsonPath("$[0].unit").value("500g"));
    }

    @Test
    void addToCart_unknownProduct_returns404() throws Exception {
        mockMvc.perform(post("/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 999, \"quantity\": 1}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void addToCart_zeroQuantity_returns400() throws Exception {
        mockMvc.perform(post("/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 1, \"quantity\": 0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void addToCart_negativeQuantity_returns400() throws Exception {
        mockMvc.perform(post("/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 1, \"quantity\": -3}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void addToCart_sameProductTwice_incrementsQuantity() throws Exception {
        mockMvc.perform(post("/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 1, \"quantity\": 2}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 1, \"quantity\": 3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void getCart_deliveryFeeCharged_whenTotalUnderThreshold() throws Exception {
        // Tomato: 2500 paise x 2 = 5000 paise (well under 50000)
        mockMvc.perform(post("/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 1, \"quantity\": 2}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bill.itemTotalPaise").value(5000))
                .andExpect(jsonPath("$.bill.deliveryFeePaise").value(3000))
                .andExpect(jsonPath("$.bill.grandTotalPaise").value(8000));
    }

    @Test
    void getCart_deliveryFeeFree_whenTotalOverThreshold() throws Exception {
        // Paneer: 9000 paise x 6 = 54000 paise (> 50000 threshold)
        mockMvc.perform(post("/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 10, \"quantity\": 6}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bill.itemTotalPaise").value(54000))
                .andExpect(jsonPath("$.bill.deliveryFeePaise").value(0))
                .andExpect(jsonPath("$.bill.grandTotalPaise").value(54000));
    }

    @Test
    void patchCart_deltaReducesQuantity() throws Exception {
        mockMvc.perform(post("/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 1, \"quantity\": 5}"))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/cart/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": -2}"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].quantity").value(3));
    }

    @Test
    void patchCart_deltaToZero_removesItem() throws Exception {
        mockMvc.perform(post("/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 1, \"quantity\": 3}"))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/cart/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": -3}"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(0));
    }

    @Test
    void patchCart_deltaMakesTotalNegative_returns400() throws Exception {
        mockMvc.perform(post("/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 1, \"quantity\": 2}"))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/cart/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": -5}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void patchCart_productNotInCart_returns404() throws Exception {
        mockMvc.perform(patch("/cart/items/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": 2}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }
}
