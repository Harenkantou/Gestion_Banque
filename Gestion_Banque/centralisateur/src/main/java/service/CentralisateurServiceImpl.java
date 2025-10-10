package main.java.service;

import dto.SoldeDetailDTO;
import ejbclient.CompteCourantEJBClient;
import ejbclient.PretEJBClient;
import soapclient.CompteDepotSoapClient;
import exception.BankingException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class CentralisateurServiceImpl implements CentralisateurService {
    
    private static final Logger logger = Logger.getLogger(CentralisateurServiceImpl.class.getName());
    
    @Inject
    private CompteCourantEJBClient compteCourantClient;
    
    @Inject
    private PretEJBClient pretClient;
    
    private CompteDepotSoapClient compteDepotClient;
    
    public CentralisateurServiceImpl() {
        this.compteDepotClient = new CompteDepotSoapClient();
    }
    
    @Override
    public BigDecimal calculerSoldeTotal(Long clientId) {
        logger.log(Level.INFO, "Calcul du solde total pour le client: {0}", clientId);
        
        try {
            // Récupérer solde compte courant (EJB local)
            BigDecimal soldeCC = getSoldeCompteCourant(clientId);
            logger.log(Level.INFO, "Solde Compte Courant: {0}", soldeCC);
            
            // Récupérer solde compte dépôt (SOAP vers .NET)
            BigDecimal soldeCD = getSoldeCompteDepot(clientId);
            logger.log(Level.INFO, "Solde Compte Dépôt: {0}", soldeCD);
            
            // Récupérer montant prêt en cours (EJB local)
            BigDecimal montantPret = getMontantPrets(clientId);
            logger.log(Level.INFO, "Montant Prêts: {0}", montantPret);
            
            // Calcul final : (CC + CD) - Prêt
            BigDecimal soldeTotal = soldeCC.add(soldeCD).subtract(montantPret);
            logger.log(Level.INFO, "Solde Total calculé: {0}", soldeTotal);
            
            return soldeTotal;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors du calcul du solde total pour le client " + clientId, e);
            throw new BankingException("ERREUR_CENTRALISATEUR_001", 
                "Impossible de calculer le solde total pour le client: " + clientId, e);
        }
    }
    
    @Override
    public SoldeDetailDTO getDetailSoldes(Long clientId) {
        logger.log(Level.INFO, "Récupération détaillée des soldes pour le client: {0}", clientId);
        
        SoldeDetailDTO detail = new SoldeDetailDTO();
        detail.setClientId(clientId);
        
        try {
            // Récupération des soldes individuels
            BigDecimal soldeCC = getSoldeCompteCourant(clientId);
            BigDecimal soldeCD = getSoldeCompteDepot(clientId);
            BigDecimal montantPrets = getMontantPrets(clientId);
            BigDecimal soldeTotal = calculerSoldeTotal(clientId);
            
            // Mise à jour du DTO
            detail.setSoldeCompteCourant(soldeCC);
            detail.setSoldeCompteDepot(soldeCD);
            detail.setMontantPrets(montantPrets);
            detail.setSoldeTotal(soldeTotal);
            
            // Calcul des pourcentages
            calculerPourcentages(detail);
            
            logger.log(Level.INFO, "Détails des soldes récupérés avec succès pour le client: {0}", clientId);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des détails pour le client " + clientId, e);
            throw new BankingException("ERREUR_CENTRALISATEUR_002", 
                "Impossible de récupérer les détails des soldes pour le client: " + clientId, e);
        }
        
        return detail;
    }
    
    @Override
    public boolean verifierEligibilitePret(Long clientId, BigDecimal montantPret) {
        logger.log(Level.INFO, "Vérification éligibilité prêt - Client: {0}, Montant: {1}", 
            new Object[]{clientId, montantPret});
        
        try {
            // Vérification du montant
            if (montantPret == null || montantPret.compareTo(BigDecimal.ZERO) <= 0) {
                logger.log(Level.WARNING, "Montant du prêt invalide: {0}", montantPret);
                return false;
            }
            
            // Récupération du solde total
            BigDecimal soldeTotal = calculerSoldeTotal(clientId);
            logger.log(Level.INFO, "Solde total pour éligibilité: {0}", soldeTotal);
            
            // Règles métier pour l'éligibilité
            boolean eligible = appliquerReglesEligibilite(clientId, montantPret, soldeTotal);
            
            logger.log(Level.INFO, "Client {0} éligible pour prêt de {1}: {2}", 
                new Object[]{clientId, montantPret, eligible});
            
            return eligible;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la vérification d'éligibilité pour le client " + clientId, e);
            return false;
        }
    }
    
    @Override
    public BigDecimal getSoldeCompteCourant(Long clientId) {
        try {
            logger.log(Level.FINE, "Récupération solde compte courant pour client: {0}", clientId);
            return compteCourantClient.getSolde(clientId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur récupération solde compte courant client: " + clientId, e);
            throw new BankingException("ERREUR_CENTRALISATEUR_003", 
                "Impossible de récupérer le solde du compte courant pour le client: " + clientId, e);
        }
    }
    
    @Override
    public BigDecimal getSoldeCompteDepot(Long clientId) {
        try {
            logger.log(Level.FINE, "Récupération solde compte dépôt pour client: {0}", clientId);
            return compteDepotClient.getSolde(clientId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur récupération solde compte dépôt client: " + clientId, e);
            throw new BankingException("ERREUR_CENTRALISATEUR_004", 
                "Impossible de récupérer le solde du compte de dépôt pour le client: " + clientId, e);
        }
    }
    
    @Override
    public BigDecimal getMontantPrets(Long clientId) {
        try {
            logger.log(Level.FINE, "Récupération montant prêts pour client: {0}", clientId);
            return pretClient.getMontantPretEnCours(clientId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur récupération montant prêts client: " + clientId, e);
            throw new BankingException("ERREUR_CENTRALISATEUR_005", 
                "Impossible de récupérer le montant des prêts pour le client: " + clientId, e);
        }
    }
    
    // ========== MÉTHODES PRIVÉES ==========
    
    /**
     * Applique les règles métier pour l'éligibilité au prêt
     */
    private boolean appliquerReglesEligibilite(Long clientId, BigDecimal montantPret, BigDecimal soldeTotal) {
        // Règle 1: Le solde total doit rester positif après le prêt
        boolean soldePositif = soldeTotal.subtract(montantPret).compareTo(BigDecimal.ZERO) >= 0;
        
        if (!soldePositif) {
            logger.log(Level.INFO, "Règle 1 échouée: solde deviendrait négatif");
            return false;
        }
        
        // Règle 2: Le montant du prêt ne doit pas dépasser 80% du solde total
        BigDecimal limiteMontant = soldeTotal.multiply(new BigDecimal("0.8"));
        boolean montantRespecte = montantPret.compareTo(limiteMontant) <= 0;
        
        if (!montantRespecte) {
            logger.log(Level.INFO, "Règle 2 échouée: montant prêt dépasse 80% du solde");
            return false;
        }
        
        // Règle 3: Vérifier l'historique avec le service de prêt
        boolean historiqueValide = pretClient.verifierHistoriqueClient(clientId);
        
        if (!historiqueValide) {
            logger.log(Level.INFO, "Règle 3 échouée: historique client non valide");
            return false;
        }
        
        return true;
    }
    
    /**
     * Calcule les pourcentages de répartition
     */
    private void calculerPourcentages(SoldeDetailDTO detail) {
        BigDecimal soldeTotal = detail.getSoldeTotal();
        
        if (soldeTotal.compareTo(BigDecimal.ZERO) > 0) {
            // Pourcentage compte courant
            BigDecimal pourcentageCC = detail.getSoldeCompteCourant()
                .multiply(BigDecimal.valueOf(100))
                .divide(soldeTotal, 2, BigDecimal.ROUND_HALF_UP);
            detail.setPourcentageCompteCourant(pourcentageCC);
            
            // Pourcentage compte dépôt
            BigDecimal pourcentageCD = detail.getSoldeCompteDepot()
                .multiply(BigDecimal.valueOf(100))
                .divide(soldeTotal, 2, BigDecimal.ROUND_HALF_UP);
            detail.setPourcentageCompteDepot(pourcentageCD);
            
            // Pourcentage prêts (négatif car c'est une dette)
            BigDecimal pourcentagePrets = detail.getMontantPrets()
                .multiply(BigDecimal.valueOf(100))
                .divide(soldeTotal, 2, BigDecimal.ROUND_HALF_UP);
            detail.setPourcentagePrets(pourcentagePrets.negate());
        } else {
            // Si solde total nul ou négatif
            detail.setPourcentageCompteCourant(BigDecimal.ZERO);
            detail.setPourcentageCompteDepot(BigDecimal.ZERO);
            detail.setPourcentagePrets(BigDecimal.ZERO);
        }
    }
    
    /**
     * Méthode utilitaire pour formater les logs
     */
    private String formatLogMessage(String operation, Long clientId, BigDecimal montant) {
        return String.format("%s - Client: %d, Montant: %s", operation, clientId, montant);
    }
}
