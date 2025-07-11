package com.anode.tool.event;

public interface EventListener {

    public void handleEvent(Event event);
    
    /**
     * Le lien avec le producteur d'evt a ete ferme
     */
    public void linkClosed();

    /**
     * Le lien avec le producteur d'evt a ete perdu de maniere inattendue
     */
    public void linkBroken();
    
    /**
     * Le lien avec le producteur d'evt a connu des perturbations, des evts ont pu etre perdus
     */
    public void linkDamaged();

}
