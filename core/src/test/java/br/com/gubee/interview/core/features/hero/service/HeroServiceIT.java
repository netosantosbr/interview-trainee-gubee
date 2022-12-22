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
        UUID heroId = heroService.create(createHero("Margus"));
        heroService.findById(heroId);
    }

    @Test
    public void findByInvalidId() {
        Throwable throwable = assertThrows(Exception.class, () -> heroService.findById(UUID.randomUUID()));
        System.err.println("findHeroByInvalidId returns: " + throwable.getMessage());
        assertFalse(throwable.getMessage().equals(null));
    }

    @Test
    public void findByName() {
        heroService.create(createHero("Morgus"));
        List<HeroResp> listOfMatchedHeroes = heroService.findByName("Morgus");
        assertTrue(listOfMatchedHeroes.size() > 0);
    }

    @Test
    public void findByInvalidName() {
        List<HeroResp> listOfMatchedHeroes = heroService.findByName("2$s23e123s2cz");
        assertEquals(0, listOfMatchedHeroes.size());
    }

    @Test
    public void findAllWithNonEmptyList() {
        heroService.create(createHero("Minus"));
        heroService.create(createHero("Crius"));
        List<HeroResp> listOfAllHeroes = heroService.findAll();
        assertTrue(listOfAllHeroes.size() > 0);
    }

    @Test
    public void findAllWithEmptyList() {
        cleanDatabase();
        List<HeroResp> listOfAllHeroes = heroService.findAll();
        assertEquals(0, listOfAllHeroes.size());
    }

    @Test
    public void deleteWithValidId() {
        UUID heroId = heroService.create(createHero("Gronio"));
        heroService.delete(heroId);
        assertThrows(Exception.class, () -> heroService.findById(heroId));
    }

    @Test
    public void deleteWithInvalidId() {
        assertThrows(Exception.class, () -> heroService.delete(UUID.randomUUID()));
    }

    @Test
    public void update() {
        HeroResp heroModify = new HeroResp();
        heroModify.setName("Polonius");
        heroModify.setRace(Race.ALIEN);

        UUID heroId = heroService.create(createHero("Merxenius"));
        heroService.update(heroId, heroModify);
        HeroResp heroModified = heroService.findById(heroId);
        assertEquals(heroModify.getName(), heroModified.getName());
        assertEquals(heroModify.getRace(), heroModified.getRace());
    }

    @Test
    public void compareTwoHeroes() {
        HeroResp firstHero = heroService.findById(heroService.create(createHero("Nominus")));
        HeroResp secondHero = heroService.findById(heroService.create(createHero("Selenius")));
        HeroCompareResp heroCompareResp = heroService.compareTwoHeroes(firstHero.getId(), secondHero.getId());
        assertEquals(heroCompareResp.getStrengthDifference(), (firstHero.getStrength() - secondHero.getStrength()));
        assertEquals(heroCompareResp.getAgilityDifference(), (firstHero.getAgility() - secondHero.getAgility()));
        assertEquals(heroCompareResp.getDexterityDifference(), (firstHero.getDexterity() - secondHero.getDexterity()));
        assertEquals(heroCompareResp.getIntelligenceDifference(), (firstHero.getIntelligence() - secondHero.getIntelligence()));
    }
}
