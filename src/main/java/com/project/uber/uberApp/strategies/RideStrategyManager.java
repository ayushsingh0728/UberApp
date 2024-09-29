package com.project.uber.uberApp.strategies;

import com.project.uber.uberApp.strategies.implementation.DriverMatchingHighestRatedDriverStrategy;
import com.project.uber.uberApp.strategies.implementation.DriverMatchingNearestDriverStrategy;
import com.project.uber.uberApp.strategies.implementation.RideFairDefaultFareCalculationStrategy;
import com.project.uber.uberApp.strategies.implementation.RideFareSurgePricingFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;
    private final RideFairDefaultFareCalculationStrategy defaultFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(double riderRating){
        if (riderRating>=4.8)
        {
            return highestRatedDriverStrategy;
        }
        else
        {
            return nearestDriverStrategy;
        }
    }
    public RideFairCalculationStrategy rideFairCalculationStrategy(){

        //peek hour 6PM-9PM

        LocalTime surgeStartTime=LocalTime.of(18,0);
        LocalTime surgeEndTime=LocalTime.of(21,0);
        LocalTime currentTime=LocalTime.now();
        boolean isSurgeTime=currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);
        if (isSurgeTime)
        {
            return surgePricingFareCalculationStrategy;
        }
        else
        {
            return defaultFareCalculationStrategy;
        }
    }
}
