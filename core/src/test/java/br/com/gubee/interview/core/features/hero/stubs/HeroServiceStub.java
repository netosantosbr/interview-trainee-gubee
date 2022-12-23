package br.com.gubee.interview.core.features.hero.stubs;

import br.com.gubee.interview.core.stub.HeroRepositoryInMemory;
import br.com.gubee.interview.core.stub.PowerStatsRepositoryInMemory;
import br.com.gubee.interview.core.features.hero.interfaces.HeroService;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.response.HeroCompareResp;
import br.com.gubee.interview.model.response.HeroResp;


import java.util.List;
import java.util.UUID;

public class HeroServiceStub implements HeroService {
    private PowerStatsRepositoryInMemory powerStatsRepository = new PowerStatsRepositoryInMemory();
    private HeroRepositoryInMemory heroRepository = new HeroRepositoryInMemory(powerStatsRepository);


    public UUID create(CreateHeroRequest createHeroRequest) {
        return heroRepository.create(new Hero(createHeroRequest,
                powerStatsRepository.create(new PowerStats(createHeroRequest))));
    }

    public HeroResp findById(UUID id){
        return heroRepository.findById(id);
    }

    public List<HeroResp> findByName(String name){
        return heroRepository.findByName(name);
    }

    public HeroCompareResp compareTwoHeroes(UUID firstId, UUID secondId){
        return heroRepository.compareTwoHeroes(firstId, secondId);
    }

    public List<HeroResp> findAll(){
        return heroRepository.findAll();
    }


    public void delete(UUID id){
        UUID powerStatsIdFromHero = heroRepository.findPowerStatsIdFromHero(id);
        heroRepository.delete(id);
        powerStatsRepository.delete(powerStatsIdFromHero);
    }


    public UUID update(UUID id, HeroResp hero){
        return heroRepository.update(id, hero);
    }
}
