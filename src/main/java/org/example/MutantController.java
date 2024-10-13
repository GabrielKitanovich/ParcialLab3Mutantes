package org.example;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mutant")
public class MutantController {

    @Autowired
    private final MutantService mutantService;

    @Autowired
    private final DnaRepository dnaRepository;

    public MutantController(MutantService mutantService, DnaRepository dnaRepository) {
        this.mutantService = mutantService;
        this.dnaRepository = dnaRepository;
    }

    @PostMapping("/")
    public ResponseEntity<String> checkMutant(@RequestBody @NotNull DnaRequest dnaRequest) {
        boolean isMutant = mutantService.isMutant(dnaRequest.getDna());
        mutantService.saveDna(dnaRequest.getDna(), isMutant);
        if (isMutant) {
            return new ResponseEntity<>("Mutant detected", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not a mutant", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        long countMutantDna = dnaRepository.countByIsMutant(true);
        long countHumanDna = dnaRepository.countByIsMutant(false);
        double ratio = countHumanDna == 0 ? 1.0 : (double) countMutantDna / countHumanDna;

        Map<String, Object> stats = new HashMap<>();
        stats.put("count_mutant_dna", countMutantDna);
        stats.put("count_human_dna", countHumanDna);
        stats.put("ratio", ratio);

        return stats;
    }
}