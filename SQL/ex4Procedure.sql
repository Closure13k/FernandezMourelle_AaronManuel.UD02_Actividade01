-- Actualiza el stock de un producto. Generalmente llamado tras realizar una venta.
-- Comprobar previamente que la venta no supera el stock actual (lo normal sería con un trigger).
DELIMITER $$
CREATE PROCEDURE `ACTUALIZAR_STOCK` (IN idProducto INT, IN cantidad INT)
BEGIN
    UPDATE PRODUCTOS P
    SET P.STOCKACTUAL = P.STOCKACTUAL - cantidad
    WHERE P.ID = idProducto;
END$$
DELIMITER ;