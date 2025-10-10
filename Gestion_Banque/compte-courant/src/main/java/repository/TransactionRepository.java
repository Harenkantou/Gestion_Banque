package repository;

import entity.Transaction;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;

/**
 * Gère la persistance de l'entité Transaction.
 * C'est un EJB Stateless pour bénéficier de la gestion transactionnelle.
 */
@Stateless
public class TransactionRepository {
    
    // Assurez-vous que l'unité de persistance est configurée (ex: "compteCourantPU" ou "gestionBanquePU")
    @PersistenceContext(unitName = "compteCourantPU") 
    private EntityManager entityManager;
    
    /**
     * Enregistre une nouvelle transaction dans la base de données.
     * @param transaction L'objet Transaction à persister.
     */
    public void save(Transaction transaction) {
        entityManager.persist(transaction);
    }
    
    /**
     * Recherche une transaction par son ID.
     * @param id L'identifiant de la transaction.
     * @return Un Optional contenant la Transaction si elle existe.
     */
    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Transaction.class, id));
    }
    
    /**
     * Récupère l'historique des transactions pour un client donné.
     * @param clientId L'identifiant du client.
     * @return Une liste des transactions.
     */
    public List<Transaction> findByClientId(Long clientId) {
        return entityManager.createQuery(
            "SELECT t FROM Transaction t WHERE t.client.idClient = :clientId ORDER BY t.dateTransaction DESC", 
            Transaction.class)
            .setParameter("clientId", clientId)
            .getResultList();
    }
}
