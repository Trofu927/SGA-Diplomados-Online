# entregable 4
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
        self.lista_profesores = []
        self.cola_certificados = []

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
        print("Alumno registrado exitosamente en memoria.")

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
                    if exito:
                        print(f"Nota {nota} asignada a {alumno_encontrado.nombre}.")
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
        cedula_buscar = input("Ingrese la cédula del alumno: ")
        for a in self.lista_alumnos:
            if a.cedula == cedula_buscar:
                if a.notas:
                    nota_removida = a.notas.pop()  # Comportamiento de Pila (LIFO)
                    print(f"Se eliminó la nota {nota_removida} de {a.nombre}.")
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
        for alumno in self.lista_alumnos:
            promedio = alumno.consultarPromedio()
            tres_notas = len(alumno.notas) == 3
            aprobado = alumno.programa.evaluarAprobacion(alumno.notas) and tres_notas
            # Muestra el estado del alumno
            estado = "APROBADO" if aprobado else "REPROBADO / INCOMPLETO"
            print(f"\nAlumno: {alumno.nombre} | Programa: {alumno.programa.nombre_programa}")
            print(f"Notas: {alumno.notas} | Promedio: {promedio:.2f} | Estado: {estado}")
        if aprobado:
            self.cola_certificados.append(alumno.nombre)
            print("\n--------------------------------------")
            print(f"Cola de Certificados en espera: {self.cola_certificados}")

    def Opcion_Volver(self):
        print("Saliendo del sistema...")

    def Mostrar_Opciones(self):
        while True:
            print("\n=== SISTEMA DE GESTIÓN ACADÉMICA (SGA) ===")
            print("1. Registrar Alumno")
            print("2. Registrar Notas")
            print("3. Deshacer última nota")
            print("4. Generar Cola de Certificados")
            print("5. Salir")

            opcion = input("Seleccione una opción: ")

            match opcion:
                case "1":
                    self.Registrar_Alumno()
                case "2":
                    self.Registrar_Notas()
                case "3":
                    self.Deshacer_Registro()
                case "4":
                    self.Generar_Cola()
                case "5":
                    self.Opcion_Volver()
                    break
                case _:
                    print("Opción no válida. Intente nuevamente.")
sistema = SGA()
sistema.Mostrar_Opciones()