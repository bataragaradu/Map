package ro.ubbcluj.cs.keep.view;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ro.ubbcluj.cs.keep.KeepClientApplication;
import ro.ubbcluj.cs.keep.controller.LoginController;
import ro.ubbcluj.cs.keep.model.User;

import java.util.Optional;

public class WelcomeView extends VBox {

    private static final Log LOG = LogFactory.getLog(WelcomeView.class);

    // ui constants
    public static final String LOGIN = "Login";
    private static final String CANCEL = "Cancel";

    // app and controller
    private KeepClientApplication application;
    private LoginController loginController;

    // fields used by several methods
    private TextField username;
    private PasswordField password;
    private Button authButton;
    private ProgressIndicator progressIndicator;

    // background service
    private Service<User> authService;

    public WelcomeView(KeepClientApplication application, LoginController loginController) {
        this.application = application;
        this.loginController = loginController;
        // build views
        ObservableList<Node> children = getChildren();
        // title
        children.add(new Text("Welcome"));
        // username (label + textfield)
        children.add(new Label("Username:"));
        username = new TextField();
        children.add(username);
        // password (label + textfield)
        children.add(new Label("Password:"));
        password = new PasswordField();
        children.add(password);
        // auth button (login or cancel)
        authButton = new Button(LOGIN);
        authButton.setOnAction(authActionHandler);
        children.add(authButton);
        // progress indicator
        progressIndicator = new ProgressIndicator();
        children.add(progressIndicator);
        // set pre-authenticating state
        setState(LOGIN);
    }

    private void setState(String authText) {
        authButton.setText(authText);
        boolean authenticating = authText.equals(CANCEL);
        progressIndicator.setVisible(authenticating);
    }

    private final EventHandler<ActionEvent> authActionHandler = btnEvent -> {
        String authButtonText = authButton.getText();
        LOG.info(authButtonText + " button triggered");
        if (authButtonText.equals(LOGIN)) {
            setState(CANCEL);
            authService = loginController.authService(username.getText(), password.getText()); // just a reference to an async call/task
            authService.setOnSucceeded(e -> { // prepare what to do when the call succeeds
                User user = (User) e.getSource().getValue();
                LOG.info("auth service succeeded, " + user); // executed on app thread
                setState(LOGIN);
                this.application.userAuthenticated(user);
            });
            authService.setOnFailed(e -> { // prepare what to do when the call fails
                setState(LOGIN); // executed on app thread
                Throwable exception = e.getSource().getException();
                LOG.warn("auth service failed", exception);
                AlertUtils.showError(exception);
            });
            authService.setOnCancelled(e -> { // prepare what to do when the call was cancelled
                setState(LOGIN); // executed on app thread
                LOG.info("auth service cancelled");
            });
            LOG.info("starting auth service");
            authService.start(); // start the async call/task (from app thread)
            // the task is executed on background threads
        } else {
            if (authService != null) {
                authService.cancel(); // cancel the call from app thread
                setState(LOGIN);
            }
        }
    };
}
