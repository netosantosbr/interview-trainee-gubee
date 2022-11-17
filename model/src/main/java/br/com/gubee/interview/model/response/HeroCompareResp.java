package br.com.gubee.interview.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HeroCompareResp {
    private UUID firstHeroId;
    private UUID secondHeroId;
    private int strengthDifference;
    private int agilityDifference;
    private int dexterityDifference;
    private int intelligenceDifference;
}
