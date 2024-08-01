import entity.Parking;
import entity.RecordNotFoundException;
import service.ParkingService;
import java.util.Date;
import java.sql.Timestamp;

public class Main {
    public static void main(String[] args) {
        Date now = new Date();
        Timestamp timestamp = new Timestamp(now.getTime());
        //enter
//        Parking parking = new Parking().setId(105).setBrand("Aa").setModel("Bb").setCarId(56).setEnterTime(timestamp);
//        try {
//            ParkingService parkingService = new ParkingService();
//            parkingService.enter(parking);
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        //exit
        Parking parking1 = new Parking().setCarId(64);
        try {
            ParkingService parkingService = new ParkingService();
            parkingService.exit(parking1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
