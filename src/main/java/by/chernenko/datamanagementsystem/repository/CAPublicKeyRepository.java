package by.chernenko.datamanagementsystem.repository;

import by.chernenko.datamanagementsystem.model.CAPublicKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CAPublicKeyRepository extends JpaRepository<CAPublicKey,Long> {
}
