# Análisis final del proceso de pruebas

**Producto:** [Automation Exercise](https://www.automationexercise.com/)  
**Proyecto:** `automation_excersise`  
**Comando ejecutado:** `mvn test -Dcucumber.filter.tags="@alta and not @Manual"`  
**Fuente de los números:** reporte Allure de esa corrida (`widgets/summary.json`)

---

## 1. Veredicto

La suite automatizada de prioridad Alta **pasó completa**: 14 escenarios ejecutados, 14 en verde, ninguno fallido ni roto, en poco más de cuatro minutos.

Ese verde tiene un límite que conviene decir en la misma frase: **no cubre el registro iniciado desde el checkout** (FL03). Ese flujo quedó fuera de la suite porque el sitio no reasigna de forma confiable el carrito anónimo al usuario recién creado. Es el riesgo abierto más relevante del ciclo.

| Indicador | Valor |
|---|---|
| Escenarios ejecutados | 14 |
| Passed | 14 (100 %) |
| Failed / Broken | 0 |
| Excluidos a propósito | 1 (FL03, tag `@Manual`) |
| Duración total | 256,7 s (~4 min 17 s) |
| Escenario más lento | FL02 – Compra con login previo (48,8 s) |
| Escenario más rápido | Checkout sin sesión iniciada (4,4 s) |

**Calificación de calidad:** el alcance automatizado se ve sólido; la calidad global del producto es **aceptable con reserva**, por FL03 y por la interferencia de la publicidad de terceros.

---

## 2. Dónde se responde cada historia

| Historia | Sección de este documento |
|---|---|
| HU32 – Analizar resultados automatizados | [4. Resultados por área funcional](#4-resultados-por-área-funcional) y [5. Lo que quedó fuera](#5-lo-que-quedó-fuera-fl03) |
| HU33 – Contabilizar escenarios ejecutados | [6. Métricas de la ejecución](#6-métricas-de-la-ejecución) |
| HU34 – Consolidar resultados | [1. Veredicto](#1-veredicto) y [6. Métricas de la ejecución](#6-métricas-de-la-ejecución) |
| HU35 – Identificar principales hallazgos | [7. Hallazgos](#7-hallazgos) |
| HU36 – Elaborar conclusiones | [8. Conclusiones](#8-conclusiones) |
| HU39 – Entregar el análisis final | Este documento completo, más [9. Reproducir la evidencia](#9-reproducir-la-evidencia) |

---

## 3. Contexto de la corrida

Se ejecutó el subconjunto de prioridad Alta excluyendo los escenarios marcados `@Manual`. El catálogo Gherkin de Alta tiene **15** escenarios; la suite automatizada corre **14**.

Por eso Maven y Allure muestran números distintos y conviene anticiparlo: Surefire informa `Tests run: 15, Skipped: 1`, mientras que Allure informa 14. No es una inconsistencia del reporte. Cuando Cucumber filtra con `not @Manual`, el escenario descartado **nunca llega al adapter de Allure**, así que no aparece siquiera como *skipped*.

Ambiente registrado en la pestaña Environment del reporte:

| Componente | Versión |
|---|---|
| Java | 17 |
| Selenium | 4.35.0 |
| Cucumber | 7.14.0 |
| JUnit | 5.11.4 |
| Allure (adapter de resultados) | 2.29.1 |
| Allure (CLI que arma el HTML) | 2.29.0 |

Durante la ejecución Chrome 153 emitió un warning de compatibilidad CDP con Selenium 4.35.0. No bajó ningún test, pero queda asentado para que nadie lo confunda con un error al leer el log.

---

## 4. Resultados por área funcional

Los catorce escenarios verdes se agrupan mejor por área que por orden de ejecución: así se ve qué parte del producto quedó respaldada y cuánto costó verificarla.

### Autenticación — 3 escenarios, 47,7 s

| Escenario | Duración |
|---|---|
| Login con credenciales válidas | 12,1 s |
| Login con credenciales inválidas | 18,4 s |
| Logout de un usuario autenticado | 17,2 s |

El login inválido tardó **más** que el válido. No es un síntoma de lentitud del producto: al mostrar el mensaje de error la página vuelve a asentarse con los anuncios cargando, y Selenium espera ese estado antes de validar. Incluir el caso negativo fue deliberado, porque comprobar solo el acceso correcto dejaría sin verificar que el sitio efectivamente **rechaza** credenciales equivocadas.

### Carrito — 3 escenarios, 16,9 s

| Escenario | Duración |
|---|---|
| Agregar un producto al carrito | 6,5 s |
| Actualizar la cantidad de un producto | 5,4 s |
| Eliminar un producto del carrito | 5,0 s |

Es el bloque más barato de toda la suite: son operaciones de una sola pantalla, sin registro ni pago. Que estén en verde confirma que el HTML actual del carrito responde de forma estable, incluido el control de cantidad, que no es el `input` que uno esperaría sino un `button`.

### Checkout — 3 escenarios, 36,1 s

| Escenario | Duración |
|---|---|
| Checkout con sesión iniciada y pedido válido | 17,6 s |
| Verificar la dirección registrada en el checkout | 14,1 s |
| Intentar acceder al checkout sin sesión iniciada | 4,4 s |

Los dos primeros cubren el camino feliz autenticado: avanzar con el pedido y comprobar que la dirección mostrada coincide con la registrada. El tercero, el más rápido de la suite, verifica lo contrario: que el producto **frene** al usuario anónimo y le ofrezca registrarse o iniciar sesión. Sin ese caso, la cobertura del checkout quedaría sesgada al camino cómodo.

### Pago — 2 escenarios, 33,0 s

| Escenario | Duración |
|---|---|
| Pago exitoso | 16,1 s |
| Pago con datos incompletos | 16,9 s |

El primero confirma el pedido y la descarga de la factura. El segundo comprueba que el formulario no se envía sin el número de tarjeta y que se pide completar el campo obligatorio. Es la misma lógica que en autenticación: éxito y rechazo, no solo éxito.

### Flujos end to end — 3 escenarios, 122,6 s

| Escenario | Duración |
|---|---|
| FL01 – Compra con registro previo | 36,1 s |
| FL02 – Compra con login previo | 48,8 s |
| FL04 – Búsqueda y compra | 37,7 s |

Tres escenarios de catorce consumen casi la mitad del tiempo total de la suite, y es razonable: cada uno recorre varias pantallas de punta a punta.

FL02 es el más largo porque concentra login, dos productos, actualización de cantidad, eliminación y pago en un solo viaje; si ese pasa, el núcleo comercial completo quedó ejercitado. FL01 valida el recorrido de quien se da de alta antes de comprar. FL04 representa al visitante que explora primero y recién se identifica al pagar.

---

## 5. Lo que quedó fuera: FL03

**FL03 – Registro durante el checkout** no se ejecutó en esta corrida.

Al automatizarlo se detectó que, tras registrarse desde el modal de checkout, el carrito armado como usuario anónimo no se reasigna de forma confiable a la cuenta nueva. A eso se suma que la página `/login` carga iframes de AdSense que demoran la finalización del DOM y hacen que Selenium no encuentre los campos dentro de tiempos razonables.

Se decidió marcarlo `@Manual` en lugar de dejarlo fallando. El criterio fue el siguiente: un rojo permanente por un defecto ya conocido no agrega información nueva en cada corrida, pero sí contamina la lectura del resto de la suite y termina normalizando el color rojo en el reporte.

La contrapartida honesta es que **FL03 no está verificado automáticamente**. Se trata como riesgo de producto abierto, no como deuda del framework.

---

## 6. Métricas de la ejecución

### Dimensión de lo ejecutado

| Concepto | Cantidad | Qué representa |
|---|---|---|
| Escenarios de prioridad Alta diseñados | 15 | Alcance de negocio definido en la planificación |
| Ejecutados por Selenium / Allure | 14 | Lo que un revisor puede reproducir con el comando de la suite |
| Excluidos por tag `@Manual` | 1 | FL03: cuenta en el alcance, no en la métrica de automatización |
| Fallidos | 0 | — |
| Duración según Allure | 256,7 s | Suma de los tests del reporte |
| Duración según Maven | 257,2 s | Incluye el arranque de Surefire |

Si se pide un número para **dimensionar la ejecución**, el honesto es **14**. Si se pide **cobertura del alcance Alta**, es **14 de 15**, con FL03 afuera por decisión documentada y no por omisión.

### Distribución por tag

| Tag | Escenarios | Estado |
|---|---|---|
| `@agregar`, `@actualizar`, `@eliminar` | 1 cada uno | Passed |
| `@sesion`, `@sin_sesion`, `@direccion` | 1 cada uno | Passed |
| `@login` | 2 | Passed |
| `@logout` | 1 | Passed |
| `@fl01`, `@fl02`, `@fl04` | 1 cada uno | Passed |
| `@fl03` | 1 | No ejecutado |
| `@exitoso`, `@validacion` | 1 cada uno | Passed |
| `@smoke` | 5 | Passed |
| `@alta` | 15 | 14 passed + 1 manual |

El grupo `@smoke` (agregar al carrito, checkout con sesión, FL01, login válido y pago exitoso) sirve como verificación rápida cuando no se dispone de los cuatro minutos de la suite completa.

### Sobre la ausencia de capturas

El reporte no incluye ninguna imagen. Es el comportamiento esperado: el hook adjunta un PNG únicamente cuando `scenario.isFailed()`. En una corrida sin fallos no hay evidencia de error que mostrar, y la evidencia de éxito son los pasos verdes con sus tiempos.

---

## 7. Hallazgos

Estos puntos son los que evitan que el verde del reporte se lea de más.

**La cuenta demo compartida no sostiene una suite.** El usuario `demo@correo.com` se reutilizaba entre testers y dejaba el carrito con ítems y cantidades de corridas anteriores. Se reemplazó por cuentas temporales creadas vía `ApiDataFactory`, con email único por ejecución. El objetivo de las pruebas es el producto, no el estado acumulado de una cuenta compartida, y un rojo por datos sucios habría sido un falso positivo.

**La publicidad de terceros interfiere con la automatización.** Los iframes de AdSense retrasan el evento `load`. Sin `PageLoadStrategy.EAGER`, sin esperar `document.readyState` y sin limpiar esos iframes antes de los clicks sensibles, los tests fallan reportando que no encuentran un campo que en realidad ya está en el HTML. Es un hallazgo de entorno del sitio, no de una regla de negocio incumplida, y golpea sobre todo a `/login`.

**El registro desde el checkout es un problema de producto.** Es el hallazgo funcional más grave del ciclo, descrito en la sección 5. Afecta un caso de uso muy real: agregar productos primero y crear la cuenta después.

**El HTML real difiere de lo que sugiere la documentación pública.** En el carrito la cantidad se maneja con `td.cart_quantity button` y la actualización pasa por el detalle del producto. Los Page Objects se ajustaron a esa realidad. Quien automatice copiando selectores genéricos va a interpretar fallos de localización como caídas del sitio.

**Cucumber no tolera dos steps con la misma frase.** Dos métodos anotados con `@Y("agrego un producto al carrito")` se pisan entre sí: uno gana el binding y el otro nunca se ejecuta. El step de búsqueda se renombró con una frase distinta. Es un aprendizaje de diseño de la suite, importante al sumar escenarios de prioridad Media y Baja.

**Hay un desfasaje de versiones en Allure.** Los resultados los escribe el adapter 2.29.1, pero el HTML lo genera el CLI 2.29.0, porque Maven Central no publica el artefacto `allure-commandline:zip:2.29.1`. No afecta el conteo ni el contenido de los escenarios; sí explica por qué forzar 2.29.1 en `reportVersion` rompe la generación.

---

## 8. Conclusiones

Conviene separar dos cosas que el reporte muestra juntas en la misma pantalla verde: la calidad del producto y la calidad de la automatización.

**Sobre el producto.** En los caminos de prioridad Alta que sí se ejecutaron, Automation Exercise se comportó según lo esperado: una persona puede registrarse antes de comprar, iniciar y cerrar sesión, armar su pedido, pagar y descargar la factura. Los casos negativos también responden: credenciales incorrectas se rechazan con mensaje y el pago no avanza sin número de tarjeta.

La reserva es concreta y no se diluye con el 100 % de verdes: **no se afirma calidad plena del checkout anónimo**. Registrarse cuando ya hay productos en el carrito es un uso habitual y hoy es el punto débil del sitio. A eso se suma la publicidad de terceros, que no es funcionalidad del producto pero degrada la experiencia y la automatización.

**Sobre el framework.** La combinación de Maven, JUnit 5, Cucumber, Page Object Model, Page Factory y Allure alcanzó para ejecutar, filtrar por tags, reportar y explicar los resultados. Las cuentas temporales y las esperas defensivas no son adornos: son la razón por la que esos catorce verdes se pueden repetir en otra máquina.

**Recomendaciones para el próximo ciclo:**

1. Ejecutar FL03 manualmente antes de cada entrega y registrar el resultado, ya que hoy es el único flujo Alta sin cobertura automática.
2. Confirmar el comportamiento de las cookies de carrito al registrarse antes de intentar reactivar FL03 en la suite.
3. Evaluar un bloqueador de anuncios en el perfil de Chrome de pruebas si se decide reincorporar los flujos afectados por AdSense.
4. Extender la suite a prioridades Media y Baja recién después de resolver el punto 2, para no ampliar cobertura sobre una base con un flujo crítico abierto.

**En una línea:** la suite Alta automatizable pasó completa en poco más de cuatro minutos; el producto se ve sólido en ese alcance y mantiene un riesgo abierto en el registro durante el checkout.

---

## 9. Reproducir la evidencia

El HTML de Allure no se versiona: se regenera en cada máquina a partir de los resultados de la corrida.

```bash
mvn test -Dcucumber.filter.tags="@alta and not @Manual"
mvn allure:report
mvn allure:serve
```

Los resultados crudos quedan en `target/allure-results/` y el reporte en `target/site/allure/index.html`. Si el navegador bloquea la carga de archivos locales, `mvn allure:serve` levanta el reporte en un servidor temporal.

Quien reproduzca la corrida debería obtener los mismos catorce escenarios en verde, salvo que el sitio de práctica haya cambiado su comportamiento o disponibilidad.
