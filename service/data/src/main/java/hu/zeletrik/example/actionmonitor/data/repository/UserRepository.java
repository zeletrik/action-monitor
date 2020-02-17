package hu.zeletrik.example.actionmonitor.data.repository;

import hu.zeletrik.example.actionmonitor.data.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
