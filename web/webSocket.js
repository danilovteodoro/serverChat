 
            var websocket;
            var nome="";
            
            window.addEventListener('beforeunload',fecharConexao);
            function fecharConexao(){
              
                websocket.close();
               
            }
            function conectar(){
                nome = document.getElementById('nome').value;
                var url= "ws://"+document.location.host+"/EchoServer/chat/"+nome;
                websocket=new WebSocket(url);
                
                websocket.onopen=function(event){
                    onOpen(event);
                };
                websocket.onclose=function(event){
                    onClose(event);
                };
                
                websocket.onmessage=function(event){
                    onMessage(event);
                };
                
                document.getElementById('display').style.display='block';
                document.getElementById('novo').style.display='none';
                document.getElementById('lblNome').innerHTML="<h3>"+nome+"</h3>";
                
            }
            
            function onOpen(event){
                
               // websocket.send("null");
            }
            function onClose(event){
                
            }
            
            function onMessage(event){
                var obj = JSON.parse(event.data);
                var mContatos =obj.contatos;
                var entrar = obj.entrar;
                var sair= obj.sair;
                var msg = obj.msg;
                
                
                if(typeof mContatos !=='undefined'){
                    contatos=mContatos;
                    carregaContatos();
                    
                   
                }
                
               else if(typeof entrar!=='undefined'){
                    contatos.push(entrar);
                    carregaContatos();
                    writeMessage(entrar.nome+" acabou de entrar na sala");
                }
                
                else if(typeof sair !=='undefined'){
                    
                    removeContatos(sair);
                    carregaContatos();
                    writeMessage(sair.nome+" deixou a sala");
                }
                else if(typeof msg!=='undefined'){
                    writeMessage("<b>"+msg.nome +" disse : </b>"+msg.message);
                }
                
                
            }
            
            function carregaContatos(){
                var html="";
                var cmbContatos =document.getElementById('lstContatos');
                cmbContatos.options.length=0;
                var index=0;
                cmbContatos.options[index]=new Option("All","All",true);
                index++;
                for(i=0;i<contatos.length;i++){
                       html+="<tr><td>"+contatos[i].nome+"</td></tr>";
                         if(contatos[i].nome!==nome){
                             cmbContatos.options[index]=new Option(contatos[i].nome,contatos[i].id,false);
                             index++;
                         }
                   }
                   document.getElementById('tblNiks').innerHTML=html;
                   
            }
            
            function writeMessage(msg){
                document.getElementById('mensagens').innerHTML+=msg+"<br>";
            }
            
            function removeContatos(obj) {
                 var arrAux = new Array();
                 for (i = 0; i < contatos.length; i++) {
                   var ob = contatos[i];
                   if (obj.id === ob.id) {
                     arrAux = arrAux.concat(contatos.slice(0, i), contatos.slice(i + 1, contatos.length));
                     contatos = arrAux;
                     break;
        }
    }

}

function enviar(){
   
    var sendTo=document.getElementById('lstContatos').value;
    var mesg = document.getElementById('send').value;
   var jsonMsg = JSON.stringify({
       "id":sendTo,
       "msg":{
           "nome":nome,
           "message":mesg   
       }
       
    });

 
   websocket.send(jsonMsg);
   document.getElementById('send').value="";
}

