package org.umldiagram;

import java.util.logging.*;
import org.jivesoftware.whack.*;
import org.xmpp.component.*;

public class Main {
	private final static String HOST = "176.34.122.48";
	private final static int PORT = 5275;
	public static void main(String[] args) {
	   
	  ExternalComponentManager mgr = new ExternalComponentManager(HOST, 5275);
      mgr.setSecretKey("umldesigner", "picpocpac");
      
      try {
         mgr.addComponent("umldesigner", new PartySpamComponent());
      } catch (ComponentException e) {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "main", e);
         System.exit(-1);
      }
      //Keep it alive
      while (true)
         try {
            Thread.sleep(10000);
         } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "main", e);
         }
   }
}