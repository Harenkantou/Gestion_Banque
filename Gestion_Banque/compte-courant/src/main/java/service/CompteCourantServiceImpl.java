package service;

import entity.CompteCourant;
import repository.CompteCourantRepository;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.Optional;

@Stateless
public class CompteCourantServiceImpl implements CompteCourantService {
    
    @EJB
    private CompteCourantRepository compteRepository;
    
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
            compte.setSolde(compte.getSolde().add(montant));
            compteRepository.update(compte);
        }
    }
    
    @Override
    public void effectuerRetrait(Long clientId, BigDecimal montant) {
        Optional<CompteCourant> compteOpt = compteRepository.findByClientId(clientId);
        if (compteOpt.isPresent()) {
            CompteCourant compte = compteOpt.get();
            BigDecimal nouveauSolde = compte.getSolde().subtract(montant);
            if (nouveauSolde.compareTo(BigDecimal.ZERO) >= 0) {
                compte.setSolde(nouveauSolde);
                compteRepository.update(compte);
            } else {
                throw new RuntimeException("Solde insuffisant");
            }
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
