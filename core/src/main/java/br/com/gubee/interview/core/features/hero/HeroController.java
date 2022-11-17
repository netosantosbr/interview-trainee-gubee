package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.response.HeroCompareResp;
import br.com.gubee.interview.model.response.HeroResp;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
@CrossOrigin
public class HeroController {

	private final HeroService heroService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Validated
                                       @RequestBody CreateHeroRequest createHeroRequest) {
        final UUID id = heroService.create(createHeroRequest);
        return created(URI.create(format("/api/v1/heroes/%s", id))).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeroResp> findById(@PathVariable UUID id){
        try {
            return ResponseEntity.ok().body(heroService.findById(id));
        } catch(NoSuchElementException exception) {
            exception.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<HeroResp>> findAll(){
        try{
            return ResponseEntity.ok().body(heroService.findAll());
        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        try {
            heroService.delete(id);
            return ResponseEntity.ok().build();
        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<HeroResp> update(@PathVariable UUID id, @RequestBody HeroResp hero){
        try {
            heroService.update(id, hero);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<HeroResp>> findByName(@RequestParam String name){
        try {
            return ResponseEntity.ok().body(heroService.findByName(name));
        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/compare")
    public ResponseEntity<HeroCompareResp> compareTwoHeroes(@RequestParam UUID firstId, @RequestParam UUID secondId) {
        try {
            return ResponseEntity.ok().body(heroService.compareTwoHeroes(firstId, secondId));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
