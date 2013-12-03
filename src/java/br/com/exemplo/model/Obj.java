/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.exemplo.model;

import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author danilo
 */
    public class Obj {

        private JsonObject json;

        public Obj(JsonObject json){
            this.json=json;
        }

        public JsonObject getJson() {
            return json;
        }

        public void setJson(JsonObject json) {
            this.json = json;
        }

        @Override
        public String toString() {
            StringWriter writer = new StringWriter();
            Json.createWriter(writer).write(json);
            return writer.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Obj)){
                return false;
            }
            Obj mObj=(Obj)obj;
            if(mObj.getJson().getString("id").equals(this.getJson().getString("id"))){
                return true;
            }

            return false;
        }




    }
