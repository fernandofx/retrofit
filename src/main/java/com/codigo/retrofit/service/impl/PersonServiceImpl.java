package com.codigo.retrofit.service.impl;


import com.codigo.retrofit.aggregates.constants.Constants;
import com.codigo.retrofit.aggregates.errors.NotFoundError;
import com.codigo.retrofit.aggregates.response.PersonResponse;
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
import java.util.List;
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
        throw new NotFoundError("The document don't exists in Reniec");
    }

    @Override
    public ResponseBase<PersonEntity> registerPerson(String dni) throws IOException{
        log.info("Registrando Persona con DNI: {}", dni);

        Response<ReniecResponse>  executeReniec = preparedClient(dni).execute();

        if(executeReniec.isSuccessful() && Objects.nonNull(executeReniec.body())){
            PersonEntity personEntity = buildPersonEntity(executeReniec.body());
            PersonEntity personSave = personRepository.save(personEntity);
            return buildResponse(2001,"OK register!!",Optional.of(personSave));
        }
        throw new NotFoundError("The document don't exists in Reniec");
    }

    @Override
    public ResponseBase<PersonEntity> updatePersonStatus(String dni, String newStatus) throws NotFoundError {
        boolean resultNewState = validateNewState(newStatus);
        if(resultNewState){
            PersonEntity personEntity = personRepository.findByNumberDocument(dni).orElseThrow(() -> new NotFoundError("the person does not exist"));
            PersonEntity updatePersonEntity = buildPersonEntity(personEntity, newStatus.toUpperCase());
            PersonEntity personSave = personRepository.save(updatePersonEntity);
            return buildResponse(2001, "OK", Optional.of(personSave));
        }
        throw new NotFoundError("Invalid new state");

    }

    @Override
    public ResponseBase<List<PersonResponse>> findAllByState(String status) throws NotFoundError {
       List<PersonEntity> personEntities = personRepository.findAllByStatus(status);
       List<PersonResponse> personResponse = personEntities.stream().map(this::buildPersonEntity).toList();
       if(!personResponse.isEmpty())
           return buildResponse(2001, "OK", Optional.of(personResponse));
       throw new NotFoundError("there is not persons with status " + status);

    }

    @Override
    public ResponseBase<PersonResponse> deleteByDni(String dni) throws NotFoundError {
        PersonEntity personEntity = personRepository.findByNumberDocument(dni).orElseThrow(()-> new NotFoundError("this dni doesn't exists in the data base"));
        PersonResponse personResponse = buildPersonEntity(personEntity);
        personRepository.deleteById(personResponse.getId());
        return buildResponse(2001, "OK", Optional.of(personResponse));
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

    private <T> ResponseBase<T> buildResponse(int code, String message, Optional<T> optional){
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

    private PersonResponse buildPersonEntity(PersonEntity person){
        return PersonResponse.builder()
                .id(person.getId())
                .apellidoPaterno(person.getLastName())
                .apellidoMaterno(person.getMotherLastName())
                .nombreCompleto(person.getFullName())
                .numeroDocumento(person.getNumberDocument())
                .tipoDocumento(person.getTypeDocument())
                .digitoVerificador(person.getCheckDigit())
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
