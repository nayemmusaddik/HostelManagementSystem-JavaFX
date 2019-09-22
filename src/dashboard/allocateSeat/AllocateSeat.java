package dashboard.allocateSeat;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import connection.ConnectionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Seat;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AllocateSeat implements Initializable {
    private ConnectionManager manager;
    @FXML
    Label seatAllocationStatus;

    @FXML
    JFXTextField regNoAllocate;

    @FXML
    private JFXComboBox<String> floorNo;
    @FXML
    private JFXComboBox<String> roomNo;
    @FXML
    private JFXComboBox<String> seatNo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = new ConnectionManager();
        try {
            floorNo.setPromptText("Select a Floor");
            floorNo.setItems(manager.getFloorNo());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        floorNo.valueProperty().addListener((obs, oldValue, newValue) -> {
            try {
                roomNo.setPromptText("Select Room");
                roomNo.setItems(manager.getRoomNo(newValue));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        roomNo.valueProperty().addListener((obs, oldValue, newValue) -> {
            try {
                seatNo.setPromptText("Select Available Seat No");
                seatNo.setItems(manager.getSeatNo(newValue));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    private  void  allocateSeat() throws SQLException {

        if(validationAllocateSeat()){
            try {
                Seat seat = new Seat();
                seat.setRegNo(regNoAllocate.getText());
                seat.setRoomNo(roomNo.getSelectionModel().getSelectedItem());
                seat.setSeatNo(seatNo.getSelectionModel().getSelectedItem());
                manager.allocateSeat(seat);
                seatAllocationStatus.setText("Success");
                seatAllocationStatus.setStyle("-fx-text-fill: green;");
                clearData();
            } catch (SQLException e) {
                seatAllocationStatus.setText(e.toString());
                seatAllocationStatus.setStyle("-fx-text-fill: red;");            }
        }

    }

    private boolean validationAllocateSeat() throws SQLException {
        if (regNoAllocate.getText().equals("")){
            seatAllocationStatus.setText("Reg. No Empty");
            seatAllocationStatus.setStyle("-fx-text-fill: red;");
            return false;
        }else if(seatNo.getSelectionModel().isEmpty()){
            seatAllocationStatus.setText("Seat Empty");
            seatAllocationStatus.setStyle("-fx-text-fill: red;");
            return false;
        }else if (manager.getSeatByReg(regNoAllocate.getText()).getSeatNo() != null){
            seatAllocationStatus.setText("Student Already has a Seat");
            seatAllocationStatus.setStyle("-fx-text-fill: red;");
            return false;
        } else if (manager.getStudentByReg(regNoAllocate.getText()).getRegNo() == null){
            seatAllocationStatus.setText("No Student Found!");
            seatAllocationStatus.setStyle("-fx-text-fill: red;");
            return false;
        } else if (manager.getStudentByReg(regNoAllocate.getText()).getStatus().equals("Due")){
            seatAllocationStatus.setText("Please Pay Your Fees!");
            seatAllocationStatus.setStyle("-fx-text-fill: red;");
            return false;
        }else {
            return true;
        }

    }
    private void clearData(){
        regNoAllocate.clear();

    }
}
