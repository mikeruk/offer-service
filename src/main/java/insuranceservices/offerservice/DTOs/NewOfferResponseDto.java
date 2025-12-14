package insuranceservices.offerservice.DTOs;

import insuranceservices.offerservice.enums.OfferStatus;
import insuranceservices.offerservice.enums.VehicleType;

import java.time.LocalDateTime;

public class NewOfferResponseDto {

    private Long offerId;
    private Long offerRequestId;

    private Double kilometers;
    private VehicleType vehicleType;
    private String postcode;

    private Double price;
    private Double kmFactor;
    private Double vehicleTypeFactor;
    private Double regionFactor;

    private OfferStatus status;
    private LocalDateTime expirationDate;

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public Long getOfferRequestId() {
        return offerRequestId;
    }

    public void setOfferRequestId(Long offerRequestId) {
        this.offerRequestId = offerRequestId;
    }

    public Double getKilometers() {
        return kilometers;
    }

    public void setKilometers(Double kilometers) {
        this.kilometers = kilometers;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getKmFactor() {
        return kmFactor;
    }

    public void setKmFactor(Double kmFactor) {
        this.kmFactor = kmFactor;
    }

    public Double getVehicleTypeFactor() {
        return vehicleTypeFactor;
    }

    public void setVehicleTypeFactor(Double vehicleTypeFactor) {
        this.vehicleTypeFactor = vehicleTypeFactor;
    }

    public Double getRegionFactor() {
        return regionFactor;
    }

    public void setRegionFactor(Double regionFactor) {
        this.regionFactor = regionFactor;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
