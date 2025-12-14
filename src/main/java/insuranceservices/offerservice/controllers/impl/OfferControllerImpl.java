package insuranceservices.offerservice.controllers.impl;

import insuranceservices.offerservice.DTOs.NewOfferRequestDto;
import insuranceservices.offerservice.DTOs.NewOfferResponseDto;
import insuranceservices.offerservice.controllers.OfferController;

import insuranceservices.offerservice.services.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/offers")
public class OfferControllerImpl implements OfferController {

    private final OfferService offerService;

    public OfferControllerImpl(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/create")
    @Override
    public ResponseEntity<NewOfferResponseDto> createOffer(@Valid @RequestBody NewOfferRequestDto createOfferDto) {

        NewOfferResponseDto response = offerService.createOffer(createOfferDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<NewOfferResponseDto> getOffer(@PathVariable Long id) {
        NewOfferResponseDto response = offerService.getOffer(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Override
    public ResponseEntity<List<NewOfferResponseDto>> getAllOffers() {
        List<NewOfferResponseDto> offers = offerService.getAllOffers();
        return ResponseEntity.ok(offers);
    }
}
