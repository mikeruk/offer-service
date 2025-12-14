package insuranceservices.offerservice.controllers;

import insuranceservices.offerservice.DTOs.NewOfferRequestDto;
import insuranceservices.offerservice.DTOs.NewOfferResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OfferController {

    ResponseEntity<NewOfferResponseDto> createOffer(NewOfferRequestDto requestDto);

    ResponseEntity<NewOfferResponseDto> getOffer(Long id);

    ResponseEntity<List<NewOfferResponseDto>> getAllOffers();

}
