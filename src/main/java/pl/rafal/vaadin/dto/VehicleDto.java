package pl.rafal.vaadin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto extends ResourceSupport{

    private long vehicleId;
    private String brand;
    private String model;
    private String color;
}
