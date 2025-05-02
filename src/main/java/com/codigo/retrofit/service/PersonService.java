package com.codigo.retrofit.service;

import com.codigo.retrofit.aggregates.response.ReniecResponse;

import java.io.IOException;
import java.util.List;

public interface PersonService {

    ReniecResponse findByDni(String dni) throws IOException;



}
