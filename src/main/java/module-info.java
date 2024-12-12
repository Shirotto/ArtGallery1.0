module com.gallery.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires jdk.jsobject;

    opens com.gallery.gui to javafx.fxml;
    exports com.gallery.gui;
}