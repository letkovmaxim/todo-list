package org.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "CATALOGUES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catalogue {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "catalogueSequence")
    @SequenceGenerator(name = "catalogueSequence", initialValue = 1, allocationSize = 1, sequenceName = "CATALOGUES_SEQUENCE")
    private Long id;
    private String name;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", nullable = false)
    User user;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    Category category;
    @OneToMany(mappedBy = "catalogue", fetch = EAGER, cascade = ALL)
    private List<Task> tasks = new ArrayList<>();
}
