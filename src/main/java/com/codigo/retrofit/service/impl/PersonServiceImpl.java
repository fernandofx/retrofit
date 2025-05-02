package com.codigo.retrofit.service.impl;


import com.codigo.retrofit.aggregates.response.ReniecResponse;
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
import java.util.Objects;


@Service
@Log4j2
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    ClientReniecService retrofitPreConfig =
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

    private Call<ReniecResponse> preparedClient(String dni){
        return retrofitPreConfig.findReniec("Bearer "+token,dni);
    }
}
