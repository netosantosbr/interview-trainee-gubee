package br.com.gubee.interview.core.features.hero;


import br.com.gubee.interview.core.features.hero.impl.HeroRepositoryInMemory;
import br.com.gubee.interview.core.features.hero.impl.PowerStatsRepositoryInMemory;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;

import static org.junit.jupiter.api.Assertions.*;

import br.com.gubee.interview.model.response.HeroResp;
import org.junit.jupiter.api.Test;

import java.util.UUID;


public class HeroServiceTest {

    private PowerStatsRepositoryInMemory psRepository = new PowerStatsRepositoryInMemory();
    private HeroRepositoryInMemory heroRepository = new HeroRepositoryInMemory();
    private HeroService heroService = new HeroService(psRepository, heroRepository);

    private CreateHeroRequest createHeroRequest() {
        return CreateHeroRequest.builder()
                .name("Batman")
                .agility(5)
                .dexterity(8)
                .strength(6)
                .intelligence(10)
                .race(Race.HUMAN)
                .build();
    }

    private CreateHeroRequest createInvalidHeroRequest() {
        return CreateHeroRequest.builder()
                .name("Koplan")
                .agility(15)
                .dexterity(15)
                .strength(15)
                .intelligence(15)
                .race(Race.ALIEN)
                .build();
    }

    @Test
    void creatingAnValidHero() {
        // GIVEN
        CreateHeroRequest chr = createHeroRequest();

        // WHEN
        UUID id = heroService.create(chr);

        // THEN
        assertNotNull(id);
    }

    @Test
    void creatingAnInvalidHero() {
        UUID id = heroService.create(createInvalidHeroRequest());

    }

    @Test
    void findWithValidId() {
        // GIVEN
        UUID id = heroService.create(createHeroRequest());

        // WHEN
        HeroResp heroResp = heroService.findById(id);

        // THEN
        assertEquals(heroResp.getId(), id);

    }

}
