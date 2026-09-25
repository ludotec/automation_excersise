# Automation Exercise – Framework de automatización

Automatización E2E de [Automation Exercise](https://www.automationexercise.com/) con **Maven**, **Selenium WebDriver**, **JUnit 5**, **Cucumber** (Gherkin) y **Allure Report**.

Alcance de la suite: escenarios de **prioridad Alta**, excepto **FL03** (`@Manual`).

## Equipo

- Bladimir Zink
- Elias Schiel
- Claudio Fuentes
- Juan Espasandin
- Anabela Juarez
- Gabriel Pretel

## Contenido

- [Equipo](#equipo)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Ejecutar las pruebas](#ejecutar-las-pruebas)
- [Generar y ver el reporte Allure](#generar-y-ver-el-reporte-allure)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Qué no se versiona](#qué-no-se-versiona)
- [Notas](#notas)

## Requisitos

- **JDK 17**
- **Apache Maven 3.9+**
- **Google Chrome** (Selenium Manager descarga ChromeDriver)

Comprobar versiones:

```bash
java -version
mvn -version
```

## Instalación

```bash
git clone <URL_DEL_REPO>
cd automation_excersise
mvn -q dependency:resolve
```

`mvn test` también descarga las dependencias. No hace falta instalar Allure CLI: el plugin `allure-maven` genera el reporte.

## Ejecutar las pruebas

Suite principal (alimenta Allure):

```bash
mvn test -Dcucumber.filter.tags="@alta and not @Manual"
```

Resultado esperado: **14 escenarios passed** y **1 omitido** (FL03). Tarda unos 4–5 minutos.

Otras corridas:

```bash
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@login"
```

FL03 (registro durante el checkout) queda fuera de la suite por el tag `@Manual`.

## Generar y ver el reporte Allure

El HTML de Allure **no está en el repositorio**: se **regenera** en cada máquina.

`docs/entregables/allure-report/` es solo una copia local de `target/site/allure`. **No hay que pushearlo.** Quien clone el repo:

```bash
mvn test -Dcucumber.filter.tags="@alta and not @Manual"
mvn allure:report
```

| Qué | Dónde |
|---|---|
| Resultados crudos | `target/allure-results/` (`*-result.json`, environment, categories) |
| Reporte HTML | `target/site/allure/index.html` |

**Visualizar** (recomendado; el navegador suele bloquear `file://`):

```bash
mvn allure:serve
```

Se abre una URL local con Overview, Suites, Graphs y Environment. Detener: `Ctrl + C`.

### Screenshots

`src/test/java/hooks/Hooks.java` adjunta un PNG a Allure **solo si el escenario falla**.

- Suite en verde: **no hay screenshots** (esperado).
- Si falla: Allure → escenario → **Attachments** → `Evidencia de fallo - <nombre>`.

Cucumber también deja `target/cucumber-reports.html` (no reemplaza Allure).

## Estructura del proyecto

```
pom.xml                              Maven, Selenium, Cucumber, JUnit 5, Allure
src/main/java/pages/                 Page Objects (Page Factory)
src/main/java/utils/                 DriverFactory, ApiDataFactory
src/test/java/hooks/                 Before/After + screenshot Allure
src/test/java/runners/               TestRunner
src/test/java/steps/                 Step definitions
src/test/resources/features/         Escenarios Gherkin
src/test/resources/allure.properties
src/test/resources/allure/           environment.properties
src/test/resources/categories.json
```

Stack: Java 17 · Selenium 4.35.0 · Cucumber 7.14.0 · JUnit 5.11.4 · Allure adapter 2.29.1 · Allure CLI 2.29.0.

## Qué no se versiona

No subir al remoto (se generan o son locales):

- `target/`
- `docs/entregables/allure-report/`
- `.idea/`

## Notas

- El artefacto `allure-commandline:2.29.1` no está en Maven Central; el plugin usa CLI **2.29.0**.
- Puede aparecer un warning de CDP (Chrome vs Selenium 4.35.0). No impide la suite.
