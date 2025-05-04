package com.codigo.retrofit.aggregates.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {
        private Long id;
        private String apellidoPaterno;
        private String apellidoMaterno;
        private String nombreCompleto;
        private String tipoDocumento;
        private String numeroDocumento;
        private String digitoVerificador;

}
