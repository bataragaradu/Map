package ro.ubbcluj.cs.keep.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ro.ubbcluj.cs.keep.controller.NoteController;
import ro.ubbcluj.cs.keep.model.Note;

import java.util.ArrayList;

public class NoteCrudView extends VBox {

    private static final Log LOG = LogFactory.getLog(NoteCrudView.class);

    private  TextField noteText;
    private final ProgressIndicator progressIndicator;
    private final TextField filmDescriptionText;
    private final TextField filmTitleText;
    private final TextField filmYearText;

    private NoteController noteController;
    private ListView<Note> listView;
    private ObservableList<Note> observableList;

    public NoteCrudView(NoteController noteController) {
        this.noteController = noteController;
        LOG.info("showing empty note list");
        ObservableList<Node> viewChildren = getChildren();
        // enter noteText (label, textfield, button)
        HBox hBox = new HBox();
        ObservableList<Node> hBoxChildren = hBox.getChildren();

        hBoxChildren.add(new Label("Title:"));
        filmTitleText = new TextField();
        hBoxChildren.add(filmTitleText);

        hBoxChildren.add(new Label("Year:"));
        filmYearText = new TextField();
        hBoxChildren.add(filmYearText);

        hBoxChildren.add(new Label("Description:"));
        filmDescriptionText = new TextField("Description");
        hBoxChildren.add(filmDescriptionText);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addNote());
        hBoxChildren.add(addButton);
        viewChildren.add(hBox);
        // progress indicator
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        viewChildren.add(progressIndicator);
        // list view
        listView = new ListView<Note>();
        observableList = FXCollections.observableArrayList(new ArrayList<>());
        listView.setItems(observableList);
        listView.getSelectionModel().selectedItemProperty().addListener(
                (lv, oldValue, newValue) -> {
                    LOG.info(String.format("list view, old sel %s, new sel %s", oldValue, newValue));
                }
        );
        viewChildren.add(listView);
        // add context menu
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(new MenuItem("Delete"));
        listView.setContextMenu(contextMenu);
        fetchNotes();
    }

    private void fetchNotes() {
        LOG.info("fetching notes");
        progressIndicator.setVisible(true);
        Service<Note[]> fetchService = noteController.fetchNotesService(); // just a reference to an async call/task
        fetchService.setOnSucceeded(e -> { // prepare what to do when the call succeeds
            LOG.info("fetch service succeeded"); // executed on app thread
            progressIndicator.setVisible(false);
            observableList.addAll(fetchService.getValue());
        });
        fetchService.setOnFailed(e -> { // prepare what to do when the call fails
            Throwable exception = e.getSource().getException();
            LOG.warn("fetch service failed", exception);
            progressIndicator.setVisible(false);
            AlertUtils.showError(exception);
        });
        fetchService.setOnCancelled(e -> { // prepare what to do when the call was cancelled
            progressIndicator.setVisible(false);
            LOG.info("fetch service cancelled");
        });
        LOG.info("starting fetch service");
        fetchService.start(); // start the async call/task (from app thread)
        // the task is executed on background threads
    }

    private void addNote() {
        LOG.info("add note");
        progressIndicator.setVisible(true);
        Service<Note> createNoteService = noteController.createNoteService(noteText.getText()); // just a reference to an async call/task
        createNoteService.setOnSucceeded(e -> { // prepare what to do when the call succeeds
            LOG.info("create service succeeded"); // executed on app thread
            progressIndicator.setVisible(false);
            observableList.add(createNoteService.getValue());
        });
        createNoteService.setOnFailed(e -> { // prepare what to do when the call fails
            Throwable exception = e.getSource().getException();
            LOG.warn("create service failed", exception);
            progressIndicator.setVisible(false);
            AlertUtils.showError(exception);
        });
        createNoteService.setOnCancelled(e -> { // prepare what to do when the call was cancelled
            progressIndicator.setVisible(false);
            LOG.info("create service cancelled");
        });
        LOG.info("starting create service");
        createNoteService.start(); // start the async call/task (from app thread)
        // the task is executed on background threads
    }
}
