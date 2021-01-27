package pl.rafal.vaadin.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.rafal.vaadin.dto.Vehicle;
import pl.rafal.vaadin.url.Url;

@Route("Vehicles")
public class Vehicles extends VerticalLayout {

    private final Url url;
    private final Grid<Vehicle> vehicleGrid;
    private final IntegerField integerField;


    @Autowired
    public Vehicles(Url url){

        this.url = url;
        vehicleGrid = new Grid<>(Vehicle.class);

        Button getVehicleButton = new Button("Pobierz pojazdy");
        getVehicleButton.setAutofocus(true);
        getVehicleButton.addClickListener(buttonClickEvent -> addVehiclesToGrid());


        integerField = new IntegerField("Podaj id pojazdu");
        Button getVehicleByIdButton = new Button("Pobierz pojazd po id");
        getVehicleByIdButton.setAutofocus(true);
        getVehicleByIdButton.addClickListener(buttonClickEvent ->{
                    if(integerField.getValue()==null){
                        Notification notification = Notification.show(
                                "Nie wprowadzono nr id. Puste pole. Wprowadź nr id.");
                        add(notification);
                    }
                    addVehiclesByIdToGrid(integerField.getValue());
                });


        add(getVehicleButton, vehicleGrid, getVehicleByIdButton, integerField );
    }

    public void addVehiclesToGrid(){
        vehicleGrid.setItems(url.getVehicles());
    }

    public void addVehiclesByIdToGrid(int id) {
        try{
            url.getVehicleById(id);
            vehicleGrid.setItems(url.getVehicleById(id));
        }
        catch (Exception e){

            Notification notification = Notification.show(
                    "Id z poza zakresu, nie ma takiego id. Podaj prawidłowe id.");
            add(notification);
        }
    }







}
