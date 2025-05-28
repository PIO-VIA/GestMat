module org.personnal.gestmat {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.personnal.gestmat to javafx.fxml;
    exports org.personnal.gestmat;
}