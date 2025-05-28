module org.personnal.gestmat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;


    opens org.personnal.gestmat to javafx.fxml;
    exports org.personnal.gestmat;
}