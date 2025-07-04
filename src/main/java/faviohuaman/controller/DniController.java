package faviohuaman.controller;

import faviohuaman.model.Dni;
import faviohuaman.service.RucService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/dni")
@RequiredArgsConstructor
public class DniController {

    private final RucService rucService;

    @GetMapping("/{dni}")
    public Mono<ResponseEntity<Dni>> consultarDni(@PathVariable String dni) {
        return rucService.consultarDni(dni)
                .map(data -> ResponseEntity.ok().body(data))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Dni.builder()
                                    .dni(dni)
                                    .nombres("Error al consultar: " + e.getMessage())
                                    .build()));
                });
    }
}
