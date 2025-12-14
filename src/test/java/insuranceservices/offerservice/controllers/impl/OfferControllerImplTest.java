package insuranceservices.offerservice.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import insuranceservices.offerservice.DTOs.NewOfferRequestDto;
import insuranceservices.offerservice.DTOs.NewOfferResponseDto;
import insuranceservices.offerservice.enums.OfferStatus;
import insuranceservices.offerservice.enums.VehicleType;
import insuranceservices.offerservice.services.OfferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OfferControllerImpl.class)
class OfferControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OfferService offerService;

    @Test
    void createOffer_returns200_andBody() throws Exception {
        NewOfferRequestDto req = new NewOfferRequestDto();
        req.setKilometers(12000.0);
        req.setVehicleType(VehicleType.PKW);
        req.setPostcode("10115");

        NewOfferResponseDto resp = new NewOfferResponseDto();
        resp.setOfferId(1L);
        resp.setKilometers(12000.0);
        resp.setVehicleType(VehicleType.PKW);
        resp.setPostcode("10115");
        resp.setPrice(250.0);
        resp.setStatus(OfferStatus.CREATED);
        resp.setExpirationDate(LocalDateTime.now().plusDays(7));

        when(offerService.createOffer(any(NewOfferRequestDto.class))).thenReturn(resp);

        mockMvc.perform(post("/api/v1/offers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.offerId").value(1))
                .andExpect(jsonPath("$.price").value(250.0))
                .andExpect(jsonPath("$.kilometers").value(12000.0))
                .andExpect(jsonPath("$.vehicleType").value("PKW"))
                .andExpect(jsonPath("$.postcode").value("10115"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.expirationDate").exists());

        verify(offerService, times(1)).createOffer(any(NewOfferRequestDto.class));
        verifyNoMoreInteractions(offerService);
    }

    @Test
    void createOffer_invalidBody_returns400_andDoesNotCallService() throws Exception {
        mockMvc.perform(post("/api/v1/offers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(offerService);
    }

    @Test
    void getOffer_returns200_andBody() throws Exception {
        NewOfferResponseDto resp = new NewOfferResponseDto();
        resp.setOfferId(42L);
        resp.setPrice(300.0);
        resp.setVehicleType(VehicleType.LKW);
        resp.setStatus(OfferStatus.CREATED);

        when(offerService.getOffer(42L)).thenReturn(resp);

        mockMvc.perform(get("/api/v1/offers/{id}", 42L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.offerId").value(42))
                .andExpect(jsonPath("$.price").value(300.0))
                .andExpect(jsonPath("$.vehicleType").value("LKW"))
                .andExpect(jsonPath("$.status").value("CREATED"));

        verify(offerService, times(1)).getOffer(42L);
        verifyNoMoreInteractions(offerService);
    }

    @Test
    void getAllOffers_returns200_andArray() throws Exception {
        NewOfferResponseDto o1 = new NewOfferResponseDto();
        o1.setOfferId(1L);
        o1.setPrice(100.0);
        o1.setVehicleType(VehicleType.PKW);

        NewOfferResponseDto o2 = new NewOfferResponseDto();
        o2.setOfferId(2L);
        o2.setPrice(200.0);
        o2.setVehicleType(VehicleType.LKW);

        when(offerService.getAllOffers()).thenReturn(List.of(o1, o2));

        mockMvc.perform(get("/api/v1/offers/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].offerId").value(1))
                .andExpect(jsonPath("$[1].offerId").value(2));

        verify(offerService, times(1)).getAllOffers();
        verifyNoMoreInteractions(offerService);
    }
}
