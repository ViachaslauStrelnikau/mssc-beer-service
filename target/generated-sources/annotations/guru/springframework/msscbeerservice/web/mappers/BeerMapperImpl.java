package guru.springframework.msscbeerservice.web.mappers;

import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerStyleEnum;
import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryRestTemplateImpl;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-03T12:25:18+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class BeerMapperImpl implements BeerMapper {

    @Autowired
    private DateMapper dateMapper;
    @Autowired
    private BeerInventoryRestTemplateImpl beerInventoryRestTemplateImpl;

    @Override
    public BeerDto beerToBeerDto(Beer beer) {
        if ( beer == null ) {
            return null;
        }

        BeerDto.BeerDtoBuilder beerDto = BeerDto.builder();

        beerDto.quantityOnHand( beerInventoryRestTemplateImpl.getOnHandInventory( beer.getId() ) );
        beerDto.id( beer.getId() );
        beerDto.createdDate( dateMapper.asOffsetDateTime( beer.getCreatedDate() ) );
        beerDto.lastModifiedDate( dateMapper.asOffsetDateTime( beer.getLastModifiedDate() ) );
        beerDto.beerName( beer.getBeerName() );
        if ( beer.getBeerStyle() != null ) {
            beerDto.beerStyle( Enum.valueOf( BeerStyleEnum.class, beer.getBeerStyle() ) );
        }
        beerDto.upc( beer.getUpc() );
        beerDto.price( beer.getPrice() );

        return beerDto.build();
    }

    @Override
    public BeerDto beerToBeerDto(Beer beer, boolean showInventoryOnHand) {
        if ( beer == null ) {
            return null;
        }

        BeerDto.BeerDtoBuilder beerDto = BeerDto.builder();

        if ( beer != null ) {
            if ( showInventoryOnHand ) {
                beerDto.quantityOnHand( beerInventoryRestTemplateImpl.getOnHandInventory( beer.getId() ) );
            }
            beerDto.id( beer.getId() );
            beerDto.createdDate( dateMapper.asOffsetDateTime( beer.getCreatedDate() ) );
            beerDto.lastModifiedDate( dateMapper.asOffsetDateTime( beer.getLastModifiedDate() ) );
            beerDto.beerName( beer.getBeerName() );
            if ( beer.getBeerStyle() != null ) {
                beerDto.beerStyle( Enum.valueOf( BeerStyleEnum.class, beer.getBeerStyle() ) );
            }
            beerDto.upc( beer.getUpc() );
            beerDto.price( beer.getPrice() );
        }

        return beerDto.build();
    }

    @Override
    public Beer beerDtoToBeer(BeerDto dto) {
        if ( dto == null ) {
            return null;
        }

        Beer.BeerBuilder beer = Beer.builder();

        beer.id( dto.getId() );
        if ( dto.getVersion() != null ) {
            beer.version( dto.getVersion().longValue() );
        }
        beer.createdDate( dateMapper.asTimestamp( dto.getCreatedDate() ) );
        beer.lastModifiedDate( dateMapper.asTimestamp( dto.getLastModifiedDate() ) );
        beer.beerName( dto.getBeerName() );
        if ( dto.getBeerStyle() != null ) {
            beer.beerStyle( dto.getBeerStyle().name() );
        }
        beer.upc( dto.getUpc() );
        beer.price( dto.getPrice() );

        return beer.build();
    }
}
