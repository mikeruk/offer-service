package insuranceservices.offerservice.DTOs;

import insuranceservices.offerservice.enums.VehicleType;

public class PriceRequestDto {

    private Double kilometers;
    private VehicleType vehicleType;
    private String postcode;

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
}
