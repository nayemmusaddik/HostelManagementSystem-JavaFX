package dashboard.overview;

import connection.ConnectionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Overview implements Initializable {
    private ConnectionManager manager;
    @FXML
    Label lblAvailableSeat;
    @FXML
    Label lblTotalStudent;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = new ConnectionManager();
        try {
            lblAvailableSeat.setText(manager.getAvailableSeatCount());
            lblTotalStudent.setText(manager.getTotalStudentCount());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void refresh(){
        try {
            lblAvailableSeat.setText(manager.getAvailableSeatCount());
            lblTotalStudent.setText(manager.getTotalStudentCount());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
