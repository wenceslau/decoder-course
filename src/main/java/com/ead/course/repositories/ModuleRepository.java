package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

    // essa anotaçao define o fetchType como eager, se a definiçao estiver como lazy
    // é util quando se é necessario trazer tudos
    @EntityGraph(attributePaths = {"courses"} )
    ModuleModel findByTitle(String tittle);

    //essa consulta nao é possivel usar os metodos jpa, precisa se feita com query
    //Estou procurando todos os modulos de um curso, sem ter que trazer o curso, so quero a lista
    //poderia usar o findCurso, e dentro do curso tem a lista, mas no caso, quero a lista, pra evitar
    //queries desnecessarias
    @Query(value="select * from tb_modules where course_course_id = :courseId", nativeQuery = true)
    List<ModuleModel> findAllLModulesIntoCourse(@Param("courseId") UUID courseId);


    //@Modifying junto com  @Query posso fazer update, delete e insert com query nativa ou jpql

    @Query(value = "select * from tb_modules where course_course_id = :courseId and module_id = :moduleId", nativeQuery = true)
    Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId") UUID moduleId);
}
