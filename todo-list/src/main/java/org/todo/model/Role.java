package org.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "ROLES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "roleSequence")
    @SequenceGenerator(name = "roleSequence", initialValue = 1, allocationSize = 1, sequenceName = "ROLES_SEQUENCE")
    private Long id;
    private String name;
}