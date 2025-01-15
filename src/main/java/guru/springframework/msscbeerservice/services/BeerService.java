package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;

import java.util.UUID;

public interface BeerService {
    BeerDto saveBeer(BeerDto beer);
    BeerDto updateBeer(UUID beerId,BeerDto beer);
    BeerDto getBeerById(UUID beerId, boolean showInventoryOnHand);
    void deleteBeer(UUID beerId);
    BeerPagedList listBeers(String beerName, String beerStyle, Integer pageNum, Integer pageSize, boolean showInventoryOnHand);
}
