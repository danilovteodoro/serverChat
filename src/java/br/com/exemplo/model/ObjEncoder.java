/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.exemplo.model;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author danilo
 */
public class ObjEncoder implements Encoder.Text<Obj>{

    @Override
    public String encode(Obj obj) throws EncodeException {
        
        return obj.getJson().toString();
    }

    @Override
    public void init(EndpointConfig config) {
        System.out.println("ObjEncoder init.");
      
    }

    @Override
    public void destroy() {
        System.out.println("ObjEncoder destroy.");
    }
    
}
