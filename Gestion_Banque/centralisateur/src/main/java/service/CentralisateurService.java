package main.java.service;

import dto.SoldeDetailDTO;
import javax.ejb.Remote;
import java.math.BigDecimal;

@Remote
public interface CentralisateurService {
    
    /**
     * Calcule le solde total d'un client
     * Formule : (Compte Courant + Compte Dépôt) - Prêts
     */
    BigDecimal calculerSoldeTotal(Long clientId);
    
    /**
     * Récupère le détail de tous les soldes
     */
    SoldeDetailDTO getDetailSoldes(Long clientId);
    
    /**
     * Vérifie l'éligibilité pour un nouveau prêt
     */
    boolean verifierEligibilitePret(Long clientId, BigDecimal montantPret);
    
    /**
     * Récupère le solde d'un compte spécifique
     */
    BigDecimal getSoldeCompteCourant(Long clientId);
    
    /**
     * Récupère le solde du compte de dépôt
     */
    BigDecimal getSoldeCompteDepot(Long clientId);
    
    /**
     * Récupère le montant total des prêts
     */
    BigDecimal getMontantPrets(Long clientId);
}
