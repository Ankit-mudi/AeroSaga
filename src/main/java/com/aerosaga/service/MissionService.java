
package com.aerosaga.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aerosaga.entity.Mission;
import com.aerosaga.repository.MissionRepository;
import com.aerosaga.exception.ResourceNotFoundException;

@Service
public class MissionService {

    private final MissionRepository missionRepository;

    public MissionService(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    public Mission getMissionById(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Mission not found with id: " + id
                    )
                );
    }

    public Mission createMission(Mission mission) {
        mission.setId(null);

        if (mission.getStatus() == null
                || mission.getStatus().isBlank()) {
            mission.setStatus("PENDING");
        }

        return missionRepository.save(mission);
    }

    public List<Mission> getMissionsByStatus(String status) {
        return missionRepository.findByStatus(status);
    }

    public Mission updateMission(Long id, Mission updatedMission) {
        Mission existingMission = missionRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Mission not found with id: " + id
                    )
                );

        existingMission.setMissionName(updatedMission.getMissionName());
        existingMission.setDestination(updatedMission.getDestination());
        existingMission.setStatus(updatedMission.getStatus());

        return missionRepository.save(existingMission);
    }

    public void deleteMission(Long id) {
        if (!missionRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                "Mission not found with id: " + id
            );
        }

        missionRepository.deleteById(id);
    }
}
