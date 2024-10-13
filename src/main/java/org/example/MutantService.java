package org.example;

import org.example.exceptions.DnaAlreadyExistsException;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class MutantService {

    private static final int SEQUENCE_LENGTH = 4;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');

    private final DnaRepository dnaRepository;

    public MutantService(DnaRepository dnaRepository) {
        this.dnaRepository = dnaRepository;
    }

    public boolean isMutant(String[] dna) {
        int n = dna.length;
        if (n == 0 || dna[0].length() != n) {
            throw new IllegalArgumentException("Invalid DNA matrix");
        }

        int sequenceCount = 0;

        // 1. Verificar secuencias horizontales y verticales
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (hasHorizontalSequence(dna, row, col, n) ||
                        hasVerticalSequence(dna, row, col, n)) {
                    sequenceCount++;
                    if (sequenceCount > 1) return true;
                }
            }
        }

        // 2. Verificar todas las diagonales principales y secundarias
        for (int row = 0; row < n; row++) {
            if (checkDiagonals(dna, row, 0, n) || checkDiagonals(dna, 0, row, n)) {
                sequenceCount++;
                if (sequenceCount > 1) return true;
            }
        }

        return false;
    }

    public void saveDna(String[] dna, boolean isMutant) {
        String dnaSequence = String.join(",", dna);
        if (dnaRepository.findBySequence(dnaSequence).isPresent()) {
            throw new DnaAlreadyExistsException("DNA sequence already exists");
        }
        Dna dnaEntity = new Dna(null, dnaSequence, isMutant);
        dnaRepository.save(dnaEntity);
    }

    // Verifica si hay una secuencia horizontal de 4 caracteres
    private boolean hasHorizontalSequence(String[] dna, int row, int col, int n) {
        if (col + SEQUENCE_LENGTH > n) return false;
        char currentChar = dna[row].charAt(col);
        if (!VALID_BASES.contains(currentChar)) return false;
        for (int i = 1; i < SEQUENCE_LENGTH; i++) {
            if (dna[row].charAt(col + i) != currentChar) return false;
        }
        return true;
    }

    // Verifica si hay una secuencia vertical de 4 caracteres
    private boolean hasVerticalSequence(String[] dna, int row, int col, int n) {
        if (row + SEQUENCE_LENGTH > n) return false;
        char currentChar = dna[row].charAt(col);
        if (!VALID_BASES.contains(currentChar)) return false;
        for (int i = 1; i < SEQUENCE_LENGTH; i++) {
            if (dna[row + i].charAt(col) != currentChar) return false;
        }
        return true;
    }

    // Verifica todas las diagonales principales y secundarias desde una posición dada
    private boolean checkDiagonals(String[] dna, int row, int col, int n) {
        return hasDiagonalRightSequence(dna, row, col, n) || hasDiagonalLeftSequence(dna, row, col, n);
    }

    // Verifica diagonal principal (de arriba-izquierda a abajo-derecha)
    private boolean hasDiagonalRightSequence(String[] dna, int row, int col, int n) {
        if (row + SEQUENCE_LENGTH > n || col + SEQUENCE_LENGTH > n) return false;
        char currentChar = dna[row].charAt(col);
        if (!VALID_BASES.contains(currentChar)) return false;
        for (int i = 1; i < SEQUENCE_LENGTH; i++) {
            if (dna[row + i].charAt(col + i) != currentChar) return false;
        }
        return true;
    }

    // Verifica diagonal secundaria (de arriba-derecha a abajo-izquierda)
    private boolean hasDiagonalLeftSequence(String[] dna, int row, int col, int n) {
        if (row + SEQUENCE_LENGTH > n || col - SEQUENCE_LENGTH < -1) return false;
        char currentChar = dna[row].charAt(col);
        if (!VALID_BASES.contains(currentChar)) return false;
        for (int i = 1; i < SEQUENCE_LENGTH; i++) {
            if (dna[row + i].charAt(col - i) != currentChar) return false;
        }
        return true;
    }
}