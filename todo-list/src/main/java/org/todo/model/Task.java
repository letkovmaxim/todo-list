package org.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "TASKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "taskSequence")
    @SequenceGenerator(name = "taskSequence", initialValue = 1, allocationSize = 1, sequenceName = "TASKS_SEQUENCE")
    private Long id;
    private String name;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", nullable = false)
    User user;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "catalogueId", nullable = false)
    Catalogue catalogue;
    private Boolean isDone;
    private Boolean isImportant;
    private Boolean isFavorite;
    private String creationDate;
    private String expirationDate;
}
