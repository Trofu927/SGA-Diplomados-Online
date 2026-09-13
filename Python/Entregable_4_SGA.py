import os
# Entregable 4
class Persona:
    def __init__(self, cedula, nombre, correo):
        self.cedula = cedula
        self.nombre = nombre
        self.correo = correo

    def asignarDatos(self):
        return f"Cédula: {self.cedula} | Nombre: {self.nombre} | Correo: {self.correo}"
    def alinearTexto(self):
        return f"{self.cedula}, {self.nombre}, {self.correo}"

# Persona 1
class Profesor(Persona):
    def __init__(self, cedula, nombre, correo, especialidad, materia):
        super(). __init__(cedula, nombre, correo)
        self.especialidad = especialidad
        self.materia = materia

    def asignarMateria(self, nueva_materia):
        self.materia = nueva_materia

# Persona 2
class Alumno(Persona):
    def __init__(self, cedula, nombre, correo, programa):
        super(). __init__(cedula, nombre, correo)
        self.programa = programa
        self.notas = [] #Pila de notas

    def registrarNota(self, nueva_nota):
        if len(self.notas) < 3:
            self.notas.append(nueva_nota)
            return True
        else:
            return False
    def consultarPromedio(self):
        if not self.notas:
            return 0
        return sum(self.notas) / len(self.notas)

# Programas:
class Programa_Academico:
    def __init__(self, nombre_programa):
        self.nombre_programa = nombre_programa
    def evaluarAprobacion(self, notas):
        pass

class Curso(Programa_Academico):
    def __init__(self):
        super(). __init__("Curso")
    def evaluarAprobacion(self, notas):
        promedio = sum(notas) / len(notas) if notas else 0
        return promedio >= 10
class Diplomado(Programa_Academico):
    def __init__(self):
        super(). __init__("Diplomado")
    def evaluarAprobacion(self, notas):
        promedio = sum(notas) / len(notas) if notas else 0
        return promedio >= 14
class Bootcamp(Programa_Academico):
    def __init__(self):
        super(). __init__("Bootcamp")
    def evaluarAprobacion(self, notas):
        return all(nota >= 14 for nota in notas) if notas else False

# Sistema de Gestión Académica
class SGA:
    def __init__(self):
        self.lista_alumnos = []
        self.pila_notas = []
        self.lista_profesores = []
        self.cola_certificados = []
        self.cargar_alumnos()
        self.cargar_profesores()

    def Registrar_Alumno(self):
        print("\n--- REGISTRO DE ALUMNO ---")
        cedula = input("Cédula: ")
        for alumno in self.lista_alumnos:
            if alumno.cedula == cedula:
                print(f"Error: Ya existe un alumno con la cédula {cedula}.")
                return
        nombre = input("Nombre: ")
        correo = input("Correo: ")
        print("Seleccione el Programa:")
        print("1. Curso | 2. Diplomado | 3. Bootcamp")
        tipo_prog = input("Opción: ")
        match tipo_prog:
            case "1":
                prog = Curso()
            case "2":
                prog = Diplomado()
            case "3":
                prog = Bootcamp()
            case _:
                print("Opción de programa no valida. Registro cancelado.")
                return
        nuevo_alumno = Alumno(cedula, nombre, correo, prog)
        self.lista_alumnos.append(nuevo_alumno)
        self.guardar_alumno(nuevo_alumno)
        print("Alumno registrado exitosamente en memoria.")

    def guardar_alumno(self, alumno):
        with open("alumnos.txt", "a", encoding="utf-8") as f:
            n1 = alumno.notas[0] if len(alumno.notas) > 0 else 0
            n2 = alumno.notas[1] if len(alumno.notas) > 1 else 0
            n3 = alumno.notas[2] if len(alumno.notas) > 2 else 0
            f.write(f"{alumno.cedula}, {alumno.nombre}, {alumno.correo}, {alumno.programa.nombre_programa}, {n1}, {n2}, {n3}\n")

    def actualizar_alumnos_txt(self):
        """Sobrescribe alumnos.txt para actualizar las notas modificadas"""
        with open("alumnos.txt", "w", encoding="utf-8") as f:
            for alumno in self.lista_alumnos:
                n1 = alumno.notas[0] if len(alumno.notas) > 0 else 0
                n2 = alumno.notas[1] if len(alumno.notas) > 1 else 0
                n3 = alumno.notas[2] if len(alumno.notas) > 2 else 0
                f.write(f"{alumno.cedula}, {alumno.nombre}, {alumno.correo}, {alumno.programa.nombre_programa}, {n1}, {n2}, {n3}\n")

    def cargar_alumnos(self):
        if not os.path.exists("alumnos.txt"):
            return

        with open("alumnos.txt", "r", encoding="utf-8") as f:
            for linea in f:
                datos = [d.strip() for d in linea.strip().split(",")]
                if len(datos) == 7:
                    cedula, nombre, correo, prog_nombre = datos[0], datos[1], datos[2], datos[3]
                    if prog_nombre == "Curso": prog = Curso()
                    elif prog_nombre == "Diplomado": prog = Diplomado()
                    elif prog_nombre == "Bootcamp": prog = Bootcamp()
                    else: continue
                    alumno = Alumno(cedula, nombre, correo, prog)
                    # Carga únicamente las notas que sean mayores a 0
                    alumno.notas = [float(n) for n in datos[4:] if float(n) > 0]
                    self.lista_alumnos.append(alumno)

    def Registrar_Profesor(self):
        print("\n--- REGISTRO DE PROFESOR ---")
        cedula = input("Cédula: ")
        for prof in self.lista_profesores:
            if prof.cedula == cedula:
                print(f"Error: YA existe un profesor con la cédula {cedula}")
                return
        nombre = input("Nombre: ")
        correo = input("Correo: ")
        especialidad = input("Especialidad: ")
        materia = input("Materia Asignada: ")
        nuevo_profesor = Profesor(cedula, nombre, correo, especialidad, materia)
        self.lista_profesores.append(nuevo_profesor)
        self.guardar_profesor(nuevo_profesor)
        print(f"Profesor {nombre} registrado exitosamente")

    def guardar_profesor(self, profesor):
        with open("profesores.txt", "a", encoding="utf-8") as f:
            f.write(f"{profesor.cedula}, {profesor.nombre}, {profesor.correo}, {profesor.especialidad}, {profesor.materia}\n")

    def cargar_profesores(self):
        if not os.path.exists("profesores.txt"):
            return
        with open("profesores.txt", "r", encoding="utf-8") as f:
            for linea in f:
                datos = [d.strip() for d in linea.strip().split(",")]
                if len(datos) == 5:
                    cedula, nombre, correo, especialidad, materia = datos
                    profesor = Profesor(cedula, nombre, correo, especialidad, materia)
                    self.lista_profesores.append(profesor)

    def Registrar_Notas(self):
        print("\n--- REGISTRO DE NOTAS ---")
        cedula_buscar = input("Ingrese la cédula del alumno: ")
        alumno_encontrado = None
        for a in self.lista_alumnos:
            if a.cedula == cedula_buscar:
                alumno_encontrado = a
                break
        if alumno_encontrado:
            try:
                nota = float(input("Ingrese la nota (0-20): "))
                if 0 <= nota <= 20:
                    exito = alumno_encontrado.registrarNota(nota)
                    self.actualizar_alumnos_txt()
                    if exito:
                        self.pila_notas.append((alumno_encontrado.cedula, nota))
                        print(f"Nota {nota} asignada a {alumno_encontrado.nombre}.")
                        self.actualizar_alumnos_txt()
                    else:
                        print(f"Error: {alumno_encontrado.nombre} ya tiene el máximo de 3 notas registradas.")
                else:
                    print("Error: La nota debe estar en el rango de 0 a 20.")
            except ValueError:
                print("Error: Debe ingresar un valor numérico válido.")
        else:
            print("Alumno no encontrado.")

    def Deshacer_Registro(self):
        print("\n--- DESHACER ÚLTIMA NOTA (Pila) ---")
        if not self.pila_notas:
            print("No hay notas registradas para deshacer.")
            return
        cedula_ultima, nota_ultima = self.pila_notas.pop()
        for a in self.lista_alumnos:
            if a.cedula == cedula_ultima:
                if a.notas:
                    a.notas.pop()
                    self.actualizar_alumnos_txt()
                    print(f"Se eliminó la nota {nota_ultima} de {a.nombre}.")
                else:
                    print("El alumno no tiene notas registradas para eliminar.")
                return
        print("Alumno no encontrado.")

    def Generar_Cola(self):
        print("\n--- GENERANDO COLA DE CERTIFICADOS ---")
        self.cola_certificados.clear()  # Limpia la cola previa
        if not self.lista_alumnos:
            print("No hay alumnos registrados en el sistema.")
            return
        print("Alumnos aprobados en espera de certificado:\n")
        for alumno in self.lista_alumnos:
            tres_notas = len(alumno.notas) == 3
            aprobado = alumno.programa.evaluarAprobacion(alumno.notas) and tres_notas
            if aprobado:
                promedio = alumno.consultarPromedio()
                self.cola_certificados.append(alumno)
                print(f"\nAlumno: {alumno.nombre} | Cédula: {alumno.cedula} | Correo: {alumno.correo}")
                print(f"Programa: {alumno.programa.nombre_programa} | Notas: {alumno.notas} | Promedio: {promedio:.2f}")
        print("\n--------------------------------------")
        if self.cola_certificados:
            print(f"Total en Cola: {len(self.cola_certificados)}")
            nombres_cola = [a.nombre for a in self.cola_certificados]
            print(f"Cola de Certificados (FIFO): {nombres_cola}")
            # --- CREACIÓN Y ESCRITURA EN EL ARCHIVO TXT ---
            with open("certificados_pendientes.txt", "w", encoding="utf-8") as f:
                f.write("=========================================\n")
                f.write("   REPORTE DE CERTIFICADOS PENDIENTES    \n")
                f.write("=========================================\n")
                f.write(f"Total de graduandos en cola: {len(self.cola_certificados)}\n\n")
                for idx, a in enumerate(self.cola_certificados, 1):
                    prom = a.consultarPromedio()
                    f.write(f"{idx}. [{a.cedula}] {a.nombre}\n")
                    f.write(f"   - Programa: {a.programa.nombre_programa}\n")
                    f.write(f"   - Promedio Final: {prom:.2f}\n")
                    f.write(f"   - Estatus: APROBADO\n\n")
                f.write("=========================================\n")
                f.write("* Fin del reporte - Generado por SGA-DO *\n")
            print("\nReporte exportado exitosamente.")
        else:
            print("No hay alumnos para generar certificados.")

    def Generar_Reporte(self):
        print("\n--- Opciones de Reporte ---\n")
        print("1. Reporte General")
        print("2. Reporte de Alumnos")
        print("3. Reporte de Profesores")
        print("---------------------------")
        opcion = input("Seleccione un tipo de reporte: ")

        match opcion:
            case "1":
                self.Reporte_General()
            case "2":
                self.Reporte_Alumnos()
            case "3":
                self.Reporte_Profesores()
            case _:
                print("Opción no válida.")

    def Reporte_General(self):
        print("\n=========================================")
        print("        REPORTE GENERAL DEL SGA          ")
        print("=========================================")
        self.Reporte_Alumnos()
        print("-----------------------------------------")
        self.Reporte_Profesores()
        print("=========================================")
        
    def Reporte_Alumnos(self):
        print("\n--- REPORTE DE ALUMNOS REGISTRADOS ---")
        if not self.lista_alumnos:
            print("No hay alumnos registrados.")
            return
        for alumno in self.lista_alumnos:
            tres_notas = len(alumno.notas) == 3
            aprobado = (alumno.programa.evaluarAprobacion(alumno.notas) and tres_notas)
            estatus = "APROBADO" if aprobado else "REPROBADO/PENDIENTE"
            promedio = alumno.consultarPromedio()
            print(f"Alumno: {alumno.nombre} | Cédula: {alumno.cedula} | Correo: {alumno.correo}")
            print(f"Programa: {alumno.programa.nombre_programa} | Notas: {alumno.notas} | Promedio: {promedio:.2f} | Estatus: {estatus}\n")

    def Reporte_Profesores(self):
        print("\n--- REPORTE DE PROFESORES ACTIVOS ---")
        if not self.lista_profesores:
            print("No hay profesores registrados.")
            return
        for prof in self.lista_profesores:
            print(f"Profesor: {prof.nombre} | Cédula: {prof.cedula} | Correo: {prof.correo}")
            print(f"Especialidad: {prof.especialidad} | Materia Asignada: {prof.materia}\n")

    def Salir(self):
        print("\nGuardando cambios y cerrando el sistema...")
        if hasattr(self, 'actualizar_alumnos_txt'):
            self.actualizar_alumnos_txt()
        #  Se limpia las estructuras en memoria RAM
        self.lista_alumnos.clear()
        self.lista_profesores.clear()
        self.cola_certificados.clear()
        print("Memoria liberada y datos guardados exitosamente. Sistema cerrado.")

    def Mostrar_Opciones(self):
        while True:
            print("\n==========================================")
            print("=== SISTEMA DE GESTIÓN ACADÉMICA (SGA) ===")
            print("==========================================")
            print("1. Registrar Alumno")
            print("2. Registrar Profesor")
            print("3. Registrar Nota")
            print("4. Deshacer Última Nota")
            print("5. Generar Cola de Certificados")
            print("6. Generar Reporte")
            print("7. Salir")
            print("==========================================")

            opcion = input("Seleccione una opción: ")

            match opcion:
                case "1":
                    self.Registrar_Alumno()
                case "2":
                    self.Registrar_Profesor()
                case "3":
                    self.Registrar_Notas()
                case "4":
                    self.Deshacer_Registro()
                case "5":
                    self.Generar_Cola()
                case "6":
                    self.Generar_Reporte()
                case "7":
                    self.Salir()
                    break
                case _:
                    print("Opción no válida. Intente nuevamente.")
sistema = SGA()
sistema.Mostrar_Opciones()