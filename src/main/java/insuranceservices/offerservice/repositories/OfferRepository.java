package insuranceservices.offerservice.repositories;

import insuranceservices.offerservice.entities.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferRepository extends JpaRepository<OfferEntity, Long> {

    Optional<OfferEntity> findByPublicId(Long publicId);

}
