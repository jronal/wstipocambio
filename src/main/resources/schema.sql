DROP TABLE IF EXISTS TIPO_CAMBIO;

CREATE TABLE TIPO_CAMBIO
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    moneda_origen  VARCHAR(3) NOT NULL,
    moneda_destino VARCHAR(3) NOT NULL,
    cotizacion     number DEFAULT NULL
);