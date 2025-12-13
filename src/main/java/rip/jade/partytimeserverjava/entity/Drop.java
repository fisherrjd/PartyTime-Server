package rip.jade.partytimeserverjava.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drop")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer value; // Value in GP

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drop_party_id", nullable = false)
    private DropParty dropParty;
}