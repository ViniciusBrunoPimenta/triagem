import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

// Classe para representar o Paciente
class Patient implements Comparable<Patient> {
    private String name;
    private String cpf;
    private String phone;
    private String address;
    private String symptoms;
    private int riskLevel;
    private Date entryTime;

    public Patient(String name, String cpf, String phone, String address, String symptoms, int riskLevel) {
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.address = address;
        this.symptoms = symptoms;
        this.riskLevel = riskLevel;
        this.entryTime = new Date();
    }

    // Implementação do Comparable para ordenação por prioridade
    @Override
    public int compareTo(Patient other) {
        // Primeiro compara por nível de risco (menor número = maior prioridade)
        int riskComparison = Integer.compare(this.riskLevel, other.riskLevel);
        if (riskComparison != 0) {
            return riskComparison;
        }
        // Se mesmo nível de risco, compara por ordem de chegada
        return this.entryTime.compareTo(other.entryTime);
    }

    // Getters e Setters
    public String getName() { return name; }
    public String getCpf() { return cpf; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getSymptoms() { return symptoms; }
    public int getRiskLevel() { return riskLevel; }
    public Date getEntryTime() { return entryTime; }
}

// Classe para gerenciar a fila de prioridade
class HospitalQueue {
    private List<Patient> patients;

    public HospitalQueue() {
        this.patients = new ArrayList<>();
    }

    // Adiciona um paciente à fila e reordena
    public void addPatient(Patient patient) {
        patients.add(patient);
        Collections.sort(patients);
        
        // Emite alerta baseado no nível de risco
        emitAlertForRiskLevel(patient);
    }

    // Remove e retorna o próximo paciente a ser atendido
    public Patient callNextPatient() {
        if (patients.isEmpty()) {
            return null;
        }
        return patients.remove(0);
    }

    // Remove um paciente específico da fila
    public boolean removePatient(String cpf) {
        return patients.removeIf(p -> p.getCpf().equals(cpf));
    }

    // Retorna a lista atual de pacientes
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    // Retorna o número de pacientes na fila
    public int getQueueSize() {
        return patients.size();
    }

    // Método para obter o texto do nível de risco
    public static String getRiskLevelText(int level) {
        switch (level) {
            case 1: return "Emergência";
            case 2: return "Muito Urgente";
            case 3: return "Urgente";
            case 4: return "Pouco Urgente";
            case 5: return "Não Urgente";
            default: return "Nível Desconhecido";
        }
    }

    // Novo método para emitir alertas
    private void emitAlertForRiskLevel(Patient patient) {
        String alertMessage = "NOVO PACIENTE: " + patient.getName() + "\n";
        String riskText = getRiskLevelText(patient.getRiskLevel());
        switch (patient.getRiskLevel()) {
            case 1:
                alertMessage += "⚠️ EMERGÊNCIA - ATENÇÃO MÁXIMA REQUERIDA! ⚠️\n" +
                              "Sintomas: " + patient.getSymptoms() + "\n" +
                              "Encaminhar imediatamente para atendimento!";
                System.err.println("\u001B[31m" + alertMessage + "\u001B[0m"); // Vermelho
                break;
                
            case 2:
                alertMessage += "⚠️ MUITO URGENTE\n" +
                              "Sintomas: " + patient.getSymptoms() + "\n" +
                              "Atendimento prioritário necessário!";
                System.err.println("\u001B[33m" + alertMessage + "\u001B[0m"); // Amarelo
                break;
                
            case 3:
                alertMessage += "! URGENTE !\n" +
                              "Sintomas: " + patient.getSymptoms();
                System.out.println("\u001B[34m" + alertMessage + "\u001B[0m"); // Azul
                break;
                
            case 4:
                alertMessage += "Pouco Urgente\n" +
                              "Sintomas: " + patient.getSymptoms();
                System.out.println("\u001B[32m" + alertMessage + "\u001B[0m"); // Verde
                break;
                
            case 5:
                alertMessage += "Não Urgente\n" +
                              "Sintomas: " + patient.getSymptoms();
                System.out.println("\u001B[37m" + alertMessage + "\u001B[0m"); // Branco
                break;
        }
        
        // Adiciona informações sobre o tempo estimado de espera
        System.out.println("Tempo estimado de espera: " + getEstimatedWaitTime(patient.getRiskLevel()));
    }

    // Novo método para calcular tempo estimado de espera
    private String getEstimatedWaitTime(int riskLevel) {
        switch (riskLevel) {
            case 1: return "Atendimento Imediato";
            case 2: return "10-15 minutos";
            case 3: return "Até 30 minutos";
            case 4: return "Até 60 minutos";
            case 5: return "Até 120 minutos";
            default: return "Tempo indeterminado";
        }
    }
}

// Classe principal para demonstração
public class Main {
    public static void main(String[] args) {
        HospitalQueue queue = new HospitalQueue();

        // Testando diferentes níveis de urgência
        Patient emergencyPatient = new Patient(
            "João Silva",
            "123.456.789-00",
            "(11) 99999-9999",
            "Rua A, 123",
            "Dor no peito e falta de ar",
            1  // Emergência
        );

        Patient urgentPatient = new Patient(
            "Maria Santos",
            "987.654.321-00",
            "(11) 88888-8888",
            "Rua B, 456",
            "Febre alta",
            2  // Muito Urgente
        );

        Patient normalPatient = new Patient(
            "Pedro Oliveira",
            "456.789.123-00",
            "(11) 77777-7777",
            "Rua C, 789",
            "Dor de cabeça",
            4  // Pouco Urgente
        );

        // Adicionando pacientes para demonstrar os diferentes alertas
        System.out.println("\n=== Adicionando Paciente de Emergência ===");
        queue.addPatient(emergencyPatient);

        System.out.println("\n=== Adicionando Paciente Muito Urgente ===");
        queue.addPatient(urgentPatient);

        System.out.println("\n=== Adicionando Paciente Pouco Urgente ===");
        queue.addPatient(normalPatient);

        // Demonstração da fila atual
        System.out.println("\n=== Status Atual da Fila ===");
        System.out.println("Total de pacientes na fila: " + queue.getQueueSize());
    }
}
