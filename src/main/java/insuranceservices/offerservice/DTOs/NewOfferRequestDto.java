package insuranceservices.offerservice.DTOs;

import insuranceservices.offerservice.enums.VehicleType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class NewOfferRequestDto {


    @NotNull(message = "kilometers is required")
    @Positive(message = "kilometers must be positive")
    @DecimalMax(value = "999999999", message = "kilometers must be less than 1,000,000,000")
    private Double kilometers;

    @NotNull(message = "vehicleType is required")
    private VehicleType vehicleType;

    @NotBlank(message = "postcode is required")
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
