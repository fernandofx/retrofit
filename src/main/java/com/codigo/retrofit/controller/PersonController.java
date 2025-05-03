package com.codigo.retrofit.controller;

import com.codigo.retrofit.aggregates.response.ReniecResponse;
import com.codigo.retrofit.aggregates.response.ResponseBase;
import com.codigo.retrofit.entity.PersonEntity;
import com.codigo.retrofit.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/person/")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/find/{dni}")
    public ResponseEntity<ReniecResponse> findPerson(@PathVariable String dni) throws IOException {
        return new ResponseEntity<>(personService.findByDni(dni), HttpStatus.OK);
    }

    @PostMapping("/save/{dni}")
    public ResponseEntity<ResponseBase<PersonEntity>> savePerson(@PathVariable String dni) throws IOException{
        return new ResponseEntity<>(personService.registerPerson(dni), HttpStatus.CREATED);
    }

    @PostMapping("/update/{dni}/{newState}")
    public ResponseEntity<ResponseBase<PersonEntity>> updatePerson(@PathVariable String dni, @PathVariable String newState) throws IOException{
        return new ResponseEntity<>(personService.updatePerson(dni, newState), HttpStatus.CREATED);
    }


}
