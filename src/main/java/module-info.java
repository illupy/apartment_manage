module hust_cs {
    requires javafx.controls;
    requires javafx.fxml;

    opens hust_cs to javafx.fxml;
    exports hust_cs;
}
