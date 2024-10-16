package pe.edu.vallegrande.aistudio.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.vallegrande.aistudio.model.ConversationsModel;
import pe.edu.vallegrande.aistudio.service.ConversationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/service/conversation")
public class ConversationsRest {

    private final ConversationsService conversationsService;

    @Autowired
    public ConversationsRest(ConversationsService conversationsService) {
        this.conversationsService = conversationsService;
    }

   // Obtener todas las conversaciones
    @GetMapping
    public Flux<ConversationsModel> getAllConversations() {
        log.info("Solicitando todas las conversaciones");
        return conversationsService.findAll();
    }
    
    // Obtener una conversación por ID
    @GetMapping("/id/{conversation_id}")
    public Mono<ResponseEntity<ConversationsModel>> getConversationById(@PathVariable Long conversation_id) {
        log.info("Solicitando conversación con ID: " + conversation_id);
        return conversationsService.findById(conversation_id)
                .map(conversation -> new ResponseEntity<>(conversation, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Obtener conversaciones por estado
    @GetMapping("/active/{active}")
    public Flux<ConversationsModel> getConversationsByActive(@PathVariable String active) {
        log.info("Solicitando conversaciones con estado: " + active);
        return conversationsService.findByActive(active);
    }
    
    @PostMapping("/save")
   public Mono<ConversationsModel> createConversation(@RequestBody ConversationsModel conversation) {
        log.info("Creando una nueva conversación");
        return conversationsService.insertConversation(conversation);
    }

   // Actualizar una conversación existente
    @PutMapping("/update/{conversation_id}")
    public Mono<ResponseEntity<ConversationsModel>> updateConversation(
            @PathVariable Long conversation_id, @RequestBody ConversationsModel updatedConversation) {
        log.info("Actualizando conversación con ID: " + conversation_id);
        return conversationsService.updateConversation(conversation_id, updatedConversation);
    }

    // Eliminar lógicamente una conversación
    @DeleteMapping("/delete/{conversation_id}")
    public Mono<ResponseEntity<ConversationsModel>> deleteConversation(@PathVariable Long conversation_id) {
        log.info("Eliminando conversación con ID: " + conversation_id);
        return conversationsService.delete(conversation_id);
    }

   // Restaurar una conversación
    @PutMapping("/restore/{conversation_id}")
    public Mono<ResponseEntity<ConversationsModel>> restoreConversation(@PathVariable Long conversation_id) {
        log.info("Restaurando conversación con ID: " + conversation_id);
        return conversationsService.restore(conversation_id);
    }

    // Eliminar físicamente una conversación
    @DeleteMapping("/permanent/{conversation_id}")
    public Mono<Void> deleteConversationPermanently(@PathVariable Long conversation_id) {
        log.info("Eliminando físicamente la conversación con ID: " + conversation_id);
        return conversationsService.deleteConversationPermanently(conversation_id);
    }

}
