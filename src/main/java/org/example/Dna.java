package org.example;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dna", uniqueConstraints = {@UniqueConstraint(columnNames = {"sequence"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sequence;

    @Column(nullable = false)
    private boolean isMutant;
}