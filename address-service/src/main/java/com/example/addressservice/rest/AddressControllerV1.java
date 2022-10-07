package com.example.addressservice.rest;

import com.example.addressservice.model.AddressDto;
import com.example.addressservice.model.Groups;
import com.example.addressservice.service.AddressService;
import com.example.addressservice.service.AddressServiceImpl;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * The controller for handling request by addresses URL.
 *<p></p>
 *First version of api
 *<p></p>
 *Use this endpoints to create,update,get,delete addresses
 *
 * @author Rostyslav Balushchak
 * @since 1.0.0-SNAPSHOT
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/addresses")
public class AddressControllerV1 {

  private final AddressService addressService;

  private final String addressServiceCB = "addressService";

  @Autowired
  public AddressControllerV1(AddressServiceImpl addressService) {
    this.addressService = addressService;
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public AddressDto getById(@PathVariable final Integer id, @RequestHeader("value")String value){
    System.out.println("Header \"value\":" + value);
    System.err.println("VALUE OBTAINED\n\n\n");
    return addressService.findById(id).get();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Collection<AddressDto> getAll() {
    return addressService.findAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AddressDto create(@RequestBody
                           @Validated(value = Groups.Create.class) AddressDto address) {
    return addressService.create(address);
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public AddressDto update(@RequestBody
                           @Validated(value = Groups.Update.class) AddressDto updateAddress) {
    return addressService.update(updateAddress, updateAddress.getId());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable final Integer id) {
    addressService.deleteById(id);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAll(){
    addressService.deleteAll();
  }
}
