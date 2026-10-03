import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;
import java.io.File;
import java.io.FileWriter;
public class Entregable_5_SGA {
    public static void main(String[] args) {
        SGA sistema = new SGA();
        Scanner scanner = new Scanner(System.in);
        String opcion;

        do {
            System.out.println("\n--- SISTEMA DE GESTIÓN ACADÉMICA (SGA) ---");
            System.out.println("1. Registrar Alumno");
            System.out.println("2. Registrar Profesor");
            System.out.println("3. Registrar Notas");
            System.out.println("4. Deshacer Ultima Nota");
            System.out.println("5. Generar Cola de Certificados");
            System.out.println("6. Generar Reporte");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    sistema.registrarAlumno(scanner);
                    break;
                case "2":
                    sistema.registrarProfesor(scanner);
                    break;
                case "3":
                    sistema.registrarNotas(scanner);
                    break;
                case "4":
                    sistema.deshacerRegistro();
                    break;
                case "5":
                    sistema.generarCola();
                    break;
                case "6":
                    sistema.generarReporte(scanner);
                    break;
                case "7":
                    sistema.salir();
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (!opcion.equals("7"));
        scanner.close();
    }
}

class Persona {
    protected String cedula;
    protected String nombre;
    protected String correo;

    public Persona(String cedula, String nombre, String correo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
    }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}

class Alumno extends Persona {
    private String programa;
    private ArrayList<Double> notas;

    public Alumno(String cedula, String nombre, String correo, String programa) {
        super(cedula, nombre, correo);
        this.programa = programa;
        this.notas = new ArrayList<>();
    }
    public String getPrograma() { return programa; }
    public ArrayList<Double> getNotas() { return notas; }
    public void agregarNota(double nota) {
        if (this.notas.size() < 3){
            this.notas.add(nota);
        } else {
            System.out.println("No se pueden agregar más de 3 notas.");
        }
    }
    public double consultarPromedio() {
        if (notas.isEmpty()) return 0.0;
        double suma = 0;
        for (double n : notas) {
            suma += n;
        }
        return suma / notas.size();
    }
}

class Profesor extends Persona {
    private String especialidad;
    private String materia;

    public Profesor(String cedula, String nombre, String correo, String especialidad, String materia) {
        super(cedula, nombre, correo);
        this.especialidad = especialidad;
        this.materia = materia;
    }
    public String getEspecialidad() { return especialidad; }
    public String getMateria() { return materia; }
}

class ProgramaAcademico {
    protected String nombrePrograma;

    public ProgramaAcademico(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }
    public String getNombrePrograma() {
        return nombrePrograma;
    }
    public boolean evaluarAprobacion(ArrayList<Double> notas) {
        return false;
    }
}

class Curso extends ProgramaAcademico {
    public Curso() {
        super("Curso");
    }
    @Override
    public boolean evaluarAprobacion(ArrayList<Double> notas) {
        if (notas == null || notas.isEmpty()) return false;
        double suma = 0;
        for (double nota : notas) {
            suma += nota;
        }
        double promedio = suma / notas.size();
        return promedio >= 10;
    }
}

class Diplomado extends ProgramaAcademico {
    public Diplomado() {
        super("Diplomado");
    }
    @Override
    public boolean evaluarAprobacion(ArrayList<Double> notas) {
        if (notas == null || notas.isEmpty()) return false;
        double suma = 0;
        for (double nota : notas) {
            suma += nota;
        }
        double promedio = suma / notas.size();
        return promedio >= 14;
    }
}

class Bootcamp extends ProgramaAcademico {
    public Bootcamp() {
        super("Bootcamp");
    }
    @Override
    public boolean evaluarAprobacion(ArrayList<Double> notas) {
        if (notas == null || notas.isEmpty()) return false;
        for (double nota : notas) {
            if (nota < 14) {
                return false;
            }
        }
        return true;
    }
}

// Para poder juntar la cédula y la nota en una sola estructura para la pila de notas.
class RegistroNota {
    String cedula;
    double nota;

    public RegistroNota(String cedula, double nota) {
        this.cedula = cedula;
        this.nota = nota;
    }
    public String getCedula() {
        return cedula;
    }
    public double getNota() {
        return nota;
    }
}

// --- CLASE SISTEMA DE GESTIÓN ACADÉMICA ---
class SGA {
    private ArrayList<Alumno> listaAlumnos;
    private ArrayList<Profesor> listaProfesores;
    private Stack<RegistroNota> pilaNotas;
    private Deque<Alumno> colaCertificados;

    public SGA() {
        this.listaAlumnos = new ArrayList<>();
        this.listaProfesores = new ArrayList<>();
        this.pilaNotas = new Stack<>();
        this.colaCertificados = new ArrayDeque<>();
        cargarAlumnos();
        cargarProfesores();
    }

    public void registrarAlumno(Scanner scanner) {
        System.out.println("\n--- REGISTRO DE ALUMNO ---");
        System.out.print("Cédula: ");
        String cedula = scanner.nextLine();
        for (Alumno a : listaAlumnos) {
            if (a.getCedula().equals(cedula)) {
                System.out.println("Error: Ya existe un alumno con la cédula " + cedula + ".");
                return;
            }
        }
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.println("Seleccione el Programa:");
        System.out.println("1. Curso | 2. Diplomado | 3. Bootcamp");
        System.out.print("Opción: ");
        String tipoProg = scanner.nextLine();
        String prog;
        switch (tipoProg) {
            case "1":
                prog = "Curso";
                break;
            case "2":
                prog = "Diplomado";
                break;
            case "3":
                prog = "Bootcamp";
                break;
            default:
                System.out.println("Opción de programa no válida. Registro cancelado.");
                return;
        }
        Alumno nuevoAlumno = new Alumno(cedula, nombre, correo, prog);
        listaAlumnos.add(nuevoAlumno);
        guardarAlumno(nuevoAlumno);
        System.out.println("Alumno registrado exitosamente en memoria.");
    }

    public void guardarAlumno(Alumno alumno) {
        try {
            FileWriter escritor = new FileWriter("alumnos.txt", true);
            double n1 = alumno.getNotas().size() > 0 ? alumno.getNotas().get(0) : 0;
            double n2 = alumno.getNotas().size() > 1 ? alumno.getNotas().get(1) : 0;
            double n3 = alumno.getNotas().size() > 2 ? alumno.getNotas().get(2) : 0;

            String linea = alumno.getCedula() + ", " + 
                        alumno.getNombre() + ", " + 
                        alumno.getCorreo() + ", " + 
                        alumno.getPrograma() + ", " + 
                        n1 + ", " + n2 + ", " + n3 + "\n";

            escritor.write(linea);
            escritor.close();
        } catch (Exception e) {
            System.out.println("Error al guardar en alumnos.txt: " + e.getMessage());
        }
    }

    public void actualizarAlumnos() {
        try {
            FileWriter escritor = new FileWriter("alumnos.txt", false);
            for (Alumno alumno : listaAlumnos) {
                double n1 = alumno.getNotas().size() > 0 ? alumno.getNotas().get(0) : 0;
                double n2 = alumno.getNotas().size() > 1 ? alumno.getNotas().get(1) : 0;
                double n3 = alumno.getNotas().size() > 2 ? alumno.getNotas().get(2) : 0;

                String linea = alumno.getCedula() + ", " + 
                            alumno.getNombre() + ", " + 
                            alumno.getCorreo() + ", " + 
                            alumno.getPrograma() + ", " + 
                            n1 + ", " + n2 + ", " + n3 + "\n";

                escritor.write(linea);
            }
            escritor.close();
        } catch (Exception e) {
            System.out.println("Error al actualizar alumnos.txt: " + e.getMessage());
        }
    }
    public void cargarAlumnos() {
        File archivo = new File("alumnos.txt");
        if (!archivo.exists()) {
            return;
        }
        try {
            Scanner lector = new Scanner(archivo);
            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();
                if (linea.isEmpty()) continue;

                String[] datos = linea.split(",");
                if (datos.length == 7) {
                    String cedula = datos[0].trim();
                    String nombre = datos[1].trim();
                    String correo = datos[2].trim();
                    String progNombre = datos[3].trim();
                    Alumno alumno = new Alumno(cedula, nombre, correo, progNombre);
                    // Cargar notas mayores a 0
                    for (int i = 4; i < 7; i++) {
                        double nota = Double.parseDouble(datos[i].trim());
                        if (nota > 0) {
                            alumno.agregarNota(nota);
                        }
                    }
                    listaAlumnos.add(alumno);
                }
            }
            lector.close();
        } catch (Exception e) {
            System.out.println("Error al cargar alumnos.txt: " + e.getMessage());
        }
    }

    public void registrarProfesor(Scanner scanner) {
        System.out.println("\n--- REGISTRO DE PROFESOR ---");
        System.out.print("Cédula: ");
        String cedula = scanner.nextLine();
        for (Profesor prof : listaProfesores) {
            if (prof.getCedula().equals(cedula)) {
                System.out.println("Error: YA existe un profesor con la cédula " + cedula);
                return;
            }
        }
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine();
        System.out.print("Materia Asignada: ");
        String materia = scanner.nextLine();
        Profesor nuevoProfesor = new Profesor(cedula, nombre, correo, especialidad, materia);
        listaProfesores.add(nuevoProfesor);
        guardarProfesor(nuevoProfesor);
        System.out.println("Profesor " + nombre + " registrado exitosamente");
    }

    public void guardarProfesor(Profesor profesor) {
        try {
            FileWriter escritor = new FileWriter("profesores.txt", true);
            String linea = profesor.getCedula() + ", " + profesor.getNombre() + ", " + profesor.getCorreo() + ", " + profesor.getEspecialidad() + ", " + profesor.getMateria() + "\n";
            escritor.write(linea);
            escritor.close();
        } catch (Exception e) {
            System.out.println("Error al guardar en profesores.txt: " + e.getMessage());
        }
    }

    public void cargarProfesores() {
        File archivo = new File("profesores.txt");
        if (!archivo.exists()) {
            return;
        }
        try {
            Scanner lector = new Scanner(archivo);
            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();
                if (linea.isEmpty()) continue;
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    String cedula = datos[0].trim();
                    String nombre = datos[1].trim();
                    String correo = datos[2].trim();
                    String especialidad = datos[3].trim();
                    String materia = datos[4].trim();
                    Profesor profesor = new Profesor(cedula, nombre, correo, especialidad, materia);
                    listaProfesores.add(profesor);
                }
            }
            lector.close();
        } catch (Exception e) {
            System.out.println("Error al cargar profesores.txt: " + e.getMessage());
        }
    }

    public void registrarNotas(Scanner scanner) {
        System.out.println("\n--- REGISTRO DE NOTAS ---");
        System.out.print("Ingrese la cédula del alumno: ");
        String cedulaBuscar = scanner.nextLine();
        Alumno alumnoEncontrado = null;
        for (Alumno a : listaAlumnos) {
            if (a.getCedula().equals(cedulaBuscar)) {
                alumnoEncontrado = a;
                break;
            }
        }
        if (alumnoEncontrado != null) {
            try {
                System.out.print("Ingrese la nota (0-20): ");
                double nota = Double.parseDouble(scanner.nextLine());
                if (nota >= 0 && nota <= 20) {
                    alumnoEncontrado.agregarNota(nota);
                    actualizarAlumnos();
                    if (alumnoEncontrado.getNotas().size() <= 3) {
                        pilaNotas.push(new RegistroNota(alumnoEncontrado.getCedula(), nota));
                        System.out.println("Nota " + nota + " asignada a " + alumnoEncontrado.getNombre() + ".");
                        actualizarAlumnos();
                    } else {
                        System.out.println("Error: " + alumnoEncontrado.getNombre() + " ya tiene el máximo de 3 notas registradas.");
                    }
                } else {
                    System.out.println("Error: La nota debe estar en el rango de 0 a 20.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un valor numérico válido.");
            }
        } else {
            System.out.println("Alumno no encontrado.");
        }
    }

    public void deshacerRegistro() {
        System.out.println("\n--- DESHACER ÚLTIMA NOTA (Pila) ---");
        if (pilaNotas.isEmpty()) {
            System.out.println("No hay notas registradas para deshacer.");
            return;
        }
        RegistroNota ultima = pilaNotas.pop();
        String cedulaUltima = ultima.getCedula();
        double notaUltima = ultima.getNota();
        Alumno alumnoEncontrado = null;
        for (Alumno a : listaAlumnos) {
            if (a.getCedula().equals(cedulaUltima)) {
                alumnoEncontrado = a;
                break;
            }
        }
        if (alumnoEncontrado != null) {
            if (!alumnoEncontrado.getNotas().isEmpty()) {
                // Elimina la última nota agregada
                alumnoEncontrado.getNotas().remove(alumnoEncontrado.getNotas().size() - 1);
                actualizarAlumnos();
                System.out.println("Se eliminó la nota " + notaUltima + " de " + alumnoEncontrado.getNombre() + ".");
            } else {
                System.out.println("El alumno no tiene notas registradas para eliminar.");
            }
        } else {
            System.out.println("Alumno no encontrado.");
        }
    }

    public void generarCola() {
        System.out.println("\n--- GENERANDO COLA DE CERTIFICADOS ---");
        colaCertificados.clear();

        if (listaAlumnos.isEmpty()) {
            System.out.println("No hay alumnos registrados en el sistema.");
            return;
        }

        System.out.println("Alumnos aprobados en espera de certificado:\n");
        for (Alumno alumno : listaAlumnos) {
            boolean tresNotas = alumno.getNotas().size() == 3;
            // Evalúa aprobación con el promedio si getPrograma() devuelve un String
            boolean aprobado = (alumno.consultarPromedio() >= 10) && tresNotas;

            if (aprobado) {
                double promedio = alumno.consultarPromedio();
                colaCertificados.add(alumno);

                System.out.println("\nAlumno: " + alumno.getNombre() + " | Cédula: " + alumno.getCedula() + " | Correo: " + alumno.getCorreo());
                System.out.println("Programa: " + alumno.getPrograma() + " | Notas: " + alumno.getNotas() + " | Promedio: " + String.format("%.2f", promedio));
                System.out.println("----------------------------------------");
            }
        }

        if (!colaCertificados.isEmpty()) {
            System.out.println("\nTotal en Cola: " + colaCertificados.size());
            
            String nombresCola = "[";
            int contador = 0;
            for (Alumno a : colaCertificados) {
                nombresCola += a.getNombre();
                if (contador < colaCertificados.size() - 1) {
                    nombresCola += ", ";
                }
                contador++;
            }
            nombresCola += "]";
            
            System.out.println("Cola de Certificados (FIFO): " + nombresCola);

            // Escritura simple en archivo
            try (FileWriter writer = new FileWriter("certificados_pendientes.txt")) {
                writer.write("==========================================\n");
                writer.write("   REPORTE DE CERTIFICADOS PENDIENTES\n");
                writer.write("==========================================\n");
                writer.write("Total de graduandos en cola: " + colaCertificados.size() + "\n\n");

                int idx = 1;
                for (Alumno a : colaCertificados) {
                    double prom = a.consultarPromedio();
                    writer.write(idx + ". [" + a.getCedula() + "] " + a.getNombre() + "\n");
                    writer.write("   - Programa: " + a.getPrograma() + "\n");
                    writer.write("   - Promedio Final: " + String.format("%.2f", prom) + "\n");
                    writer.write("   - Estatus: APROBADO\n\n");
                    idx++;
                }
                writer.write("==========================================\n");
                writer.write("* Fin del reporte - Generado por SGA-DO *\n");
                System.out.println("\nReporte exportado exitosamente.");
            } catch (Exception e) {
                System.out.println("Error al exportar el reporte: " + e.getMessage());
            }
        } else {
            System.out.println("\nNo hay alumnos para generar certificados.");
        }
    }

    public void generarReporte(Scanner scanner) {
        System.out.println("\n--- Opciones de Reporte ---");
        System.out.println("1. Reporte General");
        System.out.println("2. Reporte de Alumnos");
        System.out.println("3. Reporte de Profesores");
        System.out.println("---------------------------");
        System.out.print("Seleccione un tipo de reporte: ");
        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                reporteGeneral();
                break;
            case "2":
                reporteAlumnos();
                break;
            case "3":
                reporteProfesores();
                break;
            default:
                System.out.println("Opción no válida.");
                break;
        }
    }

    public void reporteGeneral() {
        System.out.println("\n==========================================");
        System.out.println("         REPORTE GENERAL DEL SGA          ");
        System.out.println("==========================================");
        reporteAlumnos();
        System.out.println("------------------------------------------");
        reporteProfesores();
        System.out.println("==========================================");
    }

    public void reporteAlumnos() {
        System.out.println("\n--- REPORTE DE ALUMNOS REGISTRADOS ---");
        if (listaAlumnos.isEmpty()) {
            System.out.println("No hay alumnos registrados.");
            return;
        }
        for (Alumno alumno : listaAlumnos) {
            boolean tresNotas = alumno.getNotas().size() == 3;
            double promedio = alumno.consultarPromedio();
            boolean aprobado = (promedio >= 10) && tresNotas;
            String estatus = aprobado ? "APROBADO" : "REPROBADO/PENDIENTE";

            System.out.println("Alumno: " + alumno.getNombre() + " | Cédula: " + alumno.getCedula() + " | Correo: " + alumno.getCorreo());
            System.out.println("Programa: " + alumno.getPrograma() + " | Notas: " + alumno.getNotas() + " | Promedio: " + String.format("%.2f", promedio) + " | Estatus: " + estatus + "\n");
        }
    }

    public void reporteProfesores() {
        System.out.println("\n--- REPORTE DE PROFESORES ACTIVOS ---");
        if (listaProfesores.isEmpty()) {
            System.out.println("No hay profesores registrados.");
            return;
        }
        for (Profesor prof : listaProfesores) {
            System.out.println("Profesor: " + prof.getNombre() + " | Cédula: " + prof.getCedula() + " | Correo: " + prof.getCorreo());
            System.out.println("Especialidad: " + prof.getEspecialidad() + " | Materia Asignada: " + prof.getMateria() + "\n");
        }
    }

    public void salir() {
        System.out.println("\nGuardando cambios y cerrando el sistema...");
        listaAlumnos.clear();
        listaProfesores.clear();
        colaCertificados.clear();
        pilaNotas.clear();
        System.out.println("Memoria liberada y datos guardados exitosamente. Sistema cerrado.");
    }
}
