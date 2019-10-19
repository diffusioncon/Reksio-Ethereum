package dac.reksio.secretary.files;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByFilename(String filename);
}
