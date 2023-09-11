import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

// Definición de la clase Proceso
class Proceso {
    String pcb;     // Identificador del proceso (PCB)
    String id;      // ID único del proceso
    int instru;     // Número de instrucciones pendientes
    String estado;  // Estado del proceso (N: Nuevo, L: Listo, E: Ejecución, T: Terminado)
    int posic;      // Posición en la cola de listos

    // Constructor de la clase Proceso
    Proceso() {
        // Inicialización de los atributos del proceso
    }
}

// Definición de la clase RoundRobin
class RoundRobin {
    int n = 4;           // Cantidad de procesos predeterminada
    int quantum = 10;    // Valor del quantum predeterminado
    String archivo;      // Nombre del archivo de salida
    Proceso[] procesos;  // Arreglo de objetos Proceso
    ArrayList<String> buffer; // Almacena información para guardar en el archivo

    // Constructor de la clase RoundRobin
    RoundRobin(String archivo) {
        this.archivo = archivo;
        this.procesos = new Proceso[n];
        this.buffer = new ArrayList<>();
        Proceso p;
        for (int i = 0; i < this.n; i++) {
            p = new Proceso();
            p.pcb = "P" + Integer.toString(i+1);
            p.id = "0" + Integer.toString(i+1);
            p.instru = 20;
            p.estado = "N";
            p.posic = i + 1;
            procesos[i] = p;
        }
        tituloBuffer();
        colaListo();
        procesarCola();
        System.out.println(this.archivo + " guardado correctamente");
    }

    public static void main(String[] args) {
        new RoundRobin("jatest.txt");
    }
     

    private void tituloBuffer() {
        this.buffer.add("Algoritmo de planificacion Round Robin\n");
        this.buffer.add("N(Nuevo) L(Listo) E(Ejecucion) T(Terminado)\n");
        this.buffer.add("Cantidad de procesos: " + this.n + "\n");
        this.buffer.add("Quantum: " + this.quantum + "\n");
        guardarBuffer();
    }

    private void guardarBuffer() {
        this.buffer.add("\nProceso");
        for(Proceso p : procesos) {
            this.buffer.add("\t" + p.pcb);
        }
        this.buffer.add("\nID\t");
        for(Proceso p : procesos) {
            this.buffer.add("\t" + p.id);
        }
        this.buffer.add("\nInstruc");
        for(Proceso p : procesos) {
            this.buffer.add("\t" + Integer.toString(p.instru));
        }
        this.buffer.add("\nEstado");
        for(Proceso p : procesos) {
            this.buffer.add("\t" + p.estado);
        }
        this.buffer.add("\nPosic");
        for(Proceso p : procesos) {
            this.buffer.add("\t" + Integer.toString(p.posic));
        }
        this.buffer.add("\n");
        guardarArchivo();
    }

    private void colaListo() {
        for (int i = 0; i < this.n; i++) {
            this.procesos[i].estado = "L";
        }
        guardarBuffer();
    }

    private void procesarCola() {
        while (procesosNoTerminados()) {
            for (Proceso p : procesos) {
                trabajarProceso(p);
            } 
        }
        reposicionarCola();
        guardarBuffer();
    }

    private void reposicionarCola() {
        int posicion = 0;
        for (Proceso p : procesos) {
            if (p.estado.equals("T")) {
                p.posic = 0;
            } else {
                posicion += 1;
                p.posic = posicion;
            }
        }
    }

    private boolean procesosNoTerminados() {
        for (Proceso p : procesos) {
            if (!"T".equals(p.estado)) {
                return true;
            }
        }
        return false;
    }

    private void trabajarProceso(Proceso p) {
        if (p.instru > quantum) {
            p.instru -= quantum;
            p.estado = "E";
            guardarBuffer();
            p.estado = (p.instru == 0) ? "T" : "L";
        } else if (!"T".equals(p.estado)) {
            p.instru = 0;
            p.estado = "E";
            reposicionarCola();
            guardarBuffer();
            p.estado = "T";
        }
    }

    private void guardarArchivo() {
        try {
            String buf = String.join("", this.buffer);
            FileWriter fw = new FileWriter(this.archivo, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(buf);
            pw.close();
            this.buffer.clear();
        } catch (Exception e) {
            System.out.println("Error al guardar " + this.archivo);
        }
    }
}