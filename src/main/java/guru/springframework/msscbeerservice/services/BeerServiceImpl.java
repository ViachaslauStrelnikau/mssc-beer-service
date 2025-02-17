package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.exception.NotFoundException;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerPagedList;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private BeerMapper beerMapper;

    @Override
    public BeerDto saveBeer(BeerDto beerDto) {
        Beer beer = beerMapper.beerDtoToBeer(beerDto);

        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);
        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beer.getUpc());

        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }

    @Override
    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand==false")
    public BeerDto getBeerById(UUID beerId, boolean showInventoryOnHand) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        return beerMapper.beerToBeerDto(beer,showInventoryOnHand);
    }

    @Override
    @Cacheable(cacheNames = "beerUpcCache", key = "#upc", condition = "#showInventoryOnHand==false")
    public BeerDto getBeerByUpc(String upc, boolean showInventoryOnHand){
        Beer beer = beerRepository.findByUpc(upc).orElseThrow(NotFoundException::new);

        return beerMapper.beerToBeerDto(beer,showInventoryOnHand);
    }


    @Override
    public void deleteBeer(UUID beerId) {
        if (beerRepository.existsById(beerId))
            beerRepository.deleteById(beerId);
    }

    @Override
    @Cacheable(cacheNames = "beerListCache",condition = "#showInventoryOnHand==false")
    public BeerPagedList listBeers(String beerName, String beerStyle, Integer pageNum, Integer pageSize, boolean showInventoryOnHand) {
        log.info("listBeers {},{},{},{},{}",beerName,beerStyle,pageNum,pageSize,showInventoryOnHand);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Beer> beers;

        if (StringUtils.isNotEmpty(beerName) && StringUtils.isNotEmpty(beerStyle)) {
            beers = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageable);
        } else if (StringUtils.isNotEmpty(beerName) && StringUtils.isEmpty(beerStyle)) {
            beers = beerRepository.findAllByBeerName(beerName, pageable);
        } else if (StringUtils.isEmpty(beerName) && StringUtils.isNotEmpty(beerStyle)) {
            beers = beerRepository.findAllByBeerStyle(beerStyle, pageable);
        } else
            beers = beerRepository.findAll(pageable);

        return new BeerPagedList(beers.map(beer -> beerMapper.beerToBeerDto(beer,showInventoryOnHand)).stream().toList(),
                PageRequest.of(beers.getPageable().getPageNumber(), beers.getPageable().getPageSize()),
                beers.getTotalElements()
        );
    }

    public BeerPagedList getAllBeers() {
        Iterable<Beer> beers = beerRepository.findAll();
        List<BeerDto> beerDtos = new ArrayList<>();
        for (Beer beer : beers) {
            beerDtos.add(beerMapper.beerToBeerDto(beer));
        }

        return new BeerPagedList(beerDtos);
    }
}
