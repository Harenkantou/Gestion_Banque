package main.java.ejbclient;

import service.CompteCourantService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.math.BigDecimal;
import java.util.Properties;

public class CompteCourantEJBClient {
    
    private CompteCourantService lookupCompteCourantService() {
        try {
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            props.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
            
            Context context = new InitialContext(props);
            return (CompteCourantService) context.lookup(
                "ejb:/1-compte-courant/CompteCourantServiceImpl!service.CompteCourantService"
            );
        } catch (NamingException e) {
            throw new RuntimeException("Erreur de lookup EJB CompteCourantService", e);
        }
    }
    
    public BigDecimal getSolde(Long clientId) {
        CompteCourantService service = lookupCompteCourantService();
        return service.getSolde(clientId);
    }
}
