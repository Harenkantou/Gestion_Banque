package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")

public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private Long idTransaction;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_compte")
    private TypeCompte typeCompte;

    @Column(name = "id_compte")
    private Long idCompte;

    @Column(name = "montant", precision = 15, scale = 2, nullable = false)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_transaction")
    private TypeTransaction typeTransaction;

    @Column(name = "date_transaction")
    private LocalDateTime dateTransaction;

    public enum TypeCompte {
        courant, depot
    }

    public enum TypeTransaction {
        depot, retrait
    }

    // Constructeurs, getters, setters
    public Transaction() {
        this.dateTransaction = LocalDateTime.now();
    }

    public Transaction(Client client, TypeCompte typeCompte, Long idCompte, BigDecimal montant, TypeTransaction typeTransaction) {
        this.client = client;
        this.typeCompte = typeCompte;
        this.idCompte = idCompte;
        this.montant = montant;
        this.typeTransaction = typeTransaction;
        this.dateTransaction = LocalDateTime.now();
    }

    public Long getIdTransaction() {
        return idTransaction;
    }
    public void setIdTransaction(Long idTransaction) {
        this.idTransaction = idTransaction;
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public TypeCompte getTypeCompte() {
        return typeCompte;
    }
    public void setTypeCompte(TypeCompte typeCompte) {
        this.typeCompte = typeCompte;
    }

    public Long getIdCompte() {
        return idCompte;
    }
    public void setIdCompte(Long idCompte) {
        this.idCompte = idCompte;
    }

    public BigDecimal getMontant() {
        return montant;
    }
    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public TypeTransaction getTypeTransaction() {
        return typeTransaction;
    }
    public void setTypeTransaction(TypeTransaction typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }
    public void setDateTransaction(LocalDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }
}