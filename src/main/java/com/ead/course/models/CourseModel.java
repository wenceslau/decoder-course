package com.ead.course.models;


import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_COURSES")
public class CourseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID courseId;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(nullable = false, length = 250)
    private String description;
    @Column
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;
    @Column(nullable = false)
    private UUID userInstructor;

    //Um curso para muitos modulos, ou seja um curso tem varios modulos
    //Set nao é ordernado e nao permite duplicata
    //Quando uso list, e o objeto tem mais de um relacionamento, varias colecoes
    //O hibernate nao consegue trazer todas as listas, ele traz so a primeira
    //O set sim, consegue trazer todas as colecoes em uma unica consulta
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //CascadeType.ALL, deleta os modulo do curso, se o curso for deletado
    //orphanRemoval = remove qualquer modulo orfao que nao tem curso
    //aqui a deleçao é unitaria, um delete para cada registro
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)// cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    //@OnDelete( action = OnDeleteAction.CASCADE) quando definido assim, a resposabilidade de deletar as associcao é do banco
    private Set<ModuleModel> modules;

    // no nossa caso a delecao aqui sera feita usando um metodo proprio, para ter seguranca e controls esobre nossos delete, com transacao

}
