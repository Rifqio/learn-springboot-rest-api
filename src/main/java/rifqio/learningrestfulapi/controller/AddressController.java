package rifqio.learningrestfulapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rifqio.learningrestfulapi.dto.request.address.CreateAddressRequestDTO;
import rifqio.learningrestfulapi.dto.response.AddressResponse;
import rifqio.learningrestfulapi.dto.response.WebResponse;
import rifqio.learningrestfulapi.entity.User;
import rifqio.learningrestfulapi.service.AddressService;

@RestController
@RequestMapping("/api/contacts")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/{contactId}/addresses")
    public WebResponse<AddressResponse> create(User user,
                                               @RequestBody CreateAddressRequestDTO request,
                                               @PathVariable String contactId) {
        request.setContactId(contactId);
        AddressResponse response = addressService.create(user, request);
        return WebResponse.<AddressResponse>builder()
                .message("Address has been created")
                .data(response)
                .build();
    }

}
