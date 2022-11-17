package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.powerstats.PowerStatsRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.response.HeroCompareResp;
import br.com.gubee.interview.model.response.HeroResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final PowerStatsRepository powerStatsRepository;
    private final HeroRepository heroRepository;

    @Transactional
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

    @Transactional
    public void delete(UUID id){
        UUID powerStatsIdFromHero = heroRepository.findPowerStatsIdFromHero(id);
        heroRepository.delete(id);
        powerStatsRepository.delete(powerStatsIdFromHero);
    }

    @Transactional
    public UUID update(UUID id, HeroResp hero){
        return heroRepository.update(id, hero);
    }
}
