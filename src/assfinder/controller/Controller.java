package assfinder.controller;

import assfinder.Main;
import assfinder.model.AssField;
import assfinder.model.AssFreqHolder;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Map;

public class Controller {

    @FXML   TableView<Map.Entry<SimpleStringProperty, SimpleIntegerProperty>> tableKeywords;
    @FXML   TableColumn<Map.Entry<SimpleStringProperty, SimpleIntegerProperty>, String> tableKeywordsColumn;
    @FXML   TableColumn<Map.Entry<SimpleStringProperty, SimpleIntegerProperty>, Number> tableKeywordsFreqColumn;

    @FXML   TableView<Map.Entry<SimpleStringProperty, SimpleIntegerProperty>> tableAssFreq;
    @FXML   TableColumn<Map.Entry<SimpleStringProperty, SimpleIntegerProperty>, String> tableAssColumn;
    @FXML   TableColumn<Map.Entry<SimpleStringProperty, SimpleIntegerProperty>, Number> tableFreqColumn;
    @FXML   TextArea textArea;

    @FXML   TableView<AssField> assFieldTableView;
    @FXML   TableColumn<AssField, String> aftvKeyword;
    @FXML   TableColumn<AssField, Integer> aftvFreq;
    @FXML   TableColumn<AssField, String> aftvAss;
    @FXML   TableColumn<AssField, Integer> aftvDepth;

    @FXML   Button btnFind;
    @FXML   Button btnBuild;
    @FXML   TextField textField;
    @FXML   TextField minFreq;
    @FXML   TextField minDepth;
    @FXML   CheckBox checkBoxOftAss;
    @FXML   Label keywordLabel;

    private final String NUMBER_MATCHER = "^-?\\d+$";

    private File saveFile = new File("assfindersave.bin");
    private File initialDir;
    private AssFreqHolder assFreqHolder = new AssFreqHolder();

    @FXML
    public void initialize() {
        minFreq.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (newValue.matches(NUMBER_MATCHER))
                    minFreq.setText(newValue);
                else {
                    try {
                        int value = Integer.parseInt(newValue);
                        minFreq.setText(newValue);
                    } catch (NumberFormatException ex) {
                        minFreq.setText(oldValue);
                    }
                }
            }
        });
        minDepth.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (newValue.matches(NUMBER_MATCHER))
                    minDepth.setText(newValue);
                else {
                    try {
                        int value = Integer.parseInt(newValue);
                        minDepth.setText(newValue);
                    } catch (NumberFormatException ex) {
                        minDepth.setText(oldValue);
                    }
                }
            }
        });
        btnFind.setDisable(true);
        btnBuild.setDisable(true);
        if (saveFile.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));
                initialDir = (File) ois.readObject();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void onLoadTableClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить...");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Файл анализатора AssFinder", "*.aff");
        fileChooser.getExtensionFilters().add(extensionFilter);
        if (initialDir!=null) fileChooser.setInitialDirectory(initialDir);
        File file = fileChooser.showOpenDialog(Main.getMainStage());
        if (file!=null) {
            initialDir = file.getParentFile();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile));
                oos.writeObject(initialDir);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            assFreqHolder.loadFromFile(file);
            tableKeywordsColumn.setCellValueFactory(param -> param.getValue().getKey());
            tableKeywordsFreqColumn.setCellValueFactory(param -> param.getValue().getValue());
            tableKeywords.setItems(assFreqHolder.getKeywordsProperty());
            tableKeywords.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
                tableAssFreq.setItems(assFreqHolder.getAssProperty(newValue.getKey().getValue()));
                keywordLabel.setText(newValue.getKey().getValue());
            }));
            tableKeywords.setOnMouseClicked(event -> {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){
                        textArea.appendText(tableKeywords.getSelectionModel().selectedItemProperty().getValue().getKey().get());
                        textArea.appendText(" ");
                    }
                }
            });
            tableAssColumn.setCellValueFactory(param -> param.getValue().getKey());
            tableFreqColumn.setCellValueFactory(param -> param.getValue().getValue());
            tableAssFreq.setOnMouseClicked(event -> {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){
                        textArea.appendText(tableAssFreq.getSelectionModel().selectedItemProperty().getValue().getKey().get());
                        textArea.appendText(" ");
                    }
                }
            });
            btnFind.setDisable(false);
            btnBuild.setDisable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode()==KeyCode.ENTER)
                    Platform.runLater(this::onFindClick);
            });
        }
    }

    @FXML
    public void onCopyInBufferClick() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(textArea.getText());
        clipboard.setContent(content);
    }

    @FXML
    public void onFindClick() {
        if (assFreqHolder.isContainKeyword(textField.getText())) {
            keywordLabel.setText(textField.getText());
            tableAssFreq.setItems(assFreqHolder.getAssProperty(textField.getText()));
        }
    }

    @FXML
    public void onBuildClick() {
        ObservableList<AssField> list = assFreqHolder.getAssField(Integer.parseInt(minFreq.getText()), Integer.parseInt(minDepth.getText()), checkBoxOftAss.isSelected());
        aftvKeyword.setCellValueFactory(new PropertyValueFactory<>("keyword"));
        aftvFreq.setCellValueFactory(new PropertyValueFactory<>("keywordfreq"));
        aftvAss.setCellValueFactory(new PropertyValueFactory<>("ass"));
        aftvDepth.setCellValueFactory(new PropertyValueFactory<>("assfreq"));
        assFieldTableView.setItems(list);
    }

}
