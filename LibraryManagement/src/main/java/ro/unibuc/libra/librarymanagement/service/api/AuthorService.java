package ro.unibuc.libra.librarymanagement.service.api;

import ro.unibuc.libra.librarymanagement.dto.AuthorDTO;

public interface AuthorService {

    AuthorDTO findById(Long id);
}
