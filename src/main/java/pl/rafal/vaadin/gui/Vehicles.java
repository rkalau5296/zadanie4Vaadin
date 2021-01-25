package pl.rafal.vaadin.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import pl.rafal.vaadin.dto.VehicleDto;
import pl.rafal.vaadin.url.Url;

import java.util.Collection;
import java.util.List;

@Route("Vehicles")
public class Vehicles extends HorizontalLayout {

    private final Url url;
    private final Grid<VehicleDto> vehicleGrid;

    @Autowired
    public Vehicles(Url url) {

        this.url = url;
        vehicleGrid = new Grid<>(VehicleDto.class);
        Button getVehicleButton = new Button("Pobierz pojazdy");
        getVehicleButton.addClickListener(buttonClickEvent -> addVehiclesToGrid());
        add(vehicleGrid, getVehicleButton);
    }

    public void addVehiclesToGrid() {
        List<VehicleDto> vehicleDtoList = url.getVehicles();
        vehicleGrid.setItems(vehicleDtoList);
    }
}
