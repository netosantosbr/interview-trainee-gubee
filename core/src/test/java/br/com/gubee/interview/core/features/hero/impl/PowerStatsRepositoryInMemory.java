package br.com.gubee.interview.core.features.hero.impl;

import br.com.gubee.interview.core.features.powerstats.interfaces.PowerStatsRepository;
import br.com.gubee.interview.model.PowerStats;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PowerStatsRepositoryInMemory implements PowerStatsRepository {


    List<PowerStats> powerStatsList = new ArrayList<>();

    public UUID create(PowerStats powerStats) {
        powerStats.setId(UUID.randomUUID());
        powerStats.setCreatedAt(Instant.now());
        powerStats.setUpdatedAt(Instant.now());
        powerStatsList.add(powerStats);
        return powerStats.getId();
    }

    public void delete(UUID id) {

    }
}
