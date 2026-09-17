# language: es
# - Prioridad Alta |
@alta @pago
Característica: Pago
  Como comprador en el checkout
  Quiero pagar mi pedido con tarjeta
  Para completar la compra

  @smoke @exitoso
  Escenario: Pago exitoso
    Dado que estoy en el paso de pago con un pedido armado
    Cuando completo Name on Card, Card Number, CVC y Expiration con datos válidos
    Y confirmo el pago
    Entonces veo "Your order has been placed successfully!"
    Y puedo descargar la factura del pedido

  @validacion
  Escenario: Pago con datos incompletos
    Dado que estoy en el paso de pago
    Cuando intento confirmar sin completar Card Number
    Entonces el formulario no se envía
    Y se me solicita completar el campo obligatorio
