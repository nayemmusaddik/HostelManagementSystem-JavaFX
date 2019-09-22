package dashboard.allEmployees;

import connection.ConnectionManager;
import dashboard.allStudents.AllStudents;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import model.Employee;
import model.Student;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AllEmployees implements Initializable {
    private ConnectionManager manager;

    public AnchorPane paneMain;

    private AnchorPane paneRegisterEmployee;


    @FXML
    TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> col_phone;
    @FXML
    private TableColumn<Employee, String> col_fullName;

    private ObservableList<Employee> employeeList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = new ConnectionManager();
        //Load all fxmls in a cache
        try {
            paneRegisterEmployee = FXMLLoader.load(getClass().getResource("/dashboard/registerEmployee/RegisterEmployee.fxml"));

        } catch (IOException ex) {
            Logger.getLogger(AllStudents.class.getName()).log(Level.SEVERE, null, ex);
        }

        col_fullName.setCellValueFactory(new PropertyValueFactory<Employee, String>("fullName"));
        col_phone.setCellValueFactory(new PropertyValueFactory<Employee, String>("phone"));

        try {
            employeeList = manager.getEmployee();
            employeeTable.setItems(employeeList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setNode(Node node) {
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
    public  void addNew(ActionEvent event){
        setNode(paneRegisterEmployee);
    }
    @FXML
    public void refresh() throws SQLException {
        employeeList.clear();
        employeeList=manager.getEmployee();
        employeeTable.setItems(employeeList);

    }

    @FXML
    public void delete(ActionEvent event) {
        Employee employee =
                employeeTable.getSelectionModel()
                        .getSelectedItem();
        try {
            System.out.println(employee.getPhone());
            manager.deleteEmployee(employee.getPhone());
            employeeList.remove(employeeTable.getSelectionModel()
                    .getSelectedIndex());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
