package insuranceservices.offerservice.clients;

import insuranceservices.offerservice.DTOs.PriceRequestDto;
import insuranceservices.offerservice.DTOs.PriceResponseDto;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.bind.annotation.RequestBody;

@HttpExchange("/api/v1/price")
public interface CalculatorClient {

    @PostExchange("/calculate")
    PriceResponseDto calculatePrice(@RequestBody PriceRequestDto request);
}
