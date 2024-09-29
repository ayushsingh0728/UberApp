package com.project.uber.uberApp.strategies.implementation;

import com.project.uber.uberApp.entities.RideRequest;
import com.project.uber.uberApp.services.DistanceService;
import com.project.uber.uberApp.strategies.RideFairCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFairDefaultFareCalculationStrategy implements RideFairCalculationStrategy {

    private final DistanceService distanceService;

    @Override
    public double calculateFare(RideRequest rideRequest) {
        Double distance=distanceService.calculateDistance(rideRequest.getPickupLocation(),rideRequest.getDropOffLocation());
        return distance*RIDE_FARE_MULTIPLIER;
    }
}
