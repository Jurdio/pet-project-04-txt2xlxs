package com.example.demo;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.nio.file.Files;


public class HelloApplication extends Application {
    private TableView<DataModel> tableView = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Excel Parser App");


        TableColumn<DataModel, String> emailColumn = new TableColumn<>("Пошта");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<DataModel, String> passwordColumn = new TableColumn<>("Пароль");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<DataModel, String> additionalEmailColumn = new TableColumn<>("Додаткова Пошта");
        additionalEmailColumn.setCellValueFactory(new PropertyValueFactory<>("additionalEmail"));

        TableColumn<DataModel, String> yearColumn = new TableColumn<>("Рік");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        tableView.getColumns().addAll(emailColumn, passwordColumn, additionalEmailColumn, yearColumn);

        Button loadButton = new Button("Завантажити файл");
        Button saveButton = new Button("Зберегти результат");

        loadButton.setOnAction(e -> loadFile(primaryStage));
        saveButton.setOnAction(e -> saveResult(primaryStage));

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.getChildren().addAll(loadButton, saveButton, tableView);

        primaryStage.setScene(new Scene(vBox, 600, 400));
        primaryStage.show();
    }

    private void loadFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Виберіть файл для завантаження");
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            parseFile(file);
        }
    }

    private void parseFile(File file) {
        tableView.getItems().clear();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                tableView.getItems().add(new DataModel(parts[0], parts[1], parts[2], parts[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveResult(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Оберіть директорію для збереження результату");
        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if (selectedDirectory != null) {
            saveToFile(selectedDirectory);
        }
    }

    private void saveToFile(File directory) {
        try {
            // Відкриття або створення Excel-файлу
            Workbook workbook = new XSSFWorkbook();
            // Створення аркушу
            Sheet sheet = workbook.createSheet("Пошта і Пароль");

            // Отримання даних з TableView
            ObservableList<DataModel> data = tableView.getItems();

            for (int i = 0; i < data.size(); i++) {
                DataModel dataModel = data.get(i);

                // Створення рядка
                Row row = sheet.createRow(i);

                // Вставка пошти у колонку "Пошта"
                Cell cellEmail1 = row.createCell(0);
                cellEmail1.setCellValue(dataModel.getEmail());

                // Вставка паролю у колонку "Пароль"
                Cell cellPassword = row.createCell(1);
                cellPassword.setCellValue(dataModel.getPassword());

                // Вставка додаткової пошти у колонку "Додаткова Пошта"
                Cell cellEmail2 = row.createCell(2);
                cellEmail2.setCellValue(dataModel.getAdditionalEmail());

                // Вставка року у колонку "Рік"
                Cell cellYear = row.createCell(3);
                cellYear.setCellValue(dataModel.getYear());
            }

            // Збереження в файл
            try (OutputStream fileOut = Files.newOutputStream(new File(directory, "workbook.xlsx").toPath())) {
                workbook.write(fileOut);
            }

            // Закриття ресурсів
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}