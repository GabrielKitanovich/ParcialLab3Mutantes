package org.example;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DnaRequest {
    private String[] dna;

}

