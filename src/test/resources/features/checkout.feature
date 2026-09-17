# language: es
# - Prioridad Alta |
@alta @checkout
Característica: Checkout
  Como comprador
  Quiero revisar mi pedido antes de pagarlo
  Para confirmar la dirección y los productos

  @smoke @sesion
  Escenario: Checkout con sesión iniciada y pedido válido
    Dado que tengo una sesión iniciada y productos en el carrito
    Cuando avanzo al checkout
    Entonces veo la dirección de envío y facturación cargada
    Y el resumen muestra productos, cantidades y total
    Cuando agrego un comentario y continúo al pago
    Entonces se abre el formulario de pago

  @sin_sesion
  Escenario: Intentar acceder al checkout sin sesión iniciada
    Dado que tengo productos en el carrito y no inicié sesión
    Cuando intento avanzar al checkout
    Entonces se me ofrece registrarme o iniciar sesión

  @direccion
  Escenario: Verificar la dirección registrada en el checkout
    Dado que tengo una sesión iniciada y productos en el carrito
    Cuando avanzo al checkout
    Entonces la dirección de envío coincide con la dirección registrada
    Y la dirección de facturación coincide con la dirección registrada
