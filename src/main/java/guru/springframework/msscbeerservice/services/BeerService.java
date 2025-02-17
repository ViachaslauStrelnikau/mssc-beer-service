package guru.springframework.msscbeerservice.services;

import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerPagedList;

import java.util.UUID;

public interface BeerService {
    BeerDto saveBeer(BeerDto beer);
    BeerDto updateBeer(UUID beerId,BeerDto beer);
    BeerDto getBeerById(UUID beerId, boolean showInventoryOnHand);
    BeerDto getBeerByUpc(String upc, boolean showInventoryOnHand);
    void deleteBeer(UUID beerId);
    BeerPagedList listBeers(String beerName, String beerStyle, Integer pageNum, Integer pageSize, boolean showInventoryOnHand);
}
