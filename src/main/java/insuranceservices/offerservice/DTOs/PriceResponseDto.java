package insuranceservices.offerservice.DTOs;

public class PriceResponseDto {

    private Double price;
    private Double kmFactor;
    private Double vehicleTypeFactor;
    private Double regionFactor;

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
}
