package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerService {
    BeerDto saveBeer(BeerDto beer);
    BeerDto updateBeer(BeerDto beer);
    BeerDto getBeer(UUID beerId);
    void deleteBeer(UUID beerId);
    Page<BeerDto> getBeerPage(Integer pageNum, Integer pageSize);
}
