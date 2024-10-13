package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MutantControllerTest {

    @Mock
    private MutantService mutantService;

    @Mock
    private DnaRepository dnaRepository;

    @InjectMocks
    private MutantController mutantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkMutantReturnsOkForMutantDna() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        Dna dnaEntity = new Dna(null, String.join("", dna), true);

        // Mockeamos el servicio para que devuelva true para secuencia mutante
        when(mutantService.isMutant(dna)).thenReturn(true);
        when(dnaRepository.save(any(Dna.class))).thenReturn(dnaEntity);

        // Llamamos al controlador
        ResponseEntity<String> response = mutantController.checkMutant(new DnaRequest(dna));

        // Verificamos el resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mutant detected", response.getBody());
    }

    @Test
    void checkMutantReturnsForbiddenForHumanDna() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCACTA", "TCACTG"};
        DnaRequest dnaRequest = new DnaRequest(dna);

        // Mockeamos el servicio para que devuelva false para secuencia no mutante
        when(mutantService.isMutant(dna)).thenReturn(false);

        // Llamamos al controlador
        ResponseEntity<String> response = mutantController.checkMutant(dnaRequest);

        // Verificamos el resultado
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Not a mutant", response.getBody());
    }

    @Test
    void getStatsReturnsCorrectStats() {
        // Mockeamos el repositorio para que devuelva los conteos de mutantes y humanos
        when(dnaRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRepository.countByIsMutant(false)).thenReturn(100L);

        // Llamamos al controlador
        Map<String, Object> stats = mutantController.getStats();

        // Verificamos los resultados
        assertEquals(40L, stats.get("count_mutant_dna"));
        assertEquals(100L, stats.get("count_human_dna"));
        assertEquals(0.4, stats.get("ratio"));
    }

}














