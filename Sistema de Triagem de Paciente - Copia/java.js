class Patient {
    constructor(name, cpf, phone, address, symptoms, riskLevel) {
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.address = address;
        this.symptoms = symptoms;
        this.riskLevel = riskLevel;
        this.entryTime = new Date();
    }
}

class PriorityQueue {
    constructor() {
        this.patients = [];
    }

    enqueue(patient) {
        this.patients.push(patient);
        this.patients.sort((a, b) => a.riskLevel - b.riskLevel);
        this.updateQueueDisplay();
    }

    dequeue() {
        if (this.patients.length === 0) return null;
        const patient = this.patients.shift();
        this.updateQueueDisplay();
        return patient;
    }

    updateQueueDisplay() {
        const queueDiv = document.getElementById('patientQueue');
        queueDiv.innerHTML = '';

        this.patients.forEach(patient => {
            const card = document.createElement('div');
            card.className = `patient-card ${this.getRiskClass(patient.riskLevel)}`;
            card.innerHTML = `
                <h3>${patient.name}</h3>
                <p>Nível de Risco: ${this.getRiskText(patient.riskLevel)}</p>
                <button onclick="removePatient('${patient.cpf}')">Atendido</button>
            `;
            queueDiv.appendChild(card);
        });
    }

    getRiskClass(level) {
        const classes = ['emergency', 'very-urgent', 'urgent', 'semi-urgent', 'non-urgent'];
        return classes[level - 1];
    }

    getRiskText(level) {
        const texts = ['Emergência', 'Muito Urgente', 'Urgente', 'Pouco Urgente', 'Não Urgente'];
        return texts[level - 1];
    }

    removePatientByCPF(cpf) {
        this.patients = this.patients.filter(p => p.cpf !== cpf);
        this.updateQueueDisplay();
    }
}

const queue = new PriorityQueue();

document.getElementById('patientForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const patient = new Patient(
        document.getElementById('name').value,
        document.getElementById('cpf').value,
        document.getElementById('phone').value,
        document.getElementById('address').value,
        document.getElementById('symptoms').value,
        parseInt(document.getElementById('riskLevel').value)
    );

    queue.enqueue(patient);
    this.reset();
});

function removePatient(cpf) {
    queue.removePatientByCPF(cpf);
    
}