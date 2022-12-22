package br.com.gubee.interview.core.features.hero.controller;

import br.com.gubee.interview.core.features.hero.HeroController;
import br.com.gubee.interview.core.features.hero.interfaces.HeroService;
import br.com.gubee.interview.core.features.hero.stubs.HeroServiceStub;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.response.HeroCompareResp;
import br.com.gubee.interview.model.response.HeroResp;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class HeroControllerUnit {
    HeroService heroService = new HeroServiceStub();
    HeroController heroController = new HeroController(heroService);

    private CreateHeroRequest createHeroRequest() {
        return CreateHeroRequest.builder()
                .name("Batman")
                .agility(5)
                .dexterity(10)
                .strength(6)
                .intelligence(10)
                .race(Race.HUMAN)
                .build();
    }

    private CreateHeroRequest createHeroRequestTwo() {
        return CreateHeroRequest.builder()
                .name("Premier")
                .agility(2)
                .dexterity(5)
                .strength(7)
                .intelligence(10)
                .race(Race.DIVINE)
                .build();
    }

    @Test
    void createHeroAValidHeroAndCheckItsResponseEntity() {
        // GIVEN
        CreateHeroRequest chr = createHeroRequest();

        // WHEN
        ResponseEntity<Void> result = heroController.create(chr);

        // THEN
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertFalse(result.getHeaders().get("Location").get(0).contains("null"));

    }

    @Test
    void findAHeroByIdAndCheckItsResponseEntity() {
        // GIVEN
        UUID id = heroService.create(createHeroRequest());

        // WHEN
        ResponseEntity<HeroResp> result = heroController.findById(id);

        // THEN
        assertInstanceOf(HeroResp.class, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Batman", result.getBody().getName());
    }

    @Test
    void findAllHeroesAndCheckItsResponseEntity() {
        // GIVEN
        heroController.create(createHeroRequest());
        heroController.create(createHeroRequest());
        heroController.create(createHeroRequest());

        // WHEN
        ResponseEntity<List<HeroResp>> result = heroController.findAll();

        // THEN
        assertTrue(result.getBody().size() > 0);
        assertEquals("Batman", result.getBody().get(0).getName());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void deleteAHeroAndCheckItsResponseEntity() {
        // GIVEN
        UUID id = heroService.create(createHeroRequest());

        // WHEN
        ResponseEntity<Void> result = heroController.delete(id);

        // THEN
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void updateAHeroAndCheckItsResponseEntity() {
        // GIVEN
        UUID id = heroService.create(createHeroRequest());
        HeroResp hr = new HeroResp();
        hr.setName("NewName");
        hr.setRace(Race.CYBORG);
        hr.setStrength(4);
        hr.setAgility(4);
        hr.setDexterity(4);
        hr.setIntelligence(4);

        // WHEN
        ResponseEntity<HeroResp> result = heroController.update(id, hr);

        // THEN
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void updateAHeroPassingInvalidIdAndCheckItsResponseEntity() {
        // GIVEN
        HeroResp hr = new HeroResp();
        hr.setName("NewName");
        hr.setRace(Race.CYBORG);
        hr.setStrength(4);
        hr.setAgility(4);
        hr.setDexterity(4);
        hr.setIntelligence(4);

        // WHEN
        ResponseEntity<HeroResp> result = heroController.update(UUID.randomUUID(), hr);

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findByNameAndCheckItsResponseEntity() {
        // GIVEN
        UUID id = heroService.create(createHeroRequest());

        // WHEN
        ResponseEntity<List<HeroResp>> result = heroController.findByName("Batman");

        // THEN
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertInstanceOf(HeroResp.class, result.getBody().get(0));
        assertEquals("Batman", result.getBody().get(0).getName());

    }

    @Test
    void findByAnInvalidNameAndCheckItsResponseEntity() {
        // GIVEN

        // WHEN
        ResponseEntity<List<HeroResp>> result = heroController.findByName("Seopler");

        // THEN
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void compareTwoHeroesAndCheckItsResponseEntity() {
        // GIVEN
        UUID firstId = heroService.create(createHeroRequest());
        UUID secondId = heroService.create(createHeroRequestTwo());

        // WHEN
        ResponseEntity<HeroCompareResp> result = heroController.compareTwoHeroes(firstId, secondId);

        // THEN
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(-1, result.getBody().getStrengthDifference());
        assertEquals(3, result.getBody().getAgilityDifference());
        assertEquals(5, result.getBody().getDexterityDifference());
        assertEquals(0, result.getBody().getIntelligenceDifference());

    }

    @Test
    void compareTwoHeroesWhenOneDoesNotExistsAndCheckItsResponseEntity() {
        // GIVEN
        UUID firstId = heroService.create(createHeroRequest());
        UUID secondId = UUID.randomUUID();

        // WHEN
        ResponseEntity<HeroCompareResp> result = heroController.compareTwoHeroes(firstId, secondId);

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

}
