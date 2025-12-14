package insuranceservices.offerservice.services;

import insuranceservices.offerservice.DTOs.NewOfferRequestDto;
import insuranceservices.offerservice.DTOs.NewOfferResponseDto;

import java.util.List;

public interface OfferService {

    NewOfferResponseDto createOffer(NewOfferRequestDto requestDto);

    NewOfferResponseDto getOffer(Long offerId);

    List<NewOfferResponseDto> getAllOffers();

}
