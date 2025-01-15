package guru.springframework.msscbeerservice.web.mappers;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryRestTemplateImpl;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {DateMapper.class, BeerInventoryRestTemplateImpl.class},
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BeerMapper {
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "quantityOnHand", qualifiedByName = {"BeerInventoryRestTemplateImpl", "getOnHandInventory"}, source = "id")
    BeerDto beerToBeerDto(Beer beer);
    Beer beerDtoToBeer(BeerDto dto);
}
