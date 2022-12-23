package br.com.gubee.interview.core.features.hero.service;

import br.com.gubee.interview.core.features.hero.HeroService;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.response.HeroCompareResp;
import br.com.gubee.interview.model.response.HeroResp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class HeroServiceIT {

    @Autowired
    private HeroService heroService;

    @Test
    public void createHeroWithAllRequiredArguments() {
        heroService.create(createHeroRequest());
    }

    private void cleanDatabase() {
        List<HeroResp> listOfHeroes = heroService.findAll();
        for(HeroResp h : listOfHeroes){
            heroService.delete(h.getId());
        }
    }

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

    private CreateHeroRequest createHero(String name) {
        return CreateHeroRequest.builder()
                .name(name)
                .agility(5)
                .dexterity(8)
                .strength(6)
                .intelligence(10)
                .race(Race.HUMAN)
                .build();
    }

    @Test
    public void findById() {
        // GIVEN
        UUID heroId = heroService.create(createHero("Margus"));

        // WHEN
        HeroResp result = heroService.findById(heroId);

        // THEN
        assertNotNull(result);
    }

    @Test
    public void findByInvalidId() {
        // GIVEN
        UUID idInvalido = UUID.randomUUID();

        // WHEN - THEN
        assertThrows(NoSuchElementException.class, () -> heroService.findById(idInvalido));
    }

    @Test
    public void findByName() {
        // GIVEN
        heroService.create(createHero("Morgus"));

        // WHEN
        List<HeroResp> listOfMatchedHeroes = heroService.findByName("Morgus");
        cleanDatabase();

        // THEN
        assertFalse(listOfMatchedHeroes.isEmpty());
    }

    @Test
    public void findByInvalidName() {
        // GIVEN
        String invalidName = "2$s23e123s2cz";

        // WHEN
        List<HeroResp> listOfMatchedHeroes = heroService.findByName(invalidName);

        // THEN
        assertTrue(listOfMatchedHeroes.isEmpty());
    }

    @Test
    public void findAllWithNonEmptyList() {
        // GIVEN
        heroService.create(createHero("Minus"));
        heroService.create(createHero("Crius"));

        // WHEN
        List<HeroResp> listOfAllHeroes = heroService.findAll();

        // THEN
        assertFalse(listOfAllHeroes.isEmpty());
    }

    @Test
    public void findAllWithEmptyList() {
        // GIVEN
        cleanDatabase();

        // WHEN
        List<HeroResp> listOfAllHeroes = heroService.findAll();

        // THEN
        assertTrue(listOfAllHeroes.isEmpty());
    }

    @Test
    public void deleteWithValidId() {
        // GIVEN
        UUID heroId = heroService.create(createHero("Gronio"));

        // WHEN
        heroService.delete(heroId);

        // THEN
        assertThrows(NoSuchElementException.class, () -> heroService.findById(heroId));
    }

    @Test
    public void deleteWithInvalidId() {
        // GIVEN
        UUID invalidId = UUID.randomUUID();

        // WHEN - THEN
        assertThrows(EmptyResultDataAccessException.class, () -> heroService.delete(invalidId));

    }

    @Test
    public void update() {
        // GIVEN
        HeroResp heroModify = new HeroResp();
        heroModify.setName("Polonius");
        heroModify.setRace(Race.ALIEN);
        UUID heroId = heroService.create(createHero("Merxenius"));

        // WHEN
        heroService.update(heroId, heroModify);
        HeroResp heroModified = heroService.findById(heroId);

        // THEN
        assertEquals(heroModify.getName(), heroModified.getName());
        assertEquals(heroModify.getRace(), heroModified.getRace());
    }

    @Test
    public void compareTwoHeroes() {
        // GIVEN
        HeroResp firstHero = heroService.findById(heroService.create(createHero("Nominus")));
        HeroResp secondHero = heroService.findById(heroService.create(createHero("Selenius")));

        // WHEN
        HeroCompareResp heroCompareResp = heroService.compareTwoHeroes(firstHero.getId(), secondHero.getId());

        // THEN
        assertEquals(heroCompareResp.getStrengthDifference(), (firstHero.getStrength() - secondHero.getStrength()));
        assertEquals(heroCompareResp.getAgilityDifference(), (firstHero.getAgility() - secondHero.getAgility()));
        assertEquals(heroCompareResp.getDexterityDifference(), (firstHero.getDexterity() - secondHero.getDexterity()));
        assertEquals(heroCompareResp.getIntelligenceDifference(), (firstHero.getIntelligence() - secondHero.getIntelligence()));
    }
}
