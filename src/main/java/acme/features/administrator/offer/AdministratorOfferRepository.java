package acme.features.administrator.offer;

import acme.entities.offer.Offer;
import acme.framework.repositories.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface AdministratorOfferRepository extends AbstractRepository {

    @Query("select o from Offer o where o.id = :id")
    Offer findOneOfferById(int id);

	@Query("select o from Offer o")
    Collection<Offer> findManyOffer();

	@Query("select o from Offer o where o.endDate > :currentMoment")
	Collection<Offer> findAllNotFinishedOffers(Date currentMoment);
}
