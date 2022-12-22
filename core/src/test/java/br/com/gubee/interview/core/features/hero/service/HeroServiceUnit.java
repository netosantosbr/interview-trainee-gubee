package br.com.gubee.interview.core.features.hero.service;


import br.com.gubee.interview.core.features.hero.HeroService;
import br.com.gubee.interview.core.features.hero.impl.HeroRepositoryInMemory;
import br.com.gubee.interview.core.features.hero.impl.PowerStatsRepositoryInMemory;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;

import static org.junit.jupiter.api.Assertions.*;

import br.com.gubee.interview.model.response.HeroCompareResp;
import br.com.gubee.interview.model.response.HeroResp;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


public class HeroServiceUnit {

    private PowerStatsRepositoryInMemory psRepository = new PowerStatsRepositoryInMemory();
    private HeroRepositoryInMemory heroRepository = new HeroRepositoryInMemory(psRepository);
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

    private CreateHeroRequest createHeroRequestTwo() {
        return CreateHeroRequest.builder()
                .name("Morient")
                .agility(2)
                .dexterity(3)
                .strength(4)
                .intelligence(4)
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
        // GIVEN
        CreateHeroRequest chr = createInvalidHeroRequest();

        // WHEN
        UUID id = heroService.create(chr);

        // THEN
        assertNull(id);
    }

    @Test
    void findWithValidId() {
        // GIVEN
        UUID id = heroService.create(createHeroRequest());

        // WHEN
        HeroResp heroResp = heroService.findById(id);

        // THEN
        assertEquals(heroResp.getId(), id);
        assertEquals(heroResp.getName(), "Batman");
        assertEquals(6, heroResp.getStrength());
        assertEquals(5, heroResp.getAgility());
        assertEquals(8, heroResp.getDexterity());
        assertEquals(10, heroResp.getIntelligence());
    }

    @Test
    void findWithInvalidId() {
        // GIVEN
        heroService.create(createHeroRequest());

        // WHEN
        HeroResp heroResp = heroService.findById(UUID.randomUUID());

        // THEN
        assertNull(heroResp);
    }

    @Test
    void findAll() {
        // GIVEN
        heroService.create(createHeroRequest());
        heroService.create(createHeroRequest());
        heroService.create(createHeroRequest());

        // WHEN
        List<HeroResp> result = heroService.findAll();

        // THEN
        assertEquals(3, result.size());
        assertEquals("Batman", result.get(0).getName());
        assertEquals("Batman", result.get(1).getName());
        assertEquals("Batman", result.get(2).getName());
    }


    @Test
    void findByName() {
        // GIVEN
        heroService.create(createHeroRequest());

        // WHEN
        List<HeroResp> result = heroService.findByName("Batman");

        // THEN
        assertEquals(1, result.size());
        assertEquals("Batman", result.get(0).getName());
    }

    @Test
    void findByNonexistentName() {
        // GIVEN
        heroService.create(createHeroRequest());

        // WHEN
        List<HeroResp> result = heroService.findByName("nonexistentname");

        // THEN
        assertEquals(0, result.size());
    }

    @Test
    void compareTwoHeroes() {
        // GIVEN
        UUID firstId = heroService.create(createHeroRequest());
        UUID secondId = heroService.create(createHeroRequestTwo());

        // WHEN
        HeroCompareResp result = heroService.compareTwoHeroes(firstId, secondId);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.getStrengthDifference());
        assertEquals(3, result.getAgilityDifference());
        assertEquals(5, result.getDexterityDifference());
        assertEquals(6, result.getIntelligenceDifference());
    }

    @Test
    void compareTwoHeroesWhenOneDoesNotExist() {
        // GIVEN
        UUID firstId = heroService.create(createHeroRequest());
        UUID secondId = UUID.randomUUID();

        // WHEN

        // THEN
        assertThrows(NoSuchElementException.class, () -> heroService.compareTwoHeroes(firstId, secondId));
    }

    @Test
    void update() {
        // GIVEN
        UUID id = heroService.create(createHeroRequest());
        HeroResp heroResp = new HeroResp();
        heroResp.setName("HeroTest");
        heroResp.setRace(Race.CYBORG);
        heroResp.setStrength(5);
        heroResp.setAgility(6);
        heroResp.setDexterity(7);
        heroResp.setIntelligence(8);

        // WHEN
        UUID result = heroService.update(id, heroResp);

        // THEN
        HeroResp result2 = heroService.findById(result);
        assertEquals(id, result);
        assertEquals("HeroTest", result2.getName());
        assertEquals(Race.CYBORG, result2.getRace());
        assertEquals(5, result2.getStrength());
        assertEquals(6, result2.getAgility());
        assertEquals(7, result2.getDexterity());
        assertEquals(8, result2.getIntelligence());
    }

    @Test
    void delete() {
        // GIVEN
        UUID id = heroService.create(createHeroRequest());

        // WHEN
        heroService.delete(id);
        List<HeroResp> allHeroes = heroService.findAll();

        // THEN
        assertEquals(0, allHeroes.size());

    }
}
