package faviohuaman.controller;

import faviohuaman.model.Ruc;
import faviohuaman.service.RucService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ruc")
@RequiredArgsConstructor
public class RucController {

    private final RucService rucService;

    @GetMapping("/{ruc}")
    public Mono<ResponseEntity<Ruc>> consultarRuc(@PathVariable String ruc) {
        return rucService.consultarRuc(ruc)
                .map(data -> ResponseEntity.ok().body(data))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Ruc.builder()
                                    .ruc(ruc)
                                    .estado("Error")
                                    .razonSocial("Error al consultar: " + e.getMessage())
                                    .eliminado(false)
                                    .build()));
                });
    }

    @PatchMapping("/{ruc}/activar")
    public Mono<ResponseEntity<Ruc>> activarRuc(@PathVariable String ruc) {
        return rucService.activarRuc(ruc)
                .map(data -> ResponseEntity.ok().body(data))
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Ruc.builder()
                                    .ruc(ruc)
                                    .estado("Error")
                                    .razonSocial("Error al activar: " + e.getMessage())
                                    .eliminado(false)
                                    .build()));
                });
    }

    @PatchMapping("/{ruc}/desactivar")
    public Mono<ResponseEntity<Ruc>> desactivarRuc(@PathVariable String ruc) {
        return rucService.desactivarRuc(ruc)
                .map(data -> ResponseEntity.ok().body(data))
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Ruc.builder()
                                    .ruc(ruc)
                                    .estado("Error")
                                    .razonSocial("Error al desactivar: " + e.getMessage())
                                    .eliminado(true)
                                    .build()));
                });
    }

    @GetMapping("/{ruc}/estado")
    public Mono<ResponseEntity<Ruc>> obtenerEstadoRuc(@PathVariable String ruc) {
        return rucService.obtenerRucPorNumero(ruc)
                .map(data -> ResponseEntity.ok().body(data))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Ruc.builder()
                                    .ruc(ruc)
                                    .estado("Error")
                                    .razonSocial("Error al obtener estado: " + e.getMessage())
                                    .eliminado(false)
                                    .build()));
                });
    }
}
