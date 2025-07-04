package faviohuaman.service;

import faviohuaman.model.ConsultaLog;
import faviohuaman.model.Dni;
import faviohuaman.model.Ruc;
import faviohuaman.repository.ConsultaLogRepository;
import faviohuaman.repository.DniRepository;
import faviohuaman.repository.RucRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class RucService {

    private final WebClient webClient;
    private final RucRepository rucRepository;
    private final DniRepository dniRepository;
    private final ConsultaLogRepository consultaLogRepository;
    
    private static final String API_URL_RUC = "https://dniruc.apisperu.com/api/v1/ruc/";
    private static final String API_URL_DNI = "https://dniruc.apisperu.com/api/v1/dni/";
    private static final String API_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImZhdmlvLmh1YW1hbkB2YWxsZWdyYW5kZS5lZHUucGUifQ.nkGCeMiaAaEOfx1552iYfOym0vxi9bgK_3CFDaKavQU";

    public Mono<Ruc> consultarRuc(String rucNumero) {
        // First check if we have it in our database
        return rucRepository.findByRuc(rucNumero)
                .switchIfEmpty(
                        // If not found in DB, query the external API
                        webClient.get()
                                .uri(API_URL_RUC + rucNumero + "?token=" + API_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToMono(Ruc.class)
                                .flatMap(this::saveRucData)
                )
                .flatMap(ruc -> logConsulta(rucNumero, "Exitosa").thenReturn(ruc))
                .onErrorResume(error -> {
                    return logConsulta(rucNumero, "Error: " + error.getMessage())
                            .then(Mono.error(error));
                });
    }

    private Mono<Ruc> saveRucData(Ruc ruc) {
        ruc.setFechaConsulta(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        if (ruc.getEliminado() == null) {
            ruc.setEliminado(false);
        }
        return rucRepository.save(ruc);
    }

    private Mono<ConsultaLog> logConsulta(String rucNumero, String resultado) {
        ConsultaLog log = ConsultaLog.builder()
                .numeroConsulta(rucNumero)
                .fechaConsulta(LocalDateTime.now())
                .resultado(resultado)
                .build();
        return consultaLogRepository.save(log);
    }

    public Mono<Dni> consultarDni(String dniNumero) {
        // First check if we have it in our database
        return dniRepository.findByDni(dniNumero)
                .switchIfEmpty(
                        // If not found in DB, query the external API
                        webClient.get()
                                .uri(API_URL_DNI + dniNumero + "?token=" + API_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToMono(Dni.class)
                                .flatMap(this::saveDniData)
                )
                .flatMap(dni -> logConsulta(dniNumero, "Exitosa - DNI").thenReturn(dni))
                .onErrorResume(error -> {
                    return logConsulta(dniNumero, "Error DNI: " + error.getMessage())
                            .then(Mono.error(error));
                });
    }

    private Mono<Dni> saveDniData(Dni dni) {
        dni.setFechaConsulta(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return dniRepository.save(dni);
    }

    public Mono<Ruc> activarRuc(String rucNumero) {
        return rucRepository.findByRuc(rucNumero)
                .switchIfEmpty(Mono.error(new RuntimeException("RUC no encontrado")))
                .flatMap(ruc -> {
                    ruc.setEliminado(false);
                    return rucRepository.save(ruc);
                })
                .flatMap(ruc -> logConsulta(rucNumero, "RUC Activado").thenReturn(ruc));
    }

    public Mono<Ruc> desactivarRuc(String rucNumero) {
        return rucRepository.findByRuc(rucNumero)
                .switchIfEmpty(Mono.error(new RuntimeException("RUC no encontrado")))
                .flatMap(ruc -> {
                    ruc.setEliminado(true);
                    return rucRepository.save(ruc);
                })
                .flatMap(ruc -> logConsulta(rucNumero, "RUC Desactivado").thenReturn(ruc));
    }

    public Mono<Ruc> obtenerRucPorNumero(String rucNumero) {
        return rucRepository.findByRuc(rucNumero)
                .switchIfEmpty(Mono.error(new RuntimeException("RUC no encontrado")));
    }
}
