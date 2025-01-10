package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;

import java.util.UUID;

public interface BeerService {
    BeerDto saveBeer(BeerDto beer);
    BeerDto updateBeer(BeerDto beer);
    BeerDto getBeer(UUID beerId);
    void deleteBeer(UUID beerId);
    BeerPagedList getBeerPage(Integer pageNum, Integer pageSize);
}
