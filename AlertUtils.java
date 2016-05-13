package ro.ubbcluj.cs.keep.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by ilaza on 12/13/2015.
 */
public class AlertUtils {
    public static void showError(Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(stringWriter);
//        e.printStackTrace(printWriter);
//        alert.setContentText(stringWriter.toString());
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
    public static Dialog<Boolean> cancellableDialog(String title, String headerText) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        VBox vbox = new VBox();
        vbox.getChildren().add(new ProgressIndicator());
        dialog.getDialogPane().setContent(vbox);
        dialog.setResultConverter(dialogButton -> {
            return true;
        });
        return dialog;
    }
}
