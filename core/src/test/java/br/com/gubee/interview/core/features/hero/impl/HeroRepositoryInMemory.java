package br.com.gubee.interview.core.features.hero.impl;

import br.com.gubee.interview.core.features.hero.interfaces.HeroRepository;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.response.HeroCompareResp;
import br.com.gubee.interview.model.response.HeroResp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HeroRepositoryInMemory implements HeroRepository {

    private List<Hero> heroesList = new ArrayList<>();
    private static List<PowerStats> powerStatsList = new ArrayList<>();

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
        hero.setId(UUID.randomUUID());
        hero.setCreatedAt(Instant.now());
        hero.setUpdatedAt(Instant.now());
        heroesList.add(hero);
        return hero.getId();
    }

    @Override
    public HeroResp findById(UUID id) {
        for(Hero hero : heroesList) {
            if(hero.getId() == id) {
                return new HeroResp(
                        hero.getId(),
                        hero.getName(),
                        hero.getRace(),
                        0,
                        0,
                        0,
                        0);
            }
        }
        return null;
    }

    @Override
    public List<HeroResp> findByName(String name) {
        List<HeroResp> matchedHeroes = new ArrayList<>();

        for(Hero h : heroesList) {
            if(h.getName().contains(name)) {
                matchedHeroes.add(new HeroResp(
                        h.getId(),
                        h.getName(),
                        h.getRace(),
                        0,
                        0,
                        0,
                        0));
            }
        }
        return matchedHeroes;
    }

    @Override
    public HeroCompareResp compareTwoHeroes(UUID firstId, UUID secondId) {
        HeroResp firstHero = findById(firstId);
        HeroResp secondHero = findById(secondId);
        return new HeroCompareResp(
                firstId,
                secondId,
                firstHero.getStrength() - secondHero.getStrength(),
                firstHero.getAgility() - secondHero.getAgility(),
                firstHero.getDexterity() - secondHero.getDexterity(),
                firstHero.getIntelligence() - secondHero.getIntelligence()
        );
    }

    @Override
    public List<HeroResp> findAll() {
        List<HeroResp> heroAsHeroRespList = new ArrayList<>();
        for(Hero h : heroesList) {
            heroAsHeroRespList.add(new HeroResp(
                    h.getId(),
                    h.getName(),
                    h.getRace(),
                    0,
                    0,
                    0,
                    0)
            );
        }
        return heroAsHeroRespList;
    }

    @Override
    public void delete(UUID id) {
        for(Hero h : heroesList) {
            if(h.getId() == id) {
                heroesList.remove(h);
            }
        }
    }

    @Override
    public UUID update(UUID id, HeroResp hero) {
        for(Hero h : heroesList) {
            if(h.getId() == id) {
                h.setName(hero.getName());
                h.setRace(hero.getRace());
                h.setUpdatedAt(Instant.now());
                return h.getId();
            }
        }
        return null;
    }
}
