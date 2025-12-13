package rip.jade.partytimeserverjava.entity;// Drop.java

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drop_party")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DropParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer world;

    @Column(name = "avg_drop", nullable = false)
    private Integer avgDrop;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "last_drop_at", nullable = false)
    @Builder.Default
    private Instant lastDropAt = Instant.now();

    @OneToMany(mappedBy = "dropParty", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Drop> drops = new ArrayList<>();

    // Helper methods for bidirectional relationship
    public void addDrop(Drop drop) {
        drops.add(drop);
        drop.setDropParty(this);
    }

    public void removeDrop(Drop drop) {
        drops.remove(drop);
        drop.setDropParty(null);
    }
}