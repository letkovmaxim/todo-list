package org.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "CATEGORIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "categorySequence")
    @SequenceGenerator(name = "categorySequence", initialValue = 1, allocationSize = 1, sequenceName = "CATEGORIES_SEQUENCE")
    private Long id;
    private String name;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", nullable = false)
    User user;
    @OneToMany(mappedBy = "category", fetch = LAZY, cascade = ALL)
    private List<Catalogue> catalogues = new ArrayList<>();
}