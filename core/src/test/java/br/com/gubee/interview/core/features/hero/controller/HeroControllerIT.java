package br.com.gubee.interview.core.features.hero.controller;

import br.com.gubee.interview.core.features.hero.interfaces.HeroService;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.response.HeroResp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@ActiveProfiles("it")
@SpringBootTest
public class HeroControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HeroService heroService;

    private static final String BASE_URL = "/api/v1/heroes";
    private List<UUID> heroIds = new ArrayList<>();

    private CreateHeroRequest createHeroRequest(String name) {
        return CreateHeroRequest.builder()
                .name(name)
                .agility(5)
                .dexterity(10)
                .strength(6)
                .intelligence(10)
                .race(Race.HUMAN)
                .build();
    }

    private CreateHeroRequest createInvalidHeroRequest(String name) {
        return CreateHeroRequest.builder()
                .name(name)
                .agility(15)
                .dexterity(15)
                .strength(15)
                .intelligence(15)
                .race(Race.HUMAN)
                .build();
    }

    @AfterEach
    void cleanDatabase() {
        if(!heroIds.isEmpty()) {
            for (UUID id : heroIds) {
                heroService.delete(id);
            }
        }
    }

    @Test
    void createAValidHero() throws Exception {
        // GIVEN
        String bodyContent = objectMapper.writeValueAsString(createHeroRequest("hero_that_test_create"));

        // WHEN
        ResultActions result = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent));

        heroIds.add(heroService.findByName("hero_that_test_create").get(0).getId());

        // THEN
        result.andDo(MockMvcResultHandlers.print()).andExpect(status().isCreated());
    }

    @Test
    void createAInvalidHero() throws Exception {
        // GIVEN
        String bodyContent = objectMapper.writeValueAsString(createInvalidHeroRequest("hero_that_test_create_invalid"));

        // WHEN
        ResultActions result = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent));

        // THEN
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll() throws Exception {
        // GIVEN
        heroIds.add(heroService.create(createHeroRequest("hero_that_test_findall1")));
        heroIds.add(heroService.create(createHeroRequest("hero_that_test_findall2")));
        heroIds.add(heroService.create(createHeroRequest("hero_that_test_findall3")));


        // WHEN
        ResultActions result = mockMvc.perform(get(BASE_URL + "/findAll"));

        // THEN
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].name").exists())
                .andExpect(jsonPath("$.[1].name").exists())
                .andExpect(jsonPath("$.[2].name").exists());
    }

    @Test
    void findByName() throws Exception {
        // GIVEN
        heroIds.add(heroService.create(createHeroRequest("hero_that_test_findbyname")));
        heroIds.add(heroService.create(createHeroRequest("hero_that_test_findbyname2")));

        // WHEN
        ResultActions result = mockMvc.perform(get(BASE_URL + "?name=" + "hero_that_test_findbyname"));


        // THEN
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].name").value("hero_that_test_findbyname"))
                .andExpect(jsonPath("$.[1].name").value("hero_that_test_findbyname2"));
    }

    @Test
    void findById() throws Exception {
        // GIVEN
        UUID firstId = heroService.create(createHeroRequest("hero_that_test_findbyid"));
        heroIds.add(firstId);

        // WHEN
        ResultActions result = mockMvc.perform(get(BASE_URL + "/" + firstId));

        // THEN
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("hero_that_test_findbyid"));

    }

    @Test
    void update() throws Exception {
        // GIVEN
        UUID id = heroService.create(createHeroRequest("hero_that_test_update"));
        heroIds.add(id);
        HeroResp heroResp = new HeroResp();
        heroResp.setName("hero_that_test_update_newname");
        heroResp.setRace(Race.DIVINE);
        heroResp.setStrength(3);
        heroResp.setAgility(2);
        heroResp.setDexterity(1);
        heroResp.setIntelligence(7);
        String heroRespAsJson = objectMapper.writeValueAsString(heroResp);

        // WHEN
        ResultActions result = mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(heroRespAsJson));
        HeroResp hero = heroService.findById(id);

        // THEN
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        assertEquals("hero_that_test_update_newname", heroResp.getName());
        assertEquals(3, heroResp.getStrength());
        assertEquals(2, heroResp.getAgility());
        assertEquals(1, heroResp.getDexterity());
        assertEquals(7, heroResp.getIntelligence());

    }

    @Test
    void deleteById() throws Exception {
        // GIVEN
        UUID id = heroService.create(createHeroRequest("hero_that_test_delete"));

        // WHEN
        ResultActions result = mockMvc.perform(delete(BASE_URL + "/" + id));

        // THEN
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        assertThrows(NoSuchElementException.class, () -> heroService.findById(id));

    }

    @Test
    void compareTwoHeroes() throws Exception {
        // GIVEN
        UUID firstId = heroService.create(createHeroRequest("hero_that_test_comparison"));
        UUID secondId = heroService.create(createHeroRequest("hero_that_test_comparison2"));
        heroIds.add(firstId);
        heroIds.add(secondId);

        // WHEN
        ResultActions result = mockMvc.perform(get(BASE_URL + "/compare?firstId=" + firstId + "&secondId=" + secondId));

        // THEN
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_hero_id").value(firstId.toString()))
                .andExpect(jsonPath("$.second_hero_id").value(secondId.toString()))
                .andExpect(jsonPath("$.strength_difference").value(0))
                .andExpect(jsonPath("$.agility_difference").value(0))
                .andExpect(jsonPath("$.dexterity_difference").value(0))
                .andExpect(jsonPath("$.intelligence_difference").value(0));
    }


}
