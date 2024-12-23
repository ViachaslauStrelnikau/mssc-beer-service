package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getBeerById() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/beer/{beerId}", UUID.randomUUID().toString())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void saveBeer() throws Exception {
        BeerDto beerDto = BeerDto.builder()
                .beerName("test")
                .upc(1L)
                .beerStyle(BeerStyleEnum.PILSNER)
                .price(new BigDecimal(10))
                .build();

        String json=objectMapper.writeValueAsString(beerDto);

        MvcResult result=mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andReturn();
        json=result.getResponse().getContentAsString();

        BeerDto resultBeer=objectMapper.readValue(json,BeerDto.class);

        assertAll(
                ()->assertNotNull(resultBeer.getBeerName()),
                ()->assertNotNull(resultBeer.getBeerStyle()),
                ()->assertNotNull(resultBeer.getPrice()),
                ()->assertNotNull(resultBeer.getUpc()),
                ()->assertEquals(resultBeer.getBeerName(),beerDto.getBeerName()),
                ()->assertEquals(resultBeer.getBeerStyle(),beerDto.getBeerStyle()),
                ()->assertEquals(resultBeer.getPrice(),beerDto.getPrice()),
                ()->assertEquals(resultBeer.getUpc(),beerDto.getUpc())
        );
    }

    @Test
    void updateBeer() {
    }

    @Test
    void deleteBeer() {
    }

    @Test
    void getBeerList() {
    }
}