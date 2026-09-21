# language: es
# - Prioridad Alta |
@alta @e2e
Característica: Flujos de compra de punta a punta
  Como comprador
  Quiero completar una compra por distintos recorridos
  Para confirmar que el proceso comercial funciona de principio a fin

  @smoke @fl01
  Escenario: FL01 - Compra con registro previo
    Dado que soy un visitante nuevo en el home
    Cuando me registro como usuario
    Y inicio sesión con la cuenta creada
    Y busco un producto y lo agrego al carrito
    Y avanzo al checkout confirmando dirección y pedido
    Y pago con datos de tarjeta válidos
    Entonces veo "Your order has been placed successfully!"
    Y puedo descargar la factura del pedido

  @fl02
  Escenario: FL02 - Compra con login previo
    Dado que tengo una cuenta registrada y no inicié sesión
    Cuando inicio sesión con mis credenciales válidas
    Y agrego un producto al carrito
    Y agrego otro producto al carrito
    Y actualizo la cantidad del producto
    Y elimino un producto del carrito
    Y avanzo al checkout y completo el pago
    Entonces veo "Your order has been placed successfully!"

  @fl03
  Escenario: FL03 - Registro durante el checkout
    Dado que tengo un producto en el carrito y no inicié sesión
    Cuando avanzo al checkout
    Y selecciono la opción Register / Login
    Y completo el registro con datos válidos
    Y vuelvo al checkout y completo el pago
    Entonces veo "Your order has been placed successfully!"

  @fl04
  Escenario: FL04 - Búsqueda y compra
    Dado que estoy en la página de Products y no inicié sesión
    Cuando busco un producto por su nombre
    Y agrego un resultado al carrito
    Y avanzo al checkout
    Y inicio sesión con credenciales válidas
    Y completo el pago
    Entonces veo "Your order has been placed successfully!"
