package cl.duoc.vidasalud.ms_vidasalud_notify.service;

import cl.duoc.vidasalud.ms_vidasalud_notify.event.AppointmentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendNotification(AppointmentEvent event) {
        if (event.getId() == null || event.getPatientId() == null || event.getStatus() == null) {
            throw new IllegalArgumentException("Evento invalido: faltan datos obligatorios");
        }
        // Aquí iría el envío real (email/SMS). Por ahora se simula con un log.
        log.info("Notificacion enviada -> paciente={} atencion={} estado={}",
                event.getPatientId(), event.getId(), event.getStatus());
    }
}