package faviohuaman.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "rucs")
public class Ruc {
    
    @Id
    private String id;
    private String ruc;
    private String razonSocial;
    private String condicion;
    private String direccion;
    private String departamento;
    private String provincia;
    private String distrito;
    private String estado;
    private String fechaConsulta;
    @Builder.Default
    private Boolean eliminado = false;
}
