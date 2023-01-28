package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
       String s =  mode.toUpperCase();

        PaymentMode paymentMode = PaymentMode.CASH;
        if(s.equals("CASH")){
            paymentMode = PaymentMode.CASH;
        } else if (s.equals("CARD")) {
            paymentMode = PaymentMode.CARD;
        }else if (s.equals("UPI")){
            paymentMode = PaymentMode.UPI;
        }else{
            throw new Exception("Payment mode not detected");
        }

        Reservation reservation = reservationRepository2.findById(reservationId).get();

        int hours = reservation.getNumberOfHours();
        Spot spot = reservation.getSpot();

        int bill = hours*spot.getPricePerHour();

        if(bill != amountSent){
            throw new Exception("Insufficient Amount");
        }

        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setPaymentCompleted(Boolean.TRUE);
        payment.setPaymentMode(paymentMode);
        reservation.setPayment(payment);

        paymentRepository2.save(payment);
        reservationRepository2.save(reservation);
        return payment;
    }
}
