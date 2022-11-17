package br.com.gubee.interview.model.response;

import br.com.gubee.interview.model.enums.Race;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HeroResp {
    private UUID id;
    private String name;
    private Race race;
    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;

}
