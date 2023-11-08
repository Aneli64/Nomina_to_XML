fun main(){
    val lista = Nomina_XML.selectToNominaObject("tb_nomina")
    Nomina_XML.objetosToXML(lista)
}