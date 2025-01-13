package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @MockitoBean
    private BeerService beerService;

    private final String BASE_URL = "/api/v1/beer";

    @Test
    void getBeerById() throws Exception {
        BeerDto beerDto = createValidBeerDto();
        when(beerService.getBeerById(any())).thenReturn(beerDto);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(BASE_URL + "/{beerId}", UUID.randomUUID().toString())
                                .param("isCold", "yes")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(document("v1/beer-get",
                        pathParameters(
                                parameterWithName("beerId").description("UUID of desired beer to get.")
                        ),
                        queryParameters(
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
        BeerDto beerDto = createValidBeerDto();
        when(beerService.saveBeer(any(BeerDto.class))).thenReturn(beerDto);
        beerDto.setId(null);

        String json = objectMapper.writeValueAsString(beerDto);
        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL)
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
    void updateBeer() throws Exception {
        BeerDto beerDto = createValidBeerDto();
        when(beerService.updateBeer(any(UUID.class),any(BeerDto.class))).thenReturn(beerDto);
        String json = objectMapper.writeValueAsString(beerDto);
        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        mockMvc.perform(
                        put(BASE_URL + "/{beerId}", UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andDo(document("v1/beer-update",
                        pathParameters(
                                parameterWithName("beerId").description("UUID of desired beer to get")
                        ),
                        requestFields(
                                fields.withPath("id").description("UUID of beer"),
                                fields.withPath("version").ignored(),
                                fields.withPath("createdDate").ignored(),
                                fields.withPath("lastModifiedDate").ignored(),
                                fields.withPath("beerName").description("Name of beer"),
                                fields.withPath("beerStyle").description("Style of beer"),
                                fields.withPath("upc").description("Upc of beer").attributes(),
                                fields.withPath("price").description("Price of beer"),
                                fields.withPath("quantityOnHand").description("Quantity on hand  of beer")
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
                ));
    }

    @Test
    void deleteBeer() throws Exception {
        doNothing().when(beerService).deleteBeer(any());

        mockMvc.perform(delete(BASE_URL + "/{beerId}", UUID.randomUUID().toString()))
                .andExpect(status().isNoContent())
                .andDo(document("v1/beer-delete",
                        pathParameters(
                                parameterWithName("beerId").description("UUID of desired beer to get")
                        )
                ));
    }

    @Test
    void getBeerList() throws Exception {
        BeerPagedList page = new BeerPagedList(Collections.singletonList(createValidBeerDto()));

        when(beerService.getBeerPage(any(Integer.class), any(Integer.class)))
                .thenReturn(page);
        String json = objectMapper.writeValueAsString(page);

        mockMvc.perform(get(BASE_URL)
                        .param("pageNumber",String.valueOf(1))
                        .param("pageSize",String.valueOf(1))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andDo(document("v1/beer-list",
                        queryParameters(
                                parameterWithName("pageNumber").description("Number of page"),
                                parameterWithName("pageSize").description("Number items per page")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content").description("List of beers"),
                                fieldWithPath("totalElements").description("Total elements id DB"),
                                fieldWithPath("totalPages").description("Total pages"),
                                fieldWithPath("size").description("Size of page"),
                                fieldWithPath("number").description("Number of page"),
                                fieldWithPath("numberOfElements").description("Number of elements on a page")
                        )
                ));
    }


    private static class ConstrainedFields {
        private final ConstraintDescriptions constraintDescriptions;

        private ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils.collectionToDelimitedString(
                    this.constraintDescriptions.descriptionsForProperty(path), ". ")
            ));
        }
    }

    private BeerDto createValidBeerDto() {
        return BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName("test")
                .upc(1L)
                .beerStyle(BeerStyleEnum.PILSNER)
                .price(new BigDecimal(10))
                .build();
    }
}