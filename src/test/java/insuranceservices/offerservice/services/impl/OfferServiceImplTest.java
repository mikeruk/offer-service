package insuranceservices.offerservice.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import insuranceservices.offerservice.DTOs.NewOfferResponseDto;
import insuranceservices.offerservice.clients.CalculatorClient;
import insuranceservices.offerservice.entities.NewOfferRequestEntity;
import insuranceservices.offerservice.entities.OfferEntity;
import insuranceservices.offerservice.enums.OfferStatus;
import insuranceservices.offerservice.enums.VehicleType;
import insuranceservices.offerservice.exceptions.OfferNotFoundException;
import insuranceservices.offerservice.repositories.OfferRepository;
import insuranceservices.offerservice.repositories.OfferRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock
    private OfferRequestRepository offerRequestRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private CalculatorClient calculatorClient;

    private OfferServiceImpl offerService;

    @BeforeEach
    void setUp()
    {
        offerService = new OfferServiceImpl(offerRequestRepository,offerRepository,calculatorClient,new ObjectMapper());
    }

    // helper
    private NewOfferRequestEntity buildOfferRequestEntity(double km, VehicleType type, String postcode) {
        NewOfferRequestEntity entity = new NewOfferRequestEntity();
        entity.setKilometers(km);
        entity.setVehicleType(type);
        entity.setPostcode(postcode);
        return entity;
    }

    // helper
    private OfferEntity buildOffer(NewOfferRequestEntity req,
                                   double price, double kmFactor,
                                   double vehicleTypeFactor, double regionFactor,
                                   OfferStatus status, LocalDateTime expiresAt) {

        OfferEntity offer = new OfferEntity();
        offer.setOfferRequest(req);
        offer.setPrice(price);
        offer.setKmFactor(kmFactor);
        offer.setVehicleTypeFactor(vehicleTypeFactor);
        offer.setRegionFactor(regionFactor);
        offer.setStatus(status);
        offer.setExpiresAt(expiresAt);
        return offer;
    }

    @Test
    void getOffer_marksOfferExpired_whenExpiresAtIsInPast_andStatusIsCreated() {
        Long publicId = 123L;

        NewOfferRequestEntity req = buildOfferRequestEntity(10000.0, VehicleType.PKW, "10115");
        OfferEntity offer = buildOffer( req,100.0, 1.0, 1.0, 1.0,OfferStatus.CREATED, LocalDateTime.now().minusHours(1));

        when(offerRepository.findByPublicId(publicId)).thenReturn(Optional.of(offer));

        NewOfferResponseDto response = offerService.getOffer(publicId);

        assertThat(response.getStatus()).isEqualTo(OfferStatus.EXPIRED);
        assertThat(response.getPrice()).isEqualTo(100.0);
        assertThat(response.getKilometers()).isEqualTo(10000.0);
        assertThat(response.getVehicleType()).isEqualTo(VehicleType.PKW);
    }

    @Test
    void getOffer_throwsOfferNotFoundException_whenRepositoryReturnsEmpty() {
        Long publicId = 999L;
        when(offerRepository.findByPublicId(publicId)).thenReturn(Optional.empty());

        assertThrows(OfferNotFoundException.class,
                () -> offerService.getOffer(publicId));
    }

    @Test
    void getAllOffers_marksOnlyExpiredOffersExpired_andReturnsDtosForAllOffers()  {
        NewOfferRequestEntity req1 = buildOfferRequestEntity(5000.0, VehicleType.PKW, "10115");
        OfferEntity activeOffer = buildOffer(req1,50.0, 1.0, 1.0, 1.0, OfferStatus.CREATED, LocalDateTime.now().plusHours(1));

        NewOfferRequestEntity req2 = buildOfferRequestEntity(8000.0, VehicleType.LKW, "20095");
        OfferEntity expiredOffer = buildOffer(req2, 80.0, 1.1, 1.2, 0.8, OfferStatus.CREATED, LocalDateTime.now().minusHours(2));

        when(offerRepository.findAll()).thenReturn(List.of(activeOffer, expiredOffer));

        List<NewOfferResponseDto> responses = offerService.getAllOffers();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getStatus()).isEqualTo(OfferStatus.CREATED);
        assertThat(responses.get(1).getStatus()).isEqualTo(OfferStatus.EXPIRED);
    }
}
