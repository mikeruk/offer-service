package insuranceservices.offerservice.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import insuranceservices.offerservice.DTOs.NewOfferRequestDto;
import insuranceservices.offerservice.DTOs.NewOfferResponseDto;
import insuranceservices.offerservice.DTOs.PriceRequestDto;
import insuranceservices.offerservice.DTOs.PriceResponseDto;
import insuranceservices.offerservice.clients.CalculatorClient;
import insuranceservices.offerservice.entities.OfferEntity;
import insuranceservices.offerservice.entities.NewOfferRequestEntity;
import insuranceservices.offerservice.enums.OfferStatus;
import insuranceservices.offerservice.exceptions.OfferNotFoundException;
import insuranceservices.offerservice.repositories.OfferRepository;
import insuranceservices.offerservice.repositories.OfferRequestRepository;
import insuranceservices.offerservice.services.OfferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRequestRepository offerRequestRepository;
    private final OfferRepository offerRepository;
    private final CalculatorClient calculatorClient;
    private final ObjectMapper objectMapper;

    public OfferServiceImpl(OfferRequestRepository offerRequestRepository,
                            OfferRepository offerRepository,
                            CalculatorClient calculatorClient,
                            ObjectMapper objectMapper) {
        this.offerRequestRepository = offerRequestRepository;
        this.offerRepository = offerRepository;
        this.calculatorClient = calculatorClient;
        this.objectMapper = objectMapper;
    }


    @Override
    @Transactional(
            readOnly = false,
            propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class,
            timeout = 1000,
            transactionManager = "transactionManager"
    )
    public NewOfferResponseDto createOffer(NewOfferRequestDto newOfferRequestDto) {

        NewOfferRequestEntity reqEntity =
                objectMapper.convertValue(newOfferRequestDto, NewOfferRequestEntity.class);
        NewOfferRequestEntity savedReq = offerRequestRepository.save(reqEntity);

        PriceRequestDto priceRequest =
                objectMapper.convertValue(newOfferRequestDto, PriceRequestDto.class);
        PriceResponseDto price = calculatorClient.calculatePrice(priceRequest);

        OfferEntity offer = new OfferEntity();
        offer.setOfferRequest(savedReq);
        offer.setPrice(price.getPrice());
        offer.setKmFactor(price.getKmFactor());
        offer.setVehicleTypeFactor(price.getVehicleTypeFactor());
        offer.setRegionFactor(price.getRegionFactor());
        offer.setStatus(OfferStatus.CREATED);
        offer.setExpiresAt(LocalDateTime.now().plusHours(24));

        OfferEntity savedOffer = offerRepository.save(offer);

        return mapToResponseDto(savedOffer);
    }

    @Override
    @Transactional(
            readOnly = false,
            propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class,
            timeout = 3,
            transactionManager = "transactionManager"
    )
    public NewOfferResponseDto getOffer(Long offerPublicId) {

        OfferEntity offer = offerRepository.findByPublicId(offerPublicId)
                .orElseThrow(() -> new OfferNotFoundException(offerPublicId));

        applyAutoExpiration(offer);

        return mapToResponseDto(offer);
    }

    @Override
    @Transactional(readOnly = false)
    public List<NewOfferResponseDto> getAllOffers() {

        List<OfferEntity> offers = offerRepository.findAll();

        offers.forEach(this::applyAutoExpiration);

        return offers.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    private void applyAutoExpiration(OfferEntity offer) {
        if (offer.getStatus() == OfferStatus.CREATED &&
                offer.getExpiresAt().isBefore(LocalDateTime.now()))
        {
            offer.setStatus(OfferStatus.EXPIRED);
        }
    }

    private NewOfferResponseDto mapToResponseDto(OfferEntity offer) {
        NewOfferRequestEntity req = offer.getOfferRequest();

        NewOfferResponseDto dto = new NewOfferResponseDto();
        dto.setOfferId(offer.getPublicId());
        dto.setOfferRequestId(req.getPublicId());
        dto.setKilometers(req.getKilometers());
        dto.setVehicleType(req.getVehicleType());
        dto.setPostcode(req.getPostcode());
        dto.setPrice(offer.getPrice());
        dto.setKmFactor(offer.getKmFactor());
        dto.setVehicleTypeFactor(offer.getVehicleTypeFactor());
        dto.setRegionFactor(offer.getRegionFactor());
        dto.setStatus(offer.getStatus());
        dto.setExpirationDate(offer.getExpiresAt());

        return dto;
    }
}
