package com.codigo.retrofit.service.impl;


import com.codigo.retrofit.aggregates.constants.Constants;
import com.codigo.retrofit.aggregates.response.ReniecResponse;
import com.codigo.retrofit.aggregates.response.ResponseBase;
import com.codigo.retrofit.entity.PersonEntity;
import com.codigo.retrofit.repository.PersonRepository;
import com.codigo.retrofit.retrofit.ClientReniecService;
import com.codigo.retrofit.retrofit.ClientRetrofit;
import com.codigo.retrofit.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;


@Service
@Log4j2
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final ClientReniecService retrofitPreConfig =
            ClientRetrofit.getRetrofit()
                    .create(ClientReniecService.class);

    @Value("${token.api}")
    private String token;


    @Override
    public ReniecResponse findByDni(String dni) throws IOException {
        Response<ReniecResponse>  executeReniec = preparedClient(dni).execute();
        if(executeReniec.isSuccessful() && Objects.nonNull(executeReniec.body())){
            return executeReniec.body();
        }
        return new ReniecResponse();
    }

    @Override
    public ResponseBase<PersonEntity> registerPerson(String dni) throws IOException{
        log.info("Registrando Persona con DNI: {}", dni);

        Response<ReniecResponse>  executeReniec = preparedClient(dni).execute();

        if(executeReniec.isSuccessful() && Objects.nonNull(executeReniec.body())){
            PersonEntity personEntity = buildPersonEntity(executeReniec.body());
            PersonEntity personSave = personRepository.save(personEntity);
            return buildResponse(2001,"Todo OK!!",Optional.of(personSave));
        }
        return buildResponse(4000,
                "Ocurrio un Error no existe respuesta de Reniec!!",
                Optional.empty());
    }

    @Override
    public ResponseBase<PersonEntity> updatePerson(String dni, String newState) throws RuntimeException {
        boolean resultNewState = validateNewState(newState);
        if(resultNewState){
            PersonEntity personEntity = personRepository.findByNumberDocument(dni).orElseThrow(() -> new RuntimeException("The person in not find"));
            PersonEntity updatePersonEntity = buildPersonEntity(personEntity, newState.toUpperCase());
            PersonEntity personSave = personRepository.save(updatePersonEntity);
            return buildResponse(2001, "OK", Optional.of(personSave));

        }else{
            throw new RuntimeException("Invalid new state");
        }

    }


    //====================================================================
    //=====================================================================

    private Call<ReniecResponse> preparedClient(String dni){
        return retrofitPreConfig.findReniec("Bearer "+token,dni);
    }

    private boolean validateNewState(String newState){

        return switch (newState.toLowerCase()) {
            case "active", "inactive" -> true;
            default -> false;
        };

    }

    private <T> ResponseBase<T> buildResponse(
            int code, String message, Optional<T> optional){
        ResponseBase<T> responseBase = new ResponseBase<>();
        responseBase.setCode(code);
        responseBase.setMessage(message);
        responseBase.setEntity(optional);
        return  responseBase;
    }

    private PersonEntity buildPersonEntity(ReniecResponse reniecResponse){
        return PersonEntity.builder()
                .names(reniecResponse.getNombres())
                .fullName(reniecResponse.getNombreCompleto())
                .lastName(reniecResponse.getApellidoPaterno())
                .motherLastName(reniecResponse.getApellidoMaterno())
                .typeDocument(reniecResponse.getTipoDocumento())
                .numberDocument(reniecResponse.getNumeroDocumento())
                .checkDigit(reniecResponse.getDigitoVerificador())
                .status(Constants.STATUS_ACTIVE)
                .userCreated(Constants.USER_ADMIN)
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .build();
    }
    private PersonEntity buildPersonEntity(PersonEntity person, String newStatus){
        return PersonEntity.builder()
                .id(person.getId())
                .names(person.getNames())
                .fullName(person.getFullName())
                .lastName(person.getLastName())
                .motherLastName(person.getMotherLastName())
                .typeDocument(person.getTypeDocument())
                .numberDocument(person.getNumberDocument())
                .checkDigit(person.getCheckDigit())
                .status(newStatus)
                .userCreated(Constants.USER_ADMIN)
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
