package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.spring.guru", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
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
                                .param("isCold", "yes")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("v1/beer-get",
                        pathParameters(
                                parameterWithName("beerId").description("UUID of desired beer to get.")
                        ),
                        relaxedQueryParameters(
                                parameterWithName("isCold").description("Is Beer cold query param")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of beer"),
                                fieldWithPath("version").description("Version of beer"),
                                fieldWithPath("createdDate").description("Created date of beer"),
                                fieldWithPath("lastModifiedDate").description("Last modified date of beer"),
                                fieldWithPath("beerName").description("Name of beer"),
                                fieldWithPath("beerStyle").description("Style of beer"),
                                fieldWithPath("upc").description("Upc of beer"),
                                fieldWithPath("price").description("Price of beer"),
                                fieldWithPath("quantityOnHand").description("Quantity On Hand beers")
                        )
                ))
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

        String json = objectMapper.writeValueAsString(beerDto);
        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(
                        document("v1/beer-new",
                                requestFields(
                                        fields.withPath("id").ignored(),
                                        fields.withPath("version").ignored(),
                                        fields.withPath("createdDate").ignored(),
                                        fields.withPath("lastModifiedDate").ignored(),
                                        fields.withPath("beerName").description("Name of beer"),
                                        fields.withPath("beerStyle").description("Style of beer"),
                                        fields.withPath("upc").description("Upc of beer").attributes(),
                                        fields.withPath("price").description("Price of beer"),
                                        fields.withPath("quantityOnHand").ignored()
                                )
                        )
                )
                .andReturn();
        json = result.getResponse().getContentAsString();

        BeerDto resultBeer = objectMapper.readValue(json, BeerDto.class);

        assertAll(
                () -> assertNotNull(resultBeer.getBeerName()),
                () -> assertNotNull(resultBeer.getBeerStyle()),
                () -> assertNotNull(resultBeer.getPrice()),
                () -> assertNotNull(resultBeer.getUpc()),
                () -> assertEquals(resultBeer.getBeerName(), beerDto.getBeerName()),
                () -> assertEquals(resultBeer.getBeerStyle(), beerDto.getBeerStyle()),
                () -> assertEquals(resultBeer.getPrice(), beerDto.getPrice()),
                () -> assertEquals(resultBeer.getUpc(), beerDto.getUpc())
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


    private static class ConstrainedFields{
        private final ConstraintDescriptions constraintDescriptions;

        private ConstrainedFields(Class<?> input) {
            this.constraintDescriptions =  new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path){
            return  fieldWithPath(path).attributes(key("constraints").value(StringUtils.collectionToDelimitedString(
                    this.constraintDescriptions.descriptionsForProperty(path), ". ")
            ));
        }
    }
}