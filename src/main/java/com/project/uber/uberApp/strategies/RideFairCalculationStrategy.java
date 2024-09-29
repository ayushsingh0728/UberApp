package com.project.uber.uberApp.strategies;
import com.project.uber.uberApp.entities.RideRequest;

public interface RideFairCalculationStrategy {

    double RIDE_FARE_MULTIPLIER=10;

    double calculateFare(RideRequest rideRequest);
}