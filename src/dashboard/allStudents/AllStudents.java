package dashboard.allStudents;

import connection.ConnectionManager;
import dashboard.DashboardController;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import javafx.util.Duration;
import model.Student;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AllStudents implements Initializable {
    private ConnectionManager manager;

    public AnchorPane paneMain;

    private AnchorPane paneRegisterStudent;


    @FXML
    TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, Integer> col_sId;
    @FXML
    private TableColumn<Student, String> col_fullName;
    @FXML
    private TableColumn<Student, String> col_phone;
    @FXML
    private TableColumn<Student, String> col_regNo;
    @FXML
    private TableColumn<Student, String> col_department;
    @FXML
    private TableColumn<Student, String> col_status;
    @FXML
    private TableColumn<Student, String> col_roomNo;
    @FXML
    private TableColumn<Student, String> col_seatNo;
    @FXML
    private TableColumn<Student, String> col_floorNo;

    private ObservableList<Student> studentList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = new ConnectionManager();

        //Load all fxmls in a cache
        try {
            paneRegisterStudent = FXMLLoader.load(getClass().getResource("/dashboard/registerStudent/RegisterStudent.fxml"));

        } catch (IOException ex) {
            Logger.getLogger(AllStudents.class.getName()).log(Level.SEVERE, null, ex);
        }

        col_sId.setCellValueFactory(new PropertyValueFactory<Student, Integer>("sId"));
        col_fullName.setCellValueFactory(new PropertyValueFactory<Student, String>("fullName"));
        col_phone.setCellValueFactory(new PropertyValueFactory<Student, String>("phone"));
        col_regNo.setCellValueFactory(new PropertyValueFactory<Student, String>("regNo"));
        col_department.setCellValueFactory(new PropertyValueFactory<Student, String>("department"));
        col_status.setCellValueFactory(new PropertyValueFactory<Student, String>("status"));
        col_roomNo.setCellValueFactory(new PropertyValueFactory<Student, String>("roomNo"));
        col_seatNo.setCellValueFactory(new PropertyValueFactory<Student, String>("seatNo"));
        col_floorNo.setCellValueFactory(new PropertyValueFactory<Student, String>("floorNo"));
        col_fullName.setCellFactory(TextFieldTableCell.forTableColumn());
        col_phone.setCellFactory(TextFieldTableCell.forTableColumn());

        col_fullName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> event) {
                System.out.println(event.getNewValue());
                System.out.println(event.getOldValue());
                event.getRowValue().setFullName(event.getNewValue());
            }
        });

        col_phone.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> event) {
                System.out.println(event.getNewValue());
                System.out.println(event.getOldValue());
                event.getRowValue().setPhone(event.getNewValue());
            }
        });

        try {
            studentList = manager.getStudent();
            studentTable.setItems(studentList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Set selected node to a content holder
    public void setNode(Node node) {
        paneMain.getChildren().clear();
        paneMain.getChildren().add((Node) node);
        FadeTransition ft = new FadeTransition(Duration.millis(500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    @FXML
    public void refresh() throws SQLException {
        studentList.clear();
        studentList=manager.getStudent();
        studentTable.setItems(studentList);

    }

   @FXML
   public void delete(ActionEvent event) {
       Student student =
               studentTable.getSelectionModel()
                       .getSelectedItem();
       try {
           System.out.println(student.getRegNo());
           manager.deleteStudent(student.getRegNo());
           studentList.remove(studentTable.getSelectionModel()
                   .getSelectedIndex());

       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

   @FXML
   public void unAllocateSeat(ActionEvent event) {
       Student student =
               studentTable.getSelectionModel()
                       .getSelectedItem();
       try {
           manager.unAllocateSeat(student.getRegNo());
           refresh();

       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

   @FXML
   public void update(ActionEvent event) {
        Student student =
                studentTable.getSelectionModel()
                        .getSelectedItem();
        try {
            System.out.println(student.getRegNo());
            manager.updateStudent(student);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public  void addNew(ActionEvent event){
        setNode(paneRegisterStudent);
    }


}

