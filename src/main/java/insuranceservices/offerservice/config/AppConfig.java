package insuranceservices.offerservice.config;

import insuranceservices.offerservice.clients.CalculatorClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;

@Configuration
public class AppConfig {

    @Bean
    public WebClient calculatorWebClient(@Value("${calculator-service.base-url}") String baseUrl)
    {

        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public CalculatorClient calculatorClient(WebClient calculatorWebClient) {

        WebClientAdapter adapter = WebClientAdapter.create(calculatorWebClient);

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();

        return factory.createClient(CalculatorClient.class);
    }
}
