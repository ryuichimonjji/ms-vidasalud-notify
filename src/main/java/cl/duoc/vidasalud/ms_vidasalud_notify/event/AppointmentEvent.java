package cl.duoc.vidasalud.ms_vidasalud_notify.event;

import cl.duoc.vidasalud.ms_vidasalud_notify.model.AppointmentStatus;
import java.io.Serializable;
import java.time.LocalDateTime;

public class AppointmentEvent implements Serializable {

    private Long id;
    private String patientId;
    private String serviceId;
    private AppointmentStatus status;
    private LocalDateTime timestamp;

    public AppointmentEvent() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}