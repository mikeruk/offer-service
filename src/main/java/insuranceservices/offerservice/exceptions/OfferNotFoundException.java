package insuranceservices.offerservice.exceptions;

public class OfferNotFoundException extends RuntimeException {

    private final Long offerPublicId;

    public OfferNotFoundException(Long offerPublicId) {
        super("Offer not found by offerPublicId: " + offerPublicId);
        this.offerPublicId = offerPublicId;
    }

    public Long getOfferPublicId() {
        return offerPublicId;
    }
}
