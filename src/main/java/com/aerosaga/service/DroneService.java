
package com.aerosaga.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aerosaga.entity.Drone;
import com.aerosaga.repository.DroneRepository;

@Service
public class DroneService {

    private final DroneRepository droneRepository;

    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    public Drone getDroneById(Long id) {
        return droneRepository.findById(id)
                .orElse(null);
    }

    public Drone createDrone(Drone drone) {
        drone.setId(null);
        return droneRepository.save(drone);
    }
}
