package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public BeerDto saveBeer(BeerDto beerDto){
        if(beerDto==null)
            return null;

        Beer beer =beerMapper.beerDtoToBeer(beerDto);

        return  beerMapper.beerToBeerDto(beerRepository.save(beer));
    }

    @Override
    public BeerDto updateBeer(BeerDto beerDto) {
        if(beerDto==null)
            return null;

        Beer beer =beerMapper.beerDtoToBeer(beerDto);

        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }

    @Override
    public BeerDto getBeer(UUID beerId) {
        Beer beer=beerRepository.findById(beerId).orElse(null);
        if (beer==null)
            return null;

        return beerMapper.beerToBeerDto(beer);
    }

    @Override
    public void deleteBeer(UUID beerId) {
        if (beerRepository.existsById(beerId))
            beerRepository.deleteById(beerId);
    }

    @Override
    public Page<BeerDto> getBeerPage(Integer pageNum, Integer pageSize) {
        Page<Beer> beers =beerRepository.findAll(PageRequest.of(pageNum,pageSize));

        return beers.map(beerMapper::beerToBeerDto);
    }
}
