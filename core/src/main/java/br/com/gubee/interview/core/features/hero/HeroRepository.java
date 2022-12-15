package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.response.HeroCompareResp;
import br.com.gubee.interview.model.response.HeroResp;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HeroRepository {


    private static final String CREATE_HERO_QUERY = "INSERT INTO hero" +
        " (name, race, power_stats_id)" +
        " VALUES (:name, :race, :powerStatsId) RETURNING id";

    private static final String FIND_BY_ID_QUERY = "SELECT h.id, h.name, h.race, ps.strength, ps.agility, " +
            "ps.dexterity, ps.intelligence, ps.created_at, ps.updated_at FROM hero h INNER JOIN power_stats ps ON " +
            "h.power_stats_id = ps.id WHERE h.id = :id";

    private static final String UPDATE_HERO_QUERY = "UPDATE hero SET name = :newName, race = :newRace, " +
            "updated_at = :newUpdatedAt WHERE hero.id = :id";

    private static final String UPDATE_HERO_POWER_STATS_QUERY = "UPDATE power_stats SET strength = :newStrength, " +
            "agility = :newAgility, dexterity = :newDexterity, intelligence = :newIntelligence, " +
            "updated_at = :newUpdatedAt WHERE power_stats.id = :idHeroPowerStats";

    private static final String FIND_ALL_QUERY = "SELECT h.id, h.name, h.race, ps.strength, ps.agility, " +
            "ps.dexterity, ps.intelligence, ps.created_at, ps.updated_at FROM hero h INNER JOIN power_stats ps ON " +
            "h.power_stats_id = ps.id";

    private static final String FIND_ALL_BY_NAME_QUERY = "SELECT h.id, h.name, h.race, ps.strength, ps.agility, " +
            "ps.dexterity, ps.intelligence, ps.created_at, ps.updated_at FROM hero h INNER JOIN power_stats ps ON " +
            "h.power_stats_id = ps.id WHERE lower(h.name) LIKE :name";

    private static final String DELETE_QUERY = "DELETE FROM hero h WHERE h.id = :id";

    private static final String FIND_POWER_STATS_ID_FROM_HERO_QUERY = "SELECT h.power_stats_id FROM hero h WHERE " +
            "h.id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UUID findPowerStatsIdFromHero(UUID id){
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(FIND_POWER_STATS_ID_FROM_HERO_QUERY, param, UUID.class);
    }

    //Criar
    public UUID create(Hero hero) {
        final Map<String, Object> params = Map.of("name", hero.getName(),
            "race", hero.getRace().name(),
            "powerStatsId", hero.getPowerStatsId());

        return namedParameterJdbcTemplate.queryForObject(
            CREATE_HERO_QUERY,
            params,
            UUID.class);
    }

    //Ler por ID
    public HeroResp findById(UUID id){
        RowMapper<HeroResp> rowMapper = new RowMapper<>() {
            @Override
            public HeroResp mapRow(ResultSet rs, int rowNum) throws SQLException {

                HeroResp objectResponse = new HeroResp(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("name"),
                        Race.valueOf(rs.getString("race")),
                        rs.getInt("strength"),
                        rs.getInt("agility"),
                        rs.getInt("dexterity"),
                        rs.getInt("intelligence")
                );

                return objectResponse;
            }
        };

        SqlParameterSource param = new MapSqlParameterSource("id", id);
        List<HeroResp> heroesList = namedParameterJdbcTemplate.query(FIND_BY_ID_QUERY, param, rowMapper);

        return heroesList.stream().findFirst().get();
    }

    //Ler por Nome
    public List<HeroResp> findByName(String name){
        List<HeroResp> listOfMatchedHeroes = new ArrayList<>();

        RowMapper<HeroResp> rowMapper = new RowMapper<>() {
            @Override
            public HeroResp mapRow(ResultSet rs, int rowNum) throws SQLException {

                HeroResp objectResponse = new HeroResp(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("name"),
                        Race.valueOf(rs.getString("race")),
                        rs.getInt("strength"),
                        rs.getInt("agility"),
                        rs.getInt("dexterity"),
                        rs.getInt("intelligence")
                );

                return objectResponse;
            }
        };
        name = "%" + name.toLowerCase().trim() + "%";
        SqlParameterSource param = new MapSqlParameterSource("name", name);

        listOfMatchedHeroes = namedParameterJdbcTemplate.query(FIND_ALL_BY_NAME_QUERY, param, rowMapper);

        return listOfMatchedHeroes;
    }

    //Comparar dois heróis
    public HeroCompareResp compareTwoHeroes(UUID firstId, UUID secondId){
        HeroResp firstHero = findById(firstId);
        HeroResp secondHero = findById(secondId);
        HeroCompareResp heroCompareResp = HeroCompareResp.builder()
                .firstHeroId(firstId)
                .secondHeroId(secondId)
                .strengthDifference(firstHero.getStrength() - secondHero.getStrength())
                .agilityDifference(firstHero.getAgility() - secondHero.getAgility())
                .dexterityDifference(firstHero.getDexterity() - secondHero.getDexterity())
                .intelligenceDifference(firstHero.getIntelligence() - secondHero.getIntelligence())
                .build();


        return heroCompareResp;
    }

    //Ler Todos
    public List<HeroResp> findAll(){
        RowMapper<HeroResp> rowMapper = new RowMapper<>() {
            @Override
            public HeroResp mapRow(ResultSet rs, int rowNum) throws SQLException {

                HeroResp objectResponse = new HeroResp(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("name"),
                        Race.valueOf(rs.getString("race")),
                        rs.getInt("strength"),
                        rs.getInt("agility"),
                        rs.getInt("dexterity"),
                        rs.getInt("intelligence")
                );

                return objectResponse;
            }
        };

        List<HeroResp> heroesList = namedParameterJdbcTemplate.query(FIND_ALL_QUERY, rowMapper);
        return heroesList;
    }

    //Deletar
    public void delete(UUID id){
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(DELETE_QUERY, param);
    }

    //Alterar
    public UUID update(UUID id, HeroResp hero){
        HeroResp previousHero = findById(id);

        Timestamp instantOfUpdate = Timestamp.from(Instant.now());
        UUID powerStatsIdFromHero = findPowerStatsIdFromHero(id);
        Map<String, Object> paramsUpdateHeroQuery = Map.of(
                "newName", hero.getName() != null ? hero.getName() : previousHero.getName(),
                "newRace", hero.getRace().name() != null ? hero.getRace().name() : previousHero.getRace().name(),
                "newUpdatedAt", instantOfUpdate,
                "id", id
        );
        Map<String, Object> paramsUpdateHeroPowerStatsQuery = Map.of(
                    "newStrength", hero.getStrength() != 0 ? hero.getStrength() : previousHero.getStrength(),
                    "newAgility", hero.getAgility() != 0 ? hero.getAgility() : previousHero.getAgility(),
                    "newDexterity", hero.getDexterity() != 0 ? hero.getDexterity() : previousHero.getDexterity(),
                    "newIntelligence", hero.getIntelligence() != 0 ? hero.getIntelligence() : previousHero.getIntelligence(),
                    "newUpdatedAt", instantOfUpdate,
                    "idHeroPowerStats", powerStatsIdFromHero
        );
        namedParameterJdbcTemplate.update(UPDATE_HERO_QUERY, paramsUpdateHeroQuery);
        namedParameterJdbcTemplate.update(UPDATE_HERO_POWER_STATS_QUERY, paramsUpdateHeroPowerStatsQuery);
        return hero.getId();
    }


}
