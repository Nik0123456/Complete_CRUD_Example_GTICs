package gtics.l2_20223209_implementacion.Repository;
import gtics.l2_20223209_implementacion.Entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoRepository extends JpaRepository<Auto, Integer> {

    // Ejemplo de @Query usando JPQL (Java Persistence Query Language)

    @Query("SELECT a FROM Auto a JOIN FETCH a.sede s")
    List<Auto> findAutosConSede();

    /*
       JPQL no usa los nombres de las tablas ni sus columnas sino que
       trabaja con objetos (clases) y las relaciones entre clases que son declaradas
       como atributos.

       Por ello usamos "SELECT a FROM Auto a" en vez de
       "SELECT a FROM auto a" pues incluso si la tabla de autos en SQL se
       llama "auto", en Java hemos declarado la entidad de auto como la clase
       "Auto". (Notar que JPQL permite usar alias al igual que SQL, por ejemplo "Auto a")

       Asimismo, cuando empleamos "JOIN FETCH a.sede" estamos empleando
       el atributo sede, que es una instancia de la clase Sede, que está declarado
       en la clase Auto como una relacion "@ManyToOne" en vez de usar el nombre
       de la llave foranea que conecta la tabla auto con la tabla sede en la base
       de datos, "sede_idsede".

       En lo que respecta a la logica de ejecucion del query, Hibernate que es la
       herramienta utilizada en Java para el mapeo relacional de objetos u ORM traduce
       el JPQL en lenguaje SQL entendible por MySQL (En este caso estamos usando esa
       base de datos).

       "SELECT a FROM Auto a" (JPQL) lo traduce a "SELECT a.* FROM auto a" (SQL)
       "JOIN FETCH a.sede" (JPQL) lo traduce a "SELECT s.* ... JOIN sede s ON a.sede_idsede = s.idsede" (SQL)

       En conjunto convierte el JPQL "SELECT a FROM Auto a JOIN FETCH a.sede s"
       en " SELECT a.*, s.*
            FROM auto a
            JOIN sede s ON a.sede_idsede = s.idsede; "

       Notar la diferencia fundamental entre usar "JOIN FETCH a.sede s" y solo "JOIN a.sede s"

       El primer query se traduce en lo comentado anteriormente mientras que el segundo se convierte
       en " SELECT a.*
            FROM auto a
            JOIN sede s ON a.sede_idsede = s.idsede; "

       En pocas palabras, si solo se usa JOIN se esta extrayendo de la base de datos
       la informacion de los autos que están asociados con al menos una sede, pero no se
       trae la informacion de la sede, en cambio al usar FETCH JOIN se está trayendo
       la información de los autos que están asociados con al menos una sede y la
       información de la sede con la que están asociados.

       Al traer toda la información de golpe, se evita que Hibernate utilice Lazy Loading,
       que por defecto carga los objetos relacionados solo cuando se accede a ellos.
       Si se usa solamente JOIN y luego se accede al atributo sede de cada auto,
       Hibernate realizará una consulta adicional por cada auto para traer su sede.

       ¿Por qué esto es importante?

       Porque en un solo query se obtiene toda la información necesaria, en vez de realizar una
       consulta adicional por cada Auto para obtener su sede. Esto evita lo que se conoce como
       el problema N + 1.

       Este problema ocurre, por ejemplo, cuando se hace una consulta para listar todos los
       autos (1 consulta), y luego, por cada uno de los N autos, se realiza una consulta individual
       para obtener la información de su sede. El total sería N + 1 consultas.

       Por simplicidad a menudo se usa: "List<Auto> autos = autoRepository.findAll();"

       Pero este metodo utiliza Lazy Loading por defecto. Hibernate inicialmente solo
       carga los autos, pero al acceder al atributo sede de cada uno, lanza una consulta
       individual a la base de datos para cargar esa relación, lo que implica que sucede un
       caso de problema N + 1.

       Esto puede impactar gravemente el rendimiento cuando el número de registros es alto.
       No es lo mismo hacer 11 consultas para 10 autos, que 10,001 consultas para 10,000 autos.
       Por eso, se recomienda utilizar JOIN FETCH para realizar una sola consulta más pesada pero
       eficiente, que reduce el número total de operaciones de entrada/salida contra la base de datos.

    */

    //Implementacion de paginacion logica//

    Page<Auto> findAll(Pageable pageable);

    /*
       USO BÁSICO DE PAGINACIÓN CON Pageable
       --------------------------------------

       En bases de datos con muchos registros, es ineficiente traer toda la información de golpe.
       La paginación permite dividir los resultados en "páginas" para mostrar solo una parte
       a la vez, lo cual mejora el rendimiento y la experiencia de usuario.

       En Spring, esto se logra agregando un parámetro Pageable al metodo del repositorio.
       Este metodo retorna un objeto Page<Entidad>, que no solo contiene la lista de resultados,
       sino también información útil sobre la paginación.

       Ejemplo de uso en un servicio:

           Pageable pageable = PageRequest.of(0, 10); // Página 0 (primera), 10 elementos por página
           Page<Auto> paginaAutos = autoRepository.findAll(pageable);

       ------------------------------------
       CÓMO SABER CUÁNTAS PÁGINAS HAY
       ------------------------------------

       El objeto Page<Auto> contiene:

           - paginaAutos.getTotalElements() → cantidad total de autos en la BD
           - paginaAutos.getTotalPages()    → cantidad total de páginas
           - paginaAutos.getNumber()        → número de la página actual (0-indexado)
           - paginaAutos.getContent()       → lista de autos en esta página
           - paginaAutos.hasNext()          → ¿hay página siguiente?
           - paginaAutos.hasPrevious()      → ¿hay página anterior?

       ------------------------------------
       ACCEDER A OTRA PÁGINA
       ------------------------------------

       Para acceder a otra página, basta con cambiar el índice:

           PageRequest.of(1, 10) → trae la segunda página (índice 1)
           PageRequest.of(2, 10) → tercera página, y así sucesivamente

       ------------------------------------
       ENVIAR AL FRONTEND
       ------------------------------------

       Lo ideal es crear un DTO de respuesta con esta estructura:

           public class PaginacionAutoResponse {
               private List<Auto> autos;
               private int paginaActual;
               private int totalPaginas;
               private long totalElementos;
               private boolean haySiguiente;
               private boolean hayAnterior;

               // getters y setters
           }

       Luego, en el controlador (controller):

           @GetMapping("/autos")
           public ResponseEntity<PaginacionAutoResponse> listarAutos(
               @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "10") int size) {

               Pageable pageable = PageRequest.of(page, size);
               Page<Auto> pagina = autoRepository.findAll(pageable);

               PaginacionAutoResponse response = new PaginacionAutoResponse();
               response.setAutos(pagina.getContent());
               response.setPaginaActual(pagina.getNumber());
               response.setTotalPaginas(pagina.getTotalPages());
               response.setTotalElementos(pagina.getTotalElements());
               response.setHaySiguiente(pagina.hasNext());
               response.setHayAnterior(pagina.hasPrevious());

               return ResponseEntity.ok(response);
           }

       Desde el frontend (por ejemplo con React o Angular) se puede consumir esta API
       y mostrar los datos, junto con botones para cambiar de página usando
       `page + 1`, `page - 1`, etc., basándose en `haySiguiente` y `hayAnterior`.

       ------------------------------------
       CONCLUSIÓN
       ------------------------------------

       Pageable es una forma eficiente de navegar grandes cantidades de datos sin
       saturar la base de datos ni la memoria del servidor o del cliente.
       Es ampliamente usado en APIs REST modernas con Spring.
    */
    
}
