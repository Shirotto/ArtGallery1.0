module com.gallery.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    //requires eu.hansolo.tilesfx;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires jdk.jsobject;

    opens com.gallery.gui to javafx.fxml;
    opens com.entity to org.hibernate.orm.core;
    exports com.gallery.gui;
}