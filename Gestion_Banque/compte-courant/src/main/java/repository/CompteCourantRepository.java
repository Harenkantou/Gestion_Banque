package repository;

import entity.CompteCourant;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Stateless
public class CompteCourantRepository {
    
    @PersistenceContext(unitName = "compteCourantPU")
    private EntityManager entityManager;
    
    public void save(CompteCourant compte) {
        entityManager.persist(compte);
    }
    
    public Optional<CompteCourant> findById(Long id) {
        return Optional.ofNullable(entityManager.find(CompteCourant.class, id));
    }
    
    public Optional<CompteCourant> findByClientId(Long clientId) {
        List<CompteCourant> result = entityManager.createQuery(
            "SELECT cc FROM CompteCourant cc WHERE cc.client.idClient = :clientId", CompteCourant.class)
            .setParameter("clientId", clientId)
            .getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
    
    public void update(CompteCourant compte) {
        entityManager.merge(compte);
    }
    
    public void delete(Long id) {
        CompteCourant compte = entityManager.find(CompteCourant.class, id);
        if (compte != null) {
            entityManager.remove(compte);
        }
    }
}
