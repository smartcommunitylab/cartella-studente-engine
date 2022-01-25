package it.smartcommunitylab.csengine.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import it.smartcommunitylab.csengine.model.FileDocument;

public interface FileDocumentRepository extends ReactiveMongoRepository<FileDocument, String> {
}
