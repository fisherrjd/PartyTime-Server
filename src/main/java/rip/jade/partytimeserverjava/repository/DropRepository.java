// DropRepository.java
package rip.jade.partytimeserverjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rip.jade.partytimeserverjava.entity.Drop;

import java.util.List;

@Repository
public interface DropRepository extends JpaRepository<Drop, Long> {

    // Find all drops for a party
    List<Drop> findByDropPartyId(Long dropPartyId);

    // Find drops by item
    List<Drop> findByItemId(Integer itemId);
    List<Drop> findByItemName(String itemName);

    // Find high-value drops
    List<Drop> findByValueGreaterThan(Integer value);

    // Find drops by item and party
    List<Drop> findByItemIdAndDropPartyId(Integer itemId, Long dropPartyId);

    // Count drops for a party
    Long countByDropPartyId(Long dropPartyId);

}