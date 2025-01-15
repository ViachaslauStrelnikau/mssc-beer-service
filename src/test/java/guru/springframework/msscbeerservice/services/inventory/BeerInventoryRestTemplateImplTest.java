package guru.springframework.msscbeerservice.services.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerInventoryRestTemplateImplTest {

    @Autowired
    private BeerInventoryRestTemplateImpl beerInventoryRestTemplate;

    @Test
    void getOnHandInventory() {

       Integer quantity= beerInventoryRestTemplate.getOnHandInventory(UUID.fromString("61373132-6439-3134-2d36-3165612d3436"));
       System.out.println(quantity);
    }
}