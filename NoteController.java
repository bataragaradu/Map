package ro.ubbcluj.cs.keep.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ro.ubbcluj.cs.keep.model.Note;
import ro.ubbcluj.cs.keep.model.User;
import ro.ubbcluj.cs.keep.service.NoteClient;

public class NoteController {

    private static final Log LOG = LogFactory.getLog(LoginController.class);

    private final NoteClient noteClient;

    public NoteController(NoteClient noteClient) {
        this.noteClient = noteClient;
    }

    public Service<Note[]> fetchNotesService() {
        return new Service<Note[]>() {
            @Override
            protected Task<Note[]> createTask() {
                LOG.info("creating get all task");
                return new Task<Note[]>() {
                    @Override
                    protected Note[] call() throws Exception {
                        LOG.info("executing get all task");
                        return noteClient.getAll();
                    }
                };
            }
        };
    }

    public Service<Note> createNoteService(String noteText) {
        return new Service<Note>() {
            @Override
            protected Task<Note> createTask() {
                LOG.info("creating create task");
                return new Task<Note>() {
                    @Override
                    protected Note call() throws Exception {
                        LOG.info("executing create task");
                        Note note = new Note(noteText);
                        return noteClient.create(note);
                    }
                };
            }
        };

    }
}
