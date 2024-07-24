package service;

import entity.Parking;
import repository.ParkingDA;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class ParkingService {
    private long step1;
    private long step2;
    private long step3;

    public void enter(Parking parking) throws Exception {
        try (ParkingDA parkingDA = new ParkingDA();) {
            parkingDA.insert(parking);
            parkingDA.commit();
        }
    }

    public void exit(Parking parking) throws Exception {
        try (ParkingDA parkingDA = new ParkingDA();) {
            Date now = new Date();
            Timestamp timestamp = new Timestamp(now.getTime());
            parkingDA.selectOneByCarId(parking);
            parking.setExitTime(timestamp);
            double cost = calcCost(parking);
            parking.setCost(cost);
            parkingDA.update(parking);
            parkingDA.commit();
        }
    }

    public boolean isBetween(Timestamp t, Timestamp min, Timestamp max) {
        return t.getTime() > min.getTime() && t.getTime() <= max.getTime();
    }

    public double convertTimeStampToRial(Long diff,int coEfficient) {
        return  (diff * coEfficient) / (60 * 60 * 1000);
    }

    public double calcCost(Parking parking) {
        Map<String, Integer> map = new HashMap<>();
        map.put("step1", 2);
        map.put("step2", 4);
        map.put("step3", 3);
        Timestamp originalTimestamp = new Timestamp(System.currentTimeMillis());
        LocalDateTime originalDateTime = originalTimestamp.toLocalDateTime();
        LocalTime newTime = LocalTime.of(6, 0);
        LocalDateTime updatedDateTime = originalDateTime.withHour(newTime.getHour()).withMinute(newTime.getMinute()).withSecond(newTime.getSecond()).withNano(newTime.getNano());
        Timestamp sixHourTs = Timestamp.valueOf(updatedDateTime);
        newTime = LocalTime.of(12, 0);
        updatedDateTime = originalDateTime.withHour(newTime.getHour()).withMinute(newTime.getMinute()).withSecond(newTime.getSecond()).withNano(newTime.getNano());
        Timestamp twelveHourTs = Timestamp.valueOf(updatedDateTime);
        newTime = LocalTime.of(18, 0);
        updatedDateTime = originalDateTime.withHour(newTime.getHour()).withMinute(newTime.getMinute()).withSecond(newTime.getSecond()).withNano(newTime.getNano());
        Timestamp eighteenHourTs = Timestamp.valueOf(updatedDateTime);
        newTime = LocalTime.of(23, 59);
        updatedDateTime = originalDateTime.withHour(newTime.getHour()).withMinute(newTime.getMinute()).withSecond(newTime.getSecond()).withNano(newTime.getNano());
        Timestamp lastHourTs = Timestamp.valueOf(updatedDateTime);

        if (isBetween(parking.getEnterTime(), sixHourTs, twelveHourTs)) {
            if (isBetween(parking.getExitTime(), sixHourTs, twelveHourTs)) {
                step1 = parking.getExitTime().getTime() - parking.getEnterTime().getTime();
            } else if (isBetween(parking.getExitTime(), twelveHourTs, eighteenHourTs)) {
                step2 = parking.getExitTime().getTime() - twelveHourTs.getTime();
                step1 = twelveHourTs.getTime() - parking.getEnterTime().getTime();
            } else if (isBetween(parking.getExitTime(), eighteenHourTs, lastHourTs)) {
                step3 = parking.getExitTime().getTime() - eighteenHourTs.getTime();
                step2 = eighteenHourTs.getTime() - twelveHourTs.getTime();
                step1 = twelveHourTs.getTime() - parking.getEnterTime().getTime();
            }
        } else if (isBetween(parking.getEnterTime(), twelveHourTs, eighteenHourTs)) {
            if (isBetween(parking.getExitTime(), twelveHourTs, eighteenHourTs)) {
                step2 = parking.getExitTime().getTime() - parking.getEnterTime().getTime();
            } else if (isBetween(parking.getExitTime(), eighteenHourTs, lastHourTs)) {
                step3 = parking.getExitTime().getTime() - eighteenHourTs.getTime();
                step2 = eighteenHourTs.getTime() - parking.getEnterTime().getTime();
            }
        } else if (isBetween(parking.getEnterTime(), eighteenHourTs, lastHourTs) &&
                isBetween(parking.getExitTime(), eighteenHourTs, lastHourTs)) {
            step3 = parking.getExitTime().getTime() - parking.getEnterTime().getTime();
        }
        return convertTimeStampToRial(step1, map.get("step1")) + convertTimeStampToRial(step2, map.get("step2")) + convertTimeStampToRial(step3, map.get("step3"));
    }
}


