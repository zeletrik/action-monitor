package hu.zeletrik.example.actionmonitor.data.repository;

import hu.zeletrik.example.actionmonitor.data.entity.MessageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository  extends CrudRepository<MessageEntity, Long> {
}
