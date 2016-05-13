package ro.ubbcluj.cs.keep.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ro.ubbcluj.cs.keep.KeepClientApplication;
import ro.ubbcluj.cs.keep.model.User;
import ro.ubbcluj.cs.keep.service.NoteClient;
import ro.ubbcluj.cs.keep.view.AlertUtils;

/**
 * Created by ilaza on 12/13/2015.
 */
public class LoginController {
    private static final Log LOG = LogFactory.getLog(LoginController.class);

    private final NoteClient noteClient;

    public LoginController(NoteClient noteClient) {
        this.noteClient = noteClient;
    }

    public Service<User> authService(String username, String password) {
        return new Service<User>() {
            @Override
            protected Task<User> createTask() {
                LOG.info("creating auth task");
                return new Task<User>() {
                    @Override
                    protected User call() throws Exception {
                        LOG.info("executing auth task");
                        User user = new User(username, password);
                        noteClient.setUser(user);
                        noteClient.getAll();
                        return user;
                    }
                };
            }
        };
    }
}
