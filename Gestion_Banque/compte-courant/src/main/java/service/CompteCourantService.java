package service;

import javax.ejb.Remote;
import java.math.BigDecimal;

@Remote
public interface CompteCourantService {
    BigDecimal getSolde(Long clientId);
    void effectuerDepot(Long clientId, BigDecimal montant);
    void effectuerRetrait(Long clientId, BigDecimal montant);
    BigDecimal calculerInterets(Long clientId);
}
