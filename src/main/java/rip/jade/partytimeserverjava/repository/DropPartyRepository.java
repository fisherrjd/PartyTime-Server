// DropPartyRepository.java
package rip.jade.partytimeserverjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rip.jade.partytimeserverjava.entity.DropParty;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface DropPartyRepository extends JpaRepository<DropParty, Long> {

    // Find active drop parties
    List<DropParty> findByIsActiveTrue();

    // Find by world
    List<DropParty> findByWorld(Integer world);

    // Find active party by world
    Optional<DropParty> findByWorldAndIsActiveTrue(Integer world);

    // Find parties with drops above certain average
    List<DropParty> findByAvgDropGreaterThan(Integer avgDrop);

    // Find parties created after a certain time
    List<DropParty> findByCreatedAtAfter(Instant createdAt);

    // Custom query with joins to fetch drops eagerly
    @Query("SELECT dp FROM DropParty dp LEFT JOIN FETCH dp.drops WHERE dp.id = :id")
    Optional<DropParty> findByIdWithDrops(@Param("id") Long id);

    // Find active parties with recent drops
    @Query("SELECT dp FROM DropParty dp WHERE dp.isActive = true AND dp.lastDropAt >= :since")
    List<DropParty> findActivePartiesWithRecentDrops(@Param("since") Instant since);

    // DropPartyRepository.java
    @Query("SELECT COALESCE(SUM(d.value * d.quantity), 0) FROM Drop d WHERE d.dropParty = :party")
    Long getTotalValue(@Param("party") DropParty party);
}