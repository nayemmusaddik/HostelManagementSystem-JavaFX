package dashboard;


import com.jfoenix.controls.JFXButton;
import connection.ConnectionManager;
import dashboard.allEmployees.AllEmployees;
import dashboard.allStudents.AllStudents;
import dashboard.overview.Overview;
import dashboard.revenue.Revenue;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Employee;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DashboardController implements Initializable {

    private  String userName;
    private ConnectionManager manager;

    @FXML
    public AnchorPane holder;

    @FXML
    JFXButton btnEmployees;

    @FXML
    Label lblTopUser;

    private AnchorPane overview,allStudents,payment,allocateSeat,revenue,allEmployees;

    private FXMLLoader revenueLoader=new FXMLLoader();
    private FXMLLoader overviewLoader=new FXMLLoader();
    private FXMLLoader allStudentsLoader=new FXMLLoader();
    private FXMLLoader allEmployeesLoader=new FXMLLoader();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = new ConnectionManager();
        btnEmployees.setDisable(true);
        //Load all fxmls in a cache
        try {
            allStudents = allStudentsLoader.load(getClass().getResource("/dashboard/allStudents/AllStudents.fxml").openStream());
            allEmployees = allEmployeesLoader.load(getClass().getResource("/dashboard/allEmployees/AllEmployees.fxml").openStream());
            payment = FXMLLoader.load(getClass().getResource("/dashboard/payment/Payment.fxml"));
            allocateSeat = FXMLLoader.load(getClass().getResource("/dashboard/allocateSeat/AllocateSeat.fxml"));
            overview = overviewLoader.load(getClass().getResource("/dashboard/overview/Overview.fxml").openStream());
            revenue = revenueLoader.load(getClass().getResource("/dashboard/revenue/Revenue.fxml").openStream());
            setNode(overview);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Set selected node to a content holder
    private void setNode(Node node) {
        holder.getChildren().clear();
        holder.getChildren().add((Node) node);

        FadeTransition ft = new FadeTransition(Duration.millis(500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    @FXML
    private void switchOverview(ActionEvent event) {
        Overview overview1 = (Overview) overviewLoader.getController();
        overview1.refresh();
        setNode(overview);
    }

    @FXML
    private void switchAllStudents(ActionEvent event) throws SQLException {
        AllStudents allStudents1 = (AllStudents) allStudentsLoader.getController();
        allStudents1.paneMain=holder;
        allStudents1.refresh();
        setNode(allStudents);
    }

    @FXML
    private void switchAllEmployees(ActionEvent event) throws SQLException {
        AllEmployees allEmployees1 = (AllEmployees) allEmployeesLoader.getController();
        allEmployees1.paneMain=holder;
        allEmployees1.refresh();
        setNode(allEmployees);
    }

    @FXML
    private void switchPayment(ActionEvent event) {
        setNode(payment);}

    @FXML
    private void switchAllocateSeat(ActionEvent event) {
        setNode(allocateSeat);}

    @FXML
    private void switchRevenue(ActionEvent event) throws SQLException {
        Revenue revenue1 = (Revenue) revenueLoader.getController();
        revenue1.refresh();
        setNode(revenue);}

    @FXML
    public void logout(ActionEvent event) {

        try {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setUI(String userName) throws SQLException {
        this.userName=userName;
        lblTopUser.setText("Welcome "+manager.getEmployeeByUsername(userName).getFullName());
        if (manager.getEmployeeByUsername(userName).getUserName().equals("admin")){
            btnEmployees.setDisable(false);
        }
    }

}
