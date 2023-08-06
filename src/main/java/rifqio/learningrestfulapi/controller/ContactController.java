package rifqio.learningrestfulapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import rifqio.learningrestfulapi.dto.request.contact.CreateContactRequestDTO;
import rifqio.learningrestfulapi.dto.request.contact.SearchContactRequest;
import rifqio.learningrestfulapi.dto.request.contact.UpdateContactRequestDTO;
import rifqio.learningrestfulapi.dto.response.ContactResponse;
import rifqio.learningrestfulapi.dto.response.PagingResponse;
import rifqio.learningrestfulapi.dto.response.WebResponse;
import rifqio.learningrestfulapi.entity.User;
import rifqio.learningrestfulapi.service.ContactService;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    ContactService contactService;

    @PostMapping
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequestDTO request) {
        try {
            ContactResponse contactResponse = contactService.create(user, request);
            return WebResponse.<ContactResponse>builder()
                    .message("Contact created")
                    .data(contactResponse)
                    .build();
        } catch (Exception err) {
            return WebResponse.<ContactResponse>builder()
                    .message("Contact failed to create")
                    .errors(err.getMessage())
                    .build();
        }
    }

    @GetMapping("/{id}")
    public WebResponse<ContactResponse> get(User user, @PathVariable("id") String id) {
        try {
            ContactResponse contactResponse = contactService.get(user, id);
            return WebResponse.<ContactResponse>builder()
                    .message("Contact found")
                    .data(contactResponse)
                    .build();
        } catch (Exception err) {
            return WebResponse.<ContactResponse>builder()
                    .message("Error getting contact")
                    .errors(err.getMessage())
                    .build();
        }
    }

    @PutMapping("/{id}")
    public WebResponse<ContactResponse> update(User user,
                                               @RequestBody UpdateContactRequestDTO request,
                                               @PathVariable("id") String id) {
        try {
            request.setId(id);
            ContactResponse contactResponse = contactService.update(user, request);
            return WebResponse.<ContactResponse>builder()
                    .message("Contact updated successfully")
                    .data(contactResponse)
                    .build();
        } catch (Exception err) {
            return WebResponse.<ContactResponse>builder()
                    .message("Contact failed to update")
                    .errors(err.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public WebResponse<String> delete(User user, @PathVariable("id") String id) {
        try {
            String contactResponse = contactService.delete(user, id);
            return WebResponse.<String>builder()
                    .message("Contact deleted successfully")
                    .data(contactResponse)
                    .build();
        } catch (Exception err) {
            return WebResponse.<String>builder()
                    .message("Contact failed to delete")
                    .errors(err.getMessage())
                    .build();
        }
    }

    @GetMapping
    public WebResponse<List<ContactResponse>> search(User user,
                                                     @RequestParam(value = "name", required = false) String name,
                                                     @RequestParam(value = "email", required = false) String email,
                                                     @RequestParam(value = "phone", required = false) String phone,
                                                     @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                     @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        SearchContactRequest request = SearchContactRequest.builder()
                .page(page).size(size).name(name).email(email).phone(phone).build();
        Page<ContactResponse> contactResponses = contactService.search(user, request);
        return WebResponse.<List<ContactResponse>>builder()
                .message("Contact found")
                .data(contactResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(contactResponses.getNumber())
                        .totalPage(contactResponses.getTotalPages())
                        .size(contactResponses.getSize())
                        .build())
                .build();
    }
}
