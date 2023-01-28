package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        ParkingLot parkingLot = null;
        parkingLot = parkingLotRepository3.findById(parkingLotId).get();

        List<Spot> spotList = parkingLot.getSpotList();

        Spot spot = null;

        for(Spot spot1 : spotList){
            if(spot1.getOccupied() == Boolean.FALSE && numberOfWheels == 2){
                if(spot == null || spot1.getPricePerHour()*timeInHours < spot.getPricePerHour()*timeInHours){
                    spot = spot1;
                }
            }

            if(spot1.getOccupied() == Boolean.FALSE && numberOfWheels == 4){
                if(spot == null || (spot1.getSpotType() != SpotType.TWO_WHEELER && spot1.getPricePerHour()*timeInHours < spot.getPricePerHour()*timeInHours)){
                    spot = spot1;
                }
            }else{
                if(spot == null || (spot1.getSpotType() == SpotType.OTHERS && spot1.getPricePerHour()*timeInHours < spot.getPricePerHour()*timeInHours)){
                    spot = spot1;
                }
            }
        }

        spot.setOccupied(Boolean.TRUE);

        User user = null;
        user = userRepository3.findById(userId).get();

        if(user == null || spot == null || parkingLot == null){
            throw new Exception("Cannot make reservation");
        }

        Reservation reservation = new Reservation();
        reservation.setSpot(spot);
        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);
        user.getReservationList().add(reservation);
        parkingLot.getSpotList().add(spot);


        userRepository3.save(user);
        parkingLotRepository3.save(parkingLot);

        return reservation;
    }
}
