package service;

import entity.*;
import repository.ConnectionProvider;
import repository.CostsDA;
import repository.FixedCostDA;
import repository.ParkingDA;

import java.sql.SQLException;
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
            FixedCostDA fixedCostDA = new FixedCostDA(connectionProvider.getConnection());
            FixedCost fixedCost = new FixedCost();
            fixedCost = fixedCostDA.selectAll();
            CostsDA costsDA = new CostsDA(connectionProvider.getConnection());
            List<Costs> list = new ArrayList<>();
            list = costsDA.selectAll();
            Date now = new Date();
            Timestamp timestamp = new Timestamp(now.getTime());
            timestamp = Timestamp.from(timestamp.toInstant().plus(12, ChronoUnit.HOURS));
            parkingDA.selectOneByCarId(parking);
            if (parking.getEnterTime().compareTo(timestamp) > 0) {
                throw new ExitTimeIsSmallerThanEnterTime();
            }
            parking.setExitTime(timestamp);
            double cost = calcCost(parking, list, fixedCost.getFixedCost());
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

    public double convertTimeStampToRial(Long diff, double coEfficient) {
        return (diff * coEfficient) / (60 * 60 * 1000);
    }

    public Costs makeTimeStampFromHour(Costs cost, Parking parking) {
        Timestamp originalEnterTimestamp = parking.getEnterTime();
        LocalDateTime originalDateTime = originalEnterTimestamp.toLocalDateTime();
        LocalTime newTime = LocalTime.of(Integer.parseInt(cost.getFromHour().substring(0, 2)), Integer.parseInt(cost.getFromHour().substring(3, 5)));
        LocalDateTime updatedDateTime = originalDateTime.withHour(newTime.getHour()).withMinute(newTime.getMinute()).withSecond(newTime.getSecond()).withNano(newTime.getNano());
        Timestamp fromTs = Timestamp.valueOf(updatedDateTime);
        newTime = LocalTime.of(Integer.parseInt(cost.getToHour().substring(0, 2)), Integer.parseInt(cost.getToHour().substring(3, 5)));
        updatedDateTime = originalDateTime.withHour(newTime.getHour()).withMinute(newTime.getMinute()).withSecond(newTime.getSecond()).withNano(newTime.getNano());
        Timestamp toTs = Timestamp.valueOf(updatedDateTime);
        cost.setFromTimeStamp(fromTs);
        cost.setToTimeStamp(toTs);
        return cost;
    }

    public double calcCost(Parking parking, List<Costs> list, double fixedcost) {
        boolean startingIndexFound = false;
        double perfCost = 0.0;
        for (Costs rec : list) {
            Costs costs = makeTimeStampFromHour(rec, parking);
            if (!startingIndexFound) {
                if (isBetween(parking.getEnterTime(), costs.getFromTimeStamp(), costs.getToTimeStamp())) {
                    startingIndexFound = true;
                    if (parking.getExitTime().compareTo(costs.getToTimeStamp()) > 0) {
                        perfCost = convertTimeStampToRial(costs.getToTimeStamp().getTime() - parking.getEnterTime().getTime(), costs.getCostPerHour());
                    } else {
                        perfCost = convertTimeStampToRial(parking.getExitTime().getTime() - parking.getEnterTime().getTime(), costs.getCostPerHour());
                        break;
                    }
                }
            } else if (isBetween(parking.getExitTime(), costs.getFromTimeStamp(), costs.getToTimeStamp())) {
                perfCost += convertTimeStampToRial(parking.getExitTime().getTime() -
                        costs.getFromTimeStamp().getTime(), costs.getCostPerHour());
                break;
            } else {
                perfCost += convertTimeStampToRial(costs.getToTimeStamp().getTime() - costs.getFromTimeStamp().getTime(), costs.getCostPerHour());
            }
        }
        return perfCost + fixedcost;
    }
}


