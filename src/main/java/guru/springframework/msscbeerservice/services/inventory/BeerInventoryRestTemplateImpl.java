package guru.springframework.msscbeerservice.services.inventory;

import guru.springframework.msscbeerservice.services.inventory.model.BeerInventoryDto;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
@Named("BeerInventoryRestTemplateImpl")
public class BeerInventoryRestTemplateImpl implements BeerInventoryService {

    private final String INVENTORY_PATH;
    private final String BEER_INVENTORY_SERVICE_HOST;
    private final RestTemplate restTemplate;

    public BeerInventoryRestTemplateImpl(@Value("${sfg.brewary.beer-inventory-host}") String beerInventoryServiceHost,
                                         @Value("${sfg.brewary.beer-inventory-path}") String inventoryPath,
                                         RestTemplateBuilder builder
    ) {
        this.BEER_INVENTORY_SERVICE_HOST = beerInventoryServiceHost;
        this.INVENTORY_PATH = inventoryPath;
        this.restTemplate = builder.build();
    }

    @Override
    @Named("getOnHandInventory")
    public Integer getOnHandInventory(UUID beerId) {
        log.debug("Calling inventory service for veer with id {}", beerId);

        ResponseEntity<List<BeerInventoryDto>> responseEntity = restTemplate
                .exchange(BEER_INVENTORY_SERVICE_HOST + INVENTORY_PATH,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {},
                        beerId);

        Integer quantity=Objects.requireNonNull(responseEntity.getBody())
                .stream()
                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                .sum();
        return quantity;
    }
}
