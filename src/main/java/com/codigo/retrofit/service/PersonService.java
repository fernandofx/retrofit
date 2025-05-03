package com.codigo.retrofit.service;

import com.codigo.retrofit.aggregates.response.ReniecResponse;
import com.codigo.retrofit.aggregates.response.ResponseBase;
import com.codigo.retrofit.entity.PersonEntity;

import java.io.IOException;
import java.util.List;

public interface PersonService {

    ReniecResponse findByDni(String dni) throws IOException;
    ResponseBase<PersonEntity> registerPerson(String dni) throws IOException;
    ResponseBase<PersonEntity> updatePerson(String dni, String newState) throws IOException;


}
