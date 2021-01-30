package pl.rafal.vaadin.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.rafal.vaadin.dto.Vehicle;
import java.net.URI;
import java.util.Arrays;
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

    public List<Vehicle> getVehicles() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles")
                .build().encode().toUri();

        Vehicle[] vehicles = restTemplate.getForObject(uri, Vehicle[].class);

        return Arrays.asList(Optional.ofNullable(vehicles).orElse(new Vehicle[0]));
    }

    public Vehicle getVehicleById(long id) {

        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles/vehicles/" + id)
                .build().encode().toUri();
        return Optional.ofNullable(restTemplate.getForObject(uri, Vehicle.class)).orElse(new Vehicle());
    }

    public Vehicle getVehicleByColor(String color) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles/vehicle/vehicle/" + color)
                .build().encode().toUri();
        return Optional.ofNullable(restTemplate.getForObject(uri, Vehicle.class)).orElse(new Vehicle());
    }

    //POST

    public void postVehicle(final Vehicle vehicleDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles")
                .build().encode().toUri();
        restTemplate.postForObject(uri, vehicleDto, Vehicle.class);
    }

    //PUT

    public void updateVehicle(final Vehicle vehicleDto) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles")
                .build().encode().toUri();
        restTemplate.put(uri, vehicleDto);
    }
    public void updateVehicleByIdAndColor(long id, String color) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles/" + id + "/" + color)
                .build().encode().toUri();
        restTemplate.put(uri, Vehicle.class);
    }

    //DELETE

    public void deleteVehicle(long id) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/vehicles/" + id)
                .build().encode().toUri();
        restTemplate.delete(uri);
    }
}
