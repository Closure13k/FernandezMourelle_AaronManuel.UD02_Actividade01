-- Recoge el stock de un producto.
-- En caso de no existir el producto, devolverá NULL.
DELIMITER $$
CREATE FUNCTION `getStockProducto` (idProducto INT)
RETURNS INTEGER
READS SQL DATA
BEGIN
    RETURN (
        SELECT P.STOCKACTUAL
        FROM PRODUCTOS AS P
        WHERE P.ID = idProducto
    );
END$$
DELIMITER ;