# El carrito que desapareció: cómo el testing nos permite avanzar con confianza

Guion del pitch, slide por slide. Duración estimada: 12–14 minutos.
Dentro de la presentación, la tecla **N** muestra este mismo texto como notas del orador.

Los números del ciclo manual son ilustrativos. Los de automatización salen del [reporte Allure publicado](https://ludotec.github.io/automation_excersise/). Los datos generales de la industria citan su fuente en la slide.

---

## 1 · Portada

Hoy les vamos a contar una historia sobre un carrito de compras que desapareció, y por qué esa historia explica, mejor que cualquier definición, para qué sirve el testing de software.

## 2 · La historia

Imaginen que una persona entra a una tienda online. Busca un producto, lo agrega al carrito y llega al checkout.

El sistema le pide crear una cuenta. Se registra correctamente y vuelve a la compra. Pero el carrito está vacío.

Esa persona no abre un reclamo. No llama a soporte. Simplemente cierra la página y se va.

Nosotros detectamos ese problema porque escribimos una prueba, no porque alguien se quejó. Y ese es el punto de esta presentación: el testing no solo busca errores; descubre riesgos antes de que los descubra el usuario.

## 3 · Qué hicimos

Para este trabajo analizamos Automation Exercise, una tienda online de práctica, como si fuera nuestro propio negocio.

Primero la recorrimos como cualquier comprador. Después listamos qué podía salir mal: encontramos **21 riesgos**. Con eso escribimos los casos de prueba, los ejecutamos a mano, automatizamos los más importantes y reportamos todo lo que encontramos: **10 defectos**.

¿Por qué tanto esfuerzo en una tienda? Porque en un e-commerce el producto es la compra. Si la compra falla, no hay negocio.

## 4 · Ocho formas de perder una venta

Esto no es un problema de esta tienda en particular. Cualquier e-commerce tiene estos riesgos básicos: que el pago falle o cobre dos veces, que el carrito pierda productos, que los precios o totales estén mal, que se expongan datos personales, que el sitio se caiga en un Hot Sale, que los formularios acepten cualquier cosa, que no funcione en el celular, o que se venda algo sin stock.

Los que están marcados en rojo son los que encontramos en nuestra tienda: **6 de 8**. Y todos tienen algo en común: el usuario no avisa. Se va.

## 5 · El costo de no probar

¿Y cuánto cuesta no probar? Dos datos.

El primero: un error encontrado cuando el sistema ya está en manos de los usuarios cuesta mucho más que uno encontrado a tiempo. Según una referencia clásica de la industria, **hasta cien veces más** que en la etapa de diseño.

El segundo: aun cuando todo funciona, **siete de cada diez carritos** en internet se abandonan. Los usuarios ya tienen muchos motivos para irse. Un error en el checkout es regalarles uno más.

## 6 · ¿El usuario puede terminar su compra?

Con todo esto, nos hicimos una sola pregunta que guió todo el trabajo: **¿el usuario puede terminar su compra?**

Para responderla hay que mirar en tres niveles. ¿Funciona cada pieza por separado, como un botón o un cálculo? ¿Las piezas se entienden entre sí, por ejemplo el carrito con la cuenta del usuario? Y la más importante: ¿la persona completa la compra de principio a fin?

En nuestro caso, cada paso por separado funcionaba. Pero el recorrido completo se rompía justo en el registro. Que cada pieza funcione no garantiza que la compra funcione.

## 7 · El caso real

Durante las pruebas, el registro se solía probar primero: me registro, elijo un producto y pago. Así, todo funcionaba.

Pero la gente real no compra así. Primero elige, va a pagar, y recién ahí el sitio le pide registrarse. En ese orden, el carrito llegaba vacío.

Mismo sitio, mismos pasos, otro orden. Un flujo puede funcionar en una demo y aun así fallar en una combinación real de acciones del usuario.

## 8 · Primero, lo que rompe la venta

No se puede probar todo, así que hay que elegir. De los 21 riesgos, **10 eran altos o muy altos (48 %)**, y casi todos estaban en el camino de la compra.

Por eso definimos tres prioridades. Primero, todo lo que rompe la venta: ingresar, carrito, pago y la compra completa. Después, lo necesario pero que tiene un rodeo, como el registro, la búsqueda o el contacto. Y al final, lo decorativo.

## 9 · Una prueba es una promesa verificable

Un caso de prueba es una promesa que se puede verificar. Lo escribimos en lenguaje simple: si estoy pagando y dejo vacío el número de tarjeta, la compra no debe avanzar. Lo probamos y se cumple.

Otra promesa: si pido menos cinco unidades, el carrito debe rechazarlo. Lo probamos y no se cumple: el sitio lo acepta.

Escribimos **26 escenarios** así, concentrados en la compra. Como están en lenguaje simple, cualquiera puede leer qué se prometió y si se cumplió.

## 10 · Lo que encontramos a mano

Diseñamos **52 casos** y ejecutamos **50 (96 %)**. El **78 %** se aprobó, el **18 %** falló y un **4 %** quedó bloqueado porque el sitio se cayó.

Registramos **10 defectos, 7 graves**: el carrito acepta cantidades negativas, la tarjeta acepta letras y hasta se puede generar una factura por $0 con el carrito vacío.

Nueve de esos diez defectos aparecieron probando como una persona real.

## 11 · 14 compras de prueba, en 3 minutos

Además de probar a mano, automatizamos las 14 pruebas más importantes: un programa que recorre la tienda como un comprador, solo, sin que nadie tenga que hacer clic.

Según el reporte, las 14 pasaron: **100 %, cero fallas**, en **3 minutos 17 segundos**. Las compras completas son las que más tardan, el **54 % del tiempo**, pero son las únicas capaces de ver problemas como el del carrito.

Y se ejecutan solas cada vez que alguien cambia algo. Dejan de depender de que alguien se acuerde de probar.

## 12 · Uno descubre, el otro vigila

Repetir a mano las 14 pruebas más importantes lleva unas **2 horas 20 minutos**. La automatización lo hace en 3:17: **unas 43 veces más rápido**, sin cansancio y sin saltearse pasos.

Pero la velocidad no es el punto. La prueba manual encontró 9 de los 10 defectos: es la que explora. La automática encontró uno, el más caro, porque repite exactamente el camino del comprador en cada cambio. No compiten: se complementan.

## 13 · El verde es completo

Las 14 pruebas automáticas pasan. Todas. En 3 minutos 17 segundos, solas, cada vez que algo cambia.

Eso significa que un comprador puede buscar, elegir, pagar y recibir la confirmación. Y que si alguien rompe ese camino, el reporte deja de estar en verde en minutos.

Avanzamos con evidencia, no con la memoria de quien probó.

## 14 · Lo que nos llevamos

Volvamos a la persona del principio. Nunca nos iba a avisar.

Nos llevamos tres ideas: probar según el riesgo, empezando por lo que rompe la venta; combinar prueba manual para descubrir y automática para vigilar; y repetir el verde en cada cambio, para que la confianza no dependa de que alguien se acuerde de probar.

El testing no nos frena. Nos permite avanzar con confianza.

## 15 · Gracias

Gracias. ¿Preguntas?
