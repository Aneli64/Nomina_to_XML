import org.w3c.dom.Document
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class Nomina_XML {
    companion object {
        //VARIABLES NECESARIAS PARA LA CONEXION DE LA BD
        private val statement = Conexion.connect.createStatement()

        /**
         * Metodo que guarda nuestro documento en base a un formato
         *@param doc Documento que guarda el xml
         * @param fileName Xml en el que se almacena nuestro contenido
         */
        private fun guardarDocumentoXML(doc: Document, fileName: String) {
            val transformerFactory = TransformerFactory.newInstance()
            val transformer: Transformer = transformerFactory.newTransformer()
            transformer.setOutputProperty("indent", "yes") // Habilitar la indentación
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2") // Espacios de indentación
            val source = DOMSource(doc)
            val result = StreamResult(File(fileName))
            transformer.transform(source, result)
            println("Archivo XML guardado como $fileName")
        }

        /**
         * Metodo que en base a una tabla, hace su select para convertir sus valores en una lista de Nomina()
         * @param nombre_tabla Tabla de referencia a la que haremos el select de su contenido
         */
        fun selectToNominaObject(nombre_tabla: String): MutableList<Nomina> {
            //Sentencia y variables necesarias para nuestro metodo
            val sentencia = "SELECT * FROM $nombre_tabla"
            val select = statement.executeQuery(sentencia)
            var nomina = Nomina()
            val listaNominas: MutableList<Nomina> = mutableListOf()

            /*Recorremos los registros del select y los vamos imprimiendo
            /almacenamos cada 7 valores para obtener cada objeto hasta el maximo de su size*/
            while (select.next()) {
                for (i in 1..select.metaData.columnCount step (7)) {
                    nomina.nombEmp = select.getString(i)
                    nomina.apeEmp = select.getString(i + 1)
                    nomina.nEmp = select.getString(i + 2).toInt()
                    nomina.salBase = select.getString(i + 3).toDouble()
                    nomina.hsTrab = select.getString(i + 4).toInt()
                    nomina.deducc = select.getString(i + 5).toDouble()
                    nomina.fechPag = select.getString(i + 6)
                }
                listaNominas.add(nomina) //añadimos a la lista antes de pasar a la siguiente nomina
                nomina = Nomina() //borramos la nomina una vez guardada, para almacenar la siguiente
            }
            return listaNominas
        }

        /**
         * Metodo que pasa introduce una lista de Nomina() dentro de nuestro XML
         * @param listaNominas Lista de nominas que introduciremos en el documento
         */
        fun objetosToXML(listaNominas: MutableList<Nomina>) {
            //Datos necesarios para crear nuestro File
            val xml = File("Nomina.xml")
            val instancia = DocumentBuilderFactory.newInstance()
            val constr = instancia.newDocumentBuilder()
            val doc = constr.parse(xml)

            //creación de etiquetas para su uso en nuestro XML
            val nombEmp = doc.createElement("nombEmp")
            val apeEmp = doc.createElement("apeEmp")
            val nEmp = doc.createElement("nEmp")
            val salBase = doc.createElement("salBase")
            val hsTrab = doc.createElement("hsTrab")
            val deducc = doc.createElement("deducc")
            val fechPag = doc.createElement("fechPag")

            //lista de atributos creados anteriormente, utilizados para iterar en ellos
            val listaAtrib = listOf(nombEmp, apeEmp, nEmp, salBase, hsTrab, deducc, fechPag)

            for (i in listaNominas.indices) {
                //Etiquetas de nuestro file que hacen referencia al objeto Modelo.Nomina()
                val nominaElm = doc.createElement("nomina")
                doc.documentElement.appendChild(nominaElm)

                //Bucle que itera en nuestros atributos y los crea y añade a su etiqueta nomina correspondiente
                for (j in listaAtrib.indices) {
                    val atributo = doc.createElement(listaAtrib[j].tagName)
                    nominaElm.appendChild(atributo)

                    //bucle que utilizaremos para asignar los textcontent, dependiento del tagName que recibamos
                    when (atributo.tagName) {
                        "nombEmp" -> atributo.textContent = listaNominas[i].nombEmp
                        "apeEmp" -> atributo.textContent = listaNominas[i].apeEmp
                        "nEmp" -> atributo.textContent = listaNominas[i].nEmp.toString()
                        "salBase" -> atributo.textContent = listaNominas[i].salBase.toString()
                        "hsTrab" -> atributo.textContent = listaNominas[i].hsTrab.toString()
                        "deducc" -> atributo.textContent = listaNominas[i].deducc.toString()
                        "fechPag" -> atributo.textContent = listaNominas[i].fechPag
                    }
                }
            }
            guardarDocumentoXML(doc, "Nomina.xml")
        }
    }
}