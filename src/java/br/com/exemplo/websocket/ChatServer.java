/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.exemplo.websocket;

import br.com.exemplo.model.Obj;
import br.com.exemplo.model.ObjDecoder;
import br.com.exemplo.model.ObjEncoder;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.omg.CORBA.PERSIST_STORE;

/**
 *
 * @author danilo
 */
@ServerEndpoint(value = "/chat/{nomeDoUsuario}", encoders = {ObjEncoder.class},decoders = {ObjDecoder.class})
public class ChatServer {
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    private static Set<Obj> objs = Collections.synchronizedSet(new HashSet<Obj>());
    private static Map<String,String> mapObjs = Collections.synchronizedMap(new HashMap<String,String>());
    
    @OnOpen
    public void onOpen(@PathParam("nomeDoUsuario")String nome, Session peer) throws IOException, EncodeException{
        peers.add(peer);
        mapObjs.put(peer.getId(), nome);
        sendContatos(nome, peer);
        alertaNovoContato(nome, peer);
        System.out.println(nome);
       
                                                    
    }
    
    @OnClose
    public void onClose(@PathParam("nomeDoUsuario")String nome,Session peer)throws IOException, EncodeException {
        System.out.println("onclose");
        peers.remove(peer);
        alertaSairDaSala(mapObjs.get(peer.getId()), peer);
        removeContato(peer);
        
   
    }
        
    @OnMessage
    public void broadCastObj(Obj obj,Session session) throws IOException, EncodeException {
        
        if(obj.getJson().getString("id").equalsIgnoreCase("All")){
           for(Session peer :peers){
            peer.getBasicRemote().sendObject(obj);
           }
        }
        else{
            for(Session peer :peers){
                if(peer.getId().equals(obj.getJson().getString("id"))){
                    peer.getBasicRemote().sendObject(obj);
                }
            }
            session.getBasicRemote().sendObject(obj);
        }
        System.out.println("Message >>>");
    }
    
   
    public void sendContatos(String nome,Session peer) throws IOException, EncodeException{
         JsonObject json = Json.createObjectBuilder().add("id",peer.getId())
                                                    .add("nome",nome)
                                                    .build();
         Obj obj = new Obj(json);
         objs.add(obj);
         
         JsonArray jsArray;
         JsonArrayBuilder builder=Json.createArrayBuilder();
         
         for(Obj objJson :objs){
             builder.add(objJson.getJson());
         }
         jsArray=builder.build();
         
          json = Json.createObjectBuilder()
                               .add("contatos", jsArray).build();
          peer.getBasicRemote().sendObject(new Obj(json));
         
    }
    
    
    public void alertaNovoContato(String nome,Session peer) throws IOException, EncodeException{
         JsonObject json = Json.createObjectBuilder().add("id",peer.getId())
                                                    .add("nome",nome)
                                                    .build();
         
         json=Json.createObjectBuilder().add("entrar", json).build();
         Obj obj = new Obj(json);
         
         for(Session session :peers){
             if(!session.equals(peer)){
                session.getBasicRemote().sendObject(obj);
             }
         }
        
    }
    
    public void alertaSairDaSala(String nome,Session peer) throws IOException, EncodeException{
        JsonObject json = Json.createObjectBuilder().add("id",peer.getId())
                                                    .add("nome",nome)
                                                    .build();
         
         json=Json.createObjectBuilder().add("sair", json).build();
         Obj obj = new Obj(json);
         
         for(Session session :peers){
             if(!session.equals(peer)){
                session.getBasicRemote().sendObject(obj);
             }
         }
    }
    
    public void removeContato(Session peer){
        for(Obj obj :objs){
            if(obj.getJson().getString("id").equalsIgnoreCase(peer.getId())){
                objs.remove(obj);
            }
        }
    }
    
    
}
