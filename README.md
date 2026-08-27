# Sistema de Gestión Académica (SGA-DO) 🎓

<img width="460" height="275" alt="image" src="https://github.com/user-attachments/assets/62181062-25a9-4031-8584-fdb95c546590" />


## 📌 Descripción del Proyecto
El **SGA-DO** es el motor backend por consola desarrollado para **DiplomadosOnline.com** para solucionar la crisis operativa del manejo manual de datos académicos. El sistema reemplaza el control tradicional de hojas de cálculo por una arquitectura robusta con persistencia de datos en archivos planos `.txt`.

Como parte del diplomado, la misma solución se construye progresivamente en tres lenguajes de programación distintos (Python, Java y C++) para evaluar sus diferencias y ventajas en entornos de desarrollo real.

---

## 🏗️ Arquitectura y Conceptos Clave
- **Programación Orientada a Objetos (POO):** Modelado de clases mediante herencia y polimorfismo (`Persona` -> `Alumno`/`Profesor`, y `ProgramaAcademico` -> `Curso`/`Diplomado`/`Bootcamp`)[cite: 1].
- **Pila (Stack - LIFO):** Implementación de la lógica para deshacer la última nota ingresada en caso de error de tipeo[cite: 1].
- **Cola (Queue - FIFO):** Procesamiento ordenado de estudiantes aprobados para generar el reporte de certificados[cite: 1].
- **Persistencia de Datos:** Lectura y escritura automatizada en archivos (`alumnos.txt`, `profesores.txt`, `certificados_pendientes.txt`)[cite: 1].

---

## 🛠️ Tecnologías
- **Lenguajes:** Python, Java, C++[cite: 1]
- **Control de Versiones:** Git & GitHub[cite: 1]
- **Documentación:** UML & Markdown[cite: 1]

---

## 📂 Estructura del Repositorio
```text
.
├── /docs      # Documentación técnica, plan de acción y Diagrama de Clases UML[cite: 1]
├── /python    # Código fuente y archivos de persistencia en Python[cite: 1]
├── /java      # Implementación en Java orientada a objetos con Colecciones[cite: 1]
├── /cpp       # Implementación compilada en C++ con manejo manual de memoria[cite: 1]
└── README.md  # Portada y documentación del repositorio
