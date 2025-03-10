module com.gallery.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires jdk.jsobject;
    requires mysql.connector.j;
    requires javafx.swing;
    requires java.scripting;
    requires jbcrypt;

    opens com.gallery.gui to javafx.fxml;
    opens com.entity to org.hibernate.orm.core;
    exports com.gallery.gui;
}