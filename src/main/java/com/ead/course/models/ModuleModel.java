package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_MODULES")
public class ModuleModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID moduleId;
    @Column(nullable = false, length = 150)
    private String title;
    @Column(nullable = false, length = 250)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    //Muitos modulos para um curso, ou seja um curso pode ter muios modulos
    //Cada modulo pertence a um curso
    //optional = false,  nao pode ser opcional, ao inserir um modulo, sempre tem que ter associado ao curso
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CourseModel course;

    //tipo de acesso do atributo na serializacao ou dessrializacao, so serializa ou des quando tiver escrita, o jsonignore sobressai o write only
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
    //Select, join ou subselect.
    // select = uma consulta a modulo e uma para cada lesson da lista do modulo
    // join = uma unica consulta que traz o modulo e todas as lesson da lista
    // subselect = 2 consultas, uma para curso e uma para trazer todos as lessons da lista
    // Tipo JOIN, ignora o FetchType.LAZY, e ele se torna eager
    // Ja o tipo SUBSELECT ou SELECT, o  FetchType.LAZY se mantem
    // Mas se eu nao defino o FetchMode, o padrao é o join, mas ele mantem o FetchType.LAZY se tuver sido definido
    @Fetch(FetchMode.SUBSELECT)
    private Set<LessonModel> lessons;
}
