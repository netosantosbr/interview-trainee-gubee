package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.response.HeroCompareResp;
import br.com.gubee.interview.model.response.HeroResp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(HeroController.class)
class HeroControllerTest {

    private final Integer NOT_FOUND_STATUS_CODE = 404;
    private final Integer OK_STATUS_CODE = 200;
    private final Integer CREATED_STATUS_CODE = 201;

    private final String FIND_ALL_IDS_FROM_DATABASE_QUERY = "SELECT h.id FROM hero h;";

    @MockBean
    private HeroService heroService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void initTest() {
        when(heroService.create(any())).thenReturn(UUID.randomUUID());
    }

    @Test
    void findByIdWithValidId() throws Exception{
        UUID id = UUID.randomUUID();
        HeroResp heroResp = new HeroResp(id, "Petrius Morthius", Race.HUMAN, 5, 5, 5, 5);
        when(heroService.findById(id)).thenReturn(heroResp);
        mockMvc.perform( get("/api/v1/heroes/" + id) )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void findByIdWithInvalidId() throws Exception {
        UUID invalidId = UUID.randomUUID();
        when(heroService.findById(invalidId)).thenThrow(new NoSuchElementException());
        mockMvc.perform( get("/api/v1/heroes/" + invalidId).contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void tryToUpdateHeroWhenIdMatch() throws Exception {
        UUID id = UUID.randomUUID();
        HeroResp heroResp = new HeroResp(id, "Petrius Morthius", Race.HUMAN, 5, 5, 5, 5);
        when(heroService.update(eq(id), any())).thenReturn(id);
        String heroRespAsJson = objectMapper.writeValueAsString(heroResp);

        mockMvc.perform( put("/api/v1/heroes/" + id)
                        .contentType(MediaType.APPLICATION_JSON).content(heroRespAsJson))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void tryToUpdateHeroWhenIdDoesNotMatch() throws Exception {
        UUID invalidUUID = UUID.randomUUID();
        HeroResp heroResp = new HeroResp(UUID.randomUUID(), "Petrius Morthius", Race.HUMAN, 5, 5, 5, 5);
        String heroRespAsJson = objectMapper.writeValueAsString(heroResp);
        when(heroService.update(eq(invalidUUID), any())).thenThrow(new RuntimeException());
        mockMvc.perform( put("/api/v1/heroes/" + invalidUUID)
                .contentType(MediaType.APPLICATION_JSON).content(heroRespAsJson))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void findByNameWithValidName() throws Exception{
        String validName = "Baltazar";
        when(heroService.findByName(validName)).thenReturn(anyList());
        mockMvc.perform( get("/api/v1/heroes?name=" + validName).contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void findByNameWithInvalidName() throws Exception {
        String invalidName = "unmatchedName";
        when(heroService.findByName(invalidName)).thenThrow(new RuntimeException());
        mockMvc.perform( get("/api/v1/heroes?name=" + invalidName).contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void tryToDeleteWhenIdMatch() throws Exception {
        UUID validId = UUID.randomUUID();
        doNothing().when(heroService).delete(validId);
        mockMvc.perform( delete("/api/v1/heroes/" + validId).contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void tryToDeleteWhenIdDoesNotMatch() throws Exception {
        UUID invalidId = UUID.randomUUID();
        doThrow(new RuntimeException()).when(heroService).delete(invalidId);
        mockMvc.perform( delete("/api/v1/heroes/" + invalidId).contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void findAllHeroesWhenListIsEmpty() throws Exception{
        when(heroService.findAll()).thenThrow(new RuntimeException());
        mockMvc.perform( get("/api/v1/heroes/findAll").contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void findAllHeroesWhenListIsNotEmpty() throws Exception {
        List<HeroResp> notEmptyList = new ArrayList<>();
        notEmptyList.add(new HeroResp(UUID.randomUUID(), "Prestus", Race.CYBORG, 2, 2, 2, 3));
        notEmptyList.add(new HeroResp(UUID.randomUUID(), "Morgus", Race.ALIEN, 5, 3, 2, 3));
        when(heroService.findAll()).thenReturn(notEmptyList);
        mockMvc.perform( get("/api/v1/heroes/findAll").contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void compareTwoHeroesWhenBothExists() throws Exception {
        HeroResp firstHero = new HeroResp(UUID.randomUUID(), "Prestus", Race.CYBORG, 2, 2, 2, 3);
        HeroResp secondHero = new HeroResp(UUID.randomUUID(), "Morgus", Race.ALIEN, 5, 3, 2, 3);
        HeroCompareResp hsp = HeroCompareResp.builder()
                .firstHeroId(firstHero.getId())
                .secondHeroId(secondHero.getId())
                .strengthDifference(firstHero.getStrength() - secondHero.getStrength())
                .agilityDifference(firstHero.getAgility() - secondHero.getAgility())
                .dexterityDifference(firstHero.getDexterity() - secondHero.getDexterity())
                .intelligenceDifference(firstHero.getIntelligence() - secondHero.getIntelligence()).build();
        when(heroService.compareTwoHeroes(firstHero.getId(), secondHero.getId())).thenReturn(hsp);
        mockMvc.perform( get("/api/v1/heroes/compare?firstId=" + firstHero.getId() + "&secondId=" + secondHero.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
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

}