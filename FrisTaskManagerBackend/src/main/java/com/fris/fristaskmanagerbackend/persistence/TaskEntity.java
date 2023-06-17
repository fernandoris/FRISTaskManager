package com.fris.fristaskmanagerbackend.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Tasks")
@NamedQueries({
        @NamedQuery(
                name = "com.fris.fristaskmanagerbackend.persistence.TaskDAO.findAll",
                query = "SELECT t FROM TaskEntity t"
        )
})
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tittle")
    private String tittle;

    @Column(name = "date")
    private Date date;

}
