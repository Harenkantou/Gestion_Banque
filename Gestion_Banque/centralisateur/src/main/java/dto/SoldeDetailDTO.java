package main.java.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SoldeDetailDTO {
    private Long clientId;
    private BigDecimal soldeCompteCourant;
    private BigDecimal soldeCompteDepot;
    private BigDecimal montantPrets;
    private BigDecimal soldeTotal;
    private BigDecimal pourcentageCompteCourant;
    private BigDecimal pourcentageCompteDepot;
    private BigDecimal pourcentagePrets;
    private LocalDateTime dateCalcul;
    
    // Constructeurs
    public SoldeDetailDTO() {
        this.dateCalcul = LocalDateTime.now();
    }
    
    public SoldeDetailDTO(Long clientId) {
        this();
        this.clientId = clientId;
    }
    
    // Getters et setters
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    
    public BigDecimal getSoldeCompteCourant() { return soldeCompteCourant; }
    public void setSoldeCompteCourant(BigDecimal soldeCompteCourant) { 
        this.soldeCompteCourant = soldeCompteCourant != null ? soldeCompteCourant : BigDecimal.ZERO;
    }
    
    public BigDecimal getSoldeCompteDepot() { return soldeCompteDepot; }
    public void setSoldeCompteDepot(BigDecimal soldeCompteDepot) { 
        this.soldeCompteDepot = soldeCompteDepot != null ? soldeCompteDepot : BigDecimal.ZERO;
    }
    
    public BigDecimal getMontantPrets() { return montantPrets; }
    public void setMontantPrets(BigDecimal montantPrets) { 
        this.montantPrets = montantPrets != null ? montantPrets : BigDecimal.ZERO;
    }
    
    public BigDecimal getSoldeTotal() { return soldeTotal; }
    public void setSoldeTotal(BigDecimal soldeTotal) { 
        this.soldeTotal = soldeTotal != null ? soldeTotal : BigDecimal.ZERO;
    }
    
    public BigDecimal getPourcentageCompteCourant() { return pourcentageCompteCourant; }
    public void setPourcentageCompteCourant(BigDecimal pourcentageCompteCourant) { 
        this.pourcentageCompteCourant = pourcentageCompteCourant;
    }
    
    public BigDecimal getPourcentageCompteDepot() { return pourcentageCompteDepot; }
    public void setPourcentageCompteDepot(BigDecimal pourcentageCompteDepot) { 
        this.pourcentageCompteDepot = pourcentageCompteDepot;
    }
    
    public BigDecimal getPourcentagePrets() { return pourcentagePrets; }
    public void setPourcentagePrets(BigDecimal pourcentagePrets) { 
        this.pourcentagePrets = pourcentagePrets;
    }
    
    public LocalDateTime getDateCalcul() { return dateCalcul; }
    public void setDateCalcul(LocalDateTime dateCalcul) { this.dateCalcul = dateCalcul; }
    
    @Override
    public String toString() {
        return String.format(
            "SoldeDetailDTO[clientId=%d, soldeTotal=%s, CC=%s, CD=%s, Prets=%s]",
            clientId, soldeTotal, soldeCompteCourant, soldeCompteDepot, montantPrets
        );
    }
}
