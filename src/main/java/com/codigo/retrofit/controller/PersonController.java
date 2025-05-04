package com.codigo.retrofit.controller;

import com.codigo.retrofit.aggregates.response.PersonResponse;
import com.codigo.retrofit.aggregates.response.ReniecResponse;
import com.codigo.retrofit.aggregates.response.ResponseBase;
import com.codigo.retrofit.entity.PersonEntity;
import com.codigo.retrofit.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


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

    @PostMapping("/update/{dni}/{newStatus}")
    public ResponseEntity<ResponseBase<PersonEntity>> updatePerson(@PathVariable String dni, @PathVariable String newStatus) throws IOException{
        return new ResponseEntity<>(personService.updatePersonStatus(dni, newStatus), HttpStatus.CREATED);
    }

    @GetMapping("/findAllByStatus/{status}")
    public ResponseEntity<ResponseBase<List<PersonResponse>>> findAllByStaus(@PathVariable String status) throws IOException{
        return new ResponseEntity<>(personService.findAllByState(status), HttpStatus.CREATED);
    }

    @GetMapping("/delete/{dni}")
    public ResponseEntity<ResponseBase<PersonResponse>> deleteByDni(@PathVariable String dni) throws RuntimeException{
        return new ResponseEntity<>(personService.deleteByDni(dni), HttpStatus.CREATED);
    }



}
