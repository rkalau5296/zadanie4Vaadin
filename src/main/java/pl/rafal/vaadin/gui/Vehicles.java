package pl.rafal.vaadin.gui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.ButtonOptions;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
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
        vehicleGrid.setMaxWidth("800px");
        vehicleGrid.setWidth("100%");



        vehicleGrid.addComponentColumn(item-> new Button("Delete", buttonClickEvent -> {

            deleteRowFromGrid(item.getVehicleId());
            addVehiclesToGrid();

        }));

        vehicleGrid.addComponentColumn(item-> new Button("Update", buttonClickEvent -> {

            createFormLayout().open();
        }));

        Button getVehicleButton = new Button("Pobierz pojazdy");
        getVehicleButton.addClickListener(buttonClickEvent -> addVehiclesToGrid());

        integerField = new IntegerField("Podaj id pojazdu");
        Button getVehicleByIdButton = new Button("Pobierz pojazd po id");

        getVehicleByIdButton.addClickListener(buttonClickEvent ->{
            if(integerField.getValue()==null){
                Notification notification = Notification.show(
                        "Nie wprowadzono nr id. Puste pole. Wprowadź nr id.");
                add(notification);
            }
            addVehiclesByIdToGrid(integerField.getValue());
        });

        MyCustomLayout upperLayout = new MyCustomLayout();
        upperLayout.addItemWithLabel("", getVehicleByIdButton);
        upperLayout.addItemWithLabel("", getVehicleButton);
        upperLayout.addItemWithLabel("", integerField);

        MyCustomLayout gridLayout = new MyCustomLayout();
        gridLayout.addItemWithLabel("", vehicleGrid);

        add(upperLayout, gridLayout);
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

    public void deleteRowFromGrid(Long id) {
        
        url.deleteVehicle(id);
    }

    public Dialog createFormLayout() {

        Dialog dialog = new Dialog();

        IntegerField integerField = new IntegerField("Vehicle Id");
        TextField brand = new TextField("Brand");
        TextField color = new TextField("Color");
        TextField model = new TextField("Model");

        Button save = new Button("Save", buttonClickEvent -> {
            if(integerField.getValue()==null|| brand.getValue()==null || color.getValue()==null||model.getValue()==null)
            {
                Notification notification = Notification.show(
                        "Nie wprowadzono wszystkich danych. Wypełnij wszystkie pola.");
                add(notification);
            }
            url.updateVehicle(new Vehicle(integerField.getValue(), brand.getValue(), color.getValue(), model.getValue()));
            addVehiclesToGrid();
            dialog.close();
        });
        Button cancel = new Button("Cancel", buttonClickEvent -> {
            dialog.close();
        });

        MyCustomLayout upperLayout = new MyCustomLayout();

        upperLayout.addItemWithLabel("",integerField);
        upperLayout.addItemWithLabel("",brand);
        upperLayout.addItemWithLabel("",color);
        upperLayout.addItemWithLabel("",model);
        upperLayout.addItemWithLabel("",save);
        upperLayout.addItemWithLabel("",cancel);
        dialog.add(upperLayout );

        return dialog;
    }

}
