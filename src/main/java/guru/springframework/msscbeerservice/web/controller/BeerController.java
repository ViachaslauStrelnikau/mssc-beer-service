package guru.springframework.msscbeerservice.web.controller;

import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class BeerController {
    private final Integer DEFAULT_PAGE_NUMBER = 0;
    private final Integer DEFAULT_PAGE_SIZE = 25;

    @Autowired
    private BeerService beerService;


    @GetMapping("/beer/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(
            @PathVariable UUID beerId,
            @RequestParam(required = false, defaultValue = "false") boolean showInventoryOnHand) {

        return new ResponseEntity<>(beerService.getBeerById(beerId, showInventoryOnHand), HttpStatus.OK);
    }

    @GetMapping("/beerUpc/{beerUpc}")
    public ResponseEntity<BeerDto> getBeerByUpc(
            @PathVariable String beerUpc,
            @RequestParam(required = false, defaultValue = "false") boolean showInventoryOnHand) {

        return new ResponseEntity<>(beerService.getBeerByUpc(beerUpc, showInventoryOnHand), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BeerDto> saveBeer(@RequestBody @Validated BeerDto beerDto) {

        return new ResponseEntity<>(beerService.saveBeer(beerDto), HttpStatus.CREATED);
    }

    @PutMapping("/beer/{beerId}")
    public ResponseEntity<BeerDto> updateBeer(@PathVariable UUID beerId, @RequestBody @Validated BeerDto beerDto) {

        return new ResponseEntity<>(beerService.updateBeer(beerId, beerDto), HttpStatus.OK);
    }

    @DeleteMapping("/beer/{beerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeer(@PathVariable UUID beerId) {

        beerService.deleteBeer(beerId);
    }

    @GetMapping("/beer")
    public ResponseEntity<Page<BeerDto>> getBeerList(@RequestParam(required = false) Integer pageNumber,
                                                     @RequestParam(required = false) Integer pageSize,
                                                     @RequestParam(required = false) String beerName,
                                                     @RequestParam(required = false) String beerStyle,
                                                     @RequestParam(required = false, defaultValue = "false") boolean showInventoryOnHand
    ) {
        pageNumber = pageNumber == null ? DEFAULT_PAGE_NUMBER : pageNumber;
        pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;


        return new ResponseEntity<>(beerService.listBeers(beerName, beerStyle, pageNumber, pageSize, showInventoryOnHand), HttpStatus.OK);
    }
}
