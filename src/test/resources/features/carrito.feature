# language: es
# - Prioridad Alta | 
@alta @carrito
Característica: Gestión del carrito
  Como comprador
  Quiero administrar los productos de mi carrito
  Para comprar exactamente lo que necesito

  @smoke @agregar
  Escenario: Agregar un producto al carrito
    Dado que estoy viendo un producto disponible
    Cuando agrego el producto al carrito
    Entonces el carrito muestra el producto con cantidad 1
    Y el total se corresponde con su precio

  @actualizar
  Escenario: Actualizar la cantidad de un producto
    Dado que tengo un producto en el carrito
    Cuando cambio su cantidad a 3
    Entonces el carrito muestra la cantidad 3
    Y el total se recalcula con la nueva cantidad

  @eliminar
  Escenario: Eliminar un producto del carrito
    Dado que tengo un producto en el carrito
    Cuando elimino el producto
    Entonces el producto deja de mostrarse en el carrito
    Y el total se actualiza correctamente
