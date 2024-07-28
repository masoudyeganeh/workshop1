package service;

import entity.*;
import repository.ConnectionProvider;
import repository.CostsDA;
import repository.ParkingDA;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ParkingService {
    private long step1;
    private long step2;
    private long step3;

    public void enter(Parking parking) throws Exception {
        try (ConnectionProvider connectionProvider = new ConnectionProvider();) {
            ParkingDA parkingDA = new ParkingDA(connectionProvider.getConnection());
            parkingDA.insert(parking);
        }
    }

    public void exit(Parking parking) {
        try (ConnectionProvider connectionProvider = new ConnectionProvider();) {
            ParkingDA parkingDA = new ParkingDA(connectionProvider.getConnection());
            CostsDA costsDA = new CostsDA(connectionProvider.getConnection());
            List<Costs> list = new ArrayList<>();
            list = costsDA.selectAll();
            Date now = new Date();
            Timestamp timestamp = new Timestamp(now.getTime());
//            timestamp = Timestamp.from(timestamp.toInstant().minus(10, ChronoUnit.HOURS));
            parkingDA.selectOneByCarId(parking);
            if (parking.getEnterTime().compareTo(timestamp) > 0)
            {
                throw new ExitTimeIsSmallerThanEnterTime();
            }
            parking.setExitTime(timestamp);
            double cost = calcCost(parking, list);
            parking.setCost(cost);
            parkingDA.update(parking);
        } catch (ExitTimeIsSmallerThanEnterTime e) {
            ExceptionWrapper.getExceptionMessage(e);
        } catch (RecordNotFoundException e) {
            ExceptionWrapper.getExceptionMessage(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isBetween(Timestamp t, Timestamp min, Timestamp max) {
        return t.getTime() > min.getTime() && t.getTime() <= max.getTime();
    }

    public double convertTimeStampToRial(Long diff,int coEfficient) {
        return  (diff * coEfficient) / (60 * 60 * 1000);
    }

    public List<Timestamp> makeTimeStampFromHour(String fromHour, String toHour) {
        Timestamp originalTimestamp = new Timestamp(System.currentTimeMillis());
        LocalDateTime originalDateTime = originalTimestamp.toLocalDateTime();
        LocalTime newTime = LocalTime.of(Integer.parseInt(fromHour.substring(0, 2)), Integer.parseInt(fromHour.substring(3, 5)));
        LocalDateTime updatedDateTime = originalDateTime.withHour(newTime.getHour()).withMinute(newTime.getMinute()).withSecond(newTime.getSecond()).withNano(newTime.getNano());
        Timestamp fromTs = Timestamp.valueOf(updatedDateTime);
        newTime = LocalTime.of(Integer.parseInt(toHour.substring(0, 2)), Integer.parseInt(toHour.substring(3, 5)));
        updatedDateTime = originalDateTime.withHour(newTime.getHour()).withMinute(newTime.getMinute()).withSecond(newTime.getSecond()).withNano(newTime.getNano());
        Timestamp toTs = Timestamp.valueOf(updatedDateTime);
        List<Timestamp> list = new ArrayList<>();
        list.add(fromTs);
        list.add(toTs);
        return list;
    }

    public double calcCost(Parking parking, List<Costs> list) {
        Map<List<Timestamp>, Double> map = new HashMap<>();
        for (Costs rec : list) {
            List<Timestamp> Ts = makeTimeStampFromHour(rec.getFromHour(), rec.getToHour());
            map.put(Ts, rec.getCostPerHour());
        }

        for (List key : map.keySet()) {
            Double value = map.get(key);
            if (isBetween(parking.getEnterTime(), (Timestamp) key.get(0), (Timestamp) key.get(1))) {
                int startingIndex = key.indexOf(value);
            }
            if (isBetween(parking.getExitTime(), (Timestamp) key.get(0), (Timestamp) key.get(1))) ;
            int endingIndex = key.indexOf(value);
        }
        Double db = 1.00;
        return db;
    }

//        if (isBetween(parking.getEnterTime(), sixHourTs, twelveHourTs)) {
//            if (isBetween(parking.getExitTime(), sixHourTs, twelveHourTs)) {
//                step1 = parking.getExitTime().getTime() - parking.getEnterTime().getTime();
//            } else if (isBetween(parking.getExitTime(), twelveHourTs, eighteenHourTs)) {
//                step2 = parking.getExitTime().getTime() - twelveHourTs.getTime();
//                step1 = twelveHourTs.getTime() - parking.getEnterTime().getTime();
//            } else if (isBetween(parking.getExitTime(), eighteenHourTs, lastHourTs)) {
//                step3 = parking.getExitTime().getTime() - eighteenHourTs.getTime();
//                step2 = eighteenHourTs.getTime() - twelveHourTs.getTime();
//                step1 = twelveHourTs.getTime() - parking.getEnterTime().getTime();
//            }
//        } else if (isBetween(parking.getEnterTime(), twelveHourTs, eighteenHourTs)) {
//            if (isBetween(parking.getExitTime(), twelveHourTs, eighteenHourTs)) {
//                step2 = parking.getExitTime().getTime() - parking.getEnterTime().getTime();
//            } else if (isBetween(parking.getExitTime(), eighteenHourTs, lastHourTs)) {
//                step3 = parking.getExitTime().getTime() - eighteenHourTs.getTime();
//                step2 = eighteenHourTs.getTime() - parking.getEnterTime().getTime();
//            }
//        } else if (isBetween(parking.getEnterTime(), eighteenHourTs, lastHourTs) &&
//                isBetween(parking.getExitTime(), eighteenHourTs, lastHourTs)) {
//            step3 = parking.getExitTime().getTime() - parking.getEnterTime().getTime();
//        }
//        return convertTimeStampToRial(step1, map.get("step1")) + convertTimeStampToRial(step2, map.get("step2")) + convertTimeStampToRial(step3, map.get("step3"));
}


