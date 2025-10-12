package service;

import entity.CompteCourant;
import entity.Transaction;
import entity.Transaction.TypeCompte;
import entity.Transaction.TypeTransaction;
import entity.Client;
import repository.CompteCourantRepository;
import repository.TransactionRepository;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.Optional;


@Stateless
public class CompteCourantServiceImpl implements CompteCourantService {
    @EJB
    private CompteCourantRepository compteRepository;
    @EJB
    private TransactionRepository transactionRepository;
    
    @Override
    public BigDecimal getSolde(Long clientId) {
        Optional<CompteCourant> compteOpt = compteRepository.findByClientId(clientId);
        return compteOpt.map(CompteCourant::getSolde).orElse(BigDecimal.ZERO);
    }
    
    @Override
    public void effectuerDepot(Long clientId, BigDecimal montant) {
        Optional<CompteCourant> compteOpt = compteRepository.findByClientId(clientId);
        
        if (compteOpt.isPresent()) {
            CompteCourant compte = compteOpt.get();
            Client client = compte.getClient();
            
            // 1. Mise à jour du solde du compte courant
            compte.setSolde(compte.getSolde().add(montant));
            compteRepository.update(compte);
            
            // 2. Création et enregistrement de la transaction (Audit)
            Transaction transaction = new Transaction(
                client, 
                Transaction.TypeCompte.courant, 
                compte.getIdCompteCourant(), 
                montant, 
                Transaction.TypeTransaction.depot
            );
            transactionRepository.save(transaction);
            
        } else {
            // Dans une implémentation plus robuste, on lèverait une exception ClientNotFoundException
            throw new RuntimeException("Compte courant non trouvé pour le client ID: " + clientId);
        }
    }

    @Override
    public void effectuerRetrait(Long clientId, BigDecimal montant) {
        Optional<CompteCourant> compteOpt = compteRepository.findByClientId(clientId);
        
        if (compteOpt.isPresent()) {
            CompteCourant compte = compteOpt.get();
            Client client = compte.getClient();
            BigDecimal nouveauSolde = compte.getSolde().subtract(montant);
            
            if (nouveauSolde.compareTo(BigDecimal.ZERO) >= 0) {
                
                // 1. Mise à jour du solde du compte courant
                compte.setSolde(nouveauSolde);
                compteRepository.update(compte);
                
                // 2. Création et enregistrement de la transaction (Audit)
                Transaction transaction = new Transaction(
                    client, 
                    Transaction.TypeCompte.courant, 
                    compte.getIdCompteCourant(), 
                    montant, 
                    Transaction.TypeTransaction.retrait
                );
                transactionRepository.save(transaction);
                
            } else {
                // Règle de gestion : solde insuffisant
                // Remplacer par SoldeInsuffisantException dans la version finale
                throw new RuntimeException("Solde insuffisant");
            }
        } else {
            // Remplacer par ClientNotFoundException dans la version finale
            throw new RuntimeException("Compte courant non trouvé pour le client ID: " + clientId);
        }
    }

    @Override
    public BigDecimal calculerInterets(Long clientId) {
        Optional<CompteCourant> compteOpt = compteRepository.findByClientId(clientId);
        if (compteOpt.isPresent()) {
            CompteCourant compte = compteOpt.get();
            // Exemple de calcul : solde * tauxAnnuel / 100
            return compte.getSolde().multiply(compte.getTauxAnnuel()).divide(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }
}
