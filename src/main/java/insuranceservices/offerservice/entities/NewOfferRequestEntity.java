package insuranceservices.offerservice.entities;

import insuranceservices.offerservice.enums.VehicleType;
import jakarta.persistence.*;

@Entity
@Table(name = "offer_request")
public class NewOfferRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="public_id", nullable=false, unique=true)
    private Long publicId;

    @PrePersist
    private void assignPublicId() {
        if (publicId == null) {
            publicId = System.nanoTime();
        }
    }

    @Column(nullable = false)
    private double kilometers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    @Column(nullable = false, length = 16)
    private String postcode;


    public Long getId() {
        return id;
    }

    public Long getPublicId() {
        return publicId;
    }

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers(double kilometers) {
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
