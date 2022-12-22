package br.com.gubee.interview.core.features.hero.interfaces;

import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.response.HeroCompareResp;
import br.com.gubee.interview.model.response.HeroResp;

import java.util.List;
import java.util.UUID;

public interface HeroService {
    UUID create(CreateHeroRequest createHeroRequest);

    HeroResp findById(UUID id);

    List<HeroResp> findByName(String name);

    HeroCompareResp compareTwoHeroes(UUID firstId, UUID secondId);

    List<HeroResp> findAll();

    void delete(UUID id);

    UUID update(UUID id, HeroResp hero);
}
