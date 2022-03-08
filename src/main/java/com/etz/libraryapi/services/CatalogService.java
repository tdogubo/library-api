package com.etz.libraryapi.services;

import com.etz.libraryapi.config.Mapper;
import com.etz.libraryapi.domains.requests.CatalogRequest;
import com.etz.libraryapi.domains.requests.CreateNewCatalogRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.models.Catalog;
import com.etz.libraryapi.repositories.CatalogueRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogService {
    private final CatalogueRepo catalogRepo;
    private  Mapper mapper;

    public ResponseEntity<AppResponse<List<Catalog>>> getCatalogs() {
        List<Catalog> isCatalog = catalogRepo.findAll();
        log.info("Catalogs: {}", isCatalog);
//        List<CatalogResponse> catalogResponses = isCatalog.stream().map(catalog -> mapper.modelMapper().map(catalog, CatalogResponse.class)).collect(Collectors.toList()); // getting null pointer exception
        if (isCatalog.size() != 0) {
            return new ResponseEntity<>(new AppResponse<>(true, isCatalog), HttpStatus.FOUND);
        }
        throw new IllegalStateException("Catalog is empty");
    }

    public ResponseEntity<AppResponse<String>> createNewCatalog(CreateNewCatalogRequest catalogs) {
        try {
            for (String category : catalogs.getCatalogNames()) {
                Catalog catalog = new Catalog();
                catalog.setName(category);
                catalogRepo.save(catalog);
            }
            return new ResponseEntity<>(new AppResponse<>(true, "Successful"), HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<AppResponse<String>> editCatalog(Long id, CatalogRequest request) {
        Optional<Catalog> isCatalog = catalogRepo.findById(id);
        if (isCatalog.isPresent()) {
            Catalog catalog = isCatalog.get();
            catalog.setName(request.getName());
            catalogRepo.save(catalog);
            return new ResponseEntity<>(new AppResponse<>(true, "Successful"), HttpStatus.OK);
        }
        throw new IllegalArgumentException("Catalog name does not exist");
    }

    public ResponseEntity<AppResponse<String>> deleteCatalog(CatalogRequest request) {
        Optional<Catalog> isCatalog = catalogRepo.findByName(request.getName());
        log.info("Optional isCatalog: {}", isCatalog);
        if (isCatalog.isPresent()) {
            log.info("Catalog information::: {}", isCatalog.get());
            Catalog catalog = isCatalog.get();
            catalogRepo.delete(catalog);
            return new ResponseEntity<>(new AppResponse<>(true, "Successful"), HttpStatus.FOUND);
        }
        throw new IllegalArgumentException("Catalog name does not exist");
    }
}
