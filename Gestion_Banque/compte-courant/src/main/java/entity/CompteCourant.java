package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compte_courant")
public class CompteCourant implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compte_courant")
    private Long idCompteCourant;
    
    @OneToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id_client")
    private Client client;
    
    @Column(name = "taux_annuel", precision = 5, scale = 4)
    private BigDecimal tauxAnnuel = BigDecimal.ZERO;
    
    @Column(name = "solde", precision = 15, scale = 2)
    private BigDecimal solde = BigDecimal.ZERO;
    
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;
    
    // Constructeurs
    public CompteCourant() {
        this.dateCreation = LocalDateTime.now();
    }
    public CompteCourant(Client client, BigDecimal tauxAnnuel) {
        this.client = client;
        this.tauxAnnuel = tauxAnnuel;
        this.dateCreation = LocalDateTime.now();
    }
    
    // Getters et setters
    public Long getIdCompteCourant() { return idCompteCourant; }
    public void setIdCompteCourant(Long idCompteCourant) { this.idCompteCourant = idCompteCourant; }
    
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    
    public BigDecimal getTauxAnnuel() { return tauxAnnuel; }
    public void setTauxAnnuel(BigDecimal tauxAnnuel) { this.tauxAnnuel = tauxAnnuel; }
    
    public BigDecimal getSolde() { return solde; }
    public void setSolde(BigDecimal solde) { this.solde = solde; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
