# order-management-v16

Dominio y puertos de aplicación para el flujo `order-management_v16`, siguiendo un estilo de arquitectura hexagonal.

## Estructura principal

- `src/main/java/com/example/ordermanagement/domain/model`: Entidades y Value Objects del dominio.
- `src/main/java/com/example/ordermanagement/application/ports/in`: Puertos de entrada (casos de uso).
- `src/main/java/com/example/ordermanagement/application/ports/out`: Puertos de salida (repositorios).

## Reglas de negocio implementadas (suposiciones)

Dado que no se encontró una definición de dominio local, se han aplicado las siguientes reglas de negocio mínimas y genéricas para gestión de órdenes:

- Una orden debe tener:
  - Identificador de orden (`UUID`) no nulo.
  - Identificador de cliente no nulo ni vacío.
  - Al menos un ítem.
- Un ítem de orden debe tener:
  - Identificador de producto no nulo ni vacío.
  - Cantidad mayor que cero.
  - Precio unitario no negativo.
- El total de la orden:
  - Se calcula como la suma de subtotales de los ítems.
  - Debe ser al menos 10 unidades monetarias.

Todas las invariantes se validan en constructores o métodos de fábrica, de forma que el modelo de dominio nunca pueda estar en un estado inválido.

## Ejecución de tests

Proyecto basado en Maven. Para ejecutar los tests:

```bash
mvn test
```

