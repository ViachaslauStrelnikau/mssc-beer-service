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
@RequestMapping("/api/v1/beer")
public class BeerController {

    @Autowired
    private BeerService beerService;


    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable UUID beerId){

        return new ResponseEntity<>(beerService.getBeerById(beerId),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BeerDto> saveBeer(@RequestBody @Validated BeerDto beerDto){

        return new ResponseEntity<>(beerService.saveBeer(beerDto), HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> updateBeer(@PathVariable UUID beerId, @RequestBody @Validated BeerDto beerDto){

        return new ResponseEntity<>(beerService.updateBeer(beerId,beerDto),HttpStatus.OK);
    }

    @DeleteMapping("/{beerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeer(@PathVariable UUID beerId){

        beerService.deleteBeer(beerId);
    }

    @GetMapping
    public ResponseEntity<Page<BeerDto>> getBeerList(@RequestParam Integer pageNumber,
                                                     @RequestParam Integer pageSize){

        return new ResponseEntity<>(beerService.getBeerPage(pageNumber,pageSize),HttpStatus.OK);
    }

}
