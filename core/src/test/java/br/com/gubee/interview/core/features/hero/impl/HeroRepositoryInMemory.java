package br.com.gubee.interview.core.features.hero.impl;

import br.com.gubee.interview.core.features.hero.interfaces.HeroRepository;
import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.response.HeroCompareResp;
import br.com.gubee.interview.model.response.HeroResp;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


public class HeroRepositoryInMemory implements HeroRepository {


    private List<Hero> heroesList = new ArrayList<>();
    private static List<PowerStats> powerStatsList = new ArrayList<>();

    private PowerStatsRepositoryInMemory psInMemory;

    public HeroRepositoryInMemory(PowerStatsRepositoryInMemory psInMemory) {
        this.psInMemory = psInMemory;
    }

    @Override
    public UUID findPowerStatsIdFromHero(UUID id) {
        for(Hero hero : heroesList) {
            if(hero.getPowerStatsId() == id) {
                return hero.getPowerStatsId();
            }
        }
        return null;
    }

    @Override
    public UUID create(Hero hero) {
        if(psInMemory.findById(hero.getPowerStatsId()) != null) {
            hero.setId(UUID.randomUUID());
            hero.setCreatedAt(Instant.now());
            hero.setUpdatedAt(Instant.now());
            heroesList.add(hero);
            return hero.getId();
        }

        return null;
    }

    @Override
    public HeroResp findById(UUID id) {
        for(Hero hero : heroesList) {
            if(hero.getId() == id) {
                PowerStats ps = psInMemory.findById(hero.getPowerStatsId());
                return new HeroResp(
                        hero.getId(),
                        hero.getName(),
                        hero.getRace(),
                        ps.getStrength(),
                        ps.getAgility(),
                        ps.getDexterity(),
                        ps.getIntelligence());
            }
        }
        return null;
    }

    @Override
    public List<HeroResp> findByName(String name) {
        List<HeroResp> matchedHeroes = new ArrayList<>();

        for(Hero h : heroesList) {
            if(h.getName().contains(name)) {
                PowerStats ps = psInMemory.findById(h.getPowerStatsId());
                matchedHeroes.add(new HeroResp(
                        h.getId(),
                        h.getName(),
                        h.getRace(),
                        ps.getStrength(),
                        ps.getAgility(),
                        ps.getDexterity(),
                        ps.getIntelligence()));
            }
        }
        return matchedHeroes;
    }

    @Override
    public HeroCompareResp compareTwoHeroes(UUID firstId, UUID secondId) {
        HeroResp firstHero = findById(firstId);
        HeroResp secondHero = findById(secondId);
        if(firstHero != null && secondHero != null) {
            return new HeroCompareResp(
                    firstId,
                    secondId,
                    firstHero.getStrength() - secondHero.getStrength(),
                    firstHero.getAgility() - secondHero.getAgility(),
                    firstHero.getDexterity() - secondHero.getDexterity(),
                    firstHero.getIntelligence() - secondHero.getIntelligence()
            );
        }
        throw new NoSuchElementException();
    }

    @Override
    public List<HeroResp> findAll() {
        List<HeroResp> heroAsHeroRespList = new ArrayList<>();
        for(Hero h : heroesList) {
            PowerStats ps = psInMemory.findById(h.getPowerStatsId());
            heroAsHeroRespList.add(new HeroResp(
                    h.getId(),
                    h.getName(),
                    h.getRace(),
                    ps.getStrength(),
                    ps.getAgility(),
                    ps.getDexterity(),
                    ps.getIntelligence())
            );
        }
        return heroAsHeroRespList;
    }

    @Override
    public void delete(UUID id) {
        for(int i = 0; i < heroesList.size(); i++) {
            if(heroesList.get(i).getId() == id) {
                heroesList.remove(heroesList.get(i));
            }
        }
    }

    @Override
    public UUID update(UUID id, HeroResp hero) {

        for(Hero h : heroesList) {
            if(h.getId() == id) {
                PowerStats ps = psInMemory.findById(h.getPowerStatsId());
                h.setName(hero.getName());
                h.setRace(hero.getRace());
                h.setUpdatedAt(Instant.now());
                ps.setStrength(hero.getStrength());
                ps.setAgility(hero.getAgility());
                ps.setDexterity(hero.getDexterity());
                ps.setIntelligence(hero.getIntelligence());
                return h.getId();
            }
        }
        throw new NoSuchElementException();
    }
}
