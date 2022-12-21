package br.com.gubee.interview.core.features.powerstats.interfaces;

import br.com.gubee.interview.model.PowerStats;

import java.util.UUID;

public interface PowerStatsRepository {
    UUID create(PowerStats powerStats);
    void delete(UUID id);
}
