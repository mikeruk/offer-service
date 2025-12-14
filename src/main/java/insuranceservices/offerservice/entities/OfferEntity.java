package insuranceservices.offerservice.entities;

import insuranceservices.offerservice.enums.OfferStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "offer")
public class OfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="public_id", nullable=false, unique=true)
    private Long publicId;

    @PrePersist
    private void assignPublicId() {
        if (publicId == null) {
            publicId = System.currentTimeMillis(); // example generator
        }
    }

    @OneToOne(optional = false)
    @JoinColumn(name = "offer_request_id", nullable = false, unique = true)
    private NewOfferRequestEntity offerRequest;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private double kmFactor;

    @Column(nullable = false)
    private double vehicleTypeFactor;

    @Column(nullable = false)
    private double regionFactor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferStatus status;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public Long getId() {
        return id;
    }

    public Long getPublicId() {
        return publicId;
    }

    public NewOfferRequestEntity getOfferRequest() {
        return offerRequest;
    }

    public void setOfferRequest(NewOfferRequestEntity offerRequest) {
        this.offerRequest = offerRequest;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getKmFactor() {
        return kmFactor;
    }

    public void setKmFactor(double kmFactor) {
        this.kmFactor = kmFactor;
    }

    public double getVehicleTypeFactor() {
        return vehicleTypeFactor;
    }

    public void setVehicleTypeFactor(double vehicleTypeFactor) {
        this.vehicleTypeFactor = vehicleTypeFactor;
    }

    public double getRegionFactor() {
        return regionFactor;
    }

    public void setRegionFactor(double regionFactor) {
        this.regionFactor = regionFactor;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
