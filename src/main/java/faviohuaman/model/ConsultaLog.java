package faviohuaman.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "consultas")
public class ConsultaLog {
    
    @Id
    private String id;
    private String numeroConsulta;
    private LocalDateTime fechaConsulta;
    private String resultado;
}
