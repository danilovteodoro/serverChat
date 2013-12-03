/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.exemplo.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author danilo
 */
@ServerEndpoint("/echo")
public class EchoSever {
    
    private Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    
    @OnMessage
    public String receive(String message, Session session){
        System.out.println("Session : "+session.getId());
        return message;
    }
    
//    @OnMessage
//    public void broadcastMessage(String message,Session session) throws IOException{
//        for(Session peer :peers){
//            if(!peer.equals(session)){
//                peer.getBasicRemote().sendText(message);
//            }
//        }
//    }
    
}
