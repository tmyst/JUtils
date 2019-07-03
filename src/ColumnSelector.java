import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class ColumnSelector extends Application {

    private static final String DEF_ENCODING = "MS932";
    private static final String DEF_EXTENSION = ".csv";
    private static final int DEF_NUM_HEADER = 200;
    private static final String DEF_DELIMITER = ",";
    private static final String DEF_DELIMITER_SELECTION = "Comma (,)";
    private static final String DEF_ENCODING_SELECTION = "MS932 (SHIFT_JIS)";

    private String srcFileDelimiter = DEF_DELIMITER;
    private String destFileDelimiter = DEF_DELIMITER;
    private String delimiterSelection = DEF_DELIMITER_SELECTION;

    private String srcFileEncoding = DEF_ENCODING;
    private String destFileEncoding = DEF_ENCODING;
    private String encodingSelection = DEF_ENCODING_SELECTION;

    private String srcFilePath = "";
    private String destFolderPath = "";
    private String destFileName = "";
    private String destFilePath;

    private TextField txtSrcFilePath = new TextField();
    private TextField txtDestFolderPath = new TextField();
    private TextField txtDestFileName = new TextField();
    private TextField txtMessage = new TextField();

    private ObservableList<String> headers = FXCollections.observableArrayList();
    private ObservableList<String> newHeaders = FXCollections.observableArrayList();

    private List<Integer> outputColumnIdx = new ArrayList<>();
    private ListView<String> listViewSrcFileColumns = new ListView<>();
    private ListView<String> listViewDestFileColumns = new ListView<>();

    private ObservableList<String > delimiterOptions = FXCollections.observableArrayList("Comma (,)", "Tab (\\t)", "Colon (:)", "Semicolon (;)");
    private ObservableList<String > encodingOptions = FXCollections.observableArrayList("UTF-8", "MS932 (SHIFT_JIS)");
    private List<String > delimiterList = new ArrayList<>(Arrays.asList(",", "\t", ":", ";"));
    private List<String > encodingList = new ArrayList<>(Arrays.asList("UTF-8", "MS932"));

    private ComboBox comboBoxSrcFileDelimiter = new ComboBox(delimiterOptions);
    private ComboBox comboBoxDestFileDelimiter = new ComboBox(delimiterOptions);
    private ComboBox comboBoxSrcFileEncoding = new ComboBox(encodingOptions);
    private ComboBox comboBoxDestFileEncoding = new ComboBox(encodingOptions);

    private int globalFontSize = 14;
    private String globalFontFamily = "Calibri";
    private Font globalFont = Font.font(globalFontFamily, globalFontSize);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){

        stage.setTitle("Column Selector");
        stage.setWidth(600);
        stage.setHeight(800);

        Scene scene = new Scene(new Group(), 600, 800);

        MenuBar menuBar = new MenuBar();
        generateMenu(menuBar, stage);

        //Grid and Contents 0
        GridPane grid0 = new GridPane();
        ColumnConstraints column0 = new ColumnConstraints();
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        column0.setPercentWidth(1);
        column1.setPercentWidth(39);
        column2.setPercentWidth(59);
        column3.setPercentWidth(1);
        grid0.getColumnConstraints().addAll(column0, column1, column2, column3);

        //Grid and Contents 1
        GridPane grid1 = new GridPane();
        ColumnConstraints column4 = new ColumnConstraints();
        ColumnConstraints column5 = new ColumnConstraints();
        ColumnConstraints column6 = new ColumnConstraints();
        ColumnConstraints column7 = new ColumnConstraints();
        column4.setPercentWidth(1);
        column5.setPercentWidth(69);
        column6.setPercentWidth(29);
        column7.setPercentWidth(1);
        grid1.getColumnConstraints().addAll(column4, column5, column6, column7);


        //Grid and Contents 2
        GridPane grid2 = new GridPane();
        ColumnConstraints column8 = new ColumnConstraints();
        ColumnConstraints column9 = new ColumnConstraints();
        ColumnConstraints column10 = new ColumnConstraints();
        ColumnConstraints column11 = new ColumnConstraints();
        column8.setPercentWidth(1);
        column9.setPercentWidth(25);
        column10.setPercentWidth(24);
        column11.setPercentWidth(50);
        grid2.getColumnConstraints().addAll(column8, column9, column10, column11);

        //Grid and Contents 3
        GridPane grid3 = new GridPane();
        ColumnConstraints column12 = new ColumnConstraints();
        ColumnConstraints column13 = new ColumnConstraints();
        ColumnConstraints column14 = new ColumnConstraints();
        ColumnConstraints column15 = new ColumnConstraints();
        column12.setPercentWidth(1);
        column13.setPercentWidth(49);
        column14.setPercentWidth(49);
        column15.setPercentWidth(1);
        grid3.getColumnConstraints().addAll(column12, column13, column14, column15);

        //Grid and Contents 4
        GridPane grid4 = new GridPane();
        ColumnConstraints column16 = new ColumnConstraints();
        ColumnConstraints column17 = new ColumnConstraints();
        ColumnConstraints column18 = new ColumnConstraints();
        column16.setPercentWidth(1);
        column17.setPercentWidth(98);
        column18.setPercentWidth(1);
        grid4.getColumnConstraints().addAll(column16, column17, column18);

        Separator separatorTop0 = new Separator();
        Separator separatorTop1 = new Separator();
        Separator separatorTop2 = new Separator();
        Separator separatorTop3 = new Separator();
        Separator separatorTop4 = new Separator();
        Separator separatorBottom0 = new Separator();
        Separator separatorBottom1 = new Separator();
        Separator separatorBottom2 = new Separator();
        Separator separatorBottom3 = new Separator();
        Separator separatorBottom4 = new Separator();

        separatorTop0.setVisible(false);
        separatorBottom0.setVisible(false);
        separatorTop1.setVisible(false);
        separatorBottom1.setVisible(false);
        separatorTop2.setVisible(false);
        separatorBottom2.setVisible(false);
        separatorTop3.setVisible(false);
        separatorBottom3.setVisible(false);
        separatorTop4.setVisible(false);
        separatorBottom4.setVisible(false);

        Label lblSrcFileDelimiter = new Label( "Source Delimiter (Select before reading source)");
        Label lblDestFileDelimiter = new Label("Output Delimiter");
        Label lblSrcFileEncoding = new Label("Source Encoding (Select before reading source. MS932/UTF-8)");
        Label lblDestFileEncoding = new Label("Output Encoding (MS932/UTF-8)");

        lblSrcFileDelimiter.setFont(globalFont);
        lblDestFileDelimiter.setFont(globalFont);
        lblSrcFileEncoding.setFont(globalFont);
        lblDestFileEncoding.setFont(globalFont);

        comboBoxSrcFileDelimiter.setPrefWidth(150);
        comboBoxDestFileDelimiter.setPrefWidth(150);
        comboBoxSrcFileEncoding.setPrefWidth(150);
        comboBoxDestFileEncoding.setPrefWidth(150);

        comboBoxSrcFileDelimiter.setValue(delimiterSelection);
        comboBoxDestFileDelimiter.setValue(delimiterSelection);
        comboBoxSrcFileEncoding.setValue(encodingSelection);
        comboBoxDestFileEncoding.setValue(encodingSelection);

        comboBoxSrcFileDelimiter.setOnAction((Event ev) -> { srcFileDelimiter = delimiterList.get(comboBoxSrcFileDelimiter.getSelectionModel().getSelectedIndex());});
        comboBoxDestFileDelimiter.setOnAction((Event ev) -> { destFileDelimiter = delimiterList.get(comboBoxDestFileDelimiter.getSelectionModel().getSelectedIndex());});
        comboBoxSrcFileEncoding.setOnAction((Event ev) -> { srcFileEncoding = encodingList.get(comboBoxSrcFileEncoding.getSelectionModel().getSelectedIndex());});
        comboBoxDestFileEncoding.setOnAction((Event ev) -> { destFileEncoding = encodingList.get(comboBoxDestFileEncoding.getSelectionModel().getSelectedIndex());});

        Label lblSrcFilePath = new Label("Source File Path");
        Label lblDestFolderPath = new Label("Output Folder Path");
        Label lblDestFileName = new Label("Output File Name");
        Label lblSrcColumn = new Label("Columns in Source");
        Label lblDestColumn = new Label("Select Columns in Output");

        lblSrcFilePath.setFont(globalFont);
        lblDestFolderPath.setFont(globalFont);
        lblDestFileName.setFont(globalFont);
        lblSrcColumn.setFont(globalFont);
        lblDestColumn.setFont(globalFont);

        Button getHeadersButton = new Button("Update");
        getHeadersButton.setFont(globalFont);
        getHeadersButton.setOnAction((ActionEvent e) -> {
            updateSourceFileSetting();
            clearHeaders();
            getHeaders();
        });

        listViewDestFileColumns.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewDestFileColumns.setEditable(true);
        listViewDestFileColumns.setCellFactory(TextFieldListCell.forListView());
        listViewDestFileColumns.getFocusModel().focusedIndexProperty().addListener((ov, old, current) -> {
            outputColumnIdx.clear();
            outputColumnIdx.addAll(listViewDestFileColumns.getSelectionModel().getSelectedIndices());
        });

        Button buttonExecute = new Button("Execute");
        buttonExecute.setFont(globalFont);
        buttonExecute.setOnAction((ActionEvent e) -> {
            listViewDestFileColumns.getSelectionModel().getSelectedIndices().forEach(x -> outputColumnIdx.add(x));
            newHeaders = listViewDestFileColumns.getItems();
            updateSourceFileSetting();
            updateDestFileSetting();
            executeFileWriter();
        });

        grid0.setHgap(12);
        grid0.setVgap(8);
        grid0.add(separatorTop0, 1, 0);
        grid0.add(lblSrcFilePath, 1, 1);
        grid0.add(txtSrcFilePath, 2, 1);
        grid0.add(lblDestFolderPath, 1, 2);
        grid0.add(txtDestFolderPath, 2, 2);
        grid0.add(lblDestFileName, 1, 3);
        grid0.add(txtDestFileName, 2, 3);
        grid0.add(separatorBottom0, 1, 4);

        grid1.setHgap(12);
        grid1.setHgap(8);
        grid1.add(separatorTop1, 1, 0);
        grid1.add(lblSrcFileDelimiter, 1, 1);
        grid1.add(comboBoxSrcFileDelimiter, 2, 1);
        grid1.add(lblDestFileDelimiter, 1, 2);
        grid1.add(comboBoxDestFileDelimiter, 2, 2);
        grid1.add(lblSrcFileEncoding, 1, 3);
        grid1.add(comboBoxSrcFileEncoding, 2, 3);
        grid1.add(lblDestFileEncoding, 1, 4);
        grid1.add(comboBoxDestFileEncoding, 2, 4);
        grid1.add(separatorBottom1, 1, 5);

        grid2.setHgap(12);
        grid2.setHgap(8);
        grid2.add(separatorTop2, 1, 0);
        grid2.add(lblSrcColumn, 1, 1);
        grid2.add(getHeadersButton, 2, 1);
        grid2.add(lblDestColumn, 3, 1);

        grid3.setHgap(12);
        grid3.setHgap(8);
        grid3.add(separatorTop3, 1, 0);
        grid3.add(listViewSrcFileColumns, 1, 1);
        grid3.add(listViewDestFileColumns, 2, 1);
        grid3.add(separatorBottom3, 1, 2);

        grid4.setHgap(12);
        grid4.setHgap(8);
        grid4.add(separatorTop4, 1, 0);
        grid4.add(buttonExecute, 1, 1);
        grid4.add(txtMessage, 1, 2);
        grid4.add(separatorBottom4, 1, 3);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuBar, grid0, grid1, grid2, grid3, grid4);
        vBox.prefWidthProperty().bind(scene.widthProperty());
        vBox.prefHeightProperty().bind(scene.heightProperty());
        ((Group)scene.getRoot()).getChildren().addAll(vBox);

        stage.setScene(scene);
        stage.show();
    }

    private void generateMenu(MenuBar menuBar, Stage stage){
        Menu menuFile = new Menu("Menu");
        menuFile.setStyle(
                "-fx-font-size: 12pt;" + "-fx-font-family: Calibri"
        );
        menuBar.getMenus().addAll(menuFile);

        MenuItem open = new MenuItem("Open Source File");
        open.setOnAction((ActionEvent e) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Source File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV", "*.csv"),
                    new FileChooser.ExtensionFilter( "TXT", "*.txt"),
                    new FileChooser.ExtensionFilter("TSV", "*.tsv"),
                    new FileChooser.ExtensionFilter("ALL", "*.*")
            );
            File file = fileChooser.showOpenDialog(stage);
            if (file != null){
                txtSrcFilePath.setText(file.getAbsolutePath());
                updateSourceFileSetting();
                copyParentPathFromSrc();
                clearHeaders();
                getHeaders();
            }
        });

        MenuItem outputPath = new MenuItem("Select Output Path");
        outputPath.setOnAction((ActionEvent e) -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Select Folder");
            File dir = dc.showDialog(stage);
            if( dir != null){
                destFolderPath = dir.getAbsolutePath();
                txtDestFolderPath.setText(destFolderPath);
            }
        });

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction((ActionEvent e) -> System.exit(0));
        menuFile.getItems().addAll(open, new SeparatorMenuItem(), outputPath, new SeparatorMenuItem(), exit);

    }

    private void copyParentPathFromSrc(){
        if(txtDestFolderPath.getText().isEmpty()){
            File srcFile = new File(srcFilePath);
            String srcFileDir = srcFile.getParent();
            txtDestFolderPath.setText(srcFileDir);
        }
    }

    private void updateSourceFileSetting(){
        srcFilePath = txtSrcFilePath.getText();
        srcFileEncoding = encodingList.get(comboBoxSrcFileEncoding.getSelectionModel().getSelectedIndex());
        srcFileDelimiter= delimiterList.get(comboBoxSrcFileDelimiter.getSelectionModel().getSelectedIndex());
    }

    private void updateDestFileSetting(){
        destFilePath = txtDestFolderPath.getText() + "/" + txtDestFileName.getText();
        destFileEncoding = encodingList.get(comboBoxDestFileEncoding.getSelectionModel().getSelectedIndex());
        destFileDelimiter = delimiterList.get(comboBoxDestFileDelimiter.getSelectionModel().getSelectedIndex());
    }

    private void clearHeaders(){
        headers.clear();
        newHeaders.clear();
        listViewSrcFileColumns.getItems().clear();
        listViewDestFileColumns.getItems().clear();
    }

    private void getHeaders(){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFilePath), srcFileEncoding))){
            String line = br.readLine();
            String[] strs = line.split(srcFileDelimiter);
            headers.addAll(strs);
            newHeaders.addAll(strs);
        }catch (FileNotFoundException e){
            txtMessage.setText("File Not Found: " + e.getMessage());
        }catch (IOException e){
            txtMessage.setText("IO Exception: " + e.getMessage());
        }
        listViewSrcFileColumns.setItems(headers);
        listViewDestFileColumns.setItems(newHeaders);
    }

    private void executeFileWriter(){
        LocalDateTime startTime = LocalDateTime.now();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFilePath), srcFileEncoding));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFilePath, false), destFileEncoding)))
        ){
            String line = br.readLine();
            int len = line.split(srcFileDelimiter).length;
            int[] is = IntStream.range(0, len).filter( i -> outputColumnIdx.contains(i)).toArray();
            String out = Arrays.stream(is).mapToObj(i -> newHeaders.get(i)).collect(joining(destFileDelimiter));
            pw.println(out);
            while((line = br.readLine())!=null){
                String[] strs = line.split(srcFileDelimiter);
                pw.println(Arrays.stream(is).mapToObj(i -> strs[i]).collect(joining(destFileDelimiter)));
            }
        }catch (FileNotFoundException e){
            txtMessage.setText("File Not Found: " + e.getMessage());
        }catch (IOException e){
            txtMessage.setText("IO Exception: " + e.getMessage());
        }
        LocalDateTime endTime = LocalDateTime.now();
        long timeBtw = ChronoUnit.MILLIS.between(startTime, endTime);
        txtMessage.setText(String.format("Done in %s ms.", Long.toString(timeBtw)));
    }
}
