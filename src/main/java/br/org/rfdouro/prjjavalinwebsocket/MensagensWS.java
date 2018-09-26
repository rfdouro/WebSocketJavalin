/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.rfdouro.prjjavalinwebsocket;

import io.javalin.Javalin;
import io.javalin.websocket.WsSession;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author romulo.douro
 */
public class MensagensWS {

 private static Set<WsSession> sessoes = Collections.synchronizedSet(new HashSet<WsSession>());

 public static void main(String[] args) {
  int port = (args != null && args.length > 0 && args[0] != null) ? Integer.parseInt(args[0]) : 7000;

  Javalin app = Javalin.create()
          .enableCorsForOrigin("*") // enables cors for the specified origin(s)
          .start(port);

  app.get("/", ctx -> ctx.result("MensagensWS"));

  //app.ws("/websocket/:path", ws -> {
  app.ws("/websocket", ws -> {
   ws.onConnect(session -> {
    System.out.println("Connected");
    sessoes.add(session);
   });
   ws.onMessage((session, message) -> {
    System.out.println("Received: " + message);
    for (WsSession s : sessoes) {
     if (!s.equals(session) && s.isOpen()) {
      s.getRemote().sendString(message);
     }
    }
   });
   ws.onClose((session, statusCode, reason) -> System.out.println("Closed"));
   ws.onError((session, throwable) -> System.out.println("Errored"));
  });

 }
}
