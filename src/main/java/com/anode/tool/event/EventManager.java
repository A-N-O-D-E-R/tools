package com.anode.tool.event;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

public interface EventManager extends EventProducer {
	
	public void notify(Event event) ;
	
    /**
     * @param from une borne de depart de date d'archive inclue dans la recherche, ou nulle pour toutes les dates disponibles
	 * @param host l'adresse ip du demandeur
	 * @param port le port sur lequel ecrire
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public void sendArchiveEventTo(Date from, String host, int port) throws UnknownHostException, IOException ;
    
    /**
     * @param from une borne de depart de date d'archive inclue dans la recherche, ou nulle pour toutes les dates disponibles
	 * @param host l'adresse ip du demandeur
	 * @param port le port sur lequel ecrire
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public void sendProtocoleArchiveEventTo(Date from, String host, int port) throws UnknownHostException, IOException ;
       
       
}
