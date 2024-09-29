package org.example;


public class MutantFunctions {
    public boolean isMutant(String[] dna) {
        try {
            int n = dna.length;
            int count = 0;
            for (int i = 0; i < n; i++) {
                int aux = 0;
                for (int j = 1; j < dna[i].length(); j++) {
                    if (dna[i].charAt(j) == dna[i].charAt(j - 1)) {
                        aux++;
                    }

                }
                if (aux > 3) {
                    count++;
                }
            }
            return count > 1;
        } catch (Exception e) {
            return false;
        }
    }
    public String[][] getMatrix(String[] dna) {
        String[][] matrix = new String[dna.length][dna.length];
        matrix[0] = dna;
        for (int i = 0; i < dna.length; i++) {
            String aux = "";
            for (int j = 0; j < dna.length; j++) {
                aux += dna[j].charAt(i);
            }
            matrix[1][i] = aux;
        }


        return matrix;
    }
}
