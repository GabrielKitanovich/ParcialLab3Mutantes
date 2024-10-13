package org.example;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.example.exceptions.DnaAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MutantServiceTest {

    @Mock
    private DnaRepository dnaRepository;

    @InjectMocks
    private MutantService mutantService;

    public MutantServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
void isMutantReturnsTrueForMutantDna() {
    String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
    boolean result = mutantService.isMutant(dna);
    assertTrue(result);
}

@Test
void isMutantReturnsFalseForHumanDna() {
    String[] dna = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};
    boolean result = mutantService.isMutant(dna);
    assertFalse(result);
}

@Test
void isMutantThrowsExceptionForInvalidDnaMatrix() {
    String[] dna = {"ATGCGA", "CAGTGC", "TTATGT"};
    assertThrows(IllegalArgumentException.class, () -> mutantService.isMutant(dna));
}

@Test
void saveDnaThrowsExceptionForDuplicateDna() {
    String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
    String sequence = String.join(",", dna);
    when(dnaRepository.findBySequence(sequence)).thenReturn(Optional.of(new Dna()));
    assertThrows(DnaAlreadyExistsException.class, () -> mutantService.saveDna(dna, true));
}

@Test
void saveDnaSavesNewMutantDna() {
    String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
    String sequence = String.join(",", dna);
    when(dnaRepository.findBySequence(sequence)).thenReturn(Optional.empty());
    mutantService.saveDna(dna, true);
    verify(dnaRepository, times(1)).save(any(Dna.class));
}

@Test
void saveDnaSavesNewHumanDna() {
    String[] dna = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};
    String sequence = String.join(",", dna);
    when(dnaRepository.findBySequence(sequence)).thenReturn(Optional.empty());
    mutantService.saveDna(dna, false);
    verify(dnaRepository, times(1)).save(any(Dna.class));
}
}
