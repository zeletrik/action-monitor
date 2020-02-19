package hu.zeletrik.example.actionmonitor.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@Table(name = "message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "from_user_id", nullable = false)
    private Long from;

    @Column(name = "to_user_id", nullable = false)
    private Long to;

    @Column(name = "msg", nullable = false)
    private String text;

    @Column(name = "timestamp", nullable = false)
    private Instant time;

}
