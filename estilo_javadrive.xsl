<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <head>
                <title>JavaDrive</title>
                <style>
                    /* Estilo General Dark Mode */
                    body {
                    font-family: 'Segoe UI', Roboto, sans-serif;
                    margin: 0;
                    padding: 40px;
                    background-color: #121212; /* Fondo casi negro */
                    color: #e0e0e0;
                    }
                    
                    h1 {
                    text-align: center;
                    text-transform: uppercase;
                    letter-spacing: 3px;
                    color: #bb86fc; /* Violeta neón */
                    margin-bottom: 50px;
                    }
                    
                    /* Caja de Empresa tipo Tarjeta Flotante */
                    .info-empresa {
                    background: linear-gradient(145deg, #1e1e1e, #252525);
                    padding: 20px;
                    border-left: 5px solid #bb86fc;
                    border-radius: 12px;
                    margin-bottom: 40px;
                    box-shadow: 0 10px 20px rgba(0,0,0,0.5);
                    font-size: 1.1em;
                    }
                    
                    h2 {
                    font-size: 1.5em;
                    border-bottom: 1px solid #333;
                    padding-bottom: 10px;
                    color: #03dac6; /* Turquesa */
                    }
                    
                    /* Tabla Estilo Futurista */
                    table {
                    width: 100%;
                    border-collapse: separate;
                    border-spacing: 0 10px; /* Espacio entre filas */
                    margin-bottom: 40px;
                    }
                    
                    th {
                    text-transform: uppercase;
                    font-size: 0.8em;
                    letter-spacing: 1px;
                    color: #888;
                    padding: 15px;
                    text-align: left;
                    }
                    
                    td {
                    background-color: #1e1e1e;
                    padding: 20px;
                    color: #cfcfcf;
                    }
                    
                    /* Bordes redondeados para las filas */
                    td:first-child { border-radius: 10px 0 0 10px; }
                    td:last-child { border-radius: 0 10px 10px 0; }
                    
                    tr:hover td {
                    background-color: #2c2c2c;
                    color: #fff;
                    transition: 0.3s;
                    }
                    
                    /* Etiquetas dinámicas con estilo Glow */
                    .badge {
                    padding: 6px 12px;
                    border-radius: 20px;
                    font-size: 0.75em;
                    font-weight: bold;
                    text-transform: uppercase;
                    }
                    
                    .coche {
                    border: 1px solid #03dac6;
                    color: #03dac6;
                    box-shadow: 0 0 5px rgba(3, 218, 198, 0.2);
                    }
                    
                    .furgoneta {
                    border: 1px solid #ffb74d;
                    color: #ffb74d;
                    box-shadow: 0 0 5px rgba(255, 183, 77, 0.2);
                    }
                    
                    .matricula-text {
                    font-family: 'Courier New', monospace;
                    background: #333;
                    padding: 2px 6px;
                    border-radius: 4px;
                    color: #fff;
                    }
                </style>
            </head>
            <body>
                <h1>Sistema de Gestión JavaDrive</h1>
                
                <div class="info-empresa">
                    <span>🏢 <strong>Empresa:</strong> <xsl:value-of select="javadrive/empresa/nombre"/></span>
                    <span style="margin-left: 30px;">📍 <strong>Ubicación:</strong> <xsl:value-of select="javadrive/empresa/ubicacion"/></span>
                </div>
                
                <h2><span style="margin-right:10px;">🚗</span> Inventario de Flota</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Categoría</th>
                            <th>ID Matrícula</th>
                            <th>Vehículo</th>
                            <th>Ficha Técnica</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="javadrive/flota/vehiculo">
                            <tr>
                                <td>
                                    <span class="badge">
                                        <xsl:attribute name="class">
                                            <xsl:choose>
                                                <xsl:when test="@tipo='Coche'">badge coche</xsl:when>
                                                <xsl:when test="@tipo='Furgoneta'">badge furgoneta</xsl:when>
                                                <xsl:otherwise>badge</xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:attribute>
                                        <xsl:value-of select="@tipo"/>
                                    </span>
                                </td>
                                <td><span class="matricula-text"><xsl:value-of select="matricula"/></span></td>
                                <td><strong><xsl:value-of select="marca"/></strong><xsl:text> </xsl:text><xsl:value-of select="modelo"/></td>
                                <td><xsl:value-of select="especifico"/></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
                
                <h2><span style="margin-right:10px;">👥</span> Listado de Clientes</h2>
                <table>
                    <thead>
                        <tr>
                            <th> DNI</th>
                            <th>Nombre y Apellidos</th>
                            <th>Contacto Directo</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="javadrive/clientes/cliente">
                            <tr>
                                <td><xsl:value-of select="dni"/></td>
                                <td><strong><xsl:value-of select="nombre"/></strong></td>
                                <td style="color: #bb86fc;"><xsl:value-of select="telefono"/></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>