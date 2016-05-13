package ro.ubbcluj.cs.keep;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ro.ubbcluj.cs.keep.controller.LoginController;
import ro.ubbcluj.cs.keep.controller.NoteController;
import ro.ubbcluj.cs.keep.model.User;
import ro.ubbcluj.cs.keep.service.NoteClient;
import ro.ubbcluj.cs.keep.view.NoteCrudView;
import ro.ubbcluj.cs.keep.view.WelcomeView;


public class KeepClientApplication extends Application {

    private static final Log LOG = LogFactory.getLog(KeepClientApplication.class);

    private NoteClient noteClient;
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOG.info("starting the primary stage");
        stage = primaryStage;
        noteClient = new NoteClient();
        LoginController loginController = new LoginController(noteClient);
        WelcomeView view = new WelcomeView(this, loginController);
        Scene scene = new Scene(view);
        stage.setScene(scene);
        stage.setTitle("Keep");
        LOG.info("showing the primary stage, login");
        stage.show();
    }

    public void userAuthenticated(User user) {
        LOG.info("user authenticated");
        stage.setScene(new Scene(new NoteCrudView(new NoteController(noteClient))));
    }
}
