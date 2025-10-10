package main.java.soapclient;

import javax.xml.soap.*;
import java.math.BigDecimal;
import java.net.URL;

public class CompteDepotSoapClient {
    
    private static final String SOAP_ENDPOINT = "http://localhost:5000/CompteDepotService.asmx";
    
    public BigDecimal getSolde(Long clientId) {
        try {
            // Configuration SOAP
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection connection = soapConnectionFactory.createConnection();
            
            // Création du message SOAP
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage message = messageFactory.createMessage();
            
            // En-tête SOAP
            SOAPPart soapPart = message.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("ns1", "http://tempuri.org/");
            
            // Corps du message
            SOAPBody body = envelope.getBody();
            SOAPElement getSoldeElement = body.addChildElement("GetSolde", "ns1");
            SOAPElement clientIdElement = getSoldeElement.addChildElement("clientId");
            clientIdElement.addTextNode(clientId.toString());
            
            // Appel du service
            URL endpoint = new URL(SOAP_ENDPOINT);
            SOAPMessage response = connection.call(message, endpoint);
            
            // Extraction de la réponse
            return extractBigDecimalFromSOAPResponse(response);
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur communication service .NET CompteDepot", e);
        }
    }
    
    private BigDecimal extractBigDecimalFromSOAPResponse(SOAPMessage response) throws Exception {
        // Implémentation de l'extraction du BigDecimal depuis la réponse SOAP
        // Cette partie dépend de la structure exacte de la réponse SOAP
        return BigDecimal.ZERO; // À adapter
    }
}
