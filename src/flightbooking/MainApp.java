package flightbooking;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final Manager manager = new Manager();

    private final ObservableList<Flight> flightTableData = FXCollections.observableArrayList();
    private final ObservableList<Ticket> ticketTableData = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        TabPane tabs = new TabPane();
        tabs.getTabs().add(createFlightsTab());
        tabs.getTabs().add(createSearchBookTab());
        tabs.getTabs().add(createTicketsTab());
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabs, 820, 520);
        stage.setTitle("Flight Booking System");
        stage.setScene(scene);
        stage.show();
    }

    private Tab createFlightsTab() {
        TextField tfNum = new TextField();
        TextField tfOrigin = new TextField();
        TextField tfDest = new TextField();
        TextField tfTime = new TextField();
        TextField tfCap = new TextField();
        TextField tfPrice = new TextField();

        tfNum.setPromptText("Flight Number");
        tfOrigin.setPromptText("Origin");
        tfDest.setPromptText("Destination");
        tfTime.setPromptText("Departure Time (e.g. 10:30)");
        tfCap.setPromptText("Capacity");
        tfPrice.setPromptText("Original Price");

        Button btnAdd = new Button("Add Flight");
        Label status = new Label();

        btnAdd.setOnAction(e -> {
            try {
                int num = Integer.parseInt(tfNum.getText().trim());
                String origin = tfOrigin.getText().trim();
                String dest = tfDest.getText().trim();
                String time = tfTime.getText().trim();
                int cap = Integer.parseInt(tfCap.getText().trim());
                double price = Double.parseDouble(tfPrice.getText().trim());

                Flight f = new Flight(num, origin, dest, time, cap, price);
                manager.addFlight(f);

                flightTableData.setAll(manager.getFlights());

                status.setText("✅ Flight added: " + f.toString());

                tfNum.clear(); tfOrigin.clear(); tfDest.clear();
                tfTime.clear(); tfCap.clear(); tfPrice.clear();

            } catch (NumberFormatException ex) {
                status.setText("❌ Enter valid numbers for flight number, capacity, and price.");
            } catch (IllegalArgumentException ex) {
                status.setText("❌ " + ex.getMessage());
            } catch (Exception ex) {
                status.setText("❌ Error: " + ex.getMessage());
            }
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, new Label("Flight Number:"), tfNum);
        form.addRow(1, new Label("Origin:"), tfOrigin);
        form.addRow(2, new Label("Destination:"), tfDest);
        form.addRow(3, new Label("Departure Time:"), tfTime);
        form.addRow(4, new Label("Capacity:"), tfCap);
        form.addRow(5, new Label("Original Price:"), tfPrice);

        VBox root = new VBox(12,
                new Label("Create Flights"),
                form,
                btnAdd,
                status,
                new Label("All Flights"),
                createFlightsTable()
        );
        root.setPadding(new Insets(16));

        return new Tab("Create Flight", root);
    }

    private Tab createSearchBookTab() {
        TextField tfSearchOrigin = new TextField();
        TextField tfSearchDest = new TextField();
        tfSearchOrigin.setPromptText("Origin");
        tfSearchDest.setPromptText("Destination");

        Button btnSearch = new Button("Search");

        TableView<Flight> resultsTable = createFlightsTable();
        resultsTable.setItems(FXCollections.observableArrayList());

        Label searchStatus = new Label();

        btnSearch.setOnAction(e -> {
            String o = tfSearchOrigin.getText().trim();
            String d = tfSearchDest.getText().trim();
            resultsTable.getItems().setAll(manager.findAvailableFlights(o, d));
            searchStatus.setText("Showing available flights from " + o + " to " + d);
        });

        TextField tfFlightNum = new TextField();
        TextField tfName = new TextField();
        TextField tfAge = new TextField();
        ComboBox<String> cbType = new ComboBox<>();
        TextField tfYears = new TextField();

        tfFlightNum.setPromptText("Flight #");
        tfName.setPromptText("Passenger Name");
        tfAge.setPromptText("Age");
        cbType.getItems().addAll("Member", "NonMember");
        cbType.setValue("NonMember");
        tfYears.setPromptText("Years of Membership (Member only)");
        tfYears.setDisable(true);

        cbType.setOnAction(e -> tfYears.setDisable(!"Member".equals(cbType.getValue())));

        Button btnBook = new Button("Book Seat");
        Label bookStatus = new Label();

        btnBook.setOnAction(e -> {
            try {
                int flightNum = Integer.parseInt(tfFlightNum.getText().trim());
                String name = tfName.getText().trim();
                int age = Integer.parseInt(tfAge.getText().trim());

                Passenger p;
                if ("Member".equals(cbType.getValue())) {
                    int years = Integer.parseInt(tfYears.getText().trim());
                    p = new Member(name, age, years);
                } else {
                    p = new NonMember(name, age);
                }

                Ticket t = manager.bookSeat(flightNum, p);
                if (t == null) {
                    bookStatus.setText("❌ Booking failed: flight not found OR no seats left.");
                    return;
                }

                flightTableData.setAll(manager.getFlights());
                ticketTableData.setAll(manager.getTickets());

                bookStatus.setText("✅ Booked: " + t.toString());

            } catch (NumberFormatException ex) {
                bookStatus.setText("❌ Enter valid numbers for flight #, age, and years (if member).");
            } catch (Exception ex) {
                bookStatus.setText("❌ Error: " + ex.getMessage());
            }
        });

        HBox searchRow = new HBox(10, tfSearchOrigin, tfSearchDest, btnSearch);
        VBox searchBox = new VBox(10, new Label("Search Available Flights"), searchRow, searchStatus, resultsTable);

        GridPane bookingForm = new GridPane();
        bookingForm.setHgap(10);
        bookingForm.setVgap(10);
        bookingForm.addRow(0, new Label("Flight #:"), tfFlightNum);
        bookingForm.addRow(1, new Label("Name:"), tfName);
        bookingForm.addRow(2, new Label("Age:"), tfAge);
        bookingForm.addRow(3, new Label("Type:"), cbType);
        bookingForm.addRow(4, new Label("Years:"), tfYears);

        VBox bookingBox = new VBox(10, new Label("Book a Seat"), bookingForm, btnBook, bookStatus);

        HBox root = new HBox(18, searchBox, bookingBox);
        root.setPadding(new Insets(16));
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        searchBox.setPrefWidth(520);

        return new Tab("Search & Book", root);
    }

    private Tab createTicketsTab() {
        TableView<Ticket> ticketTable = new TableView<>(ticketTableData);

        TableColumn<Ticket, Integer> colNum = new TableColumn<>("Ticket #");
        colNum.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Ticket, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Ticket, Passenger> colPassenger = new TableColumn<>("Passenger");
        colPassenger.setCellValueFactory(new PropertyValueFactory<>("passenger"));
        colPassenger.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Passenger p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? "" : p.getName() + " (" + p.getAge() + ")");
            }
        });

        TableColumn<Ticket, Flight> colFlight = new TableColumn<>("Flight");
        colFlight.setCellValueFactory(new PropertyValueFactory<>("flight"));
        colFlight.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Flight f, boolean empty) {
                super.updateItem(f, empty);
                setText(empty || f == null ? "" : f.getFlightNumber() + " " + f.getOrigin() + "→" + f.getDestination());
            }
        });

        ticketTable.getColumns().addAll(colNum, colPassenger, colFlight, colPrice);
        ticketTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox root = new VBox(10, new Label("Tickets Created"), ticketTable);
        root.setPadding(new Insets(16));

        return new Tab("Tickets", root);
    }

    private TableView<Flight> createFlightsTable() {
        TableView<Flight> table = new TableView<>(flightTableData);

        TableColumn<Flight, Integer> colNum = new TableColumn<>("Flight #");
        colNum.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));

        TableColumn<Flight, String> colOrigin = new TableColumn<>("Origin");
        colOrigin.setCellValueFactory(new PropertyValueFactory<>("origin"));

        TableColumn<Flight, String> colDest = new TableColumn<>("Destination");
        colDest.setCellValueFactory(new PropertyValueFactory<>("destination"));

        TableColumn<Flight, String> colTime = new TableColumn<>("Time");
        colTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));

        TableColumn<Flight, Integer> colSeats = new TableColumn<>("Seats Left");
        colSeats.setCellValueFactory(new PropertyValueFactory<>("numberOfSeatsLeft"));

        TableColumn<Flight, Double> colPrice = new TableColumn<>("Original Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("originalPrice"));

        table.getColumns().addAll(colNum, colOrigin, colDest, colTime, colSeats, colPrice);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(220);

        return table;
    }

    public static void main(String[] args) {
        launch();
    }
}
