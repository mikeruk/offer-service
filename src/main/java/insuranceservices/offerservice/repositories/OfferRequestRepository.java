package insuranceservices.offerservice.repositories;

import insuranceservices.offerservice.entities.NewOfferRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRequestRepository extends JpaRepository<NewOfferRequestEntity, Long> {
}
