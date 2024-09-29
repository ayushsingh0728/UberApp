package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.dto.DriverDto;
import com.project.uber.uberApp.dto.RideDto;
import com.project.uber.uberApp.dto.RideRequestDto;
import com.project.uber.uberApp.dto.RiderDto;
import com.project.uber.uberApp.entities.*;
import com.project.uber.uberApp.entities.enums.RideRequestStatus;
import com.project.uber.uberApp.entities.enums.RideStatus;
import com.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.project.uber.uberApp.repositories.RideRequestRepository;
import com.project.uber.uberApp.repositories.RiderRepository;
import com.project.uber.uberApp.services.*;
import com.project.uber.uberApp.strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RiderServiceImplementation implements RiderService {

    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final RideService rideService;
    private final DriverService driverService;
    private final RatingService ratingService;
    private final EmailSenderService emailSenderService;

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        Rider rider=getCurrentRider();
        RideRequest rideRequest=modelMapper.map(rideRequestDto,RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);

        Double fare=rideStrategyManager.rideFairCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest=rideRequestRepository.save(rideRequest);

        List<Driver> drivers=rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);

        //TODO : Send notification to all the drivers about this ride request

        String subject = "New Ride Request";
        String body = "A new ride request has been made with ID: " + rideRequest.getId() +
                ". Please check the details and accept the ride if available.";

        Set<String> emailSet = drivers.stream()
                .map(driver -> driver.getUser().getEmail())
                .collect(Collectors.toSet());
        String[] emails = emailSet.toArray(new String[0]);

        emailSenderService.sendEmail(emails, subject, body);

        return modelMapper.map(savedRideRequest,RideRequestDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Rider rider=getCurrentRider();
        Ride ride=rideService.getRideById(rideId);
        if(!rider.equals(ride.getRider()))
        {
            throw new RuntimeException("Rider does not own this ride with id "+rideId);
        }
        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED))
        {
            throw new RuntimeException("Ride cannot be cancelled, invalid status: "+ride.getRideStatus());
        }
        Ride savedRide=rideService.updateRideStatus(ride,RideStatus.CANCELLED);
        driverService.updateDriverAvailability(ride.getDriver(),true);
        return modelMapper.map(savedRide ,RideDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        Ride ride=rideService.getRideById(rideId);
        Rider rider=getCurrentRider();

        if (!rider.equals(ride.getRider()))
        {
            throw new RuntimeException("Rider is not the owner of this ride");
        }
        if (!ride.getRideStatus().equals(RideStatus.ENDED))
        {
            throw new RuntimeException("Ride status is not Ended hence cannot start rating, status: "+ride.getRideStatus());
        }

        return ratingService.rateDriver(ride,rating);
    }

    @Override
    public RiderDto getMyProfile() {
        Rider currentRider=getCurrentRider();
        return modelMapper.map(currentRider,RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Rider currentRider=getCurrentRider();
        return rideService.getAllRidesOfRider(currentRider,pageRequest).map(
                ride -> modelMapper.map(ride,RideDto.class)
        );
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider=Rider.builder()
                .user(user)
                .rating(0.0)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return riderRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException(
                "Rider not associated with user with id"+user.getId()
        ));
    }

}
