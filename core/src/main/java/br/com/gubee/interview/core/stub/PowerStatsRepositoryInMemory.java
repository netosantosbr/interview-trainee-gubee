package br.com.gubee.interview.core.stub;

import br.com.gubee.interview.core.features.powerstats.interfaces.PowerStatsRepository;
import br.com.gubee.interview.model.PowerStats;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PowerStatsRepositoryInMemory implements PowerStatsRepository {

    public boolean betweenZeroAndTen(int value) {
        if(value >= 0 && value <= 10) {
            return true;
        }
        return false;
    }

    List<PowerStats> powerStatsList = new ArrayList<>();

    public UUID create(PowerStats powerStats) {
        if(betweenZeroAndTen(powerStats.getStrength()) && betweenZeroAndTen(powerStats.getAgility())
        && betweenZeroAndTen(powerStats.getDexterity()) && betweenZeroAndTen(powerStats.getIntelligence())) {
            powerStats.setId(UUID.randomUUID());
            powerStats.setCreatedAt(Instant.now());
            powerStats.setUpdatedAt(Instant.now());
            powerStatsList.add(powerStats);
            return powerStats.getId();
        }
        return null;
    }

    public void delete(UUID id) {

    }

    public PowerStats findById(UUID id) {
        for(PowerStats ps : powerStatsList) {
            if(ps.getId() == id) {
                return ps;
            }
        }
        return null;
    }
}
