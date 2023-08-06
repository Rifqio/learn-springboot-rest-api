package rifqio.learningrestfulapi.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import rifqio.learningrestfulapi.dto.request.contact.CreateContactRequestDTO;
import rifqio.learningrestfulapi.dto.request.contact.SearchContactRequest;
import rifqio.learningrestfulapi.dto.request.contact.UpdateContactRequestDTO;
import rifqio.learningrestfulapi.dto.response.ContactResponse;
import rifqio.learningrestfulapi.entity.Contact;
import rifqio.learningrestfulapi.entity.User;
import rifqio.learningrestfulapi.repository.ContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    private ContactResponse toContactResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    @Transactional
    public ContactResponse create(User user, CreateContactRequestDTO request) {
        validationService.validate(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    @Transactional
    public ContactResponse get(User user, String id) {
        Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        return toContactResponse(contact);
    }

    @Transactional
    public ContactResponse update(User user, UpdateContactRequestDTO request) {
        validationService.validate(request);
        Contact contact = contactRepository.findFirstByUserAndId(user, request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    @Transactional
    public String delete(User user, String id) {
        Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        contactRepository.delete(contact);
        return id;
    }

    @Transactional
    public Page<ContactResponse> search(User user, SearchContactRequest request) {
        Specification<Contact> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("user"), user));
            if (Objects.nonNull(request.getName())) {
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + request.getName() + "%"));
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + request.getName() + "%"));
            }
            if (Objects.nonNull(request.getEmail())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + request.getEmail() + "%"));
            }
            if (Objects.nonNull(request.getPhone())) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + request.getPhone() + "%"));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        });
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);
        List<ContactResponse> contactResponses = contacts.getContent()
                .stream()
                .map(this::toContactResponse)
                .toList();
        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }
}
