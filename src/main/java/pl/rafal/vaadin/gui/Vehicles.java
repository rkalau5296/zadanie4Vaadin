package pl.rafal.vaadin.gui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
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
            deleteDialog(item.getVehicleId()).open();
        }));

        vehicleGrid.addComponentColumn(item-> new Button("Update", buttonClickEvent -> {
            updateVehicleDialog().open();
        }));

        Button getVehicleButton = new Button("Pobierz pojazdy");
        getVehicleButton.addClickListener(buttonClickEvent -> addVehiclesToGrid());

        integerField = new IntegerField("Podaj id pojazdu");
        Button getVehicleByIdButton = new Button("Pobierz pojazd po id");

        getVehicleByIdButton.addClickListener(buttonClickEvent -> {
            if(integerField.getValue()==null){
                Notification notification = Notification.show(
                        "Nie wprowadzono nr id. Puste pole. Wprowadź nr id.");
                add(notification);
            }
            addVehiclesByIdToGrid(integerField.getValue());
        });

        TextField colorField = new TextField("Podaj kolor pojazdu");
        Text emptyText = new Text("   ");

        Button colorButton = new Button("Pobierz pojazd po kolorze");
        colorButton.addClickListener(buttonClickEvent -> {
            addVehiclesByColorToGrid(colorField.getValue());
        });


        Button newVehicle = new Button("New vehicle");
        newVehicle.addClickListener(buttonClickEvent -> addNewVehicleDialog().open());

        MyCustomLayout upperLayout = new MyCustomLayout();
        upperLayout.addItemWithLabel("", getVehicleByIdButton);
        upperLayout.addItemWithLabel("", getVehicleButton);
        upperLayout.addItemWithLabel("", integerField);

        MyCustomLayout anotherUpperLayout = new MyCustomLayout();
        anotherUpperLayout.addItemWithLabel("", colorButton);
        anotherUpperLayout.addItemWithLabel("", emptyText);
        anotherUpperLayout.addItemWithLabel("", colorField);

        MyCustomLayout gridLayout = new MyCustomLayout();
        gridLayout.addItemWithLabel("", vehicleGrid);

        MyCustomLayout lowerLayout = new MyCustomLayout();
        lowerLayout.addItemWithLabel("", newVehicle);

        add(upperLayout, anotherUpperLayout, gridLayout, lowerLayout);
    }

    public void addVehiclesToGrid(){
        vehicleGrid.setItems(url.getVehicles());
    }

    public void addVehiclesByIdToGrid(long id) {
        try{
            vehicleGrid.setItems(url.getVehicleById(id));
        }
        catch (Exception e){
            Notification notification = Notification.show(
                    "Id z poza zakresu, nie ma takiego id. Podaj prawidłowe id.");
            add(notification);
        }
    }

    public void addVehiclesByColorToGrid(String color) {
        try{
            vehicleGrid.setItems(url.getVehicleByColor(color));
        }
        catch (Exception e){
            Notification notification = Notification.show(
                    "Nie ma pojazdu z podanym kolorze. Podaj prawidłowy kolor");
            add(notification);
        }
    }

    public Dialog updateVehicleDialog() {

        Dialog dialog = new Dialog(new Text("Update vehicle"));

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
        Button abort = new Button("Cancel", buttonClickEvent -> {
            dialog.close();
        });

        MyCustomLayout layout1 = new MyCustomLayout();

        layout1.addItemWithLabel("",integerField);
        layout1.addItemWithLabel("",brand);
        layout1.addItemWithLabel("",color);
        layout1.addItemWithLabel("",model);
        layout1.addItemWithLabel("",abort);
        layout1.addItemWithLabel("",save);

        dialog.add(layout1 );

        return dialog;
    }
    public Dialog addNewVehicleDialog() {

        Dialog dialog = new Dialog(new Text("Add new vehicle"));

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
            url.postVehicle(new Vehicle(integerField.getValue(), brand.getValue(), color.getValue(), model.getValue()));
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
        upperLayout.addItemWithLabel("", cancel);
        upperLayout.addItemWithLabel("",save);

        dialog.add(upperLayout );

        return dialog;
    }

    public Dialog deleteDialog(Long id) {

        Dialog dialog = new Dialog(new Text("Delete vehicle"));
        Text deleteTextConfirmation = new Text("Are you sure you want to delete this vehicle from list?");
        Text emptyText = new Text("   ");
        Button delete = new Button("Delete", buttonClickEvent -> {
            url.deleteVehicle(id);
            addVehiclesToGrid();
            dialog.close();
        });
        Button cancel = new Button("Cancel", buttonClickEvent -> {
            dialog.close();
        });

        MyCustomLayout layout = new MyCustomLayout();
        layout.addItemWithLabel("", deleteTextConfirmation);
        layout.addItemWithLabel("", emptyText);
        layout.addItemWithLabel("", cancel);
        layout.addItemWithLabel("", delete);

        dialog.add(layout);

        return dialog;
    }
}
