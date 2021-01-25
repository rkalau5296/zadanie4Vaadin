package pl.rafal.vaadin.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.rafal.vaadin.dto.VehicleDto;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class Url {

    private final RestTemplate restTemplate;

    @Autowired
    public Url(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    //GET

    public List<VehicleDto> getVehicles() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles")
                .build().encode().toUri();

        VehicleDto[] vehicleDtos = restTemplate.getForObject(uri, VehicleDto[].class);

        return Arrays.asList(Optional.ofNullable(vehicleDtos).orElse(new VehicleDto[0]));
    }

    public VehicleDto getVehicleById(long id) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles" + id)
                .build().encode().toUri();
        return Optional.ofNullable(restTemplate.getForObject(uri, VehicleDto.class)).orElse(new VehicleDto());
    }

    public VehicleDto getVehicleByColor(String color) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles/vehicle/" + color)
                .build().encode().toUri();
        return Optional.ofNullable(restTemplate.getForObject(uri, VehicleDto.class)).orElse(new VehicleDto());
    }

    //POST

    public VehicleDto postVehicle(final VehicleDto vehicleDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles")
                .build().encode().toUri();
        return restTemplate.postForObject(uri, vehicleDto, VehicleDto.class);
    }

    //PUT

    public void updateVehicle(final VehicleDto vehicleDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles")
                .build().encode().toUri();
        restTemplate.put(uri, vehicleDto);
    }
    public void updateVehicleByIdAndColor(long id, String color) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles/" + id + "/" + color)
                .build().encode().toUri();
        restTemplate.put(uri, VehicleDto.class);
    }

    //DELETE

    public void deleteVehicle(long id) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles" + id)
                .build().encode().toUri();
        restTemplate.delete(uri);
    }
}
